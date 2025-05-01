package com.bruce.entity;

public class SecurityConsts {

    public static final String LOGIN_SALT = "question-admin";

    /**
     * request请求头属性
     */
    public static final String REQUEST_AUTH_HEADER="Authorization";

    /**
     * JWT-account
     */
    public static final String ACCOUNT = "account";

    /**
     * 组织ID
     */
    public static final String ORG_ID_TOKEN = "orgIdToken";

    /**
     * Shiro redis 前缀
     */
    public static final String PREFIX_SHIRO_CACHE = "question-admin:cache:";

    /**
     * redis-key-前缀-shiro:refresh_token
     */
    public final static String PREFIX_SHIRO_REFRESH_TOKEN = "question-admin:refresh_token:";

    /**
     * redis-key-前缀-shiro:refresh_check
     */
    public final static String PREFIX_SHIRO_REFRESH_CHECK = "question:refresh_check:";

    /**
     * JWT-currentTimeMillis
     */
    public final static String CURRENT_TIME_MILLIS = "currentTimeMillis";

    public final static String HIDDEN_PASSWORD="******";

}
