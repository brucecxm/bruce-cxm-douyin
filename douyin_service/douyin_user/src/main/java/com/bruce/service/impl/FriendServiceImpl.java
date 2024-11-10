package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.FriendDao;
import com.bruce.entity.Friend;
import com.bruce.service.FriendService;
import org.springframework.stereotype.Service;

/**
 * (Friend)表服务实现类
 *
 * @author makejava
 * @since 2024-11-10 20:23:23
 */
@Service("friendService")
public class FriendServiceImpl extends ServiceImpl<FriendDao, Friend> implements FriendService {

}

