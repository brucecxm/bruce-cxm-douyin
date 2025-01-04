package com.bruce.dao;

import com.bruce.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<CommentDto> getCommentid();
    List<CommentDto> getCommentide(Integer fuid);
}
