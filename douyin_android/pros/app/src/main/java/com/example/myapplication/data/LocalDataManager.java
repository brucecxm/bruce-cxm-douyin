package com.example.myapplication.data;

import com.example.myapplication.model.CartItem;
import com.example.myapplication.model.CartRequest;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.Product;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.ApiConstants;
import com.example.myapplication.utils.OkHttpManager;
import com.example.myapplication.model.ApiResponse;

import java.util.ArrayList;
import java.util.List;

public class LocalDataManager {
    private static LocalDataManager instance;
    private List<Product> products;
    private List<CartItem> cartItems;
    private List<Order> orders;
    private List<User> users;
    private User currentUser;
    private int orderIdCounter = 1;
    private int productIdCounter = 17; // 从17开始，因为已有16个商品
    private int userIdCounter = 2;

    private LocalDataManager() {
        initializeUsers();
        initializeProducts();
        cartItems = new ArrayList<>();
        orders = new ArrayList<>();
        // 设置默认登录用户
        this.currentUser = users.get(0);
    }

    public static synchronized LocalDataManager getInstance() {
        if (instance == null) {
            instance = new LocalDataManager();
        }
        return instance;
    }

    private void initializeUsers() {
        users = new ArrayList<>();
        // 添加测试用户
        users.add(new User(1, "user1", "123456", "13800138000"));
        users.get(0).setNickname("测试用户1");
        users.get(0).setLocation("北京市朝阳区");
        users.get(0).setLatitude(39.9042);
        users.get(0).setLongitude(116.4074);
    }

    private void initializeProducts() {
        products = new ArrayList<>();
        
        // 电子产品
        Product product1 = new Product(1, "iPhone 15 Pro", "最新款iPhone，配备A17 Pro芯片", 9999.0, "https://via.placeholder.com/300x300?text=iPhone+15+Pro", "电子产品", 50, 4.8, 128);
        product1.setSellerId(1);
        product1.setSellerName("测试用户1");
        product1.setCondition("全新");
        product1.setLocation("北京市朝阳区");
        product1.setLatitude(39.9042);
        product1.setLongitude(116.4074);
        product1.setContactPhone("13800138000");
        products.add(product1);
        
        Product product2 = new Product(2, "MacBook Pro 16", "专业级笔记本电脑，M3 Pro芯片", 18999.0, "https://via.placeholder.com/300x300?text=MacBook+Pro", "电子产品", 30, 4.9, 95);
        product2.setSellerId(1);
        product2.setSellerName("测试用户1");
        product2.setCondition("全新");
        product2.setLocation("北京市朝阳区");
        product2.setLatitude(39.9042);
        product2.setLongitude(116.4074);
        product2.setContactPhone("13800138000");
        products.add(product2);
        
        // 服装
        Product product3 = new Product(5, "Nike Air Max", "经典运动鞋，舒适透气", 899.0, "https://via.placeholder.com/300x300?text=Nike+Air+Max", "服装", 200, 4.5, 342);
        product3.setSellerId(1);
        product3.setSellerName("测试用户1");
        product3.setCondition("九成新");
        product3.setLocation("北京市朝阳区");
        product3.setLatitude(39.9042);
        product3.setLongitude(116.4074);
        product3.setContactPhone("13800138000");
        products.add(product3);
        
        // 家居用品
        Product product4 = new Product(9, "戴森吸尘器", "无线手持，强力清洁", 2999.0, "https://via.placeholder.com/300x300?text=戴森吸尘器", "家居用品", 25, 4.8, 67);
        product4.setSellerId(1);
        product4.setSellerName("测试用户1");
        product4.setCondition("八成新");
        product4.setLocation("北京市朝阳区");
        product4.setLatitude(39.9042);
        product4.setLongitude(116.4074);
        product4.setContactPhone("13800138000");
        products.add(product4);
        
        // 图书
        Product product5 = new Product(13, "《三体》", "刘慈欣科幻小说", 39.0, "https://via.placeholder.com/300x300?text=三体", "图书", 500, 4.9, 1250);
        product5.setSellerId(1);
        product5.setSellerName("测试用户1");
        product5.setCondition("全新");
        product5.setLocation("北京市朝阳区");
        product5.setLatitude(39.9042);
        product5.setLongitude(116.4074);
        product5.setContactPhone("13800138000");
        products.add(product5);
    }

    // User相关方法
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (Integer.parseInt(user.getId()) == id) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public void addUser(User user) {
        user.setId(String.valueOf(userIdCounter++));
        users.add(user);
    }

    // Product相关方法
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                result.add(product);
            }
        }
        return result;
    }

    public Product getProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        for (Product product : products) {
            if (!categories.contains(product.getCategory())) {
                categories.add(product.getCategory());
            }
        }
        return categories;
    }

    // 添加发布物品方法
    public void publishProduct(Product product) {
        product.setId(productIdCounter++);
        product.setSellerId(Integer.parseInt(currentUser.getId()));
        product.setSellerName(currentUser.getNickname());
        product.setLocation(currentUser.getLocation());
        product.setLatitude(currentUser.getLatitude());
        product.setLongitude(currentUser.getLongitude());
        product.setContactPhone(currentUser.getPhone());
        products.add(product);
    }

    // 获取用户发布的物品
    public List<Product> getProductsBySeller(int sellerId) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getSellerId() == sellerId) {
                result.add(product);
            }
        }
        return result;
    }

    // 购物车相关方法
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }
    
    // 从后端获取购物车列表
    public void fetchCartItems(OkHttpManager.NetworkCallback<ApiResponse> callback) {
        OkHttpManager.get(ApiConstants.GET_CART_LIST_URL, null, ApiResponse.class, callback);
    }

    // 添加商品到购物车（本地+后端）
    public void addToCart(Product product, int quantity) {
        // 本地添加
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(product, quantity));
        
        // 后端添加
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId((long) product.getId());
        cartRequest.setProductName(product.getName());
        cartRequest.setProductPrice((int) product.getPrice());
        cartRequest.setProductImg(product.getImageUrl());
        cartRequest.setQuantity(quantity);
        cartRequest.setSelected(1); // 默认选中
        
        OkHttpManager.postJson(ApiConstants.ADD_TO_CART_URL, cartRequest, ApiResponse.class, new OkHttpManager.NetworkCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                // 处理成功响应
            }
            
            @Override
            public void onFailure(String error) {
                // 处理失败响应
            }
        });
    }

    // 从购物车移除商品（本地+后端）
    public void removeFromCart(int productId) {
        // 本地移除
        cartItems.removeIf(item -> item.getProduct().getId() == productId);
        
        // 后端移除
        // 这里需要实现后端删除购物车商品的API调用
    }

    // 更新购物车商品数量（本地+后端）
    public void updateCartItemQuantity(int productId, int quantity) {
        // 本地更新
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                if (quantity <= 0) {
                    cartItems.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
        
        // 后端更新：注意后端接口是/cart/update/quantity，需要传递cartId和quantity参数
        // 这里需要获取购物车ID，然后调用后端API
    }

    public void clearCart() {
        // 本地清空
        cartItems.clear();
        
        // 后端清空
        // 这里需要实现后端清空购物车的API调用
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getCartItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    // 订单相关方法
    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public void addOrder(Order order) {
        order.setId(orderIdCounter++);
        orders.add(order);
    }

    public Order getOrderById(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    public void updateOrderStatus(int orderId, String status) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
        }
    }
}
