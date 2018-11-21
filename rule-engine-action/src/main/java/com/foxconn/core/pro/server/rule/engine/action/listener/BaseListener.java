/**
 * Project Name:rule-engine-action
 * File Name:BaseListener.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.listener
 * Date:2018年8月29日上午8:34:41
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.listener;

import com.alibaba.fastjson.JSONObject;

/**
 * 监听器,监听每个事件 ClassName:BaseListener <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月29日 上午8:34:41 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public interface BaseListener
{
	void action(JSONObject parameter ,JSONObject bean,JSONObject systemData);
}
