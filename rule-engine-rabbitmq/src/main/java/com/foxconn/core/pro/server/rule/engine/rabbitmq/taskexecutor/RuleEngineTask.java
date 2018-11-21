package com.foxconn.core.pro.server.rule.engine.rabbitmq.taskexecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.foxconn.core.pro.server.rule.engine.core.entity.data.MqttData;
import com.foxconn.core.pro.server.rule.engine.rabbitmq.constant.CommonConstant;
import com.foxconn.core.pro.server.rule.engine.rabbitmq.service.IRuleService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Service
@Scope(CommonConstant.PROTOTYPE)
@Slf4j
public class RuleEngineTask implements Runnable
{

	private MqttData data;

	@Autowired
	private IRuleService ruleService;

	@Override
	public void run()
	{
		try
		{
			ruleService.excute(data);
		} catch (Exception e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
	}
	

}
