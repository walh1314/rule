/**
 * Project Name:rule-engine-action
 * File Name:VelocityUtil.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.velocity
 * Date:2018年9月7日下午2:32:11
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.velocity;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

/**
 * ClassName:VelocityUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月7日 下午2:32:11 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
public class VelocityUtil
{

	private static Properties props = new Properties();
	private static final VelocityEngine engine;
	static
	{
		props.setProperty(Velocity.INPUT_ENCODING, StandardCharsets.UTF_8.name());
		props.setProperty(Velocity.RESOURCE_LOADER, CommonConstant.CLA_SS);
		props.setProperty(CommonConstant.CLASS_RESOURCE_LOADER_CLASS,
				CommonConstant.LOADER_CLASSPATHRESOURCELOADER);
		engine = new VelocityEngine(props);
	}

	/**
	 * 测试模板静态方法使用
	 */
	public static synchronized String commonsString(JSONObject json, String template)
	{
		// 取得velocity的上下文context
		VelocityContext context = new VelocityContext();
		// 把数据填入上下文
		if (json != null)
		{
			context.put(CommonConstant.R_OOT, json);
			for (Entry<String, Object> entry : json.entrySet())
			{
				context.put(entry.getKey(), entry.getValue());
			}
			StringWriter writer = new StringWriter();
			engine.evaluate(context, writer, CommonConstant.NU_LL, template);
			return writer.toString();
		}
		return null;
	}

}
