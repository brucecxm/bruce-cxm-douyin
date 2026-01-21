# 接口分析：getVideoPage

## 接口概述

- **URL**: `/video/getVideoPage`
- **请求方式**: GET
- **功能**: 分页获取视频列表

## 查询条件分析

接口接收三个参数：

- `page`: 页码，默认1
- `size`: 每页数量，默认4
- `videoType`: 视频类型（必填）

## 核心查询逻辑

```java
// 第231-233行：查询条件构建
LambdaQueryWrapper<Video> videoQueryWrapper = new LambdaQueryWrapper<Video>()
        .eq(Video::getType, videoType)  // 仅按视频类型过滤
        .orderByDesc(Video::getVideoId); // 按视频ID降序排序

// 第234行：执行分页查询
IPage<Video> videoPage = videoService.page(pageInfo, videoQueryWrapper);
```

**关键观察点**：

- 查询条件**仅包含 `videoType`**，没有添加 `Video::getUserId` 相关的过滤条件
- 因此，查询结果包含**所有用户**发布的、符合视频类型条件的视频

## 登录用户ID的用途

第238-252行获取当前登录用户ID，目的是：

- 用于后续判断**当前用户是否点赞该视频**（第302-316行）
- 用于后续判断**当前用户是否收藏该视频**（第318-333行）
- 并非用于过滤视频列表

## 结论

**该接口不是只能查询当前用户发布的视频**，而是可以查询**所有用户**发布的、符合指定视频类型条件的视频。
