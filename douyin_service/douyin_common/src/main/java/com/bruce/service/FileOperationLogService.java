package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.FileOperationLog;

/**
 * 文件操作记录服务接口
 *
 * @author makejava
 * @since 2025-07-29
 */
public interface FileOperationLogService extends IService<FileOperationLog> {
    
    /**
     * 记录文件操作日志
     * @param log 文件操作日志对象
     */
    void saveLog(FileOperationLog log);
}