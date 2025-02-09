package com.bruce.baguwen;


import com.bruce.config.RedissonConfig;
import com.bruce.dtos.Order;
import com.bruce.service.OrderService;
import com.bruce.service.OrderoneService;
import com.bruce.utils.RedisUtils;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class redisController {

    @Autowired
    private OrderoneService orderoneService;
    @Autowired
    private RedisUtils redisUtils;


    //todo 这里用的是restful风格  增删改查
    //todo 这里用的是restful风格  增删改查
    //todo 这里用的是restful风格  增删改查

    // 获取所有订单
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderoneService.getAllOrders();
        return ResponseEntity.ok(orders);
    }


    //todo  这里演示redis分布式锁的使用
    //todo  这里演示redis分布式锁的使用
    //todo  这里演示redis分布式锁的使用

    @PostMapping
    public ResponseEntity<Order> Setnx(@RequestBody Order order) {

        /*
         * 1.常规的redis使用分布式锁的方法
         * */


        /*
         * 2.redission实现分布式锁的方法
         * */


        Order createdOrder = orderoneService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }


    // 根据订单ID获取单个订单
    //todo  这里解决并演示缓存穿透的问题
    //todo  这里解决并演示缓存穿透的问题
    //todo  这里解决并演示缓存穿透的问题
    //缓存穿透：查询一个不存在的数据，mysql查询不到数据也不会直接写入缓存，就会导致每次请求都查询数据库
    //缓存穿透：查询一个不存在的数据，mysql查询不到数据也不会直接写入缓存，就会导致每次请求都查询数据库
//    如果有黑客知道了这种链接  可以发起非常大的并发请求到达数据库 使得数据库崩溃
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {

        int method = 1;   //0为常规  1为解法1  2为解法2


        //一般逻辑 先查redis  如果redis查不到 则查数据库 查询数据库并缓存数据
        Order order = redisUtils.getOrderById(id);
        if (order == null) {
            order = orderoneService.getOrderById(id);
            redisUtils.putOrder(order);
        }

        //1.解法1----》缓存空数据，查询返回的结果为空，任把这个空结果进行缓存  容易数据不一致 太消耗内存  具体的缓存细节是放在redisUtils.putOrder


        //为了解决上面的消耗内存 和数据不一致的问题 因此选择用布隆过滤器替代那个问题
        //2.解法2----》设置布隆过滤器，先查布隆过滤器是否存在，存在则查redis，redis查不到查db  不存在直接返回
        //（布隆过滤器的原理 是利用位图这个数据结果  将数据利用哈希计算存于位图 存了的就是1  没存的就是0）
        //ps：缓存预热的时候，要预热布隆过滤器
        //实现方式 1. Google Guava        2.Redisson

        // 创建布隆过滤器，预计存储 1000 个字符串，误判率为 0.01（1%）
        BloomFilter<String> bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8), 1000, 0.01);
        // 添加元素到布隆过滤器中   ----------》布隆过滤器只需要存储键即可
        bloomFilter.put("apple");
        bloomFilter.mightContain("apple");
        bloomFilter.mightContain("pineapple");


        // 获取 Redisson 客户端
        RedissonClient redissonClient = RedissonConfig.getRedissonClient();
        RBloomFilter<String> bloomFilterone = redissonClient.getBloomFilter("myBloomFilter");
        bloomFilterone.tryInit(1000L, 0.01);
        bloomFilterone.add("apple");
        System.out.println("apple 是否存在: " + bloomFilterone.contains("apple"));   // true
        redissonClient.shutdown();

        return ResponseEntity.ok(order);
    }


    //todo  这里解决并演示缓存击穿的问题
    //todo  这里解决并演示缓存击穿的问题
    //todo  这里解决并演示缓存击穿的问题
    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(@RequestParam String status) {
        /*
         * 缓存击穿：如果给一个key设置了过期时间，当key过期的时候，缓存有一个重建的时间 比如50毫秒 只在数据量比较大的场景下
         * 会有重建时间比较长的情况
         * 恰好这个时间点对这个key有大量的并发请求过来，这些并发请求可能会瞬间把db压垮
         * */


        long key = 1; //需要获取的key

       /* 解法1：互斥锁（分布式锁） 就是 如果有一个线程无法从redis获取值  直接上锁 然后别的线程只能等待
       特点：强一致  时间慢   如果线程2无法获取锁的话 那么它就会停止 直到锁释放
        */
        // 1. 尝试从缓存中获取数据

        Order orderById = redisUtils.getOrderById(key);
        if (orderById == null) {

        }



        /* 解法2：逻辑过期
        如果有一个线程无法从redis获取值
         */


        List<Order> orders = orderoneService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }


    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderoneService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }


    //todo  这里解决并演示缓存雪崩的问题
    //todo  这里解决并演示缓存雪崩的问题
    //todo  这里解决并演示缓存雪崩的问题

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {

        /*
         * 缓存雪崩：同一时段 ---》大量的缓存key同时失效 --》或者redis服务宕机  导致大量的请求到达数据库 带来巨大的压力
         * */

        /*
         * 解决方案：
         * 1.给不同的key添加ttl随机值
         * 2.利用redis集群提高服务的可用性   --》哨兵模式 集群模式
         * 3.给缓存业务添加降级限流策略       --》nginx   springcloudgataway    降级可以作为保底策略 适用于 雪崩 击穿  穿透
         * 4.给业务添加多级缓存              ---》guava  caffeine
         * */

        Order updatedOrder = orderoneService.updateOrder(id, order);
        return ResponseEntity.ok(updatedOrder);
    }




    //todo  这里解决并演示redis和mysql的双写一致性的问题
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderoneService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


}
