package com.foxconn.core.pro.server.rule.engine.front.common.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.foxconn.core.pro.server.rule.engine.front.exception.ErrorCodes;

/**
 * 序列化类
 * @author liupingan
 * @param <T>
 */
public class ResultMap<T> implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6298254734180792093L;

	private T data;
	private String code;
	private String msg;
	private Integer status;
	
	@JSONField(serialize = false)
	private String[] args;

	public ResultMap()
	{
		this(ErrorCodes.SCUUESS.getCode(), ErrorCodes.SCUUESS.getDesc(),ErrorCodes.SCUUESS.getStatus());
	}

	public ResultMap(String code)
	{
		this(code, null, null);
	}

	public ResultMap(String code, String msg, Integer status)
	{
		this.code = code;
		this.msg = msg;
		this.status =status;
	}

	public ResultMap(ErrorCodes error)
	{
		this.code = error.getCode();
		this.msg = error.getDesc();
		this.status = error.getStatus();
	}

	public T getData()
	{
		return data;
	}

	public void setData(T data)
	{
		this.data = data;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String[] getArgs()
	{
		return args;
	}

	public void setArgs(String[] args)
	{
		this.args = args;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}
}
