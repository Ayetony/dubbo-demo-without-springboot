package com.learn.dubbo.service;

import com.learn.dubbo.model.User;

public interface UserService {
    User queryUserById(Integer id);
}
