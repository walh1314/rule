/**
 * Project Name:rule-engine-front
 * File Name:ActionsService.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.service
 * Date:2018年8月28日下午4:09:10
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.ActionsDto;
import com.foxconn.core.pro.server.rule.engine.front.dto.InputMap;

/**
 * ClassName:ActionsService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月28日 下午4:09:10 <br/>
 * @author   hewanwan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public interface ActionsService
{
	
	ResultMap<? extends Object> insert(InputMap<ActionsDto> bean)throws Exception, JsonMappingException, IOException;

	ResultMap<? extends Object> deleteById(InputMap<ActionsDto> bean)throws Exception, JsonMappingException, IOException;

	ResultMap<? extends Object> selectById(InputMap<ActionsDto> bean)throws Exception, JsonMappingException, IOException;

	ResultMap<? extends Object> update(InputMap<ActionsDto> bean)throws Exception, JsonMappingException, IOException;
	
}

