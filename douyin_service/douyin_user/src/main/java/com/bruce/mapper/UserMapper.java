package com.bruce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author bruce
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 查询热门用户ID列表
     * @param limit 查询数量限制
     * @return 热门用户ID列表
     */
    @Select("SELECT id FROM user ORDER BY active_time DESC LIMIT #{limit}")
    List<Long> selectHotUserIds(int limit);
}