package com.learn.dubbo.controller;

import com.learn.dubbo.model.User;
import com.learn.dubbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/user")
    public String getUserById(Model model, Integer id){
       User user =  userService.queryUserById(id);
       model.addAttribute("user",user);
       return "user";
    }

}
