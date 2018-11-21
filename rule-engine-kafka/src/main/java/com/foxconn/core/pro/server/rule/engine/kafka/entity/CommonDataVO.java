/**
 * 
 */
package com.foxconn.core.pro.server.rule.engine.kafka.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.foxconn.core.pro.server.rule.engine.kafka.constant.CommonConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lxy
 *
 */
@Setter
@Getter
public class CommonDataVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5141894496011700042L;
	
	@JSONField(name = CommonConstant.APP_PARAMS)
	private List<Map<String , Object>> appParams = new ArrayList<Map<String , Object>>();
	
	@JSONField(name = CommonConstant.SYSTEM_PARAMS)
	private Map<String , String> systemParams = new HashMap<String , String>();
	
}
