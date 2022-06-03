package com.pilipili;


import com.pilipili.app.service.websocket.WebSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableFeignClients(basePackages = "com.pilipili.app.service.feign")
@EnableHystrix
public class Application {
    public static void main(String[] args){
        ApplicationContext app = SpringApplication.run(Application.class, args);
        WebSocketService.setApplicationContext(app);
    }
}
