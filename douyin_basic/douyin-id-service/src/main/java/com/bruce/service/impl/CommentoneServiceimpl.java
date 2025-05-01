package com.bruce.service.impl;

import com.bruce.dto.CommentDto;
import com.bruce.dao.CommentMapper;
import com.bruce.service.CommentoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentoneServiceimpl implements CommentoneService {
@Autowired
private CommentMapper commentMapper;

    @Override
    public List<Map> getcomment(int videoId) {
        int video_id=1;
        List<Map> one =commentMapper.getCommentid(videoId);
        return one;
    }

    @Override
    public List<CommentDto> getcommenti(Integer id) {
        return commentMapper.getCommentide(id);
    }
}
