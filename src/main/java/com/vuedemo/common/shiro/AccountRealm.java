package com.vuedemo.common.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vuedemo.common.util.TokenUtil;
import com.vuedemo.entity.User;
import com.vuedemo.service.UserService;

import cn.hutool.core.bean.BeanUtil;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    UserService userService;

    /**
     * 使Realm支持JwtToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {

        return token instanceof JwtToken;
    }

    /**
     * 授权方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        return null;
    }

    /**
     * 获取认证信息并进行认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken)token;

        String userId = tokenUtil.getClaimByToken((String)jwtToken.getPrincipal()).getSubject();

        User user = userService.getById(Long.valueOf(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }

        if (user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定");
        }

        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);

        return new SimpleAuthenticationInfo(profile, token.getCredentials(), getName());
    }
}
