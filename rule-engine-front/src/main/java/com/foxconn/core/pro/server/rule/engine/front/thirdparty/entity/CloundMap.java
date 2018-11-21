/**
 * Project Name:rule-engine-front
 * File Name:CloundMap.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.thirdparty.entity
 * Date:2018年9月11日上午8:54:29
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.thirdparty.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:CloundMap <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月11日 上午8:54:29 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class CloundMap<T> implements Serializable
{

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 5131303692838378397L;
	private List<T> payload;
	private Integer status;
	private String errmsg;

	@JSONField(serialize = false)
	private String[] args;

}
