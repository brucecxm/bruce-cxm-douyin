package com.bruce.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.WalletDetailDao;
import com.bruce.entity.WalletDetail;
import com.bruce.service.WalletDetailService;
import org.springframework.stereotype.Service;

/**
 * (WalletDetail)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 00:22:26
 */
@Service("walletDetailService")
public class WalletDetailServiceImpl extends ServiceImpl<WalletDetailDao, WalletDetail> implements WalletDetailService {

}

