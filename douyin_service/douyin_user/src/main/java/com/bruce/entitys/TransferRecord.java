package com.bruce.entitys;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class TransferRecord {
    private Long id;

    private Long fromUserId;
    private Long toUserId;
    private BigDecimal amount;
    private Timestamp createTime;
}
