package com.xcq.master;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class GfsMasterApplicationTests {
    @Test
    void contextLoads() throws Exception {
        System.out.println(System.getProperty("user.home") + "/.dubbo/dubbo-registry-");
    }

}
