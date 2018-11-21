/**
 * Project Name:rule-engine-core
 * File Name:CoreproCommonService.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.thirdparty.common.service
 * Date:2018年8月30日上午10:39:36
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.thirdparty.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.action.common.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.action.common.exception.ErrorCodes;
import com.foxconn.core.pro.server.rule.engine.action.common.util.HttpClientUtil;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.action.thirdparty.config.CoreproCommonProperties;
import com.foxconn.core.pro.server.rule.engine.action.thirdparty.constant.CoreproCommonConstant;
import com.foxconn.core.pro.server.rule.engine.action.thirdparty.entity.UserBean;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:CoreproCommonService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月30日 上午10:39:36 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component
@Slf4j
public class CoreproCommonService
{
	private Map<String, Object> headers;

	@Autowired
	private CoreproCommonProperties coreproCommonProperties;

	public CoreproCommonService()
	{

	}

	public CoreproCommonService(Map<String, Object> headers)
	{
		this.headers = headers;
	}

	@PostConstruct
	public void init()
	{
		headers = new HashMap<>();
		headers.put(CommonConstant.CONTENT_TYPE, CommonConstant.APPLICATION_JSON);
		headers.put(CommonConstant.ACCEPT, CommonConstant.APPLICATION_JSON);

	}

	public List<String> getTopic(String dataId,String type)
	{
		String url = coreproCommonProperties.getTopicUrl();
		Map<String,Object> params = new HashMap<>(1);
		params.put(CommonConstant.DATAID, dataId);
		params.put(CommonConstant.TYPE, type);
		HttpEntity httpEntity = HttpClientUtil.httpGet(url,params, headers);
		if (httpEntity == null)
		{
			throw new BaseException(ErrorCodes.COREPRO_COMMON_TOPIC_EMPTY);
		}
		try
		{
			InputStream input = httpEntity.getContent();
			StringBuffer buffer = new StringBuffer();
			buffer.append(IOUtils.toString(input,StandardCharsets.UTF_8));
			log.info("=======getTopic=========="+buffer);
			if (StringUtils.isBlank(buffer.toString()))
			{
				throw new BaseException(ErrorCodes.COREPRO_COMMON_TOPIC_EMPTY);
			}
			if(buffer!= null && StringUtils.isNotBlank(buffer.toString())){
				JSONObject result  = JSONObject.parseObject(buffer.toString());
				
				if(!result.containsKey(CoreproCommonConstant.STATUS)){
					throw new BaseException(ErrorCodes.COREPRO_COMMON_TOPIC_EMPTY);
					
				}
				
				int status = result.getInteger(CoreproCommonConstant.STATUS);
				
				if(status != CoreproCommonConstant.SUCCESS){
					throw new BaseException(ErrorCodes.COREPRO_COMMON_TOPIC_EMPTY);
				}
				
				if(!result.containsKey(CoreproCommonConstant.PAYLOAD)){
					throw new BaseException(ErrorCodes.COREPRO_COMMON_TOPIC_EMPTY);
				}
				
				List<String> beans = JSONObject.parseArray(result.getJSONArray(CoreproCommonConstant.PAYLOAD).toJSONString(), String.class);
				if(beans == null || beans.size() == 0){
					return null;
				}
				return beans;
			}
			return null;
		} catch (UnsupportedOperationException e)
		{
			log.error(e.getMessage());
		} catch (IOException e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	public List<UserBean> getUserInfo(String productKey)
	{
		String url = coreproCommonProperties.getUerInfoUrl();
		Map<String,Object> params = new HashMap<>(1);
		params.put(CommonConstant.PRODUCTKEY, productKey);
		HttpEntity httpEntity = HttpClientUtil.httpGet(url,params, headers);
		if (httpEntity == null)
		{
			throw new BaseException(ErrorCodes.COREPRO_COMMON_USERID_EMPTY);
		}
		try
		{
			InputStream input = httpEntity.getContent();
			StringBuffer buffer = new StringBuffer();
			buffer.append(IOUtils.toString(input,StandardCharsets.UTF_8));
			log.info("=======getUserInfo=========="+buffer);
			if (StringUtils.isNotBlank(buffer.toString()))
			{
				throw new BaseException(ErrorCodes.COREPRO_COMMON_USERID_EMPTY);
			}
			if(buffer!= null && StringUtils.isNotBlank(buffer.toString())){
				JSONObject result  = JSONObject.parseObject(buffer.toString());
				
				if(!result.containsKey(CoreproCommonConstant.STATUS)){
					throw new BaseException(ErrorCodes.COREPRO_COMMON_USERID_EMPTY);
				}
				
				int status = result.getInteger(CoreproCommonConstant.STATUS);
				
				if(status != CoreproCommonConstant.SUCCESS){
					throw new BaseException(ErrorCodes.COREPRO_COMMON_USERID_EMPTY);
				}
				
				if(!result.containsKey(CoreproCommonConstant.PAYLOAD)){
					throw new BaseException(ErrorCodes.COREPRO_COMMON_USERID_EMPTY);
				}
				List<UserBean> beans = JSONObject.parseArray(result.getJSONArray(CoreproCommonConstant.PAYLOAD).toJSONString(), UserBean.class);
				if(beans == null || beans.size() == 0){
					return null;
				}
				return beans;
			}
			
			return null;
		} catch (UnsupportedOperationException e)
		{
			log.error(e.getMessage());
		} catch (IOException e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
}
