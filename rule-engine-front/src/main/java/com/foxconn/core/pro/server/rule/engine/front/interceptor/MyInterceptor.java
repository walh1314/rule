/**
 * Project Name:rule-engine-front
 * File Name:MyInterceptor.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.interceptor
 * Date:2018年9月8日下午5:32:35
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.interceptor;

import java.sql.Connection;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.common.service.CoreproCommonService;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.UserDataBaseBean;
import com.foxconn.core.pro.server.rule.engine.front.common.util.SystemUtil;
import com.foxconn.core.pro.server.rule.engine.front.entity.UserInfo;
import com.foxconn.core.pro.server.rule.engine.front.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.front.exception.ErrorCodes;
import com.foxconn.core.pro.server.rule.engine.front.interceptor.parser.BaseDeParser;
import com.foxconn.core.pro.server.rule.engine.front.interceptor.parser.MyDeleteDeParser;
import com.foxconn.core.pro.server.rule.engine.front.interceptor.parser.MyInsertDeParser;
import com.foxconn.core.pro.server.rule.engine.front.interceptor.parser.MySelectDeParser;
import com.foxconn.core.pro.server.rule.engine.front.interceptor.parser.MyUpdateDeParser;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserManager;

/**
 * ClassName:MyInterceptor <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月8日 下午5:32:35 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Intercepts(
{ @Signature(method = CommonConstant.PREPARE, type = StatementHandler.class, args =
		{ Connection.class, Integer.class }) })
@Slf4j
@Component
public class MyInterceptor implements Interceptor
{
	CCJSqlParserManager parserManager = new CCJSqlParserManager();

	@Autowired
	private SystemUtil systemUtil;

	@Autowired
	private CoreproCommonService coreproCommonService;

	@Override
	public Object intercept(Invocation invocation) throws Throwable
	{
		StatementHandler handler = (StatementHandler) invocation.getTarget();
		// 由于mappedStatement为protected的，所以要通过反射获取
		MetaObject statementHandler = SystemMetaObject.forObject(handler);
		// mappedStatement中有我们需要的方法id
		MappedStatement mappedStatement = (MappedStatement) statementHandler.getValue(CommonConstant.DELEGATE_MAPPEDSTATEMENT);
		// 获取sql
		BoundSql boundSql = handler.getBoundSql();
		String sql = boundSql.getSql();
		// 获取方法id
		// String id = mappedStatement.getId();
		// 获得方法类型
		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

		// Statement statement = parserManager.parse(new StringReader(sql));
		BaseDeParser baseDeParser = null;
		if (SqlCommandType.SELECT.equals(sqlCommandType))
		{
			baseDeParser = new MySelectDeParser();
			String tempSql = baseDeParser.getSql(sql, getSchema());
			statementHandler.setValue(CommonConstant.DELEGATE_BOUNDSQL_SQL, tempSql);
		} else if (SqlCommandType.UPDATE.equals(sqlCommandType))
		{
			baseDeParser = new MyUpdateDeParser();
			String tempSql = baseDeParser.getSql(sql, getSchema());
			statementHandler.setValue(CommonConstant.DELEGATE_BOUNDSQL_SQL, tempSql);
		} else if (SqlCommandType.INSERT.equals(sqlCommandType))
		{
			baseDeParser = new MyInsertDeParser();
			String tempSql = baseDeParser.getSql(sql, getSchema());
			statementHandler.setValue(CommonConstant.DELEGATE_BOUNDSQL_SQL, tempSql);
		} else if (SqlCommandType.DELETE.equals(sqlCommandType))
		{
			baseDeParser = new MyDeleteDeParser();
			String tempSql = baseDeParser.getSql(sql, getSchema());
			statementHandler.setValue(CommonConstant.DELEGATE_BOUNDSQL_SQL, tempSql);
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target)
	{
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties)
	{

	}

	private String getSchema()
	{
		UserDataBaseBean tenantDb = systemUtil.getUserDataBaseBean();
		if (tenantDb != null && tenantDb.getDb() != null && StringUtils.isNotBlank(tenantDb.getDb()))
		{
			return tenantDb.getDb().trim();
		} else
		{
			UserInfo userInfo = systemUtil.getCurrentUser();
			if (userInfo != null && userInfo.getUserId() != null && StringUtils.isNotBlank(userInfo.getUserId()))
			{
				String userId = userInfo.getUserId().trim();

				UserDataBaseBean result = coreproCommonService.getUserDataBase(userId);
				// CloundMap<TenantDb> result =
				// accountDbService.getTenantDb(userId,requestMap);
				// 如果为成功，则进行处理
				if (result != null)
				{
					if (result.getDb() != null
							&& StringUtils.isNotBlank(result.getDb()))
					{
						result.setDeviceOwner(userId);
						systemUtil.setUserDataBaseBean(result);
						return result.getDb().trim();
					} else
					{
						throw new BaseException(ErrorCodes.COREPRO_GET_TENANT_DB_FAIL);
					}
				} else
				{
					log.error("get TenantDb error ");
					throw new BaseException(ErrorCodes.COREPRO_GET_TENANT_DB_FAIL);
				}
			}
		}
		return null;
	}

}
