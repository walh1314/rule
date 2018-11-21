package com.foxconn.core.pro.server.rule.engine.core.sql;

import lombok.Getter;
import lombok.Setter;

/**
 * 单句Sql解析器，单句即非嵌套的意思 ClassName: BaseSingleSqlParser <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年8月23日 上午11:40:27 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 */
@Getter
@Setter
public abstract class BaseSingleParser
{
	protected String originalValue;

	public BaseSingleParser()
	{

	}

	public BaseSingleParser(String originalValue)
	{
		this.originalValue = originalValue;
	}

	public abstract String getQLExpress();

	public abstract String getQLExpress(String splitPattern);

	public abstract String getHeaders();
}