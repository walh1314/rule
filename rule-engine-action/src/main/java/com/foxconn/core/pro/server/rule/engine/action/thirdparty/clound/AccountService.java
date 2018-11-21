package com.foxconn.core.pro.server.rule.engine.action.thirdparty.clound;

import com.alibaba.fastjson.JSONObject;

//@FeignClient(name = "${com.server.account}")
public interface AccountService
{
	//@RequestMapping(value = "corepro-common/userid", method = RequestMethod.GET)
	JSONObject getUserInfo(String productKey);
}