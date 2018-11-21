/**
 * Project Name:rule-engine-core
 * File Name:ServerConfig.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.thirdparty.config
 * Date:2018年8月29日下午3:32:03
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.thirdparty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:ServerConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月29日 下午3:32:03 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = CommonConstant.COM_SERVER_FRONT_PARAMS)
@Setter
@Getter
public class ServerParamConfig
{
	// 获取账号的服务
	private String xNameSpaceCode;
	// 获取topic的服务
	private String xMicroServiceName;
	
}
