/**
 * Project Name:rule-engine-front
 * File Name:MyExpressionDeParser.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.interceptor.parser
 * Date:2018年9月10日上午11:19:01
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.interceptor.parser;

import lombok.Getter;
import lombok.Setter;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

/**
 * ClassName:MyExpressionDeParser <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月10日 上午11:19:01 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class MyExpressionDeParser extends ExpressionDeParser
{
	private String prefix = "";

	public MyExpressionDeParser()
	{
		super();
	}
	public MyExpressionDeParser(SelectVisitor selectVisitor, StringBuilder buffer, String prefix)
	{
		super(selectVisitor, buffer);
		this.prefix = prefix;
	}

	@Override
	public void visit(SubSelect subSelect)
	{
		super.visit(subSelect);
	}
}
