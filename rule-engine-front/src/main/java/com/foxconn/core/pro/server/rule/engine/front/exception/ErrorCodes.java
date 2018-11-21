package com.foxconn.core.pro.server.rule.engine.front.exception;

public enum ErrorCodes
{
	SCUUESS("1", "Successful",0), 
	FAILED("-1", "Failed",101),
	
	/**success 返回 0   參數錯誤error301  null empty302   非法请求101   系统异常  401*/
	RULE_TYPE_NOT_EXIST("ruleEngine-1000-01", "rule.type.not.exist",301),
	/**增加规则引擎*/
	RULE_ENGINE_NAME_EMPTY("ruleEngine-1000-02", "ruleEngine.name.empty",302),
	RULE_ENGINE_ADD_FAIL("ruleEngine-1000-03", "add.ruleEngine.fail",101),
	
	RULE_ENGINE_SQL_EMPTY("ruleEngine-1000-04", "ruleEngine.sql.empty",302),
	RULE_ENGINE_FIELDS_EMPTY("ruleEngine-1000-05", "ruleEngine.fields.empty",302),
	RULE_ENGINE_TOPIC_EMPTY("ruleEngine-1000-06", "ruleEngine.topic.empty",302),
	RULE_ENGINE_ACTIONS_EMPTY("ruleEngine-1000-07", "ruleEngine.actions.empty",302),
	RULE_ENGINE_ACTIONTYPEID_EMPTY("ruleEngine-1000-08", "ruleEngine.actionTypeId.empty",302),
	RULE_ENGINE_ACTIONPARAM_EMPTY("ruleEngine-1000-09", "ruleEngine.actionParam.empty",302),
	RULE_ENGINE_DATATYPE_EMPTY("ruleEngine-1000-10", "ruleEngine.datatype.empty",302),
	RULE_ENGINE_VERSION_EMPTY("ruleEngine-1000-11", "ruleEngine.version.empty",302),
	RULE_ENGINE_ACCESSMODE_EMPTY("ruleEngine-1000-12", "ruleEngine.accessMode.empty",302),
	
	/**分页查询*/
	RULE_ENGINE_PAGESIZE_EMPTY("ruleEngine-1000-13", "ruleEngine.pageSize.empty",302),
	RULE_ENGINE_CURRENTPAGE_EMPTY("ruleEngine-1000-14", "ruleEngine.currentPage.empty",302),
	RULE_ENGINE_STATUS_EMPTY("ruleEngine-1000-15", "ruleEngine.status.empty",302),
	
	/**删除规则引擎*/
	RULE_ENGINE_ID_EMPTY("ruleEngine-1000-16", "ruleEngine.id.empty",302),
	RULE_ENGINE_ID_ERROR("ruleEngine-1000-17", "ruleEngine.id.error",301),
	RULE_ENGINE_DELETE_FAIL("ruleEngine-1000-18", "delete.ruleEngine.fail",101),
	

	
	/**更新规则引擎*/
	RULE_ENGINE_UPDATE_FAIL("ruleEngine-1000-19", "update.ruleEngine.fail",101),
	
	/**规则名字已经存在*/
	RULE_ENGINE_RULE_NAME_EXISTS("ruleEngine-1000-20", "ruleEngine.name.exists",301),
	
	/**规则引擎测试*/
	RULE_ENGINE_DEBUG_FIELD_EMPTY("ruleEngine-1001-01", "ruleEngine.field.empty",302),
	RULE_ENGINE_DEBUG_TOPIC_EMPTY("ruleEngine-1001-02", "ruleEngine.topic.empty",302),
	RULE_ENGINE_DEBUG_DATA_EMPTY("ruleEngine-1001-03", "ruleEngine.data.empty",302),
	RULE_ENGINE_DEBUG_DATA_PARAM_EMPTY("ruleEngine-1001-04", "ruleEngine.data.param.empty",302),
	
	/**动作删除**/
	RULE_ACTION_DELETE_FAIL("ruleEngine-1002-01", "ruleEngine.action.delete.fail",101),
	RULE_ACTION_ID_ERROR("ruleEngine-1002-02", "ruleEngine.action.id.error",301),
	
	/**更新规则引擎*/
	RULE_ACTION_UPDATE_FAIL("ruleEngine-1002-03", "update.ruleEngine.action.fail",101),
	
	COREPRO_COMMON_USERID_EMPTY("corepro-common-1000-01", "corepro.common.userid.empty",302),
	COREPRO_COMMON_TOPIC_EMPTY("corepro-common-1000-02", "corepro.common.topic.empty",302),
	
	/**获取db异常**/
	COREPRO_GET_TENANT_DB_FAIL("corepro-common-1001-01", "corepro.get.Tenant.db.fail",301),
	
	SYSTEM_EXCEPTION("sys-1000-01", "system.exception",401),
	
	/**验证字段取值范围*/
	RULE_ENGINE_ACCESSMODE_RANGE_OF_VALUE("ruleEngine-1000-21", "ruleEngine.accessMode.beyond.the.range.of.values",301),
	RULE_ENGINE_VERSION_RANGE_OF_VALUE("ruleEngine-1000-22", "ruleEngine.version.beyond.the.range.of.values",301),
	RULE_ENGINE_STATUS_RANGE_OF_VALUE("ruleEngine-1000-23", "ruleEngine.status.beyond.the.range.of.values",301),
	RULE_ENGINE_DATATYPE_RANGE_OF_VALUE("ruleEngine-1000-24", "ruleEngine.dataType.beyond.the.range.of.values",301),
	
	/**不符合命名规则*/
	ERROR_NAME_RULE("sql-error-name-01","ruleEngine.name.error",301),
	ERROR_GRAMMAR_RULE("sql-error-grammar-01","ruleEngine.grammar.error",301),
	ERROR_GRAMMAR_FUNCTION_NAME_RULE("sql-error-grammar-01","ruleEngine.grammar.function.not.exist",301),
	ERROR_GRAMMAR_FUNCTION_PARMETER_RULE("sql-error-grammar-01","ruleEngine.grammar.function.parameter.error",301),
	ERROR_GRAMMAR_CASE_WHEN_ELSE_RULE("sql-error-grammar-01","ruleEngine.grammar.case.when.else.empty",302)
	;
	private String code;
	private String desc;
	private Integer status;

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	private ErrorCodes(String code, String desc,Integer status)
	{
		this.code = code;
		this.desc = desc;
		this.status = status;
	}
}
