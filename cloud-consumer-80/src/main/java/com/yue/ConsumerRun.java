package com.yue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ConsumerRun {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerRun.class,args);
    }
}
