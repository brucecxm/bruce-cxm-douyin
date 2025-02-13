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
        如果有一个线程查询缓存 发现数据逻辑过期 那么会获取互斥锁  然后新建一个线程 去查询数据库完成同步并释放锁
        然后主线程返回过期数据

        此时另外一个线程查询缓存 发现逻辑过期 无法获取锁 直接返回过期数据 不会被阻塞
         */


        List<Order> orders = orderoneService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }


    @PostMapping("/createOrder ")
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
    //todo  这里解决并演示redis和mysql的双写一致性的问题
    //todo  这里解决并演示redis和mysql的双写一致性的问题
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {

        /*
         * 修改了数据库的数据也要同时更新缓存的数据， 缓存和数据库的数据要保持一致
         *
         * 极大的控制了脏数据的风险 但是也只是控制了一部分  因为延时的过程中  会有大量的请求过来 导致脏数据的产生
         * 主要是延时的时间不好控制
         *
         * */

//如果不是强一致的情况下

        /*
         * 读操作：缓存命中，直接返回 缓存未命中查询数据库 写入缓存 设定超时时间
         * */

        /*
         * 写操作：采用延时双删 ---》先删除缓存 再修改数据库  再（延时一会）删除缓存
         * ---》双删的目的是因此先删除数据库 或者删除缓存都会出问题 会出现脏数据 所以要删除两次缓存
         * ---》延时的目的是因为redis一般是主从同步的结构 所以要延时一会等待主从同步再删除
         * */

//如果是强一致的情况下
 /*
 * 采用分布式锁：强一致 性能低
 * */

      /*  因为一般存入redis的数据 都是（读多写少）的数据，所以出现了两把锁
                共享锁--》 读锁 readlock 加锁后 其他线程可以共享读操作
                排他锁--》  独占锁writelock 加锁之后 ，阻塞其他线程读写操作

                这两把锁 redission都有提供 直接引入依赖  调用即可

*/

//        如果是允许短暂的不一致的情况下 用到mq  一定要保证mq消息的可靠性  或者用canal 重点掌握mq的  这个策略是最常用到的


        orderoneService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }




    //todo 这里演示解释redis持久化的问题
    //todo 这里演示解释redis持久化的问题
    //todo 这里演示解释redis持久化的问题
    // 获取所有订单
    @GetMapping("/chijiuhua")
    public ResponseEntity<List<Order>> chijiuhua() {


/*
*
* RDB：记录数据  会记录redis的所有数据到一个rdb文件 如果redis重启之类的  会到rdb读取数据
*
* */

  /*
  *
  * AOP:记录命令（默认关闭 需要配置开启）  文件比较小 因为只记录命令
  *
  * */






        List<Order> orders = orderoneService.getAllOrders();
        return ResponseEntity.ok(orders);
    }




    //todo 这里演示解释缓存过期策略
    //todo 这里演示解释缓存过期策略
    //todo 这里演示解释缓存过期策略

    // 获取所有订单
    @GetMapping("/guoqi")
    public ResponseEntity<List<Order>> guoqi() {


        /*
        * redis的数据过期之后 需要将数据从内存中删除 可以按照不同的规则进行删除  这种规则叫数据删除策略（数据过期策略）
        * */


        /*
        * 1.惰性删除：key过期之后不去管他 知道有人访问这个key发现过期了再删除
        * 对cpu友好 不用重复执行多余的操作
        * 对内存不友好  会占用大量内存
        * */

        /*
        * 2.定期删除：
        * */


        List<Order> orders = orderoneService.getAllOrders();
        return ResponseEntity.ok(orders);
    }






    //todo 这里演示解释缓存数据淘汰策略

    // 获取所有订单
    @GetMapping("/taotai")
    public ResponseEntity<List<Order>> taotai() {

        List<Order> orders = orderoneService.getAllOrders();
        return ResponseEntity.ok(orders);
    }



    //todo 这里演示解释redis的集群
    //todo 这里演示解释redis的集群
    //todo 这里演示解释redis的集群

    @GetMapping("/jiqun")
    public ResponseEntity<List<Order>> jiqun() {

        /*
        * 主从复制：单节点redis的并发能力是有上线的 要提高并发能力 就要搭建主从集群 实现读写分离 、
        * 一般主节点负责写数据 从节点负责读数据
        *  主节点 master  从节点 slave
        *
        * 主从全量同步：slave执行replicaof给master请求数据同步 master判断是否是第一次同步 如果是第一次 返回master的数据版本信息
        * 给slave  slave保存版本信息  然后master执行bgsave 生成rdb文件发送给slave  slave情况本地数据 加载rdb文件
        * （重点：在同步过程中 主节点也会收到别的命令 在整个同步过程中 主节点会记录rdb期间的所有命令 发送给slave  slave在执行完rdb后
        * 会执行master发功过来的repl_baklog命令 完整全量同步）
        *
        *
        * 主从增量同步：slave重启 后者后期数据变化  slave重启后 发送psync replid offset  master判断replid是否一致
        * 如果一致 则不是第一次 增量同步 回复cotinue  然后master去repl_baklog获取offset后的数据 发送offset后的命令给
        * slave  然后slave执行命令
        *
        * */




        /*
           主从模式有一个缺点 无法保证redis 的高可用  如果主节点丧失功能 redis集群丧失了写数据的能力 因此提供了哨兵模式解决这个问题
         * 哨兵模式： redis提供了哨兵（sentinel）机制来实现主从集群的自动故障恢复
         *
         * 一般至少会提供三个哨兵  哨兵也是一个redis节点 是由多个redis组成的集群

         *
          功能1：监控  sentinel会不断检查master或者slave是否按照预期工作
          功能2：自动故障恢复 如果master故障 sentinel会将一个slave提升未master  故障的master恢复之后依然以新的master为主
          功能3：通知 sentinel会将新的master推给redis客户端 确保客户端能正确连接到新的redis的master节点


         * */



        /*
         前面两个有两个问题没解决  1.海量数据存储问题  2.大量的写请求
         * 分片集群：redis集群中有多个master 每个master存储不同的数据  每个master有多个从节点  master相互之间会执行ping命令检查
         是否可用  redisclient的请求会被正确路由到对应的master

         如何路由：（哈希槽）  每个master都会有一个范围 比如0-1000 1000-2000 2000-3000  存数据的时候 会通过哈希计算
         将数据存入对应的范围的master  读数据同样 计算哈希值 到对应的master对应的slave读数据
         * */


        List<Order> orders = orderoneService.getAllOrders();
        return ResponseEntity.ok(orders);
    }




}
