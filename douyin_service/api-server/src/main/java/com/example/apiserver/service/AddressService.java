package com.example.apiserver.service;

import com.example.apiserver.entity.Address;
import com.example.apiserver.entity.User;
import com.example.apiserver.repository.AddressRepository;
import com.example.apiserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    public Address createAddress(Address address) {
        // 从前端接收的address中获取userId，然后查询完整的User对象
        Long userId = address.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        address.setUser(user);
        
        // 如果设置为默认地址，先将其他地址设为非默认
        if (address.getIsDefault()) {
            List<Address> addresses = addressRepository.findByUserId(userId);
            for (Address addr : addresses) {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            }
        }
        return addressRepository.save(address);
    }

    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("地址不存在"));
    }

    public Address updateAddress(Long id, Address address) {
        Address existingAddress = getAddressById(id);
        
        // 确保existingAddress的User对象存在
        if (existingAddress.getUser() == null) {
            throw new RuntimeException("地址所属用户不存在");
        }
        
        // 如果设置为默认地址，先将其他地址设为非默认
        if (address.getIsDefault()) {
            List<Address> addresses = addressRepository.findByUserId(existingAddress.getUser().getId());
            for (Address addr : addresses) {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            }
        }
        
        existingAddress.setReceiver(address.getReceiver());
        existingAddress.setPhone(address.getPhone());
        existingAddress.setProvince(address.getProvince());
        existingAddress.setCity(address.getCity());
        existingAddress.setDistrict(address.getDistrict());
        existingAddress.setDetail(address.getDetail());
        existingAddress.setIsDefault(address.getIsDefault());
        
        return addressRepository.save(existingAddress);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}