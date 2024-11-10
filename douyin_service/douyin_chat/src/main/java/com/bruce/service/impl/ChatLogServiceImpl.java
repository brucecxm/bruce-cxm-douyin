package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.ChatLogDao;
import com.bruce.entity.ChatLog;
import com.bruce.service.ChatLogService;
import org.springframework.stereotype.Service;

/**
 * (ChatLog)表服务实现类
 *
 * @author makejava
 * @since 2024-11-10 19:54:39
 */
@Service("chatLogService")
public class ChatLogServiceImpl extends ServiceImpl<ChatLogDao, ChatLog> implements ChatLogService {

}

