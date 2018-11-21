/**
 * Project Name:rule-engine-core
 * File Name:IRuleService.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.service
 * Date:2018年8月23日下午5:20:25
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.rabbitmq.service;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.MqttData;

/**
 * ClassName:IRuleService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月23日 下午5:20:25 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public interface IRuleService
{
	public void excute(MqttData data) throws Exception;

	public ResultMap<? extends Object> debug(JSONObject data);
}
