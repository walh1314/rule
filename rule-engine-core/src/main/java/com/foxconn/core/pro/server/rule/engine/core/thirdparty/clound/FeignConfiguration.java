/**
 * Project Name:rule-engine-front
 * File Name:FeignConfiguration.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.thirdparty.clound
 * Date:2018年9月10日下午5:36:53
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.thirdparty.clound;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.Request;
import feign.Retryer;

/**
 * ClassName:FeignConfiguration <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月10日 下午5:36:53 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Configuration
public class FeignConfiguration
{
	/*
	 * @Bean public Contract useFeignAnnotations(){ return new
	 * feign.Contract.Default(); }
	 */

	public static int connectTimeOutMillis = 12000;// 超时时间
	public static int readTimeOutMillis = 12000;

	@Bean
	public Request.Options options()
	{
		return new Request.Options(connectTimeOutMillis, readTimeOutMillis);
	}

	@Bean
	public Retryer feignRetryer()
	{
		Retryer retryer = new Retryer.Default(100, 1000, 4);
		return retryer;
	}

	@Bean
	Logger.Level feignLoggerLevel()
	{
		return Logger.Level.FULL;
	}

}
