/**
 * Project Name:rule-engine-core
 * File Name:RabbitmqSubscribe.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.rabbit
 * Date:2018年8月28日上午8:53:18
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.rabbitmq;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.action.common.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.action.common.util.RedisUtil;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.action.entity.ActionBean;
import com.foxconn.core.pro.server.rule.engine.action.listener.BaseListener;
import com.foxconn.core.pro.server.rule.engine.action.thirdparty.clound.FrontService;
import com.foxconn.core.pro.server.rule.engine.action.thirdparty.common.service.CoreproCommonService;
import com.foxconn.core.pro.server.rule.engine.action.thirdparty.entity.ConfigBean;
import com.foxconn.core.pro.server.rule.engine.action.thirdparty.entity.DataBean;
import com.foxconn.core.pro.server.rule.engine.action.thirdparty.entity.NoticeRedisBean;
import com.foxconn.core.pro.server.rule.engine.action.util.SpringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * RabbitmqSubscribe <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月28日 上午8:53:18 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component
// @RabbitListener(queues = "${com.rule.engine.action.rabbitmq.queue}")
@Slf4j
public class RabbitmqConsumer
{

	@Autowired
    private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private CoreproCommonService coreproCommonService;
	
	@Resource
	private FrontService frontService;

	/**
	 * action处理 action:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author liupingan
	 * @param msg
	 * @since JDK 1.8
	 */
	@RabbitListener(containerFactory = CommonConstant.POINT_TASKCONTAINER_FACTORY,bindings =
	{ @QueueBinding(value = @Queue(value = CommonConstant.ACTION_RABBITMQ_QUEUE, autoDelete = CommonConstant.RABBITMQ_QUEUE_AUTO_DELETE, durable = CommonConstant.RABBITMQ_QUEUE_DURABLE, exclusive = CommonConstant.ACTION_RABBITMQ_EXCLUSIVE), exchange = @Exchange(value = CommonConstant.ACTION_RABBITMQ_EXCHANGE, durable = CommonConstant.RABBITMQ_EXCHANGE_DURABLE, autoDelete = CommonConstant.RABBITMQ_EXCHANGE_AUTO_DELETE, type = ExchangeTypes.TOPIC)) })

	public void action(@Header(CommonConstant.AMQP_RECEIVEDROUTINGKEY) String routeKey, org.springframework.amqp.core.Message data)
	{
		// 接收规则引擎传送的action数据
		try
		{
			// if(action)
			// 如果不为空,则进行处理
			// 查找redis缓存action应用
			// 进行分批处理，采用监听器模式
			log.info("-------------- action start --------------");
			log.info("-------------- action data --------------" + data != null ? JSONObject.toJSONString(data) : null);
			ActionBean actionBean = null;
			BaseListener listener = null;
			JSONArray actions = null;
			String msg = null;
			Object actionTypeId = null;
			Integer actionTypeIdI = null;
			if (data != null && StringUtils.isNotEmpty(new String(data.getBody(),StandardCharsets.UTF_8)))
			{
				msg = new String(data.getBody(),StandardCharsets.UTF_8);
				log.info("-------------- action msg --------------" + msg);
				actionBean = JSONObject.parseObject(msg, ActionBean.class);
				JSONObject systemData = null;
				String topic = null;
				if (actionBean != null)
				{
					actions = getActions(actionBean.getRuleId(), actionBean.getDbName(),actionBean.getDeviceOwner());
					if (actions != null)
					{
						systemData = getSystemData(actionBean);
						// systemData.put("", value)
						for (int i = 0; i < actions.size(); i++)
						{
							log.info("-------------- action " + i + " --classtype------------"
									+ actions.getJSONObject(i).getString(CommonConstant.CLASSTYPE));
							// 监听事件
							listener = SpringUtil.getBean(actions.getJSONObject(i).getString(CommonConstant.CLASSTYPE),
									BaseListener.class);
							if (actions.getJSONObject(i).containsKey(CommonConstant.ACTIONTYPEID))
							{
								actionTypeId = actions.getJSONObject(i).get(CommonConstant.ACTIONTYPEID);
								if (actionTypeId != null && actionTypeId instanceof Integer)
								{
									actionTypeIdI = (Integer) actionTypeId;
									// 如果为本地自己的kafaka，需要做特殊处理DBS
									if (actionTypeIdI == 1)
									{
										topic = getTopic(actionBean.getDataId(),CommonConstant.ZERO);//0:DBS,1:Kvlin
										actions.getJSONObject(i).getJSONObject(CommonConstant.PARAMS).put(CommonConstant.TOPIC, topic);
										systemData.put(CommonConstant.TOPIC, topic);
										systemData.put(CommonConstant.WAY, CommonConstant.FOXCONN);
										systemData.put(CommonConstant.TYPE, CommonConstant.ZERO);
									} else if (actionTypeIdI == 4)//Kvlin
									{
										topic = getTopic(actionBean.getDataId(),CommonConstant.ONE);
										actions.getJSONObject(i).getJSONObject(CommonConstant.PARAMS).put(CommonConstant.TOPIC, topic);
										systemData.put(CommonConstant.TOPIC, topic);
										systemData.put(CommonConstant.WAY, CommonConstant.FOXCONN);
										systemData.put(CommonConstant.TYPE, CommonConstant.ONE);
									}
								}
							}
							// 获取参数类型
							listener.action(actions.getJSONObject(i).getJSONObject(CommonConstant.PARAMS), actionBean.getData(),
									systemData);
						}
					}
				}
			}
			log.info("-------------- action end --------------");
		} catch (BaseException e)
		{
			log.error("action recive base exception:" + data != null ? JSONObject.toJSONString(data) : null, e);
		} catch (Exception e)
		{
			log.error("action recive exception:" + data != null ? JSONObject.toJSONString(data) : null, e);
		}

	}

	/**
	 * 获取product的值 getSystemData:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author liupingan
	 * @param actionBean
	 * @return
	 * @since JDK 1.8
	 */
	private JSONObject getSystemData(ActionBean actionBean)
	{
		JSONObject systemData = new JSONObject();
		systemData.put(CommonConstant.PRODUCTID, actionBean.getProductId());
		systemData.put(CommonConstant.DEVICE_NAME, actionBean.getDeviceName());
		systemData.put(CommonConstant.TIME_STAMP, actionBean.getTimestamp());
		systemData.put(CommonConstant.ID, actionBean.getId());
		systemData.put(CommonConstant.SQUE, actionBean.getSque());
		return systemData;
	}

	/**
	 * getActions: 获取动作. <br/>
	 *
	 * @author liupingan
	 * @param ruleId
	 * @return
	 * @since JDK 1.8
	 */
	private JSONArray getActions(String ruleId, String dbName,String deviceOwner)
	{
		JSONArray result = null;
		String redisKey = RedisUtil.getRuleActionKey(dbName,deviceOwner);
		if(dbName == null || StringUtils.isBlank(dbName) || deviceOwner == null || StringUtils.isBlank(deviceOwner)) {
			return result;
		}
		//如果为空，则进行处理
		if (!hashOperations.hasKey(redisKey, ruleId))
		{
			NoticeRedisBean noticeRedisBean = new NoticeRedisBean();
			ConfigBean config = new ConfigBean();
			config.setUserId(deviceOwner);
			DataBean data = new DataBean();
			data.setDbName(dbName);
			data.setDeviceOwner(deviceOwner);
			noticeRedisBean.setConfig(config);
			noticeRedisBean.setData(data);

			JSONObject jsonObject = frontService.noticeRedis(noticeRedisBean);
			if (jsonObject != null)
			{
				log.debug("redisResult is success" + jsonObject.toJSONString());
			} else {
				log.debug("redisResult is error");
			}
		}
		//获取缓存信息
		if(hashOperations.hasKey(redisKey, ruleId)){
			hashOperations.getOperations().expire(RedisUtil.getRuleKey(redisKey,deviceOwner), 7, TimeUnit.DAYS);
			Object actionBean = hashOperations.get(redisKey, ruleId);
			if (actionBean != null)
			{
				result = JSONObject.parseArray(actionBean.toString());
			}
		}
		
		return result;
	}

	private String getTopic(String dataId,String type)
	{
		if (dataId == null || StringUtils.isEmpty(dataId))
		{
			return null;
		}
		List<String> list = coreproCommonService.getTopic(dataId,type);
		if (list != null && list.size() == 1)
		{
			return list.get(0);
		}
		return null;
	}

}
