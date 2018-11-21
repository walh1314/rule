/**
 * Project Name:rule-engine-front
 * File Name:RuleEngineAddMapper.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.mapper
 * Date:2018年8月25日上午8:56:33
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.mapper;

import java.util.List;
import java.util.Map;

import com.foxconn.core.pro.server.rule.engine.front.entity.RuleEngine;

/**
 * ClassName:RuleEngineAddMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月25日 上午8:56:33 <br/>
 * @author   hewanwan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public interface RuleEngineMapper
{
	Integer insert(RuleEngine ruleEngineAdd);
	
	Integer deleteById(Integer id);
	
	Integer updateById(RuleEngine ruleEngineAdd);
	
	RuleEngine selectById(Integer id);
	
	List<RuleEngine> selectByMap(Map<String, Object> map);
	
	List<RuleEngine> selectByMapOrderbyCreator(Map<String, Object> map);
	
	
}

