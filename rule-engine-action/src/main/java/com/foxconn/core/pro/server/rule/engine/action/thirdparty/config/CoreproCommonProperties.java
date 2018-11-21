/**
 * Project Name:rule-engine-core
 * File Name:CoreproCommonProperties.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.thirdparty.config
 * Date:2018年8月30日上午10:54:56
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.thirdparty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:CoreproCommonProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月30日 上午10:54:56 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = CommonConstant.THIRDPARTY_CORE_PRO_COMMON)
@Setter
@Getter
public class CoreproCommonProperties
{
	String uerInfoUrl;
	String topicUrl;
}
