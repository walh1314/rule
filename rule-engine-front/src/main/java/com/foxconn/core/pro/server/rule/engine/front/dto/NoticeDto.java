/**
 * Project Name:rule-engine-front
 * File Name:NoticeDto.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.dto
 * Date:2018年9月11日下午4:17:22
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.dto;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:NoticeDto <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年9月11日 下午4:17:22 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Setter
@Getter
public class NoticeDto implements Serializable
{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -6497951500306246382L;

	@JSONField(name=CommonConstant.TENANT_ID)
	private Integer tenantId;
	
	@JSONField(name=CommonConstant.DATABASE_TYPE)
	private String databaseType;
	
	@JSONField(name=CommonConstant.DB_NAME)
	private String dbName;
	
	@JSONField(name=CommonConstant.DEVICE_OWNER)
	private String deviceOwner;

}

