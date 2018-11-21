/**
 * 
 */
package com.foxconn.core.pro.server.rule.engine.kafka.consumer;

import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.MqttData;
import com.foxconn.core.pro.server.rule.engine.core.entity.data.PayloadBean;
import com.foxconn.core.pro.server.rule.engine.kafka.entity.CommonDataVO;
import com.foxconn.core.pro.server.rule.engine.kafka.service.IInvokeRule;
import com.foxconn.core.pro.server.rule.engine.kafka.util.SHA256Util;
import com.foxconn.core.pro.server.rule.engine.kafka.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * kafka topic 消费类
 * @author lxy
 *
 */
@Component
@Slf4j
public class KafkaTopicConsumer {
	
	@Autowired
	private IInvokeRule invokeRule;

	/**
	 * 消费 _data开头的 topic
	 * @param record
	 */
	@KafkaListener(topicPattern = CommonConstant.KAFKA_CONSUMER_BASE_TOPIC)
	public void listen(ConsumerRecord<?, ?> record) {
		log.info("topic = " + record.topic() + " ,key = " + record.key() + " ,value = " + record.value());
		String topic = record.topic();
		// 处理基础版的 topic
		Object objVal = record.value();
		if(null == objVal) {
			log.warn(topic + " value is null");
			return ;
		}
		String value = String.valueOf(objVal);
		CommonDataVO commonData = JSONObject.parseObject(value, CommonDataVO.class);
		
		// 验签 ， 以后改成调用验签 的接口 
		Map<String , String> sysPara = commonData.getSystemParams();
		String origin = CommonConstant.APPID + sysPara.get(CommonConstant.APPID) + CommonConstant.TIMES_TAMP + sysPara.get(CommonConstant.TIMES_TAMP) + CommonConstant.TYPE + sysPara.get(CommonConstant.TYPE) ;
		try {
			String encrypt = SHA256Util.encryptSHA(origin);
			// 与 redis 缓存的 安全密钥进行对比
			/*String token = sysPara.get("token");
			if(!token.equals(encrypt)) {
				log.warn("data is not correct!");
				return ;
			}
*/		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Map<String , Object>> appData = commonData.getAppParams();
		if(null == appData || appData.isEmpty()) {
			log.warn(topic +  " app_params is null");
			return ;
		}   
		 
		String productKey = topic.substring(topic.indexOf(CommonConstant.STRIKE_THROUGH,   1) + 1, topic.lastIndexOf(CommonConstant.STRIKE_THROUGH));
		String dataId = productKey + CommonConstant.SYMBOL_TWO + topic.substring(topic.lastIndexOf(CommonConstant.STRIKE_THROUGH) + 1);
		// 转换 topic 
		String ruleTopic = topic.replaceAll(CommonConstant.STRIKE_THROUGH, CommonConstant.LEFT_UNDERLINE);
		for(Map<String , Object> temMap : appData) {
			MqttData data = new MqttData();
			data.setAccessMode(0);
			data.setDataId(dataId);
			data.setId(sysPara.get(CommonConstant.ID));     // 取外层 id
			PayloadBean payload = new PayloadBean();
			payload.setDeviceName(String.valueOf(temMap.get(CommonConstant.DEVICE_NAME)));    // 取数据中的 deviceName
			JSON params = new JSONObject(temMap);
			payload.setParams(params);
			// 从 topic 中取productKey
			payload.setProductkey(productKey);  //   可以从 topic 中获取
			payload.setTimestamp(System.currentTimeMillis());
			data.setPayload(payload);
			data.setTimestamp(System.currentTimeMillis());
			data.setTopic(ruleTopic);
			data.setVersion(0);
			
			log.info("data:" + JSONObject.toJSONString(data));
			// 调用规则 ， 执行 action
			invokeRule.invoke(data);
		}
	}
	
}
