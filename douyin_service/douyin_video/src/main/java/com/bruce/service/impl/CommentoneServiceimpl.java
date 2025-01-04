package com.bruce.service.impl;

import com.bruce.dto.CommentDto;
import com.bruce.mapper.CommentMapper;
import com.bruce.service.CommentoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentoneServiceimpl implements CommentoneService {
@Autowired
private CommentMapper commentMapper;

    @Override
    public List<CommentDto> getcomment() {
        return commentMapper.getCommentid();
    }

    @Override
    public List<CommentDto> getcommenti(Integer id) {
        return commentMapper.getCommentide(id);
    }
}
