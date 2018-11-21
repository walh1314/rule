/**
 * 
 */
package com.foxconn.core.pro.server.rule.engine.kafka.consumer;

import java.util.HashMap;
import java.util.Map;
import com.foxconn.core.pro.server.rule.engine.kafka.constant.CommonConstant;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;


import lombok.extern.slf4j.Slf4j;

/**
 * @author lxy
 *
 */
@Configuration
@EnableKafka
@Slf4j
public class KafkaConsumerConfig {
	
	@Value(CommonConstant.KAFKA_CONSUMER_SERVERS)
    private String servers;
	
	@Value(CommonConstant.KAFKA_CONSUMER_ENABLE_AUTO_COMMIT)
    private boolean enableAutoCommit;
	
    @Value(CommonConstant.KAFKA_CONSUMER_SESSION_TIMEOUT_MS)
    private String sessionTimeout;
    
    
    @Value(CommonConstant.KAFKA_CONSUMER_AUTO_COMMIT_INTERVAL)
    private String autoCommitInterval;
    
    @Value(CommonConstant.KAFKA_CONSUMER_GROUP_ID)
    private String groupId;
    
    @Value(CommonConstant.KAFKA_CONSUMER_AUTO_OFFSET_RESET)
    private String autoOffsetReset;
    
    @Value(CommonConstant.KAFKA_CONSUMER_CONCURRENCY)
    private int concurrency;
    
    @Value(CommonConstant.KAFKA_CONSUMER_PROPERTIES_SECURITY_PROTOCOL)
    private String securityProtocl;
    
    @Value(CommonConstant.KAFKA_CONSUMER_PROPERTIES_SASL_MECHANISM)
    private String mechanism;
    
    @Value(CommonConstant.KAFKA_CONSUMER_MAX_POLL_RECORDS)
    private int maxRecord;
    
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(this.concurrency);
        factory.getContainerProperties().setPollTimeout(1500);
        return factory;
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.servers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, this.autoCommitInterval);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, this.sessionTimeout);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.autoOffsetReset);
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxRecord);
        
        // 下面两行是 安全认证的配置
        propsMap.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.securityProtocl);
        propsMap.put(SaslConfigs.SASL_MECHANISM, this.mechanism);
        return propsMap;
    }

    @Bean
    public KafkaTopicConsumer listener(){
    	log.info("kafka consumer create successfully!");
        return new KafkaTopicConsumer();
    }
    
}
