/**
 * Project Name:rule-engine-core
 * File Name:ConditionAnalysis.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.analysis
 * Date:2018年8月23日下午5:31:06
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.common.util.DataUtil;
import com.foxconn.core.pro.server.rule.engine.core.common.util.RedisUtil;
import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.core.entity.Rule;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.MqttData;
import com.foxconn.core.pro.server.rule.engine.core.express.SpringBeanRunner;
import com.foxconn.core.pro.server.rule.engine.core.sql.BaseSingleParser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:ConditionAnalysis <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月23日 下午5:31:06 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Setter
@Getter
@Slf4j
public class ConditionAnalysis
{
	// 得到满足条件的topic
	private List<Rule> rules;

	@Resource
	private SpringBeanRunner runner;

	@Autowired
	@Qualifier(CommonConstant.CONDITION_PARSER)
	private BaseSingleParser baseSingleParser;

	private MqttData data;

	public ConditionAnalysis()
	{

	}

	public ConditionAnalysis(MqttData data)
	{
		this(data, null);
	}

	public ConditionAnalysis(MqttData data, List<Rule> rules)
	{
		this.data = data;
		this.rules = rules;
	}

	/**
	 * isMatch:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author liupingan
	 * @return
	 * @since JDK 1.8
	 */
	public boolean isMatchRule()
	{
		return isMatchRule(this.data);
	}

	/**
	 * 
	 * isMatchRule:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author liupingan
	 * @param topic
	 * @return
	 * @since JDK 1.8
	 */
	public boolean isMatchRule(MqttData data)
	{
		boolean result = false;

		return result;
	}
	
	public boolean isMatchRule(Map<String, Object> context, String condition)
	{
		boolean isMatch = false;
		if (StringUtils.isEmpty(condition))
		{// 没有条件默认匹配
			isMatch = true;
		} else
		{
			baseSingleParser.setOriginalValue(condition);
			String content = baseSingleParser.getQLExpress();
			isMatch = (boolean) runner.executeExpress(content, context);
		}

		return isMatch;
	}

	public List<Rule> getMatchRuleList(Map<String, Object> context) throws Exception
	{
		return getMatchRuleList(this.data, context, this.rules);
	}
	
	
	public List<Rule> getMatchRuleList(Map<String, Object> context,String dbName,HashOperations<String, String, Object> hashOperations) throws Exception
	{
		return getMatchRuleList(this.data, context, this.rules);
	}

	/**
	 * 
	 * getMatchRuleList:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author liupingan
	 * @param topic
	 * @return
	 * @since JDK 1.8
	 */
	public List<Rule> getMatchRuleList(Map<String, Object> context, List<Rule> rules) throws Exception
	{
		return getMatchRuleList(this.data, context, rules);
	}

	/**
	 * 
	 * getMatchRuleList:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author liupingan
	 * @return
	 * @throws Exception
	 * @since JDK 1.8
	 */
	public List<Rule> getMatchRuleList(MqttData data, Map<String, Object> context, List<Rule> rules) throws Exception
	{
		List<Rule> result = null;
		if (rules == null || rules.size() == 0)
		{
			return result;
		}
		// 进行分半初始化
		result = new ArrayList<>(rules.size() / 2);
		// 根据正则表达式匹配
		boolean isMatch = false;
		String topicTemp = null;
		Rule rule = null;
		for (int i = 0; i < rules.size(); i++)
		{
			rule = rules.get(i);
			topicTemp = rule.getTopic();
			// 如果没有topic，则直接返回
			if (StringUtils.isEmpty(topicTemp))
			{
				continue;
			}
			if (StringUtils.isEmpty(rules.get(i).getCondition()))
			{// 没有条件默认匹配
				isMatch = true;
			} else
			{
				baseSingleParser.setOriginalValue(rules.get(i).getCondition());
				String content = baseSingleParser.getQLExpress();
				// log.info(content);
				isMatch = (boolean) runner.executeExpress(content, context);
				// isMatch = Pattern.matches(topicTemp, topic);
				// 如果匹配成功，则返回对那个的List
			}

			if (isMatch)
			{
				result.add(DataUtil.copyImplSerializable(rule));
			}
		}
		return result;
	}
	
	/**
	 * 
	 * getMatchRuleList:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author liupingan
	 * @param data
	 * @param context
	 * @param rules
	 * @param dbName
	 * @param hashOperations
	 * @return
	 * @throws Exception
	 * @since JDK 1.8
	 */
	public List<Rule> getMatchRuleList(MqttData data, Map<String, Object> context, List<Rule> rules,String dbName,String userId,HashOperations<String, String, Object> hashOperations) throws Exception
	{
		List<Rule> result = null;
		if (rules == null || rules.size() == 0)
		{
			return result;
		}
		// 进行分半初始化
		result = new ArrayList<>(rules.size() / 2);
		// 根据正则表达式匹配
		boolean isMatch = false;
		String topicTemp = null;
		Rule rule = null;
		String ruleId = null;
		String modifyTime= null;
		String objectValue = null;
		String contentStr;
		for (int i = 0; i < rules.size(); i++)
		{
			rule = rules.get(i);
			topicTemp = rule.getTopic();
			// 如果没有topic，则直接返回
			if (StringUtils.isEmpty(topicTemp))
			{
				continue;
			}
			try{
				if (rules.get(i).getCondition() == null || StringUtils.isEmpty(rules.get(i).getCondition()))
				{// 没有条件默认匹配
					isMatch = true;
				} else
				{
					
					ruleId = String.valueOf(rule.getId());
					modifyTime= JSONObject.toJSONString(rule.getModifyTime());
					objectValue = ruleId+CommonConstant.UNDER_LINE+modifyTime;
					if(hashOperations.hasKey(RedisUtil.getRuleActionConditionScriptKey(dbName,userId),objectValue)) {
						Object content=hashOperations.get(RedisUtil.getRuleActionConditionScriptKey(dbName,userId),objectValue);
						if(content != null){//设置有效期限继续增加
							hashOperations.getOperations().expire(RedisUtil.getRuleActionConditionScriptKey(dbName,userId), 7, TimeUnit.DAYS);
							contentStr = content.toString();
						} else {
							return null;
						}
					} else {
						baseSingleParser.setOriginalValue(rules.get(i).getCondition());
						contentStr  = baseSingleParser.getQLExpress();
						hashOperations.put(RedisUtil.getRuleActionConditionScriptKey(dbName,userId), objectValue, contentStr);
						//一个星期过期一次
						hashOperations.getOperations().expire(RedisUtil.getRuleActionConditionScriptKey(dbName,userId), 7, TimeUnit.DAYS);
					}
					isMatch = (boolean) runner.executeExpress(contentStr, context);
					// 如果匹配成功，则返回对那个的List
				}
			} catch(Exception e){
				isMatch = false;
				log.error("conditon anlysis Exception:",e);
			}
			if (isMatch)
			{
				result.add(DataUtil.copyImplSerializable(rule));
			}
		}
		return result;
	}
}
