/**
 * 
 */
package com.foxconn.core.pro.server.rule.engine.kafka.service;

import com.foxconn.core.pro.server.rule.engine.core.entity.data.MqttData;

/**
 * @author lxy
 *
 */
public interface IInvokeRule {

	public void invoke(MqttData data);
	
}
