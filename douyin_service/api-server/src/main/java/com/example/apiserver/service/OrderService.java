package com.example.apiserver.service;

import com.example.apiserver.entity.Address;
import com.example.apiserver.entity.Order;
import com.example.apiserver.entity.Product;
import com.example.apiserver.entity.User;
import com.example.apiserver.repository.AddressRepository;
import com.example.apiserver.repository.OrderRepository;
import com.example.apiserver.repository.ProductRepository;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.util.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private AddressRepository addressRepository;

    public Order createOrder(OrderRequest orderRequest) {
        // 检查用户是否存在
        Map<String, Object> userMap = userRepository.findById(orderRequest.getUserId());
        if (userMap == null || userMap.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = UserConverter.convertToUser(userMap);

        // 检查商品是否存在
        Product product = productRepository.findById(orderRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        
        // 检查地址是否存在
        if (orderRequest.getAddressId() == null) {
            throw new RuntimeException("收货地址不能为空");
        }
        
        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new RuntimeException("地址不存在"));

        // 生成订单号
        String orderNo = UUID.randomUUID().toString().replace("-", "");

        // 创建订单对象
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUser(user);
        order.setProduct(product);
        order.setAddress(address);
        order.setQuantity(orderRequest.getQuantity());
        order.setTotalAmount(orderRequest.getTotalPrice());
        order.setStatus("pending_payment");
        order.setPaymentMethod(orderRequest.getPaymentMethod() != null ? orderRequest.getPaymentMethod() : "未选择");
        order.setShippingFee(orderRequest.getShippingFee() != null ? orderRequest.getShippingFee() : 0.0);
        order.setDiscount(orderRequest.getDiscount() != null ? orderRequest.getDiscount() : 0.0);
        
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // 订单请求参数类
    public static class OrderRequest {
        private Long userId;
        private Long productId;
        private Long addressId;
        private Integer quantity;
        private Double totalPrice;
        private String paymentMethod;
        private Double shippingFee;
        private Double discount;

        // getter 和 setter 方法
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
        
        public Long getAddressId() {
            return addressId;
        }
        
        public void setAddressId(Long addressId) {
            this.addressId = addressId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
        }
        
        public String getPaymentMethod() {
            return paymentMethod;
        }
        
        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
        
        public Double getShippingFee() {
            return shippingFee;
        }
        
        public void setShippingFee(Double shippingFee) {
            this.shippingFee = shippingFee;
        }
        
        public Double getDiscount() {
            return discount;
        }
        
        public void setDiscount(Double discount) {
            this.discount = discount;
        }
    }
}