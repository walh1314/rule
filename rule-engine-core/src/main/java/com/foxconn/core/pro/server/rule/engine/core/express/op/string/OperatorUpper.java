/**
 * Project Name:rule-engine-core
 * File Name:OperatorUpper.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op
 * Date:2018年10月15日上午11:01:58
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.string;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.Operator;

/**
 * ClassName:OperatorUpper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月15日 上午11:01:58 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorUpper extends Operator
{

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -5898096870149517808L;

	public OperatorUpper(String aName)
	{
		this.name = aName;
	}

	public OperatorUpper(String aAliasName, String aName, String aErrorInfo)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	@Override
	public Object executeInner(Object[] list) throws Exception
	{
		Object obj = list[0];
		if (obj == null)
		{
			// 对象为空，不能执行方法
			return obj;
		} else if ((obj instanceof Number || obj instanceof String) == false)
		{
			String msg = CommonConstant.MSG_UPPER;
			throw new Exception(msg + obj.getClass().getName());
		} else if (list.length > 1)
		{
			String msg = CommonConstant.MSG_FIVE;
			throw new Exception(msg + list);
		}
		return String.valueOf(obj).toUpperCase();
	}

}
