/**
 * Project Name:rule-engine-core
 * File Name:OperatorPower.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.number
 * Date:2018年10月16日下午3:06:37
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.number;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.Operator;

/**
 * ClassName:OperatorPower <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月16日 下午3:06:37 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorPower extends Operator
{

	/**
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -3233220268330505526L;

	public OperatorPower(String aName)
	{
		this.name = aName;
	}

	public OperatorPower(String aAliasName, String aName, String aErrorInfo)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	@Override
	public Object executeInner(Object[] list) throws Exception
	{
		if (list == null || list.length != 2)
		{
			String msg = CommonConstant.MSG_EIGHT;
			throw new Exception(msg + list);
		}
		Object obj1 = list[0];
		Object obj2 = list[1];
		if (obj1 == null || ((obj1 instanceof Integer || obj1 instanceof Long || obj2 instanceof Double
				|| obj2 instanceof Float) == false))
		{
			String msg = CommonConstant.MSG_TEN;
			throw new Exception(msg + obj1);
		}

		if (obj2 == null || ((obj2 instanceof Integer || obj2 instanceof Long || obj2 instanceof Double
				|| obj2 instanceof Float) == false))
		{
			String msg = CommonConstant.MSG_ELE;
			throw new Exception(msg + obj2);
		}
		Double n = Double.valueOf(obj1.toString());
		Double m = Double.valueOf(obj2.toString());
		return Math.pow(n, m);
	}

}
