/**
 * Project Name:rule-engine-core
 * File Name:OperatorCrypto.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.sys
 * Date:2018年10月15日下午2:43:04
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.sys;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.ArraySwap;
import com.ql.util.express.ExpressUtil;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.op.OperatorBase;

/**
 * ClassName:OperatorCrypto <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年10月15日 下午2:43:04 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class OperatorCrypto extends OperatorBase
{

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 1257863637042392443L;

	public OperatorCrypto(String aName)
	{
		this.name = aName;
	}

	public OperatorCrypto(String aAliasName, String aName, String aErrorInfo)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	public OperateData executeInner(InstructionSetContext context, ArraySwap list) throws Exception
	{
		Object[] parameters = new Object[list.length];
		for (int i = 0; i < list.length; i++)
		{
			parameters[i] = list.get(i).getObject(context);
		}
		Object result = null;
		if (parameters.length == 2)
		{
			Object split = parameters[0];
			StringBuilder build = new StringBuilder();
			for (int i = 1; i < parameters.length; i++)
			{
				if (i == 1)
				{
					build.append(parameters[i]);
				} else
				{
					build.append(split);
					build.append(parameters[i]);
				}
			}
			result = (build == null ? null : build.toString());
		} else
		{
			String msg = CommonConstant.MSG_TWO;
			throw new Exception(msg + parameters);
		}
		if (result != null && result.getClass().equals(OperateData.class))
		{
			throw new Exception(CommonConstant.MSG_THREE + this.getAliasName());
		}
		if (result == null)
		{
			return OperateDataCacheManager.fetchOperateData(null, null);
		} else
		{
			return OperateDataCacheManager.fetchOperateData(result, ExpressUtil.getSimpleDataType(result.getClass()));
		}
	}

}

