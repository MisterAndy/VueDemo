package com.vuedemo.common.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vuedemo.common.shiro.AccountRealm;
import com.vuedemo.common.shiro.JwtFilter;

@Configuration
public class ShiroConfig {

    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // 注入 redisSessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    // 配置核心安全事务管理器
    @Bean
    public DefaultWebSecurityManager securityManager(AccountRealm accountRealm, SessionManager sessionManager,
        RedisCacheManager redisCacheManager) {

        // 配置自定义Realm
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);

        // 注入 sessionManager
        securityManager.setSessionManager(sessionManager);

        // 注入 redisCacheManager
        securityManager.setCacheManager(redisCacheManager);
        return securityManager;
    }

    // 注入配置好的过滤路径ShiroFilterChain
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        Map<String, String> filterMap = new LinkedHashMap<>();

        //anon为不过滤
        filterMap.put("/login", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");

        filterMap.put("/**", "jwt");//使用自定义名为jwt的过滤器过滤所有文件

        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }

    // 注入配置好的过滤工厂
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
        ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();

        // 这里不能注入的jwtFilter，不然所有路径都被jwtFilter拦截
        filters.put("jwt", new JwtFilter());//设置名为jwt的过滤器
        shiroFilter.setFilters(filters);// 给过滤工厂ShiroFilter设置过滤器jwtFilter

        // 使用jwtFilter过滤FilterChain中的路径
        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
        shiroFilter.setFilterChainDefinitionMap(filterMap);// 给过滤工厂ShiroFilter设置过滤路径ShiroFilterChain
        return shiroFilter;
    }

}
