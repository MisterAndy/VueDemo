package com.vuedemo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vuedemo.common.util.Result;
import com.vuedemo.common.util.JwtUtil;
import com.vuedemo.dto.LoginUserDto;
import com.vuedemo.entity.User;
import com.vuedemo.service.UserService;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(tags = "*登录管理*")
@RestController
public class AccountController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    public AccountController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

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

        String token = jwtUtil.generateToken(user.getId());
        String header = jwtUtil.getHeader();// 获取令牌自定义标识
        response.setHeader(header, token);
        response.setHeader("Access-control-Expose-Headers", header);//跨域请求结果中header的权限控制

        Map<Object, Object> map = MapUtil.builder().put("id", user.getId()).put("username", user.getUsername())
            .put("avatar", user.getAvatar()).put("email", user.getEmail()).map();
        return Result.success(map);

    }

    @ApiOperation("用户退出")
    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success();
    }
}
