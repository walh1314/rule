/**
 * Project Name:rule-engine-front
 * File Name:MyItemsListVisitor.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.interceptor.parser
 * Date:2018年9月10日上午10:25:51
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.interceptor.parser;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

/**
 * ClassName:MyItemsListVisitor <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月10日 上午10:25:51 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class MyItemsListVisitor implements ItemsListVisitor
{
	private StringBuilder buffer;
	private String prefix;

	public MyItemsListVisitor()
	{

	}

	public MyItemsListVisitor(StringBuilder buffer, String prefix)
	{

		this.buffer = buffer;
		this.prefix = prefix;
	}

	@Override
	public void visit(SubSelect subSelect)
	{
		if (subSelect != null)
		{
			ExpressionDeParser expressionDeParser = new ExpressionDeParser();
			MySelectDeParser deparser01 = new MySelectDeParser(expressionDeParser, new StringBuilder(), this.prefix);
			expressionDeParser.setSelectVisitor(deparser01);
			expressionDeParser.setBuffer(buffer);
			subSelect.getSelectBody().accept(deparser01);
		}
	}

	@Override
	public void visit(ExpressionList expressionList)
	{
		Expression expression = null;
		SubSelect select = null;
		for (int i = 0; i < expressionList.getExpressions().size(); i++)
		{
			expression = expressionList.getExpressions().get(i);
			if (expression instanceof net.sf.jsqlparser.statement.select.SubSelect)
			{
				select = (SubSelect) expression;

				if (select != null)
				{
					
					ExpressionDeParser expressionDeParser = new ExpressionDeParser();
					MySelectDeParser deparser01 = new MySelectDeParser(expressionDeParser, new StringBuilder(), this.prefix);
					expressionDeParser.setSelectVisitor(deparser01);
					expressionDeParser.setBuffer(buffer);
					select.getSelectBody().accept(deparser01);
				}
			}
		}
	}

	@Override
	public void visit(MultiExpressionList multiExpressionList)
	{
	}

}
