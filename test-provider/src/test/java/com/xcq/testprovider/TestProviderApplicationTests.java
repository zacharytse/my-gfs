package com.xcq.testprovider;

import com.xcq.testprovider.service.UserService;
import com.xcq.testprovider.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestProviderApplication.class)
@RunWith(SpringRunner.class)
public class TestProviderApplicationTests {
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void contextLoads() {
        System.out.println(userService.queryUserById(1));
    }

}
