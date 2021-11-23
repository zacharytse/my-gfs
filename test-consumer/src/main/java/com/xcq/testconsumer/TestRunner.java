package com.xcq.testconsumer;

import com.xcq.testprovider.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestRunner implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(TestRunner.class,args);
    }
    @Reference
    private UserService userService;
    @Override
    public void run(String... strings) throws Exception {
        System.out.println(userService.queryUserById(1));

    }
}
