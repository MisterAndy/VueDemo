package com.vuedemo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vuedemo.common.http.Result;
import com.vuedemo.common.util.TokenUtil;
import com.vuedemo.dto.LoginUserDto;
import com.vuedemo.entity.User;
import com.vuedemo.service.UserService;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(tags = "1-登录管理")
@RestController
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation("用户登录")
    @ApiImplicitParam(name = "loginUserDto", value = "用户信息", dataType = "LoginUserDto")
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginUserDto.getUsername()));
        if (user == null) {
            return Result.fail("该用户不存在！");
        }

        if (!user.getPassword().equals(SecureUtil.md5(loginUserDto.getPassword()))) {
            return Result.fail("密码不正确!");
        }

        String token = tokenUtil.generateToken(user.getId());
        String header = tokenUtil.getHeader();// 获取令牌自定义标识
        response.setHeader(header, token);
        response.setHeader("Access-control-Expose-Headers", header);

        Map<Object, Object> map = MapUtil.builder().put("id", user.getId()).put("username", user.getUsername())
            .put("avatar", user.getAvatar()).put("email", user.getEmail()).map();
        return Result.success(map);

    }
}
