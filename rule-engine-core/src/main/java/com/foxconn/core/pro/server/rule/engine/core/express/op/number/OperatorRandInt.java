/**
 * Project Name:rule-engine-core
 * File Name:OperatorRandInt.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op
 * Date:2018年10月15日上午11:08:45
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.number;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.Operator;

/**
 * ClassName:OperatorRandInt <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月15日 上午11:08:45 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorRandInt extends Operator
{

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -720846976699714115L;

	public OperatorRandInt(String aName)
	{
		this.name = aName;
	}

	public OperatorRandInt(String aAliasName, String aName, String aErrorInfo)
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
		Long min = Long.valueOf( obj1.toString());
		Long max =  Long.valueOf( obj2.toString());
		if (min >= max)
		{
			String msg = CommonConstant.MSG_SEVENTEEN;
			throw new Exception(msg + list);
		}
		return min + (int) (Math.random() * (max - min + 1));
	}
}
