package com.vuedemo.common.shiro;

import java.io.Serializable;

import lombok.Data;

/**
 * 用于认证后返回的用户信息，不包括敏感信息，如：密码等
 */
@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String username;

    private String avatar;

    private String email;

}