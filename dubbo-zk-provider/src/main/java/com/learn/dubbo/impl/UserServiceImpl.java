package com.learn.dubbo.impl;

import com.learn.dubbo.model.User;
import com.learn.dubbo.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User queryUserById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setUserName("dubbo zk provider impl sir");
        return user;
    }

}
