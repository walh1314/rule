/**
 * Project Name:rule-engine-core
 * File Name:TopicAnalysis.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.analysis
 * Date:2018年8月23日下午5:30:47
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.foxconn.core.pro.server.rule.engine.core.common.util.DataUtil;
import com.foxconn.core.pro.server.rule.engine.core.entity.Rule;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:TopicAnalysis <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月23日 下午5:30:47 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
// @AllArgsConstructor
public class TopicAnalysis
{
	private String topic;
	private List<Rule> rules;

	public TopicAnalysis()
	{

	}

	public TopicAnalysis(String topic)
	{
		this(topic, null);
	}

	public TopicAnalysis(String topic, List<Rule> rules)
	{
		this.topic = topic;
		this.rules = rules;
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
	public boolean isMatchRule(String sourceData,String topic)
	{
		boolean isMatch = false;
		if (StringUtils.isEmpty(topic))
		{
			return isMatch;
		}
		isMatch = Pattern.matches(topic, sourceData);
		// 如果匹配成功，则返回对那个的List
		return isMatch;
	}

	public List<Rule> getMatchRuleList()throws Exception
	{
		return getMatchRuleList(this.topic, this.rules);
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
	public List<Rule> getMatchRuleList(List<Rule> rules)throws Exception
	{
		return getMatchRuleList(this.topic, rules);
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
	public List<Rule> getMatchRuleList(String topic, List<Rule> rules) throws Exception
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
			if (StringUtils.isEmpty(topicTemp))
			{
				continue;
			}
			isMatch = Pattern.matches(topicTemp, topic);
			// 如果匹配成功，则返回对那个的List
			if (isMatch)
			{
				result.add(DataUtil.copyImplSerializable(rule));
			}
		}
		return result;
	}
}
