/**
 * Project Name:rule-engine-core
 * File Name:OperatorReplace.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.string
 * Date:2018年10月16日下午2:59:35
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.string;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.Operator;

/**
 * ClassName:OperatorReplace <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年10月16日 下午2:59:35 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class OperatorReplace extends Operator
{

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 5547567657229695070L;

	public OperatorReplace(String aName)
	{
		this.name = aName;
	}

	public OperatorReplace(String aAliasName, String aName, String aErrorInfo)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	@Override
	public Object executeInner(Object[] list) throws Exception
	{
		if (list == null || list.length != 3)
		{
			String msg = CommonConstant.MSG_THREESTR;
			throw new Exception(msg + list);
		}
		Object source = list[0];
		Object substring = list[1];
		Object replacement = list[2];
		//source, substring, replacement
		if (source == null || substring == null || replacement == null)
		{
			return source;
		}
		return source.toString().replaceAll(substring.toString(), replacement.toString());
	}

}

