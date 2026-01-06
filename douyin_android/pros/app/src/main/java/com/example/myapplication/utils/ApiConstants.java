package com.example.myapplication.utils;

public class ApiConstants {
    // 后端服务地址
    public static final String BASE_URL = "http://10.7.27.169:7400";
    
    // 登录接口
    public static final String LOGIN_URL = "/user/doLogin";
    
    // 注册接口
    public static final String REGISTER_URL = "/user/register";
    
    // 用户信息接口
    public static final String USER_INFO_URL = "/user/userInfo";
    
    // 更新用户信息接口
    public static final String UPDATE_USER_URL = "/user/update";
    
    // 商品相关接口
    public static final String CREATE_SHOP_URL = "/shop/create";
    public static final String SHOP_LIST_URL = "/shop/list";
    public static final String SHOP_DETAIL_URL = "/shop/detail";
    public static final String SHOP_LIST_BY_TYPE_URL = "/shop/list/type";
    public static final String SHOP_SEARCH_URL = "/shop/search";
    
    // 文件上传相关接口
    public static final String UPLOAD_IMAGE_URL = "/file/upload/image";
    
    // 购物车相关接口
    public static final String ADD_TO_CART_URL = "/cart/add";
    public static final String GET_CART_LIST_URL = "/cart/list";
    public static final String UPDATE_CART_URL = "/cart/update";
    public static final String DELETE_CART_ITEM_URL = "/cart/delete";
    public static final String CLEAR_CART_URL = "/cart/clear";
    public static final String UPDATE_CART_STATUS_URL = "/cart/update/status";
    public static final String UPDATE_CART_ALL_STATUS_URL = "/cart/update/all/status";
    public static final String GET_SELECTED_CART_URL = "/cart/selected";
    
    // 订单相关接口
    public static final String CREATE_ORDER_URL = "/orders/create";
    public static final String GET_ORDERS_URL = "/orders/list";
    public static final String GET_ORDERS_BY_STATUS_URL = "/orders/list/status";
    public static final String GET_ORDER_DETAIL_URL = "/orders/detail/";
    public static final String UPDATE_ORDER_STATUS_URL = "/orders/update/status";
    public static final String CANCEL_ORDER_URL = "/orders/cancel/";
    public static final String CONFIRM_ORDER_URL = "/orders/confirm/";
}
