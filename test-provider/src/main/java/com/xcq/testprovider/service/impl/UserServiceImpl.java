package com.xcq.testprovider.service.impl;

import com.xcq.testprovider.dto.User;
import com.xcq.testprovider.service.UserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Service
@Component
public class UserServiceImpl implements UserService {
    @Override
    public User queryUserById(int id) {
        return new User(id,"zhangsan",new Date(),new Date());
    }
}
