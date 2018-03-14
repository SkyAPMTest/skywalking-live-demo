package org.skywalking.springcloud.test.projectb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan("org.skywalking.springcloud.test")
public class ProjectB_Main {

    public static void main(String[] args) {
        SpringApplication.run(ProjectB_Main.class, args);
    }
}
