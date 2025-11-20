package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * (FileOperationLog)表实体类
 * 文件操作记录表
 *
 * @author makejava
 * @since 2025-07-29
 */
@SuppressWarnings("serial")
@Data
public class FileOperationLog extends Model<FileOperationLog> {

    private Long id;
    //操作用户ID
    private Long userId;
    //操作类型：upload/download/delete
    private String operationType;
    //文件URL
    private String fileUrl;
    //文件名
    private String fileName;
    //文件大小（字节）
    private Long fileSize;
    //文件类型
    private String fileType;
    //操作IP地址
    private String ipAddress;
    //操作状态：1成功，0失败
    private Integer status;
    //错误信息
    private String errorMessage;
    //操作时间
    private Date createTime;

}