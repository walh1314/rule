/**
 * Project Name:rule-engine-core
 * File Name:OperatorTimestamp.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op
 * Date:2018年10月15日上午11:36:50
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.sys;

import java.text.SimpleDateFormat;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.ArraySwap;
import com.ql.util.express.ExpressUtil;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.op.OperatorBase;

/**
 * ClassName:OperatorTimestamp <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年10月15日 上午11:36:50 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class OperatorTimestamp extends OperatorBase
{

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 5315542040305170161L;

	public OperatorTimestamp(String aName)
	{
		this.name = aName;
	}

	public OperatorTimestamp(String aAliasName, String aName, String aErrorInfo)
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
			//返回毫秒数
			result = System.currentTimeMillis();
		} else if (parameters.length == 1 && parameters[0] != null && (parameters[0] instanceof String))
		{
			//返回毫秒数
			SimpleDateFormat time=new SimpleDateFormat(parameters[0].toString()); 
			result = time.format(System.currentTimeMillis());
		} else {
			throw new Exception(CommonConstant.MSG_ERR);
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

