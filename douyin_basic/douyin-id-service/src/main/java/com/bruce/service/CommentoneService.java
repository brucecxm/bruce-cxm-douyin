package com.bruce.service;

import com.bruce.dto.CommentDto;

import java.util.List;
import java.util.Map;

public interface CommentoneService  {
    List<Map> getcomment(int videoId);
    List<CommentDto> getcommenti(Integer id);
}
