/**
 * Project Name:rule-engine-core
 * File Name:RuleTemplate.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.entity
 * Date:2018年8月24日上午8:22:40
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.entity;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:RuleTemplate <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月24日 上午8:22:40 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public abstract class RuleTemplate
{
	protected Integer id;

	// 头部信息
	protected String headers;

	// 执行内容
	protected String body;

	public String getQLExpress()
	{
		return (StringUtils.isEmpty(headers) ? "" : headers) + (StringUtils.isEmpty(body) ? "" : body);
	}
}
