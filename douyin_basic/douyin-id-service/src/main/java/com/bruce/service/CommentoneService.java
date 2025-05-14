package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.dto.CommentDto;
import com.bruce.entity.Comment;
import com.bruce.entity.Friend;

import java.util.List;
import java.util.Map;

public interface CommentoneService  extends IService<Comment> {
    List<Map> getcomment(int videoId);
    List<CommentDto> getcommenti(Integer id);
}
