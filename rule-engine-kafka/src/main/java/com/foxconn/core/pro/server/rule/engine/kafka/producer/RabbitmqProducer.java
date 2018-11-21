/**
 * Project Name:rule-engine-core
 * File Name:RabbitmqProducer.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.core.rabbit
 * Date:2018年8月30日上午8:53:18
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.kafka.producer;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxconn.core.pro.server.rule.engine.kafka.constant.CommonConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:RabbitmqProducer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2018年8月30日 上午8:53:18 <br/>
 * 
 * @author liupingan
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Slf4j
public class RabbitmqProducer
{
	@Resource(name = CommonConstant.ACTION_RABBIT_TEMPLATE)
	private AmqpTemplate actionRabbitTemplate;

	@Autowired
	private RabbitmqConfigProperties rabbitmqConfigProperties;

	/**
	 * 发布消息 publish:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 *
	 * @author liupingan
	 * @param message
	 * @since JDK 1.8
	 */
	public void publish(String message)
	{
		log.info("------------RabbitmqProducer to Action start -----"+message);
		this.actionRabbitTemplate.convertAndSend(rabbitmqConfigProperties.getAction().getExchange(),
				rabbitmqConfigProperties.getAction().getTopic(), message);
		log.info("------------RabbitmqProducer to Action end-----");
	}
}
