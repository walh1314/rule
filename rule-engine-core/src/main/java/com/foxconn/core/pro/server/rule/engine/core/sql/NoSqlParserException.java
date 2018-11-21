package com.foxconn.core.pro.server.rule.engine.core.sql;

public class NoSqlParserException extends Exception
{
	private static final long serialVersionUID = 3065934017540313394L;

	NoSqlParserException()
	{

	}

	NoSqlParserException(String sql)
	{
		// 调用父类方法
		super(sql);
	}
}