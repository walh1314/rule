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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.foxconn.core.pro.server.rule.engine.front.common.RuleRedisService;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.PageResult;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.common.util.SystemUtil;
import com.foxconn.core.pro.server.rule.engine.front.dto.ActionsDto;
import com.foxconn.core.pro.server.rule.engine.front.dto.InputMap;
import com.foxconn.core.pro.server.rule.engine.front.entity.Actions;
import com.foxconn.core.pro.server.rule.engine.front.entity.RuleEngine;
import com.foxconn.core.pro.server.rule.engine.front.entity.UserInfo;
import com.foxconn.core.pro.server.rule.engine.front.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.front.exception.ErrorCodes;
import com.foxconn.core.pro.server.rule.engine.front.mapper.ActionsMapper;
import com.foxconn.core.pro.server.rule.engine.front.mapper.RuleEngineMapper;
import com.foxconn.core.pro.server.rule.engine.front.service.ActionTypeService;
import com.foxconn.core.pro.server.rule.engine.front.service.ActionsService;
import org.apache.commons.lang.StringUtils;

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
public class ActionsServiceImpl implements ActionsService
{

	@Autowired
	private RuleRedisService ruleRedisService;
	
	@Autowired
	private SystemUtil systemUtil;
	
	@Autowired
	private ActionsMapper actionsMapper;
	
	@Autowired
	private ActionTypeService actionTypeService;
	
	@Autowired
	private RuleEngineMapper ruleEngineMapper;

	@Override
	public ResultMap<? extends Object> insert(InputMap<ActionsDto> bean)
			throws Exception, JsonMappingException, IOException
	{
		ResultMap<PageResult<RuleEngine>> result = new ResultMap<>();
		// 判断是否合法 userId与创建人是否一致
		boolean flag = this.isExitsUser(bean);// 用户是否存在，如果不存在，直接报错

		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		// TODO Auto-generated method stub
		ActionsDto actionsDto = bean.getData();
		if (actionsDto == null || actionsDto.getRuleId() == null || actionsDto.getActionTypeId() == null)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		// 检查是否合法
		if (actionTypeService.selectById(actionsDto.getActionTypeId()) == null)
		{
			throw new BaseException(ErrorCodes.RULE_TYPE_NOT_EXIST);
		}
		
		RuleEngine ruleEngine = ruleEngineMapper.selectById(actionsDto.getRuleId());
		flag = validationUser(bean.getConfig(),ruleEngine == null ? "":ruleEngine.getCreator());
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		Actions actions = new Actions();
		actions.setActionTypeId(actionsDto.getActionTypeId());
		actions.setActionParamString(
				actionsDto.getActionParam() != null ? JSON.toJSONString(actionsDto.getActionParam()) : null);
		actions.setRuleId(actionsDto.getRuleId());
		systemUtil.setCreaterAndModifier(actions, bean.getConfig(), true);
		actionsMapper.insert(actions);
		UserInfo currentUser = bean.getConfig();
		if (currentUser != null)
		{
			ruleRedisService.updateUserRuleById(actionsDto.getRuleId());
		}
		return result;
	}

	@Override
	public ResultMap<? extends Object> deleteById(InputMap<ActionsDto> bean)
			throws Exception, JsonMappingException, IOException
	{

		ResultMap<? extends Object> result = new ResultMap<>();
		// 判断是否合法 userId与创建人是否一致
		boolean flag = this.isExitsUser(bean);// 用户是否存在，如果不存在，直接报错

		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		if(bean.getData() == null || bean.getData().getId() == null){
			throw new BaseException(ErrorCodes.FAILED);
		}
		Integer id = bean.getData().getId();

		if (id == null || StringUtils.isBlank(String.valueOf(id)))
		{
			result.setCode(ErrorCodes.RULE_ACTION_ID_ERROR.getCode());
			result.setMsg(ErrorCodes.RULE_ACTION_ID_ERROR.getDesc());
			result.setStatus(ErrorCodes.RULE_ACTION_ID_ERROR.getStatus());
			return result;
		} 
		
		Actions action = actionsMapper.selectById(id);
		if(action==null) {
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		flag = validationUser(bean.getConfig(),action == null ? "":action.getCreator());
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		Integer count  = actionsMapper.deleteById(id);
		if (count != 1)
		{
			throw new BaseException(ErrorCodes.RULE_ACTION_DELETE_FAIL);
		}
		UserInfo currentUser = bean.getConfig();
		if (currentUser != null)
		{
			ruleRedisService.updateUserRuleById(action.getRuleId());
		}
		return result;
	}

	@Override
	public ResultMap<? extends Object> selectById(InputMap<ActionsDto> bean)
			throws Exception, JsonMappingException, IOException
	{

		ResultMap<ActionsDto> result = new ResultMap<>();
		// 判断是否合法 userId与创建人是否一致
		boolean flag = this.isExitsUser(bean);// 用户是否存在，如果不存在，直接报错

		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		if(bean.getData() == null || bean.getData().getId() == null){
			throw new BaseException(ErrorCodes.FAILED);
		}
		Integer id = bean.getData().getId();

		if (id == null || StringUtils.isBlank(String.valueOf(id)))
		{
			result.setCode(ErrorCodes.RULE_ACTION_ID_ERROR.getCode());
			result.setMsg(ErrorCodes.RULE_ACTION_ID_ERROR.getDesc());
			result.setStatus(ErrorCodes.RULE_ACTION_ID_ERROR.getStatus());
			return result;
		} 
		
		Actions action = actionsMapper.selectById(id);
		flag = validationUser(bean.getConfig(),action == null ? "":action.getCreator());
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		ActionsDto actionsDto = new ActionsDto();
		/*actionsDto.setActionParam(action.getActionParam());
		actionsDto.setId(action.getId());
		actionsDto.setRuleId(action.getRuleId());
		actionsDto.setActionTypeId(action.getActionTypeId());
		actionsDto.setName(action.getName());*/
		BeanUtils.copyProperties(action,actionsDto);
		result.setData(actionsDto);
		return result;
	}

	@Override
	public ResultMap<? extends Object> update(InputMap<ActionsDto> bean)
			throws Exception, JsonMappingException, IOException
	{

		ResultMap<? extends Object> result = new ResultMap<>();
		// 判断是否合法 userId与创建人是否一致
		boolean flag = this.isExitsUser(bean);// 用户是否存在，如果不存在，直接报错

		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		if(bean.getData() == null || bean.getData().getId() == null){
			throw new BaseException(ErrorCodes.FAILED);
		}
		Integer id = bean.getData().getId();

		if (id == null || StringUtils.isBlank(String.valueOf(id)))
		{
			result.setCode(ErrorCodes.RULE_ACTION_ID_ERROR.getCode());
			result.setMsg(ErrorCodes.RULE_ACTION_ID_ERROR.getDesc());
			result.setStatus(ErrorCodes.RULE_ACTION_ID_ERROR.getStatus());
			return result;
		} 
		
		Actions action = actionsMapper.selectById(id);
		flag = validationUser(bean.getConfig(),action == null ? "":action.getCreator());
		if (flag == false)
		{
			throw new BaseException(ErrorCodes.FAILED);
		}
		
		ActionsDto actionDao=bean.getData();
		//action = new Actions();
		action.setActionParamString(actionDao.getActionParam() != null ? JSON.toJSONString(actionDao.getActionParam()) : null);
		BeanUtils.copyProperties(actionDao,action);
		Integer count = actionsMapper.updateById(action);
		if (count != 1)
		{
			throw new BaseException(ErrorCodes.RULE_ACTION_UPDATE_FAIL);
		}
		UserInfo currentUser = bean.getConfig();
		if (currentUser != null)
		{
			ruleRedisService.updateUserRuleById(action.getRuleId());
		}
		return result;
	}

	private boolean isExitsUser(InputMap<? extends Object> input)
	{
		if (input == null)
		{
			return false;
		}

		UserInfo currentUser = input.getConfig();
		if (currentUser == null)
		{
			return false;
		}
		if (StringUtils.isBlank(currentUser.getUserId()))
		{
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

}
