/**
 * Project Name:rule-engine-core
 * File Name:OperatorDigest.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op.digest
 * Date:2018年10月16日下午12:56:55
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.digest;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.ArraySwap;
import com.ql.util.express.ExpressUtil;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.op.OperatorBase;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:OperatorDigest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月16日 下午12:56:55 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class OperatorDigest extends OperatorBase
{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 6662927157785709928L;
	private String algorithm;// suanfa

	public OperatorDigest(String aName, String algorithm)
	{
		this.name = aName;
		this.algorithm = algorithm;
	}
	
	public OperatorDigest(String aName)
	{
		this.name = aName;
	}

	public OperatorDigest(String aAliasName, String aName, String aErrorInfo)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	public OperatorDigest(String aAliasName, String aName, String aErrorInfo, String algorithm)
	{
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
		this.algorithm = algorithm;
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
		if (parameters.length == 2)
		{
			//field,String
			Object field = parameters[0];
			Object algorithm = parameters[1];
			if (field == null || algorithm == null)
			{
				// 对象为空，不能执行方法
				result = field;
			} else if ((algorithm instanceof String) == false)
			{
				//
				String msg = CommonConstant.MSG_ONE;
				throw new Exception(msg + algorithm);
			} else
			{
				String algorithmStr = (String)algorithm;
				MessageDigest messageDigest= DigestUtils.getDigest(algorithmStr);
				result = Hex.encodeHexString(messageDigest.digest(field.toString().getBytes()));
			}
		} else
		{
			String msg = CommonConstant.MSG_TWO;
			throw new Exception(msg + parameters);
		}
		if (result != null && result.getClass().equals(OperateData.class))
		{
			throw new Exception(CommonConstant.AB_THREE + this.getAliasName());
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
