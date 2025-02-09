package com.bruce;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HashTable<K, V> {
    private LinkedList<Entry<K, V>>[] table;
    private int capacity;

    // 哈希表的构造函数，初始化一个指定大小的数组
    public HashTable(int capacity) {
        this.capacity = capacity;
        table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    // 定义键值对类
    static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // 哈希函数，计算键的索引
    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // 插入操作
    public void put(K key, V value) {
        int index = hash(key);  // 计算索引
        LinkedList<Entry<K, V>> bucket = table[index];  // 获取索引位置的桶
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;  // 如果键已存在，则更新值
                return;
            }
        }
        bucket.add(new Entry<>(key, value));  // 如果键不存在，则添加新元素
    }

    // 查找操作
    public V get(K key) {
        int index = hash(key);  // 计算索引
        LinkedList<Entry<K, V>> bucket = table[index];  // 获取索引位置的桶
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;  // 找到键，返回对应的值
            }
        }
        return null;  // 如果没有找到对应的键，返回null
    }

    // 删除操作
    public void remove(K key) {
        int index = hash(key);  // 计算索引
        LinkedList<Entry<K, V>> bucket = table[index];  // 获取索引位置的桶
        bucket.removeIf(entry -> entry.key.equals(key));  // 移除对应的键值对
    }

    public static void main(String[] args) {
        HashTable<String, String> hashTable = new HashTable<>(10);

        // 插入一些键值对
        hashTable.put("name", "Alice");
        hashTable.put("age", "30");
        hashTable.put("city", "New York");

        // 查找键值对
        System.out.println("Name: " + hashTable.get("name"));  // 输出: Name: Alice
        System.out.println("Age: " + hashTable.get("age"));    // 输出: Age: 30
        System.out.println("City: " + hashTable.get("city"));  // 输出: City: New York

        // 删除键值对
        hashTable.remove("age");
        System.out.println("Age after removal: " + hashTable.get("age"));  // 输出: Age after removal: null
    }
}