package com.vuedemo.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginUserDto {
    @ApiModelProperty(name = "用户姓名", position = 1)
    @NotBlank(message = "姓名不能为空")
    private String username;

    @ApiModelProperty(name = "密码", position = 2)
    @NotBlank(message = "密码不能为空")
    private String password;
}
