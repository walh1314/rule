/**
 * Project Name:rule-engine-front
 * File Name:RuleCache.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.common.cache.entity
 * Date:2018年9月1日下午2:59:44
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.common.cache.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:RuleCache <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月1日 下午2:59:44 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class RuleCache implements Serializable
{
	private static final long serialVersionUID = -5509230317615494239L;

	private Integer id;

	private String ruleName;

	// 规则匹配判断条件-表达式
	private String condition;

	// 规则匹配判断条件-表达式
	private String topic;

	// 规则匹配执行内容-表达式
	private String field;

	// 缓存时间,如果时间相等,则表示已经进行过模板生成,否则进行模板加载
	private Date modifyTime;
	
	//版本
	private Integer version;
	
	//接入方式
	private Integer accessMode;
}
