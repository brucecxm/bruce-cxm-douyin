package com.bruce.entitys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_live")
public class Live implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 主播ID
    private Long hostId;

    // 主播用户名
    private String hostUsername;

    // 直播标题
    private String title;

    // 直播封面
    private String coverUrl;

    // 直播频道名称
    private String channelName;

    // 直播状态：0-已结束，1-直播中
    private Integer status;

    // 开始时间
    private Date startTime;

    // 结束时间
    private Date endTime;

    // 观看人数
    private Integer viewerCount;

    // 直播描述
    private String description;

    // 直播回放地址
    private String replayUrl;

    // 创建时间
    private Date createTime;

    // 更新时间
    private Date updateTime;
}
