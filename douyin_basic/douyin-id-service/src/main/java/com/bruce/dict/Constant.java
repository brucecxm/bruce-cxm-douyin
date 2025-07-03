package com.bruce.dict;

/**
 * 项目通用常量类，使用静态内部类分类管理，方便维护与可读性
 */
public class Constant {

    /**
     * 🟩 接口参数字段常量
     */
    public static class ApiParam {
        public static final String TYPE = "Type";
        public static final String VIDEO_TYPE = "videoType";
        public static final String CONTENT_ID = "contentId";
        public static final String CONTENT_TYPE = "contentType";
        public static final String COMMENT = "comment";
        public static final String CONTENT = "content";
        public static final String FATHER_ID = "fatherId";
        public static final String URL = "url";
        public static final String VIDEO_ID = "videoId";
        public static final String USER_ID = "userId";
        public static final String MUSIC_ID = "musicId";
        public static final String PAGE = "page";
        public static final String LIMIT = "limit";
    }

    /**
     * 🟩 视频相关字段常量
     */
    public static class Video {
        public static final String VIDEO_URL = "videoUrl";
        public static final String VIDEO_CONTENT = "videoContent";
        public static final String VIDEO_PAGE_LIST = "videoPageList";
        public static final String TOTAL = "total";
    }

    /**
     * 🟩 用户相关字段常量
     */
    public static class User {
        public static final String USER_NAME = "userName";
        public static final String USER_AVATAR = "userAvatar";
        public static final String USER_INFO = "userInfo";
        public static final String DEFAULT_USER_AVATAR = "default_user_avatar";
        public static final String DEFAULT_USER_BACK_IMG = "default_user_back_img";
    }

    /**
     * 🟩 音乐相关字段常量
     */
    public static class Music {
        public static final String MUSIC = "music";
        public static final String MUSIC_AVATAR = "musicAvatar";
    }

    /**
     * 🟩 统计字段常量（点赞数、评论数等）
     */
    public static class Stat {
        public static final String LIKE_NUM = "likeNum";
        public static final String COMMENT_NUM = "commentNum";
        public static final String COLLECT_NUM = "collectNum";
        public static final String SHARE_NUM = "shareNum";
    }

    /**
     * 🟩 统计字段常量（点赞数、评论数等）
     */
    public static class Role {
        public static final String USER = "user";
    }

    /**
     * 🟩 统计字段常量（点赞数、评论数等）
     */
    public static class Chinese {
        public static final String NEW_USER = "新用户";
    }

    /**
     * 🟩 统计字段常量（点赞数、评论数等）
     */
    public static class Error {
        public static final String USER_REPEAT = "用户名已经被占用";
    }



}
