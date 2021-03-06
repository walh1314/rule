/**
 * Project Name:rule-engine-core
 * File Name:RabbitmqSourceProperties.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.config.rabbitmq
 * Date:2018年8月30日上午8:11:56
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.rabbitmq.config.rabbitmq;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:RabbitmqSourceProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月30日 上午8:11:56 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class RabbitmqSourceProperties
{
	private String address;
	private String username;
	private String password;
	private String virtualHost = "";
	private String topic;
	private String queue;
	private String exchange;
	private boolean exchangeDurable = true;
	private boolean exchangeAutoDelete = false;
	private boolean queueDurable = true;
	private boolean queueAutoDelete = false;
	private boolean exclusive = false;

	private Integer defaultConcurrent = 10;
	private Integer defaultPrefetchCount = 10;
}
