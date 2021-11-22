package com.xcq.testconsumer.service;

import com.xcq.testprovider.dto.User;
import com.xcq.testprovider.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class TestService {
    @Reference
    private UserService userService;

    public void test() {
        User user = userService.queryUserById(1);
        System.out.println(user);
    }
}
