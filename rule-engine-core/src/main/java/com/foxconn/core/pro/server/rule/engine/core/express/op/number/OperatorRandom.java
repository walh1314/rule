/**
 * Project Name:rule-engine-core
 * File Name:OperatorRandom.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op
 * Date:2018年10月15日上午11:06:22
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.number;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.Operator;

/**
 * ClassName:OperatorRandom <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年10月15日 上午11:06:22 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class OperatorRandom extends Operator
{

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -3812739332479929148L;

	public OperatorRandom(String aName)
	{
		this.name = aName;
	}

	public OperatorRandom(String aAliasName, String aName, String aErrorInfo)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	@Override
	public Object executeInner(Object[] list) throws Exception
	{
		if(list != null && list.length>0){
			String msg = CommonConstant.MSG_FORETEEN;
			throw new Exception(msg + list);
		}
		return Math.random();
	}

}

