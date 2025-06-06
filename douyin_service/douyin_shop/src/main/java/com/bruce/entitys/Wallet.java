package com.bruce.entitys;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Wallet {
    private Long id;

    private Long userId;

    private BigDecimal balance;

    private Timestamp updateTime;
}
