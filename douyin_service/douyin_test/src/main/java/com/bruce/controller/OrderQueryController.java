package com.bruce.controller;


import com.bruce.client.ThirdPartyClient;
import com.bruce.dtos.OrderRequest;
import com.bruce.dtos.OrderResponse;
import com.bruce.utils.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderQueryController {

    @Autowired
    private ThirdPartyClient thirdPartyClient;

    @GetMapping("/query")
    public OrderResponse queryOrder(@RequestParam String orderNo) throws Exception {
        OrderRequest req = new OrderRequest();
        req.setOrderNo(orderNo);
        req.setMerchantId("MERCHANT123");

        // 模拟调用三方系统
        String responseXml = thirdPartyClient.queryOrder(req);

        // 解析三方返回的 XML
        return XmlUtil.fromXml(responseXml, OrderResponse.class);
    }
}
