/**
 * Project Name:rule-engine-core
 * File Name:CoreproCommonService.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.thirdparty.common.service
 * Date:2018年8月30日上午10:39:36
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.thirdparty.common.service;

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
import com.foxconn.core.pro.server.rule.engine.core.common.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.core.common.exception.ErrorCodes;
import com.foxconn.core.pro.server.rule.engine.core.common.util.HttpClientUtil;
import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.config.CoreproCommonProperties;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.constant.CoreproCommonConstant;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.UserBean;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.UserDataBaseBean;

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

	public List<String> getTopic(String dataId)
	{
		String url = coreproCommonProperties.getTopicUrl();
		Map<String,Object> params = new HashMap<>(1);
		params.put(CommonConstant.DATA_ID, dataId);
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
			log.debug("=======getTopic=========="+buffer);
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
				List<String> beans = JSONObject.parseArray(result.getJSONObject(CoreproCommonConstant.PAYLOAD).toJSONString(), String.class);
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

	public List<UserBean> getUserInfo(String productKey,String deviceName)
	{
		String url = coreproCommonProperties.getUerInfoUrl();
		Map<String,Object> params = new HashMap<>(1);
		params.put(CommonConstant.PRODUCT_KEY, productKey);
		params.put(CommonConstant.DEVICE_NAME, deviceName);
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
			log.debug("=======getUserInfo=========="+buffer);
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
	
	/**
	 * 
	 * getUserDataBase:(根据用户id获取用户数据库). <br/>
	 * @author liupingan
	 * @param userId
	 * @return
	 * @since JDK 1.8
	 */
	public UserDataBaseBean getUserDataBase(String userId)
	{
		String url = coreproCommonProperties.getUserDatabaseUrl();
		Map<String,Object> params = new HashMap<>(1);
		params.put(CommonConstant.USERID, userId);
		HttpEntity httpEntity = HttpClientUtil.httpGet(url,params, headers);
		if (httpEntity == null)
		{
			throw new BaseException(ErrorCodes.COREPRO_COMMON_USER_DATA_BASE_EMPTY);
		}
		try
		{
			InputStream input = httpEntity.getContent();
			StringBuffer buffer = new StringBuffer();
			buffer.append(IOUtils.toString(input,StandardCharsets.UTF_8));
			log.debug("=======getUserDataBase=========="+buffer);
			if(buffer!= null && StringUtils.isNotBlank(buffer.toString())){
				JSONObject result  = JSONObject.parseObject(buffer.toString());
				
				if(!result.containsKey(CoreproCommonConstant.STATUS)){
					throw new BaseException(ErrorCodes.COREPRO_COMMON_USER_DATA_BASE_EMPTY);
				}
				
				int status = result.getInteger(CoreproCommonConstant.STATUS);
				
				if(status != CoreproCommonConstant.SUCCESS){
					throw new BaseException(ErrorCodes.COREPRO_COMMON_USER_DATA_BASE_EMPTY);
				}
				
				if(!result.containsKey(CoreproCommonConstant.PAYLOAD)){
					throw new BaseException(ErrorCodes.COREPRO_COMMON_USER_DATA_BASE_EMPTY);
				}
				UserDataBaseBean bean = JSONObject.parseObject(result.getJSONObject(CoreproCommonConstant.PAYLOAD).toJSONString(), UserDataBaseBean.class);
				if(bean == null ){
					return null;
				}
				return bean;
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
