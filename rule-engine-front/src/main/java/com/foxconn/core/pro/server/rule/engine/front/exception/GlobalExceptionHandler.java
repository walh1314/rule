/**
 * Project Name:rule-engine-front
 * File Name:GlobalExceptionHandler.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.exception
 * Date:2018年9月5日上午8:22:42
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.foxconn.core.pro.server.rule.engine.front.common.entity.ResultMap;
import com.foxconn.core.pro.server.rule.engine.front.controller.base.BaseMessageResource;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:GlobalExceptionHandler全局异常处理类 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年9月5日 上午8:22:42 <br/>
 * 通过使用@ControllerAdvice定义统一的异常处理类，可以不用在每个Controller中逐个定义异常处理方式
 * 
 * @ExceptionHandler 用来定义函数针对的异常类型，controller通过抛出的异常类型匹配 最后将Exception对象和请求URL映射到
 *                   resource/templates/error.html中
 * @author hewanwan
 * @version
 * @since JDK 1.8
 * @see
 */

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler
{
	@Autowired
	private BaseMessageResource baseMessageResource;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	// @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResultMap<? extends Object> handleIllegalParamException(MethodArgumentNotValidException e)
	{
		List<ObjectError> errors = e.getBindingResult().getAllErrors();
		String tips = "参数不合法";
		if (errors.size() > 0)
		{
			tips = errors.get(0).getDefaultMessage();
		}
		ResultMap<? extends Object> result = new ResultMap<>(ErrorCodes.FAILED);
		result.setMsg(tips);
		log.error(e.getMessage());
		return baseMessageResource.getMessage(result);
	}

	/** * 默认异常处理方法,返回异常请求路径和异常信息 */
	@ExceptionHandler(value = Exception.class)
	public ResultMap<? extends Object> defaulErrorHandler(HttpServletRequest request, Exception e) throws Exception
	{	

		ResultMap<? extends Object> error = new ResultMap<>(ErrorCodes.FAILED);
		log.error(e.getMessage());
		return baseMessageResource.getMessage(error);
	}

	/**
	 * @ExceptionHandler 匹配抛出的异常类型 ErrorInfo<String> 为自定义的一个数据封装类，用于返回的json数据
	 *                   如果返回的是json格式，需要加上@ResponsBody
	 */
	@ExceptionHandler(value = BaseException.class)
	@ResponseBody
	public ResultMap<? extends Object> jsonErrorHandler(HttpServletRequest request, BaseException e) throws Exception
	{

		ResultMap<? extends Object> error = new ResultMap<>();
		error.setCode(e.getCode());
		error.setMsg(e.getMsg());
		error.setStatus(e.getStatus());
		log.error(e.getMessage());
		return baseMessageResource.getMessage(error);
	}

}
