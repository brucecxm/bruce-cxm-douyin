package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.UserBlacklistDao;
import com.bruce.entity.UserBlacklist;
import com.bruce.service.UserBlacklistService;
import org.springframework.stereotype.Service;

/**
 * 用户黑名单表(UserBlacklist)表服务实现类
 *
 * @author makejava
 * @since 2025-07-29 17:01:23
 */
@Service("userBlacklistService")
public class UserBlacklistServiceImpl extends ServiceImpl<UserBlacklistDao, UserBlacklist> implements UserBlacklistService {

}

