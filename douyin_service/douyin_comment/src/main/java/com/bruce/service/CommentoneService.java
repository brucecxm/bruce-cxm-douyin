package com.bruce.service;

import com.bruce.dto.CommentDto;

import java.util.List;

public interface CommentoneService  {
    List<CommentDto> getcomment();
    List<CommentDto> getcommenti(Integer id);
}
