/**
 * Project Name:rule-engine-rabbitmq
 * File Name:RabbitmqJunitTest.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.rabbitmq
 * Date:2018年10月31日上午10:57:24
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.rabbitmq;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.MqttData;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.PayloadBean;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * ClassName:RabbitmqJunitTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月31日 上午10:57:24 <br/>
 * 
 * @author hewanwan
 * @version
 * @since JDK 1.8
 * @see
 */
public class RabbitmqJunitTest
{

	public final static String QUEUE_NAME = "rule_engine.event";

	public static void main(String[] args) throws IOException, Exception
	{
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ相关信息
		factory.setHost("192.168.99.100");
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setPort(5672);
		// 创建一个新的连接
		Connection connection = factory.newConnection();
		// 创建一个通道
		Channel channel = connection.createChannel();
		/*声明一个队列
		queueDeclare第一个参数表示队列名称、第二个参数为是否持久化（true表示是，队列将在服务器重启时生存）、
		第三个参数为是否是独占队列（创建者可以使用的私有队列，断开后自动删除）、第四个参数为当所有消费者客户端连接断开时是否自动删除队列、第五个参数为队列的其他参数*/
		channel.queueDeclare("rule_engine", true, false, false, null);
		channel.queueBind("rule_engine", "rule_engine", "rule_engine");
		
		int count = 0;
		int max = 2;
		String message = "Hello RabbitMQ";
		MqttData mqttData = new MqttData();
		mqttData.setTopic("test");
		mqttData.setDataId("DataId");
		mqttData.setId("2");
		mqttData.setTimestamp(1L);
		mqttData.setType("type");
		mqttData.setAccessMode(null);
		mqttData.setVersion(0);
		PayloadBean payloadBean = new PayloadBean();
		payloadBean.setTimestamp(1);
		payloadBean.setProductkey("Productkey");
		payloadBean.setDeviceName("name");
		JSONObject params = new JSONObject();
		while (count < max)
		{
			count++;
			params = new JSONObject();
			params.put("status", 1);
			params.put("count", 1);
			params.put("value", 100);
			params.put("round", Math.random()*100);
			params.put("time", System.nanoTime());
			payloadBean.setParams(params);
			mqttData.setPayload(payloadBean);
			message = JSONObject.toJSONString(mqttData);
			//发送消息到队列中
			//basicPublish第一个参数为交换机名称、第二个参数为队列映射的路由key、第三个参数为消息的其他属性、第四个参数为发送信息的主体
			channel.basicPublish("rule_engine", "rule_engine", null, message.getBytes("UTF-8"));
		}
		// 关闭通道和连接
		channel.close();
		connection.close();
	}

}
