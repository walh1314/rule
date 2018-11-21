/**
 * Project Name:rule-engine-front
 * File Name:VerifySqlLegitimacyService1.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.service
 * Date:2018年9月14日上午10:44:52
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.service;

import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.ErrorMessage;
import com.foxconn.core.pro.server.rule.engine.front.dto.InputMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.RuleCheckDto;

/**
 * ClassName:VerifySqlLegitimacyService1 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月14日 上午10:44:52 <br/>
 * 
 * @author hewanwan
 * @version
 * @since JDK 1.8
 * @see
 */
public interface VerifySqlLegitimacyService
{
	public ResultMap<ErrorMessage> verifyField(String sql);

	public ResultMap<ErrorMessage> verifyCondition(String sql);

	public ResultMap<ErrorMessage> verifyField(InputMap<RuleCheckDto> bean);

	public ResultMap<ErrorMessage> verifyCondition(InputMap<RuleCheckDto> bean);

	public ResultMap<ErrorMessage> verifySql(String field, String condition);
}
