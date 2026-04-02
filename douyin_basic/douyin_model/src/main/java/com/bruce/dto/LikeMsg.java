package com.bruce.dto;
import lombok.Data;
import java.io.Serializable;

/**
 * 视频点赞消息体
 * 对应 JSON:
 * {
 *   "videoId": 123,
 *   "userId": 888,
 *   "delta": +1,
 *   "timestamp": 1712345678
 * }
 */
@Data
public class LikeMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频ID
     */
    private Long videoId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 点赞变动值：+1点赞 / -1取消点赞
     */
    private Integer delta;

    /**
     * 时间戳（秒级）
     */
    private Long timestamp;
}
