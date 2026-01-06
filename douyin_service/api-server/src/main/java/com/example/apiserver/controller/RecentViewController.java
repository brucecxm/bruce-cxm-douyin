package com.example.apiserver.controller;

import com.example.apiserver.common.Response;
import com.example.apiserver.entity.RecentView;
import com.example.apiserver.service.RecentViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recent-view")
public class RecentViewController {

    @Autowired
    private RecentViewService recentViewService;

    /**
     * 添加或更新最近浏览记录
     */
    @PostMapping
    public Response<RecentView> addRecentView(@RequestBody RecentViewService.RecentViewRequest recentViewRequest) {
        try {
            RecentView recentView = recentViewService.addRecentView(
                    recentViewRequest.getUserId(),
                    recentViewRequest.getProductId()
            );
            return Response.success("添加浏览记录成功", recentView);
        } catch (Exception e) {
            return Response.error("添加浏览记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户最近浏览记录
     */
    @GetMapping("/{userId}")
    public Response<List<RecentView>> getRecentViews(@PathVariable Long userId) {
        try {
            List<RecentView> recentViews = recentViewService.getRecentViewsByUserId(userId);
            return Response.success(recentViews);
        } catch (Exception e) {
            return Response.error("获取浏览记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户最近浏览的前N条记录
     */
    @GetMapping("/{userId}/top/{limit}")
    public Response<List<RecentView>> getTopRecentViews(@PathVariable Long userId, @PathVariable int limit) {
        try {
            List<RecentView> recentViews = recentViewService.getTopRecentViewsByUserId(userId, limit);
            return Response.success(recentViews);
        } catch (Exception e) {
            return Response.error("获取浏览记录失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户的指定商品浏览记录
     */
    @DeleteMapping
    public Response<Void> deleteRecentView(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            recentViewService.deleteRecentView(userId, productId);
            return Response.<Void>success("删除浏览记录成功", null);
        } catch (Exception e) {
            return Response.error("删除浏览记录失败: " + e.getMessage());
        }
    }

    /**
     * 清空用户的最近浏览记录
     */
    @DeleteMapping("/{userId}")
    public Response<Void> clearRecentViews(@PathVariable Long userId) {
        try {
            recentViewService.clearRecentViews(userId);
            return Response.<Void>success("清空浏览记录成功", null);
        } catch (Exception e) {
            return Response.error("清空浏览记录失败: " + e.getMessage());
        }
    }
}