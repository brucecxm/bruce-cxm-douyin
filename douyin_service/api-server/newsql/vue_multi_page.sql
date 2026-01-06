/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : vue_multi_page

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 31/12/2025 17:15:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for addresses
-- ----------------------------
DROP TABLE IF EXISTS `addresses`;
CREATE TABLE `addresses`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `receiver` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_default` tinyint(1) NULL DEFAULT 0,
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_addresses_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `addresses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of addresses
-- ----------------------------
INSERT INTO `addresses` VALUES (5, 30, '555555', '17379557175', '55', '22', '11', '11111111111', 1, '2025-12-30 18:00:23', '2025-12-30 18:00:23');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKpu4bcbluhsxagirmbdn7dilm5`(`product_id`) USING BTREE,
  INDEX `FKg5uhi8vpsuy0lgloxk2h4w5o6`(`user_id`) USING BTREE,
  CONSTRAINT `FKg5uhi8vpsuy0lgloxk2h4w5o6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKpu4bcbluhsxagirmbdn7dilm5` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (1, '2025-12-30 17:27:36.305000', 7, '2025-12-30 17:27:49.825000', 35, 30);
INSERT INTO `cart` VALUES (2, '2025-12-30 17:28:01.135000', 1, '2025-12-30 17:28:01.135000', 40, 30);
INSERT INTO `cart` VALUES (3, '2025-12-31 09:43:44.567000', 1, '2025-12-31 09:43:44.567000', 41, 30);

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `likes` int(11) NULL DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `replies` int(11) NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK6uv0qku8gsu6x1r2jkrtqwjtn`(`product_id`) USING BTREE,
  INDEX `FK8omq0tc18jd43bu5tjh6jvraq`(`user_id`) USING BTREE,
  CONSTRAINT `FK6uv0qku8gsu6x1r2jkrtqwjtn` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES (1, '55', '2025-12-30 16:41:21.158000', 0, 5, 0, '2025-12-30 16:41:21.158000', 41, 30);
INSERT INTO `comments` VALUES (2, '55555', '2025-12-30 16:41:25.023000', 0, 5, 0, '2025-12-30 16:41:25.023000', 41, 30);
INSERT INTO `comments` VALUES (3, '22222222', '2025-12-30 17:16:32.331000', 0, 5, 0, '2025-12-30 17:16:32.331000', 35, 30);

-- ----------------------------
-- Table structure for favorites
-- ----------------------------
DROP TABLE IF EXISTS `favorites`;
CREATE TABLE `favorites`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id`, `product_id`) USING BTREE,
  UNIQUE INDEX `UKgh1s14hhb9qb8p2do933hscsf`(`user_id`, `product_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `idx_favorites_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `favorites_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorites
-- ----------------------------
INSERT INTO `favorites` VALUES (8, 30, 40, '2025-12-30 17:33:55');
INSERT INTO `favorites` VALUES (9, 30, 44, '2025-12-30 17:43:51');
INSERT INTO `favorites` VALUES (10, 30, 41, '2025-12-31 09:43:49');

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sender_id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_read` tinyint(1) NULL DEFAULT 0,
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_messages_sender_id`(`sender_id`) USING BTREE,
  INDEX `idx_messages_receiver_id`(`receiver_id`) USING BTREE,
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of messages
-- ----------------------------
INSERT INTO `messages` VALUES (6, 30, 25, '2', 0, '2025-12-26 17:01:21');
INSERT INTO `messages` VALUES (7, 30, 25, '2', 0, '2025-12-26 17:01:24');
INSERT INTO `messages` VALUES (8, 30, 25, '2', 0, '2025-12-26 17:01:25');
INSERT INTO `messages` VALUES (9, 31, 32, '2', 0, '2025-12-26 17:05:50');
INSERT INTO `messages` VALUES (10, 32, 31, '22222222', 0, '2025-12-26 17:05:58');
INSERT INTO `messages` VALUES (11, 32, 31, '22222222222222', 0, '2025-12-26 17:06:01');
INSERT INTO `messages` VALUES (12, 30, 30, '222', 0, '2025-12-31 09:43:58');
INSERT INTO `messages` VALUES (13, 33, 30, '666666666666', 0, '2025-12-31 09:57:17');
INSERT INTO `messages` VALUES (14, 30, 33, '55555555', 0, '2025-12-31 09:58:56');
INSERT INTO `messages` VALUES (15, 33, 30, '5555555555', 0, '2025-12-31 09:59:08');
INSERT INTO `messages` VALUES (16, 30, 33, '555555555', 0, '2025-12-31 09:59:13');
INSERT INTO `messages` VALUES (17, 30, 33, '999999999', 0, '2025-12-31 09:59:19');
INSERT INTO `messages` VALUES (18, 33, 30, '222', 0, '2025-12-31 10:41:32');
INSERT INTO `messages` VALUES (19, 33, 30, '432434324324', 0, '2025-12-31 10:41:44');
INSERT INTO `messages` VALUES (20, 33, 30, '432432432434', 0, '2025-12-31 10:42:14');
INSERT INTO `messages` VALUES (21, 30, 33, 'qqqqqqq', 0, '2025-12-31 10:45:52');
INSERT INTO `messages` VALUES (22, 30, 33, 'qqqqqq', 0, '2025-12-31 10:45:55');
INSERT INTO `messages` VALUES (23, 33, 30, 'qqqqqqqqqq', 0, '2025-12-31 10:46:03');
INSERT INTO `messages` VALUES (24, 33, 30, '222222222222222222', 0, '2025-12-31 10:46:07');
INSERT INTO `messages` VALUES (25, 33, 30, '222222222222222222', 0, '2025-12-31 10:46:15');
INSERT INTO `messages` VALUES (26, 33, 30, '888', 0, '2025-12-31 11:22:15');
INSERT INTO `messages` VALUES (27, 33, 30, '9999999999', 0, '2025-12-31 11:22:26');
INSERT INTO `messages` VALUES (28, 25, 31, '????', 0, '2025-12-31 13:28:37');
INSERT INTO `messages` VALUES (29, 33, 30, '4444', 0, '2025-12-31 16:46:39');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  `total_amount` double NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `receiver` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `discount` double NULL DEFAULT NULL,
  `pay_time` datetime(6) NULL DEFAULT NULL,
  `payment_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shipping_fee` double NULL DEFAULT NULL,
  `address_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_orders_user_id`(`user_id`) USING BTREE,
  INDEX `idx_orders_product_id`(`product_id`) USING BTREE,
  INDEX `FKhlglkvf5i60dv6dn397ethgpt`(`address_id`) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `FKhlglkvf5i60dv6dn397ethgpt` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (4, '0657b839be214308b7115038956cba62', 30, 41, 1, 44, 'pending', NULL, NULL, NULL, '2025-12-30 17:44:21', '2025-12-31 09:42:11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `orders` VALUES (5, 'c15c89e7560148c2a25fd23b0c5af9e7', 30, 41, 1, 44, 'pending', NULL, NULL, NULL, '2025-12-30 17:44:28', '2025-12-31 09:42:11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `orders` VALUES (6, '5c4bb340df554c87b9fe7921698d3b34', 30, 41, 1, 44, 'pending', NULL, NULL, NULL, '2025-12-30 17:46:32', '2025-12-31 09:42:11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `orders` VALUES (7, '696bbf259dcf4f818f1421eca3a3d377', 30, 41, 1, 44, 'pending', NULL, NULL, NULL, '2025-12-30 17:46:54', '2025-12-31 09:42:11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `orders` VALUES (8, '467e1bc2c25c4c868de0bd91c095259d', 30, 41, 1, 44, 'pending', NULL, NULL, NULL, '2025-12-30 17:48:40', '2025-12-31 09:42:11', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `orders` VALUES (9, '5cd86d81f5be4c72a1c49282a04fa595', 30, 41, 1, 44, 'pending_payment', NULL, NULL, NULL, '2025-12-30 18:00:40', '2025-12-31 09:42:11', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for product_images
-- ----------------------------
DROP TABLE IF EXISTS `product_images`;
CREATE TABLE `product_images`  (
  `product_id` bigint(20) NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  INDEX `FKqnq71xsohugpqwf3c9gxmsuy`(`product_id`) USING BTREE,
  CONSTRAINT `FKqnq71xsohugpqwf3c9gxmsuy` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_images
-- ----------------------------
INSERT INTO `product_images` VALUES (35, 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200');
INSERT INTO `product_images` VALUES (40, 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200');
INSERT INTO `product_images` VALUES (41, 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200');
INSERT INTO `product_images` VALUES (42, 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200');
INSERT INTO `product_images` VALUES (43, 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200');
INSERT INTO `product_images` VALUES (44, 'http://1.94.131.16:9000/douyin/product/20251230/438b5d9c-db87-46b0-978b-c03270e4efbd.png');
INSERT INTO `product_images` VALUES (45, 'http://1.94.131.16:9000/douyin/product/20251231/b2872e44-e76d-40bf-bc2c-b8ff860df056.png');

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_products_user_id`(`user_id`) USING BTREE,
  INDEX `idx_products_category`(`category`) USING BTREE,
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (35, '444444', '11111111111', 'https://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960', 44, 1, '������������', '������ ���������', 'active', 25, '2025-12-26 15:22:41', '2025-12-26 17:27:58');
INSERT INTO `products` VALUES (40, '111', '4114444444', 'https://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960', 11, 1, '������������', '������ ���������', 'active', 30, '2025-12-26 15:30:01', '2025-12-26 17:28:00');
INSERT INTO `products` VALUES (41, '444', '77������������������������', 'https://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960', 44, 1, '������������', '������ ���������', 'active', 30, '2025-12-26 15:30:40', '2025-12-26 17:28:01');
INSERT INTO `products` VALUES (42, '111111111111111', '1111111111111111', 'https://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960', 111, 1, '手机数码', '北京 朝阳区', 'active', 31, '2025-12-26 17:04:55', '2025-12-26 17:28:01');
INSERT INTO `products` VALUES (43, '22222222222222', '222222222222222222', 'https://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960', 2, 1, '服装鞋包', '北京 朝阳区', 'active', 32, '2025-12-26 17:05:30', '2025-12-26 17:28:05');
INSERT INTO `products` VALUES (44, '55', '11111111111111', NULL, 1, 1, '服装鞋包', '北京 朝阳区', 'active', 30, '2025-12-30 17:08:40', '2025-12-30 17:08:40');
INSERT INTO `products` VALUES (45, '555555555', '55555555555555', NULL, 5, 1, '服装鞋包', '北京 朝阳区', 'active', 33, '2025-12-31 09:58:30', '2025-12-31 09:58:30');

-- ----------------------------
-- Table structure for recent_view
-- ----------------------------
DROP TABLE IF EXISTS `recent_view`;
CREATE TABLE `recent_view`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `viewed_at` datetime(6) NULL DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK4whi9shp3ce8oivcsce2qs7he`(`product_id`) USING BTREE,
  INDEX `FKg9l7vfuf3g796unremiso164u`(`user_id`) USING BTREE,
  CONSTRAINT `FK4whi9shp3ce8oivcsce2qs7he` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKg9l7vfuf3g796unremiso164u` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of recent_view
-- ----------------------------
INSERT INTO `recent_view` VALUES (1, '2025-12-31 09:43:16.378000', 41, 30);
INSERT INTO `recent_view` VALUES (2, '2025-12-30 17:43:51.069000', 44, 30);
INSERT INTO `recent_view` VALUES (3, '2025-12-31 16:46:29.418000', 41, 33);
INSERT INTO `recent_view` VALUES (4, '2025-12-31 16:46:09.371000', 45, 30);
INSERT INTO `recent_view` VALUES (5, '2025-12-31 10:42:24.562000', 40, 33);
INSERT INTO `recent_view` VALUES (6, '2025-12-31 10:46:27.420000', 35, 30);
INSERT INTO `recent_view` VALUES (7, '2025-12-31 11:21:53.408000', 42, 33);
INSERT INTO `recent_view` VALUES (8, '2025-12-31 11:22:00.770000', 44, 33);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `birthday` date NULL DEFAULT NULL,
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (25, 'user_1', 'default_password', '用户1', 'user_1@example.com', '13800000000', NULL, NULL, NULL, NULL, '2025-12-26 15:22:40', '2025-12-26 15:22:40');
INSERT INTO `users` VALUES (30, '000000', '$2a$10$OfBOlm6iXjtGbf2.kQ568eO59Jc0.Q6GVLcuzkN.GJBawJIy8aENK', '000000', '1@qq.com', NULL, NULL, NULL, NULL, NULL, '2025-12-26 15:29:38', '2025-12-26 15:29:38');
INSERT INTO `users` VALUES (31, 'aaaaaa', '$2a$10$OeLbtXJ.vOdBEo.OkpCzaOZmPhc5kDtNPepBp.iDCNg1uf4biOiAS', 'aaaaaa', '000000@qq.com', NULL, NULL, NULL, NULL, NULL, '2025-12-26 17:03:58', '2025-12-26 17:03:58');
INSERT INTO `users` VALUES (32, 'bbbbbb', '$2a$10$0O7CmHkKc7h4hEcbGb2oxOPiBZtYMCwMXRUFKV9K8iBExfOyCXkOK', 'bbbbbb', '0000030@qq.com', NULL, NULL, NULL, NULL, NULL, '2025-12-26 17:04:17', '2025-12-26 17:04:17');
INSERT INTO `users` VALUES (33, 'chenchen', '$2a$10$Iyy5gqwiNijpzF8TALHireA5jxVls6HoxFq36exO70vpsTDoGJ3ka', 'chenchen', 'chenchen@qq.com', NULL, NULL, NULL, NULL, NULL, '2025-12-31 09:45:12', '2025-12-31 09:45:12');

SET FOREIGN_KEY_CHECKS = 1;
