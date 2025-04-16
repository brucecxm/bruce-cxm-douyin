package com.bruce.service.impl;

import com.bruce.config.AppConfig;
import com.bruce.service.IdService;
import com.bruce.utils.IdGenerator;
import com.bruce.utils.SimpleIdGenerator;
import com.bruce.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

@Service
@Import({AppConfig.class, IdGenerator.class,SimpleIdGenerator.class,SnowflakeIdGenerator.class})
public class IdServiceImpl implements IdService {
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private SimpleIdGenerator simpleIdGenerator;
    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;


    @Override
    public String uuidGen() {
        return idGenerator.generateUUID();
    }

    @Override
    public long SnowflakeGen() {
        return snowflakeIdGenerator.generateId();
    }

    @Override
    public long insertGen() {
        return simpleIdGenerator.generateId();
    }
}
