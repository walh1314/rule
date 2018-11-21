/**
 * Project Name:rule-engine-core
 * File Name:RabbitmqConfigProperties.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.config.rabbitmq
 * Date:2018年8月30日上午8:11:21
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.kafka.producer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import com.foxconn.core.pro.server.rule.engine.kafka.constant.CommonConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:RabbitmqConfigProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月30日 上午8:11:21 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component(CommonConstant.RABBITMQPROPERTIES)
@RefreshScope
@ConfigurationProperties(prefix = CommonConstant.SPRING_RABBITMQ)
@Setter
@Getter
public class RabbitmqConfigProperties
{
	private RabbitmqActionProperties action;
}
