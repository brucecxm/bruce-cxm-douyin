package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.FileOperationLogDao;
import com.bruce.entity.FileOperationLog;
import com.bruce.service.FileOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件操作记录服务实现类
 *
 * @author makejava
 * @since 2025-07-29
 */
@Service
public class FileOperationLogServiceImpl extends ServiceImpl<FileOperationLogDao, FileOperationLog> implements FileOperationLogService {

    @Autowired
    private FileOperationLogDao fileOperationLogDao;

    @Override
    public void saveLog(FileOperationLog log) {
        // 使用MyBatis-Plus的save方法保存日志
        this.save(log);
    }
}