/**
 * Project Name:rule-engine-front
 * File Name:InsertRuleServiceImpl.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.service.impl
 * Date:2018年8月24日下午2:47:18
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.foxconn.core.pro.server.rule.engine.core.analysis.ConditionAnalysis;
import com.foxconn.core.pro.server.rule.engine.core.analysis.FieldAnalysis;
import com.foxconn.core.pro.server.rule.engine.core.analysis.TopicAnalysis;
import com.foxconn.core.pro.server.rule.engine.front.common.RuleRedisService;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.FrontPage;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.PageResult;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.common.util.CodeUtil;
import com.foxconn.core.pro.server.rule.engine.front.common.util.SystemUtil;
import com.foxconn.core.pro.server.rule.engine.front.dto.ActionsDto;
import com.foxconn.core.pro.server.rule.engine.front.dto.InputMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.RuleDebugDto;
import com.foxconn.core.pro.server.rule.engine.front.dto.RuleEngineDto;
import com.foxconn.core.pro.server.rule.engine.front.entity.Actions;
import com.foxconn.core.pro.server.rule.engine.front.entity.RuleEngine;
import com.foxconn.core.pro.server.rule.engine.front.entity.Sql;
import com.foxconn.core.pro.server.rule.engine.front.entity.UserInfo;
import com.foxconn.core.pro.server.rule.engine.front.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.front.exception.ErrorCodes;
import com.foxconn.core.pro.server.rule.engine.front.mapper.ActionsMapper;
import com.foxconn.core.pro.server.rule.engine.front.mapper.RuleEngineMapper;
import com.foxconn.core.pro.server.rule.engine.front.service.ActionTypeService;
import com.foxconn.core.pro.server.rule.engine.front.service.RuleEngineService;
import com.foxconn.core.pro.server.rule.engine.front.service.VerifySqlLegitimacyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:InsertRuleServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月24日 下午2:47:18 <br/>
 * 
 * @author hewanwan
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor =
{ Exception.class, BaseException.class, RuntimeException.class, Throwable.class })
@Slf4j
public class RuleEngineServiceImpl implements RuleEngineService
{
	@Autowired
	private SystemUtil systemUtil;

	@Autowired
	private RuleEngineMapper ruleEngineAddMapper;
	@Autowired
	private ActionsMapper actionsMapper;

	@Autowired
	private ActionTypeService actionTypeService;

	@Autowired
	private RuleRedisService ruleRedisService;
	
	@Resource
	private ConditionAnalysis conditionAnalysis;
	
	@Resource
	private FieldAnalysis fieldAnalysis;
	
	@Autowired
	private VerifySqlLegitimacyService verifySqlLegitimacyService;

	/** 分页查询 */
	@Override
	public ResultMap<PageResult<RuleEngine>> selectPage(FrontPage<RuleEngine> page, InputMap<RuleEngineDto> inputMap)
			throws Exception, JsonMappingException, IOException
	{
		ResultMap<PageResult<RuleEngine>> result = new ResultMap<>();
		// 判断是否合法 userId与创建人是否一致
		boolean flag = this.isExitsUser(inputMap);//用户是否存在，如果不存在，直接报错
		
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		Map<String, Object> map = new HashMap<>(3);
		RuleEngineDto bean = inputMap.getData();
		
		if (bean.getPageSize()==null)
		{
			result.setCode(ErrorCodes.RULE_ENGINE_PAGESIZE_EMPTY.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_PAGESIZE_EMPTY.getDesc());
			result.setStatus(ErrorCodes.RULE_ENGINE_PAGESIZE_EMPTY.getStatus());
			return result;
		}
		if (bean.getCurrentPage()==null)
		{
			result.setCode(ErrorCodes.RULE_ENGINE_CURRENTPAGE_EMPTY.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_CURRENTPAGE_EMPTY.getDesc());
			result.setStatus(ErrorCodes.RULE_ENGINE_CURRENTPAGE_EMPTY.getStatus());
			return result;
		}
		/*if (bean.getStatus()==null)
		{
			result.setCode(ErrorCodes.RULE_ENGINE_STATUS_EMPTY.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_STATUS_EMPTY.getDesc());
			return result;
		}*/
		
		if(bean != null){
			BeanUtils.copyProperties(bean, page);
			if (!StringUtils.isBlank(bean.getName()))
			{
				map.put(CommonConstant.NAME, bean.getName());
			}
			if (!StringUtils.isBlank(String.valueOf(bean.getDataType())))
			{
				map.put(CommonConstant.STATUS, bean.getStatus());
			}
			if (!StringUtils.isBlank(String.valueOf(bean.getDataType())))
			{
				map.put(CommonConstant.DATA_TYPE, bean.getDataType());
			}
		}
		//设置创建人
		map.put(CommonConstant.CREATOR, inputMap.getConfig().getUserId());
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<RuleEngine> pageList = ruleEngineAddMapper.selectByMap(map);
		PageInfo<RuleEngine> pageInfo = new PageInfo<>(pageList);
		PageResult<RuleEngine> pageResult = new PageResult<>(pageInfo);
		result.setData(pageResult);
		return result;
	}

	private boolean isExitsUser(InputMap<? extends Object> input){
		if(input == null ){
			return false;
		} 
		
		UserInfo currentUser = input.getConfig();
		if(currentUser == null ) {
			return false;
		}
		if(StringUtils.isBlank(currentUser.getUserId())){
			return false;
		}
		return true;
	}
	/** 判断用户和创建者是否一致 */
	public boolean validationUser(UserInfo currentUser,String user)
	{
		if(currentUser == null || StringUtils.isBlank(currentUser.getUserId()) ||  StringUtils.isBlank(user)){
			return false;
		} else if(!currentUser.getUserId().trim().equals(user.trim())){
			return false;
		}
		return true;
	}

	/** 增加功能引擎 */
	@Override
	public ResultMap<? extends Object> insert(InputMap<RuleEngineDto> inputMap)
			throws Exception, JsonMappingException, IOException
	{
		boolean flag = this.isExitsUser(inputMap);//用户是否存在，如果不存在，直接报错
		
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		ResultMap<? extends Object> result = new ResultMap<>();
		RuleEngineDto ruleEngineDto = inputMap.getData();
		// 设置错误编码和描述
		validationBooleanIsNull(ruleEngineDto);
		
		/**
		 * 检查规则名字是否存在
		 */
		checkRuleName(ruleEngineDto.getName(),null);
		//检查字段取值范围
		checkVersionAccessModeDataType(ruleEngineDto);

		RuleEngine insertRule = new RuleEngine();
		BeanUtils.copyProperties(ruleEngineDto, insertRule);
		Sql sql = ruleEngineDto.getSql();
		insertRule.setSqlString(JSON.toJSONString(sql));
		List<ActionsDto> actionsDtolist = ruleEngineDto.getActions();
		
		
		ResultMap<? extends Object> verifyResult =  verifySqlLegitimacyService.verifySql(ruleEngineDto.getSql().getFields(),ruleEngineDto.getSql().getCondition());
		//如果验证没有通过，则不用处理
		if(!CommonConstant.SERVICE_SUCCESS.equals(verifyResult.getCode())){
			return verifyResult;
		}
		// 设置用户信息
		systemUtil.setCreaterAndModifier(insertRule, inputMap.getConfig(), true);
		// CodeUtil 产生随机编码，加入规则引擎
		insertRule.setCode(CodeUtil.getCode());
		Integer updateResult = ruleEngineAddMapper.insert(insertRule);
		if (updateResult != 1)
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_ADD_FAIL);
		}
		if(actionsDtolist != null){
			for (ActionsDto actionsDto : actionsDtolist)
			{
				if (actionsDto.getActionTypeId()==null)
				{
					result.setCode(ErrorCodes.RULE_ENGINE_ACTIONTYPEID_EMPTY.getCode());
					result.setMsg(ErrorCodes.RULE_ENGINE_ACTIONTYPEID_EMPTY.getDesc());
					result.setStatus(ErrorCodes.RULE_ENGINE_ACTIONTYPEID_EMPTY.getStatus());
					return result;
				}
				/*if (actionsDto.getActionParam().isEmpty())
				{
					result.setCode(ErrorCodes.RULE_ENGINE_ACTIONPARAM_EMPTY.getCode());
					result.setMsg(ErrorCodes.RULE_ENGINE_ACTIONPARAM_EMPTY.getDesc());
					return result;
				}*/
				Actions actions = new Actions();
				actions.setActionTypeId(actionsDto.getActionTypeId());
				actions.setActionParamString(actionsDto.getActionParam() != null ?JSON.toJSONString(actionsDto.getActionParam()):null);
				actions.setRuleId(insertRule.getId());
				// 检查是否合法
				if (actionTypeService.selectById(actions.getActionTypeId()) == null)
				{
					throw new BaseException(ErrorCodes.RULE_TYPE_NOT_EXIST);
				}
				// 设置用户信息
				systemUtil.setCreaterAndModifier(actions, inputMap.getConfig(), true);
				actionsMapper.insert(actions);
			}
		}
		
		UserInfo currentUser = inputMap.getConfig();
		if (currentUser != null)
		{
			ruleRedisService.addUserRuleById(insertRule.getId());
		}
		return result;
	}
	
	/***
	 * 验证 version 和 accessMode   dataType是否符合取值范围
	 * checkVersionAccessModeDataTypeStatus:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author hewanwan
	 * @since JDK 1.8
	 */
	public void checkVersionAccessModeDataType(RuleEngineDto ruleEngineDto) {
		
		if(ruleEngineDto.getVersion().intValue() != 0 && ruleEngineDto.getVersion().intValue() != 1) {
			throw new BaseException(ErrorCodes.RULE_ENGINE_VERSION_RANGE_OF_VALUE.getCode(),
					ErrorCodes.RULE_ENGINE_VERSION_RANGE_OF_VALUE.getDesc(),ErrorCodes.RULE_ENGINE_VERSION_RANGE_OF_VALUE.getStatus());
		}
		if(ruleEngineDto.getAccessMode().intValue() != 0 && ruleEngineDto.getAccessMode() != 1 ) {
			throw new BaseException(ErrorCodes.RULE_ENGINE_ACCESSMODE_RANGE_OF_VALUE.getCode(),
					ErrorCodes.RULE_ENGINE_ACCESSMODE_RANGE_OF_VALUE.getDesc(),ErrorCodes.RULE_ENGINE_ACCESSMODE_RANGE_OF_VALUE.getStatus());
		}
		if(ruleEngineDto.getDataType().longValue() != 1) {
			throw new BaseException(ErrorCodes.RULE_ENGINE_DATATYPE_RANGE_OF_VALUE.getCode(),
					ErrorCodes.RULE_ENGINE_DATATYPE_RANGE_OF_VALUE.getDesc(),ErrorCodes.RULE_ENGINE_DATATYPE_RANGE_OF_VALUE.getStatus());
		}
	}
	
	/**
	 * status 是否符合取值范围
	 * checkStatus:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author hewanwan
	 * @param ruleEngineDto
	 * @since JDK 1.8
	 */
	
	public void checkStatus(RuleEngineDto ruleEngineDto) {
		if(ruleEngineDto.getStatus().intValue() != 0 && ruleEngineDto.getStatus().intValue() != 1) {
			throw new BaseException(ErrorCodes.RULE_ENGINE_STATUS_RANGE_OF_VALUE.getCode(),
					ErrorCodes.RULE_ENGINE_STATUS_RANGE_OF_VALUE.getDesc(),ErrorCodes.RULE_ENGINE_STATUS_RANGE_OF_VALUE.getStatus());
		}
	}
	
	public boolean validationBooleanIsNull(RuleEngineDto ruleEngineDto) {
		
		if (StringUtils.isBlank(ruleEngineDto.getName()))
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_NAME_EMPTY.getCode(),ErrorCodes.RULE_ENGINE_NAME_EMPTY.getDesc(),ErrorCodes.RULE_ENGINE_NAME_EMPTY.getStatus());
		}
		if (ruleEngineDto.getDataType() == null)
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_DATATYPE_EMPTY.getCode(),ErrorCodes.RULE_ENGINE_DATATYPE_EMPTY.getDesc(),ErrorCodes.RULE_ENGINE_DATATYPE_EMPTY.getStatus());
		}
		if(ruleEngineDto.getVersion() == null) {
			throw new BaseException(ErrorCodes.RULE_ENGINE_VERSION_EMPTY.getCode(),ErrorCodes.RULE_ENGINE_VERSION_EMPTY.getDesc(),ErrorCodes.RULE_ENGINE_VERSION_EMPTY.getStatus());
		}	
		if(ruleEngineDto.getAccessMode() == null) {
			throw new BaseException(ErrorCodes.RULE_ENGINE_ACCESSMODE_EMPTY.getCode(),ErrorCodes.RULE_ENGINE_ACCESSMODE_EMPTY.getDesc(),ErrorCodes.RULE_ENGINE_ACCESSMODE_EMPTY.getStatus());
		}
	
		if (ruleEngineDto.getSql() == null)
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_SQL_EMPTY.getCode(),ErrorCodes.RULE_ENGINE_SQL_EMPTY.getDesc(),ErrorCodes.RULE_ENGINE_SQL_EMPTY.getStatus());
		}
		if (StringUtils.isBlank(ruleEngineDto.getSql().getFields()))
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_FIELDS_EMPTY.getCode(),ErrorCodes.RULE_ENGINE_FIELDS_EMPTY.getDesc(),ErrorCodes.RULE_ENGINE_FIELDS_EMPTY.getStatus());
		}
		if (StringUtils.isBlank(ruleEngineDto.getSql().getTopic()))
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_TOPIC_EMPTY.getCode(),ErrorCodes.RULE_ENGINE_TOPIC_EMPTY.getDesc(),ErrorCodes.RULE_ENGINE_TOPIC_EMPTY.getStatus());
		}
		return true;
	}

	/** 删除功能引擎 */
	@Override
	public ResultMap<? extends Object> deleteById(InputMap<RuleEngineDto> inputMap)
			throws Exception, JsonMappingException, IOException
	{
		boolean flag = this.isExitsUser(inputMap);//用户是否存在，如果不存在，直接报错
		
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		if(inputMap.getData() == null || inputMap.getData().getId() == null){
			throw new BaseException(ErrorCodes.FAILED);
		}
		ResultMap<? extends Object> result = new ResultMap<>();
		Integer id = null;
		
		if (inputMap != null && inputMap.getData() != null)
		{
			id = inputMap.getData().getId();
		}

		if (id == null || StringUtils.isBlank(String.valueOf(id)))
		{
			result.setCode(ErrorCodes.RULE_ENGINE_ID_ERROR.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_ID_ERROR.getDesc());
			result.setStatus(ErrorCodes.RULE_ENGINE_ID_ERROR.getStatus());
			return result;
		} 
		
		RuleEngine ruleEngine = ruleEngineAddMapper.selectById(id);
		flag = validationUser(inputMap.getConfig(),ruleEngine == null ? "":ruleEngine.getCreator());
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		actionsMapper.deleteByRuleId(id);
		Integer count = ruleEngineAddMapper.deleteById(id);
		if (count != 1)
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_DELETE_FAIL);
		}
		UserInfo currentUser = inputMap.getConfig();
		if (currentUser != null)
		{
			ruleRedisService.deleteUserRuleById(id);
		}
		return result;
	}

	/** 查询功能引擎 */
	@Override
	public ResultMap<? extends Object> selectById(InputMap<RuleEngineDto> inputMap)
			throws Exception, JsonMappingException, IOException
	{
		boolean flag = this.isExitsUser(inputMap);//用户是否存在，如果不存在，直接报错
		
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		if(inputMap.getData() == null || inputMap.getData().getId() == null){
			throw new BaseException(ErrorCodes.FAILED);
		}
		ResultMap<RuleEngineDto> result = new ResultMap<>();
		Integer id = null;
		if (inputMap != null && inputMap.getData() != null)
		{
			id = inputMap.getData().getId();
		}

		if (id == null || StringUtils.isBlank(String.valueOf(id)))
		{
			result.setCode(ErrorCodes.RULE_ENGINE_ID_EMPTY.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_ID_EMPTY.getDesc());
			result.setStatus(ErrorCodes.RULE_ENGINE_ID_EMPTY.getStatus());
			return result;
		}

		RuleEngine ruleEngine = ruleEngineAddMapper.selectById(id);
		RuleEngineDto ruleEngineDto = new RuleEngineDto();
		// 如果为空，则直接返回
		if (ruleEngine == null)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		flag = validationUser(inputMap.getConfig(),ruleEngine == null ? "":ruleEngine.getCreator());
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		BeanUtils.copyProperties(ruleEngine, ruleEngineDto);
		List<ActionsDto> actionsDtolist = new ArrayList<ActionsDto>();
		Map<String,Object> queryParam = new HashMap<>(1);
		queryParam.put(CommonConstant.RULE_ID, id);
		List<Actions> actionslist = actionsMapper.selectByRuleId(queryParam);
		for (Actions actions : actionslist)
		{
			ActionsDto actionsDto = new ActionsDto();
			BeanUtils.copyProperties(actions, actionsDto);
			actionsDtolist.add(actionsDto);
		}
		ruleEngineDto.setActions(actionsDtolist);
		result.setData(ruleEngineDto);
		return result;
	}

	/** 更新功能引擎 */
	@Override
	public ResultMap<? extends Object> update(InputMap<RuleEngineDto> inputMap)
			throws Exception, JsonMappingException, IOException
	{
		ResultMap<RuleEngineDto> result = new ResultMap<>();
		
		boolean flag = this.isExitsUser(inputMap);//用户是否存在，如果不存在，直接报错
		
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		if(inputMap.getData() == null || inputMap.getData().getId() == null){
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		/**
		 * 检查规则名字是否存在
		 */
		checkRuleName(inputMap.getData().getName(),inputMap.getData().getId());
		
		RuleEngine ruleEngine = ruleEngineAddMapper.selectById(inputMap.getData().getId());
		flag = validationUser(inputMap.getConfig(),ruleEngine == null ? "":ruleEngine.getCreator());
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		RuleEngineDto ruleEngineDto = inputMap.getData();
		
		//检查字段取值范围
		checkVersionAccessModeDataType(ruleEngineDto);
		
		checkStatus(ruleEngineDto);
		//检查是否合法
		validationBooleanIsNull(ruleEngineDto);
		ResultMap<? extends Object> verifyResult =  verifySqlLegitimacyService.verifySql(ruleEngineDto.getSql().getFields(),ruleEngineDto.getSql().getCondition());
		//如果验证没有通过，则不用处理
		if(!CommonConstant.SERVICE_SUCCESS.equals(verifyResult.getCode())){
			return verifyResult;
		}
		
		List<ActionsDto> actionslist = ruleEngineDto.getActions();
		Map<String,Object> deleteMap = new HashMap<>();
		deleteMap.put(CommonConstant.RULE_ID, inputMap.getData().getId());
		List<Integer> extandList = new ArrayList<>();
		if (actionslist != null)
		{
			for (ActionsDto actionsDto : actionslist)
			{
				Actions actions = new Actions();
				BeanUtils.copyProperties(actionsDto, actions);
				actions.setRuleId(inputMap.getData().getId());
				actions.setActionParamString(JSON.toJSONString(actionsDto.getActionParam()));

				// 检查是否合法
				if (actionsDto.getActionTypeId()==null)
				{
					result.setCode(ErrorCodes.RULE_ENGINE_ACTIONTYPEID_EMPTY.getCode());
					result.setMsg(ErrorCodes.RULE_ENGINE_ACTIONTYPEID_EMPTY.getDesc());
					result.setStatus(ErrorCodes.RULE_ENGINE_ACTIONTYPEID_EMPTY.getStatus());
					return result;
				}
				if (actionsDto.getActionParam().isEmpty())
				{
					result.setCode(ErrorCodes.RULE_ENGINE_ACTIONPARAM_EMPTY.getCode());
					result.setMsg(ErrorCodes.RULE_ENGINE_ACTIONPARAM_EMPTY.getDesc());
					result.setStatus(ErrorCodes.RULE_ENGINE_ACTIONPARAM_EMPTY.getStatus());
					return result;
				}
				if (actionTypeService.selectById(actions.getActionTypeId()) == null)
				{
					throw new BaseException(ErrorCodes.RULE_TYPE_NOT_EXIST);
				}
				
				if (actions.getId() != null)
				{
					// 设置用户信息
					systemUtil.setCreaterAndModifier(actions, inputMap.getConfig(), false);
					actionsMapper.updateById(actions);
					
				} else
				{
					// 设置用户信息
					systemUtil.setCreaterAndModifier(actions, inputMap.getConfig(), true);
					actionsMapper.insert(actions);
				}
				extandList.add(actions.getId());
			}
		}
		deleteMap.put(CommonConstant.EXTANDIDS, extandList);
		actionsMapper.deleteByMap(deleteMap);
		RuleEngine insertRule = new RuleEngine();
		BeanUtils.copyProperties(ruleEngineDto, insertRule);
		Sql sql = ruleEngineDto.getSql();
		insertRule.setSqlString(JSON.toJSONString(sql));
		// 设置用户信息
		systemUtil.setCreaterAndModifier(insertRule, inputMap.getConfig(), false);
		Integer updateResult = ruleEngineAddMapper.updateById(insertRule);
		if (updateResult != 1)
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_UPDATE_FAIL);
		}
		UserInfo currentUser = inputMap.getConfig();
		if (currentUser != null)
		{
			/*String deleteRuleId = null;
			if(insertRule.getStatus() != null && insertRule.getStatus().intValue() ==0){
				deleteRuleId =String.valueOf(insertRule.getId());
			}*/
			ruleRedisService.updateUserRuleById(insertRule.getId());
		}
		return result;
	}

	/**
	 * 
	 * TODO 测试规则引擎配置是否正确,采用微服务调用ruleEngine类
	 * 
	 * @see com.foxconn.core.pro.server.rule.engine.front.service.RuleEngineService#debug(com.foxconn.core.pro.server.rule.engine.front.dto.RuleDebugDto)
	 */
	@Override
	public ResultMap<? extends Object> debug(InputMap<RuleDebugDto> inputMap)
			throws Exception, JsonMappingException, IOException
	{
		ResultMap<JSON> result = new ResultMap<>();
		RuleDebugDto bean = inputMap.getData();
		
		// 判断是否合法 userId与创建人是否一致
		boolean flag = this.isExitsUser(inputMap);//用户是否存在，如果不存在，直接报错
		
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		if (StringUtils.isBlank(bean.getFields()))
		{
			result.setCode(ErrorCodes.RULE_ENGINE_DEBUG_FIELD_EMPTY.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_DEBUG_FIELD_EMPTY.getDesc());
			result.setStatus(ErrorCodes.RULE_ENGINE_DEBUG_FIELD_EMPTY.getStatus());
			return result;
		}

		if (StringUtils.isBlank(bean.getTopic()))
		{
			result.setCode(ErrorCodes.RULE_ENGINE_DEBUG_TOPIC_EMPTY.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_DEBUG_TOPIC_EMPTY.getDesc());
			result.setStatus(ErrorCodes.RULE_ENGINE_DEBUG_TOPIC_EMPTY.getStatus());
			return result;
		}
		if (bean.getData() == null)
		{
			result.setCode(ErrorCodes.RULE_ENGINE_DEBUG_DATA_EMPTY.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_DEBUG_DATA_EMPTY.getDesc());
			result.setStatus(ErrorCodes.RULE_ENGINE_DEBUG_DATA_EMPTY.getStatus());
			return result;
		}
		ResultMap<? extends Object> verifyResult =  verifySqlLegitimacyService.verifySql(bean.getFields(),bean.getCondition());
		//如果验证没有通过，则不用处理
		if(!CommonConstant.SERVICE_SUCCESS.equals(verifyResult.getCode())){
			return verifyResult;
		}
		JSONObject sourceData =  bean.getData();
		String dataTopic = null;
		JSONObject dataParams = null;
		String topic =bean.getTopic();
		String condition = bean.getCondition();
		String fileds = bean.getFields();
		JSONObject dataAction = null;
		if(sourceData.get(CommonConstant.PARAMS) instanceof JSONObject){
			dataTopic = sourceData.getString(CommonConstant.TOPIC);
			dataParams = sourceData.getJSONObject(CommonConstant.PARAMS);
			result.setData(getDataAction(dataTopic,topic,condition,fileds,dataParams));
		} else if(sourceData.get(CommonConstant.PARAMS) instanceof JSONArray){
			JSONArray jsonArray = sourceData.getJSONArray(CommonConstant.PARAMS);
			dataTopic = sourceData.getString(CommonConstant.TOPIC);
			if(jsonArray != null && jsonArray.size()>0){
				JSONArray jsonActionArray = new JSONArray(jsonArray.size());
				for(int i = 0; i < jsonArray.size(); i++){
					dataParams = jsonArray.getJSONObject(i);
					dataAction = getDataAction(dataTopic,topic,condition,fileds,dataParams);
					log.debug("==========dataAction======"+dataAction);
					if(dataAction != null){
						jsonActionArray.add(dataAction);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * getDataAction:获取单条规则数据. <br/>
	 * @author liupingan
	 * @return
	 * @since JDK 1.8
	 */
	private JSONObject getDataAction(String dataTopic,String topic,String condition,String fileds,JSONObject dataParams ){
		
		if (StringUtils.isBlank(dataTopic) || dataParams == null)
		{
			return null;
		}

		// topic处理
		if (!StringUtils.isBlank(topic))
		{
			// 替换topic表达式
			topic = CommonConstant.OPERATOR_NO + topic;
			topic = topic.replaceAll(CommonConstant.DOUBLE_SLASH_ADD, CommonConstant.REGULAR_EXPRESSION_TWO).replace(CommonConstant.WELL_NUMBER_ONE, CommonConstant.REGULAR_EXPRESSION_THREE);
			topic = topic + CommonConstant.DOLLAR;
		} else
		{
			return null;
		}
		TopicAnalysis topicAnalysis = new TopicAnalysis();
		boolean isMatch = topicAnalysis.isMatchRule(dataTopic, topic);
		if (!isMatch)
		{
			return null;
		}
		// 进行数据处理
		
		Map<String,Object> context = null;
		if(StringUtils.isBlank(condition)){
			
		} else {
			context = new HashMap<>(2);
			context.put(CommonConstant.TOPIC, dataTopic);
			context.put(CommonConstant.ROOT, dataParams);// 根据协议去解析
			isMatch = conditionAnalysis.isMatchRule(context,condition);
		}
		
		if (!isMatch)
		{
			return null;
		}
		
		context = new HashMap<>(2);
		context.put(CommonConstant.TOPIC, context);
		context.put(CommonConstant.ROOT, dataParams);// 根据协议去解析
		JSONObject dataAction = null;
		try{
			dataAction = fieldAnalysis.excute(context, fileds);
			return dataAction;
		} catch(Exception e){
			return null;
		}
	}

	@Override
	public ResultMap<? extends Object> updateStatus(InputMap<RuleEngineDto> inputMap)
			throws Exception, JsonMappingException, IOException
	{
		ResultMap<RuleEngineDto> result = new ResultMap<>();
		// 判断是否合法 userId与创建人是否一致
		boolean flag = this.isExitsUser(inputMap);//用户是否存在，如果不存在，直接报错
		
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		if(inputMap.getData() == null || inputMap.getData().getId() == null){
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		
		RuleEngine ruleEngine = ruleEngineAddMapper.selectById(inputMap.getData().getId());
		flag = validationUser(inputMap.getConfig(),ruleEngine == null ? "":ruleEngine.getCreator());
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		Integer ruleId = inputMap.getData().getId();
		RuleEngineDto ruleEngineDto = inputMap.getData();

		if (ruleEngineDto.getStatus() == null)
		{
			result.setCode(ErrorCodes.RULE_ENGINE_NAME_EMPTY.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_NAME_EMPTY.getDesc());
			result.setStatus(ErrorCodes.RULE_ENGINE_NAME_EMPTY.getStatus());
			return result;
		}
		//判断status的取值范围
		checkStatus(ruleEngineDto);
		
		if (ruleEngineDto.getId() == null)
		{
			result.setCode(ErrorCodes.RULE_ENGINE_ID_EMPTY.getCode());
			result.setMsg(ErrorCodes.RULE_ENGINE_ID_EMPTY.getDesc());
			result.setStatus(ErrorCodes.RULE_ENGINE_ID_EMPTY.getStatus());
			return result;
		}

		RuleEngine insertRule = new RuleEngine();
		// BeanUtils.copyProperties(ruleEngineDto, insertRule);
		insertRule.setId(ruleEngineDto.getId());
		insertRule.setStatus(ruleEngineDto.getStatus());
		// 设置用户信息
		systemUtil.setCreaterAndModifier(insertRule, inputMap.getConfig(), false);
		Integer updateResult = ruleEngineAddMapper.updateById(insertRule);
		if (updateResult != 1)
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_UPDATE_FAIL);
		}
		UserInfo currentUser = inputMap.getConfig();
		if (currentUser != null)
		{
			if(ruleEngineDto.getStatus().intValue() == 0){
				ruleRedisService.deleteUserRuleById(ruleId);
			} else {
				ruleRedisService.addUserRuleById(ruleId);
			}
			//ruleRedisService.updateUserRule(ruleEngineDto.getStatus().intValue() == 0 ? String.valueOf(ruleId):null);
		}
		return result;
	}

	/**
	 * 
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.foxconn.core.pro.server.rule.engine.front.service.RuleEngineService#checkRuleName(com.foxconn.core.pro.server.rule.engine.front.dto.InputMap)
	 */
	@Override
	public ResultMap<? extends Object> checkRuleName(InputMap<RuleEngineDto> inputMap)
			throws Exception, JsonMappingException, IOException
	{
		boolean flag = this.isExitsUser(inputMap);//用户是否存在，如果不存在，直接报错
		
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		ResultMap<? extends Object> result = new ResultMap<>();
		RuleEngineDto ruleEngineDto = inputMap.getData();
		/**
		 * 检查规则名字是否存在
		 */
		checkRuleName(ruleEngineDto.getName(),ruleEngineDto.getId());
		return result;
	}
	
	/**
	 * checkRuleName:(檢查名字是否存在). <br/>
	 *
	 * @author liupingan
	 * @param ruleName
	 * @param id
	 * @since JDK 1.8
	 */
	private void checkRuleName(String ruleName, Integer id)
	{
		if (StringUtils.isBlank(ruleName))
		{
			throw new BaseException(ErrorCodes.RULE_ENGINE_NAME_EMPTY.getCode(),
					ErrorCodes.RULE_ENGINE_NAME_EMPTY.getDesc(),ErrorCodes.RULE_ENGINE_NAME_EMPTY.getStatus());
		}

		Map<String, Object> map = new HashMap<>(1);
		map.put(CommonConstant.NAME, ruleName);
		List<RuleEngine> resultList = ruleEngineAddMapper.selectByMap(map);
		if (resultList != null && resultList.size() > 0)
		{
			if (id == null)
			{
				throw new BaseException(ErrorCodes.RULE_ENGINE_RULE_NAME_EXISTS.getCode(),
						ErrorCodes.RULE_ENGINE_RULE_NAME_EXISTS.getDesc(),ErrorCodes.RULE_ENGINE_RULE_NAME_EXISTS.getStatus());
			}
			for (int i = 0; i < resultList.size(); i++)
			{
				if(id.intValue() != resultList.get(i).getId().intValue()) 
				{
					throw new BaseException(ErrorCodes.RULE_ENGINE_RULE_NAME_EXISTS.getCode(),
							ErrorCodes.RULE_ENGINE_RULE_NAME_EXISTS.getDesc(),ErrorCodes.RULE_ENGINE_RULE_NAME_EXISTS.getStatus());
				}
			}
		}
	}
}