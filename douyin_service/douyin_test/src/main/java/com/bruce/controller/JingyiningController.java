package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.Jingyining;
import com.bruce.service.JingyiningService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (Jingyining)表控制层
 *
 * @author makejava
 * @since 2025-05-12 19:36:28
 */
@RestController
@RequestMapping("/test")
public class JingyiningController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private JingyiningService jingyiningService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param jingyining 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Jingyining> page, Jingyining jingyining) {
        return success(this.jingyiningService.page(page, new QueryWrapper<>(jingyining)));
    }




    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.jingyiningService.getById(id));
    }






    @GetMapping("/cha")
    public R selectAllaa() {
        List one = this.jingyiningService.list();


        return success(one);
    }


    /**
     * 新增数据
     *
     * @param jingyining 实体对象
     * @return 新增结果
     */
    @PostMapping("/zeng")
    public R insert(@RequestBody Jingyining jingyining) {

        Jingyining a=new Jingyining();

        int b;

        List c=new ArrayList();

        return success(this.jingyiningService.save(jingyining));
    }

    /**
     * 修改数据
     *
     * @param jingyining 实体对象
     * @return 修改结果
     */
    @PostMapping("/gai")
    public R update(@RequestBody Jingyining jingyining) {
//        LambdaQueryWrapper queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(Jingyining::getId,jingyining.getId());
//        this.jingyiningService.update();
//
        return success(this.jingyiningService.updateById(jingyining));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @PostMapping("/shan")
    public R delete(@RequestBody Jingyining jingyining) {
//        LambdaQueryWrapper queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(Jingyining::getId,jingyining.getId());
//        this.jingyiningService.remove(queryWrapper);
//
        this.jingyiningService.removeById(jingyining);

        return success("sss");
    }
}

