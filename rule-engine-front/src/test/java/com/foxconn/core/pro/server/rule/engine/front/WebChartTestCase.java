package com.foxconn.core.pro.server.rule.engine.front;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.foxconn.core.pro.server.rule.engine.front.dto.ActionTypeDto;
import com.foxconn.core.pro.server.rule.engine.front.dto.ActionsDto;
import com.foxconn.core.pro.server.rule.engine.front.dto.InputMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.RuleCheckDto;
import com.foxconn.core.pro.server.rule.engine.front.dto.RuleDebugDto;
import com.foxconn.core.pro.server.rule.engine.front.dto.RuleEngineDto;
import com.foxconn.core.pro.server.rule.engine.front.entity.Sql;
import com.foxconn.core.pro.server.rule.engine.front.entity.UserInfo;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 
 * ClassName: WebChartTestCase <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年10月22日 下午2:27:51 <br/>
 * 
 * @author hewanwan
 * @version
 * @since JDK 1.8
 */

public class WebChartTestCase extends BaseRestfulTest
{

	@Test
	public void checkConditionTest() throws Exception
	{
		InputMap<RuleCheckDto> bean =new InputMap<RuleCheckDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		RuleCheckDto ruleCheckDto =new RuleCheckDto();
		ruleCheckDto.setCondition("test>0");
		ruleCheckDto.setFields("test,person.`name`");
		bean.setData(ruleCheckDto);

		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/check/condition").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.01 Condition校验",
						requestFields(
								//attributes(key("constraints").value("Fields for user creation")), 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id")
								.attributes(key("constraints").value("必填字段")),//Must not be null. Must not be empty
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名 ")
								.attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.condition").type(JsonFieldType.STRING).description("验证条件 ")
								.attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.fields").type(JsonFieldType.STRING).description("验证字段")
								.attributes(key("constraints").value("非必填字段"))),
						responseFields(
							//	attributes(key("title").value("Fields for user creation")), 
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void pageListTest() throws Exception
	{
		InputMap<RuleEngineDto> bean =new InputMap<RuleEngineDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		RuleEngineDto ruleEngineDto =new RuleEngineDto();
		ruleEngineDto.setName("rule_test");
		ruleEngineDto.setDataType(1L);
		ruleEngineDto.setPageSize(10);
		ruleEngineDto.setCurrentPage(1);
		ruleEngineDto.setStatus(1);
		bean.setData(ruleEngineDto);

		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/pageList").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document(new String("1.02 分页查询".getBytes(),"UTF-8"),
						requestFields( 
								attributes(key("constraints").value("Fields for user creation")), 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.STRING).description("规则名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.dataType").type(JsonFieldType.NUMBER).description("数据类型").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.pageSize").type(JsonFieldType.NUMBER).description("分页大小").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("当前页").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.status").type(JsonFieldType.NUMBER).description("状态 ").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NULL).description("id").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.NULL).description("备注").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.sql").type(JsonFieldType.NULL).description("sql数据").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.version").type(JsonFieldType.NULL).description("版本").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.actions").type(JsonFieldType.NULL).description("规则行为").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.accessMode").type(JsonFieldType.NULL).description("接入方式").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.code").type(JsonFieldType.NULL).description("代码").attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息"),
								fieldWithPath("data.curretPage").type(JsonFieldType.NUMBER).description("当前页"),
								fieldWithPath("data.pageSize").type(JsonFieldType.NUMBER).description("分页大小"),
								fieldWithPath("data.pages").type(JsonFieldType.NUMBER).description("总页数"),
								fieldWithPath("data.rows").type(JsonFieldType.ARRAY).description("接入方式"),
							/*	fieldWithPath("data.rows[].accessMode").type(JsonFieldType.NUMBER).description("接入方式"),
								fieldWithPath("data.rows[].actions").type(JsonFieldType.ARRAY).description("行为规则"),
								fieldWithPath("data.rows[].code").type(JsonFieldType.STRING).description("代码"),
								fieldWithPath("data.rows[].createTime").type(JsonFieldType.NUMBER).description("创建时间"),
								fieldWithPath("data.rows[].dataType").type(JsonFieldType.NUMBER).description("数据类型"),
								fieldWithPath("data.rows[].desc").type(JsonFieldType.STRING).description("描述"),
								fieldWithPath("data.rows[].id").type(JsonFieldType.NUMBER).description("id"),
								fieldWithPath("data.rows[].modifier").type(JsonFieldType.STRING).description("修改者"),
								fieldWithPath("data.rows[].modifyTime").type(JsonFieldType.NUMBER).description("修改时间"),
								fieldWithPath("data.rows[].name").type(JsonFieldType.STRING).description("名称"),
								fieldWithPath("data.rows[].sql").type(JsonFieldType.OBJECT).description("sql数据"),
								fieldWithPath("data.rows[].status").type(JsonFieldType.NUMBER).description("状态"),
								fieldWithPath("data.rows[].creator").type(JsonFieldType.STRING).description("创建者"),
								fieldWithPath("data.rows[].version").type(JsonFieldType.NUMBER).description("版本"),*/
								fieldWithPath("data.total").type(JsonFieldType.NUMBER).description("总条数")
								))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void actionTypeListTest() throws Exception
	{
		InputMap<ActionTypeDto> bean =new InputMap<ActionTypeDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		ActionTypeDto actionTypeDto =new ActionTypeDto();
		actionTypeDto.setId(1);
		bean.setData(actionTypeDto);

		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/actionType/list").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.03 查询Action",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("行为类型id").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.NULL).description("行为类型名称").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.NULL).description("描述").attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息"),
								fieldWithPath("data[].createTime").type(JsonFieldType.NUMBER).description("创建时间"),
								fieldWithPath("data[].creator").type(JsonFieldType.STRING).description("创建者"),
								fieldWithPath("data[].desc").type(JsonFieldType.STRING).description("备注"),
								fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("id"),
								fieldWithPath("data[].name").type(JsonFieldType.STRING).description("名称")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void addTest() throws Exception
	{
		InputMap<RuleEngineDto> bean =new InputMap<RuleEngineDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		RuleEngineDto ruleEngineDto =new RuleEngineDto();
		ruleEngineDto.setId(6);
		ruleEngineDto.setName("ru_123");
		ruleEngineDto.setDesc("test_01");
		ruleEngineDto.setDataType(1L);
		ruleEngineDto.setVersion(2);
		ruleEngineDto.setAccessMode(2);
		ruleEngineDto.setStatus(1);
		Sql sql =new Sql();
		sql.setCondition("`count` <= 600");
		sql.setFields("test");
		sql.setTopic("/test/+");
		ruleEngineDto.setSql(sql);
		List<ActionsDto> actionsDtolist= new ArrayList<>();
		ActionsDto actionsDto =new ActionsDto();
		actionsDto.setId(6);
		actionsDto.setRuleId(6);
		actionsDto.setActionTypeId(2);
		JSONObject obj = new JSONObject();
		obj.put("url", "192.168.1.23:188311111111");
		actionsDto.setActionParam(obj);
		actionsDtolist.add(actionsDto);
		ruleEngineDto.setActions(actionsDtolist);
		bean.setData(ruleEngineDto);
		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/add").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.04 增加规则引擎",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("规则id").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.STRING).description("规则名").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.actions").type(JsonFieldType.ARRAY).description("规则行为").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.STRING).description("描述").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.dataType").type(JsonFieldType.NUMBER).description("规则类型").attributes(key("constraints").value("必填字段)")),
								fieldWithPath("data.status").type(JsonFieldType.NUMBER).description("状态").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.STRING).description("描述").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.version").type(JsonFieldType.NUMBER).description("版本").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.accessMode").type(JsonFieldType.NUMBER).description("接入方式").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.sql.fields").type(JsonFieldType.STRING).description("验证字段").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.sql.topic").type(JsonFieldType.STRING).description("话题").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.sql.condition").type(JsonFieldType.STRING).description("条件").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.currentPage").type(JsonFieldType.NULL).description("当前页").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.pageSize").type(JsonFieldType.NULL).description("分页大小").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.code").type(JsonFieldType.NULL).description("代码" ).attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void delRuleEngineTest() throws Exception
	{
		InputMap<RuleEngineDto> bean =new InputMap<RuleEngineDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		bean.setConfig(userInfo);
		RuleEngineDto ruleEngineDto =new RuleEngineDto();
		ruleEngineDto.setId(6);
		bean.setData(ruleEngineDto);

		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/del").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.05 删除规则引擎",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("规则Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.NULL).description("用户名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.NULL).description("规则名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.actions").type(JsonFieldType.NULL).description("规则行为").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.NULL).description("描述").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.dataType").type(JsonFieldType.NULL).description("规则类型").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.status").type(JsonFieldType.NULL).description("状态").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.currentPage").type(JsonFieldType.NULL).description("当前页").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.NULL).description("描述").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.pageSize").type(JsonFieldType.NULL).description("分页大小").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.code").type(JsonFieldType.NULL).description("代码").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.version").type(JsonFieldType.NULL).description("版本").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.accessMode").type(JsonFieldType.NULL).description("接入方式").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.sql").type(JsonFieldType.NULL).description("sql数据").attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void debugRuleEngineTest() throws Exception
	{
		InputMap<RuleDebugDto> bean =new InputMap<RuleDebugDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		RuleDebugDto ruleDebugDto =new RuleDebugDto();
		ruleDebugDto.setCondition("test>0");
		JSONObject obj = new JSONObject();
	    obj.put("url", "url");
		ruleDebugDto.setFields("testa");
		ruleDebugDto.setTopic("1");
		ruleDebugDto.setData(obj);
		bean.setData(ruleDebugDto);
		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/debug").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.06 规则引擎调试",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名 ").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.data").type(JsonFieldType.OBJECT).description("data数据").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.data.url").type(JsonFieldType.STRING).description("路径").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.topic").type(JsonFieldType.STRING).description("topic").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.fields").type(JsonFieldType.STRING).description("验证字段").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.condition").type(JsonFieldType.STRING).description("验证条件").attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息"))
				)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void detailTest() throws Exception
	{
		InputMap<RuleEngineDto> bean =new InputMap<RuleEngineDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		bean.setConfig(userInfo);
		RuleEngineDto ruleEngineDto =new RuleEngineDto();
		ruleEngineDto.setId(6);
		bean.setData(ruleEngineDto);

		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/detail").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.07 查询规则引擎",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.NULL).description("用户名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("规则Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.NULL).description("规则名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.NULL).description("描述").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.dataType").type(JsonFieldType.NULL).description("规则类型").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.sql").type(JsonFieldType.NULL).description("sql数据").attributes(key("constraints").value("非必填字段")),
								/*	fieldWithPath("data.sql.fields").type(JsonFieldType.STRING).description("验证字段(必填字段)"),
								fieldWithPath("data.sql.topic").type(JsonFieldType.STRING).description("话题(必填字段)"),
								fieldWithPath("data.sql.condition").type(JsonFieldType.STRING).description("验证条件(必填字段)"),*/
								fieldWithPath("data.actions").type(JsonFieldType.NULL).description("规则行为").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.version").type(JsonFieldType.NULL).description("版本").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.accessMode").type(JsonFieldType.NULL).description("接入方式").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.status").type(JsonFieldType.NULL).description("状态").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.code").type(JsonFieldType.NULL).description("代码").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.currentPage").type(JsonFieldType.NULL).description("当前页").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.pageSize").type(JsonFieldType.NULL).description("分页大小").attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401")
								/*,fieldWithPath("data.code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("id"),
								fieldWithPath("data.name").type(JsonFieldType.STRING).description("名称"),
								fieldWithPath("data.sql").type(JsonFieldType.OBJECT).description("sql数据"),
								fieldWithPath("data.status").type(JsonFieldType.NUMBER).description("状态"),
								fieldWithPath("data.version").type(JsonFieldType.NUMBER).description("版本"),
								fieldWithPath("data.accessMode").type(JsonFieldType.NUMBER).description("接入方式"),
								fieldWithPath("data.desc").type(JsonFieldType.STRING).description("规则行为"),
								fieldWithPath("data.actions").type(JsonFieldType.ARRAY).description("规则行为"),
								fieldWithPath("data.dataType").type(JsonFieldType.NUMBER).description("规则类型")*/
								))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void updateStatusTest() throws Exception
	{
		InputMap<RuleEngineDto> bean =new InputMap<RuleEngineDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		RuleEngineDto ruleEngineDto =new RuleEngineDto();
		ruleEngineDto.setId(3);
		ruleEngineDto.setStatus(2);
		bean.setData(ruleEngineDto);

		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/updateStatus").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.08 更新状态",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名 ").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("规则Id ").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.status").type(JsonFieldType.NUMBER).description("状态").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.NULL).description("规则名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.actions").type(JsonFieldType.NULL).description("规则行为").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.NULL).description("描述").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.dataType").type(JsonFieldType.NULL).description("规则类型").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.currentPage").type(JsonFieldType.NULL).description("当前页").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.NULL).description("描述").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.pageSize").type(JsonFieldType.NULL).description("分页大小").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.code").type(JsonFieldType.NULL).description("代码").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.version").type(JsonFieldType.NULL).description("版本").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.accessMode").type(JsonFieldType.NULL).description("接入方式").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.sql").type(JsonFieldType.NULL).description("sql数据").attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void checkFieldsTest() throws Exception
	{
		InputMap<RuleCheckDto> bean =new InputMap<RuleCheckDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		RuleCheckDto ruleCheckDto =new RuleCheckDto();
		ruleCheckDto.setCondition("test>0");
		ruleCheckDto.setFields("test,person.`name`");
		bean.setData(ruleCheckDto);

		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/check/fields").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.09 Fields验证",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名 ").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.condition").type(JsonFieldType.STRING).description("验证条件").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.fields").type(JsonFieldType.STRING).description("验证字段").attributes(key("constraints").value("必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void actionAddTest() throws Exception
	{
		InputMap<ActionsDto> bean =new InputMap<ActionsDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		ActionsDto actionsDto =new ActionsDto();
		actionsDto.setId(88);
		actionsDto.setRuleId(3);
		actionsDto.setActionTypeId(2);
		JSONObject obj = new JSONObject();
		obj.put("url", "192.168.1.23:222222222");
		actionsDto.setActionParam(obj);
		bean.setData(actionsDto);
		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/action/add").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.10 添加Action",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("规则Id").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.ruleId").type(JsonFieldType.NUMBER).description("规则Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.actionTypeId").type(JsonFieldType.NUMBER).description("行为类型Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.actionParam.url").type(JsonFieldType.STRING).description("路径").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.NULL).description("行为名称").attributes(key("constraints").value("非必填字段")))
						,
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
		
	@Test
	public void actionDetailTest() throws Exception
	{
		InputMap<ActionsDto> bean =new InputMap<ActionsDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		ActionsDto actionsDto =new ActionsDto();
		actionsDto.setId(82);
		bean.setData(actionsDto);
		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/action/detail").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.11 查询Action",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名 ").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("规则Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.NULL).description("行为名称").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.ruleId").type(JsonFieldType.NULL).description("规则Id").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.actionTypeId").type(JsonFieldType.NULL).description("行为类型Id").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.actionParam").type(JsonFieldType.NULL).description("行为参数").attributes(key("constraints").value("非必填字段")))
						,
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void updateRuleEngineTest() throws Exception
	{
		InputMap<RuleEngineDto> bean =new InputMap<RuleEngineDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		RuleEngineDto ruleEngineDto =new RuleEngineDto();
		ruleEngineDto.setId(3);
		ruleEngineDto.setName("ru_123");
		ruleEngineDto.setDesc("test_01");
		ruleEngineDto.setDataType(1L);
		ruleEngineDto.setVersion(2);
		ruleEngineDto.setAccessMode(2);
		ruleEngineDto.setStatus(1);
		Sql sql =new Sql();
		sql.setCondition("`count` <= 2222");
		sql.setFields("test");
		sql.setTopic("/test/+");
		ruleEngineDto.setSql(sql);
		List<ActionsDto> actionsDtolist= new ArrayList<>();
		ActionsDto actionsDto =new ActionsDto();
		actionsDto.setId(80);
		actionsDto.setRuleId(3);
		actionsDto.setActionTypeId(2);
		JSONObject obj = new JSONObject();
		obj.put("url", "192.168.1.23:188311111111");
		actionsDto.setActionParam(obj);
		actionsDtolist.add(actionsDto);
		ruleEngineDto.setActions(actionsDtolist);
		bean.setData(ruleEngineDto);

		ObjectMapper mapper = new ObjectMapper();
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/update").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.12 更新规则引擎",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名 ").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("规则id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.STRING).description("规则名").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.actions").type(JsonFieldType.ARRAY).description("规则行为").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.desc").type(JsonFieldType.STRING).description("描述").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.dataType").type(JsonFieldType.NUMBER).description("规则类型").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.status").type(JsonFieldType.NUMBER).description("状态").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.version").type(JsonFieldType.NUMBER).description("版本").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.accessMode").type(JsonFieldType.NUMBER).description("接入方式").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.sql.fields").type(JsonFieldType.STRING).description("验证字段").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.sql.topic").type(JsonFieldType.STRING).description("话题").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.sql.condition").type(JsonFieldType.STRING).description("验证条件").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.currentPage").type(JsonFieldType.NULL).description("当前页").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.pageSize").type(JsonFieldType.NULL).description("分页大小").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.code").type(JsonFieldType.NULL).description("代码").attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息"))
				)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void actionUpdateTest() throws Exception
	{
		InputMap<ActionsDto> bean =new InputMap<ActionsDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		ActionsDto actionsDto =new ActionsDto();
		actionsDto.setId(82);
		actionsDto.setRuleId(4);
		actionsDto.setActionTypeId(2);
		JSONObject obj = new JSONObject();
		obj.put("url", "192.168.1.23:1111");
		actionsDto.setActionParam(obj);
		bean.setData(actionsDto);
		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/action/update").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.13 更新Action",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名 ").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("规则Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.ruleId").type(JsonFieldType.NUMBER).description("规则Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.actionTypeId").type(JsonFieldType.NUMBER).description("行为类型Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.actionParam.url").type(JsonFieldType.STRING).description("路径").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.NULL).description("行为名称").attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
		
	@Test
	public void actionDelTest() throws Exception
	{
		InputMap<ActionsDto> bean =new InputMap<ActionsDto>();
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId("test1");
		userInfo.setName("test1");
		bean.setConfig(userInfo);
		ActionsDto actionsDto =new ActionsDto();
		actionsDto.setId(82);
		bean.setData(actionsDto);
		ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bean);
		this.mockMvc
				.perform(post("/ruleEngine/action/del").contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andDo(document("1.14 删除Action",
						requestFields( 
								fieldWithPath("config.userId").type(JsonFieldType.STRING).description("用户Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("config.name").type(JsonFieldType.STRING).description("用户名 ").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("规则Id").attributes(key("constraints").value("必填字段")),
								fieldWithPath("data.name").type(JsonFieldType.NULL).description("行为名称").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.ruleId").type(JsonFieldType.NULL).description("规则Id").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.actionTypeId").type(JsonFieldType.NULL).description("行为类型Id").attributes(key("constraints").value("非必填字段")),
								fieldWithPath("data.actionParam").type(JsonFieldType.NULL).description("行为参数").attributes(key("constraints").value("非必填字段"))),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("-1.失败 1.成功"),
								fieldWithPath("status").type(JsonFieldType.NUMBER).description("success 返回 0   參數錯誤error301  null或者empty302   非法请求101   系统异常  401"),
								fieldWithPath("msg").type(JsonFieldType.STRING).description("返回信息")))
				).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}
}
