/**
 * Project Name:rule-engine-action
 * File Name:SpringUtil.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.util
 * Date:2018年8月29日上午8:42:31
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ClassName:SpringUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月29日 上午8:42:31 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */

@Component
public class SpringUtil implements ApplicationContextAware
{

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		if (SpringUtil.applicationContext == null)
		{
			SpringUtil.applicationContext = applicationContext;
		}
		System.out.println("---------------------------------------------------------------------");

		System.out.println("========ApplicationContext配置成功,在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象,applicationContext=" + SpringUtil.applicationContext + "========");

		System.out.println("---------------------------------------------------------------------");
	}

	// 获取applicationContext
	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	// 通过name获取 Bean.
	public static Object getBean(String name)
	{
		return getApplicationContext().getBean(name);
	}

	// 通过class获取Bean.
	public static <T> T getBean(Class<T> clazz)
	{
		return getApplicationContext().getBean(clazz);
	}

	// 通过name,以及Clazz返回指定的Bean
	public static <T> T getBean(String name, Class<T> clazz)
	{
		return getApplicationContext().getBean(name, clazz);
	}

}
