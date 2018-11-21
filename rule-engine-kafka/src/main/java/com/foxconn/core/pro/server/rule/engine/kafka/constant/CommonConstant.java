/**
 * Project Name:rule-engine-core
 * File Name:CommonConstant.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.constant
 * Date:2018年8月28日下午5:31:23
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.kafka.constant;

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
	
	String  KAFKA_CONSUMER_SERVERS = "${kafka.consumer.servers}";
	String  KAFKA_CONSUMER_ENABLE_AUTO_COMMIT = "${kafka.consumer.enable.auto.commit}";
	String  KAFKA_CONSUMER_SESSION_TIMEOUT_MS = "${kafka.consumer.session.timeout.ms}";
	String  KAFKA_CONSUMER_AUTO_COMMIT_INTERVAL = "${kafka.consumer.auto.commit.interval}";
	String  KAFKA_CONSUMER_GROUP_ID = "${kafka.consumer.group.id}";
	String  KAFKA_CONSUMER_AUTO_OFFSET_RESET = "${kafka.consumer.auto.offset.reset}";
	String  KAFKA_CONSUMER_CONCURRENCY = "${kafka.consumer.concurrency}";
	String  KAFKA_CONSUMER_PROPERTIES_SECURITY_PROTOCOL = "${kafka.consumer.properties.security.protocol}";
	String  KAFKA_CONSUMER_PROPERTIES_SASL_MECHANISM = "${kafka.consumer.properties.sasl.mechanism}";
	String  KAFKA_CONSUMER_MAX_POLL_RECORDS = "${kafka.consumer.max.poll.records}";
	
	String  KAFKA_CONSUMER_BASE_TOPIC = "${kafka.consumer.base.topic}";
	String  APPID = "appid";
	String  TOPIC = "topic";
	String  TIMES_TAMP = "timestamp";
	String  STRIKE_THROUGH = "-";
	String  SYMBOL_TWO = "@@";
	String  LEFT_UNDERLINE = "/";
	String  ID = "id";
	String  DEVICE_NAME= "deviceName";
	String DEVICE_NAME1= "devicename";
	
	String  APP_PARAMS = "app_params";
	
	String RABBITMQPROPERTIES = "rabbitmqProperties";
	String SPRING_RABBITMQ = "spring.rabbitmq";
	
	String ACTION_RABBIT_TEMPLATE = "actionRabbitTemplate";
	String RABBITMQCONFIGURATION = "rabbitmqConfiguration";
	String ACTION_CONNECTION_FACTORY = "actionConnectionFactory";
	String ACTION_CONTAINER_FACTORY = "actionContainerFactory";
	String ACTION_QUEUE = "actionQueue";
	String EXCHANGE_ACTION = "exchangeAction";
	
	String UTF_8 = "UTF-8";
	String RULE_ENGINE_CORE = "com.foxconn.core.pro.server.rule.engine.core";
	String RULE_ENGINE_KAFKA = "com.foxconn.core.pro.server.rule.engine.kafka";
	
	String ROOT = "root";
	String PRODUCT_KEY = "productkey";
	String TYPE = "type";
	String ACCESS_MODE = "accessMode";
	String SYSTEM_PARAMS= "systemParams";
	String DATA= "data";
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
	
}
