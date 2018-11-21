/**
 * Project Name:rule-engine-core
 * File Name:SimpleRuleService.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.service.impl
 * Date:2018年8月23日下午5:22:33
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.rabbitmq.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.foxconn.core.pro.server.rule.engine.rabbitmq.constant.CommonConstant;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.analysis.ConditionAnalysis;
import com.foxconn.core.pro.server.rule.engine.core.analysis.FieldAnalysis;
import com.foxconn.core.pro.server.rule.engine.core.analysis.TopicAnalysis;
import com.foxconn.core.pro.server.rule.engine.core.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.core.common.exception.ErrorCodes;
import com.foxconn.core.pro.server.rule.engine.core.common.util.DataUtil;
import com.foxconn.core.pro.server.rule.engine.core.common.util.RedisUtil;
import com.foxconn.core.pro.server.rule.engine.core.entity.Rule;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.MqttData;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.PayloadBean;
import com.foxconn.core.pro.server.rule.engine.core.express.SpringBeanRunner;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.clound.FrontService;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.common.service.CoreproCommonService;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.config.ServerParamConfig;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.ConfigBean;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.DataBean;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.NoticeRedisBean;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.UserBean;
import com.foxconn.core.pro.server.rule.engine.rabbitmq.rabbitmq.RabbitmqProducer;
import com.foxconn.core.pro.server.rule.engine.rabbitmq.service.IRuleService;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:SimpleRuleService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月23日 下午5:22:33 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Slf4j
public class SimpleRuleService implements IRuleService
{

	@Autowired
    private HashOperations<String, String, Object> hashOperations;

	@Resource
	private SpringBeanRunner runner;

	@Resource
	private ConditionAnalysis conditionAnalysis;

	@Resource
	private FieldAnalysis fieldAnalysis;

	@Resource
	private RabbitmqProducer rabbitmqProducer;

	@Resource
	private FrontService frontService;

	@Resource
	private ServerParamConfig serverParamConfig;

	// 用户信息
	/*
	 * @Autowired private AccountService accountService;
	 */

	@Autowired
	private CoreproCommonService coreproCommonService;

	public void excute(MqttData data) throws Exception
	{
		log.info("----------- rule excute start-----" + JSONObject.toJSONString(data));
		TopicAnalysis topicAnalysis = new TopicAnalysis();
		if (data == null || data.getPayload() == null || StringUtils.isEmpty(data.getPayload().getProductkey()))
		{
			log.info("rule engine is illege:" + JSONObject.toJSONString(data));
			return;
		}

		log.info("-----------getProductkey-----" + data.getPayload().getProductkey());
		
		// TODO 测试用例
		// rules = getTestList();

		List<Rule> rulesTopicMatch = null;
		List<Rule> rulesDataMatch = null;
		try
		{
			

			// 获取MqttData数组，匹配所有数据
			List<MqttData> listMqttData = getMqttDataList(data);
			if (listMqttData == null || listMqttData.size() == 0)
			{
				log.info("match rule failer");
				return;
			}
			log.info("-----------rulesTopicMatchrulesListMqttData-----" + (listMqttData != null
					? JSONObject.toJSONString(listMqttData) : null));

			MqttData tempData = null;
			int sque = 0;
			UserBean userBean = null;
			List<Rule> rules  = null;
			for (int n = 0; n < listMqttData.size(); n++)
			{
				try
				{
					tempData = listMqttData.get(n);
					log.info("-----------getDeviceName-----" + tempData.getPayload().getDeviceName());
					userBean = this.getUserByProductKey(tempData.getPayload().getProductkey(),tempData.getPayload().getDeviceName());
					log.info("-----------userBean-----" + (userBean != null ? JSONObject.toJSONString(userBean) : CommonConstant.NU_LL));
					if (userBean == null)
					{
						log.info("rule engine is illege:" + JSONObject.toJSONString(tempData));
						continue;
					}
					rules = getRuleListByUser(userBean, data);// 根据userId获取规则
					log.info("-----------rulesList-----" + (rules != null ? JSONObject.toJSONString(rules) : null));
					
					rulesTopicMatch = topicAnalysis.getMatchRuleList(data.getTopic(), rules);

					log.info("-----------rulesTopicMatchrulesList-----" + (rulesTopicMatch != null
							? JSONObject.toJSONString(rulesTopicMatch) : null));
					sque = n;
					
					// 如果匹配成功,则查看条件是否符合
					Map<String, Object> context = null;
					// 如果有匹配成功的，则继续处理,进行数据筛选,数据筛选采用模板引擎处理
					String condition = null;
					JSONObject message = null;
					JSONObject dataAction = null;
					MqttData fieldData = null;
					PayloadBean payloadBean = null;
					JSONObject filedParams = null;
					JSONObject systemParams = null;
					Rule rule = null;

					if (rulesTopicMatch != null && rulesTopicMatch.size() > 0)
					{
						rulesDataMatch = new ArrayList<>(rulesTopicMatch.size() / 2);
						context = new HashMap<>(2);
						context.put(CommonConstant.TOPIC, tempData.getTopic());

						conditionAnalysis.setRules(rulesTopicMatch);
						context.put(CommonConstant.ROOT, tempData.getPayload().getParams());// 根据协议去解析
						conditionAnalysis.setData(tempData);
						rulesDataMatch = conditionAnalysis.getMatchRuleList(tempData,context,rulesTopicMatch,userBean.getDb(),userBean.getDeviceOwner(),hashOperations);
					}

					log.info("------------topic rule result-----");
					log.info("------------topic rule result-----" + (rulesDataMatch != null
							? JSONObject.toJSONString(rulesDataMatch) : ""));

					
					if (rulesDataMatch != null && rulesDataMatch.size() > 0)
					{
						for (int i = 0; i < rulesDataMatch.size(); i++)
						{
							fieldData = new MqttData();
							payloadBean = new PayloadBean();
							rule = rulesDataMatch.get(i);
							if (tempData.getPayload().getParams() instanceof JSONObject)
							{
								filedParams = (JSONObject) tempData.getPayload().getParams();
								payloadBean.setParams(filedParams);

								payloadBean.setDeviceName(tempData.getPayload().getDeviceName());
								payloadBean.setProductkey(tempData.getPayload().getProductkey());
								payloadBean.setTimestamp(tempData.getPayload().getTimestamp());

								fieldData.setPayload(payloadBean);

								fieldData.setId(tempData.getId());
								fieldData.setAccessMode(tempData.getAccessMode());

								fieldData.setTimestamp(tempData.getTimestamp());
								fieldData.setTopic(tempData.getTopic());
								fieldData.setType(tempData.getType());

								condition = rule.getField();
								
								systemParams = new JSONObject();
								systemParams.put(CommonConstant.TOPIC, tempData.getTopic());
								systemParams.put(CommonConstant.TIMES_TAMP, tempData.getPayload().getTimestamp());
								systemParams.put(CommonConstant.PRODUCT_KEY, tempData.getPayload().getProductkey());
								systemParams.put(CommonConstant.DEVICE_NAME, tempData.getPayload().getDeviceName());
								systemParams.put(CommonConstant.TYPE, tempData.getType());
								systemParams.put(CommonConstant.ACCESS_MODE, tempData.getAccessMode());
								
								context = new HashMap<>(2);
								context.put(CommonConstant.SYSTEM_PARAMS, systemParams);
								context.put(CommonConstant.ROOT, filedParams);
								dataAction = fieldAnalysis.excute(fieldData, context, condition,userBean.getDb(),userBean.getDeviceOwner(),hashOperations,rule);
								log.info("------------dataAction result-----" + i + "-------" + (dataAction != null
										? JSONObject.toJSONString(dataAction) : ""));
								if (dataAction != null)
								{
									message = new JSONObject();
									message.put(CommonConstant.DATA, dataAction);
									// messsageid
									message.put(CommonConstant.ID, tempData.getId());
									message.put(CommonConstant.DEVICE_OWNER, userBean.getDeviceOwner());
									message.put(CommonConstant.RULE_ID, rule.getId());
									message.put(CommonConstant.DB_NAME, userBean.getDb());
									message.put(CommonConstant.DATA_ID, tempData.getDataId());
									message.put(CommonConstant.TYPE, tempData.getType());
									message.put(CommonConstant.PRODUCT_ID, tempData.getPayload().getProductkey());
									message.put(CommonConstant.DEVICE_NAME, tempData.getPayload().getDeviceName());
									message.put(CommonConstant.TIMES_TAMP, tempData.getPayload().getTimestamp());
									if (tempData.getVersion() != null && 0 == tempData.getVersion().intValue()) //添加序号
									{
										message.put(CommonConstant.SQUE, sque);
									}
									rabbitmqProducer.publish(message.toJSONString());
								}
							}
						}
					} else
					{
						log.info("Match failure:", JSONObject.toJSONString(tempData));
					}
				} catch (Exception e)
				{
					log.info("----------- MatchException-----" + JSONObject.toJSONString(data));
					log.error("Match exception:", e);
				}
			}
			log.info("----------- rule excute end-----");
		} catch (Exception e)
		{
			log.info("----------- MatchException-----" + JSONObject.toJSONString(data));
			log.error("Match exception:", e);
		}
	}

	/**
	 *
	 * getMqttDataList:( 获取List列表数据). <br/>
	 * 
	 * @author liupingan
	 * @param data
	 * @return
	 * @since JDK 1.8
	 */
	private List<MqttData> getMqttDataList(MqttData data)
	{
		if (data == null || data.getPayload() == null)
			return null;
		JSON params = data.getPayload().getParams();
		JSONArray jsonArrayParams = null;
		if (params == null)
		{
			List<MqttData> list = new ArrayList<>(1);
			list.add(data);
			return list;
		} else if (params instanceof JSONObject)
		{
			List<MqttData> list = new ArrayList<>(1);
			list.add(data);
			return list;
		} else if (params instanceof JSONArray)
		{
			jsonArrayParams = (JSONArray) params;
			if (jsonArrayParams != null && jsonArrayParams.size() > 0)
			{
				List<MqttData> list = new ArrayList<>(jsonArrayParams.size());
				for (int m = 0; m < jsonArrayParams.size(); m++)
				{
					list.add(getMqttData(data, jsonArrayParams.getJSONObject(m)));
				}
				return list;
			} else
			{
				List<MqttData> list = new ArrayList<>(1);
				list.add(data);
				return list;
			}
		} else
		{
			List<MqttData> list = new ArrayList<>(1);
			list.add(data);
			return list;
		}
	}

	/**
	 * 获取mqttData数据 getMqttData:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author liupingan
	 * @param data
	 * @param params
	 * @return
	 * @since JDK 1.8
	 */
	private MqttData getMqttData(MqttData data, JSONObject params)
	{
		MqttData result = null;
		try
		{
			result = DataUtil.copyImplSerializable(data);
			// BeanUtils.copyProperties(result, data);
			PayloadBean payloadBean = result.getPayload();

			if (payloadBean != null)
			{
				payloadBean.setParams(params);
				payloadBean.setDeviceName(data.getPayload().getDeviceName());
				payloadBean.setProductkey(data.getPayload().getProductkey());
				payloadBean.setTimestamp(data.getPayload().getTimestamp());
				if (params.containsKey(CommonConstant.DEVICE_NAME))
				{
					payloadBean.setDeviceName(params.getString(CommonConstant.DEVICE_NAME));
				}
				payloadBean.setParams(params);
				result.setPayload(payloadBean);
			}
		} catch (IllegalAccessException e)
		{
			log.error("getConditionMqttData error", e);
		} catch (InvocationTargetException e)
		{
			log.error("getConditionMqttData error", e);
		} catch (InstantiationException e)
		{
			log.error("getConditionMqttData error", e);

		} catch (NoSuchMethodException e)
		{
			log.error("getConditionMqttData error", e);
		} catch (Exception e)
		{
			log.error("getConditionMqttData error", e);
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

	public ResultMap<? extends Object> debug(JSONObject data)
	{
		ResultMap<JSONObject> result = new ResultMap<>();
		if (data == null || !data.containsKey(CommonConstant.FIELDS) || !data.containsKey(CommonConstant.TOPIC) || !data.containsKey(CommonConstant.DATA))
		{
			result.setCode(ErrorCodes.RULE_PARAM_EMPT.getCode());
			result.setMsg(ErrorCodes.RULE_PARAM_EMPT.getDesc());
			return result;
		}

		JSONObject sourceData = data.getJSONObject(CommonConstant.DATA);

		String dataTopic = null;
		JSONObject dataParams = null;
		if (sourceData != null)
		{
			dataTopic = sourceData.getString(CommonConstant.TOPIC);
			dataParams = sourceData.getJSONObject(CommonConstant.PARAMS);
		}
		if (StringUtils.isEmpty(dataTopic) || dataParams == null)
		{
			result.setCode(ErrorCodes.RULE_PARAM_EMPT.getCode());
			result.setMsg(ErrorCodes.RULE_PARAM_EMPT.getDesc());
			return result;
		}

		// topi处理
		String topic = data.getString(CommonConstant.TOPIC);
		// topic处理
		if (StringUtils.isNotEmpty(topic))
		{
			// 替换topic表达式
			topic = CommonConstant.OPERATOR_NO + topic;
			topic = topic.replaceAll(CommonConstant.DOUBLE_SLASH_ADD, CommonConstant.REGULAR_EXPRESSION_TWO).replace(CommonConstant.WELL_NUMBER_ONE, CommonConstant.REGULAR_EXPRESSION_THREE);
			topic = topic + CommonConstant.DOLLAR;
		} else
		{
			result.setCode(ErrorCodes.RULE_PARAM_EMPT.getCode());
			result.setMsg(ErrorCodes.RULE_PARAM_EMPT.getDesc());
		}
		TopicAnalysis topicAnalysis = new TopicAnalysis();
		boolean isMatch = topicAnalysis.isMatchRule(dataTopic, topic);
		if (!isMatch)
		{
			return result;
		}
		// 进行数据处理

		String condition = null;
		if (data.containsKey(CommonConstant.CONDITION))
		{
			condition = data.getString(CommonConstant.CONDITION);
		}

		Map<String, Object> context = null;
		if (StringUtils.isEmpty(condition))
		{

		} else
		{
			context = new HashMap<>(2);
			context.put(CommonConstant.TOPIC, context);
			context.put(CommonConstant.ROOT, dataParams);// 根据协议去解析
			isMatch = conditionAnalysis.isMatchRule(context, condition);
		}

		if (!isMatch)
		{
			return result;
		}

		String fileds = data.getString(CommonConstant.FIELDS);
		context = new HashMap<>(2);
		context.put(CommonConstant.TOPIC, context);
		context.put(CommonConstant.ROOT, dataParams);// 根据协议去解析
		JSONObject dataAction = null;
		try
		{
			dataAction = fieldAnalysis.excute(context, fileds);
			result.setData(dataAction);
		} catch (Exception e)
		{
			log.error("rule debug Exception", e);
		}
		log.info("result");
		log.info(dataAction == null ? null : dataAction.toString());
		return result;
	}

}
