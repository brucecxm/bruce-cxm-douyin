package com.bruce.dtos;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "OrderResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderResponse {
    @XmlElement(name = "OrderNo")
    private String orderNo;

    @XmlElement(name = "Status")
    private String status;

    @XmlElement(name = "Amount")
    private String amount;

    // Getter & Setter
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
}
