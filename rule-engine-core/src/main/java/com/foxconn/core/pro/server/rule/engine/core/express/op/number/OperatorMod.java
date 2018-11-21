/**
 * Project Name:rule-engine-core
 * File Name:OperatorMod.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.number
 * Date:2018年10月16日下午2:46:45
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.number;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.Operator;

/**
 * ClassName:OperatorMod <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月16日 下午2:46:45 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorMod extends Operator
{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -6534878054224902788L;

	public OperatorMod(String aName)
	{
		this.name = aName;
	}

	public OperatorMod(String aAliasName, String aName, String aErrorInfo)
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
		if (obj1 == null || ((obj1 instanceof Integer || obj1 instanceof Long) == false))
		{
			String msg = CommonConstant.MSG_FIFTTEEN;
			throw new Exception(msg + obj1);
		}

		if (obj2 == null || ((obj2 instanceof Integer || obj2 instanceof Long) == false))
		{
			String msg = CommonConstant.MSG_SIXTEEN;
			throw new Exception(msg + obj2);
		}

		Long n = Long.valueOf(obj1.toString());
		Long m =  Long.valueOf(obj2.toString());
		if (m == 0)
		{
			return null;
		}
		return Math.floorMod(n, m);
	}

}
