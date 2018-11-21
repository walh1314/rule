/**
 * Project Name:rule-engine-front
 * File Name:CodeUtil.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.common.util
 * Date:2018年9月3日上午8:19:56
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:CodeUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月3日 上午8:19:56 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Slf4j
public class CodeUtil
{
	/** 用户id和随机数总长度 */
	private static final int maxLength = 7;

	/** 随即编码 */
	private static final int[] code = new int[]
	{ 7, 9, 6, 2, 8, 1, 3, 0, 5, 4 };

	/**
	 * 更具id进行加密+加随机数组成固定长度编码
	 */
	private static String toCode(Long id)
	{
		String idStr = id.toString();
		StringBuilder idsbs = new StringBuilder();
		for (int i = idStr.length() - 1; i >= 0; i--)
		{
			idsbs.append(code[idStr.charAt(i) - '0']);
		}
		return idsbs.append(getRandom(maxLength - idStr.length())).toString();
	}

	/**
	 * 生成时间戳
	 */
	private static String getDateTime()
	{
		DateFormat sdf = new SimpleDateFormat(CommonConstant.YYYY_MM_DD);
		return sdf.format(new Date());
	}

	/**
	 * 生成固定长度随机码
	 * 
	 * @param len
	 *            长度
	 */
	private static long getRandom(long len)
	{
		long min = 1, max = 9;
		for (int i = 1; i < len; i++)
		{
			min *= 10;
			max *= 10;
		}
		long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min;
		return rangeLong;
	}

	/**
	 * 生成不带类别标头的编码
	 * 
	 * @param id
	 */
	public static synchronized String getCode()
	{
		int count = 0;
		String result = "";
		try
		{
			Random random = new Random();
			for (int i = 0; i < 100; i++)
			{
				count = random.nextInt(51) + 10;
			}
			result = getDateTime() + toCode(Long.valueOf(count));
		} catch (Exception e)
		{
			log.error("create code exception", e);
			throw e;
		}
		return result;
	}
}
