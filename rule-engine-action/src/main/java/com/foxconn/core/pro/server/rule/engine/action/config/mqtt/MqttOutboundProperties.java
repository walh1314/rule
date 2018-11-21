package com.foxconn.core.pro.server.rule.engine.action.config.mqtt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MqttOutboundProperties
{
	private String url;
	
	private String username;
	
	private String password;
	
	private String clientId;
	
	private String topic;
	
	private Integer qos = 1;
	
	private Integer keepLiveInterval = 10;
}
