/**
 * Project Name:rule-engine-action
 * File Name:ProducerConsumer.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.test
 * Date:2018年9月13日下午2:20:29
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ClassName:ProducerConsumer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月13日 下午2:20:29 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class ProducerConsumer
{
	public static void main(String[] args) throws IOException
	{

		/*
		 * //211.159.183.125:9092, 139.199.77.21:9092, 118.89.37.185:9092
		 * //props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
		 * "192.168.1.233:9092");
		 * props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
		 * "211.159.183.125:9092, 139.199.77.21:9092, 118.89.37.185:9092");
		 * props.put("group.id", "test01"); //props.put("client.id", "test");
		 * //props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
		 * "3"); props.put("enable.auto.commit", true);// 显示设置偏移量自动提交
		 * props.put("auto.commit.interval.ms", 1000);// 设置偏移量提交时间间隔
		 * props.put("key.deserializer",
		 * "org.apache.kafka.common.serialization.StringDeserializer");
		 * props.put("value.deserializer",
		 * "org.apache.kafka.common.serialization.StringDeserializer");
		 */
		Properties props = new Properties();

		props.put("bootstrap.servers", "192.168.99.100:9092");
		//props.put("bootstrap.servers", "211.159.183.125:9092, 139.199.77.21:9092, 118.89.37.185:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");// 16M
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		/**
		 * 两个泛型参数 第一个泛型参数：指的就是kafka中一条记录key的类型 第二个泛型参数：指的就是kafka中一条记录value的类型
		 */
		String[] girls = new String[]
		{ "姚慧莹", "刘向前", "周  新", "杨柳" };
		Producer<String, String> producer = new KafkaProducer<String, String>(props);

		String value = buildData();
		// consumer.subscribe(Arrays.asList("sys.corepro.action"));// 订阅主题
		//String topic = "sys.corepro.action";// props.getProperty(Constants.KAFKA_PRODUCER_TOPIC);
		String topic = "-data.kafka.consumer.base.topic";// props.getProperty(Constants.KAFKA_PRODUCER_TOPIC);
		String key = "1";
		//String value = "今天的姑娘们很美111";
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, key, value);
		producer.send(producerRecord);
		producer.close();
	}
	
	public static String buildData() throws JsonProcessingException {
		Map<String , String> sys = new HashMap<String , String>();
		sys.put("appid", "appid0001");
		sys.put("timestamp", new Date().toLocaleString());
		sys.put("type", "sign");
		sys.put("token", "");
		sys.put("id", "5");
		Map<String , Object> data = new HashMap<String , Object>();
		data.put("system_params", sys);
		List<Map<String,Object>> appList = new ArrayList<Map<String,Object>>();
		Map<String,Object> d1 = new HashMap<String , Object>();
		d1.put("deviceName", "dn001");
		d1.put("vol", 20);
		d1.put("cur", 30);
		d1.put("a1", "sone");
		appList.add(d1);
		
		
		Map<String,Object> d2 = new HashMap<String , Object>();
		d2.put("deviceName", "dn002");
		d2.put("vol", 30);
		d2.put("cur", 40);
		d1.put("a1", "cone");
		appList.add(d2);
		data.put("app_params", appList);
		
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(data);
	}

}
