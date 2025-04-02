package com.bruce.service.impl;

import com.bruce.service.idService;
import com.bruce.utils.IdGenerator;
import com.bruce.utils.SimpleIdGenerator;
import com.bruce.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class idServiceImpl implements idService {
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
