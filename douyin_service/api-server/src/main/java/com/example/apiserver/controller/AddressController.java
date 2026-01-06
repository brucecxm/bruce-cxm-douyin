package com.example.apiserver.controller;

import com.example.apiserver.common.Response;
import com.example.apiserver.entity.Address;
import com.example.apiserver.entity.User;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Response<Address> createAddress(@RequestBody Map<String, Object> addressData) {
        try {
            // 创建Address对象
            Address address = new Address();
            address.setReceiver((String) addressData.get("receiver"));
            address.setPhone((String) addressData.get("phone"));
            address.setProvince((String) addressData.get("province"));
            address.setCity((String) addressData.get("city"));
            address.setDistrict((String) addressData.get("district"));
            address.setDetail((String) addressData.get("detail"));
            address.setIsDefault((Boolean) addressData.getOrDefault("isDefault", false));
            
            // 处理用户关联
            Long userId = ((Number) addressData.get("userId")).longValue();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
            address.setUser(user);
            
            Address createdAddress = addressService.createAddress(address);
            return Response.success(createdAddress);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public Response<List<Address>> getAddressesByUserId(@PathVariable Long userId) {
        try {
            List<Address> addresses = addressService.getAddressesByUserId(userId);
            return Response.success(addresses);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Response<Address> getAddressById(@PathVariable Long id) {
        try {
            Address address = addressService.getAddressById(id);
            return Response.success(address);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Response<Address> updateAddress(@PathVariable Long id, @RequestBody Map<String, Object> addressData) {
        try {
            // 获取现有地址
            Address existingAddress = addressService.getAddressById(id);
            
            // 更新地址信息
            existingAddress.setReceiver((String) addressData.get("receiver"));
            existingAddress.setPhone((String) addressData.get("phone"));
            existingAddress.setProvince((String) addressData.get("province"));
            existingAddress.setCity((String) addressData.get("city"));
            existingAddress.setDistrict((String) addressData.get("district"));
            existingAddress.setDetail((String) addressData.get("detail"));
            existingAddress.setIsDefault((Boolean) addressData.getOrDefault("isDefault", false));
            
            // 如果提供了userId，更新用户关联
            if (addressData.containsKey("userId")) {
                Long userId = ((Number) addressData.get("userId")).longValue();
                User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
                existingAddress.setUser(user);
            }
            
            Address updatedAddress = addressService.updateAddress(id, existingAddress);
            return Response.success(updatedAddress);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return Response.success();
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }
}