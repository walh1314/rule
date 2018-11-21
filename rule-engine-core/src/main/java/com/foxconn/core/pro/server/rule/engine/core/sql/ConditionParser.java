package com.foxconn.core.pro.server.rule.engine.core.sql;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.config.template.RuleTemplateProperties;
import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
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
@Service(CommonConstant.CONDITIONPARSER)
public class ConditionParser extends BaseSingleParser
{

	@Autowired
	private RuleTemplateProperties ruleTemplateProperties;

	public ConditionParser()
	{
		super();
	}

	public ConditionParser(String originalValue)
	{
		super(originalValue);
	}

	/**
	 * 
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see com.foxconn.core.pro.server.rule.engine.core.sql.BaseSingleParser#getQLExpress()
	 */
	@Override
	public String getQLExpress(String splitPattern)
	{
		if (StringUtils.isEmpty(originalValue))
		{
			return null;
		}
		String sqlCondition = formatterString(originalValue);

		CCJSqlParserManager parser = new CCJSqlParserManager();
		// 关键字过滤纸针对保留关键字
		Statement stmt = null;
		StringBuilder buffer = new StringBuilder();
		StringBuilder bufferValue = new StringBuilder();
		try
		{
			stmt = parser.parse(new StringReader(CommonConstant.SELECT_FROM + sqlCondition));
		} catch (JSQLParserException e)
		{
			
			log.error("getQLExpress Exception",e);
			return null;
		}
		if (stmt instanceof Select)
		{
			Select selectStatement = (Select) stmt;
			PlainSelect selectBody = (PlainSelect) selectStatement.getSelectBody();
			Expression expression = selectBody.getWhere();
		
			Map<String, String> containMap = new HashMap<String, String>(20);
			Integer count = 0;
			expressionParse(expression, buffer,bufferValue, containMap,++count);
		}
		StringBuffer result = new StringBuffer();
		//TODO 修改头部
		result.append(getHeaders());
		result.append(CommonConstant.R_N);
		result.append(bufferValue.toString());
		result.append(CommonConstant.R_N);
		result.append(CommonConstant.RETU_RN);
		result.append(buffer);
		result.append(CommonConstant.COMMA);
		return result.toString();
	}

	@Override
	public String getQLExpress()
	{
		return getQLExpress(CommonConstant.Nu_LL);
	}

	private String formatterString(String str)
	{
		String regEx = CommonConstant.REG_EX;
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		String[] words = pattern.split(str);
		if (words.length > 0)
		{
			int count = 0;
			while (count < words.length)
			{
				if (matcher.find())
				{
					words[count] += CommonConstant.Nu_LL + matcher.group() + CommonConstant.Nu_LL;
				}
				count++;
			}
		}
		char[] strArray = str.toCharArray();
		char rightBrackets = ')';
		int count = 0;
		for (int i = strArray.length - 1; i >= 0; i--)
		{
			if (rightBrackets == strArray[i])
			{
				count++;
			} else
			{
				break;
			}
		}
		if (count > 1)
		{
			for (int j = 0; j < count - 1; j++)
			{
				words[words.length - 1] += CommonConstant.Nu_LL + rightBrackets + CommonConstant.Nu_LL;
			}
		}
		String result = StringUtils.arrayToDelimitedString(words, CommonConstant.NULL_NO);
		result = result.replaceAll(CommonConstant.STRING_EXP, CommonConstant.Nu_LL);
		return result.trim();
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see com.foxconn.core.pro.server.rule.engine.core.sql.BaseSingleParser#getHeaders()
	 */
	@Override
	public String getHeaders()
	{
		String result = CommonConstant.NULL_NO;
		InputStream stencilsetStream = ConditionParser.class.getClassLoader()
				.getResourceAsStream(ruleTemplateProperties.getCondition().getHeaderFile());
		try
		{
			result = IOUtils.toString(stencilsetStream, StandardCharsets.UTF_8);
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public String expressionParse(Expression expression, StringBuilder buffer,StringBuilder bufferValue, Map<String, String> containMap,Integer count)
	{
		AndExpression andExpression = null;
		OrExpression orExpression = null;
		Modulo modulo = null;
		BinaryExpression binaryExpression = null;
		ComparisonOperator comparisonOperator = null;
		InExpression inExpression = null;
		Parenthesis parenthesis = null;

		ItemsList itemsList = null;
		
		String varStr = null;
		String varKeysStr = null;
		Column column = null;
		if (expression instanceof BitwiseAnd || expression instanceof BitwiseOr || expression instanceof BitwiseXor
				|| expression instanceof IsNullExpression || expression instanceof Between)
		{
			return CommonConstant.NULL_NO;
		} else if(expression instanceof SignedExpression){
			SignedExpression signedExpression= (SignedExpression)expression;
			buffer.append(signedExpression.getSign());
			buffer.append(expressionParse(signedExpression.getExpression(),  buffer,bufferValue, containMap,++count));
		} else if (expression instanceof AndExpression)
		{
			andExpression = (AndExpression) expression;
			if (andExpression.isNot())
			{
				return CommonConstant.NULL_NO;
			}
			//2
			buffer.append(expressionParse(andExpression.getLeftExpression(),  buffer,bufferValue, containMap,++count));
			buffer.append(CommonConstant.Nu_LL + andExpression.getStringExpression().toLowerCase() + CommonConstant.Nu_LL);
			//2
			buffer.append(expressionParse(andExpression.getRightExpression(),  buffer,bufferValue, containMap,++count));
		} else if (expression instanceof OrExpression)
		{
			orExpression = (OrExpression) expression;
			if (orExpression.isNot())
			{
				return CommonConstant.NULL_NO;
			}
			buffer.append(expressionParse(orExpression.getLeftExpression(),  buffer,bufferValue, containMap,++count));
			buffer.append(CommonConstant.Nu_LL + orExpression.getStringExpression().toLowerCase() + CommonConstant.Nu_LL);
			buffer.append(expressionParse(orExpression.getRightExpression(),  buffer,bufferValue, containMap,++count));
		} else if (expression instanceof Parenthesis)
		{
			parenthesis = (Parenthesis) expression;
			if (parenthesis.isNot())
			{
				return CommonConstant.NULL_NO;
			}
			buffer.append(CommonConstant.LEFT_PARENTHESIS);
			buffer.append(expressionParse(parenthesis.getExpression(),  buffer,bufferValue, containMap,++count));
			buffer.append(CommonConstant.RIGHT_PARENTHESIS);
		} else if (expression instanceof Column)
		{
			column = (Column) expression;
			List<String> cloumns = getColumnNames(column.getFullyQualifiedName());
			String finalClomn = getFinalColumnName(cloumns);
			if (containMap.containsKey(finalClomn))
			{
				buffer.append(containMap.get(finalClomn));
			} else
			{
				varStr = CommonConstant.TEMP + count +CommonConstant.UNDERLINE;
				boolean break_ = true;
				while(break_){
					if(containMap.containsKey(varStr) && !containMap.get(varStr).equals(finalClomn)){
						varStr = CommonConstant.TEMP + (++count) +CommonConstant.UNDERLINE;
					} else {
						break_ = false;
					}
				}
				bufferValue.append(CommonConstant.Nu_LL + varStr + CommonConstant.EQUA_NULL);
				varKeysStr = CommonConstant.TEMP_KEYS + count +CommonConstant.UNDERLINE;
				bufferValue.append(CommonConstant.Nu_LL + varKeysStr + CommonConstant.EQUANULL);
				bufferValue.append(CommonConstant.Nu_LL + varKeysStr + CommonConstant.EQUAl +StringEscapeUtils.escapeJava(JSONObject.toJSONString(cloumns)) + CommonConstant.R_STR);
				bufferValue.append(CommonConstant.Nu_LL + varStr + CommonConstant.JSON_Object_Util + varKeysStr + CommonConstant.N_STR);
				buffer.append(varStr);
				containMap.put(finalClomn, varStr);
				containMap.put(varStr, finalClomn);
			}
			//buffer.append(expression.toString());
		} else if (expression instanceof Modulo)
		{
			// % 取模处理
			modulo = (Modulo) expression;
			if (modulo.isNot())
			{
				return CommonConstant.NULL_NO;
			}
			buffer.append(expressionParse(modulo.getLeftExpression(),  buffer,bufferValue, containMap,++count));
			buffer.append(CommonConstant.Nu_LL + modulo.getStringExpression() + CommonConstant.Nu_LL);
			buffer.append(expressionParse(modulo.getRightExpression(),  buffer,bufferValue, containMap,++count));
		} else if (expression instanceof EqualsTo || expression instanceof GreaterThan
				|| expression instanceof GreaterThanEquals || expression instanceof MinorThan
				|| expression instanceof MinorThanEquals || expression instanceof NotEqualsTo)
		{
			// > >= = < <= != <>处理
			comparisonOperator = (ComparisonOperator) expression;
			if (comparisonOperator.isNot())
			{
				return CommonConstant.NULL_NO;
			}
			// comparisonOperator.getStringExpression();
			
			buffer.append(expressionParse(comparisonOperator.getLeftExpression(),  buffer,bufferValue, containMap,count));
			if(expression instanceof NotEqualsTo){
				buffer.append(CommonConstant.EQUAL_NO);
			} else {
				buffer.append(CommonConstant.Nu_LL + ((expression instanceof EqualsTo ) ? CommonConstant.EQ :CommonConstant.NULL_NO)+comparisonOperator.getStringExpression() + CommonConstant.Nu_LL);
			}
			buffer.append(expressionParse(comparisonOperator.getRightExpression(),  buffer,bufferValue, containMap,++count));
		} else if (expression instanceof Addition || expression instanceof Division
				|| expression instanceof Multiplication || expression instanceof Subtraction)
		{
			// ( + - * / )加减乘除处理
			binaryExpression = (BinaryExpression) expression;
			if (binaryExpression.isNot())
			{
				return CommonConstant.NULL_NO;
			}
			buffer.append(expressionParse(binaryExpression.getLeftExpression(),  buffer,bufferValue, containMap,++count));
			buffer.append(CommonConstant.Nu_LL + binaryExpression.getStringExpression() + CommonConstant.Nu_LL);
			buffer.append(expressionParse(binaryExpression.getRightExpression(),  buffer,bufferValue, containMap,++count));
		} else if (expression instanceof InExpression)
		{
			// ( + - * / )加减乘除处理
			inExpression = (InExpression) expression;
			
			itemsList = inExpression.getRightItemsList();
			if(inExpression.getLeftExpression() == null){
				ExpressionList expressionList = (ExpressionList) inExpression.getLeftItemsList();
				List<Expression> expressionslist = (List<Expression>)expressionList.getExpressions();
				buffer.append(CommonConstant.LEFTPARENTHESIS);
				for (int i = 0; i < expressionslist.size(); i++)
				{
					if(i != 0){
						buffer.append(CommonConstant.DOUHAO);
					}
					buffer.append(expressionParse(expressionslist.get(i),  buffer,bufferValue, containMap,++count));
				}
				buffer.append(CommonConstant.RIGHTPARENTHESIS);
			} else {
				buffer.append(expressionParse(inExpression.getLeftExpression(),  buffer,bufferValue, containMap,count));
			}
			
			if (inExpression.isNot())
			{
				return CommonConstant.NULL_NO;
			} else if (itemsList instanceof SubSelect)
			{
				return CommonConstant.NULL_NO;
			}
			
			buffer.append(CommonConstant.IN);
			ExpressionList expressionList = (ExpressionList) inExpression.getRightItemsList();
			List<Expression> expressionslist = (List<Expression>)expressionList.getExpressions();
			for (int i = 0; i < expressionslist.size(); i++)
			{
				if(i != 0){
					buffer.append(CommonConstant.DOUHAO);
				}
				buffer.append(expressionParse(expressionslist.get(i),  buffer,bufferValue, containMap,++count));
			}
			buffer.append(CommonConstant.RIGHTPARENTHESIS);
			//return expression.toString();
		} else if (expression instanceof DoubleValue)
		{
			return String.valueOf(((DoubleValue) expression).getValue());
		} else if (expression instanceof LongValue)
		{
			return String.valueOf(((LongValue) expression).getValue());
		} else if (expression instanceof DateValue)
		{
			return String.valueOf(((DateValue) expression).getValue());
		} else if (expression instanceof TimeValue)
		{
			return String.valueOf(((TimeValue) expression).getValue());
		} else if (expression instanceof TimestampValue)
		{
			return String.valueOf(((TimestampValue) expression).getValue());
		} else if (expression instanceof StringValue)
		{
			return CommonConstant.RIGHT_LINE+StringEscapeUtils.escapeJava(String.valueOf(((StringValue) expression).getValue()))+CommonConstant.RIGHT_LINE;
		} else if(expression instanceof Function){
			Function function= (Function)expression;
			buffer.append(CommonConstant.Nu_LL+ function.getName()+CommonConstant.LEFTPARENTHESIS);
			if(function.getParameters() != null){
				List<Expression> expressionslist = function.getParameters().getExpressions();
				if(expressionslist != null){
					for (int i = 0; i < expressionslist.size(); i++)
					{
						if(i != 0){
							buffer.append(CommonConstant.DOUHAO);
						}
						buffer.append(expressionParse(expressionslist.get(i),  buffer,bufferValue, containMap,++count));
					}
				}
			}
			buffer.append(CommonConstant.RIGHT);
		}
		return CommonConstant.NULL_NO;
	}

	public String getFinalColumnName(List<String> source)
	{
		StringBuffer result = new StringBuffer();
		result.append(CommonConstant.QUOTATION_MARK + source.get(0));
		for (int i = 1; i < source.size(); i++)
		{
			result.append(CommonConstant.PROINT_TWO + source.get(i));
		}
		result.append(CommonConstant.QUOTATION_MARK);
		return result.toString();
	}

	public List<String> getColumnNames(String source)
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
	/*
	public static void main(String[] args)
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
		
		ConditionParser conditionParser = new ConditionParser();
		
		IExpressContext<String, Object> expressContext = new DefaultContext<>();
		expressContext.put(CommonConstant.QL_DEVICE_NAME, "test_deviceName_0001");
		JSONObject root = new JSONObject();
		root.put("tan_v", 88);
		root.put("str1", 99999);
		//root.put("aiddd", "yyy");
		//expressContext.put("tan_v", 88);
		//expressContext.put("str1", 99999);
		expressContext.put("root", root);
		
		
		conditionParser.setOriginalValue("aiddd = concat(str1,tan(tan_v)) or deviceName() ='test_dev11iceName_0001'");
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
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		
	}*/
}