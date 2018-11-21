/**
 * Project Name:rule-engine-core
 * File Name:OperatorNanvl.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.sys
 * Date:2018年10月16日下午2:01:48
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.sys;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.Operator;

/**
 * ClassName:OperatorNanvl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月16日 下午2:01:48 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorNanvl extends Operator
{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -6021531565481824269L;

	public OperatorNanvl(String aName)
	{
		this.name = aName;
	}

	public OperatorNanvl(String aAliasName, String aName, String aErrorInfo)
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
			String msg = CommonConstant.MSG_INFO;
			throw new Exception(msg + list);
		}
		Object obj1 = list[0];
		if (obj1 == null)
		{
			return list[1];
		} else {
			return obj1;
		}
	}
}
