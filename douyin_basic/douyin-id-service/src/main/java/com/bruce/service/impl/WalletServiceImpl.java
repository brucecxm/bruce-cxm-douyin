package com.bruce.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.WalletDao;
import com.bruce.entity.Wallet;
import com.bruce.service.WalletService;
import org.springframework.stereotype.Service;

/**
 * (Wallet)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 00:22:01
 */
@Service("walletService")
public class WalletServiceImpl extends ServiceImpl<WalletDao, Wallet> implements WalletService {

}

