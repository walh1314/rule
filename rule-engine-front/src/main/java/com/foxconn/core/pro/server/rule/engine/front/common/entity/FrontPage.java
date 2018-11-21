package com.foxconn.core.pro.server.rule.engine.front.common.entity;

public class FrontPage<T>
{
	private Integer currentPage;
	private Integer pageSize;

	public Integer getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage)
	{
		this.currentPage = currentPage;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}
}
