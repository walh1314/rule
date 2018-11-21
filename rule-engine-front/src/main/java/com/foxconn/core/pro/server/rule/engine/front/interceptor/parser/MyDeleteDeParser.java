/**
 * Project Name:rule-engine-front
 * File Name:MyDeleteDeParser.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.interceptor.parser
 * Date:2018年9月10日上午9:02:17
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.interceptor.parser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.util.deparser.DeleteDeParser;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

/**
 * ClassName:MyDeleteDeParser <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月10日 上午9:02:17 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
@Slf4j
public class MyDeleteDeParser extends DeleteDeParser implements BaseDeParser
{
	private String prefix = "";

	public MyDeleteDeParser()
	{
		super();
	}
	public MyDeleteDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer, String prefix)
	{
		super(expressionVisitor, buffer);
		this.prefix = prefix;
	}

	@Override
	public String getSql(String source, String prefix)
	{
		Delete delete;
		try
		{
			delete = (Delete) CCJSqlParserUtil.parse(source);

			Table table = delete.getTable();
			if (table != null)
			{
				table.setSchemaName(prefix);
				table.setName(delete.getTable().getName());
				delete.setTable(table);
			}

			StringBuilder buffer = new StringBuilder();
			MySelectDeParser mySelectDeParser = new MySelectDeParser(prefix);
			MyDeleteDeParser myDeleteDeParser = new MyDeleteDeParser(new ExpressionDeParser(mySelectDeParser, buffer),
					buffer, prefix);
			myDeleteDeParser.deParse(delete);
			return delete.toString();
		} catch (JSQLParserException e)
		{
			log.error(e.getMessage(), e);
		}
		return source;
	}
}
