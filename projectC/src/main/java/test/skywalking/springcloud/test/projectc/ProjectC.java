package test.skywalking.springcloud.test.projectc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("test.skywalking.springcloud.test")
public class ProjectC {

    public static void main(String[] args) {
        SpringApplication.run(ProjectC.class, args);
    }
}
