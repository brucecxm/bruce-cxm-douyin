//package com.bruce.handlle;
//import cn.dev33.satoken.stp.StpUtil;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//
//@Component
//public class CustomRequestInterceptor implements GlobalFilter, Ordered {
//
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        // Log the request URI
//        System.out.println("Request URI: " + request.getURI());
//        MultiValueMap<String, String> queryParams = request.getQueryParams();
////        String param1 = queryParams.getFirst("username");
////
////        // 将值存储在 exchange 的 attributes 中
////        exchange.getAttributes().put("username", param1);
//
//
//        // Add custom headers or perform other actions
//        ServerHttpRequest modifiedRequest = request.mutate()
//                .header("X-Custom-Header", "MyCustomValue")
//                .build();
//        return chain.filter(exchange.mutate().request(modifiedRequest).build());
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.LOWEST_PRECEDENCE;
//    }
//}
