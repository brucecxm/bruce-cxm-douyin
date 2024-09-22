//package com.bruce.feign;
//
//
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.api.ApiController;
//import com.baomidou.mybatisplus.extension.api.R;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.bruce.entity.User;
//import com.bruce.service.UserService;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.io.Serializable;
//import java.util.List;
//
///**
// * (User)表控制层
// *
// * @author makejava
// * @since 2024-06-25 13:57:40
// */
//@RestController
//@RequestMapping("/userfeign")
//public class userFeign extends ApiController {
//    /**
//     * 服务对象
//     */
//    @Resource
//    private UserService userService;
//
//    @GetMapping("/avatar/{authId}")
//    public User avatar(@PathVariable("authId") int authId) {
//        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper();
//        queryWrapper.eq(User::getUserId, authId);
//        User user=  this.userService.getOne(queryWrapper);
//        return user;
//    }
//
//}
//
