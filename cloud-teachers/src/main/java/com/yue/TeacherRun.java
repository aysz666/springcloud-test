package com.yue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TeacherRun {
    public static void main(String[] args) {
        SpringApplication.run(TeacherRun.class,args);
    }
}
