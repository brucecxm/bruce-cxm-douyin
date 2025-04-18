package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.FriendRequestDao;
import com.bruce.entity.FriendRequest;
import com.bruce.service.FriendRequestService;
import org.springframework.stereotype.Service;

/**
 * 好友请求表(FriendRequest)表服务实现类
 *
 * @author makejava
 * @since 2025-04-18 13:52:23
 */
@Service("friendRequestService")
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestDao, FriendRequest> implements FriendRequestService {

}

