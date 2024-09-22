package com.bruce.servid;
import com.bruce.entile.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 创建用户
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // 查询所有用户
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 根据ID查询用户
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // 更新用户
    public User updateUser(String id, User user) {
        user.setId(id); // 设置ID以确保更新现有用户
        return userRepository.save(user);
    }

    // 删除用户
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
