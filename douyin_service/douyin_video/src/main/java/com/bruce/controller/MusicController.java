package com.bruce.controller;



import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.dao.musiconeDao;
import com.bruce.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Music)表控制层
 *
 * @author makejava
 * @since 2024-06-25 18:10:38
 */
@RestController
@RequestMapping("/music")
public class MusicController extends ApiController {


    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);





@Autowired
private musiconeDao musiconedao;

    /**
     * 服务对象
     */
    @Resource
    private MusicService musicService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param music 查询实体
     * @return 所有数据
     */

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.musicService.getById(id));
    }

    @GetMapping("/getmusic")
    public Map selectOne( String music_id) {

        List<Map> one=musiconedao.one(music_id);

List<String> videoArr=new ArrayList<String>();
for(Map a:one) {
   String ss= String.valueOf(a.get("video_img"));
   videoArr.add(ss);
}

Map oneww =new HashMap();

oneww.put("videoimg",videoArr);
oneww.put("music_info",one.get(0));

return oneww;

    }

    /**
     * 新增数据
     *
     * @param music 实体对象
     * @return 新增结果
     */
//    @PostMapping
//    public R insert(@RequestBody Music music) {
//        return success(this.musicService.save(music));
//    }

    /**
     * 修改数据
     *
     * @param music 实体对象
     * @return 修改结果
     */
//    @PutMapping
//    public R update(@RequestBody Music music) {
//        return success(this.musicService.updateById(music));
//    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.musicService.removeByIds(idList));
    }
}

