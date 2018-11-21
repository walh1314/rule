/**
 * Project Name:rule-engine-core
 * File Name:CommonConstant.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.constant
 * Date:2018年8月28日下午5:31:23
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.constant;

/**
 * ClassName:CommonConstant <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月28日 下午5:31:23 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public interface CommonConstant
{
	String RABBITMQ_QUEUE = "rule.engine.queue";

	String QL_DEVICE_NAME = "__device_name__";
	String QL_PRODUCT_KEY = "__product_key__";
	String QL_TOPIC = "__topic__";
	
	String CONDITION_PARSER = "conditionParser";
	
	String FIELD_PARSER = "fieldParser";
	String UNDER_LINE = "_";
	
	String SERIAL_VERSION_UID = "serialVersionUID";
	String UNCHECKED = "unchecked";
	String STRING_STR= "对象中包含没有继承序列化的对象";
	
	String ERROR_CODE = "ErrorCode: ";
	String ERROR_MSG = ",ErrorMsg:\r\n";
	String ERRORMSG = "ErrorMsg:\r\n";
	
	String LEFT = "左边第";
	String PARENTHESIS_AND = "位括号和左边第";
	String PARENTHESIS = "位括号对应";
	String PARENTHESIS_LEFT_NO = "位括号没对应左括号";
	String PARENTHESIS_RIGHT_NO = "位括号没对应右括号";
	String AB_ONE = "(a+b))(";
	String AB_TWO = "(a+(b))))(";
	String AB_THREE = "(a+b))())(";
	
	String HTTPS = "https";
	String HTTP = "http";
	String SSLV3 = "SSLv3";
	String SESSION_ID = "sessionID";
	String FIN_ALLY ="finally";
	String WELL_NUMBER_SIX = "######";
	String WELL_NUMBER_FIVE = "#####";
	String DOUBLE_SLASH = "/";
	String QUESTION_MARK ="?";
	String ADD_OPERATOR ="&";
	String UTF_8 = "UTF-8";
	
	String DOUBLE_SLASH_SPOT ="\\.";
	String PARSE_OBJECT = "{\"name\":\"123\",\"data\":{\"test02\":\"aaa\"}}";
	String DATA = "data";
	String TEST_02 = "test02";
	String NAME = "name";
	
	String COLON = ":";
	String RULE_ENGINE = "rule_engine";
	String ID = "id";
	String RULE_ENGINE_ACTION = "rule_engine_action";
	String RULE_ID = "rule_id" ;
	String RULE_ENGINE_ACTION_CONDITION = "rule_engine_action_condition";
	String RULE_ENGINE_ACTION_FIELDS = "rule_engine_action_fields";
	
	String RULE_TEMPLATE = "rule.template";
	
	String DATAID = "dataid";
	String ACCESSMODEL = "accessModel";
	
	String PRODUCTKEY = "productKey";
	String DEVICENAME = "devicename";
	
	String MSG_ONE = "加密函数第二个必须为字符串,当前参数为:";
	String MSG_TWO = "函数参数个数不匹配,当前数据是:";
	String MSG_THREE = "函数定义的返回类型错误：";
	String MSG_FORE = "对象类型不匹配，只有数字才能执行 abs 操作,当前数据类型是:";
	String MSG_FIVE = " 只支持单数据参数，当前参数为";
	String MSG_SIX = "对象类型不匹配，只有数字才能执行 acos 操作,当前数据类型是:";
	String MSG_SEVEN = "对象类型不匹配，只有数字才能执行  cos 操作,当前数据类型是:";
	String MSG_EIGHT = "函数参数个数不匹配，只支持两个参数,当前数据是:";
	String MSG_NIGHT = "对象类型不匹配，只有数字才能执行 Asin 操作,当前数据类型是:";
	String MSG_TEN = "函数第1个参数为空,或者不是数字";
	String MSG_ELE = "函数第2个参数为空,或者不是数字";
	String MSG_THR = "对象类型不匹配，只有数字才能执行  ";
	String MSG_TWE = " 操作,当前数据类型是:";
	String MSG_FORETEEN = "该函数不支持参数,当前参数有:";
	String MSG_FIFTTEEN = "函数第1个参数为空,或者不是整数";
	String MSG_SIXTEEN = "函数第2个参数为空,或者不是整数";
	String MSG_SEVENTEEN = "函数第2个参数不能小于等于第1个参数";
	String MSG_EIGHTTEEN = "函数参数个数不匹配，只支持1个或者2个参数,当前数据是:";
	String MSG_EXP = "对象类型不匹配，只有数字才能执行 Exp 操作,当前数据类型是:";
	String MSG_COSH = "对象类型不匹配，只有数字才能执行 cosh 操作,当前数据类型是:";
	String MSG_NIGHTTEEN = "函数第二个必须为字符串,当前参数为:";
	String MSG_UPPER = "对象类型不匹配，只有数字、支付传才能执行 upper操作,当前数据类型是:";
	String MSG_THREESTR = "函数参数个数不匹配，只支持3个参数,当前数据是:";
	String MSG_ERROR = "函数参数类型错误，当前数据为:";
	String TOPIC = "__topic__";
	String MSG_ERR = " 函数参数错误";
	String LINE = "-";
	String MSG_INFO = "函数参数个数不匹配，只支持2个参数,当前数据是:";
	
	String EXP = "表达式容器获取对象失败";
	
	String ABS = "abs";
	String ASIN = "asin";
	String CONCAT_WS = "concat_ws";
	String CONCAT = "concat";
	String COS = "cos";
	String COSH = "cosh";
	String CRYPTO = "crypto";
	String ENDSWITH = "endswith";
	String DEVICE_NAME = "deviceName";
	String EX_P = "exp";
	String FLOOR = "floor";
	String LOG = "log";
	String LOWER = "lower";
	String MODULO = "modulo";
	String NANVL = "nanvl";
	String NEWUUID = "newuuid";
	String POWER = "power";
	String PRODUCTID = "productId";
	String RANDOM = "random";
	String RANDINT = "randint";
	String REPLACE = "replace";
	String SIN = "sin";
	String SINH = "sinh";
	String TAN = "tan";
	String TANH = "tanh";
	String TIMESTAMP = "timestamp";
	String TOP_IC = "topic";
	String UPPER = "upper";
	
	String DATA_ID = "dataId";
	
	String TENANTID = "tenantId";
	String USERID = "userId";
	String DB = "db";
	String PROJECTID = "projectId";
	String DEVICEOWNER = "deviceOwner";
	
	String COM_SERVER_FRONT_PARAMS = "com.server.front-params";
	String COM_SERVER = "com.server";
	String THIRDPARTY_CORE_PRO_COMMON = "thirdparty.core.pro.common";
	
	String COM_SERVER_FRONT = "${com.server.front}";
	String COM_SERVER_FRONT_URL = "${com.server.front-url}";
	String RULEENGINE_NOTICE_REDIS = "/ruleEngine//notice/redis";
	
	String CONTENT_TYPE = "Content-Type";
	String APPLICATION_JSON = "application/json";
	String ACCEPT = "Accept";
	String PRODUCT_KEY = "productKey";
	
	String CONDITIONPARSER = "conditionParser";
	String SELECT_FROM =  "select * from table where ";
	String R_N = "\r\n";
	String RETU_RN = " return ";
	String COMMA = " ;";
	String Nu_LL = " ";
	String REG_EX = "(<>)|(!=)|(<=)|(>=)|(<)|(>)|(=)|(\\()|(\\))";
	String NULL_NO = "";
	String STRING_EXP = "[\\t\\s]{1,}";
	String TEMP = " __temp_";
	String UNDERLINE = "__";
	String TEMP_KEYS = " __temp_keys_";
	String RIGHT_PARENTHESIS = " ) ";
	String LEFT_PARENTHESIS = " ( ";
	String QUOTATION_MARK = "`";
	String EQUA_NULL = " = null;\r\n";
	String EQUANULL = "= null;\r\n";
	String EQUAl = "=\"";
	String JSON_Object_Util =  " = JSONObjectUtil.getList(root,";
	String N_STR = ");\r\n";
	String R_STR = "\";\r\n";
	String EQUAL_NO = " != ";
	String EQ = "=";
	String RIGHT_LINE = "\"";
	String LEFTPARENTHESIS = "(";
	String RIGHTPARENTHESIS = ")";
	String DOUHAO = ", ";
	String PROINT_TWO = "`.`";
	String IN =" in (";
	String RIGHT = ") ";
	
	
			
	
}
