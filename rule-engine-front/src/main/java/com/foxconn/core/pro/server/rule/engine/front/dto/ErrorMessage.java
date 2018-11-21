/**
 * Project Name:rule-engine-front
 * File Name:ErrorMessage.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.dto
 * Date:2018年9月19日上午10:36:29
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:ErrorMessage <br/>
 * Function: TODO ADD FUNCTION. 抛出错误信息 <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月19日 上午10:36:29 <br/>
 * 
 * @author hewanwan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class ErrorMessage
{
	/** 开始位置 */
	private Integer startPosition;
	/** 结束位置 */
	private Integer endPosition;
	/** 错误字段 */
	private String wrongField;
	/** 截取剩余字段 */
	private String otherField;
	
	private String type;

	public void setErrorMessage(Integer startPosition, Integer endPosition, String wrongField, String type)
	{
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.wrongField = wrongField;
		this.type = type;
	}

	public void setErrorMessage(Integer startPosition, Integer endPosition, String wrongField, String otherField, String type)
	{
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.wrongField = wrongField;
		this.otherField = otherField;
		this.type = type;
	}

}
