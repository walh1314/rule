/**
 * Project Name:rule-engine-action
 * File Name: RabbitConfig.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.config.rabbitmq
 * Date:2018年8月28日下午3:45:10
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

/**
 * ClassName: RabbitConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月28日 下午3:45:10 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Configuration
public class RabbitConfig
{
	@Autowired
	private RabbitmqProperties rabbitmqProperties;

	@Bean(name = CommonConstant.MESSAGE)
	public Queue queueMessage()
	{
		return new Queue(rabbitmqProperties.getQueue(), rabbitmqProperties.isQueueDurable(),
				rabbitmqProperties.isExclusive(), rabbitmqProperties.isQueueAutoDelete());
	}

	@Bean
	public TopicExchange exchange()
	{
		return new TopicExchange(rabbitmqProperties.getExchange(), rabbitmqProperties.isExchangeDurable(),
				rabbitmqProperties.isExchangeAutoDelete());
	}

	@Bean
	Binding bindingExchangeMessage(@Qualifier(CommonConstant.MESSAGE) Queue queueMessage, TopicExchange exchange)
	{
		return BindingBuilder.bind(queueMessage).to(exchange).with(rabbitmqProperties.getTopic());
	}

	@Bean(CommonConstant.POINT_TASK_CONTAINER_FACTORY)
	public SimpleRabbitListenerContainerFactory pointTaskContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setPrefetchCount(rabbitmqProperties.getPrefetchCount());
		factory.setConcurrentConsumers(rabbitmqProperties.getConcurrent());
		configurer.configure(factory, connectionFactory);
		return factory;
	}
}
