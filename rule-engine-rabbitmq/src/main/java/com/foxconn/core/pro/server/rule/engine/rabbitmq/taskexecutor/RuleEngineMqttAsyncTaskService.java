package com.foxconn.core.pro.server.rule.engine.rabbitmq.taskexecutor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RuleEngineMqttAsyncTaskService
{

	@Async
    public void executeAysncTask(Integer i){
        System.out.println("执行异步任务："+i);
    }
}


