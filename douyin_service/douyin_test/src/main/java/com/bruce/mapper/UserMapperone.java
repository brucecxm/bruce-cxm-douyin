package com.bruce.mapper;

import com.bruce.dtos.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapperone {

    User getUserById(int id);
}
