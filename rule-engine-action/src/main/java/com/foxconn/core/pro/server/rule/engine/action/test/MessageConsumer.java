/**
 * Project Name:rule-engine-action
 * File Name:MessageConsumer.java
 * Package Name:com.foxconn.core.pro.server.rule.engine.action.test
 * Date:2018年8月29日上午11:51:16
 * Copyright (c) 2018, Foxconn All Rights Reserved.
 *
*/

package com.foxconn.core.pro.server.rule.engine.action.test;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;

/**
 * ClassName:MessageConsumer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月29日 上午11:51:16 <br/>
 * @author   liupingan
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class MessageConsumer {

    public static void main(String[] arg) {
    	Properties props = new Properties();
    	//211.159.183.125:9092, 139.199.77.21:9092, 118.89.37.185:9092
    	//props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.233:9092");
    	props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "211.159.183.125:9092, 139.199.77.21:9092, 118.89.37.185:9092");
    	props.put("group.id", "test01");
    	//props.put("client.id", "test");
    	//props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "3");
    	props.put("enable.auto.commit", true);// 显示设置偏移量自动提交
    	props.put("auto.commit.interval.ms", 1000);// 设置偏移量提交时间间隔
    	props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
    	props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
    	 
    	KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);// 创建消费者
    	//6443304708360043449_event_warning
    	//consumer.subscribe(Arrays.asList("sys.corepro.action"));// 订阅主题
    	//test_123
    	//consumer.subscribe(Arrays.asList("6443304708360043449_event_warning"));// 订阅主题
    	consumer.subscribe(Arrays.asList("_data_6448400350861954150_datakeydadaf610bd7d11e8bc4e0a58c0a80385"));// 订阅主题
    	//
    	try {
    	    int minCommitSize = 10;// 最少处理10 条消息后才进行提交
    	    int icount = 0 ;// 消息计算器
    	    while (true) {
    	    	Thread.sleep(5000);
    	        // 等待拉取消息
    	        ConsumerRecords<String, String> records = consumer.poll(1000);
    	        System.out.println("====================================");
    	        for (ConsumerRecord<String, String> record : records) {
    	            // 简单打印出消息内容,模拟业务处理
    	            System.out.printf("partition = %d, offset = %d,key= %s value = %s%n", record. partition(), record.offset(), record.key(),record.value());
    	            icount++;
    	        }
    	        // 在业务逻辑处理成功后提交偏移量
    	        if (icount >= minCommitSize){
    	            consumer.commitAsync(new OffsetCommitCallback() {
    	                @Override
    	                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
    	                    if (null == exception) {
    	                    // TODO 表示偏移量成功提交
    	                    System.out.println("提交成功");
    	                    } else {
    	                        // TODO 表示提交偏移量发生了异常，根据业务进行相关处理
    	                        System.out.println("发生了异常");
    	                    }
    	                }
    	            });
    	            icount=0; // 重置计数器
    	        }
    	    }
    	} catch(Exception e){
    	    // TODO 异常处理
    	    e.printStackTrace();
    	} finally {
    	    consumer.close();

    	}

    }

}
