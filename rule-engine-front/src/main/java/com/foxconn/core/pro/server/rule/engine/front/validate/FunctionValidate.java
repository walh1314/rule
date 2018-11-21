/**
 * Project Name:rule-engine-front
 * File Name:FunctionValidate.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.validate
 * Date:2018年10月22日上午10:50:29
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.validate;

import java.util.Iterator;
import java.util.List;

import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.ErrorMessage;
import com.foxconn.core.pro.server.rule.engine.front.entity.FunctionValidateBean;
import com.foxconn.core.pro.server.rule.engine.front.entity.Parameter;
import com.foxconn.core.pro.server.rule.engine.front.exception.ErrorCodes;
import com.foxconn.core.pro.server.rule.engine.front.service.impl.VerifySqlLegitimacyServiceImpl;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

/**
 * ClassName:FunctionValidate <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月22日 上午10:50:29 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public interface FunctionValidate
{
	/**
	 * 
	 * validate:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author liupingan
	 * @param functionValidateBean
	 * @param list
	 * @param result
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @since JDK 1.8
	 */
	default boolean validate(FunctionValidateBean functionValidateBean, Function expression, ExpressionList list,
			ResultMap<ErrorMessage> result, VerifySqlLegitimacyServiceImpl verifySqlLegitimacyService,
			StringBuilder buffer,boolean isAdd) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		List<Parameter> params = functionValidateBean.getParams();

		if ((params == null || params.isEmpty())
				&& (list == null || list.getExpressions() == null || list.getExpressions().isEmpty()))
		{
			return true;
		} else if ((list == null || list.getExpressions() == null || list.getExpressions().isEmpty())
				&& (params != null && params.size() > 0))
		{
			Iterator<Parameter> iter = params.iterator();
			Parameter parameter = null;
			while (iter.hasNext())
			{ // 执行过程中会执行数据锁定，性能稍差，若在
				parameter = iter.next();
				if(parameter.getNum() == -1 || parameter.getNum() == 0){
					continue;
				}
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setStartPosition(buffer.length() - expression.toString().length());
				errorMessage.setEndPosition(buffer.length());
				errorMessage.setWrongField( expression.toString());
				result.setData(errorMessage);
				result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
				result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
				return false;
			}
			return true;
		} else if ((list != null && list.getExpressions().size() > 0) && (params == null || params.isEmpty()))
		{
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setStartPosition(buffer.length() - expression.toString().length());
			errorMessage.setEndPosition(buffer.length());
			errorMessage.setWrongField( expression.toString());
			result.setData(errorMessage);
			result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
			result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
			return false;
		} else
		{
			List<Expression> expressions = list.getExpressions();
			Iterator<Parameter> iter = params.iterator();
			Integer count = 0;
			Parameter parameter = null;
			boolean isLimitless = false;
			// 获取数据
			while (iter.hasNext())
			{ // 执行过程中会执行数据锁定，性能稍差，若在循环过程中要去掉某个元素只能调用iter.remove()方法。
				// if

				if (count != 0 && isAdd)
				{
					buffer.append(", ");
				}

				parameter = iter.next();
				count++;
				// 如果未无限制的就不用考虑了
				if (parameter.getNum() == -1)
				{
					isLimitless = true;
					count--;// 如果没有限制则直接退出
					break;
				} else if (parameter.getNum() == 0)
				{// 可选
					if (expressions.size() > count)
					{
						if (expressions.size() < count)
						{
							ErrorMessage errorMessage = new ErrorMessage();
							errorMessage.setStartPosition(buffer.length() - expression.toString().length());
							errorMessage.setEndPosition(buffer.length());
							errorMessage.setWrongField( expression.toString());
							result.setData(errorMessage);
							result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
							result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
							return false;
						} else if (parameter.getValues() != null)
						{
							String[] values = parameter.getValues().split(";");
							boolean isExits = false;
							Expression tempExpression = expressions.get(count - 1);
							String tempValue = tempExpression.toString();
							if(tempExpression instanceof  net.sf.jsqlparser.expression.StringValue ){
								tempValue = ((net.sf.jsqlparser.expression.StringValue)tempExpression).getValue();
							} else if(tempExpression instanceof  net.sf.jsqlparser.schema.Column ){
								ErrorMessage errorMessage = new ErrorMessage();
								errorMessage.setStartPosition(buffer.length() - expression.toString().length());
								errorMessage.setEndPosition(buffer.length());
								errorMessage.setWrongField( expression.toString());
								result.setData(errorMessage);
								result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
								result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
								return false;
							}
							for (int i = 0; i < values.length; i++)
							{
								
								if (values[i].trim().equals(tempValue))
								{// 如果相等
									isExits = true;
									break;
								}
							}
							if (isExits)
							{
								continue;
							} else
							{
								ErrorMessage errorMessage = new ErrorMessage();
								errorMessage.setStartPosition(buffer.length() - expression.toString().length());
								errorMessage.setStartPosition(buffer.length());
								errorMessage.setWrongField( expression.toString());
								result.setData(errorMessage);
								result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
								result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
								return false;
							}
						} else if (!verifySqlLegitimacyService.judgEexpression(expressions.get(count - 1), buffer,
								result, true, parameter.getTypes(),isAdd))
						{
							/*ErrorMessage errorMessage = new ErrorMessage();
							errorMessage.setStartPosition(buffer.length() - expression.toString().length());
							errorMessage.setStartPosition(buffer.length());
							errorMessage.setWrongField( expression.toString());
							result.setData(errorMessage);*/
							result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
							result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
							return false;
						}
					} else if (expressions.size() == count)
					{
						/*Expression tempExpression = expressions.get(count - 1);
						if(tempExpression instanceof  net.sf.jsqlparser.expression.StringValue){//tempExpression
							ErrorMessage errorMessage = new ErrorMessage();
							errorMessage.setStartPosition(buffer.length() - expression.toString().length());
							errorMessage.setEndPosition(buffer.length());
							errorMessage.setWrongField( expression.toString());
							result.setData(errorMessage);
							result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
							result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
							return false;
						}else {
							return true;
						}*/
						 if (!verifySqlLegitimacyService.judgEexpression(expressions.get(count-1), buffer,
									result, true, parameter.getTypes(),isAdd))
							{
								/*ErrorMessage errorMessage = new ErrorMessage();
								errorMessage.setStartPosition(buffer.length() - expression.toString().length());
								errorMessage.setEndPosition(buffer.length());
								errorMessage.setWrongField( expression.toString());
								result.setData(errorMessage);*/
								result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
								result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
								return false;
							}
					} else if(expressions.size() - 1 < count){
						return true;
					} else
					{
						ErrorMessage errorMessage = new ErrorMessage();
						errorMessage.setStartPosition(buffer.length() - expression.toString().length());
						errorMessage.setStartPosition(buffer.length());
						errorMessage.setWrongField( expression.toString());
						result.setData(errorMessage);
						result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
						result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
						return false;
					}
					continue;
				} else if (parameter.getNum() == 1)
				{// 必填
					if (expressions.size() < count)
					{
						// 设置为参数类型不匹配;
						ErrorMessage errorMessage = new ErrorMessage();
						errorMessage.setStartPosition(buffer.length() - expression.toString().length());
						errorMessage.setEndPosition(buffer.length());
						errorMessage.setWrongField( expression.toString());
						result.setData(errorMessage);
						result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
						result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
						return false;
					} else
					{
						if (expressions.size() < count)
						{
							ErrorMessage errorMessage = new ErrorMessage();
							errorMessage.setStartPosition(buffer.length() - expression.toString().length());
							errorMessage.setEndPosition(buffer.length());
							errorMessage.setWrongField( expression.toString());
							result.setData(errorMessage);
							result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
							result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
							return false;
						} else if (parameter.getValues() != null)
						{
							String[] values = parameter.getValues().split(";");
							boolean isExits = false;
							Expression tempExpression = expressions.get(count - 1);
							String tempValue = tempExpression.toString();
							if(tempExpression instanceof  net.sf.jsqlparser.expression.StringValue ){
								tempValue = ((net.sf.jsqlparser.expression.StringValue)tempExpression).getValue();
							} else if(tempExpression instanceof  net.sf.jsqlparser.schema.Column ){
								ErrorMessage errorMessage = new ErrorMessage();
								errorMessage.setStartPosition(buffer.length() - expression.toString().length());
								errorMessage.setEndPosition(buffer.length());
								errorMessage.setWrongField( expression.toString());
								result.setData(errorMessage);
								result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
								result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
								return false;
							}
							for (int i = 0; i < values.length; i++)
							{
								
								if (values[i].trim().equals(tempValue))
								{// 如果相等
									isExits = true;
									break;
								}
							}
							if (isExits)
							{
								continue;
							} else
							{
								ErrorMessage errorMessage = new ErrorMessage();
								errorMessage.setStartPosition(buffer.length() - expression.toString().length());
								errorMessage.setEndPosition(buffer.length());
								errorMessage.setWrongField( expression.toString());
								result.setData(errorMessage);
								result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
								result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
								return false;
							}
						} else if (!verifySqlLegitimacyService.judgEexpression(expressions.get(count - 1), buffer,
								result, true, parameter.getTypes(),isAdd))
						{
							/*ErrorMessage errorMessage = new ErrorMessage();
							errorMessage.setStartPosition(buffer.length() - expression.toString().length());
							errorMessage.setEndPosition(buffer.length());
							errorMessage.setWrongField( expression.toString());
							result.setData(errorMessage);*/
							result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
							result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
							return false;
						}
					}
					continue;
				}
			}
			// 如果出现没有限制函数
			if (isLimitless && count > expressions.size())
			{
				// 不成立
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setStartPosition(buffer.length() - expression.toString().length());
				errorMessage.setEndPosition(buffer.length());
				errorMessage.setWrongField( expression.toString());
				result.setData(errorMessage);
				result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
				result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
				return false;

			} else if (!isLimitless && count < expressions.size())
			{// 参数个数不符合
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setStartPosition(buffer.length() - expression.toString().length());
				errorMessage.setEndPosition(buffer.length());
				errorMessage.setWrongField( expression.toString());
				result.setData(errorMessage);
				result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getCode());
				result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_PARMETER_RULE.getDesc());
				return false;
			}
		}

		// 判断参数个数

		return true;
	}
}
