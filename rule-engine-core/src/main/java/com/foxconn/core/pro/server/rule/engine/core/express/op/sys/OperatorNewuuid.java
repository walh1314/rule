/**
 * Project Name:rule-engine-core
 * File Name:OperatorNewuuid.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.sys
 * Date:2018年10月16日下午2:52:24
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.sys;

import java.util.UUID;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.ArraySwap;
import com.ql.util.express.ExpressUtil;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.op.OperatorBase;

/**
 * ClassName:OperatorNewuuid <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月16日 下午2:52:24 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorNewuuid extends OperatorBase
{

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -3953532108863817348L;

	public OperatorNewuuid(String aName)
	{
		this.name = aName;
	}

	public OperatorNewuuid(String aAliasName, String aName, String aErrorInfo)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	public OperateData executeInner(InstructionSetContext context, ArraySwap list) throws Exception
	{

		Object result = null;
		if (list == null || list.length == 0)
		{
			result = UUID.randomUUID().toString().replace(CommonConstant.LINE, "").toLowerCase();
		} else
		{
			Object[] parameters = new Object[list.length];
			for (int i = 0; i < list.length; i++)
			{
				parameters[i] = list.get(i).getObject(context);
			}
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
