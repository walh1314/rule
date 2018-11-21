package com.foxconn.core.pro.server.rule.engine.front.common.entity;

import java.util.List;

import com.github.pagehelper.PageInfo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageResult<T>
{
	private List<T> rows;
	// 当前页
	private Integer curretPage;
	// 每页数量
	private Integer pageSize;
	// 总条数
	private Long total;
	// 总页数
	private Integer pages;

	public PageResult(PageInfo<T> pageInfo)
	{
		this.rows = pageInfo.getList();
		this.total = pageInfo.getTotal();
		this.pageSize = pageInfo.getPageSize();
		this.pages = pageInfo.getPages();
		this.curretPage = pageInfo.getPageNum();
	}

	public PageResult(List<T> t)
	{
		this.rows = t;
	}

	public PageResult()
	{

	}

}
