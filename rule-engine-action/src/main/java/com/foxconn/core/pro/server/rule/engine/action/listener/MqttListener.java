/**
 * Project Name:rule-engine-action
 * File Name:MqttListener.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.listener
 * Date:2018年8月31日上午9:43:14
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.action.config.mqtt.spring.MqttGateway;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:MqttListener <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月31日 上午9:43:14 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component(CommonConstant.MQTT_LISTENER)
@Slf4j
public class MqttListener implements BaseListener
{

	@Autowired
	private MqttGateway mqttGateway;

	@Override
	public void action(JSONObject parameter, JSONObject bean,JSONObject systemData)
	{
		try
		{
			log.info("=========mqttStart==========");
			log.info("-------------- mqttStartParameter --------------" + parameter);
			log.info("-------------- mqttStartBean --------------" + bean);
			String topic = parameter.getString(CommonConstant.TOPIC);
			Integer qos = parameter.getInteger(CommonConstant.QOS);

			if (topic == null)
			{
				log.error("mqtt Listener topic is null:" + (parameter != null ? parameter.toJSONString() : CommonConstant.NU_LL) + CommonConstant.COMMA
						+ (bean != null ? bean.toJSONString() : CommonConstant.NU_LL));
				return;
			}
			if (qos == null)
			{
				mqttGateway.sendToMqtt(topic, bean.toJSONString());
			} else
			{
				mqttGateway.sendToMqtt(topic, qos, bean.toJSONString());
			}
			log.info("-------------- mqttNormalEnd --------------");
		} catch (Exception e)
		{
			log.error("Mqtt Listener Exception:" + (parameter != null ? parameter.toJSONString() : CommonConstant.NU_LL) + CommonConstant.COMMA
					+ (bean != null ? bean.toJSONString() : CommonConstant.NU_LL), e);
		}
	}

}
