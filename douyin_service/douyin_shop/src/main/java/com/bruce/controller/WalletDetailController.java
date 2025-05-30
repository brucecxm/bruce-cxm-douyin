package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.WalletDetail;
import com.bruce.service.WalletDetailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (WalletDetail)表控制层
 *
 * @author makejava
 * @since 2025-01-21 00:22:26
 */
@RestController
@RequestMapping("/shop/walletDetail")
public class WalletDetailController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private WalletDetailService walletDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param walletDetail 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<WalletDetail> page, WalletDetail walletDetail) {
        return success(this.walletDetailService.page(page, new QueryWrapper<>(walletDetail)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.walletDetailService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param walletDetail 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody WalletDetail walletDetail) {
        return success(this.walletDetailService.save(walletDetail));
    }

    /**
     * 修改数据
     *
     * @param walletDetail 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody WalletDetail walletDetail) {
        return success(this.walletDetailService.updateById(walletDetail));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.walletDetailService.removeByIds(idList));
    }
}

