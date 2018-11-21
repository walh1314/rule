package com.foxconn.core.pro.server.rule.engine.action.thirdparty.clound;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

//@FeignClient(name = "${com.server.topic}")
public interface TopicService
{
	//@RequestMapping(value = "/getTopic/{dataId}", method = RequestMethod.GET)
	String getTopic(@PathVariable(CommonConstant.DATAID) String dataId);
}