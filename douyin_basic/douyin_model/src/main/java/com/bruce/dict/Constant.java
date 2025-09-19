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
        public static final String PASSWORD = "password";
        public static final String ISCHECK = "isCheck";
        public static final String VERIFICATION_CODE = "verificationCode";
        public static final String EMAIL_VERIFICATION_CODE = "emailVerificationCode";
        public static final String USER_AVATAR = "userAvatar";
        public static final String USER_INFO = "userInfo";
        public static final String ROLE_LIST = "roleList";
        public static final String PERMISSION_LIST = "permissionList";
        public static final String DEFAULT_USER_AVATAR = "default_user_avatar";
        public static final String DEFAULT_USER_BACK_IMG = "default_user_back_img";
        public static final String EMAIL_CODE_PREFIX = "email:code:"; // 补全邮箱验证码前缀常量
        public static final String SA_TOKEN = "sa-token"; // 补全 SA-Token 常量
        public static final String LOGIN_TYPE = "loginType"; // 补全 SA-Token 常量
        public static final String TOKEN = "token"; // 补全 SA-Token 常量
        public static final String _USER_INFO = "_user_info"; // 补全 SA-Token 常量
        public static final String JWT = "jwt"; // 补全 SA-Token 常量
        // Redis缓存键前缀
        public static final String USER_ROLES_PREFIX = "user:roles:"; // 用户角色缓存前缀
        public static final String USER_PERMISSIONS_PREFIX = "user:permissions:"; // 用户权限缓存前缀
        // 会话中角色和权限的存储键
        public static final String SESSION_ROLES_KEY = "session_roles"; // 会话中角色存储键
        public static final String SESSION_PERMISSIONS_KEY = "session_permissions"; // 会话中权限存储键
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
        public static final String HASH_TRANSFORM_ERROR = "哈希转化错误"; // 补全错误常量
        public static final String USER_REPEAT = "用户名已经被占用";
        public static final String USER_INVALID = "请核对用户名是否正确";
        public static final String LOGIN_FAILED = "登录失败"; // 补全最后一个错误常量
        public static final String CAPTCHA_ERROR = "验证码错误"; // 验证码错误常量
        public static final String INVALID_CREDENTIALS = "用户名或密码错误"; // 用户名或密码错误常量
    }


}
