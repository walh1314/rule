/**
 * Project Name:rule-engine-front
 * File Name:InputMap.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.dto
 * Date:2018年9月5日下午11:23:41
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.dto;

import java.io.Serializable;

import com.foxconn.core.pro.server.rule.engine.front.entity.UserInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:InputMap <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月5日 下午11:23:41 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class InputMap<T> implements Serializable
{

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 75536758518744938L;
	private UserInfo config;
	private T data;
}
