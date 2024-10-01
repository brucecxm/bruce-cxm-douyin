//package com.bruce.filter;
//
//
//import com.bruce.utils.JwtUtil;
//import io.jsonwebtoken.Claims;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.Map;
//
//@Component
//@Slf4j
//public class AuthorizeFilter implements Ordered, GlobalFilter {
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        //1.获取request和response对象
//        ServerHttpRequest request = exchange.getRequest();
//        ServerHttpResponse response = exchange.getResponse();
//
//        //2.判断是否是登录
//        if(request.getURI().getPath().contains("/login")){
//            //放行
//            return chain.filter(exchange);
//        }
//
//        //3.获取token
//        String token = request.getHeaders().getFirst("token");
//
//        //4.判断token是否存在
//        if(StringUtils.isBlank(token)){
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
//
//
//
//        //5.判断token是否有效
//        try {
//
//            //从redis中获取相同的token
//            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//            String redisToken = operations.get(token);
//            if (redisToken==null){
//                //token已经失效了
//                throw new RuntimeException();
//            }
//            Map<String, Object> claims = JwtUtil.parseToken(token);
//stringRedisTemplate.opsForValue().set("claims", String.valueOf(claims));
//
////            //存储header中
////            ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
////                httpHeaders.add("userId", userId + "");
////            }).build();
////            //重置请求
////            exchange.mutate().request(serverHttpRequest);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //6.放行
//        return chain.filter(exchange);
//    }
//
//    /**
//     * 优先级设置  值越小  优先级越高
//     * @return
//     */
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
