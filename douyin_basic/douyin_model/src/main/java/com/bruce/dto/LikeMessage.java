package com.bruce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeMessage implements Serializable {
    private Long videoId;
    private Long userId;
    private String type;
    private Long timestamp;
}
