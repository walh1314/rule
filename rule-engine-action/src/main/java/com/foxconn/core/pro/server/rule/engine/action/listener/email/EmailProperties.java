/**
 * Project Name:rule-engine-action
 * File Name:EmailProperties.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.listener.email
 * Date:2018年9月7日下午10:31:55
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.listener.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:EmailProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年9月7日 下午10:31:55 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Setter
@Getter
@Component
@RefreshScope
@ConfigurationProperties(prefix = CommonConstant.COM_EMAIL)
public class EmailProperties
{
	private String url;
}

