-- 修复数据库中文乱码问题的SQL脚本

-- 1. 查看当前数据库字符集
SHOW CREATE DATABASE vue_multi_page;

-- 2. 修改数据库字符集为UTF-8
ALTER DATABASE vue_multi_page CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 3. 查看所有表
SHOW TABLES;

-- 4. 修改所有表的字符集
ALTER TABLE products CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE product_images CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE addresses CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE favorites CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE messages CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE orders CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 5. 查看表结构，确认字符集是否已修改
SHOW CREATE TABLE products;
SHOW CREATE TABLE users;

-- 6. 查看当前连接的字符集设置
SHOW VARIABLES LIKE 'character_set_%';
SHOW VARIABLES LIKE 'collation_%';
