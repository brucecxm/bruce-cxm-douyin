package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.CommentDao;
import com.bruce.dao.FriendDao;
import com.bruce.dto.CommentDto;
import com.bruce.dao.CommentMapper;
import com.bruce.entity.Comment;
import com.bruce.entity.Friend;
import com.bruce.service.CommentoneService;
import com.bruce.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentoneServiceimpl extends ServiceImpl<CommentDao, Comment> implements CommentoneService {
@Autowired
private CommentMapper commentMapper;

    @Override
    public List<Map> getcomment(int videoId) {

        List<Map> one =commentMapper.getCommentid(videoId);
        return one;
    }

    @Override
    public List<CommentDto> getcommenti(Integer id) {
        return commentMapper.getCommentide(id);
    }
}
