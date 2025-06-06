package com.bruce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entitys.Wallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WalletMapper extends BaseMapper<Wallet> {

    @Select("SELECT * FROM wallet WHERE user_id = #{userId} LIMIT 1")
    Wallet selectByUserId(@Param("userId") Long userId);
}
