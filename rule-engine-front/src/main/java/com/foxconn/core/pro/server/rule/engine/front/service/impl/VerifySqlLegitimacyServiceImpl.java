/**
 * Project Name:rule-engine-front
 * File Name:VerifySqlLegitimacyServiceImpl.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.service.impl
 * Date:2018年9月18日上午8:52:27
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/
package com.foxconn.core.pro.server.rule.engine.front.service.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxconn.core.pro.server.rule.engine.front.cache.SqlCache;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.config.FunctionConfig;
import com.foxconn.core.pro.server.rule.engine.front.dto.ErrorMessage;
import com.foxconn.core.pro.server.rule.engine.front.dto.InputMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.RuleCheckDto;
import com.foxconn.core.pro.server.rule.engine.front.entity.FunctionValidateBean;
import com.foxconn.core.pro.server.rule.engine.front.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.front.exception.ErrorCodes;
import com.foxconn.core.pro.server.rule.engine.front.service.VerifySqlLegitimacyService;
import com.foxconn.core.pro.server.rule.engine.front.validate.FunctionValidate;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.MySQLGroupConcat;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.parser.Token;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * 验证sql的合法性 ClassName:InsertRuleServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月24日 下午2:47:18 <br/>
 * 
 * @author hewanwan
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Slf4j
public class VerifySqlLegitimacyServiceImpl implements VerifySqlLegitimacyService
{
	private final static String sql_select = "SELECT ";
	private final static String sql_from = " FROM table";
	private final static String sql_from_where = sql_select + "*" + sql_from + " WHERE ";

	@Autowired
	private FunctionConfig functionConfig;
	/***
	 * verifyField:(验证sql字段). <br/>
	 * 
	 * @author hewanwan
	 * @param sql
	 * @throws JSQLParserException
	 * @since JDK 1.8
	 */
	public ResultMap<ErrorMessage> verifyField(String sql)
	{

		CCJSqlParserManager parser = new CCJSqlParserManager();
		StringBuilder buffer = new StringBuilder();
		ResultMap<ErrorMessage> result = new ResultMap<>();
		ErrorMessage errorMessage = new ErrorMessage();
		sql = sql.trim();
		String parseSql = sql_select + sql.trim() + sql_from;
		try
		{
			Statement stmt = parser.parse(new StringReader(parseSql));
			if (stmt instanceof Select)
			{
				Select selectStatement = (Select) stmt;
				PlainSelect selectBody = (PlainSelect) selectStatement.getSelectBody();
				List<SelectItem> selectItemlist = selectBody.getSelectItems();
				SelectExpressionItem selectExpressionItem = null;
				Expression expression = null;
				AllTableColumns allTableColumns = null;
				Alias alias = null;
				SelectItem selectItem = null;
				if (selectItemlist != null)
				{
					for (int i = 0; i < selectItemlist.size(); i++)
					{
						selectItem = selectItemlist.get(i);
						if (selectItem instanceof SelectExpressionItem)
						{
							if(i!=0){
								buffer.append(CommonConstant.COMMA);
							}
							selectItem = (SelectExpressionItem) selectItem;

							selectExpressionItem = (SelectExpressionItem) selectItemlist.get(i);
							buffer.append(selectExpressionItem.toString());
							alias = selectExpressionItem.getAlias();
							if (alias != null && !judgeAlias(alias, buffer, selectExpressionItem, result)) // 如果没有验证通过，则进行返回
							{
								result.getData().setOtherField(selectBody.toString().substring(sql_select.length(),
										selectBody.toString().lastIndexOf(sql_from)));
								return result;
							}
							expression = selectExpressionItem.getExpression();// 获取字段
							if (!judgEexpression(expression, buffer, selectExpressionItem, result, alias, true))
							{
								result.getData().setOtherField(selectBody.toString().substring(sql_select.length(),
										selectBody.toString().lastIndexOf(sql_from)));
								return result;
							}
						} else if (selectItem instanceof AllTableColumns)
						{
							if(i!=0){
								buffer.append(CommonConstant.COMMA);
							}
							selectItem = (AllTableColumns) selectItem;
							allTableColumns = (AllTableColumns) selectItemlist.get(i);
							buffer.append(allTableColumns.toString());
							result.setCode(ErrorCodes.ERROR_NAME_RULE.getCode());
                            result.setMsg(ErrorCodes.ERROR_NAME_RULE.getDesc());
                            result.setStatus(ErrorCodes.ERROR_NAME_RULE.getStatus());
                            Integer startPosition = buffer.length() - allTableColumns.toString().length();
                            Integer endPosition = buffer.length();
                            errorMessage.setErrorMessage(startPosition, endPosition, allTableColumns.toString(),CommonConstant.VALIDATE_FIELD_TYPE);
                            result.setData(errorMessage);
							result.getData().setOtherField(selectBody.toString().substring(sql_select.length(),
									selectBody.toString().lastIndexOf(sql_from)));
							return result;
						} else {
							if(i!=0){
								buffer.append(CommonConstant.COMMA);
							}
							buffer.append(selectItem.toString());
						}
					}
				}
			}
		} catch (JSQLParserException e)
		{
			log.error("verifyField Exception", e);
			if (e.getCause() instanceof ParseException)
			{
				if (e.getCause() instanceof ParseException)
				{
					ParseException parseException = (ParseException) e.getCause();
					Token token = parseException.currentToken;
					if(token.absoluteBegin- sql_select.length() > 0){
						errorMessage.setEndPosition(token.absoluteEnd- sql_select.length());
						errorMessage.setStartPosition(token.absoluteBegin- sql_select.length());
						errorMessage.setOtherField(sql);
						errorMessage.setWrongField(token.toString());
					}  else {
						String message = parseException.getMessage();
						String mes = StringUtils.substringBetween(message,CommonConstant.ENCOUNTERED_UNEXPECTED_TOKEN,CommonConstant.AT);
						String wrongField = StringUtils.substringBetween(mes, CommonConstant.SLASH, CommonConstant.SLASH);
						Integer end = Integer.valueOf(StringUtils.substringBetween(message, CommonConstant.COLUMN, CommonConstant.SPOT).trim());
						errorMessage.setEndPosition(end - sql_select.length());
						errorMessage.setStartPosition(end + wrongField.length() - sql_select.length());
						errorMessage.setWrongField(wrongField);
						errorMessage.setOtherField(sql.trim());
					}
					result.setCode(ErrorCodes.ERROR_GRAMMAR_RULE.getCode());
					result.setMsg(ErrorCodes.ERROR_GRAMMAR_RULE.getDesc());
					result.setStatus(ErrorCodes.ERROR_GRAMMAR_RULE.getStatus());
					result.setData(errorMessage);
				} else
				{
					result.setCode(ErrorCodes.ERROR_GRAMMAR_RULE.getCode());
					result.setMsg(ErrorCodes.ERROR_GRAMMAR_RULE.getDesc());
					result.setStatus(ErrorCodes.ERROR_GRAMMAR_RULE.getStatus());
				}
			}
		} catch (Exception e)
		{
			log.error("verifyField Exception", e);
			result.setCode(ErrorCodes.ERROR_GRAMMAR_RULE.getCode());
			result.setMsg(ErrorCodes.ERROR_GRAMMAR_RULE.getDesc());
			result.setStatus(ErrorCodes.ERROR_GRAMMAR_RULE.getStatus());
			
		}
		return result;
	}

	/***
	 * 当return false 返回错误信息 errorInfo:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author hewanwan
	 * @param a
	 * @param buffer
	 * @param result
	 * @return
	 * @since JDK 1.8
	 */
	public ResultMap<ErrorMessage> errorInfo(Object a, StringBuilder buffer, ResultMap<ErrorMessage> result,String type)
	{
		return errorInfo( a,  buffer,  result, type, ErrorCodes.ERROR_NAME_RULE);
	}
	
	public ResultMap<ErrorMessage> errorInfo(Object a, StringBuilder buffer, ResultMap<ErrorMessage> result,String type,ErrorCodes errors)
	{
		ErrorMessage errorMessage = new ErrorMessage();
		result.setCode(errors.getCode());
		result.setMsg(errors.getDesc());
		result.setStatus(errors.getStatus());
		Integer startPosition = buffer.length() - a.toString().length();
		Integer endPosition = buffer.length();
		errorMessage.setErrorMessage(startPosition, endPosition, a.toString(),type);
		result.setData(errorMessage);
		return result;
	}

	public boolean judgEexpression(Expression expression, StringBuilder buffer,
			SelectExpressionItem selectExpressionItem, ResultMap<ErrorMessage> result, Alias alias,
			boolean isValidateAlias) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		
		String columnName = null;
		boolean flag = true;
		
		if (expression instanceof NullValue
				|| expression instanceof JdbcParameter || expression instanceof JdbcNamedParameter
				|| expression instanceof HexValue /*|| expression instanceof CaseExpression*/
				|| expression instanceof WhenClause || expression instanceof DateTimeLiteralExpression
				|| expression instanceof Concat || expression instanceof ExtractExpression
				|| expression instanceof IntervalExpression || expression instanceof RegExpMatchOperator
				|| expression instanceof NumericBind || expression instanceof KeepExpression)
		{
			result = errorInfo(selectExpressionItem, buffer, result,CommonConstant.VALIDATE_FIELD_TYPE);
			return false;
		} else if(expression instanceof SignedExpression){
			SignedExpression signedExpression = (SignedExpression)expression;
			if (!judgEexpression(signedExpression.getExpression(), buffer, selectExpressionItem, result, alias, false))
			{
				return false;
			}
		} else if (expression instanceof DoubleValue || expression instanceof LongValue || expression instanceof DateValue
				|| expression instanceof TimeValue || expression instanceof TimestampValue
				|| expression instanceof StringValue)
		{
			if (isValidateAlias && alias == null)
			{
				result = errorInfo(selectExpressionItem, buffer, result,CommonConstant.VALIDATE_FIELD_TYPE);
				return false;
			}
		} else if (expression instanceof Column)
		{
			Column column = (Column) expression;// 获取字段
			columnName = column.getFullyQualifiedName();
			// `aaaa`.`bbb.ccc`.`cvxcv`
			List<String> columnNamelist = null;
			try{
				columnNamelist = VerifySqlLegitimacyServiceImpl.getColumnNames(columnName);
			} catch (BaseException e){
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_FIELD_TYPE);
				result.setCode(e.getCode());
				result.setMsg(e.getMsg());
				result.setStatus(e.getStatus());
				return false;
			}
			//List<String> columnNamelist = getColumnNames(columnName);
			flag = isSpecialChar(columnNamelist);
			if (flag == false)
			{
				result = errorInfo(selectExpressionItem, buffer, result,CommonConstant.VALIDATE_FIELD_TYPE);
				return false;
			}
		} else if (expression instanceof Parenthesis)
		{
			Parenthesis parenthesis = (Parenthesis) expression;
			if (parenthesis.isNot())
			{
				result = errorInfo(selectExpressionItem, buffer, result,CommonConstant.VALIDATE_FIELD_TYPE);
				return false;
			}
			if (!judgEexpression(parenthesis.getExpression(), buffer, selectExpressionItem, result, alias, false))
			{
				return false;
			}
		} else if (expression instanceof Addition || expression instanceof Division || expression instanceof

		Multiplication || expression instanceof Subtraction)
		{
			if (isValidateAlias && alias == null)
			{
				result = errorInfo(selectExpressionItem, buffer, result,CommonConstant.VALIDATE_FIELD_TYPE);
				return false;
			}
			BinaryExpression binaryExpression = (BinaryExpression) expression;
			if (!judgEexpression(binaryExpression.getLeftExpression(), buffer, selectExpressionItem, result,

					alias, false))
			{
				return false;
			}

			if (!judgEexpression(binaryExpression.getRightExpression(), buffer, selectExpressionItem, result,

					alias, false))
			{
				return false;
			}
		} else if(expression instanceof Function){
			Function function = (Function)expression;
			if (isValidateAlias && alias == null)
			{
				result = errorInfo(selectExpressionItem, buffer, result,CommonConstant.VALIDATE_FIELD_TYPE);
				return false;
			}
			String name = function.getName();
			//函数是否存在
			Map<String,FunctionValidateBean> functionMaps = functionConfig.getMaps();
			if(functionMaps.containsKey(name)){
				FunctionValidateBean functionValidateBean = functionMaps.get(name);
				String classString = functionValidateBean.getCls();
				FunctionValidate functionValidate = null;
				if(classString == null || StringUtils.isBlank(classString) 
						|| CommonConstant.FUNCTION_VALIDATE.equals(classString.trim())){
					functionValidate = new FunctionValidate(){
						
					};
				} else {
					functionValidate = (FunctionValidate)Class.forName(classString).newInstance();
				}
				if(functionValidate.validate(functionValidateBean, function,function.getParameters(),result,this,buffer,false)){
					return true;
				} else {
					return false;
				}
			} else {
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_NAME_RULE.getCode());
				result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_NAME_RULE.getDesc());
				result.setStatus(ErrorCodes.ERROR_GRAMMAR_FUNCTION_NAME_RULE.getStatus());
				return false;
			}
		} else if(expression instanceof CaseExpression){
			if (isValidateAlias && alias == null)
			{
				result = errorInfo(selectExpressionItem, buffer, result,CommonConstant.VALIDATE_FIELD_TYPE);
				return false;
			}
			CaseExpression caseExpression = (CaseExpression)expression;
			
			Expression switchExpression =  caseExpression.getSwitchExpression();
			if(switchExpression !=null && !judgEexpression(switchExpression, buffer, selectExpressionItem, result, alias, false)){
				return false;
			}
			
			List<WhenClause> whenClauseList = caseExpression.getWhenClauses();
			WhenClause whenClause = null;
			Expression whenExpression = null;
			Expression thenExpression = null;
			if(whenClauseList != null && whenClauseList.size()>0){
				for(int i =0;i<whenClauseList.size();i++){
					whenClause =  whenClauseList.get(i);
					whenExpression = whenClause.getWhenExpression();
					if(!judgEexpression(whenExpression, buffer, selectExpressionItem, result, alias, false)){
						return false;
					}
					thenExpression = whenClause.getThenExpression();
					if(!judgEexpression(thenExpression, buffer, selectExpressionItem, result, alias, false)){
						return false;
					}
				 }
			}
			Expression elseExpression = caseExpression.getElseExpression();
			if(elseExpression == null){
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE,ErrorCodes.ERROR_GRAMMAR_CASE_WHEN_ELSE_RULE);
				return false;
			} else if (!judgEexpression(elseExpression, buffer, selectExpressionItem, result, alias, false)){
				return false;
			}
			return true;
		}
		return true;
	}

	/**
	 * judgeAlias: 判断别名. <br/>
	 * 
	 * @author liupingan
	 * @param alias
	 * @param buffer
	 * @param selectExpressionItem
	 * @param result
	 * @return
	 * @since JDK 1.8
	 */
	public boolean judgeAlias(Alias alias, StringBuilder buffer, SelectExpressionItem selectExpressionItem,
			ResultMap<ErrorMessage> result)
	{
		List<String> columnNamelist = getColumnNames(alias.getName());
		boolean flag = isSpecialChar(columnNamelist);
		if (flag == false)
		{
			result = errorInfo(selectExpressionItem, buffer, result,CommonConstant.VALIDATE_FIELD_TYPE);
			return false;
		}
		return true;
	}

	public static List<String> getColumnNames(String source)
	{
		if (source == null)
		{
			return null;
		}
		int start = source.indexOf(CommonConstant.QUOTATION_MARK);
		if (start == -1)
		{
			List<String> result = new ArrayList<>();
			Collections.addAll(result, source.split(CommonConstant.DOUBLE_SLASH_SPOT));
			//判断是否是关键字
			for(String stringList:result) {
				while(SqlCache.getSqlKeys().containsValue(stringList)) {
					throw new BaseException(ErrorCodes.ERROR_NAME_RULE.getCode(),ErrorCodes.ERROR_NAME_RULE.getDesc(),ErrorCodes.ERROR_NAME_RULE.getStatus());
				}
			}
			return result;
		}
		List<String> result = new ArrayList<>();
		int end = -1;
		start = 0;
		int temp = 0;
		String tempString = null;
		for (int i = 0; i < source.split(CommonConstant.QUOTATION_MARK).length - 1; i++)
		{
			temp = source.indexOf(CommonConstant.QUOTATION_MARK, start);
			// 如果相等，并且为第一个

			if (i == 0 && start == temp)
			{
				end = source.indexOf(CommonConstant.QUOTATION_MARK, temp + 1);
				if (end == -1)
					break;
				tempString = source.substring(temp + 1, end);
				result.add(tempString);
				start = end + 2;
			} else if (i == 0 && start != temp)
			{
				end = source.indexOf(CommonConstant.QUOTATION_MARK, temp + 1);
				// tempString = source.substring(temp+1, end);
				Collections.addAll(result, source.substring(0, temp).split(CommonConstant.DOUBLE_SLASH_SPOT));

				if (end == -1)
					break;
				tempString = source.substring(temp + 1, end);
				result.add(tempString);
				start = end + 2;
			} else if (temp - 2 == end)
			{// 如果为连续的
				end = source.indexOf(CommonConstant.QUOTATION_MARK, temp + 1);
				if (end == -1)
					break;
				tempString = source.substring(temp + 1, end);
				result.add(tempString);
				start = end + 2;
			} else
			{
				Collections.addAll(result, source.substring(end + 2, temp).split(CommonConstant.DOUBLE_SLASH_SPOT));
				end = source.indexOf(CommonConstant.QUOTATION_MARK, temp + 1);
				if (end == -1)
					break;
				tempString = source.substring(temp + 1, end);
				result.add(tempString);
				start = end + 2;
			}
			i++;
		}
		if (end + 1 != source.length())
		{
			Collections.addAll(result, source.substring(end + 2).trim().split(CommonConstant.DOUBLE_SLASH_SPOT));
		}
		return result;
	}

	/** 验证where条件 */
	@Override
	public ResultMap<ErrorMessage> verifyCondition(String sql)
	{

		CCJSqlParserManager parser = new CCJSqlParserManager();
		Statement stmt = null;
		ResultMap<ErrorMessage> result = new ResultMap<>();
		ErrorMessage errorMessage = new ErrorMessage();
		sql = sql.trim();
		String parseSql = sql_from_where + sql;
		Expression expression = null;
		try
		{
			stmt = parser.parse(new StringReader( parseSql));
			if (stmt instanceof Select)
			{
				Select selectStatement = (Select) stmt;
				PlainSelect selectBody = (PlainSelect) selectStatement.getSelectBody();
				expression = selectBody.getWhere();
				
				StringBuilder buffer = new StringBuilder();
				if(expression instanceof Column){
					buffer.append(expression.toString());
					result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
					result.setCode(ErrorCodes.ERROR_GRAMMAR_RULE.getCode());
					result.setMsg(ErrorCodes.ERROR_GRAMMAR_RULE.getDesc());
					result.setStatus(ErrorCodes.ERROR_GRAMMAR_RULE.getStatus());
					return result;
				}
				boolean isOnlyValue = false;
				if(!judgEexpression(expression, buffer, result,isOnlyValue,null,true)){
					result.getData().setOtherField(expression.toString());
				}
			}
		} catch (JSQLParserException e)
		{
			log.error("verifyField Exception", e);
			if (e.getCause() instanceof ParseException)
			{
				ParseException parseException = (ParseException) e.getCause();

				Token token = parseException.currentToken;
				
				if(token.absoluteBegin- sql_from_where.length() >0){
					errorMessage.setEndPosition(token.absoluteEnd- sql_from_where.length());
					errorMessage.setStartPosition(token.absoluteBegin- sql_from_where.length());
					errorMessage.setOtherField(sql.trim());
					errorMessage.setWrongField(token.toString());
				} else {
					String message = parseException.getMessage();
					String mes = StringUtils.substringBetween(message, CommonConstant.ENCOUNTERED_UNEXPECTED_TOKEN, CommonConstant.AT);
					String wrongField = StringUtils.substringBetween(mes, CommonConstant.SLASH, CommonConstant.SLASH);
					Integer end = Integer.valueOf(StringUtils.substringBetween(message, CommonConstant.COLUMN, CommonConstant.SPOT).trim());
					errorMessage.setEndPosition(end - sql_from_where.length());
					errorMessage.setStartPosition(end + wrongField.length() - sql_from_where.length());
					errorMessage.setWrongField(wrongField);
					errorMessage.setOtherField(sql.trim());
				}

				result.setCode(ErrorCodes.ERROR_GRAMMAR_RULE.getCode());
				result.setMsg(ErrorCodes.ERROR_GRAMMAR_RULE.getDesc());
				result.setStatus(ErrorCodes.ERROR_GRAMMAR_RULE.getStatus());
				result.setData(errorMessage);
			} else
			{
				result.setCode(ErrorCodes.ERROR_GRAMMAR_RULE.getCode());
				result.setMsg(ErrorCodes.ERROR_GRAMMAR_RULE.getDesc());
				result.setStatus(ErrorCodes.ERROR_GRAMMAR_RULE.getStatus());
			}
		} catch (Exception e)
		{
			
			log.error("verifyField Exception", e);
			result.setCode(ErrorCodes.ERROR_GRAMMAR_RULE.getCode());
			result.setMsg(ErrorCodes.ERROR_GRAMMAR_RULE.getDesc());
			result.setStatus(ErrorCodes.ERROR_GRAMMAR_RULE.getStatus());
		}
		return result;
	}

	/**
	 * 
	 * judgEexpression:(条件语句判断). <br/>
	 * 
	 * @author liupingan
	 * @param expression
	 * @param buffer
	 * @param result
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @since JDK 1.8
	 */
	public boolean judgEexpression(Expression expression, StringBuilder buffer, ResultMap<ErrorMessage> result,boolean isOnlyValue,String type,boolean isAdd) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		AndExpression andExpression = null;
		OrExpression orExpression = null;
		Modulo modulo = null;
		BinaryExpression binaryExpression = null;
		ComparisonOperator comparisonOperator = null;
		InExpression inExpression = null;
		Parenthesis parenthesis = null;

		ItemsList itemsList = null;

		Column column = null;
		String columnName = null;

		boolean flag = true;
		if (expression instanceof BitwiseAnd || expression instanceof BitwiseOr || expression instanceof BitwiseXor
				|| expression instanceof IsNullExpression || expression instanceof Between
				|| expression instanceof NullValue/*
				|| expression instanceof SignedExpression*/ || expression instanceof JdbcParameter
				|| expression instanceof JdbcNamedParameter || expression instanceof HexValue
				|| expression instanceof LikeExpression || expression instanceof CaseExpression
				|| expression instanceof WhenClause || expression instanceof ExistsExpression
				|| expression instanceof AllComparisonExpression || expression instanceof AnyComparisonExpression
				|| expression instanceof Concat || expression instanceof Matches
				|| expression instanceof AnalyticExpression || expression instanceof CastExpression
				|| expression instanceof ExtractExpression || expression instanceof IntervalExpression
				|| expression instanceof OracleHierarchicalExpression || expression instanceof RegExpMatchOperator
				|| expression instanceof JsonExpression || expression instanceof RegExpMySQLOperator
				|| expression instanceof NumericBind || expression instanceof KeepExpression
				|| expression instanceof MySQLGroupConcat || expression instanceof RowConstructor
				|| expression instanceof OracleHint || expression instanceof TimeKeyExpression
				|| expression instanceof DateTimeLiteralExpression)
		{
			if(isAdd)
			buffer.append(expression);
			result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
			return false;
		} else if(expression instanceof SignedExpression){
			SignedExpression signedExpression = (SignedExpression)expression;
			if(isAdd)
				buffer.append(signedExpression.getSign());
			if (!judgEexpression(signedExpression.getExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
			
		} else if (expression instanceof AndExpression)
		{
			andExpression = (AndExpression) expression;
			if (andExpression.isNot())
			{
				return false;
			}
			if (!judgEexpression(andExpression.getLeftExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
			if(isAdd)
			buffer.append(CommonConstant.AND);
			if (!judgEexpression(andExpression.getRightExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
		} else if (expression instanceof OrExpression)
		{
			orExpression = (OrExpression) expression;
			if (orExpression.isNot())
			{
				return false;
			}
			if (!judgEexpression(orExpression.getLeftExpression(), buffer, result,true,type,isAdd))
			{
				return false;

			}
			if(isAdd)
			buffer.append(CommonConstant.OR);
			if (!judgEexpression(orExpression.getRightExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
		} else if (expression instanceof Parenthesis)
		{
			parenthesis = (Parenthesis) expression;
			if (parenthesis.isNot())
			{
				buffer.append(expression.toString());
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
			if(isAdd)
			buffer.append(CommonConstant.LEFT_PARENTHESIS);
			if (!judgEexpression(parenthesis.getExpression(), buffer, result,true,type,isAdd))
			{
				if(isAdd)
				buffer.append(CommonConstant.RIGHT_PARENTHESIS);
				return false;
			} else
			{
				if(isAdd)
				buffer.append(CommonConstant.RIGHT_PARENTHESIS);
				return true;
			}
		} else if (expression instanceof Column)
		{
			column = (Column) expression;
			columnName = column.getFullyQualifiedName();
			if(isAdd)
			buffer.append(columnName);
			List<String> namelist = null;
			try{
				namelist = VerifySqlLegitimacyServiceImpl.getColumnNames(columnName);
			} catch (BaseException e){
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				result.setCode(e.getCode());
				result.setMsg(e.getMsg());
				result.setStatus(e.getStatus());
				return false;
			}
			
			flag = isSpecialChar(namelist);
			if (flag == false)
			{
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
		} else if (expression instanceof Modulo)
		{
			// % 取模处理
			modulo = (Modulo) expression;
			if (modulo.isNot())
			{
				return false;
			}
			if (!judgEexpression(modulo.getLeftExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
			if(isAdd)
			buffer.append(CommonConstant.PERCENTAGE_NUMBER);
			if (!judgEexpression(modulo.getRightExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
		} else if (expression instanceof EqualsTo || expression instanceof GreaterThan
				|| expression instanceof GreaterThanEquals || expression instanceof MinorThan
				|| expression instanceof MinorThanEquals || expression instanceof NotEqualsTo)
		{
			// > >= = < <= != <>处理
			comparisonOperator = (ComparisonOperator) expression;
			if (comparisonOperator.isNot())
			{
				return false;
			}
			if (!judgEexpression(comparisonOperator.getLeftExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
			if(isAdd)
			buffer.append(" "+comparisonOperator.getStringExpression()+" ");
			if (!judgEexpression(comparisonOperator.getRightExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
		} else if (expression instanceof Addition || expression instanceof Division
				|| expression instanceof Multiplication || expression instanceof Subtraction)
		{
			// ( + - * / )加减乘除处理
			binaryExpression = (BinaryExpression) expression;
			if (binaryExpression.isNot())
			{
				return false;
			}
			if (!judgEexpression(binaryExpression.getLeftExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
			if(isAdd)
			buffer.append(" "+binaryExpression.getStringExpression()+" ");
			if (!judgEexpression(binaryExpression.getRightExpression(), buffer, result,true,type,isAdd))
			{
				return false;
			}
		} else if (expression instanceof InExpression)
		{
			// ( + - * / )加减乘除处理
			inExpression = (InExpression) expression;
			// itemsList = inExpression.getRightItemsList();
			ExpressionList expressionList = (ExpressionList) inExpression.getRightItemsList();
			if (inExpression.isNot())
			{
				buffer.append(expression.toString());
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			} else if (inExpression.getLeftExpression() == null)
			{
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
			if (!judgEexpression(inExpression.getLeftExpression(), buffer, result,true,type,isAdd))
			{
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
			if(isAdd)
			buffer.append(CommonConstant.IN);
			if (expressionList == null)
			{
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
			
			List<Expression> expressionslist = (List<Expression>) expressionList.getExpressions();
			for (int i = 0; i < expressionslist.size(); i++)
			{
				if( i !=0){
					if(isAdd)
					buffer.append(CommonConstant.COMMA);   
				}
				if (!judgEexpression(expressionslist.get(i), buffer, result,true,type,isAdd))
				{
					return false;
				}
			}
			if(isAdd)
			buffer.append(CommonConstant.RIGHT_PARENTHESIS);
			if (itemsList instanceof SubSelect)
			{
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
		} else if (expression instanceof DoubleValue || expression instanceof LongValue
				|| expression instanceof DateValue || expression instanceof TimeValue
				|| expression instanceof TimestampValue)
		{
			if((expression instanceof DoubleValue || expression instanceof LongValue
					|| expression instanceof DateValue ) && type != null && (!type.equals(CommonConstant.JAVA_LANG_NUMBER) && !type.equals(CommonConstant.JAVA_LANG_OBJECT) )){
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
			if(isAdd)
			buffer.append(expression.toString());
			if(!isOnlyValue){
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
			return true;
		} else if(expression instanceof StringValue){
			if(type.equals(CommonConstant.JAVA_LANG_NUMBER)){
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
			if(isAdd)
			buffer.append(CommonConstant.RIGHT_LINE + expression.toString()+CommonConstant.RIGHT_LINE);
			if(!isOnlyValue){
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				return false;
			}
			return true;
		} else if(expression instanceof Function){
			Function function = (Function)expression;
			String name = function.getName();
			
			//函数是否存在
			Map<String,FunctionValidateBean> functionMaps = functionConfig.getMaps();
			if(functionMaps.containsKey(name)){
				FunctionValidateBean functionValidateBean = functionMaps.get(name);
				String classString = functionValidateBean.getCls();
				FunctionValidate functionValidate = null;
				if(classString == null || StringUtils.isBlank(classString) 
						|| CommonConstant.FUNCTION_VALIDATE.equals(classString.trim())){
					functionValidate = new FunctionValidate(){
						
					};
				} else {
					functionValidate = (FunctionValidate)Class.forName(classString).newInstance();
				}
				if(isAdd)
				buffer.append(name + CommonConstant.LEFT_PARENTHESIS);
				if(functionValidate.validate(functionValidateBean,function, function.getParameters(),result,this,buffer,true)){
					if(isAdd)
					buffer.append(CommonConstant.RIGHT_PARENTHESIS); 
					return true;
				} else {
					if(isAdd)
					buffer.append(CommonConstant.RIGHT_PARENTHESIS);
					return false;
				}
				
			} else {
				if(isAdd)
				buffer.append(function.toString());
				result = errorInfo(expression, buffer, result,CommonConstant.VALIDATE_CONDITION_TYPE);
				result.setCode(ErrorCodes.ERROR_GRAMMAR_FUNCTION_NAME_RULE.getCode());
				result.setMsg(ErrorCodes.ERROR_GRAMMAR_FUNCTION_NAME_RULE.getDesc());
				result.setStatus(ErrorCodes.ERROR_GRAMMAR_FUNCTION_NAME_RULE.getStatus());
				return false;
			}
		}
		return true;
	}
	
	public boolean validateFunction(){
		//functionConfig
		
		return true;
	}

	/***
	 * 由数字、26个英文字母或者下划线或者.组成的字符串,不能以下划线和.为开头和结尾 不能出现两次下划线
	 * 
	 * @return true为包含，false为不包含
	 */
	public boolean isSpecialChar(String str)
	{
		String regEx = "^(?!_)(?!.*?_$)([a-zA-Z0-9.]|_(?!_))+$";
		boolean flag = str.matches(regEx);
		if(flag==true) {
			if(str.indexOf(CommonConstant.COLON) !=-1) {
				flag=false;
			}
		}
		return flag;
	}

	public boolean isSpecialChar(List<String> strs)
	{
		boolean flag = true;
		for (String name : strs)
		{
			/*while(SqlCache.getSqlKeys().containsValue(name)) {
				return false;
			}*/
			if (!isSpecialChar(name))
			{
				return false;
			}
		}
		return flag;
	}

	@Override
	public ResultMap<ErrorMessage> verifyField(InputMap<RuleCheckDto> bean)
	{
		ResultMap<ErrorMessage> result = new ResultMap<>();
		if (bean == null || bean.getConfig() == null || bean.getConfig().getUserId() == null
				|| StringUtils.isBlank(bean.getConfig().getUserId()) || bean.getData() == null
				|| bean.getData().getFields() == null || StringUtils.isBlank(bean.getData().getFields()))
		{
			result.setCode(ErrorCodes.FAILED.getCode());
			result.setMsg(ErrorCodes.FAILED.getDesc());
			result.setStatus(ErrorCodes.FAILED.getStatus());
			return result;
		}
		return verifyField(bean.getData().getFields());
	}

	@Override
	public ResultMap<ErrorMessage> verifyCondition(InputMap<RuleCheckDto> bean)
	{

		ResultMap<ErrorMessage> result = new ResultMap<>();
		if (bean == null || bean.getConfig() == null || bean.getConfig().getUserId() == null
				|| StringUtils.isBlank(bean.getConfig().getUserId()) || bean.getData() == null
				|| bean.getData().getCondition() == null || StringUtils.isBlank(bean.getData().getCondition()))
		{
			result.setCode(ErrorCodes.FAILED.getCode());
			result.setMsg(ErrorCodes.FAILED.getDesc());
			result.setStatus(ErrorCodes.FAILED.getStatus());
			return result;
		}
		return verifyCondition(bean.getData().getCondition());
	}

	/**
	 * 
	 * TODO 如果字段存在，则需要判断，如果条件不存在，则为真
	 * @see com.foxconn.core.pro.server.rule.engine.front.service.VerifySqlLegitimacyService#verifySql(java.lang.String, java.lang.String)
	 */
	@Override
	public ResultMap<ErrorMessage> verifySql(String field, String condition)
	{
		ResultMap<ErrorMessage> result = new ResultMap<>();
		if (field == null || StringUtils.isBlank(field))
		{
			result.setCode(ErrorCodes.FAILED.getCode());
			result.setMsg(ErrorCodes.FAILED.getDesc());
			result.setStatus(ErrorCodes.FAILED.getStatus());
			return result;
		}
		result = verifyField(field);
		if (result == null)
		{
			result = new ResultMap<>(ErrorCodes.FAILED);
		} else if (CommonConstant.SERVICE_SUCCESS.equals(result.getCode()))
		{
			if(condition == null || StringUtils.isBlank(condition)){
				return result;
			}
			result = verifyCondition(condition);
			if (result == null)
			{
				result = new ResultMap<>(ErrorCodes.FAILED);
			}
		}
		return result;
	}
}
