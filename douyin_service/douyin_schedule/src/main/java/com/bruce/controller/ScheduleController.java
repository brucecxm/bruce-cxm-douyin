package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.Schedule;
import com.bruce.entity.ScheduleES;
import com.bruce.service.ScheduleESRepository;
import com.bruce.service.ScheduleService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Schedule)表控制层
 *
 * @author makejava
 * @since 2025-01-22 00:37:51
 */
@RestController
@RequestMapping("schedule")
public class ScheduleController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleESRepository scheduleESRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;  // 需要注入 ElasticsearchRestTemplate



    @GetMapping("/migrate")
    public R migrateDataToElasticsearch() {

        List<Schedule> all=scheduleService.list();

        // 将数据转换为 Elasticsearch 格式并保存到 Elasticsearch
        for (Schedule product : all) {
            ScheduleES scheduleES = new ScheduleES();
            scheduleES.setId(product.getId());
            scheduleES.setName(product.getName());

            // 保存到 Elasticsearch
            scheduleESRepository.save(scheduleES);
        }
        return success("迁移完毕");
    }







    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param schedule 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Schedule> page, Schedule schedule) {

String name=schedule.getName();

        // 创建分页查询
        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", name))  // 模糊查询
                .withPageable(PageRequest.of((int) page.getCurrent() - 1, (int) page.getSize()))  // 分页查询，页码从 0 开始
                .build();

        // 返回分页结果
//        org.springframework.data.domain.Page<ScheduleES> esPage = elasticsearchRestTemplate.queryForPage(query, ScheduleES.class);

        // 返回分页数据
//        return success(esPage);


        return success(this.scheduleService.page(page, new QueryWrapper<>(schedule)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.scheduleService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param schedule 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Schedule schedule) {
        return success(this.scheduleService.save(schedule));
    }

    /**
     * 修改数据
     *
     * @param schedule 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Schedule schedule) {
        return success(this.scheduleService.updateById(schedule));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.scheduleService.removeByIds(idList));
    }
}

