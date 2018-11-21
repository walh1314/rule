/**
 * Project Name:rule-engine-core
 * File Name:OperatorEndWith.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.string
 * Date:2018年10月16日下午1:31:28
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.string;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.ArraySwap;
import com.ql.util.express.ExpressUtil;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.op.OperatorBase;

/**
 * ClassName:OperatorEndWith <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月16日 下午1:31:28 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorEndWith extends OperatorBase
{

	/**
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -3713041605080468470L;

	public OperatorEndWith(String aName)
	{
		this.name = aName;
	}

	public OperatorEndWith(String aAliasName, String aName, String aErrorInfo)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	@Override
	public OperateData executeInner(InstructionSetContext context, ArraySwap list) throws Exception
	{

		Object[] parameters = new Object[list.length];
		for (int i = 0; i < list.length; i++)
		{
			parameters[i] = list.get(i).getObject(context);
		}
		
		Object result = Boolean.FALSE;
		if (parameters.length == 2)
		{
			// field,String
			Object input = parameters[0];
			Object suffix = parameters[1];
			if (input == null || suffix == null)
			{
				// 对象为空，不能执行方法
				result = Boolean.FALSE;
			} else if ((suffix instanceof String) == false)
			{
				//
				String msg = CommonConstant.MSG_NIGHTTEEN;
				throw new Exception(msg + suffix);
			} else
			{
				String suffixStr = (String) suffix;
				if (input.toString().endsWith(suffixStr))
				{
					result = Boolean.TRUE;
				}
			}
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