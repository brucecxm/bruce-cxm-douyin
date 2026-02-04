package com.bruce.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 使用 addAllowedOriginPattern 代替 addAllowedOrigin，因为 allowCredentials 为 true 时不能使用 "*" 通配符
        config.addAllowedOriginPattern("*"); // 允许所有源（生产环境需指定具体域名）
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true); // 若允许 Cookie，需注意：addAllowedOrigin("*") 与 allowCredentials 不能同时为 true

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
