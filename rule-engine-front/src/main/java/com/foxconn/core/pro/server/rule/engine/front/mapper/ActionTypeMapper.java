/**
 * Project Name:rule-engine-front
 * File Name:ActionTypeMapper.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.mapper
 * Date:2018年8月31日下午4:25:12
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.mapper;

import java.util.List;
import java.util.Map;

import com.foxconn.core.pro.server.rule.engine.front.entity.ActionType;

/**
 * ClassName:ActionTypeMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月31日 下午4:25:12 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public interface ActionTypeMapper
{
	ActionType selectById(Integer id);
	
	String selecNametById(Map<String,Object> map);
	
	List<ActionType> selectByMap(Map<String,Object> map);
	
	List<ActionType> selectAll();
}

