package com.example.myapplication.model;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private int stock;
    private double rating;
    private int reviewCount;
    
    // 二手物品相关字段
    private int sellerId; // 发布者ID
    private String sellerName; // 发布者名称
    private Date publishTime; // 发布时间
    private String condition; // 使用状态（全新、九成新、八成新等）
    private String location; // 位置信息
    private double latitude; // 纬度
    private double longitude; // 经度
    private String contactPhone; // 联系电话
    private String itemType; // 物品类型（全新/二手）

    public Product() {
        this.publishTime = new Date();
        this.itemType = "二手"; // 默认二手
    }

    public Product(int id, String name, String description, double price, String imageUrl, String category, int stock, double rating, int reviewCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.stock = stock;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.publishTime = new Date();
        this.itemType = "二手"; // 默认二手
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", condition='" + condition + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
