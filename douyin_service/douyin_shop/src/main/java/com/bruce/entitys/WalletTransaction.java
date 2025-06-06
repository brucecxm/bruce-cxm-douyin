package com.bruce.entitys;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class WalletTransaction {
    private Long id;

    private Long walletId;
    private String type; // TRANSFER_IN / TRANSFER_OUT
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String description;
    private Timestamp createTime;
}
