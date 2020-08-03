package com.vuedemo.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.vuedemo.common.http.Result;
import com.vuedemo.entity.User;
import com.vuedemo.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qipd
 * @since 2020-07-31
 */
@Api(tags = "用户信息管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation("获取用户名称")
    @RequiresAuthentication
    @GetMapping("/index")
    public String index() {

        User user = userService.getById(1L);
        return user.getUsername();
    }

    @ApiOperation("获取用户名称w无授权")
    @GetMapping("/list")
    public String list() {

        User user = userService.getById(1L);
        return user.getUsername();
    }

    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user) {
        return Result.success(user);
    }

}
