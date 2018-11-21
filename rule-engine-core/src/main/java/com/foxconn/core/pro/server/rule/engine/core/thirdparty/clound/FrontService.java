package com.foxconn.core.pro.server.rule.engine.core.thirdparty.clound;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.core.thirdparty.entity.NoticeRedisBean;

@FeignClient(name = CommonConstant.COM_SERVER_FRONT, url = CommonConstant.COM_SERVER_FRONT_URL, configuration = FeignConfiguration.class)
public interface FrontService
{
	@RequestMapping(value = CommonConstant.RULEENGINE_NOTICE_REDIS, method = RequestMethod.POST)
	JSONObject noticeRedis(NoticeRedisBean bean);
}
