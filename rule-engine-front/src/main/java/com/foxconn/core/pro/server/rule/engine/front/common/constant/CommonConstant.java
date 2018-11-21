package com.foxconn.core.pro.server.rule.engine.front.common.constant;

public interface CommonConstant
{

	static final String SYSTEM_ACCOUNT = "system";
	
	static final String SERVICE_SUCCESS = "1";
	static final String SERVICE_FAIL = "-1";
	
	static final String VALIDATE_CONDITION_TYPE="1";
	static final String VALIDATE_FIELD_TYPE="0";
	
	String DOUBLE_SLASH_ADD = "\\+";
	
	String WELL_NUMBER_SIX = "######";
	String WELL_NUMBER_FIVE = "#####";
	String DOUBLE_SLASH = "/";
	String QUESTION_MARK ="?";
	String ADD_OPERATOR ="&";
	String DOLLAR = "$";
	
	String OPERATOR_NO="^";
	
	String UNCHECKED = "unchecked";
	
	String ASTERISK = "*";
	String DOUBLE_SLASH_ASTERISK_TWO ="/**";
	String WELL_NUMBER_ONE = "#";
	String REGULAR_EXPRESSION_THREE = "[\\S]{0,}";
	
	String REGULAR_EXPRESSION_TWO = "[^\\s\\/]+" ;
	
	String REGULAR_EXPRESSION = "([,;]{1})|(q=[0-9][.]?[0-9]?)";
	String LINE = "-";
	String DOUBLE_SLASH_SPOT ="\\.";
	String SLASH = "\""; 
	String RIGHT_LINE = "\'";
	
	String COMMA = ", ";
	String RIGHT_PARENTHESIS = ")";
	String SPOT = ".";
	String LEFT_PARENTHESIS = "(";
	String PERCENTAGE_NUMBER = " % ";
	
	String HTTPS = "https";
	String HTTP = "http";
	String SSLV3 = "SSLv3";
	String SESSION_ID = "sessionID";
	String FIN_ALLY ="finally";
	
	String CURRENT_USER = "currentUser";
	String TENANT_DB = "tenantDb";
	String SET_MODIFIER = "setModifier";
	String SET_CREATOR ="setCreator";
	String SET_CREATETIME ="setCreateTime";
	String SET_MODIFYTIME ="setModifyTime";
	
	String YYYY_MM_DD = "yyyyMMddHHmmssSSS";
	
	String SINGLE_TON ="singleton";
	String STATUS = "status";
	String ID = "id";
	
	String RULE_SQLSESSION_FACTORY = "ruleSqlSessionFactory";
	String RULE_DATA_SOURCE = "ruleDataSource";
	String RULE_TRANSACTION_MANAGER  = "ruleTransactionManager";
	
	String FUNCTION_CONFIG = "functionConfig";
	String COM_SQL_FUNCTION = "com.sql.function";
	
	String ACCEPT_LANGUAGE = "Accept-Language";
	
	String DELEGATE_MAPPEDSTATEMENT = "delegate.mappedStatement";
	String DELEGATE_BOUNDSQL_SQL = "delegate.boundSql.sql";
	String PREPARE  = "prepare";
	
	String TENANT_ID = "tenantId";
	String DATABASE_TYPE = "databaseType";
	String DB_NAME = "dbName";
	String DEVICE_OWNER = "deviceOwner";
	
	String VALUES  = " VALUES (";
	String QUOTATION_MARK = "`";
	
	String USER_ID  = "userId";
	String DB ="db";
	String PRODUCT_ID ="productId";
	
	String NAME = "name";
	String DATA_TYPE = "dataType";
	String CREATOR = "creator";
	String RULE_ID = "ruleId";
	String PARAMS = "params";
	String TOPIC = "topic";
	String ROOT = "root";
	String EXTANDIDS ="extandIds";
	
	String RULE_DATASOURCE_URL = "${rule.datasource.url}";
	String RULE_DATASOURCE_USERNAME = "${rule.datasource.username}";
	String RULE_DATASOURCE_PASSWORD = "${rule.datasource.password}";
	String RULE_DATASOURCE_DRIVERCLASSNAME = "${rule.datasource.driverClassName}";
	
	String SPRING_MESSAGES_BASENAME = "${spring.messages.basename}";
	String SPRING_MESSAGES_CACHE_DURATION = "${spring.messages.cache-duration}";
	String SPRING_MESSAGES_ENCODING = "${spring.messages.encoding}";
	
	String ENCOUNTERED_UNEXPECTED_TOKEN = "Encountered unexpected token: ";
	String AT ="at";
	String COLUMN = "column";
	String AND = " and ";
	String OR = " or ";
	String IN =" IN (" ;
	String JAVA_LANG_NUMBER = "java.lang.Number";
	String JAVA_LANG_OBJECT = "java.lang.Object";
	String FUNCTION_VALIDATE = "com.foxconn.core.pro.server.rule.engine.front.validate.FunctionValidate";
	String RULE_ENGINE_FRONT = "com.foxconn.core.pro.server.rule.engine.front";
	String RULE_ENGINE_CORE = "com.foxconn.core.pro.server.rule.engine.core";
	String COLON = "..";
	
}
