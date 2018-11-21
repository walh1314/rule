/**
 * Project Name:rule-engine-action
 * File Name:SMSListener.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.listener.sms
 * Date:2018年11月10日下午1:49:17
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.listener.sms;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.action.common.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.action.common.exception.ErrorCodes;
import com.foxconn.core.pro.server.rule.engine.action.common.util.HttpClientUtil;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.action.listener.BaseListener;
import com.foxconn.core.pro.server.rule.engine.action.thirdparty.constant.CoreproCommonConstant;
import com.foxconn.core.pro.server.rule.engine.action.velocity.VelocityUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:SMSListener <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年11月10日 下午1:49:17 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.6
 * @see
 */
@Component(CommonConstant.SMS_LISTENER)
@Slf4j
public class SMSListener implements BaseListener
{

	@Autowired
	private SMSProperties smsProperties;

	@Override
	public void action(JSONObject parameter, JSONObject bean, JSONObject systemData)
	{

		try
		{
			Map<String, Object> headers = init();

			String numbers = parameter.getString(CommonConstant.SMS_NUMBERS);
			//String contentTemplate = parameter.getString("content");
			//String content = VelocityUtil.commonsString(bean, contentTemplate);
			if (!StringUtils.isAnyBlank(numbers))
			{
				JSONObject body = new JSONObject();
				body.put(CommonConstant.SMS_TO_COMMON_NUMBERS, numbers.split(CommonConstant.SMS_NUMBERS_SPLIT));
				//body.put("content", content);
				HttpEntity httpEntity = HttpClientUtil.httpPost(smsProperties.getUrl(), headers, body.toJSONString());
				if (httpEntity != null)
				{
					InputStream input = httpEntity.getContent();
					StringBuffer buffer = new StringBuffer();
					buffer.append(IOUtils.toString(input, StandardCharsets.UTF_8));
					log.info("=======sms==========" + buffer);
					if (StringUtils.isBlank(buffer.toString()))
					{
						throw new BaseException(ErrorCodes.COREPRO_COMMON_SMS_FAIL);
					}
					if (buffer != null && StringUtils.isNotBlank(buffer.toString()))
					{
						JSONObject result = JSONObject.parseObject(buffer.toString());

						if (!result.containsKey(CoreproCommonConstant.STATUS))
						{
							throw new BaseException(ErrorCodes.COREPRO_COMMON_SMS_FAIL);

						}

						int status = result.getInteger(CoreproCommonConstant.STATUS);

						if (status != CoreproCommonConstant.SUCCESS)
						{
							throw new BaseException(ErrorCodes.COREPRO_COMMON_SMS_FAIL);
						}
					}
				} else
				{
					throw new BaseException(ErrorCodes.COREPRO_COMMON_SMS_FAIL);
				}
			}
		} catch (Exception e)
		{
			log.error("SMS Listener Exception:" + (parameter != null ? parameter.toJSONString() : "") + ","
					+ (bean != null ? bean.toJSONString() : ""), e);
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
