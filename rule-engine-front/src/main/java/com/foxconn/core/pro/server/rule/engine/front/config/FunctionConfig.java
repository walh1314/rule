/**
 * Project Name:rule-engine-front
 * File Name:FunctionConfig.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.front.config
 * Date:2018年10月22日上午11:20:08
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.front.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import com.foxconn.core.pro.server.rule.engine.front.common.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.front.entity.FunctionValidateBean;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:FunctionConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年10月22日 上午11:20:08 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Component(CommonConstant.FUNCTION_CONFIG)
@RefreshScope
@ConfigurationProperties(prefix = CommonConstant.COM_SQL_FUNCTION)
@Setter
@Getter
public class FunctionConfig
{
    private Map<String,FunctionValidateBean> maps;
    //函数名,验证类，[参数类型，个数,参数类型,个数]
}

