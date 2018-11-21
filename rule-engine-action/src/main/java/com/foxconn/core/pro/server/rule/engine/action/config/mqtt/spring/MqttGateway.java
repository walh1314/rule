package com.foxconn.core.pro.server.rule.engine.action.config.mqtt.spring;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

@MessagingGateway(defaultRequestChannel = CommonConstant.MQTT_OUT_BOUND_CHANNEL)
public interface MqttGateway
{
	void sendToMqtt(String data);

	void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);

	void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
}