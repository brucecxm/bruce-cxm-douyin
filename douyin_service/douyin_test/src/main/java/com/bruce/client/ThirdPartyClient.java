package com.bruce.client;
import com.bruce.dtos.OrderRequest;
import com.bruce.utils.XmlUtil;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyClient {

    public String queryOrder(OrderRequest req) throws Exception {
        // 模拟请求 XML 封包
        String reqXml = XmlUtil.toXml(req, OrderRequest.class);
        System.out.println("发送给三方系统的XML：\n" + reqXml);

        // 模拟三方系统返回的响应 XML
        String responseXml = "<OrderResponse>" +
                "<OrderNo>" + req.getOrderNo() + "</OrderNo>" +
                "<Status>SUCCESS</Status>" +
                "<Amount>199.99</Amount>" +
                "</OrderResponse>";

        return responseXml;
    }
}
