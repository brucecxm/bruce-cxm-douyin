package com.bruce.yewu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dtos.Order;
import com.bruce.entity.Users;
import com.bruce.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/excels")
public class excelImportController {
    @Autowired
    private UsersService usersService;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 核心线程数
        executor.setMaxPoolSize(20); // 最大线程数
        executor.setQueueCapacity(100); // 队列容量
        executor.setThreadNamePrefix("excel-import-");
        executor.initialize();
        return executor;
    }

    @GetMapping("/{id}")
    public String Setnxs(@PathVariable int id) {
        int pageSize = 1000; // 每页查询1000条数据
        int currentPage = 1;
        boolean hasMoreData = true;
        // 获取 MemoryMXBean
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        // 查询之前获取堆内存使用情况
        MemoryUsage beforeHeapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long beforeUsedHeapMemory = beforeHeapMemoryUsage.getUsed(); // 查询前的堆内存使用量

        if(id==1)
        {
            while (hasMoreData) {
                Page<Users> page = new Page<>(currentPage, pageSize);
                QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
                // 添加查询条件（如果有的话）
                // queryWrapper.eq("column_name", value);

                Page<Users> userPage = usersService.page(page, queryWrapper);
                List<Users> users = userPage.getRecords();

                if (users.isEmpty()) {
                    hasMoreData = false;
                } else {
                    // 提交查询任务到线程池
                    taskExecutor().execute(() -> processUsers(users));
                    currentPage++;
                }
            }
        }else {
            // 查询操作
            List<Users> users = usersService.list(); // 查询数据
for(Users users1:users)
{
    System.out.println(users1);
}
        }






        // 查询之后获取堆内存使用情况
        MemoryUsage afterHeapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long afterUsedHeapMemory = afterHeapMemoryUsage.getUsed(); // 查询后的堆内存使用量

        // 计算查询操作消耗的内存
        long usedMemoryForQuery = afterUsedHeapMemory - beforeUsedHeapMemory;

        // 打印内存使用情况
        System.out.println("Used Heap Memory before query: " + beforeUsedHeapMemory / (1024 * 1024) + " MB");
        System.out.println("Used Heap Memory after query: " + afterUsedHeapMemory / (1024 * 1024) + " MB");
        System.out.println("Memory used for query: " + usedMemoryForQuery / (1024 * 1024) + " MB");
return "ss";

    }
    private void processUsers(List<Users> users) {
        // 处理查询结果
        for (Users user : users) {
            // 处理每个用户
            System.out.println(user);
        }
    }


}
