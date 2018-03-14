package org.skywalking.springcloud.test.eureka.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaService_Main {

    public static void main(String[] args) {
        SpringApplication.run(EurekaService_Main.class, args);
    }
}
