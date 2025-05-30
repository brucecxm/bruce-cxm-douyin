package com.bruce.controller;

import com.bruce.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单明细
 */
@Slf4j
@RestController
@RequestMapping("/shop/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

}