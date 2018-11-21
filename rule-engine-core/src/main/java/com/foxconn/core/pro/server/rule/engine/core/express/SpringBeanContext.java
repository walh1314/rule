/**
 * Project Name:rule-engine-core
 * File Name:SpringBeanContext.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.express
 * Date:2018年8月23日下午4:07:35
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.express;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.ql.util.express.IExpressContext;

/**
 * ClassName:SpringBeanContext <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月23日 下午4:07:35 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class SpringBeanContext extends HashMap<String, Object> implements IExpressContext<String, Object>
{
	/**
	 * serialVersionUID:TODO(序列化id).
	 * 
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 9173001762829770141L;
	private ApplicationContext applicationContext;

	public SpringBeanContext(Map<String, Object> aProperties, ApplicationContext applicationContext)
	{
		super(aProperties);
		this.applicationContext = applicationContext;
	}

	/**
	 * 根据key从容器里面获取对象
	 *
	 * @param key
	 * @return
	 */
	public Object get(Object key)
	{
		Object object = super.get(key);
		try
		{
			if (object == null && this.applicationContext != null && this.applicationContext.containsBean((String) key))
			{
				object = this.applicationContext.getBean((String) key, JSONObject.class);
			}
		} catch (Exception e)
		{
			throw new RuntimeException(CommonConstant.EXP, e);
		}
		return object;
	}

	/**
	 * 把key-value放到容器里面去
	 *
	 * @param key
	 * @param value
	 */
	public Object put(String key, Object value)
	{
		return super.put(key, value);
	}
}
