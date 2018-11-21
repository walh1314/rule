package com.foxconn.core.pro.server.rule.engine.front.config.ds;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.front.interceptor.MyInterceptor;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = DataSourceConfig.PACKAGE, sqlSessionFactoryRef = CommonConstant.RULE_SQLSESSION_FACTORY)
public class DataSourceConfig
{

	// 精确到 master 目录，以便跟其他数据源隔离
	static final String PACKAGE = "com.foxconn.core.pro.server.rule.engine.front.mapper";
	static final String MAPPER_LOCATION = "classpath:mapper/rule/*.xml";

	@Value(CommonConstant.RULE_DATASOURCE_URL)
	private String url;

	@Value(CommonConstant.RULE_DATASOURCE_USERNAME)
	private String user;

	@Value(CommonConstant.RULE_DATASOURCE_PASSWORD)
	private String password;

	@Value(CommonConstant.RULE_DATASOURCE_DRIVERCLASSNAME)
	private String driverClass;

	@Bean(name = CommonConstant.RULE_DATA_SOURCE)
	@Primary
	public DataSource ruleDataSource()
	{
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean(name = CommonConstant.RULE_TRANSACTION_MANAGER)
	@Primary
	public DataSourceTransactionManager ruleTransactionManager()
	{
		return new DataSourceTransactionManager(ruleDataSource());
	}

	@Bean(name = CommonConstant.RULE_SQLSESSION_FACTORY)
	@Primary
	public SqlSessionFactory ruleSqlSessionFactory(@Qualifier(CommonConstant.RULE_DATA_SOURCE) DataSource ruleDataSource)
			throws Exception
	{
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(ruleDataSource);
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(DataSourceConfig.MAPPER_LOCATION));
		sessionFactory.setPlugins(new Interceptor[]{myInterceptor()});
		return sessionFactory.getObject();
	}

	@Bean
	public Interceptor myInterceptor()
	{
		return new MyInterceptor();
	}

}