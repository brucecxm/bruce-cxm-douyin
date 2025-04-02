package com.bruce.service;

public interface idService {
    //uuid
    String uuidGen();
    //雪花算法
    long SnowflakeGen();
    //自增
    long insertGen();
}
