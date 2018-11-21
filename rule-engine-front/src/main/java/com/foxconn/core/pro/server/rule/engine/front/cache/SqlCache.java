/**
 * Project Name:rule-engine-front
 * File Name:SqlCache.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.cache
 * Date:2018年10月5日下午4:07:30
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.cache;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:SqlCache <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年10月5日 下午4:07:30 <br/>
 * 
 * @author hewanwan
 * @version
 * @since JDK 1.8
 * @see
 */
@Slf4j
public class SqlCache
{
	private static Map<String, String> keyWordMap = new HashMap<>(700);
	private static String read_keyWord = "keyWord.txt";
	static
	{
		readTxt();
	}

	public static void readTxt()
	{

		try
		{
			InputStream inpuStream = SqlCache.class.getClassLoader().getResourceAsStream(read_keyWord);
		
			if (inpuStream != null)
			{
				InputStreamReader isr = new InputStreamReader(inpuStream, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr);
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null)
				{
					log.info("......................" + lineTxt);
					if (!"".equals(lineTxt))
					{
						String reds = lineTxt.split(CommonConstant.DOUBLE_SLASH_ADD)[0].toLowerCase();
						keyWordMap.put(reds, reds);
					}
				}
				isr.close();
				br.close();
			}
		} catch (Exception e)
		{
			log.error("读取配置文件错误！", e);
		}
	}
	
	public static Map<String, String> getSqlKeys(){
		return keyWordMap;
	}
}
