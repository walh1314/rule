/**
 * Project Name:rule-engine-front
 * File Name:RuleRedisService.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.common
 * Date:2018年9月1日下午1:54:00
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.common.util.RedisUtil;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.UserDataBaseBean;
import com.foxconn.core.pro.server.rule.engine.front.common.cache.entity.ActionCache;
import com.foxconn.core.pro.server.rule.engine.front.common.cache.entity.RuleCache;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.common.util.SystemUtil;
import com.foxconn.core.pro.server.rule.engine.front.dto.InputMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.NoticeDto;
import com.foxconn.core.pro.server.rule.engine.front.entity.ActionType;
import com.foxconn.core.pro.server.rule.engine.front.entity.Actions;
import com.foxconn.core.pro.server.rule.engine.front.entity.RuleEngine;
import com.foxconn.core.pro.server.rule.engine.front.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.front.mapper.ActionTypeMapper;
import com.foxconn.core.pro.server.rule.engine.front.mapper.RuleEngineMapper;

/**
 * 规则缓存服务 ClassName:RuleRedisService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月1日 下午1:54:00 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Component
@Scope(CommonConstant.SINGLE_TON)
@Transactional(propagation = Propagation.REQUIRED, rollbackFor =
{ Exception.class, BaseException.class, RuntimeException.class, Throwable.class })
public class RuleRedisService
{
/*	@Autowired
	StringRedisTemplate stringRedisTemplate;*/
	

   // @Autowired
   // private RedisTemplate redisTemplate;
	
	@Autowired
    private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private ActionTypeMapper actionTypeMapper;

	@Autowired
	private RuleEngineMapper ruleEngineMapper;

	@Autowired
	private SystemUtil systemUtil;

	public void deleteUserRuleById(Integer ruleId)
	{
		UserDataBaseBean tenantDb = systemUtil.getUserDataBaseBean();
		String dbName = "";
		if (tenantDb != null && tenantDb.getDeviceOwner() != null && StringUtils.isNotBlank(tenantDb.getDeviceOwner()))
		{
			dbName =tenantDb.getDb().trim();
		}
		
		String userId = "";
		if (tenantDb != null && tenantDb.getDeviceOwner() != null && StringUtils.isNotBlank(tenantDb.getDeviceOwner()))
		{
			userId =tenantDb.getDeviceOwner().trim();
		}
		
		Set<String> sets = hashOperations.keys(RedisUtil.getRuleKey(dbName,userId));
		if(sets != null && sets.size()>0 ){
			hashOperations.delete(RedisUtil.getRuleKey(dbName,userId), String.valueOf(ruleId));
			if(hashOperations.hasKey(RedisUtil.getRuleActionKey(dbName,userId),String.valueOf(ruleId))){
				hashOperations.delete(RedisUtil.getRuleActionKey(dbName,userId), String.valueOf(ruleId));
			}
		} else {
			Map<String, Object> map = new HashMap<>(1);
			map.put(CommonConstant.STATUS, 1);
			// 只查询有效的规则引擎
			List<RuleEngine> rules = ruleEngineMapper.selectByMapOrderbyCreator(map);
			
			// 查询所有动作类型
			List<ActionType> actionTypes = actionTypeMapper.selectAll();
			Map<Integer, ActionType> actionTypeMap = new HashMap<>();
			if (actionTypes != null && actionTypes.size() > 0)
			{
				actionTypeMap = getNameAccountMap(actionTypes);
			}
			cacheRule(rules, actionTypeMap,dbName,userId);
		}
	}
	
	public void addUserRuleById(Integer ruleId)
	{
		Map<String, Object> map = new HashMap<>(1);
		map.put(CommonConstant.STATUS, 1);
		
		UserDataBaseBean tenantDb = systemUtil.getUserDataBaseBean();
		String dbName = "";
		if (tenantDb != null && tenantDb.getDb() != null && StringUtils.isNotBlank(tenantDb.getDb()))
		{
			dbName =tenantDb.getDb().trim();
		}
		String userId = "";
		if (tenantDb != null && tenantDb.getDeviceOwner() != null && StringUtils.isNotBlank(tenantDb.getDeviceOwner()))
		{
			userId =tenantDb.getDeviceOwner().trim();
		}
		
		//判断是否有缓存，如果没有，则进行全部缓存处理,
		Set<String> sets = hashOperations.keys(RedisUtil.getRuleKey(dbName,userId));
		if(sets != null && sets.size()>0 ){
			map.put(CommonConstant.ID, ruleId);
		}
		// 只查询有效的规则引擎
		List<RuleEngine> rules = ruleEngineMapper.selectByMapOrderbyCreator(map);
		
		// 查询所有动作类型
		List<ActionType> actionTypes = actionTypeMapper.selectAll();
		Map<Integer, ActionType> actionTypeMap = new HashMap<>();
		if (actionTypes != null && actionTypes.size() > 0)
		{
			actionTypeMap = getNameAccountMap(actionTypes);
		}
		cacheRule(rules, actionTypeMap,dbName,userId,ruleId);
	}
	
	public void updateUserRuleById(Integer ruleId)
	{
		Map<String, Object> map = new HashMap<>(1);
		map.put(CommonConstant.STATUS, 1);
		UserDataBaseBean tenantDb = systemUtil.getUserDataBaseBean();
		String dbName = "";
		if (tenantDb != null && tenantDb.getDb() != null && StringUtils.isNotBlank(tenantDb.getDb()))
		{
			dbName =tenantDb.getDb().trim();
		}
		String userId = "";
		if (tenantDb != null && tenantDb.getDeviceOwner() != null && StringUtils.isNotBlank(tenantDb.getDeviceOwner()))
		{
			userId =tenantDb.getDeviceOwner().trim();
		}
		//判断是否有缓存，如果没有，则进行全部缓存处理,
		Set<String> sets = hashOperations.keys(RedisUtil.getRuleKey(dbName,userId));
		if(sets != null && sets.size()>0 ){
			map.put(CommonConstant.ID, ruleId);
		}
		// 只查询有效的规则引擎
		List<RuleEngine> rules = ruleEngineMapper.selectByMapOrderbyCreator(map);
		
		// 查询所有动作类型
		List<ActionType> actionTypes = actionTypeMapper.selectAll();
		Map<Integer, ActionType> actionTypeMap = new HashMap<>();
		if (actionTypes != null && actionTypes.size() > 0)
		{
			actionTypeMap = getNameAccountMap(actionTypes);
		}
		//cacheRule(rules, actionTypeMap);
		cacheRule(rules, actionTypeMap,dbName,userId,ruleId);
	}

	public ResultMap<? extends Object> updateUserRule(InputMap<NoticeDto> bean)
	{
		ResultMap<? extends Object> result = new ResultMap<>();
		// 如果有传入db，则直接用此db，否则
		if (bean != null && bean.getData() != null && bean.getData().getDbName() != null
				&& StringUtils.isNotBlank(bean.getData().getDbName()) && bean.getData().getDeviceOwner() != null
						&& StringUtils.isNotBlank(bean.getData().getDeviceOwner()))
		{
			UserDataBaseBean tenantDb = new UserDataBaseBean();
			tenantDb.setDb(bean.getData().getDbName());
			tenantDb.setTenantId(String.valueOf(bean.getData().getTenantId()));
			tenantDb.setDeviceOwner(bean.getData().getDeviceOwner() );
			systemUtil.setUserDataBaseBean(tenantDb);
		} else
		{
			systemUtil.setUserDataBaseBean(bean.getConfig());
		}
		Map<String, Object> map = new HashMap<>(1);
		map.put(CommonConstant.STATUS, 1);
		// 只查询有效的规则引擎
		List<RuleEngine> rules = ruleEngineMapper.selectByMapOrderbyCreator(map);
		List<ActionType> actionTypes = actionTypeMapper.selectAll();
		Map<Integer, ActionType> actionTypeMap = new HashMap<>();
		if (actionTypes != null && actionTypes.size() > 0)
		{
			actionTypeMap = getNameAccountMap(actionTypes);
		}
		cacheRule(rules, actionTypeMap);
		return result;
	}


	/**
	 * cacheRule:(设置当前库所有缓存). <br/>
	 * 
	 * @author liupingan
	 * @param rules
	 * @param opsForValue
	 * @param actionTypeMap
	 * @since JDK 1.8
	 */
	private void cacheRule(List<RuleEngine> rules, Map<Integer, ActionType> actionTypeMap)
	{
		UserDataBaseBean tenantDb = systemUtil.getUserDataBaseBean();
		String dbName = "";
		String userId = "";
		if (tenantDb != null && tenantDb.getDb() != null && StringUtils.isNotBlank(tenantDb.getDb()))
		{
			dbName =tenantDb.getDb().trim();
		}
		if (tenantDb != null && tenantDb.getDb() != null && StringUtils.isNotBlank(tenantDb.getDeviceOwner()))
		{
			userId =tenantDb.getDeviceOwner().trim();
		}
		if (rules != null && rules.size() > 0)
		{
			// 获取规则List
			RuleCache ruleCache = null;
			for (RuleEngine rule : rules)
			{
				setActionCache(rule.getActions(), rule.getId(), actionTypeMap,dbName,rule.getModifier());

				ruleCache = ruleEngineToRuleCache(rule);
				ruleCache.setModifyTime(rule.getModifyTime());
				// 进行缓存处理
				this.hashOperations.put(RedisUtil.getRuleKey(dbName,rule.getModifier()), String.valueOf(rule.getId()), JSONObject.toJSONString(ruleCache));
				hashOperations.getOperations().expire(RedisUtil.getRuleKey(dbName,rule.getModifier()), 7, TimeUnit.DAYS);
			}
		} else {
			this.hashOperations.getOperations().delete(RedisUtil.getRuleKey(dbName,userId));
			this.hashOperations.getOperations().delete(RedisUtil.getRuleActionKey(dbName,userId));
		}
	}
	
	private void cacheRule(List<RuleEngine> rules, Map<Integer, ActionType> actionTypeMap,String  dbName,String userId)
	{
		
		Set<String> setsRule = hashOperations.keys(RedisUtil.getRuleKey(dbName,userId));
		Set<String> setsAction = hashOperations.keys(RedisUtil.getRuleActionKey(dbName,userId));
		if (rules != null && rules.size() > 0)
		{
			// 获取规则List
			RuleCache ruleCache = null;
			
			for (RuleEngine rule : rules)
			{
				setActionCache(rule.getActions(), rule.getId(), actionTypeMap,dbName,rule.getModifier());
				ruleCache = ruleEngineToRuleCache(rule);
				ruleCache.setModifyTime(rule.getModifyTime());
				// 进行缓存处理
				this.hashOperations.put(RedisUtil.getRuleKey(dbName,rule.getModifier()), String.valueOf(rule.getId()), JSONObject.toJSONString(ruleCache));
				hashOperations.getOperations().expire(RedisUtil.getRuleKey(dbName,rule.getModifier()), 7, TimeUnit.DAYS);
			}
		} else {
			if(setsAction != null && setsAction.size()>0 ){
				this.hashOperations.getOperations().delete(RedisUtil.getRuleActionKey(dbName,userId));
			}
			
			if(setsRule != null && setsRule.size()>0 ){
				this.hashOperations.getOperations().delete(RedisUtil.getRuleKey(dbName,userId));
			}
		}
	}
	
	private void cacheRule(List<RuleEngine> rules, Map<Integer, ActionType> actionTypeMap,String  dbName,String userId,Integer ruleId)
	{
		if(hashOperations.hasKey(RedisUtil.getRuleKey(dbName,userId),String.valueOf(ruleId))){
			hashOperations.delete(RedisUtil.getRuleKey(dbName,userId), String.valueOf(ruleId));
		}
		if(hashOperations.hasKey(RedisUtil.getRuleActionKey(dbName,userId),String.valueOf(ruleId))){
			hashOperations.delete(RedisUtil.getRuleActionKey(dbName,userId), String.valueOf(ruleId));
		}
		if (rules != null && rules.size() > 0)
		{
			// 获取规则List
			RuleCache ruleCache = null;
			//删除所有规则
			for (RuleEngine rule : rules)
			{
				setActionCache(rule.getActions(), rule.getId(), actionTypeMap,dbName,rule.getModifier());

				ruleCache = ruleEngineToRuleCache(rule);
				ruleCache.setModifyTime(rule.getModifyTime());
				// 进行缓存处理
				this.hashOperations.put(RedisUtil.getRuleKey(dbName,rule.getModifier()), String.valueOf(rule.getId()), JSONObject.toJSONString(ruleCache));
				hashOperations.getOperations().expire(RedisUtil.getRuleKey(dbName,rule.getModifier()), 7, TimeUnit.DAYS);
				//System.out.println(hashOperations.get(RedisUtil.getRuleKey(dbName,rule.getModifier()), String.valueOf(rule.getId())));
			}
		}
		
	}

	
	private void setActionCache(List<Actions> actions, Integer ruleId, Map<Integer, ActionType> actionTypeMap,String dbName,String userId)
	{
		List<ActionCache> result = null;
		if (actions != null)
		{
			ActionCache actionCache = null;
			ActionType actionType = null;
			JSONObject actionParams = null;
			JSONObject params = null;
			JSONObject defalutParams = null;
			result = new ArrayList<>(actions.size());
			for (Actions action : actions)
			{
				actionCache = new ActionCache();
				actionParams = action.getActionParam();
				if (actionTypeMap.containsKey(action.getActionTypeId()))
				{
					defalutParams = null;
					actionType = actionTypeMap.get(action.getActionTypeId());
					if (actionType != null)
					{
						actionCache.setClassType(actionType.getClassType());
						defalutParams = actionType.getDefaultParams();
					}
				}
				// 设置缓存时间
				actionCache.setModifyTime(action.getModifyTime());
				params = mergeJSONObject(defalutParams, actionParams);
				actionCache.setParams(params);
				actionCache.setId(action.getId());
				actionCache.setActionTypeId(action.getActionTypeId());
				result.add(actionCache);
			}
		}
		if(result != null){
			this.hashOperations.put(RedisUtil.getRuleActionKey(dbName,userId), String.valueOf(ruleId), JSONObject.toJSONString(result));
			hashOperations.getOperations().expire(RedisUtil.getRuleActionKey(dbName,userId), 7, TimeUnit.DAYS);
		} else if(hashOperations.hasKey(RedisUtil.getRuleActionKey(dbName,userId),String.valueOf(ruleId))){
			hashOperations.delete(RedisUtil.getRuleActionKey(dbName,userId), String.valueOf(ruleId));
		}
	}

	@SuppressWarnings(CommonConstant.UNCHECKED)
	private JSONObject mergeJSONObject(JSONObject bean01, JSONObject bean02)
	{
		if (bean01 == null)
		{
			return bean02;
		}
		if (bean02 == null)
		{
			return bean01;
		}
		Map<String, Object> map1 = JSONObject.parseObject(bean01.toJSONString(), Map.class);
		Map<String, Object> map2 = JSONObject.parseObject(bean02.toJSONString(), Map.class);
		Map<String, Object> map3 = new HashMap<>();
		map3.putAll(map1);
		map3.putAll(map2);
		return new JSONObject(map3);

	}

	/**
	 * 
	 * ruleEngineToRuleCache:进行属性转换. <br/>
	 *
	 * @author liupingan
	 * @param bean
	 * @return
	 * @since JDK 1.8
	 */
	private RuleCache ruleEngineToRuleCache(RuleEngine bean)
	{
		RuleCache result = new RuleCache();
		BeanUtils.copyProperties(bean, result);
		if (bean.getSql() != null)
		{
			result.setField(bean.getSql().getFields());
			result.setTopic(bean.getSql().getTopic());
			result.setCondition(bean.getSql().getCondition());
			result.setAccessMode(bean.getAccessMode());
			result.setVersion(bean.getVersion());
		}
		return result;
	}

	/**
	 * getNameAccountMap:( Action Type list转map集合). <br/>
	 * 
	 * @author liupingan
	 * @param accounts
	 * @return
	 * @since JDK 1.8
	 */
	private Map<Integer, ActionType> getNameAccountMap(List<ActionType> accounts)
	{
		return accounts.stream().collect(Collectors.toMap(ActionType::getId, Function.identity()));
	}

}
