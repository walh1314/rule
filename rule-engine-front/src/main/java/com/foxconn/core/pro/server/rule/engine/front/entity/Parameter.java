/**
 * Project Name:rule-engine-front
 * File Name:Parameter.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.entity
 * Date:2018年10月23日上午11:17:49
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:Parameter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月23日 上午11:17:49 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Setter
@Getter
public class Parameter implements Serializable
{
	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 693473909734314613L;
	private String types = "java.lang.String";
	private Integer num = -1;
	private String values;
}
