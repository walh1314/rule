/**
 * Project Name:rule-engine-front
 * File Name:MyUpdateDeParser.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.interceptor.parser
 * Date:2018年9月10日上午9:00:09
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.interceptor.parser;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.UpdateDeParser;

/**
 * ClassName:MyUpdateDeParser <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月10日 上午9:00:09 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
@Slf4j
public class MyUpdateDeParser extends UpdateDeParser implements BaseDeParser
{
	private String prefix = "";
	
	public MyUpdateDeParser(){
		super();
	}

	public MyUpdateDeParser(ExpressionVisitor expressionVisitor, SelectVisitor selectVisitor, StringBuilder buffer,
			String prefix)
	{
		super(expressionVisitor, selectVisitor, buffer);
		
		if (this.prefix == null || StringUtils.isBlank(this.prefix))
		{
			this.prefix = prefix;
		}
	}

	@Override
	public String getSql(String source,String prefix)
	{
		Update update;
		try
		{
			update = (Update) CCJSqlParserUtil.parse(source);
			List<Table> tables = update.getTables();
			if (tables != null && tables.size() > 0)
			{
				for (int i = 0; i < tables.size(); i++)
				{
					if (prefix != null && StringUtils.isNotBlank(prefix))
					{
						tables.get(i).setSchemaName(prefix);
					}
				}
				update.setTables(tables);
			}
			StringBuilder buffer = new StringBuilder();
			MySelectDeParser mySelectDeParser = new MySelectDeParser(prefix);
			MyUpdateDeParser myUpdateDeParser = new MyUpdateDeParser(new ExpressionDeParser(mySelectDeParser, buffer),
					mySelectDeParser, buffer, prefix);
			myUpdateDeParser.deParse(update);
			return update.toString();
		} catch (JSQLParserException e)
		{
			log.error(e.getMessage(), e);
		}
		return source;
	}
}
