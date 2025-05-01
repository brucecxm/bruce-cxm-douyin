package com.bruce.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String comment_id;
    private String last_id;
    private String comment;
    private String createtime;
}
