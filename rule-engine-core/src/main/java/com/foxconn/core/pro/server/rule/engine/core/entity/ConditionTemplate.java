/**
 * Project Name:rule-engine-core
 * File Name:ConditionTemplate.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.entity
 * Date:2018年8月24日上午8:24:52
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.core.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foxconn.core.pro.server.rule.engine.core.config.template.RuleTemplateProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:ConditionTemplate <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月24日 上午8:24:52 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Slf4j
@Component
public class ConditionTemplate extends RuleTemplate
{
	@Autowired
	private RuleTemplateProperties ruleTemplateProperties;

	public void init()
	{
		try
		{
			FileInputStream fileInput = new FileInputStream(ruleTemplateProperties.getField().getHeaderFile());
			headers = new String(IOUtils.toCharArray(fileInput, StandardCharsets.UTF_8));
		} catch (FileNotFoundException e)
		{
			log.error(e.getLocalizedMessage(), e);

		} catch (IOException e)
		{
			log.error(e.getLocalizedMessage(), e);

		}
	}
}
