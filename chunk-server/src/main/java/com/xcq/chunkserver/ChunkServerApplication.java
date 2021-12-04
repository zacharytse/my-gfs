package com.xcq.chunkserver;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ChunkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChunkServerApplication.class, args);
    }

}
