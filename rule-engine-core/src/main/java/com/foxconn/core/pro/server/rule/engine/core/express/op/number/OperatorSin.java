/**
 * Project Name:rule-engine-core
 * File Name:OperatorSin.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.number
 * Date:2018年10月16日下午3:14:17
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.number;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.ArraySwap;
import com.ql.util.express.ExpressUtil;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.op.OperatorBase;

/**
 * ClassName:OperatorSin <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月16日 下午3:14:17 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorSin extends OperatorBase
{
	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -886071383285493763L;

	public OperatorSin(String aName)
	{
		this.name = aName;
	}

	public OperatorSin(String aAliasName, String aName, String aErrorInfo)
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
		Object result = null;
		if (parameters.length == 1)
		{
			Object obj = parameters[0];
			if (obj == null)
			{
				// 对象为空，不能执行方法
				result = obj;
			} else if ((obj instanceof Integer || obj instanceof Long || obj instanceof Float || obj instanceof Double
					|| obj instanceof Byte || obj instanceof Short) == false)
			{
				String msg = CommonConstant.MSG_THR+this.name+CommonConstant.MSG_TWE;
				throw new Exception(msg + obj.getClass().getName());
			} else
			{
				Double valueI = Double.valueOf(obj.toString());
				result = Math.sin(valueI);
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
