package com.bruce.threading;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;


/*
*对于需要加载大量内容的页面（如商品信息、用户评论、商品图片等），
* 我们可以通过多线程并行加载内容，提升页面渲染速度。
* */
@Service
public class EcommerceOptimization {

    // 模拟商品信息加载
    public void loadProductInfo() {
        System.out.println("Loading product info...");
        try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Product info loaded.");
    }

    // 模拟商品评论加载
    public void loadUserComments() {
        System.out.println("Loading user comments...");
        try { Thread.sleep(700); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("User comments loaded.");
    }

    // 模拟商品图片加载
    public void loadProductImages() {
        System.out.println("Loading product images...");
        try { Thread.sleep(800); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Product images loaded.");
    }

    // 页面渲染优化：并行加载商品信息、评论和图片
    public void renderPage() {
        ExecutorService executor = Executors.newFixedThreadPool(3);  // 线程池，用于并行处理

        // 提交任务到线程池
        Future<?> productInfoFuture = executor.submit(() -> loadProductInfo());
        Future<?> userCommentsFuture = executor.submit(() -> loadUserComments());
        Future<?> productImagesFuture = executor.submit(() -> loadProductImages());

        try {
            // 等待所有任务完成
            productInfoFuture.get();
            userCommentsFuture.get();
            productImagesFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();  // 关闭线程池
        }

        System.out.println("Page rendering completed.");
    }

    // 请求分流与负载均衡
    public void handleRequest(int requestId) {
        // 模拟负载均衡，将请求分配给不同的线程池
        ExecutorService executor = Executors.newFixedThreadPool(5);  // 每个线程池最多 5 个线程

        // 根据请求ID决定请求处理的方式（例如不同的请求分配到不同的线程池）
        if (requestId % 2 == 0) {
            System.out.println("Request " + requestId + " handled by server 1.");
            executor.submit(() -> processRequest(requestId));
        } else {
            System.out.println("Request " + requestId + " handled by server 2.");
            executor.submit(() -> processRequest(requestId));
        }

        executor.shutdown();  // 关闭线程池
    }

    // 处理用户请求（模拟任务处理）
    public void processRequest(int requestId) {
        System.out.println("Processing request " + requestId + "...");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Request " + requestId + " processed.");
    }

    public static void main(String[] args) {
        EcommerceOptimization ecommerceOptimization = new EcommerceOptimization();

        // 示例 1: 页面渲染优化
        ecommerceOptimization.renderPage();  // 并行加载商品信息、评论、图片等

        // 示例 2: 请求分流与负载均衡
        // 模拟 10 个用户请求
        for (int i = 1; i <= 10; i++) {
            ecommerceOptimization.handleRequest(i);
        }
    }
}
