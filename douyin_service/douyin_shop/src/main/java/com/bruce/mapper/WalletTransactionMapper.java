package com.bruce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entitys.WalletTransaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WalletTransactionMapper extends BaseMapper<WalletTransaction> {}