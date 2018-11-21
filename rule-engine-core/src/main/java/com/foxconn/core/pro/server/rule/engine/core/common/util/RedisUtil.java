/**
 * Project Name:rule-engine-front
 * File Name:RedisKeyUtil.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.common.util
 * Date:2018年10月8日下午4:31:59
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.common.util;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;

/**
 * ClassName:RedisKeyUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月8日 下午4:31:59 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class RedisUtil
{

	public static String getKey(String dbName, String userId, String tableName, String majorKey)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(dbName).append(CommonConstant.COLON);
		buffer.append(userId).append(CommonConstant.COLON);
		buffer.append(tableName).append(CommonConstant.COLON);
		buffer.append(majorKey).append(CommonConstant.COLON);
		return buffer.toString();
	}

	public static String getKey(String dbName, String userId, String tableName, String majorKey, String majorValue,
			String modifyTime)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(dbName).append(CommonConstant.COLON);
		buffer.append(userId).append(CommonConstant.COLON);
		buffer.append(tableName).append(CommonConstant.COLON);
		buffer.append(majorKey).append(CommonConstant.COLON);
		buffer.append(modifyTime).append(CommonConstant.COLON);
		return buffer.toString();
	}

	public static String getRuleKey(String dbName, String userId)
	{
		return getKey(dbName, userId, CommonConstant.RULE_ENGINE, CommonConstant.ID);
	}

	public static String getRuleActionKey(String dbName, String userId)
	{
		return getKey(dbName, userId, CommonConstant.RULE_ENGINE_ACTION, CommonConstant.RULE_ID);
	}

	public static String getRuleActionConditionScriptKey(String dbName, String userId)
	{
		return getKey(dbName, userId, CommonConstant.RULE_ENGINE_ACTION_CONDITION, CommonConstant.RULE_ID);
	}

	public static String getRuleActionFieldsScriptKey(String dbName, String userId)
	{
		return getKey(dbName, userId, CommonConstant.RULE_ENGINE_ACTION_FIELDS, CommonConstant.RULE_ID);
	}

}
