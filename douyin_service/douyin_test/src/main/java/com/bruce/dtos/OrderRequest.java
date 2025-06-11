package com.bruce.dtos;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "OrderRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderRequest {
    @XmlElement(name = "OrderNo")
    private String orderNo;

    @XmlElement(name = "MerchantId")
    private String merchantId;

    // Getter & Setter
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
}
