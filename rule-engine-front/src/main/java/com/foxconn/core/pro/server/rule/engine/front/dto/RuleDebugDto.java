/**
 * Project Name:rule-engine-front
 * File Name:RuleDebugDto.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.dto
 * Date:2018年9月1日上午8:50:32
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.dto;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:RuleDebugDto <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月1日 上午8:50:32 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Getter
@Setter
public class RuleDebugDto implements Serializable
{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -5276024847449585088L;
	private String fields;
	private String topic;
	private String condition;
	private JSONObject data;
}
