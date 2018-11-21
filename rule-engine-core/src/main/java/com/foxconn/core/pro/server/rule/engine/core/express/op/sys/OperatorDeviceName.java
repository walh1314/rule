/**
 * Project Name:rule-engine-core
 * File Name:OperatorDeviceName.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op
 * Date:2018年10月15日上午11:39:20
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
 * ClassName:OperatorDeviceName <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月15日 上午11:39:20 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class OperatorDeviceName extends OperatorBase
{
	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 4819002555000852244L;

	public OperatorDeviceName(String aName)
	{
		this.name = aName;
	}

	public OperatorDeviceName(String aAliasName, String aName, String aErrorInfo)
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
		if (parameters.length == 0)
		{
			if(context.getParent() != null){
				result = context.getParent().get(CommonConstant.QL_DEVICE_NAME);
			} else {
				result = null;
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