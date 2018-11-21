/**
 * Project Name:rule-engine-front
 * File Name:ActionsDto.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.dto
 * Date:2018年8月28日下午2:37:15
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.dto;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:ActionsDto <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月28日 下午2:37:15 <br/>
 * @author   hewanwan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Setter
@Getter
public class ActionsDto
{
	private Integer id;
	/**行为类型id */
	private Integer actionTypeId;
	/**行为参数 */
	private JSONObject actionParam;
	
	private Integer ruleId;
	
	private String name;
	
}

