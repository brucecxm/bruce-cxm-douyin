package com.bruce.baguwen;

import com.bruce.dtos.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mysql")
public class mysqlController {



    //todo  这里演示sql的调优

    @PostMapping
    public void Setnx(@RequestBody Order order) {

        /*
         * 1.不用select *
         * */

/*
* 2.数据量小的表驱动数据量大的表 减少数据量的计算
* 因为有一个连接缓冲区 表连接先把主表的某些数据放入缓冲区与附表连接
* 如果主表越小  添加缓冲区的过程就越快
* */


        /*
        * 3.用连接查询替代子查询  因为有子查询的sql其实是执行两次sql查询 比连表查询慢  而且连接查询更容易能用到索引
        * */


        /*
        * 4.提升group by 的效率 如果group by没有索引 会非常慢  因此可以给group by 的数据加一个索引
        * */


        /*
        * 5.批量插入多条数据的情况下   用循环非常慢    其实sql提供了批量插入的sql语句  mybatisplus也提供了  insertBatch
        * 每批数据尽量500条以内  不然会分批次插入
        * */


        /*
        * 6.使用limit 能增快查询的速度  但是一般会限制页数  因为对于百万级别的表  页数太后 查询速度非常慢
        * 而且对于百万级别的数据 查询后面的数据的概率比较小 因为一般都是历史数据之类的  匹配度不高
        * 一般都是用查询条件查询历史数据
        *
        * 6.1 利用自增id索引 如果id自增 加一个where id 》1000 limit 10 这样更快
        *
        * */



        /*
        * 用union all 不用 union  因为 union会去重 使得速度变慢
        * */


        /*
        *
        * join的表不能太多  因为查询效率会下降
        * */


    }









}
