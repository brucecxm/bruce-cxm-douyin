package com.example.apiserver.service;

import com.example.apiserver.entity.User;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.util.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(User user) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        System.out.println("Login attempt for username: " + username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("User found: " + user.getUsername());
            System.out.println("Password provided: " + password);
            System.out.println("Stored password: " + user.getPassword());
            boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Password match: " + passwordMatch);
            if (passwordMatch) {
                return user;
            }
        } else {
            System.out.println("User not found: " + username);
        }
        throw new RuntimeException("用户名或密码错误");
    }

    public User getUserById(Long id) {
        Map<String, Object> userMap = userRepository.findById(id);
        if (userMap == null || userMap.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        return UserConverter.convertToUser(userMap);
    }

    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        existingUser.setNickname(user.getNickname());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAvatar(user.getAvatar());
        existingUser.setGender(user.getGender());
        existingUser.setBirthday(user.getBirthday());
        existingUser.setBio(user.getBio());
        return userRepository.save(existingUser);
    }

    public User changePassword(Long id, String oldPassword, String newPassword) {
        User user = getUserById(id);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}