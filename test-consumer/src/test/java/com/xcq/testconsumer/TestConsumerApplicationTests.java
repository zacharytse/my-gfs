package com.xcq.testconsumer;

import com.xcq.testconsumer.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestConsumerApplication.class)
@RunWith(SpringRunner.class)
public class TestConsumerApplicationTests {

    @Autowired
    private TestService testService;
    @Test
    public void contextLoads() {
       testService.test();
    }

}
