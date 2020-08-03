package com.vuedemo.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginUserDto {
    @NotBlank(message = "姓名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
