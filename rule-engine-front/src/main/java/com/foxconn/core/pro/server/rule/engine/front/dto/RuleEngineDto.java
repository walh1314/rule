/**
 * Project Name:rule-engine-front
 * File Name:RuleEngineDto.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.dto
 * Date:2018年8月28日下午1:41:45
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.dto;

import java.util.List;


import com.foxconn.core.pro.server.rule.engine.front.entity.Sql;
import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:RuleEngineDto <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月28日 下午1:41:45 <br/>
 * @author   hewanwan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Setter
@Getter
public class RuleEngineDto
{

	private Integer id;
	
	private String name;
	/**描述 */
	private String desc;
	/**默认为1，json格式 */
	private Long dataType;
	/**SQL语句 */
	private Sql sql;
	/**行为*/
	private List<ActionsDto> actions;
	
	/**版本 0  基础版本,1 加强版**/
	private Integer version;
	
	/**接入方式 0 topic,1 http**/
	private Integer accessMode;
	
	private Integer status;
	private String code;
	
	private Integer currentPage;
	private Integer pageSize;
}

