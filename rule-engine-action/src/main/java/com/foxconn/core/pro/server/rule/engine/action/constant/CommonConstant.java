/**
 * Project Name:rule-engine-core
 * File Name:CommonConstant.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.constant
 * Date:2018年8月28日下午5:31:23
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.constant;

/**
 * ClassName:CommonConstant <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月28日 下午5:31:23 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public interface CommonConstant
{
	
	String HTTPS = "https";
	String HTTP = "http";
	String SSLV3 = "SSLv3";
	String WELL_NUMBER_SIX = "######";
	String WELL_NUMBER_FIVE = "#####";
	String DOUBLE_SLASH = "/";
	String SESSION_ID = "sessionID";
	String FIN_ALLY ="finally";
	String QUESTION_MARK ="?";
	String ADD_OPERATOR ="&";
	String COLON = ":";
	String RULE_ENGINE = "rule_engine";
	String ID = "id";
	String RULE_ENGINE_ACTION = "rule_engine_action";
	String RULE_ID = "rule_id";
	
	String MQTT_OUT_BOUND_CHANNEL = "mqttOutboundChannel";
	
	String COM_MQTT = "com.mqtt";
	
	String MESSAGE = "message";
	String POINT_TASK_CONTAINER_FACTORY = "pointTaskContainerFactory";
	String COM_RULE_ENGINE_ACTION_RABBITMQ = "com.rule.engine.action.rabbitmq";
	
	String PRODUCT_ID ="productId";
	String DEVICE_NAME = "devicename";
	String TIME_STAMP = "timestamp";
	
	String EMAIL_LISTENER = "emailListener";
	String ADDRESS = "address";
	String CONTENT = "content";
	String SUBJECT = "subject";
	String TO = "to";
	String CONTENT_TYPE = "Content-Type";
	String APPLICATION_JSON = "application/json";
	String ACCEPT = "Accept";
	String COM_EMAIL = "com.email";
	String EMAIL_ADDRESS_SPLIT = ",";
	
	String SMS_LISTENER = "smsListener";
	String SMS_NUMBERS = "numbers";
	String SMS_TO_COMMON_NUMBERS = "number";
	String SMS_NUMBERS_SPLIT = ",";
	String COM_SMS = "com.sms";
	
	String HTTP_LISTENER = "httpListener";
	String HTTP_LISTENER_URL = "url";
	String KAFKA_LISTENER = "kafkaListener";
	String MQTT_LISTENER = "mqttListener";
	
	String COMMA = ",";
	String MQTT_CRT_CA_CRT = "mqtt/crt/ca.crt";
	String MQTT_CRT_CLIENT_PFX = "mqtt/crt/client.pfx";
	String X_509 = "X.509";
	String EXPECTED_NON_EMPTY = "expected non-empty set of trusted certificates";
	String PKCS12 = "PKCS12";
	String SUNX_509 = "SunX509";
	String UNEXPECTED_DEFAULT = "Unexpected default trust managers:";
	String TLSV1 = "TLSv1";
	
	String POINT_TASKCONTAINER_FACTORY = "pointTaskContainerFactory";
	String ACTION_RABBITMQ_QUEUE = "${com.rule.engine.action.rabbitmq.queue}";
	String RABBITMQ_QUEUE_AUTO_DELETE = "${com.rule.engine.action.rabbitmq.queue-auto-delete}";
	String RABBITMQ_QUEUE_DURABLE = "${com.rule.engine.action.rabbitmq.queue-durable}";
	String ACTION_RABBITMQ_EXCLUSIVE = "${com.rule.engine.action.rabbitmq.exclusive}";
	String ACTION_RABBITMQ_EXCHANGE = "${com.rule.engine.action.rabbitmq.exchange}";
	String RABBITMQ_EXCHANGE_DURABLE = "${com.rule.engine.action.rabbitmq.exchange-durable}";
	String RABBITMQ_EXCHANGE_AUTO_DELETE = "${com.rule.engine.action.rabbitmq.exchange-auto-delete}";
	String AMQP_RECEIVEDROUTINGKEY = "amqp_receivedRoutingKey";
	String CLASSTYPE = "classType";
	String ACTIONTYPEID = "actionTypeId";
	String PARAMS = "params";
	String TOPIC = "topic";
	String WAY = "way";
	String FOXCONN = "foxconn";
	String TYPE = "type";
	String ONE =  "1";
	String ZERO = "0";
	String SQUE = "sque" ;
	String PRODUCTID = "product_id";
	
	String COM_SERVER_FRONT = "${com.server.front}";
	String COM_SERVER_FRONT_URL = "${com.server.front-url}";
	String RULEENGINE_NOTICE_REDIS = "/ruleEngine//notice/redis";
	String DATAID = "dataId";
	String PRODUCTKEY = "productKey";
	
	String THIRDPARTY_CORE_PRO_COMMON = "thirdparty.core.pro.common";
	
	String COM_SERVER = "com.server";
	
	String COM_SERVER_FRONT_PARAMS = "com.server.front-params";
	
	String TENANTID = "tenantId";
	String USERID = "userId";
	String DB = "db";
	
	String CLA_SS = "class";
	String CLASS_RESOURCE_LOADER_CLASS = "class.resource.loader.class";
	String LOADER_CLASSPATHRESOURCELOADER = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
	String R_OOT= "__root__";
	
	String NU_LL = "";
	
	String _DEVICE_NAME = "_device_name";
	String DEVICENAME = "devicename";
	String _TIMESTAMP = "_timestamp";
	String TIMESTAMP = "timestamp";
	String _PRODUCT_ID = "_product_id";
	String PRO_DUCT_ID = "product_id";
	String _ID = "_id";
	String _SQUE = "_sque";
	String ALL = "all";
	
	String QOS = "qos";
			
	
}
