/**
 * Project Name:rule-engine-front
 * File Name:FunctionValidateBean.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.entity
 * Date:2018年10月23日上午11:01:23
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.entity;
/**
 * ClassName:FunctionValidateBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年10月23日 上午11:01:23 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FunctionValidateBean implements Serializable
{
	/**
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -949681220632251778L;
	private String cls = "com.foxconn.core.pro.server.rule.engine.front.validate.FunctionValidate";
	private List<Parameter> params = new ArrayList<>();

}
