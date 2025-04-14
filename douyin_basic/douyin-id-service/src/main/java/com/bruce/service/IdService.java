package com.bruce.service;

public interface IdService {
    //uuid
    String uuidGen();
    //雪花算法
    long SnowflakeGen();
    //自增
    long insertGen();
}
