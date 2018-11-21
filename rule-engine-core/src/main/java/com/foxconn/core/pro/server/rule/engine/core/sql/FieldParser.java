package com.foxconn.core.pro.server.rule.engine.core.sql;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.config.template.RuleTemplateProperties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
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
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

/** */
/**
 *
 * 单句查询语句解析器
 * 
 * @author 赵朝峰
 *
 * @since 2013-6-10
 * @version 1.00
 */
@Getter
@Slf4j
@Service("fieldParser")
public class FieldParser extends BaseSingleParser
{

	protected final static String splitPattern = ",";

	@Autowired
	private RuleTemplateProperties ruleTemplateProperties;

	public FieldParser(String originalValue)
	{
		super(originalValue);
	}

	public FieldParser()
	{
		super();
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.foxconn.core.pro.server.rule.engine.core.sql.BaseSingleParser#getQLExpress()
	 */
	@Override
	public String getQLExpress(String splitPattern)
	{
		if (StringUtils.isEmpty(originalValue))
		{
			return null;
		}
		String temp = originalValue.trim();
		CCJSqlParserManager parser = new CCJSqlParserManager();
		Statement stmt = null;
		try
		{
			stmt = parser.parse(new StringReader("select " + temp + " from table "));
		} catch (JSQLParserException e)
		{
			log.error("getQLExpress fail", e);
			return null;
		}
		if (stmt instanceof Select)
		{
			Select selectStatement = (Select) stmt;
			PlainSelect selectBody = (PlainSelect) selectStatement.getSelectBody();
			StringBuilder buffer = null;
			Map<String, String> containMap = new HashMap<String, String>(20);
			Integer count = 0;

			StringBuilder bufferValue = null;
			StringBuilder bufferCaseStr = null;
			List<SelectItem> selectItemlist = selectBody.getSelectItems();
			SelectExpressionItem selectExpressionItem = null;
			Expression expression = null;
			SelectItem selectItem = null;
			Alias alias = null;
			String keyStr = null;
			StringBuilder resultBuilder = new StringBuilder();
			Column column = null;
			// 关键字过滤只对保留关键字
			try
			{

				if (selectItemlist != null)
				{
					for (int i = 0; i < selectItemlist.size(); i++)
					{
						selectItem = selectItemlist.get(i);
						bufferValue = new StringBuilder();
						buffer = new StringBuilder();
						bufferCaseStr = new StringBuilder();

						if (selectItem instanceof SelectExpressionItem)
						{
							selectExpressionItem = (SelectExpressionItem) selectItemlist.get(i);
							alias = selectExpressionItem.getAlias();
							expression = selectExpressionItem.getExpression();// 获取字段
							if (alias != null) // 如果没有验证通过，则进行返回
							{
								keyStr = StringEscapeUtils.escapeJava(alias.getName());
								expressionParse(expression, buffer, bufferValue,bufferCaseStr, selectExpressionItem, alias,
										containMap, ++count);
								
								resultBuilder.append(bufferValue + "");
								resultBuilder.append(bufferCaseStr);
								resultBuilder.append("__temp_cloumn_Value_" + i + "__=" + buffer + ";\r\n");
								resultBuilder.append(
										"__result__.put(\"" + keyStr + "\",__temp_cloumn_Value_" + i + "__); \r\n");
							} else if (expression instanceof Column)
							{
								column = (Column) expression;
								List<String> cloumns = getColumnNames(column.getFullyQualifiedName());
								String finalClomn = getFinalColumnName(cloumns);
								bufferValue.append(" __temp_cloumn_Key_" + i + "__ =\""
										+ StringEscapeUtils.escapeJava(JSONObject.toJSONString(cloumns)) + "\";\r\n");
								bufferValue.append("__temp_cloumn_Value_" + i
										+ "__ = JSONObjectUtil.getList(root,__temp_cloumn_Key_" + i + "__);\r\n");
								resultBuilder.append(bufferValue);
								resultBuilder.append("__result__.put(\"" + StringEscapeUtils.escapeJava(finalClomn)
										+ "\",__temp_cloumn_Value_" + i + "__); \r\n");
							} else
							{
								continue;
							}
						} else if (selectItem instanceof AllColumns)
						{
							// 如果为* 塞入所有数据
							resultBuilder.append("__result__.putAll(root);\r\n");
						} else if (selectItem instanceof AllTableColumns)
						{
							continue;
						}
					}
				}
			} catch (Exception e)
			{
				log.error("validate fields error", e.getMessage());
				return null;
			}
			StringBuffer result = new StringBuffer();
			//TODO : 信息组装
			result.append(getHeaders());
			result.append("\r\n");
			result.append("JSONObject __result__ = new JSONObject(); \r\n");
			result.append(resultBuilder.toString());
			result.append(" return __result__;");
			return result.toString();
		}
		return null;
	}

	@Override
	public String getQLExpress()
	{
		return getQLExpress(splitPattern);
	}

	@Override
	public String getHeaders()
	{
		String result = "";
		InputStream stencilsetStream = ConditionParser.class.getClassLoader()
				.getResourceAsStream(ruleTemplateProperties.getField().getHeaderFile());
		try
		{
			result = IOUtils.toString(stencilsetStream, StandardCharsets.UTF_8);
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String expressionParse(Expression expression, StringBuilder buffer, StringBuilder bufferValue,StringBuilder bufferCaseStr,
			SelectExpressionItem selectExpressionItem, Alias alias, Map<String, String> containMap, Integer count)
	{
		String varStr = null;
		String varKeysStr = null;
		Modulo modulo = null;
		if (expression instanceof NullValue/* || expression instanceof Function || expression instanceof SignedExpression*/
				|| expression instanceof JdbcParameter || expression instanceof JdbcNamedParameter
				|| expression instanceof HexValue /*|| expression instanceof CaseExpression
				|| expression instanceof WhenClause*/ || expression instanceof DateTimeLiteralExpression
				|| expression instanceof Concat || expression instanceof ExtractExpression
				|| expression instanceof IntervalExpression || expression instanceof RegExpMatchOperator
				|| expression instanceof IntervalExpression || expression instanceof NumericBind
				|| expression instanceof KeepExpression)
		{
			return "";
		} else if(expression instanceof SignedExpression){
			SignedExpression signedExpression= (SignedExpression)expression;
			buffer.append(signedExpression.getSign());
			buffer.append(expressionParse(signedExpression.getExpression(), buffer, bufferValue,bufferCaseStr,
					selectExpressionItem, alias, containMap, ++count));
		} else if (expression instanceof DoubleValue || expression instanceof LongValue || expression instanceof DateValue
				|| expression instanceof TimeValue || expression instanceof TimestampValue )
		{
			buffer.append(expression);
		} else if (expression instanceof StringValue)
		{
			return "\""+StringEscapeUtils.escapeJava(String.valueOf(((StringValue) expression).getValue()))+"\"";
		} else if (expression instanceof Column)
		{
			Column column = (Column) expression;// 获取字段
			column = (Column) expression;
			List<String> cloumns = getColumnNames(column.getFullyQualifiedName());
			String finalClomn = getFinalExpressionColumnName(cloumns);
			varStr = " __temp_express_" + count + "__";
			boolean break_ = true;
			while (break_)
			{
				if (containMap.containsKey(varStr) && !containMap.get(varStr).equals(finalClomn))
				{
					varStr = " __temp_express_" + (++count) + "__";
				} else
				{
					break_ = false;
				}
			}
			// 如果为表达式的值，如何支持
			bufferValue.append(" " + varStr + " = null;\r\n");
			varKeysStr = varStr + "keys__";
			bufferValue.append(" " + varKeysStr + "= null;\r\n");
			bufferValue.append(" " + varKeysStr + "=\"" + StringEscapeUtils.escapeJava(JSONObject.toJSONString(cloumns))
					+ "\";\r\n");
			bufferValue.append(" " + varStr + " = JSONObjectUtil.getList(root," + varKeysStr + ");\r\n");
			buffer.append(varStr);
			// containMap.put(finalClomn, varStr);
			containMap.put(varStr, finalClomn);
		} else if (expression instanceof Addition || expression instanceof Division
				|| expression instanceof Multiplication || expression instanceof Subtraction)
		{
			BinaryExpression binaryExpression = (BinaryExpression) expression;
			// (Expression expression, StringBuilder buffer,SelectExpressionItem
			// selectExpressionItem, Alias alias, Map<String, String>
			// containMap,Integer count)
			buffer.append(expressionParse(binaryExpression.getLeftExpression(), buffer, bufferValue,bufferCaseStr,
					selectExpressionItem, alias, containMap, ++count));
			buffer.append(" " + binaryExpression.getStringExpression() + " ");
			buffer.append(expressionParse(binaryExpression.getRightExpression(), buffer, bufferValue,bufferCaseStr,
					selectExpressionItem, alias, containMap, ++count));
		} else if (expression instanceof Parenthesis)
		{
			Parenthesis parenthesis = (Parenthesis) expression;
			if (parenthesis.isNot())
			{
				return "";
			}
			buffer.append(" ( ");
			buffer.append(expressionParse(parenthesis.getExpression(), buffer, bufferValue,bufferCaseStr, selectExpressionItem, alias,
					containMap, ++count));
			buffer.append(" ) ");
		} else if (expression instanceof Modulo)
		{
			modulo = (Modulo) expression;
			if (modulo.isNot())
			{
				return "";
			}
			buffer.append(expressionParse(modulo.getLeftExpression(), buffer, bufferValue,bufferCaseStr, selectExpressionItem, alias,
					containMap, ++count));
			buffer.append(" " + modulo.getStringExpression() + " ");
			buffer.append(expressionParse(modulo.getRightExpression(), buffer, bufferValue,bufferCaseStr, selectExpressionItem, alias,
					containMap, ++count));
		} else if(expression instanceof Function){
			Function function= (Function)expression;
			buffer.append(" "+ function.getName()+"(");
			if(function.getParameters() != null){
				List<Expression> expressionslist = function.getParameters().getExpressions();
				if(expressionslist != null){
					for (int i = 0; i < expressionslist.size(); i++)
					{
						if(i != 0){
							buffer.append(", ");
						}
						buffer.append(expressionParse(expressionslist.get(i), buffer, bufferValue,bufferCaseStr, selectExpressionItem, alias,
								containMap, ++count));
					}
				}
			}
			buffer.append(") ");
		} else if(expression instanceof CaseExpression){
			CaseExpression caseExpression = (CaseExpression)expression;
			
			Expression switchExpression =  caseExpression.getSwitchExpression();
			
			List<WhenClause> whenClauseList = caseExpression.getWhenClauses();
			WhenClause whenClause = null;
			Expression whenExpression = null;
			Expression thenExpression = null;
			
			Expression elseExpression = caseExpression.getElseExpression();
			
			StringBuilder bufferCase = new StringBuilder();
			StringBuilder bufferCaseValue = new StringBuilder();
			StringBuilder bufferCaseResult = new StringBuilder();
			if(switchExpression ==null){
				if(whenClauseList != null && whenClauseList.size()>0){
					varStr = " __temp_express_" + count + "__";
					boolean break_ = true;
					while (break_)
					{
						if (containMap.containsKey(varStr))
						{
							varStr = " __temp_express_" + (++count) + "__";
						} else
						{
							break_ = false;
						}
					}
					bufferValue.append(" " + varStr + " = null;\r\n");
					//varKeysStr = varStr + "keys__";
					for(int i =0;i<whenClauseList.size();i++){
						whenClause =  whenClauseList.get(i);
						whenExpression = whenClause.getWhenExpression();
						if(i !=0){
							bufferCaseStr.append(" } else ");
						}
						bufferCase = new StringBuilder();
						bufferCaseValue = new StringBuilder();
						
						bufferCase.append(expressionParse(whenExpression, bufferCase, bufferCaseValue,bufferCaseStr, selectExpressionItem, alias,
								containMap, ++count));
						bufferCaseResult.append(bufferCaseValue);
						
						//条件语句
						bufferCaseStr.append(" if( ");
						bufferCaseStr.append(bufferCase + " ) {");
						bufferCase = new StringBuilder();
						bufferCaseValue = new StringBuilder();
						
						thenExpression = whenClause.getThenExpression();
						bufferCase.append(expressionParse(thenExpression, bufferCase, bufferCaseValue,bufferCaseStr, selectExpressionItem, alias,
								containMap, ++count));
						bufferCaseStr.append(varStr + " = "+ bufferCase+";\r\n");
						bufferCaseResult.append(bufferCaseValue);
						
						
						bufferCaseStr.append("}");
					 }
					
				}
				bufferValue.append(bufferCaseResult);
				bufferCaseStr.append(" else {\r\n");
				bufferCase = new StringBuilder();
				bufferCaseValue = new StringBuilder();
				bufferCase.append(expressionParse(elseExpression, bufferCase, bufferCaseValue,bufferCaseStr, selectExpressionItem, alias,
						containMap, ++count));
				buffer.append(varStr);
				bufferValue.append(bufferCaseValue);
				bufferCaseStr.append(varStr + " = "+ bufferCase+";\r\n");
				bufferCaseStr.append("}\r\n ");
				
			} else {
				bufferCase = new StringBuilder();
				bufferCaseValue = new StringBuilder();
				
				bufferCase.append(expressionParse(switchExpression, bufferCase, bufferCaseValue,bufferCaseStr, selectExpressionItem, alias,
						containMap, ++count));
				//添加值运算
				bufferCaseResult.append(bufferCaseValue);
				
				
				
				varStr = " __temp_express_case_01_" + count + "__";
				boolean break_ = true;
				while (break_)
				{
					if (containMap.containsKey(varStr))
					{
						varStr = " __temp_express_case_01_" + (++count) + "__";
					} else
					{
						break_ = false;
					}
				}
				containMap.put(varStr, null);
				bufferCaseResult.append(varStr+" = "+bufferCase+";\r\n");
				//buffer.append("if("+varStr+"="+";\r\n");
				
	/*			System.out.println(bufferCase);
				System.out.println(bufferCaseValue);*/
				String varStr2 = null;
				String varStr3 = null;
				if(whenClauseList != null && whenClauseList.size()>0){
					varStr3 = " __temp_express_case_03_" + count + "__";
					break_ = true;
					while (break_)
					{
						if (containMap.containsKey(varStr3))
						{
							varStr3 = " __temp_express_case_03_" + (++count) + "__";
						} else
						{
							break_ = false;
						}
					}
					containMap.put(varStr3, null);
					bufferValue.append(" " + varStr3 + " = null;\r\n");
					//varKeysStr = varStr + "keys__";
					for(int i =0;i<whenClauseList.size();i++){
						varStr2 = " __temp_express_case_02_" + count + "__";
						break_ = true;
						while (break_)
						{
							if (containMap.containsKey(varStr2))
							{
								varStr2 = " __temp_express_case_02_" + (++count) + "__";
							} else
							{
								break_ = false;
							}
						}
						containMap.put(varStr2, null);
						whenClause =  whenClauseList.get(i);
						whenExpression = whenClause.getWhenExpression();
						if(i !=0){
							bufferCaseStr.append(" } else ");
						}
						bufferCase = new StringBuilder();
						bufferCaseValue = new StringBuilder();
						
						bufferCase.append(expressionParse(whenExpression, bufferCase, bufferCaseValue,bufferCaseStr, selectExpressionItem, alias,
								containMap, ++count));
						bufferCaseResult.append(bufferCaseValue);
						bufferCaseResult.append(varStr2+" = "+bufferCase+";\r\n");
						//条件语句
						bufferCaseStr.append(" if( ");
						bufferCaseStr.append(varStr +"=="+ varStr2 + " ) {");
						bufferCase = new StringBuilder();
						bufferCaseValue = new StringBuilder();
						
						thenExpression = whenClause.getThenExpression();
						bufferCase.append(expressionParse(thenExpression, bufferCase, bufferCaseValue,bufferCaseStr, selectExpressionItem, alias,
								containMap, ++count));
						bufferCaseStr.append(varStr3 + " = "+ bufferCase+";\r\n");
						bufferCaseResult.append(bufferCaseValue);
						
						bufferCaseStr.append("}");
					 }
					
				}
				bufferValue.append(bufferCaseResult);
				bufferCaseStr.append(" else {\r\n");
				bufferCase = new StringBuilder();
				bufferCaseValue = new StringBuilder();
				bufferCase.append(expressionParse(elseExpression, bufferCase, bufferCaseValue,bufferCaseStr, selectExpressionItem, alias,
						containMap, ++count));
				buffer.append(varStr3);
				bufferValue.append(bufferCaseValue);
				bufferCaseStr.append(varStr3 + " = "+ bufferCase+";\r\n");
				bufferCaseStr.append("}\r\n ");
			}
			
		} else if (expression instanceof EqualsTo || expression instanceof GreaterThan
				|| expression instanceof GreaterThanEquals || expression instanceof MinorThan
				|| expression instanceof MinorThanEquals || expression instanceof NotEqualsTo)
		{
			// > >= = < <= != <>处理
			ComparisonOperator comparisonOperator = (ComparisonOperator) expression;
			if (comparisonOperator.isNot())
			{
				return "";
			}
			// comparisonOperator.getStringExpression();
		
			buffer.append(expressionParse(comparisonOperator.getLeftExpression(),  buffer,bufferValue,bufferCaseStr,selectExpressionItem, alias, containMap,count));
			if(expression instanceof NotEqualsTo){
				buffer.append(" != ");
			} else {
				buffer.append(" " + ((expression instanceof EqualsTo ) ? "=" :"")+comparisonOperator.getStringExpression() + " ");
			}
			buffer.append(expressionParse(comparisonOperator.getRightExpression(),  buffer,bufferValue,bufferCaseStr,selectExpressionItem, alias, containMap,++count));
		} else if (expression instanceof AndExpression)
		{
			AndExpression andExpression = (AndExpression) expression;
			if (andExpression.isNot())
			{
				return "";
			}
			//2
			buffer.append(expressionParse(andExpression.getLeftExpression(),  buffer,bufferValue, bufferCaseStr,selectExpressionItem, alias,containMap,++count));
			buffer.append(" " + andExpression.getStringExpression().toLowerCase() + " ");
			//2
			buffer.append(expressionParse(andExpression.getRightExpression(),  buffer,bufferValue, bufferCaseStr,selectExpressionItem, alias,containMap,++count));
		} else if (expression instanceof OrExpression)
		{
			OrExpression orExpression = (OrExpression) expression;
			if (orExpression.isNot())
			{
				return "";
			}
			buffer.append(expressionParse(orExpression.getLeftExpression(),  buffer,bufferValue, bufferCaseStr,selectExpressionItem, alias,containMap,++count));
			buffer.append(" " + orExpression.getStringExpression().toLowerCase() + " ");
			buffer.append(expressionParse(orExpression.getRightExpression(),  buffer,bufferValue, bufferCaseStr,selectExpressionItem, alias,containMap,++count));
		} else if (expression instanceof InExpression)
		{
			// ( + - * / )加减乘除处理
			InExpression inExpression = (InExpression) expression;
			
			ItemsList itemsList = inExpression.getRightItemsList();
			if(inExpression.getLeftExpression() == null){
				ExpressionList expressionList = (ExpressionList) inExpression.getLeftItemsList();
				List<Expression> expressionslist = (List<Expression>)expressionList.getExpressions();
				buffer.append("(");
				for (int i = 0; i < expressionslist.size(); i++)
				{
					if(i != 0){
						buffer.append(", ");
					}
					buffer.append(expressionParse(expressionslist.get(i),  buffer,bufferValue,bufferCaseStr,selectExpressionItem, alias, containMap,++count));
				}
				buffer.append(")");
			} else {
				buffer.append(expressionParse(inExpression.getLeftExpression(),  buffer,bufferValue,bufferCaseStr,selectExpressionItem, alias, containMap,count));
			}
			
			if (inExpression.isNot())
			{
				return "";
			} else if (itemsList instanceof SubSelect)
			{
				return "";
			}
			
			buffer.append(" in (");
			ExpressionList expressionList = (ExpressionList) inExpression.getRightItemsList();
			List<Expression> expressionslist = (List<Expression>)expressionList.getExpressions();
			for (int i = 0; i < expressionslist.size(); i++)
			{
				if(i != 0){
					buffer.append(", ");
				}
				buffer.append(expressionParse(expressionslist.get(i),  buffer,bufferValue, bufferCaseStr,selectExpressionItem, alias,containMap,++count));
			}
			buffer.append(")");
			//return expression.toString();
		}
		return "";
	}

	public String getFinalExpressionColumnName(List<String> source)
	{
		StringBuffer result = new StringBuffer();
		result.append("`" + source.get(0));
		for (int i = 1; i < source.size(); i++)
		{
			result.append("`.`" + source.get(i));
		}
		result.append("`");
		return result.toString();
	}

	public String getFinalColumnName(List<String> source)
	{
		StringBuffer result = new StringBuffer();
		result.append(source.get(0));
		for (int i = 1; i < source.size(); i++)
		{
			result.append(source.get(i));
		}
		return result.toString();
	}

	public List<String> getColumnNames(String source)
	{
		if (source == null)
		{
			return null;
		}
		int start = source.indexOf("`");
		if (start == -1)
		{
			List<String> result = new ArrayList<>();
			Collections.addAll(result, source.split("\\."));
			return result;
		}
		List<String> result = new ArrayList<>();
		int end = -1;
		start = 0;
		int temp = 0;
		String tempString = null;
		for (int i = 0; i < source.split("`").length - 1; i++)
		{
			temp = source.indexOf("`", start);
			// 如果相等，并且为第一个

			if (i == 0 && start == temp)
			{
				end = source.indexOf("`", temp + 1);
				if (end == -1)
					break;
				tempString = source.substring(temp + 1, end);
				result.add(tempString);
				start = end + 2;
			} else if (i == 0 && start != temp)
			{
				end = source.indexOf("`", temp + 1);
				// tempString = source.substring(temp+1, end);
				Collections.addAll(result, source.substring(0, temp).split("\\."));

				if (end == -1)
					break;
				tempString = source.substring(temp + 1, end);
				result.add(tempString);
				start = end + 2;
			} else if (temp - 2 == end)
			{// 如果为连续的
				end = source.indexOf("`", temp + 1);
				if (end == -1)
					break;
				tempString = source.substring(temp + 1, end);
				result.add(tempString);
				start = end + 2;
			} else
			{
				Collections.addAll(result, source.substring(end + 2, temp).split("\\."));
				end = source.indexOf("`", temp + 1);
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
			Collections.addAll(result, source.substring(end + 2).trim().split("\\."));
		}
		return result;

	}
	
	
	/*public static void main(String[] args)
	{
		String exp = "import com.alibaba.fastjson.JSONObject;" +
				"import com.foxconn.core.pro.server.rule.engine.core.common.util.JSONObjectUtil;";
		ExpressRunner runner = new ExpressRunner();
		
		
		runner.addFunction("abs", new OperatorAbs("abs"));
		runner.addFunction("asin", new OperatorAsin("asin"));

		runner.addFunction("concat_ws", new OperatorConcatWs("concat_ws"));
		runner.addFunction("concat", new OperatorConcat("concat"));
		runner.addFunction("cos", new OperatorCos("cos"));
		runner.addFunction("cosh", new OperatorCosh("cosh"));
		runner.addFunction("crypto", new OperatorDigest("crypto"));

		runner.addFunction("deviceName", new OperatorDeviceName("deviceName"));

		runner.addFunction("endswith", new OperatorEndWith("endswith"));
		runner.addFunction("exp", new OperatorExp("exp"));

		runner.addFunction("floor", new OperatorFloor("floor"));

		runner.addFunction("log", new OperatorLog("log"));
		runner.addFunction("lower", new OperatorLower("lower"));

		runner.addFunction("modulo", new OperatorMod("modulo"));

		runner.addFunction("nanvl", new OperatorNanvl("nanvl"));
		runner.addFunction("newuuid", new OperatorNewuuid("newuuid"));

		runner.addFunction("power", new OperatorPower("power"));
		runner.addFunction("productId", new OperatorProductId("productId"));

		runner.addFunction("random", new OperatorRandom("random"));
		runner.addFunction("randint", new OperatorRandInt("randint"));	
		runner.addFunction("replace", new OperatorReplace("replace"));

		runner.addFunction("sin", new OperatorSin("sin"));
		runner.addFunction("sinh", new OperatorSinh("sinh"));

		runner.addFunction("tan", new OperatorTan("tan"));
		runner.addFunction("tanh", new OperatorTanh("tanh"));
		runner.addFunction("timestamp", new OperatorTimestamp("timestamp"));
		runner.addFunction("topic", new OperatorTopic("topic"));
		
		runner.addFunction("upper", new OperatorUpper("upper"));
		
		FieldParser conditionParser = new FieldParser();
		
		IExpressContext<String, Object> expressContext = new DefaultContext<>();
		expressContext.put(CommonConstant.QL_DEVICE_NAME, "test_deviceName_0001");
		JSONObject root = new JSONObject();
		root.put("tan_v", 88);
		root.put("str1", 99999);
		root.put("aa", 99999);
		//root.put("aiddd", "yyy");
		//expressContext.put("tan_v", 88);
		//expressContext.put("str1", 99999);
		expressContext.put("root", root);
		expressContext.put("aa", 123);
		
		conditionParser.setOriginalValue("case deviceName() when  ('test_deviceName_0001') then '55555rrr5'+aa else '8888888'+aa end as deviceNam_p,case  when  (deviceName() = 'test_deviceName_000122') then '55555rrr5'+aa else '8888888'+aa end as deviceNam_p22");
		//conditionParser.setOriginalValue("case  when deviceName() <> 'test_deviceName_0001' and 1=1 then '555555' else '8888888' end as deviceNam_p");
		String buffer = conditionParser.getQLExpress();
		//System.out.println(buffer);
		//执行表达式，并将结果赋给r
		Object r = null;
		try
		{
			System.out.println(exp+buffer);
			r = runner.execute(exp+buffer,expressContext,null,false,true);
			System.out.println(r);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/
}