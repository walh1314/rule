/**
 * Project Name:rule-engine-action
 * File Name:SMSlProperties.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.listener.sms
 * Date:2018年11月10日下午1:49:41
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.listener.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:SMSlProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年11月10日 下午1:49:41 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
@Setter
@Getter
@Component
@RefreshScope
@ConfigurationProperties(prefix = CommonConstant.COM_SMS)
public class SMSProperties
{
	private String url;
}

