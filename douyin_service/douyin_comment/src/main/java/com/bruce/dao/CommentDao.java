package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.comment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Comment)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-26 23:27:02
 */
@Mapper
public interface CommentDao extends BaseMapper<Comment> {

}

