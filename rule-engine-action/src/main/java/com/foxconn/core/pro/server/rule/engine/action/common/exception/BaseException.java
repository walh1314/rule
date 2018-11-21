package com.foxconn.core.pro.server.rule.engine.action.common.exception;

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
		super("ErrorCode: " + code + ",ErrorMsg:\r\n" + msg, t);
		this.code = code;
		this.msg = msg;
	}

	public BaseException(String code, String msg)
	{
		super("ErrorCode: " + code + ",ErrorMsg:\r\n" + msg, null);
		this.code = code;
		this.msg = msg;
	}

	public BaseException(ErrorCodes error, Throwable t)
	{
		super("ErrorCode: " + error.getCode() + ",ErrorMsg:\r\n" + error.getDesc(), t);
		this.code = error.getCode();
		this.msg = error.getDesc();
	}

	public BaseException(ErrorCodes error)
	{
		super("ErrorCode: " + error.getCode() + ",ErrorMsg:\r\n" + error.getDesc());
		this.code = error.getCode();
		this.msg = error.getDesc();
	}

	public BaseException(String msg, Throwable t)
	{
		super("ErrorMsg:\r\n" + msg, t);
	}

	public BaseException(String msg)
	{
		super("ErrorMsg:\r\n" + msg);
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
