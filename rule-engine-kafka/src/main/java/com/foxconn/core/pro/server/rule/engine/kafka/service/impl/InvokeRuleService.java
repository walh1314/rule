/**
 * 
 */
package com.foxconn.core.pro.server.rule.engine.kafka.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.analysis.ConditionAnalysis;
import com.foxconn.core.pro.server.rule.engine.core.analysis.FieldAnalysis;
import com.foxconn.core.pro.server.rule.engine.core.analysis.TopicAnalysis;
import com.foxconn.core.pro.server.rule.engine.core.common.util.RedisUtil;
import com.foxconn.core.pro.server.rule.engine.core.entity.Rule;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.MqttData;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.PayloadBean;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.clound.FrontService;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.common.service.CoreproCommonService;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.config.ServerParamConfig;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.ConfigBean;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.DataBean;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.NoticeRedisBean;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.UserBean;
import com.foxconn.core.pro.server.rule.engine.kafka.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.kafka.producer.RabbitmqProducer;
import com.foxconn.core.pro.server.rule.engine.kafka.service.IInvokeRule;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lxy
 *
 */
@Service
@Slf4j
public class InvokeRuleService implements IInvokeRule {
	
	@Autowired
    private HashOperations<String, String, Object> hashOperations;

	@Resource
	private FrontService frontService;

	@Resource
	private ConditionAnalysis conditionAnalysis;
	
	@Resource
	private FieldAnalysis fieldAnalysis;

	@Resource
	private RabbitmqProducer rabbitmqProducer;

	@Resource
	private ServerParamConfig serverParamConfig;
	
	@Autowired
	private CoreproCommonService coreproCommonService;


	@Override
	public void invoke(MqttData data) {
		TopicAnalysis topicAnalysis = new TopicAnalysis();

		log.info("-----------getProductkey-----" + data.getPayload().getProductkey());
		UserBean userBean = this.getUserByProductKey(data.getPayload().getProductkey(),data.getPayload().getDeviceName());; // 根据topic查找 ，userID
		
		if (userBean == null) {
			log.warn("rule engine is illege:" + JSONObject.toJSONString(data));
			return;
		}
		log.info("-----------userBean-----" + JSONObject.toJSONString(userBean));
		
		// TODO 测试用例
		List<Rule> rules = getRuleListByUser(userBean, data);// 根据userId获取规则
		if(null == rules || rules.isEmpty()) {
			log.warn("user's rule is empty , user:" + JSONObject.toJSONString(userBean));
			return ;
		}
		
		log.debug("-----------rulesList-----" + JSONObject.toJSONString(rules));
		// rules = getTestList();

		List<Rule> rulesTopicMatch = null;
		List<Rule> rulesDataMatch = null;
		try {
			rulesTopicMatch = topicAnalysis.getMatchRuleList(data.getTopic(), rules);
			if(null == rulesTopicMatch || rulesTopicMatch.isEmpty()) {
				log.warn("topic's rule is empty" + data.getTopic() );
				return ;
			}

			log.info("-----------rulesTopicMatchrulesList-----" +JSONObject.toJSONString(rulesTopicMatch));
			// 如果匹配成功,则查看条件是否符合
			Map<String, Object> context = null;
			
			rulesDataMatch = new ArrayList<>(rulesTopicMatch.size() / 2);
			context = new HashMap<>(2);
			context.put(CommonConstant.TOPIC, data.getTopic());
			context.put(CommonConstant.ROOT, data.getPayload().getParams());// 根据协议去解析
			conditionAnalysis.setData(data);
			conditionAnalysis.setRules(rulesTopicMatch);
			rulesDataMatch = conditionAnalysis.getMatchRuleList(context);
			
			if(null == rulesDataMatch || rulesDataMatch.isEmpty()) {
				log.warn("topic has not match!" + data.getTopic());
				return ; 
			}
			
			log.info("------------topic rule result-----" + JSONObject.toJSONString(rulesDataMatch));
			
			// 如果有匹配成功的，则继续处理,进行数据筛选,数据筛选采用模板引擎处理
			String condition = null;
			JSONObject message = null;
			JSONObject dataAction = null;

			MqttData fieldData = null;
			PayloadBean payloadBean = null;
			JSONObject filedParams = null;
			
			JSONObject systemParams = null;
			Rule rule = null;
			
			for (int i = 0; i < rulesDataMatch.size(); i++)
			{
				fieldData = new MqttData();
				payloadBean = new PayloadBean();
				rule = rulesDataMatch.get(i);
				if (data.getPayload().getParams() instanceof JSONObject)
				{
					filedParams = (JSONObject) data.getPayload().getParams();
					payloadBean.setParams(filedParams);

					payloadBean.setDeviceName(data.getPayload().getDeviceName());
					payloadBean.setProductkey(data.getPayload().getProductkey());
					payloadBean.setTimestamp(data.getPayload().getTimestamp());

					fieldData.setPayload(payloadBean);

					fieldData.setId(data.getId());
					fieldData.setAccessMode(data.getAccessMode());

					fieldData.setTimestamp(data.getTimestamp());
					fieldData.setTopic(data.getTopic());
					fieldData.setType(data.getType());

					condition = rule.getField();
					
					systemParams = new JSONObject();
					systemParams.put(CommonConstant.TOPIC, data.getTopic());
					systemParams.put(CommonConstant.TIMES_TAMP, data.getPayload().getTimestamp());
					systemParams.put(CommonConstant.PRODUCT_KEY, data.getPayload().getProductkey());
					systemParams.put(CommonConstant.DEVICE_NAME1, data.getPayload().getDeviceName());
					systemParams.put(CommonConstant.TYPE, data.getType());
					systemParams.put(CommonConstant.ACCESS_MODE, data.getAccessMode());
					
					context = new HashMap<>(2);
					context.put(CommonConstant.SYSTEM_PARAMS, systemParams);
					context.put(CommonConstant.ROOT, filedParams);
					dataAction = fieldAnalysis.excute(fieldData, context, condition);
					log.info("------------dataAction result-----" + i + "-------" + dataAction != null
							? JSONObject.toJSONString(dataAction) : "");
					if (dataAction != null)
					{
						message = new JSONObject();
						message.put(CommonConstant.DATA, dataAction);
						// messsageid
						message.put(CommonConstant.ID, data.getId());
						message.put(CommonConstant.DEVICE_OWNER, userBean.getDeviceOwner());
						message.put(CommonConstant.RULE_ID, rule.getId());
						message.put(CommonConstant.DB_NAME, userBean.getDb());
						message.put(CommonConstant.DATA_ID, data.getDataId());
						message.put(CommonConstant.TYPE, data.getType());
						message.put(CommonConstant.PRODUCT_ID, data.getPayload().getProductkey());
						message.put(CommonConstant.DEVICE_NAME1, data.getPayload().getDeviceName());
						message.put(CommonConstant.TIMES_TAMP, data.getPayload().getTimestamp());
						if (data.getVersion() != null && 0 == data.getVersion().intValue()) //添加序号
						{
							message.put(CommonConstant.SQUE, i);
						}
						rabbitmqProducer.publish(message.toJSONString());
					}
				}
			}
			
			log.info("----------- rule excute end-----");
		} catch (Exception e) {
			log.error("Match exception:", e);
		}

	}

	private List<Rule> getRuleListByUser(UserBean bean, MqttData mqttData)
	{
		if (bean == null || ((bean.getDb() == null || StringUtils.isBlank(bean.getDb()))
				&& (bean.getUserId() == null || StringUtils.isBlank(bean.getUserId()))
				&& (bean.getDeviceOwner() == null || StringUtils.isBlank(bean.getDeviceOwner()))))
		{
			return null;
		}
		String redisKey = RedisUtil.getRuleKey(bean.getDb(),bean.getDeviceOwner());
		Set<String> ruleSet = hashOperations.keys(redisKey);
		// 获取规则List
		if (ruleSet == null || ruleSet.size() == 0)
		{
			// 重新去获取数据
			NoticeRedisBean noticeRedisBean = new NoticeRedisBean();
			ConfigBean config = new ConfigBean();
			config.setUserId(bean.getUserId());
			DataBean data = new DataBean();
			data.setDbName(bean.getDb());
			data.setDeviceOwner(bean.getDeviceOwner());
			noticeRedisBean.setConfig(config);
			noticeRedisBean.setData(data);

			JSONObject jsonObject = frontService.noticeRedis(noticeRedisBean);
			if (jsonObject != null)
			{
				log.info("redisResult is success" + jsonObject.toJSONString());
			}
			ruleSet = hashOperations.keys(RedisUtil.getRuleKey(bean.getDb(),bean.getDeviceOwner()));
		}
		// 不管成功还是失败，直接获取缓存
		if (ruleSet == null || ruleSet.size() == 0)
		{
			return null;
		} else {
			hashOperations.getOperations().expire(RedisUtil.getRuleKey(bean.getDb(),bean.getDeviceOwner()), 7, TimeUnit.DAYS);
		}
		Rule rule = null;
		String topic = null;
		// 进行值替换
		List<Rule> result = new ArrayList<>(ruleSet.size());
		
		Iterator<String> it = ruleSet.iterator();  
		String ruleId = null;
		Object ruleBean = null;
		while (it.hasNext()) { 
			ruleId= it.next();
			if(hashOperations.hasKey(redisKey, ruleId)){
				ruleBean = hashOperations.get(redisKey, ruleId);
				if(ruleBean != null){
					rule = JSONObject.parseObject(ruleBean.toString(), Rule.class);
					topic = rule.getTopic();
					// 如果接入方式存在，并且不一样，则直接返回
					if (mqttData.getAccessMode() != null && !mqttData.getAccessMode().equals(rule.getAccessMode())
							&& mqttData.getVersion() != null && !mqttData.getVersion().equals(rule.getVersion()))
					{
						continue;
					}
					// topic处理
					if (StringUtils.isNotEmpty(topic))
					{
						// 替换topic表达式
						topic = CommonConstant.OPERATOR_NO + topic;
						topic = topic.replaceAll(CommonConstant.DOUBLE_SLASH_ADD, CommonConstant.EXPRESSION_ADD).replace(CommonConstant.WELL_NUMBER_ONE, CommonConstant.REGULAR_EXPRESSION_THREE);
						topic = topic + CommonConstant.DOLLAR;
					}
					rule.setTopic(topic);
					result.add(rule);// 替换topic值
				}
			}
		} 
		return result;	
	}
	
	private UserBean getUserByProductKey(String productKey,String deviceName)
	{
		List<UserBean> beans = coreproCommonService.getUserInfo(productKey,deviceName);
		if (beans != null && beans.size() > 0)
		{
			UserBean bean = beans.get(0);
			if (bean != null)
			{
				return bean;
			}
		}
		return null;
	}

}
