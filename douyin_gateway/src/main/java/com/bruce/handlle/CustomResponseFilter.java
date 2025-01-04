//package com.bruce.handlle;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import java.util.List;
//
//@Component
//public class CustomResponseFilter extends AbstractGatewayFilterFactory<CustomResponseFilter.Config> {
//
//    public CustomResponseFilter() {
//        super(Config.class);
//    }
//
//    @Override public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            // 在响应发送之前进行处理
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                // 拦截响应并修改
//
//
//
//
//
//
//
//                exchange.getResponse().getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
//                List<String> sa_token=exchange.getResponse().getHeaders().get("Set-Cookie");
//                List<String> sa_token_one=exchange.getResponse().getHeaders().get("Set-Cookie");
//String sa_token_string=sa_token.get(0);
////                String value = (String) exchange.getAttributes().get("username");
////                String valuse = (String) exchange.getAttributes().get("username");
//                // 其他响应处理逻辑
//            }));
//        };
//    }
//
//    public static class Config {
//        // 配置参数
//    }
//}
