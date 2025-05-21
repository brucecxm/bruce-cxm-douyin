package com.bruce.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.CommentDao;
import com.bruce.entity.Comment;
import com.bruce.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-06-26 23:27:04
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

}

