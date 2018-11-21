/**
 * Project Name:rule-engine-front
 * File Name:ActionTypeService.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.service
 * Date:2018年8月31日下午4:22:21
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.service;

import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.ActionTypeDto;
import com.foxconn.core.pro.server.rule.engine.front.entity.ActionType;

/**
 * ClassName:ActionTypeService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月31日 下午4:22:21 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public interface ActionTypeService
{
	ActionType selectById(Integer id);

	ResultMap<? extends Object> selectByMaps(ActionTypeDto bean);
}

