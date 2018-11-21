/**
 * Project Name:rule-engine-front
 * File Name:InsertRule.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.entity
 * Date:2018年8月24日上午10:55:38
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:InsertRule  插入功能类 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月24日 上午10:55:38 <br/>
 * @author   hewanwan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Setter
@Getter
@Slf4j
public class RuleEngine 
{
	@NotNull
	private Integer id;
	/**名称 */
	@NotNull
	private String name;
	/**描述 */
	private String desc;
	/**默认为1，json格式 */
	@NotNull
	private Long dataType= 1L;
	
	/**版本 0  基础版本,1 加强版**/
	private Integer version;
	
	/**接入方式 0 topic,1 http**/
	private Integer accessMode;
	
	/**SQL语句 */
	@NotNull
	private Sql sql;
	/**新添加的字段*/
	@JSONField(serialize = false)
	private String sqlString;
	
	private List<Actions> actions;
	@NotNull
	private Integer status;
	
	private Date createTime;

	private String creator;
	
	private Date modifyTime;

	private String modifier;
	@NotNull
	private String code;

	public void setSqlString(String sqlString) 
	{
		this.sqlString = sqlString;
		 try 
		 {  
	            if(sqlString != null && !StringUtils.isEmpty(sqlString))
	            {
	            	Sql list = JSONObject.parseObject(sqlString,Sql.class);
	            	//调用setStar方法  
	                setSql(list);
	            }
	        } catch (Exception e)
		 	{  
	           log.error(e.getMessage());
	        } 
	}
	
	
		/**行为*/
	/*	private List<Actions> actions;
	
	@JSONField(serialize = false)
	private String actionString;
	
	public void setActionString(String actionString)
	{
		this.actionString = actionString;
		try
		{
			if (actionString != null && !StringUtils.isEmpty(actionString))
			{
				List<Actions> list = JSONArray.parseArray(actionString, Actions.class);
				// 调用setStar方法
				
				setActions(list);
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}*/

}