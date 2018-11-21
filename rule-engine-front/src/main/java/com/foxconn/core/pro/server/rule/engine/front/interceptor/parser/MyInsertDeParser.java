/**
 * Project Name:rule-engine-front
 * File Name:MyInsertDeParser.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.interceptor.parser
 * Date:2018年9月10日上午9:03:47
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.interceptor.parser;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.InsertDeParser;

/**
 * ClassName:MyInsertDeParser <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月10日 上午9:03:47 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
@Slf4j
public class MyInsertDeParser extends InsertDeParser implements BaseDeParser
{
	private String prefix = "";

	public MyInsertDeParser()
	{
		super();
	}
	public MyInsertDeParser(ExpressionVisitor expressionVisitor, SelectVisitor selectVisitor, StringBuilder buffer,
			String prefix)
	{
		super(expressionVisitor, selectVisitor, buffer);
		if (StringUtils.isBlank(this.prefix))
		{
			this.prefix = prefix;
		}

	}

	@Override
	public void visit(SubSelect subSelect)
	{

		if (subSelect != null)
		{
			ExpressionDeParser expressionDeParser = new ExpressionDeParser();
			MySelectDeParser deparser01 = new MySelectDeParser(expressionDeParser, super.getBuffer(), this.prefix);
			expressionDeParser.setSelectVisitor(deparser01);
			expressionDeParser.setBuffer(super.getBuffer());
			subSelect.getSelectBody().accept(deparser01);
		}
	}

	@Override
	public void visit(ExpressionList expressionList)
	{
		Expression expression = null;
		SubSelect select = null;
		super.getBuffer().append(CommonConstant.VALUES);
		for (Iterator<Expression> iter = expressionList.getExpressions().iterator(); iter.hasNext();)
		{
			expression = iter.next();
			if (expression instanceof net.sf.jsqlparser.statement.select.SubSelect)
			{
				select = (SubSelect) expression;

				if (select != null)
				{
					ExpressionDeParser expressionDeParser = new ExpressionDeParser();
					MySelectDeParser deparser01 = new MySelectDeParser(expressionDeParser, super.getBuffer(),
							this.prefix);

					expressionDeParser.setSelectVisitor(deparser01);
					expressionDeParser.setBuffer(super.getBuffer());
					select.getSelectBody().accept(deparser01);
				}
			}
			if (iter.hasNext())
			{
				super.getBuffer().append(CommonConstant.COMMA);
			}
		}
		super.getBuffer().append(CommonConstant.RIGHT_PARENTHESIS);
	}

	@Override
	public String getSql(String source, String prefix)
	{
		Insert insert;
		try
		{
			insert = (Insert) CCJSqlParserUtil.parse(source);
			Table table = insert.getTable();
			if (table != null)
			{
				table.setSchemaName(prefix);
				table.setName(insert.getTable().getName());
				insert.setTable(table);
			}

			StringBuilder buffer = new StringBuilder();
			ExpressionDeParser expressionDeParser01 = new ExpressionDeParser();
			MySelectDeParser mySelectDeParser = new MySelectDeParser(prefix);
			MyInsertDeParser myInsertDeParser = new MyInsertDeParser(expressionDeParser01, mySelectDeParser, buffer,
					prefix);
			Select select = insert.getSelect();

			if (select != null)
			{
				ExpressionDeParser expressionDeParser = new ExpressionDeParser();
				MySelectDeParser deparser01 = new MySelectDeParser(expressionDeParser, buffer, prefix);
				expressionDeParser.setSelectVisitor(deparser01);
				expressionDeParser.setBuffer(buffer);
				select.getSelectBody().accept(deparser01);
			}
			myInsertDeParser.deParse(insert);
			return insert.toString();
		} catch (JSQLParserException e)
		{
			log.error(e.getMessage(), e);
		}
		return source;
	}
}
