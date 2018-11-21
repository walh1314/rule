/**
 * Project Name:rule-engine-front
 * File Name:MySelectDeParser.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.interceptor
 * Date:2018年9月8日下午5:38:53
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.interceptor.parser;

import org.apache.commons.lang.StringUtils;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Pivot;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

/**
 * ClassName:MySelectDeParser <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月8日 下午5:38:53 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
@Slf4j
public class MySelectDeParser extends SelectDeParser implements BaseDeParser
{
	private String prefix = "";
	
	public MySelectDeParser()
	{
	}

	public MySelectDeParser(String prefix)
	{
		if (this.prefix == null || StringUtils.isBlank(this.prefix))
		{
			this.prefix = prefix;
		}
	}

	public MySelectDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer, String prefix)
	{
		super(expressionVisitor, buffer);
		if (this.prefix == null || StringUtils.isBlank(this.prefix))
		{
			this.prefix = prefix;
		}
	}

	@Override
	public void visit(Table tableName)
	{
		String schema = tableName.getSchemaName();
		if (StringUtils.isNotBlank(prefix))
		{
			if (schema != null && StringUtils.isNotBlank(schema))
			{
				tableName.setSchemaName(prefix + CommonConstant.SPOT + schema);
			} else
			{
				tableName.setSchemaName(prefix);
			}
		}
		StringBuilder buffer = getBuffer();
		buffer.append(tableName.getFullyQualifiedName());
		Pivot pivot = tableName.getPivot();
		if (pivot != null)
		{
			pivot.accept(this);
		}
		Alias alias = tableName.getAlias();
		if (alias != null)
		{
			buffer.append(alias);
		}
	}

	@Override
	public String getSql(String source,String prefix)
	{
		Select select;
		try
		{
			select = (Select) CCJSqlParserUtil.parse(source);
			StringBuilder buffer = new StringBuilder();
			ExpressionDeParser expressionDeParser = new ExpressionDeParser();
			SelectDeParser deparser = new MySelectDeParser(expressionDeParser, buffer, prefix);
			expressionDeParser.setSelectVisitor(deparser);
			expressionDeParser.setBuffer(buffer);
			select.getSelectBody().accept(deparser);

			return select.toString();
		} catch (JSQLParserException e)
		{
			log.error(e.getMessage(), e);

		}
		return source;
	}
}
