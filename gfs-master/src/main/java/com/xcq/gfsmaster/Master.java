package com.xcq.gfsmaster;

import com.xcq.gfsmaster.service.impl.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * master节点
 */
@SpringBootApplication
public class Master implements CommandLineRunner {
    @Autowired
    private FileSystem fileSystem;

    public static void main(String[] args) {
        SpringApplication.run(Master.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("running.....");
    }
}
