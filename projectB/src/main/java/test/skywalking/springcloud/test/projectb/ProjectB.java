package test.skywalking.springcloud.test.projectb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("test.skywalking.springcloud.test")
public class ProjectB {

    public static void main(String[] args) {
        SpringApplication.run(ProjectB.class, args);
    }
}
