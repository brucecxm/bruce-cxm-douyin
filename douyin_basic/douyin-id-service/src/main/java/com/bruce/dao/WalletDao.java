package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Wallet;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Wallet)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-21 00:22:01
 */
@Mapper
public interface WalletDao extends BaseMapper<Wallet> {

}

