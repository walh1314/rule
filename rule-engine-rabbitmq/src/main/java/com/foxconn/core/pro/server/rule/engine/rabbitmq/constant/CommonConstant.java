/**
 * Project Name:rule-engine-core
 * File Name:CommonConstant.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.constant
 * Date:2018年8月28日下午5:31:23
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.rabbitmq.constant;

/**
 * 
 * ClassName: CommonConstant <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年11月10日 下午2:50:37 <br/>
 *
 * @author hewanwan
 * @version 
 * @since JDK 1.8
 */
public interface CommonConstant
{
	
	String RABBITMQPROPERTIES = "rabbitmqProperties";
	String SPRING_RABBITMQ = "spring.rabbitmq";
	
	String RABBITMQCONFIGURATION = "rabbitmqConfiguration";
	String SOURCE_CONNECTION_FACTORY = "sourceConnectionFactory";
	String SOURCE_RABBIT_TEMPLATE = "sourceRabbitTemplate";
	String SOURCE_CONTAINER_FACTORY = "sourceContainerFactory";
	String SOURCE_QUEUE = "sourceQueue";
	String EXCHANGE_SOURCE = "exchangeSource";
	String ACTION_CONNECTION_FACTORY = "actionConnectionFactory";
	String ACTION_RABBIT_TEMPLATE = "actionRabbitTemplate";
	String ACTION_CONTAINER_FACTORY = "actionContainerFactory";
	String ACTION_QUEUE = "actionQueue";
	String EXCHANGE_ACTION = "exchangeAction";
	
	String SPRING_RABBITMQ_SOURCE_QUEUE = "${spring.rabbitmq.source.queue}";
	String SPRING_RABBITMQ_SOURCE_QUEUE_AUTO_DELETE = "${spring.rabbitmq.source.queue-auto-delete}";
	String SPRING_RABBITMQ_SOURCE_QUEUE_DURABLE = "${spring.rabbitmq.source.queue-durable}";
	String SPRING_RABBITMQ_SOURCE_EXCLUSIVE = "${spring.rabbitmq.source.exclusive}";
	String SPRING_RABBITMQ_SOURCE_EXCHANGE = "${spring.rabbitmq.source.exchange}";
	String SPRING_RABBITMQ_SOURCE_EXCHANGE_DURABLE = "${spring.rabbitmq.source.exchange-durable}";
	String SPRING_RABBITMQ_SOURCE_EXCHANGE_AUTO_DELETE = "${spring.rabbitmq.source.exchange-auto-delete}";
	String AMQP_RECEIVED_ROUTINGKEY = "amqp_receivedRoutingKey";
	
	String TOPIC = "topic";
	String ROOT = "root";
	String NU_LL= "";
	String TIMES_TAMP = "timestamp";
	String PRODUCT_KEY = "productkey";
	String DEVICE_NAME= "devicename";
	String TYPE = "type";
	String ACCESS_MODE = "accessMode";
	String SYSTEM_PARAMS = "systemParams";
	String DATA= "data";
	String ID = "id";
	String DEVICE_OWNER = "deviceOwner";
	String RULE_ID = "ruleId";
	String DB_NAME = "dbName";
	String DATA_ID = "dataId";
	String PRODUCT_ID= "product_id";
	String SQUE = "sque";
	String OPERATOR_NO = "^";
	String DOUBLE_SLASH_ADD = "\\+";
	String EXPRESSION_ADD = "[^\\\\s\\/]+";
	String WELL_NUMBER_ONE = "#";
	String REGULAR_EXPRESSION_THREE = "[\\S]{0,}";
	String DOLLAR = "$";
	String FIELDS = "fields";
	String PARAMS = "params";
	String REGULAR_EXPRESSION_TWO = "[^\\s\\/]+";
	String CONDITION = "condition";
	
	String PROTOTYPE = "prototype";
	
	String COM_MQTT_TASK = "com.mqtt.task";
	
	String RULEENGINE_TASKEXECUTOR = "ruleEngineTaskExecutor";
	
	String SERVER_RULE_ENGINE_CORE = "com.foxconn.core.pro.server.rule.engine.core";
	String SERVER_RULE_ENGINE_RABBITMQ = "com.foxconn.core.pro.server.rule.engine.rabbitmq";

	
	
}
