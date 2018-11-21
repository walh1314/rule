/**
 * Project Name:rule-engine-core
 * File Name:Rule.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.entity
 * Date:2018年8月23日下午3:00:06
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:Rule <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月23日 下午3:00:06 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class Rule implements Serializable
{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -5509230317615494239L;

	private Integer id;

	private String ruleName;

	// 规则匹配判断条件-表达式
	private String condition;
	
	// 规则匹配判断条件-表达式
	private String topic;

	// 规则匹配执行内容-表达式
	private String field;
	
	//缓存时间,如果时间相等,则表示已经进行过模板生成,否则进行模板加载
	private Long modifyTime;
	
	//版本
	private Integer version;
	//接入方式
	private Integer accessMode;
	
}
