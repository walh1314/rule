package com.foxconn.core.pro.server.rule.engine.action;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.tsf.route.annotation.EnableTsfRoute;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.tsf.annotation.EnableTsfMetrics;
import org.springframework.tsf.auth.annotation.EnableTsfAuth;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient // 使用服务注册发现时请启用
@EnableFeignClients // 使用Feign微服务调用时请启用
@EnableScheduling
@EnableTsfAuth
@EnableTsfRoute
@EnableTsfMetrics
public class RuleEngineActionApplication
{
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	@LoadBalanced
	@Bean
	public AsyncRestTemplate asyncRestTemplate()
	{
		return new AsyncRestTemplate();
	}

	public static void main(String[] args) throws InterruptedException
	{
		SpringApplication.run(RuleEngineActionApplication.class, args);
	}
	
}
