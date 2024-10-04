package com.bruce.service;
import com.bruce.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TicketService {

    @Autowired
    private RestTemplate restTemplate;

    // SSO 校验接口的地址
    private static final String CHECK_TICKET_URL = "http://localhost:8988//sso/checkTicket";

    // 假设这是你的密钥
    private static final String SECRET_KEY = "secretkey";

    public String checkTicket(String ticket, String client, String ssoLogoutCall) {
        // 获取当前时间戳（13位）
        long timestamp = System.currentTimeMillis();

        // 随机生成一个 nonce（这里简单使用UUID作为随机字符串）
        String nonce = java.util.UUID.randomUUID().toString();

        // 拼接需要签名的字符串
        StringBuilder sb = new StringBuilder();
        if (client != null && !client.isEmpty()) {
            sb.append("client=").append(client).append("&");
        }
        sb.append("nonce=").append(nonce).append("&");
        if (ssoLogoutCall != null && !ssoLogoutCall.isEmpty()) {
            sb.append("ssoLogoutCall=").append(ssoLogoutCall).append("&");
        }
        sb.append("ticket=").append(ticket).append("&");
        sb.append("timestamp=").append(timestamp).append("&");
        sb.append("key=").append(SECRET_KEY);

        // 生成 MD5 签名
        String sign = MD5Util.generateMD5(sb.toString());

        // 创建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("ticket", ticket);
        params.put("timestamp", String.valueOf(timestamp));
        params.put("nonce", nonce);
        params.put("sign", sign);
        if (client != null) {
            params.put("client", client);
        }
        if (ssoLogoutCall != null) {
            params.put("ssoLogoutCall", ssoLogoutCall);
        }

        // 发起 HTTP 请求进行校验
        String response = restTemplate.getForObject(CHECK_TICKET_URL, String.class, params);
        return response;
    }
}
