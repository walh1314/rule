/**
 * Project Name:rule-engine-front
 * File Name:CorsConfig.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.config
 * Date:2018年9月5日下午6:04:35
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;

/**
 * ClassName:CorsConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月5日 下午6:04:35 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Configuration
public class CorsConfig
{
	private CorsConfiguration buildConfig()
	{
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin(CommonConstant.ASTERISK); // 1
		corsConfiguration.addAllowedHeader(CommonConstant.ASTERISK); // 2
		corsConfiguration.addAllowedMethod(CommonConstant.ASTERISK); // 3
		return corsConfiguration;
	}

	@Bean
	public CorsFilter corsFilter()
	{
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(CommonConstant.DOUBLE_SLASH_ASTERISK_TWO, buildConfig()); // 4
		return new CorsFilter(source);
	}
}
