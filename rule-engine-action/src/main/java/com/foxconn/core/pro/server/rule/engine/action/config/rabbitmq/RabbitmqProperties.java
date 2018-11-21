/**
 * Project Name:rule-engine-core
 * File Name:RabbitmqProperties.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.config.rabbitmq
 * Date:2018年8月28日上午8:55:10
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:RabbitmqProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月28日 上午8:55:10 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
@Component
@RefreshScope
@ConfigurationProperties(prefix = CommonConstant.COM_RULE_ENGINE_ACTION_RABBITMQ)
public class RabbitmqProperties
{
	private String topic;
	private String queue;
	private String exchange;
	private boolean exchangeDurable = true;
	private boolean exchangeAutoDelete = true;
	private boolean queueDurable = true;
	private boolean queueAutoDelete = true;
	private boolean exclusive = false;
	
	private Integer concurrent = 10;
	private Integer prefetchCount  = 50;
}
