/**
 * Project Name:rule-engine-core
 * File Name:OperatorLog.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.number
 * Date:2018年10月16日下午1:53:51
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.number;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.Operator;

/**
 * ClassName:OperatorLog <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月16日 下午1:53:51 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorLog extends Operator
{

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -6358483517065884171L;

	public OperatorLog(String aName)
	{
		this.name = aName;
	}

	public OperatorLog(String aAliasName, String aName, String aErrorInfo)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	@Override
	public Object executeInner(Object[] list) throws Exception
	{
		if (list == null || (list.length != 2 && list.length != 1))
		{
			String msg = CommonConstant.MSG_EIGHTTEEN;
			throw new Exception(msg + list);
		}
		Object obj1 = list[0];
		if (obj1 == null || ((obj1 instanceof Integer || obj1 instanceof Long || obj1 instanceof Float|| obj1 instanceof Double) == false))
		{
			String msg = CommonConstant.MSG_TEN;
			throw new Exception(msg + obj1);
		}
		Object obj2 = null;
		if(list.length == 1){
			
			return Math.log(Double.valueOf(obj1.toString()));
		} else {
			obj2 = list[1];
			if (obj2 == null || ((obj2 instanceof Integer || obj2 instanceof Long || obj2 instanceof Float|| obj2 instanceof Double) == false))
			{
				String msg = CommonConstant.MSG_ELE;
				throw new Exception(msg + obj1);
			}
			return Math.log(Double.valueOf(obj1.toString())) / Math.log(Double.valueOf(obj2.toString()));
		}
	}
}
