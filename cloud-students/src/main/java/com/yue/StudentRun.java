package com.yue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StudentRun {
    public static void main(String[] args) {
        SpringApplication.run(StudentRun.class,args);
    }
}
