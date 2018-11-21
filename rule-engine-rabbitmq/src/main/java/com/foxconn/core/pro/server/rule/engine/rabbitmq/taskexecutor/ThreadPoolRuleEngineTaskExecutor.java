package com.foxconn.core.pro.server.rule.engine.rabbitmq.taskexecutor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import com.foxconn.core.pro.server.rule.engine.rabbitmq.constant.CommonConstant;

@Component(CommonConstant.RULEENGINE_TASKEXECUTOR)
public class ThreadPoolRuleEngineTaskExecutor extends ThreadPoolTaskExecutor
{

	@Autowired
	private RuleEngineTaskExecutorConfig ruleEngineTaskExecutorConfig;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4226379536213261422L;

	public ThreadPoolRuleEngineTaskExecutor(){
		super();
	}
	
	/**
	 * 初始化方法注解
	 */
	@PostConstruct
	public void init(){
		this.setCorePoolSize(ruleEngineTaskExecutorConfig.getCorePoolSize());
		this.setKeepAliveSeconds(ruleEngineTaskExecutorConfig.getKeepAliveSeconds());
		this.setMaxPoolSize(ruleEngineTaskExecutorConfig.getMaxPoolSize());
		this.setQueueCapacity(ruleEngineTaskExecutorConfig.getQueueCapacity());
		this.initialize();
	}
}
