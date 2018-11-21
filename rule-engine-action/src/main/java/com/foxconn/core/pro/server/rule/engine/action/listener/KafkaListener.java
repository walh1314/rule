/**
 * Project Name:rule-engine-action
 * File Name:KafkaListener.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.listener
 * Date:2018年8月29日上午8:36:21
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.listener;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:KafkaListener <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月29日 上午8:36:21 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component(CommonConstant.KAFKA_LISTENER)
@Slf4j
public class KafkaListener implements BaseListener
{
	@Autowired
	private KafkaProperties kafkaProperties;
	@Override
	public void action(JSONObject parameter, JSONObject bean, JSONObject systemData)
	{
		// producer.send(record)
		KafkaProducer<String, String> producer = null;
		log.info("-------------- kafka start --------------");
		log.info("-------------- kafka parameter --------------" + parameter);
		log.info("-------------- kafka bean --------------" + bean);
		try
		{

			producer = init(parameter);
			ProducerRecord<String, String> msg = null;
			if (systemData != null && systemData.containsKey(CommonConstant.WAY) && CommonConstant.FOXCONN.equals(systemData.getString(CommonConstant.WAY)))
			{
				/*if(systemData.containsKey(CommonConstant.TYPE) && CommonConstant.ZERO.equals(systemData.getString(CommonConstant.TYPE))){
					msg = new ProducerRecord<String, String>(parameter.getString(CommonConstant.TOPIC),
							getDBSData(systemData, bean).toJSONString());
				} else*/
				if(systemData.containsKey(CommonConstant.TYPE) && CommonConstant.ONE.equals(systemData.getString(CommonConstant.TYPE))){
					msg = new ProducerRecord<String, String>(parameter.getString(CommonConstant.TOPIC),
							getKivlinData(systemData, bean).toJSONString());
				} else {
					msg = new ProducerRecord<String, String>(parameter.getString(CommonConstant.TOPIC), bean.toJSONString());
				}
			} else
			{
				msg = new ProducerRecord<String, String>(parameter.getString(CommonConstant.TOPIC), bean.toJSONString());
			}
			// 进行数据处理

			producer.send(msg);
			log.info("-------------- kafka normal end --------------");
		} catch (Exception e)
		{
			log.error("Kafaka Listener Exception:" + (parameter != null ? parameter.toJSONString() : "") + ","
					+ (bean != null ? bean.toJSONString() : CommonConstant.NU_LL), e);
		} finally
		{
			log.info("-------------- kafka end --------------");
			if (producer != null)
			{
				// 进行关闭
				producer.close(100, TimeUnit.MILLISECONDS);
			}
		}
	}

	private JSONObject getKivlinData(JSONObject systemData, JSONObject data)
	{
		JSONObject result = new JSONObject();
		result.putAll(data);
		result.put(CommonConstant._DEVICE_NAME, systemData.get(CommonConstant.DEVICENAME));
		
		result.put(CommonConstant._TIMESTAMP, systemData.get(CommonConstant.TIMESTAMP));
		
		result.put(CommonConstant._PRODUCT_ID, systemData.get(CommonConstant.PRO_DUCT_ID));
		
		result.put(CommonConstant._ID, systemData.get(CommonConstant.ID));
		
		result.put(CommonConstant._SQUE, systemData.get(CommonConstant.SQUE));
		
		return result;
	}
	
	private JSONObject getDBSData(JSONObject systemData, JSONObject data)
	{
		JSONObject result = new JSONObject();
		result.putAll(data);
		result.put(CommonConstant._DEVICE_NAME, systemData.get(CommonConstant.DEVICENAME));
		
		result.put(CommonConstant._TIMESTAMP, systemData.get(CommonConstant.TIMESTAMP));
		
		result.put(CommonConstant._PRODUCT_ID, systemData.get(CommonConstant.PRO_DUCT_ID));
		
		result.put(CommonConstant._ID, systemData.get(CommonConstant.ID));
		result.put(CommonConstant._SQUE, systemData.get(CommonConstant.SQUE));
		return result;
	}

	private KafkaProducer<String, String> init(JSONObject parameter)
	{
		Properties props = new Properties();
		// 初始化默认值
		props.setProperty(ProducerConfig.LINGER_MS_CONFIG, CommonConstant.ONE);
		props.setProperty(ProducerConfig.ACKS_CONFIG, CommonConstant.ALL);
		props.setProperty(ProducerConfig.RETRIES_CONFIG, CommonConstant.ZERO);
		if (parameter != null)
		{
			for (Entry<String, Object> entry : parameter.entrySet())
			{
				// 排除topic字段
				if (entry.getValue() != null || !CommonConstant.TOPIC.equals(entry.getKey()))
				{
					props.put(entry.getKey(), String.valueOf(entry.getValue()).trim());
				}
			}
		}
		//如果没有配置地址，默认为自定义地址
		if(props.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG) == null
				|| StringUtils.isBlank(props.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG))){
			props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProducer().getBootstrapServers());
		}
		// 测试数据配置
		return new KafkaProducer<String, String>(props);
	}

}
