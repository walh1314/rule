/**
 * Project Name:rule-engine-front
 * File Name:ActionTypeServiceImpl.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.service.impl
 * Date:2018年8月31日下午4:23:51
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.esotericsoftware.minlog.Log;
import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.dto.ActionTypeDto;
import com.foxconn.core.pro.server.rule.engine.front.entity.ActionType;
import com.foxconn.core.pro.server.rule.engine.front.exception.BaseException;
import com.foxconn.core.pro.server.rule.engine.front.mapper.ActionTypeMapper;
import com.foxconn.core.pro.server.rule.engine.front.service.ActionTypeService;

/**
 * ClassName:ActionTypeServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月31日 下午4:23:51 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor =
{ Exception.class, BaseException.class, RuntimeException.class, Throwable.class })
public class ActionTypeServiceImpl implements ActionTypeService
{

	@Autowired
	private ActionTypeMapper actionTypeMapper;

	@Override
	public ActionType selectById(Integer id)
	{
		if(id == null){
			return null;
		}
		return  actionTypeMapper.selectById(id);
	}

	@Override
	public ResultMap<? extends Object> selectByMaps(ActionTypeDto bean)
	{
		ResultMap<List<ActionType>> result = new ResultMap<>();
		Map<String, Object> queryMap = new HashMap<>();
		try
		{
			BeanUtilsBean2.getInstance().populate(bean, queryMap);
		} catch (IllegalAccessException e)
		{
			Log.error(e.getLocalizedMessage());
		} catch (InvocationTargetException e)
		{
			Log.error(e.getLocalizedMessage());
		}
		List<ActionType> beans = actionTypeMapper.selectByMap(queryMap);
		result.setData(beans);
		return result;
	}

}
