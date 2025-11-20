package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.FileOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * (FileOperationLog)表数据库访问层
 * 文件操作记录的数据访问接口
 *
 * @author makejava
 * @since 2025-07-29
 */
@Mapper
public interface FileOperationLogDao extends BaseMapper<FileOperationLog> {

}