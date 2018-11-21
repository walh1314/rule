/**
 * Project Name:rule-engine-core
 * File Name:ConfigBean.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity
 * Date:2018年9月12日上午10:59:16
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:ConfigBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年9月12日 上午10:59:16 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Setter
@Getter
public class ConfigBean  implements Serializable
{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -5212386810709936127L;
	private String userId;
}

