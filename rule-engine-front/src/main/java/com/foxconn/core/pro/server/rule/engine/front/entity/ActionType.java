/**
 * Project Name:rule-engine-front
 * File Name:ActionType.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.entity
 * Date:2018年8月31日下午4:15:20
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:ActionType <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月31日 下午4:15:20 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
@Slf4j
public class ActionType
{
	@NotNull
	private Integer id;
	@NotNull
	private String name;
	private String desc;
	private String creator;
	private Date createTime;
	private String modifier;
	private Date modifyTime;
	private String classType;
	private JSONObject defaultParams;
	private String defaultParamsString;

	public void setDefaultParamsString(String defaultParamsString)
	{
		this.defaultParamsString = defaultParamsString;
		try
		{
			if (defaultParamsString != null && !StringUtils.isEmpty(defaultParamsString))
			{
				JSONObject json = JSONObject.parseObject(defaultParamsString);
				// 调用setStar方法
				setDefaultParams(json);
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}
}
