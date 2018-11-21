/**
 * Project Name:rule-engine-core
 * File Name:RabbitMqConfiguration.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.config.rabbitmq
 * Date:2018年8月30日上午8:02:09
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.kafka.producer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.foxconn.core.pro.server.rule.engine.kafka.constant.CommonConstant;
/**
 * ClassName:RabbitMqConfiguration <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月30日 上午8:02:09 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Configuration(CommonConstant.RABBITMQCONFIGURATION)
public class RabbitMqConfiguration
{

	@Autowired
	private RabbitmqConfigProperties rabbitmqConfigProperties;

	@Bean(name = CommonConstant.ACTION_CONNECTION_FACTORY)
	@Primary
	public ConnectionFactory actionConnectionFactory()
	{
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(rabbitmqConfigProperties.getAction().getAddress());
		connectionFactory.setUsername(rabbitmqConfigProperties.getAction().getUsername());
		connectionFactory.setPassword(rabbitmqConfigProperties.getAction().getPassword());
		connectionFactory.setVirtualHost(rabbitmqConfigProperties.getAction().getVirtualHost());
		return connectionFactory;
	}

	@Bean(name = CommonConstant.ACTION_RABBIT_TEMPLATE)
	// @Primary
	public RabbitTemplate actionRabbitTemplate(@Qualifier(CommonConstant.ACTION_CONNECTION_FACTORY) ConnectionFactory connectionFactory)
	{
		RabbitTemplate actionRabbitTemplate = new RabbitTemplate(connectionFactory);
		//actionRabbitTemplate.setChannelTransacted(true);
		// 使用外部事物
		// ydtRabbitTemplate.setChannelTransacted(true);
		return actionRabbitTemplate;
	}

	@Bean(name = CommonConstant.ACTION_CONTAINER_FACTORY)
	public SimpleRabbitListenerContainerFactory actionFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
			@Qualifier(CommonConstant.ACTION_CONNECTION_FACTORY) ConnectionFactory connectionFactory)
	{
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}


	@Bean(name = CommonConstant.ACTION_QUEUE)
	public Queue actionQueue()
	{
		return new Queue(rabbitmqConfigProperties.getAction().getQueue(), rabbitmqConfigProperties.getAction().isQueueDurable(),
				rabbitmqConfigProperties.getAction().isExclusive(), rabbitmqConfigProperties.getAction().isQueueAutoDelete());
	}


	@Bean(name = CommonConstant.EXCHANGE_ACTION)
	public TopicExchange exchangeAction()
	{
		return new TopicExchange(rabbitmqConfigProperties.getAction().getExchange(),
				rabbitmqConfigProperties.getAction().isExchangeDurable(), rabbitmqConfigProperties.getAction().isExchangeAutoDelete());
	}


	@Bean
	Binding bindingExchangeActionMessage(@Qualifier(CommonConstant.ACTION_QUEUE) Queue queueMessage,
			@Qualifier(CommonConstant.EXCHANGE_ACTION) TopicExchange exchange)
	{
		return BindingBuilder.bind(queueMessage).to(exchange).with(rabbitmqConfigProperties.getAction().getTopic());
	}

}
