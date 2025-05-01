package com.bruce.fileter;
import com.bruce.utils.JwtUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil = new JwtUtil();



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉Bearer前缀
            if (jwtUtil.isTokenExpired(token)) {
                httpServletResponse.setStatus(401); // Token过期
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
