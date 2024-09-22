package com.bruce.feign;



import com.baomidou.mybatisplus.extension.api.ApiController;
import com.bruce.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (Comment)表控制层
 *
 * @author makejava
 * @since 2024-06-26 23:27:01
 */
@RestController
@RequestMapping("/feigncomment")
public class commentFeign extends ApiController {
    /**
     * 服务对象
     */
    @Autowired
    private CommentService commentService;


}

