/**
 * Project Name:rule-engine-action
 * File Name:EmailListener.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.listener.email
 * Date:2018年9月7日下午2:15:39
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.listener.email;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
 * ClassName:EmailListener <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月7日 下午2:15:39 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component(CommonConstant.EMAIL_LISTENER)
@Slf4j
public class EmailListener implements BaseListener
{

	@Autowired
	private EmailProperties emailProperties;

	@Override
	public void action(JSONObject parameter, JSONObject bean,JSONObject systemData)
	{
		try
		{
			// HttpClientUtil
			Map<String, Object> headers = init();

			String address = parameter.getString(CommonConstant.ADDRESS);
			String contentTemplate = parameter.getString(CommonConstant.CONTENT);
			String content = VelocityUtil.commonsString(bean, contentTemplate);
			String subjectTemplate = parameter.getString(CommonConstant.SUBJECT);
			String subject = VelocityUtil.commonsString(bean, subjectTemplate);
			if (!StringUtils.isAnyBlank(subject))
			{
				JSONObject body = new JSONObject();
				body.put(CommonConstant.TO, StringUtils.isNotBlank(address)?address.split(CommonConstant.EMAIL_ADDRESS_SPLIT):(new ArrayList<String>(1)));
				body.put(CommonConstant.CONTENT, content);
				body.put(CommonConstant.SUBJECT, subject);
				HttpEntity httpEntity = HttpClientUtil.httpPost(emailProperties.getUrl(), headers, body.toJSONString());
				if (httpEntity != null)
				{
					InputStream input = httpEntity.getContent();
					StringBuffer buffer = new StringBuffer();
					buffer.append(IOUtils.toString(input, StandardCharsets.UTF_8));
					log.info("=======email==========" + buffer);
					if (StringUtils.isBlank(buffer.toString()))
					{
						throw new BaseException(ErrorCodes.COREPRO_COMMON_EMAIL_FAIL);
					}
					if (buffer != null && StringUtils.isNotBlank(buffer.toString()))
					{
						JSONObject result = JSONObject.parseObject(buffer.toString());

						if (!result.containsKey(CoreproCommonConstant.STATUS))
						{
							throw new BaseException(ErrorCodes.COREPRO_COMMON_EMAIL_FAIL);

						}

						int status = result.getInteger(CoreproCommonConstant.STATUS);

						if (status != CoreproCommonConstant.SUCCESS)
						{
							throw new BaseException(ErrorCodes.COREPRO_COMMON_EMAIL_FAIL);
						}
					}
				} else
				{
					throw new BaseException(ErrorCodes.COREPRO_COMMON_EMAIL_FAIL);
				}
			}
		} catch (Exception e)
		{
			log.error("HTTP Listener Exception:" + (parameter != null ? parameter.toJSONString() : "") + ","
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
