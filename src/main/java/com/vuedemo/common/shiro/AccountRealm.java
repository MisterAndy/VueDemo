package com.vuedemo.common.shiro;

import com.sun.tools.javac.comp.Todo;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vuedemo.common.util.JwtUtil;
import com.vuedemo.entity.User;
import com.vuedemo.service.UserService;

import cn.hutool.core.bean.BeanUtil;

@Component
public class AccountRealm extends AuthorizingRealm {

    final JwtUtil jwtUtil;

    final UserService userService;

    public AccountRealm(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

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

        //TODO 增加用户菜单权限管理后完善

        return null;
    }

    /**
     * 获取认证信息并进行认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //TODO 待token对应人员入库后从数据库查询获取token
        JwtToken jwtToken = (JwtToken)token;

        String userId = jwtUtil.getClaimByToken((String)jwtToken.getPrincipal()).getSubject();

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
