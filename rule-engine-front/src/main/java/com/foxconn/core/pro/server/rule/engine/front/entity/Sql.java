/**
 * Project Name:rule-engine-front
 * File Name:Sql.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.entity
 * Date:2018年8月25日上午8:30:01
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.entity;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:Sql <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月25日 上午8:30:01 <br/>
 * @author   hewanwan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Setter
@Getter
public class Sql
{
	/**需要传输的数据字段 */
	@NotNull
	private String fields;
	/**需要处理的topic可以使用+或者#，#只能放最后 */
	@NotNull
	private String topic;
	/**行为触发条件，例如 count <= 5; */
	private String condition;

}

