/**
 * Project Name:rule-engine-front
 * File Name:InsertRuleService.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.service
 * Date:2018年8月24日下午2:41:31
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.FrontPage;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.PageResult;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.InputMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.RuleDebugDto;
import com.foxconn.core.pro.server.rule.engine.front.dto.RuleEngineDto;
import com.foxconn.core.pro.server.rule.engine.front.entity.RuleEngine;

/**
 * ClassName:InsertRuleService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月24日 下午2:41:31 <br/>
 * 
 * @author hewanwan
 * @version
 * @since JDK 1.8
 * @see
 */

public interface RuleEngineService
{

	ResultMap<? extends Object> insert(InputMap<RuleEngineDto> bean)throws Exception, JsonMappingException, IOException;

	ResultMap<? extends Object> deleteById(InputMap<RuleEngineDto> bean)throws Exception, JsonMappingException, IOException;

	ResultMap<? extends Object> selectById(InputMap<RuleEngineDto> bean)throws Exception, JsonMappingException, IOException;

	ResultMap<? extends Object> update(InputMap<RuleEngineDto> bean)throws Exception, JsonMappingException, IOException;
	
	ResultMap<? extends Object> updateStatus(InputMap<RuleEngineDto> bean)throws Exception, JsonMappingException, IOException;

	ResultMap<? extends Object> debug(InputMap<RuleDebugDto> bean)throws Exception, JsonMappingException, IOException;

	ResultMap<PageResult<RuleEngine>> selectPage(FrontPage<RuleEngine> page, InputMap<RuleEngineDto> bean)throws Exception, JsonMappingException, IOException;
	
	/**
	 * 
	 * checkRuleName: 检查ruleName的唯一性. <br/>
	 * @author liupingan
	 * @param bean
	 * @return
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws IOException
	 * @since JDK 1.8
	 */
	ResultMap<? extends Object> checkRuleName(InputMap<RuleEngineDto> bean)throws Exception, JsonMappingException, IOException;
}
