package com.foxconn.core.pro.server.rule.engine.core.thirdparty.clound;

import org.springframework.web.bind.annotation.PathVariable;

import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;

//@FeignClient(name = "${com.server.topic}")
public interface TopicService
{
	//@RequestMapping(value = "/getTopic/{dataId}", method = RequestMethod.GET)
	String getTopic(@PathVariable(CommonConstant.DATA_ID) String dataId);
}