package com.learn.dubbo.service.impl;

import com.learn.dubbo.model.User;
import com.learn.dubbo.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User queryUserById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setAge(13);
        user.setName("good boy 阿三");
        return user;
    }
}
