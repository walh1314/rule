/**
 * Project Name:rule-engine-core
 * File Name:OperatorAbs.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express.op
 * Date:2018年10月15日上午10:21:14
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express.op.number;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.Operator;

/**
 * ClassName:OperatorAbs <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年10月15日 上午10:21:14 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class OperatorAbs extends Operator
{

	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 8807467239996177755L;

	public OperatorAbs(String aName) {
		this.name = aName;
	}

	public OperatorAbs(String aAliasName, String aName, String aErrorInfo) {
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}
	
	@Override
	public Object executeInner(Object[] list) throws Exception
	{
		Object obj = list[0];
		if (obj == null) {
			// 对象为空，不能执行方法
			return obj;
		} else if((obj instanceof Integer || obj instanceof Long || obj instanceof Float|| obj instanceof Double || obj instanceof Byte || obj instanceof Short) == false) {
			String msg = CommonConstant.MSG_FORE;
			throw new Exception(msg + obj.getClass().getName());
		} else if(list.length > 1){
			String msg = CommonConstant.MSG_FIVE;
			throw new Exception(msg + list);
		} else if(obj instanceof Integer){
			Integer valueI = (Integer)obj;
			return Math.abs(valueI);
		} else if(obj instanceof Long){
			Long valueI = (Long)obj;
			return Math.abs(valueI);
		} else if(obj instanceof Float){
			Float valueI = (Float)obj;
			return Math.abs(valueI);
		} else if(obj instanceof Double){
			Double valueI = (Double)obj;
			return Math.abs(valueI);
		} else if(obj instanceof Byte){
			Byte valueI = (Byte)obj;
			return Math.abs(valueI);
		} else if(obj instanceof Short){
			Short valueI = (Short)obj;
			return Math.abs(valueI);
		} 
		return obj;
	}

}

