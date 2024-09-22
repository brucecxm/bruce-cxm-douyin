package com.bruce.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret; // JWT 密钥，从配置文件中获取

    @Value("${jwt.expiration}")
    private int expiration; // JWT 过期时间，从配置文件中获取，单位为秒

    /**
     * 生成 JWT Token
     *
     * @param username 用户名
     * @return JWT Token
     */
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 设置 Token 主题为用户名
                .setIssuedAt(new Date()) // 设置 Token 签发时间为当前时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) // 设置 Token 过期时间
                .signWith(SignatureAlgorithm.HS512, secret) // 使用 HS512 签名算法和密钥对 Token 进行签名
                .compact(); // 生成 Token 字符串
    }

    /**
     * 从 JWT Token 中解析获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret) // 使用密钥解析 Token
                .parseClaimsJws(token) // 解析 Token 的 JWS
                .getBody() // 获取 Token 的 Payload
                .getSubject(); // 获取 Payload 中的主题（即用户名）
    }

    /**
     * 验证 JWT Token 是否有效
     *
     * @param authToken JWT Token
     * @return true 如果 Token 有效；false 如果 Token 无效或过期
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken); // 使用密钥解析 Token
            return true; // Token 有效
        } catch (Exception e) {
            return false; // Token 无效
        }
    }
}
