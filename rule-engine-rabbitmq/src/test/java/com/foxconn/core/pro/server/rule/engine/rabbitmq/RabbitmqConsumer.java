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
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

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
public class RabbitmqConsumer
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
		//声明要关注的队列
		channel.queueDeclare("rule_engine", true, false, false, null);
		channel.queueBind("rule_engine", "rule_engine", "rule_engine");
		 // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
		Consumer consumer = new DefaultConsumer(channel)
		{
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException
			{
				String message = new String(body, "UTF-8");
				System.out.println("Customer Received '" + message + "'");
			}
		};
		// 自动回复队列应答 -- RabbitMQ中的消息确认机制
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}

}
