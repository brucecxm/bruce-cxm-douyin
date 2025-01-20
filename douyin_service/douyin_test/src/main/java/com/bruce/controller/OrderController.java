package com.bruce.controller;

import com.bruce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    @Qualifier("orderServiceimpl")
    private OrderService orderService;

    // Get mapping for test order selection
    @GetMapping("/test")
    @ResponseBody  // Adding @ResponseBody to return a response body
    public String OrderSelect() {
        orderService.myMethod(); // Call the service method
        return "Order method executed successfully!"; // Returning a success message
    }
}
