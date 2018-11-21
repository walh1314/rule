/**
 * Project Name:rule-engine-action
 * File Name:HttpListener.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.listener
 * Date:2018年8月31日上午11:42:32
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.listener;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.action.common.util.HttpClientUtil;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:HttpListener <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月31日 上午11:42:32 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component(CommonConstant.HTTP_LISTENER)
@Slf4j
public class HttpListener implements BaseListener
{

	@Override
	public void action(JSONObject parameter, JSONObject bean,JSONObject systemData)
	{
		log.info("-------------- http start --------------");
		log.info("-------------- http parameter --------------" + parameter);
		log.info("-------------- http bean --------------" + bean);
		try
		{
			// HttpClientUtil
			Map<String, Object> headers = init();
			String url = parameter.getString(CommonConstant.HTTP_LISTENER_URL);
			// String url = parameter.getString("address");
			// 进行post请求
			HttpClientUtil.httpPost(url, headers, bean.toJSONString());
			log.info("-------------- http normal end --------------");
		} catch (Exception e)
		{
			log.error("HTTP Listener Exception:" + (parameter != null ? parameter.toJSONString() : CommonConstant.NU_LL) + CommonConstant.COMMA
					+ (bean != null ? bean.toJSONString() : CommonConstant.NU_LL), e);
		}
	}

	public Map<String, Object> init()
	{
		Map<String, Object> headers = new HashMap<>();
		headers.put(CommonConstant.CONTENT_TYPE, CommonConstant.APPLICATION_JSON);
		headers.put(CommonConstant.ACCEPT, CommonConstant.APPLICATION_JSON);
		return headers;
	}
}
