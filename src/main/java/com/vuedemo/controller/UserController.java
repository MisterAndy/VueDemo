package com.vuedemo.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vuedemo.common.http.Result;
import com.vuedemo.entity.User;
import com.vuedemo.service.UserService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qipd
 * @since 2020-07-31
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/index")
    public String index() {

        User user = userService.getById(1L);
        return user.getUsername();
    }

    @RequiresAuthentication
    @RequestMapping("/getuser")
    public Object getuser() {

        User user = userService.getById(1L);
        return Result.success(user);
    }

}
