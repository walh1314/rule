/**
 * Project Name:rule-engine-action
 * File Name:VeloctityTest.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.test
 * Date:2018年9月7日下午2:20:22
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.test;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * ClassName:VeloctityTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月7日 下午2:20:22 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class VeloctityTest
{

	public static void main(String[] args)
	{
		// 初始化模板引擎
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("directive.set.null.allowed ",true);
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		// 获取模板文件
		Template t = ve.getTemplate("hellovelocity.vm");
		// 设置变量
		VelocityContext ctx = new VelocityContext();
		ctx.put("name", "Velocity");
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		ctx.put("list", list);
		// 输出
		StringWriter sw = new StringWriter();
		t.merge(ctx, sw);
		System.out.println(sw.toString());
		
		testStringVelocity();
	}

	/**
	 * 测试字符串模板替换
	 */
	private static void testStringVelocity()
	{
		// 初始化并取得Velocity引擎
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		// 字符串模版
		String template = "$!{ppp}：您的${type} : ${bill} 在  ${date} 日已支付成功";
		// 取得velocity的上下文context
		VelocityContext context = new VelocityContext();
		// 把数据填入上下文
		context.put("ppp____", "99999999");
		context.put("bill", "201203221000029763");
		context.put("type", "订单");
		context.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		StringWriter writer = new StringWriter();
		engine.evaluate(context, writer, "", template);
		//engine.e
		System.out.println(writer.toString());

	}
}
