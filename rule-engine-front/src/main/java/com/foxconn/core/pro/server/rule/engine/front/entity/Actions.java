/**
 * Project Name:rule-engine-front
 * File Name:Actions.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.entity
 * Date:2018年8月25日上午8:31:43
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:Actions <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月25日 上午8:31:43 <br/>
 * @author   hewanwan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Setter
@Getter
@Slf4j
public class Actions
{
	
	private Integer id;
	/**行为类型id */
	@NotNull
	private Integer actionTypeId;
	/**行为参数 */
	@NotNull
	private JSONObject actionParam;
	
	/**新增加字段*/
	@JSONField(serialize =false)
	private String actionParamString;
	@NotNull
	private Integer ruleId;
	
	private String creator;

	private String modifier;

	private Date modifyTime;

	private Date createTime;
	
	private String name;
	
	public void setActionParamString(String actionParamString)
	{
		this.actionParamString=actionParamString;
		try
		{
			if(actionParamString !=null && !StringUtils.isEmpty(actionParamString))
			{
				JSONObject list=JSONObject.parseObject(actionParamString);
				setActionParam(list);
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}
}

