package com.foxconn.core.pro.server.rule.engine.core.common.exception;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;

/**
 * 
 * @author liupingan
 *
 */
public class BaseException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7882913178538047286L;
	private String code;
	private String msg;

	public BaseException(String code, String msg, Throwable t)
	{
		super(CommonConstant.ERROR_CODE + code + CommonConstant.ERROR_MSG + msg, t);
		this.code = code;
		this.msg = msg;
	}

	public BaseException(String code, String msg)
	{
		super(CommonConstant.ERROR_CODE + code + CommonConstant.ERROR_MSG + msg, null);
		this.code = code;
		this.msg = msg;
	}

	public BaseException(ErrorCodes error, Throwable t)
	{
		super(CommonConstant.ERROR_CODE + error.getCode() + CommonConstant.ERROR_MSG + error.getDesc(), t);
		this.code = error.getCode();
		this.msg = error.getDesc();
	}

	public BaseException(ErrorCodes error)
	{
		super(CommonConstant.ERROR_CODE + error.getCode() + CommonConstant.ERROR_MSG + error.getDesc());
		this.code = error.getCode();
		this.msg = error.getDesc();
	}

	public BaseException(String msg, Throwable t)
	{
		super(CommonConstant.ERRORMSG + msg, t);
	}

	public BaseException(String msg)
	{
		super(CommonConstant.ERRORMSG + msg);
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

}
