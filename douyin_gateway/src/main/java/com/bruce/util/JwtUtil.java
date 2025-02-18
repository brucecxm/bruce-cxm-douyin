package com.bruce.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private String secretKey = "yourSecretKey"; // 密钥

    // 生成JWT token
    public String generateToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey); // 使用HMAC256算法
        return JWT.create()
                .withSubject(username) // 设置主题
                .withIssuedAt(new Date()) // 设置签发时间
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 设置过期时间为1小时
                .sign(algorithm); // 使用指定的算法进行签名
    }

    // 解析JWT token
    public DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.require(algorithm)
                .build()
                .verify(token); // 解密并验证token
    }

    // 获取用户名
    public String extractUsername(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getSubject(); // 获取JWT中的主题（即用户名）
    }

    // 判断JWT是否过期
    public boolean isTokenExpired(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getExpiresAt().before(new Date()); // 检查过期时间
    }

    // 验证token
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
