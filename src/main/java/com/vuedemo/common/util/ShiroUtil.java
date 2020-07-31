package com.vuedemo.common.util;

import org.apache.shiro.SecurityUtils;

import com.vuedemo.common.shiro.AccountProfile;

public class ShiroUtil {

    public static AccountProfile getProfile() {

        return (AccountProfile)SecurityUtils.getSubject().getPrincipal();
    }

}
