package com.example.apiserver.controller;

import com.example.apiserver.common.Response;
import com.example.apiserver.entity.Favorite;
import com.example.apiserver.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public Response<Favorite> addFavorite(@RequestBody FavoriteService.FavoriteRequest favoriteRequest) {
        try {
            Favorite createdFavorite = favoriteService.addFavorite(favoriteRequest.getUserId(), favoriteRequest.getProductId());
            return Response.success("收藏成功", createdFavorite);
        } catch (Exception e) {
            return Response.error("收藏失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public Response<List<Favorite>> getFavoritesByUserId(@PathVariable Long userId) {
        try {
            List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
            return Response.success(favorites);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @DeleteMapping
    public Response<Void> removeFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            favoriteService.removeFavorite(userId, productId);
            return Response.success();
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @GetMapping("/check")
    public Response<Boolean> isFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            Boolean isFavorite = favoriteService.isFavorite(userId, productId);
            return Response.success(isFavorite);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }
}