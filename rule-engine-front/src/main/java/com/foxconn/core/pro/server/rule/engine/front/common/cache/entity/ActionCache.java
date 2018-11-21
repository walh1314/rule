/**
 * Project Name:rule-engine-front
 * File Name:ActionCache.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.common.cache.entity
 * Date:2018年9月1日下午4:12:57
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.common.cache.entity;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:ActionCache <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月1日 下午4:12:57 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class ActionCache
{
	private JSONObject params;
	private String classType;
	private Date modifyTime;
	private Integer id;
	private Integer actionTypeId;
}
