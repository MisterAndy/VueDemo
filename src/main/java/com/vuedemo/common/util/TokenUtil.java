package com.vuedemo.common.util;

import java.util.Date;

import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * jwt工具类
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "token")
public class TokenUtil {

    private String header;
    private String secret;// 密钥盐
    private long expire;// 令牌有效期

    /**
     * 根据当前用户获取token
     * 
     * @param userId
     * @return
     */
    public String getToken(String userId) {
        return getToken(userId, System.currentTimeMillis());
    }

    /**
     * 根据当前用户和自定义过期起始时间获取token
     * 
     * @param userId
     * @param startTime
     * @return
     */
    public String getToken(String userId, Long startTime) {

        String token = null;
        try {
            Date expireAt = new Date(startTime + expire * 60 * 1000);
            token = JWT.create().withIssuer("TengDi")// 发行人
                .withClaim("account", userId)// 存放数据
                .withClaim("startTime", startTime).withExpiresAt(expireAt)// 过期时间
                .sign(Algorithm.HMAC256(secret));
        } catch (IllegalArgumentException | JWTCreationException je) {

        }
        return token;
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * 解析token数据获取userId
     */
    public String getUserId(AuthenticationToken token) {
        try {
            String jwttoken = (String)token.getPrincipal();
            DecodedJWT decodedJWT = JWT.decode(jwttoken);
            return decodedJWT.getClaim("account").asString();

        } catch (JWTCreationException e) {
            return null;
        }
    }

    /**
     * 解析token数据获取startTime
     */
    public Long getStartTime(AuthenticationToken token) {
        try {
            String jwttoken = (String)token.getPrincipal();
            DecodedJWT decodedJWT = JWT.decode(jwttoken);
            return decodedJWT.getClaim("startTime").asLong();

        } catch (JWTCreationException e) {
            return null;
        }
    }

    /**
     * token验证
     */
    public Boolean verify(AuthenticationToken token) throws Exception {
        String jwttoken = (String)token.getPrincipal();
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).withIssuer("TengDi").build();// 创建token验证器
        DecodedJWT decodedJWT = jwtVerifier.verify(jwttoken);
        System.out.println("认证通过：");
        System.out.println("account: " + decodedJWT.getClaim("userId").asString());
        System.out.println("过期时间：      " + decodedJWT.getExpiresAt());
        return true;
    }
}
