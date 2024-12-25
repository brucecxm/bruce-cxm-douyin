package com.bruce.feign;




import com.bruce.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2024-06-25 13:57:40
 */
@RestController
@RequestMapping("/userfeign")
public class userFeign  {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

//    @GetMapping("/avatar")
//    public User avatar(@RequestParam int authId) {
//
//        User user=  this.userService.findById(authId);
//        return user;
//    }

}

