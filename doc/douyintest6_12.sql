-- MySQL dump 10.13  Distrib 8.0.11, for Win64 (x86_64)
--
-- Host: localhost    Database: douyintest
-- ------------------------------------------------------
-- Server version	8.0.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address_book`
--

DROP TABLE IF EXISTS `address_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `address_book` (
  `id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `consignee` varchar(50) DEFAULT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `province_code` varchar(12) DEFAULT NULL,
  `province_name` varchar(32) DEFAULT NULL,
  `city_code` varchar(12) DEFAULT NULL,
  `city_name` varchar(32) DEFAULT NULL,
  `district_code` varchar(12) DEFAULT NULL,
  `district_name` varchar(32) DEFAULT NULL,
  `detail` varchar(200) DEFAULT NULL,
  `label` varchar(100) DEFAULT NULL,
  `is_default` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address_book`
--

LOCK TABLES `address_book` WRITE;
/*!40000 ALTER TABLE `address_book` DISABLE KEYS */;
INSERT INTO `address_book` VALUES (1417414526093082626,1417012167126876162,'小明',1,'13812345678',NULL,NULL,NULL,NULL,NULL,NULL,'昌平区金燕龙办公楼','公司',1,'2021-07-20 17:22:12','2021-07-20 17:26:33',1417012167126876162,1417012167126876162,0),(1417414926166769666,1417012167126876162,'小李',1,'13512345678',NULL,NULL,NULL,NULL,NULL,NULL,'测试','家',0,'2021-07-20 17:23:47','2021-07-20 17:23:47',1417012167126876162,1417012167126876162,0),(1801541519460118529,131,'11',1,'17379557174',NULL,NULL,NULL,NULL,NULL,NULL,'111','公司',1,'2024-06-14 17:05:44','2024-06-14 17:05:51',1767515082532777985,1767515082532777985,1),(1804781888427618305,1767515082532777985,'5555',1,'13695662143',NULL,NULL,NULL,NULL,NULL,NULL,'111','公司',0,'2024-06-23 15:41:48','2024-06-23 15:41:48',1767515082532777985,1767515082532777985,0),(1804782049954459649,1767515082532777985,'123',1,'17896552365',NULL,NULL,NULL,NULL,NULL,NULL,'126','公司',0,'2024-06-23 15:42:27','2024-06-23 15:42:27',1767515082532777985,1767515082532777985,0);
/*!40000 ALTER TABLE `address_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `category` (
  `id` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1234567890,2,'Sample Name',5,'2025-04-15 20:21:13',NULL,NULL,NULL),(1912119033692499970,2,'Sample Name',5,'2025-04-15 20:21:18',NULL,NULL,NULL),(1912119399104458754,2,'Sample Name',5,'2025-04-15 20:22:45',NULL,NULL,NULL);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_detail`
--

DROP TABLE IF EXISTS `chat_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `chat_detail` (
  `user_id` int(11) DEFAULT NULL,
  `friend_id` int(11) DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  `chat_context` varchar(500) DEFAULT NULL,
  `img_url` varchar(500) DEFAULT NULL,
  `video_url` varchar(500) DEFAULT NULL,
  `file_url` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_detail`
--

LOCK TABLES `chat_detail` WRITE;
/*!40000 ALTER TABLE `chat_detail` DISABLE KEYS */;
INSERT INTO `chat_detail` VALUES (101,102,'2024-11-11','11111111',NULL,NULL,NULL),(101,102,'2024-11-11','55555',NULL,NULL,NULL),(101,103,'2024-11-13',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `chat_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_group`
--

DROP TABLE IF EXISTS `chat_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `chat_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL,
  `created_time` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `group_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1913138602456399874 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_group`
--

LOCK TABLES `chat_group` WRITE;
/*!40000 ALTER TABLE `chat_group` DISABLE KEYS */;
INSERT INTO `chat_group` VALUES (1913130891652104194,'开发讨论组','2025-04-18 15:22:03',123456,NULL),(1913130973931765762,'开发讨论组','2025-04-18 15:22:23',123456,NULL),(1913130977857634306,'开发讨论组','2025-04-18 15:22:24',123456,NULL),(1913130981376655361,'开发讨论组','2025-04-18 15:22:25',123456,NULL),(1913138602456399873,'开发讨论组','2025-04-18 15:52:41',138,NULL);
/*!40000 ALTER TABLE `chat_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_group_member`
--

DROP TABLE IF EXISTS `chat_group_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `chat_group_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `join_time` datetime NOT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_group_member`
--

LOCK TABLES `chat_group_member` WRITE;
/*!40000 ALTER TABLE `chat_group_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_group_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_log`
--

DROP TABLE IF EXISTS `chat_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `chat_log` (
  `user_id` varchar(500) DEFAULT NULL,
  `friend_id` varchar(500) DEFAULT NULL,
  `create_time` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_log`
--

LOCK TABLES `chat_log` WRITE;
/*!40000 ALTER TABLE `chat_log` DISABLE KEYS */;
INSERT INTO `chat_log` VALUES ('11','brucebbb','2024-11-10'),('11','brucebbb','2024-11-11'),('11','bruceccc','2024-11-11'),('11','bruceccc','2024-11-11'),('11','[object PointerEvent]','2025-01-03'),('11','[object PointerEvent]','2025-01-03'),('11','[object PointerEvent]','2025-01-03');
/*!40000 ALTER TABLE `chat_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_message`
--

DROP TABLE IF EXISTS `chat_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `chat_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sender_id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `message_type` int(11) DEFAULT NULL,
  `message_content` text,
  `sent_time` datetime NOT NULL,
  `status` int(11) DEFAULT '0',
  `is_group` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1913138967532814339 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message`
--

LOCK TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
INSERT INTO `chat_message` VALUES (1913129308088434690,138,131,NULL,1,'ceshi','2025-04-18 15:15:43',0,0),(1913138967532814338,138,131,1913138602456399873,1,'ceshi','2025-04-18 15:54:09',0,1);
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `comment` (
  `comment_id` bigint(20) NOT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `comment_type` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `father_id` int(11) DEFAULT NULL,
  `content_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (469813287245857,'测试',12,138,NULL,NULL),(469813903392801,'大王的',12,NULL,NULL,NULL),(469815170063393,'dw ',12,NULL,NULL,NULL),(469815173470241,'dwa ',12,NULL,NULL,NULL),(469815212000289,'dawd',12,NULL,NULL,NULL),(469815376882721,'dw',12,NULL,NULL,NULL),(469815376883745,'fwaf',12,NULL,NULL,NULL),(469815377574945,'dwa ',12,NULL,NULL,NULL),(469815458760737,'测试',12,12,NULL,NULL),(469815470116897,'测试',12,12,NULL,NULL),(469815492931617,'测试',12,12,NULL,NULL),(470166252942369,'你好',NULL,137,NULL,NULL),(470770013184033,'你好',NULL,NULL,NULL,NULL),(470770084061217,'哈哈',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS `coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon`
--

LOCK TABLES `coupon` WRITE;
/*!40000 ALTER TABLE `coupon` DISABLE KEYS */;
INSERT INTO `coupon` VALUES (1,10,'得物优惠券');
/*!40000 ALTER TABLE `coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dc_system`
--

DROP TABLE IF EXISTS `dc_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `dc_system` (
  `name` varchar(500) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dc_system`
--

LOCK TABLES `dc_system` WRITE;
/*!40000 ALTER TABLE `dc_system` DISABLE KEYS */;
INSERT INTO `dc_system` VALUES ('login_token','sa-token',1);
/*!40000 ALTER TABLE `dc_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dish`
--

DROP TABLE IF EXISTS `dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `dish` (
  `id` bigint(20) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `code` varchar(64) DEFAULT NULL,
  `image` varchar(200) DEFAULT NULL,
  `description` varchar(400) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL,
  `store_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish`
--

LOCK TABLES `dish` WRITE;
/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
INSERT INTO `dish` VALUES (1397849739276890114,'辣子鸡',1397844263642378242,7800.00,'222222222','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','来自鲜嫩美味的小鸡，值得一尝',1,0,'2021-05-27 09:38:43','2021-05-27 09:38:43',1,1,0,NULL),(1397850140982161409,'毛氏红烧肉',1397844263642378242,6800.00,'123412341234','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','毛氏红烧肉毛氏红烧肉，确定不来一份？',1,0,'2021-05-27 09:40:19','2021-05-27 09:40:19',1,1,0,NULL),(1397850392090947585,'组庵鱼翅',1397844263642378242,4800.00,'123412341234','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','组庵鱼翅，看图足以表明好吃程度',1,0,'2021-05-27 09:41:19','2021-05-27 09:41:19',1,1,0,NULL),(1397850851245600769,'霸王别姬',1397844263642378242,12800.00,'123412341234','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','还有什么比霸王别姬更美味的呢？',1,0,'2021-05-27 09:43:08','2021-05-27 09:43:08',1,1,0,NULL),(1397851099502260226,'全家福',1397844263642378242,11800.00,'23412341234','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','别光吃肉啦，来份全家福吧，让你长寿又美味',1,0,'2021-05-27 09:44:08','2021-05-27 09:44:08',1,1,0,NULL),(1397851370462687234,'邵阳猪血丸子',1397844263642378242,13800.00,'1246812345678','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','看，美味不？来嘛来嘛，这才是最爱吖',1,0,'2021-05-27 09:45:12','2021-05-27 09:45:12',1,1,0,NULL),(1397851668262465537,'口味蛇',1397844263642378242,16800.00,'1234567812345678','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','爬行界的扛把子，东兴-口味蛇，让你欲罢不能',1,0,'2021-05-27 09:46:23','2021-05-27 09:46:23',1,1,0,NULL),(1397852391150759938,'辣子鸡丁',1397844303408574465,8800.00,'2346812468','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','辣子鸡丁，辣子鸡丁，永远的魂',1,0,'2021-05-27 09:49:16','2021-05-27 09:49:16',1,1,0,NULL),(1397853183287013378,'麻辣兔头',1397844303408574465,19800.00,'123456787654321','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','麻辣兔头的详细制作，麻辣鲜香，色泽红润，回味悠长',1,0,'2021-05-27 09:52:24','2021-05-27 09:52:24',1,1,0,NULL),(1397853709101740034,'蒜泥白肉',1397844303408574465,9800.00,'1234321234321','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','多么的有食欲啊',1,0,'2021-05-27 09:54:30','2021-05-27 09:54:30',1,1,0,NULL),(1397853890262118402,'鱼香肉丝',1397844303408574465,3800.00,'1234212321234','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','鱼香肉丝简直就是我们童年回忆的一道经典菜，上学的时候点个鱼香肉丝盖饭坐在宿舍床上看着肥皂剧，绝了！现在完美复刻一下上学的时候感觉',1,0,'2021-05-27 09:55:13','2021-05-27 09:55:13',1,1,0,NULL),(1397854652581064706,'麻辣水煮鱼',1397844303408574465,14800.00,'2345312·345321','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','鱼片是买的切好的鱼片，放几个虾，增加味道',1,0,'2021-05-27 09:58:15','2021-05-27 09:58:15',1,1,0,NULL),(1397854865672679425,'鱼香炒鸡蛋',1397844303408574465,2000.00,'23456431·23456','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','鱼香菜也是川味的特色。里面没有鱼却鱼香味',1,0,'2021-05-27 09:59:06','2021-05-27 09:59:06',1,1,0,NULL),(1397860242057375745,'脆皮烧鹅',1397844391040167938,12800.00,'123456786543213456','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','“广东烤鸭美而香，却胜烧鹅说古冈（今新会），燕瘦环肥各佳妙，君休偏重便宜坊”，可见烧鹅与烧鸭在粤菜之中已早负盛名。作为广州最普遍和最受欢迎的烧烤肉食，以它的“色泽金红，皮脆肉嫩，味香可口”的特色，在省城各大街小巷的烧卤店随处可见。',1,0,'2021-05-27 10:20:27','2021-05-27 10:20:27',1,1,0,NULL),(1397860578738352129,'白切鸡',1397844391040167938,6600.00,'12345678654','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','白切鸡是一道色香味俱全的特色传统名肴，又叫白斩鸡，是粤菜系鸡肴中的一种，始于清代的民间。白切鸡通常选用细骨农家鸡与沙姜、蒜茸等食材，慢火煮浸白切鸡皮爽肉滑，清淡鲜美。著名的泮溪酒家白切鸡，曾获商业部优质产品金鼎奖。湛江白切鸡更是驰名粤港澳。粤菜厨坛中，鸡的菜式有200余款之多，而最为人常食不厌的正是白切鸡，深受食家青睐。',1,0,'2021-05-27 10:21:48','2021-05-27 10:21:48',1,1,0,NULL),(1397860792492666881,'烤乳猪',1397844391040167938,38800.00,'213456432123456','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','广式烧乳猪主料是小乳猪，辅料是蒜，调料是五香粉、芝麻酱、八角粉等，本菜品主要通过将食材放入炭火中烧烤而成。烤乳猪是广州最著名的特色菜，并且是“满汉全席”中的主打菜肴之一。烤乳猪也是许多年来广东人祭祖的祭品之一，是家家都少不了的应节之物，用乳猪祭完先人后，亲戚们再聚餐食用。',1,0,'2021-05-27 10:22:39','2021-05-27 10:22:39',1,1,0,NULL),(1397860963880316929,'脆皮乳鸽',1397844391040167938,10800.00,'1234563212345','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','“脆皮乳鸽”是广东菜中的一道传统名菜，属于粤菜系，具有皮脆肉嫩、色泽红亮、鲜香味美的特点，常吃可使身体强健，清肺顺气。随着菜品制作工艺的不断发展，逐渐形成了熟炸法、生炸法和烤制法三种制作方法。无论那种制作方法，都是在鸽子经过一系列的加工，挂脆皮水后再加工而成，正宗的“脆皮乳鸽皮脆肉嫩、色泽红亮、鲜香味美、香气馥郁。这三种方法的制作过程都不算复杂，但想达到理想的效果并不容易。',1,0,'2021-05-27 10:23:19','2021-05-27 10:23:19',1,1,0,NULL),(1397861683434139649,'清蒸河鲜海鲜',1397844391040167938,38800.00,'1234567876543213456','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','新鲜的海鲜，清蒸是最好的处理方式。鲜，体会为什么叫海鲜。清蒸是广州最经典的烹饪手法，过去岭南地区由于峻山大岭阻隔，交通不便，经济发展起步慢，自家打的鱼放在锅里煮了就吃，没有太多的讲究，但却发现这清淡的煮法能使鱼的鲜甜跃然舌尖。',1,0,'2021-05-27 10:26:11','2021-05-27 10:26:11',1,1,0,NULL),(1397862198033297410,'老火靓汤',1397844391040167938,49800.00,'123456786532455','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','老火靓汤又称广府汤，是广府人传承数千年的食补养生秘方，慢火煲煮的中华老火靓汤，火候足，时间长，既取药补之效，又取入口之甘甜。 广府老火汤种类繁多，可以用各种汤料和烹调方法，烹制出各种不同口味、不同功效的汤来。',1,0,'2021-05-27 10:28:14','2021-05-27 10:28:14',1,1,0,NULL),(1397862477831122945,'上汤焗龙虾',1397844391040167938,108800.00,'1234567865432','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','上汤焗龙虾是一道色香味俱全的传统名菜，属于粤菜系。此菜以龙虾为主料，配以高汤制成的一道海鲜美食。本品肉质洁白细嫩，味道鲜美，蛋白质含量高，脂肪含量低，营养丰富。是色香味俱全的传统名菜。',1,0,'2021-05-27 10:29:20','2021-05-27 10:29:20',1,1,0,NULL),(1413342036832100354,'北冰洋',1413341197421846529,500.00,'','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','',1,0,'2021-07-09 11:39:35','2021-07-09 15:12:18',1,1,0,NULL),(1413384757047271425,'王老吉',1413341197421846529,500.00,'','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','',1,0,'2021-07-09 14:29:20','2021-07-12 09:09:16',1,1,0,NULL),(1413385247889891330,'米饭',1413384954989060097,200.00,'','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','',1,0,'2021-07-09 14:31:17','2021-07-11 16:35:26',1,1,0,NULL),(1788590919193358338,'嗡嗡嗡',1397844303408574465,10000.00,'','https://gips3.baidu.com/it/u=3732737575,1337431568&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f1440_2560','所说啥',1,0,'2024-05-09 23:24:40','2024-05-09 23:24:40',1,1,0,NULL);
/*!40000 ALTER TABLE `dish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dish_flavor`
--

DROP TABLE IF EXISTS `dish_flavor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `dish_flavor` (
  `id` bigint(20) DEFAULT NULL,
  `dish_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_flavor`
--

LOCK TABLES `dish_flavor` WRITE;
/*!40000 ALTER TABLE `dish_flavor` DISABLE KEYS */;
INSERT INTO `dish_flavor` VALUES (1397849417888346113,1397849417854791681,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:37:27','2021-05-27 09:37:27',1,1,0),(1397849739297861633,1397849739276890114,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:38:43','2021-05-27 09:38:43',1,1,0),(1397849739323027458,1397849739276890114,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:38:43','2021-05-27 09:38:43',1,1,0),(1397849936421761025,1397849936404983809,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:39:30','2021-05-27 09:39:30',1,1,0),(1397849936438538241,1397849936404983809,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:39:30','2021-05-27 09:39:30',1,1,0),(1397850141015715841,1397850140982161409,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:40:19','2021-05-27 09:40:19',1,1,0),(1397850141040881665,1397850140982161409,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:40:19','2021-05-27 09:40:19',1,1,0),(1397850392120307713,1397850392090947585,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:41:19','2021-05-27 09:41:19',1,1,0),(1397850392137084929,1397850392090947585,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:41:19','2021-05-27 09:41:19',1,1,0),(1397850630734262274,1397850630700707841,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:42:16','2021-05-27 09:42:16',1,1,0),(1397850630755233794,1397850630700707841,'辣度','[\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:42:16','2021-05-27 09:42:16',1,1,0),(1397850851274960898,1397850851245600769,'忌口','[\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:43:08','2021-05-27 09:43:08',1,1,0),(1397850851283349505,1397850851245600769,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:43:08','2021-05-27 09:43:08',1,1,0),(1397851099523231745,1397851099502260226,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:44:08','2021-05-27 09:44:08',1,1,0),(1397851099527426050,1397851099502260226,'辣度','[\"不辣\",\"微辣\",\"中辣\"]','2021-05-27 09:44:08','2021-05-27 09:44:08',1,1,0),(1397851370483658754,1397851370462687234,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]','2021-05-27 09:45:12','2021-05-27 09:45:12',1,1,0),(1397851370483658755,1397851370462687234,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:45:12','2021-05-27 09:45:12',1,1,0),(1397851370483658756,1397851370462687234,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:45:12','2021-05-27 09:45:12',1,1,0),(1397851668283437058,1397851668262465537,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]','2021-05-27 09:46:23','2021-05-27 09:46:23',1,1,0),(1397852391180120065,1397852391150759938,'忌口','[\"不要葱\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:49:16','2021-05-27 09:49:16',1,1,0),(1397852391196897281,1397852391150759938,'辣度','[\"不辣\",\"微辣\",\"重辣\"]','2021-05-27 09:49:16','2021-05-27 09:49:16',1,1,0),(1397853183307984898,1397853183287013378,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:52:24','2021-05-27 09:52:24',1,1,0),(1397853423486414850,1397853423461249026,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:53:22','2021-05-27 09:53:22',1,1,0),(1397853709126905857,1397853709101740034,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:54:30','2021-05-27 09:54:30',1,1,0),(1397853890283089922,1397853890262118402,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:55:13','2021-05-27 09:55:13',1,1,0),(1397854133632413697,1397854133603053569,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]','2021-05-27 09:56:11','2021-05-27 09:56:11',1,1,0),(1397854652623007745,1397854652581064706,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:58:15','2021-05-27 09:58:15',1,1,0),(1397854652635590658,1397854652581064706,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:58:15','2021-05-27 09:58:15',1,1,0),(1397854865735593986,1397854865672679425,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:59:06','2021-05-27 09:59:06',1,1,0),(1397855742303186946,1397855742273826817,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:02:35','2021-05-27 10:02:35',1,1,0),(1397855906497605633,1397855906468245506,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 10:03:14','2021-05-27 10:03:14',1,1,0),(1397856190573621250,1397856190540066818,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:04:21','2021-05-27 10:04:21',1,1,0),(1397859056709316609,1397859056684150785,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:15:45','2021-05-27 10:15:45',1,1,0),(1397859277837217794,1397859277812051969,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:16:37','2021-05-27 10:16:37',1,1,0),(1397859487502086146,1397859487476920321,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:17:27','2021-05-27 10:17:27',1,1,0),(1397859757061615618,1397859757036449794,'甜味','[\"无糖\",\"少糖\",\"半躺\",\"多糖\",\"全糖\"]','2021-05-27 10:18:32','2021-05-27 10:18:32',1,1,0),(1397860242086735874,1397860242057375745,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:20:27','2021-05-27 10:20:27',1,1,0),(1397860963918065665,1397860963880316929,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:23:19','2021-05-27 10:23:19',1,1,0),(1397861135754506242,1397861135733534722,'甜味','[\"无糖\",\"少糖\",\"半躺\",\"多糖\",\"全糖\"]','2021-05-27 10:24:00','2021-05-27 10:24:00',1,1,0),(1397861370035744769,1397861370010578945,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:24:56','2021-05-27 10:24:56',1,1,0),(1397861683459305474,1397861683434139649,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 10:26:11','2021-05-27 10:26:11',1,1,0),(1397861898467717121,1397861898438356993,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 10:27:02','2021-05-27 10:27:02',1,1,0),(1397862198054268929,1397862198033297410,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 10:28:14','2021-05-27 10:28:14',1,1,0),(1397862477835317250,1397862477831122945,'辣度','[\"不辣\",\"微辣\",\"中辣\"]','2021-05-27 10:29:20','2021-05-27 10:29:20',1,1,0),(1398089545865015297,1398089545676271617,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]','2021-05-28 01:31:38','2021-05-28 01:31:38',1,1,0),(1398089782323097601,1398089782285348866,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:32:34','2021-05-28 01:32:34',1,1,0),(1398090003262255106,1398090003228700673,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-28 01:33:27','2021-05-28 01:33:27',1,1,0),(1398090264554811394,1398090264517062657,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-28 01:34:29','2021-05-28 01:34:29',1,1,0),(1398090455399837698,1398090455324340225,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:35:14','2021-05-28 01:35:14',1,1,0),(1398090685449023490,1398090685419663362,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]','2021-05-28 01:36:09','2021-05-28 01:36:09',1,1,0),(1398090825358422017,1398090825329061889,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-28 01:36:43','2021-05-28 01:36:43',1,1,0),(1398091007051476993,1398091007017922561,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:37:26','2021-05-28 01:37:26',1,1,0),(1398091296164851713,1398091296131297281,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:38:35','2021-05-28 01:38:35',1,1,0),(1398091546531246081,1398091546480914433,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-28 01:39:35','2021-05-28 01:39:35',1,1,0),(1398091729809747969,1398091729788776450,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:40:18','2021-05-28 01:40:18',1,1,0),(1398091889499484161,1398091889449152513,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:40:56','2021-05-28 01:40:56',1,1,0),(1398092095179763713,1398092095142014978,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:41:45','2021-05-28 01:41:45',1,1,0),(1398092283877306370,1398092283847946241,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:42:30','2021-05-28 01:42:30',1,1,0),(1398094018939236354,1398094018893099009,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:49:24','2021-05-28 01:49:24',1,1,0),(1398094391494094850,1398094391456346113,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:50:53','2021-05-28 01:50:53',1,1,0),(1399574026165727233,1399305325713600514,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-06-01 03:50:25','2021-06-01 03:50:25',1399309715396669441,1399309715396669441,0),(1413389540592263169,1413384757047271425,'温度','[\"常温\",\"冷藏\"]','2021-07-12 09:09:16','2021-07-12 09:09:16',1,1,0),(1413389684020682754,1413342036832100354,'温度','[\"常温\",\"冷藏\"]','2021-07-09 15:12:18','2021-07-09 15:12:18',1,1,0),(1788590919193358339,1788590919193358338,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2024-05-09 23:24:40','2024-05-09 23:24:40',1,1,0);
/*!40000 ALTER TABLE `dish_flavor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `employee` (
  `id` bigint(20) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `id_number` varchar(18) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'管理员','admin','e10adc3949ba59abbe56e057f20f883e','13812312312','1','110101199001010047',1,'2021-05-06 17:20:07','2021-05-10 02:24:09',1,1),(1763061797927165954,'井怡宁','2111','e10adc3949ba59abbe56e057f20f883e','17379557475','1','362201200302132816',1,'2024-02-29 12:41:03','2024-02-29 12:41:03',1,1);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favor`
--

DROP TABLE IF EXISTS `favor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `favor` (
  `user_id` int(11) DEFAULT NULL,
  `comment_id` int(11) DEFAULT NULL,
  `comment_type` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favor`
--

LOCK TABLES `favor` WRITE;
/*!40000 ALTER TABLE `favor` DISABLE KEYS */;
INSERT INTO `favor` VALUES (138,12,NULL),(145,5,NULL);
/*!40000 ALTER TABLE `favor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_img`
--

DROP TABLE IF EXISTS `file_img`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `file_img` (
  `user_id` bigint(20) DEFAULT NULL,
  `img_url` varchar(2550) DEFAULT NULL,
  `href_url` varchar(2550) DEFAULT NULL,
  `file_url` varchar(2550) DEFAULT NULL,
  `type` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_img`
--

LOCK TABLES `file_img` WRITE;
/*!40000 ALTER TABLE `file_img` DISABLE KEYS */;
INSERT INTO `file_img` VALUES (NULL,'http://192.168.200.130:9000/douyin/shop/menu/1.png',NULL,NULL,'shop_menu'),(NULL,'http://192.168.200.130:9000/douyin/shop/menu/2.png',NULL,NULL,'shop_menu'),(NULL,'http://192.168.200.130:9000/douyin/shop/menu/3.png',NULL,NULL,'shop_menu'),(NULL,'http://192.168.200.130:9000/douyin/shop/menu/4.png',NULL,NULL,'shop_menu'),(NULL,'http://192.168.200.130:9000/douyin/shop/menu/5.png',NULL,NULL,'shop_menu'),(NULL,'http://192.168.200.130:9000/douyin/shop/menu/6.png',NULL,NULL,'shop_menu');
/*!40000 ALTER TABLE `file_img` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '自己的用户ID',
  `friend_id` bigint(20) NOT NULL COMMENT '好友的用户ID',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注名',
  `status` int(11) DEFAULT '1' COMMENT '状态：1=正常，2=拉黑',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_user_friend` (`user_id`,`friend_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_friend_id` (`friend_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1932735928988712963 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='好友关系表（单向）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
INSERT INTO `friend` VALUES (1913113353614565378,131,138,NULL,1,'2025-04-18 14:12:22'),(1913113353677479937,138,131,NULL,1,'2025-04-18 14:12:22'),(1924392351720136706,137,128,NULL,1,'2025-05-19 17:11:05'),(1924392351787245569,128,137,NULL,1,'2025-05-19 17:11:05'),(1932735928925798402,147,146,NULL,1,'2025-06-11 17:45:29'),(1932735928988712962,146,147,NULL,1,'2025-06-11 17:45:29');
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_request`
--

DROP TABLE IF EXISTS `friend_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `friend_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '请求ID',
  `from_user_id` bigint(20) NOT NULL COMMENT '请求发起人ID',
  `to_user_id` bigint(20) NOT NULL COMMENT '请求接收人ID',
  `verify_msg` varchar(200) DEFAULT NULL COMMENT '验证消息',
  `status` int(11) DEFAULT '0' COMMENT '请求状态：0=待处理，1=已同意，2=已拒绝',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `idx_to_user` (`to_user_id`),
  KEY `idx_from_user` (`from_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1932733593625411587 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='好友请求表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_request`
--

LOCK TABLES `friend_request` WRITE;
/*!40000 ALTER TABLE `friend_request` DISABLE KEYS */;
INSERT INTO `friend_request` VALUES (1913112618806059009,138,131,'ceshi',1,'2025-04-18 14:09:27'),(1913114862611898370,138,128,'ceshi',1,'2025-04-18 14:18:22'),(1918159773241503746,138,138,'ceshi',0,'2025-05-02 12:25:02'),(1924385628452151297,138,138,'ceshi',0,'2025-05-19 16:44:22'),(1924387597749182465,138,137,'请求关注',0,'2025-05-19 16:52:11'),(1924392076859006978,128,137,'ceshi',0,'2025-05-19 17:09:59'),(1924392298532167681,128,137,'ceshi',1,'2025-05-19 17:10:52'),(1925801020829356034,137,137,'请求关注',0,'2025-05-23 14:28:38'),(1932697973892890625,144,144,'请求关注',0,'2025-06-11 15:14:39'),(1932733593625411586,146,147,'你好',1,'2025-06-11 17:36:12');
/*!40000 ALTER TABLE `friend_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `img_detail`
--

DROP TABLE IF EXISTS `img_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `img_detail` (
  `id` int(11) DEFAULT NULL,
  `img_url` varchar(5000) DEFAULT NULL,
  `img_type` varchar(50) DEFAULT NULL,
  `lian_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `img_detail`
--

LOCK TABLES `img_detail` WRITE;
/*!40000 ALTER TABLE `img_detail` DISABLE KEYS */;
INSERT INTO `img_detail` VALUES (1,'sssssssssss','shop_head',1397849739276890114),(NULL,'ssssssssswwwwwww','shop_main',1397849739276890114);
/*!40000 ALTER TABLE `img_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lianjie`
--

DROP TABLE IF EXISTS `lianjie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `lianjie` (
  `lianjie` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lianjie`
--

LOCK TABLES `lianjie` WRITE;
/*!40000 ALTER TABLE `lianjie` DISABLE KEYS */;
INSERT INTO `lianjie` VALUES ('sss'),('ss'),('ss'),('ss'),('ss'),('ss'),('ss'),('ss'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2'),('链接1'),('链接2。');
/*!40000 ALTER TABLE `lianjie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `live`
--

DROP TABLE IF EXISTS `live`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `live` (
  `id` int(11) DEFAULT NULL,
  `name` int(11) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='直播表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `live`
--

LOCK TABLES `live` WRITE;
/*!40000 ALTER TABLE `live` DISABLE KEYS */;
/*!40000 ALTER TABLE `live` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `menu` (
  `menu_id` int(11) DEFAULT NULL,
  `menu_img` varchar(500) DEFAULT NULL,
  `menu_name` varchar(500) DEFAULT NULL,
  `menu_type` varchar(500) DEFAULT NULL,
  `menu_lev` int(11) DEFAULT NULL,
  `menu_href` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'//gw.alicdn.com/imgextra/i2/O1CN01qrrUAN1wRjrhtfk6Q_!!6000000006305-2-tps-183-144.png_q90.jpg','淘票票','shop_home_menu',NULL,NULL,1),(2,'//gw.alicdn.com/imgextra/i3/O1CN01ULqxIg1sMvM4cBneE_!!6000000005753-2-tps-183-144.png_q90.jpg','飞猪','shop_home_menu',NULL,NULL,1),(3,'//gw.alicdn.com/imgextra/i1/O1CN01ExCBy027VMvYQPjuN_!!6000000007802-2-tps-183-144.png_q90.jpg','练练消','shop_home_menu',NULL,NULL,1),(4,'//m11.360buyimg.com/babel/s135x168_jfs/t1/274758/21/27697/7229/680b3cfeFc7c79209/305695e04dc3ced4.png.webp','空调','shop_home_nav',NULL,NULL,1),(5,'//m11.360buyimg.com/babel/s135x168_jfs/t1/270636/28/28132/9006/680b3d2aF36316686/f40f3b9c4e2fb99c.png.webp','热水器','shop_home_nav',NULL,NULL,1),(55,'//m11.360buyimg.com/babel/s135x168_jfs/t1/279467/9/27186/7197/680b3d21F5e7b0177/825fe2ad693faa42.png.webp','燃气灶','shop_home_nav',NULL,NULL,1),(2212,'//m11.360buyimg.com/babel/s135x168_jfs/t1/274980/15/27875/8362/680b3cf4F023d07f0/fef6ae987d5b17e1.png.webp','洗衣柜','shop_home_nav',NULL,NULL,1),(432,'//m11.360buyimg.com/babel/s135x168_jfs/t1/286269/33/3219/9562/681dcb51F90fa1342/cbff60893c342381.png.webp','电视机','shop_home_nav',NULL,NULL,1),(435,'//m11.360buyimg.com/babel/s135x168_jfs/t1/279467/9/27186/7197/680b3d21F5e7b0177/825fe2ad693faa42.png.webp','菜单1','shop_home_nav',NULL,NULL,1),(43567,'//m11.360buyimg.com/babel/s135x168_jfs/t1/279467/9/27186/7197/680b3d21F5e7b0177/825fe2ad693faa42.png.webp','菜单1','shop_home_nav',NULL,NULL,1),(88657,'//m11.360buyimg.com/babel/s135x168_jfs/t1/279467/9/27186/7197/680b3d21F5e7b0177/825fe2ad693faa42.png.webp','菜单1','shop_home_nav',NULL,NULL,1),(765745,'//gw.alicdn.com/tfs/TB1XRlKOXzqK1RjSZFCXXbbxVXa-183-144.png_q90.jpg','U试用','shop_home_menu',NULL,NULL,1),(7567,'//gw.alicdn.com/imgextra/i1/O1CN01Lil7RM1U7JCGBNT1U_!!6000000002470-2-tps-183-144.png_q90.jpg','聚划算','shop_home_menu',NULL,NULL,1),(8768,'//gw.alicdn.com/imgextra/i4/O1CN01pFtEB01bCWtK2kxcc_!!6000000003429-2-tps-183-144.png_q90.jpg','小黑盒','shop_home_menu',NULL,NULL,1),(54645,'//gw.alicdn.com/imgextra/i2/O1CN01ZOR1cv1yjGFORGh1V_!!6000000006614-2-tps-183-144.png_q90.jpg','阿里拍卖','shop_home_menu',NULL,NULL,1),(8678,'//gw.alicdn.com/imgextra/i2/O1CN01fV2JYQ1CzCaMFAxwK_!!6000000000151-2-tps-183-144.png_q90.jpg','斗地主','shop_home_menu',NULL,NULL,1);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music`
--

DROP TABLE IF EXISTS `music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `music` (
  `music_avatar` varchar(1000) DEFAULT NULL,
  `music_name` varchar(20) DEFAULT NULL,
  `id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music`
--

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES ('https://p3-pc.douyinpic.com/img/aweme-avatar/mosaic-legacy_20b7700050147c01968f3~c5_168x168.jpeg?from=2956013662','月牙湾',9);
/*!40000 ALTER TABLE `music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nav`
--

DROP TABLE IF EXISTS `nav`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `nav` (
  `nav_id` int(11) DEFAULT NULL,
  `nav_name` int(11) DEFAULT NULL,
  `nav_img` varchar(5000) DEFAULT NULL,
  `nav_href` int(11) DEFAULT NULL,
  `nav_type` varchar(50) DEFAULT NULL,
  `nav_lev` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nav`
--

LOCK TABLES `nav` WRITE;
/*!40000 ALTER TABLE `nav` DISABLE KEYS */;
INSERT INTO `nav` VALUES (1,1,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,'shop',1,1),(2,3,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,'shop',1,1),(3,5,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,'shop',1,1),(4,6,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,'shop',1,1),(5,7,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,'shop',0,0),(NULL,NULL,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,NULL,NULL,NULL),(NULL,NULL,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,NULL,NULL,NULL),(NULL,NULL,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,NULL,NULL,NULL),(NULL,NULL,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,NULL,NULL,NULL),(NULL,NULL,'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/icon/%E6%B6%82%E6%8A%B9%E6%B6%88%E9%99%A4day.png',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `nav` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `order_detail` (
  `id` bigint(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `dish_id` bigint(20) DEFAULT NULL,
  `setmeal_id` bigint(20) DEFAULT NULL,
  `dish_flavor` varchar(50) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (1804781709729296386,'米饭','ee04a05a-1230-46b6-8ad5-1a95b140fff3.png',1804781709662187522,1413385247889891330,NULL,NULL,1,2.00),(1804781709729296387,'嗡嗡嗡','8575f75a-89ec-402b-81dc-2f8fc8a3a901.jpg',1804781709662187522,1788590919193358338,NULL,'不要香菜',1,100.00),(1804781709729296388,'麻辣水煮鱼','1fdbfbf3-1d86-4b29-a3fc-46345852f2f8.jpg',1804781709662187522,1397854652581064706,NULL,'不要香菜,中辣',1,148.00),(1804781938981564418,'儿童套餐A计划','61d20592-b37f-4d72-a864-07ad5bb8f3bb.jpg',1804781938981564417,NULL,1415580119015145474,NULL,1,40.00),(1912433364825018370,'口味蛇','0f4bd884-dc9c-4cf9-b59e-7d5958fec3dd.jpg',1912433302858371074,1397851668262465537,NULL,'多冰',2,168.00);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `orders` (
  `id` bigint(20) DEFAULT NULL,
  `number` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `address_book_id` bigint(20) DEFAULT NULL,
  `order_time` datetime DEFAULT NULL,
  `checkout_time` datetime DEFAULT NULL,
  `pay_method` int(11) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `consignee` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1804781709662187522,'1804781709662187522',2,1767515082532777985,1801541519460118529,'2024-06-23 15:41:05','2024-06-23 15:41:05',1,250.00,'','17379557174','111',NULL,'11'),(1804781938981564417,'1804781938981564417',2,1767515082532777985,1801541519460118529,'2024-06-23 15:42:00','2024-06-23 15:42:00',1,40.00,'','17379557174','111',NULL,'11'),(1912433302858371074,'1912433302858371074',2,128,1801541519460118529,'2025-04-16 17:10:13','2025-04-16 17:10:13',NULL,336.00,NULL,'17379557174','111',NULL,'11');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `permission` (
  `id` int(11) DEFAULT NULL,
  `permission` varchar(500) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'get',NULL),(2,'set',NULL);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `role` (
  `id` int(11) DEFAULT NULL,
  `role` varchar(500) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'admin',NULL),(2,'user',NULL),(NULL,NULL,NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salt`
--

DROP TABLE IF EXISTS `salt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `salt` (
  `username` varchar(5000) DEFAULT NULL,
  `salt` varchar(5000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salt`
--

LOCK TABLES `salt` WRITE;
/*!40000 ALTER TABLE `salt` DISABLE KEYS */;
INSERT INTO `salt` VALUES ('bruceccc','VZ4bhGInhIrMn5tc'),('1','8omHGImKD92vcS9g'),('2','1ycGOlVNmihtWUFc');
/*!40000 ALTER TABLE `salt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `schedule` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,'Schedule Name 1'),(2,'Schedule Name 2'),(3,'Schedule Name 3'),(4,'Schedule Name 4'),(5,'Schedule Name 5'),(6,'Schedule Name 6'),(7,'Schedule Name 7'),(8,'Schedule Name 8'),(9,'Schedule Name 9'),(10,'Schedule Name 10'),(11,'Schedule Name 11'),(12,'Schedule Name 12'),(13,'Schedule Name 13'),(14,'Schedule Name 14'),(15,'Schedule Name 15'),(16,'Schedule Name 16'),(17,'Schedule Name 17'),(18,'Schedule Name 18'),(19,'Schedule Name 19'),(20,'Schedule Name 20'),(21,'Schedule Name 21'),(22,'Schedule Name 22'),(23,'Schedule Name 23'),(24,'Schedule Name 24'),(25,'Schedule Name 25'),(26,'Schedule Name 26'),(27,'Schedule Name 27'),(28,'Schedule Name 28'),(29,'Schedule Name 29'),(30,'Schedule Name 30'),(31,'Schedule Name 31'),(32,'Schedule Name 32'),(33,'Schedule Name 33'),(34,'Schedule Name 34'),(35,'Schedule Name 35'),(36,'Schedule Name 36'),(37,'Schedule Name 37'),(38,'Schedule Name 38'),(39,'Schedule Name 39'),(40,'Schedule Name 40'),(41,'Schedule Name 41'),(42,'Schedule Name 42'),(43,'Schedule Name 43'),(44,'Schedule Name 44'),(45,'Schedule Name 45'),(46,'Schedule Name 46'),(47,'Schedule Name 47'),(48,'Schedule Name 48'),(49,'Schedule Name 49'),(50,'Schedule Name 50'),(51,'Schedule Name 51'),(52,'Schedule Name 52'),(53,'Schedule Name 53'),(54,'Schedule Name 54'),(55,'Schedule Name 55'),(56,'Schedule Name 56'),(57,'Schedule Name 57'),(58,'Schedule Name 58'),(59,'Schedule Name 59'),(60,'Schedule Name 60'),(61,'Schedule Name 61'),(62,'Schedule Name 62'),(63,'Schedule Name 63'),(64,'Schedule Name 64'),(65,'Schedule Name 65'),(66,'Schedule Name 66'),(67,'Schedule Name 67'),(68,'Schedule Name 68'),(69,'Schedule Name 69'),(70,'Schedule Name 70'),(71,'Schedule Name 71'),(72,'Schedule Name 72'),(73,'Schedule Name 73'),(74,'Schedule Name 74'),(75,'Schedule Name 75'),(76,'Schedule Name 76'),(77,'Schedule Name 77'),(78,'Schedule Name 78'),(79,'Schedule Name 79'),(80,'Schedule Name 80'),(81,'Schedule Name 81'),(82,'Schedule Name 82'),(83,'Schedule Name 83'),(84,'Schedule Name 84'),(85,'Schedule Name 85'),(86,'Schedule Name 86'),(87,'Schedule Name 87'),(88,'Schedule Name 88'),(89,'Schedule Name 89'),(90,'Schedule Name 90'),(91,'Schedule Name 91'),(92,'Schedule Name 92'),(93,'Schedule Name 93'),(94,'Schedule Name 94'),(95,'Schedule Name 95'),(96,'Schedule Name 96'),(97,'Schedule Name 97'),(98,'Schedule Name 98'),(99,'Schedule Name 99'),(100,'Schedule Name 100'),(101,'Schedule Name 101'),(102,'Schedule Name 102'),(103,'Schedule Name 103'),(104,'Schedule Name 104'),(105,'Schedule Name 105'),(106,'Schedule Name 106'),(107,'Schedule Name 107'),(108,'Schedule Name 108'),(109,'Schedule Name 109'),(110,'Schedule Name 110'),(111,'Schedule Name 111'),(112,'Schedule Name 112'),(113,'Schedule Name 113'),(114,'Schedule Name 114'),(115,'Schedule Name 115'),(116,'Schedule Name 116'),(117,'Schedule Name 117'),(118,'Schedule Name 118'),(119,'Schedule Name 119'),(120,'Schedule Name 120'),(121,'Schedule Name 121'),(122,'Schedule Name 122'),(123,'Schedule Name 123'),(124,'Schedule Name 124'),(125,'Schedule Name 125'),(126,'Schedule Name 126'),(127,'Schedule Name 127'),(128,'Schedule Name 128'),(129,'Schedule Name 129'),(130,'Schedule Name 130'),(131,'Schedule Name 131'),(132,'Schedule Name 132'),(133,'Schedule Name 133'),(134,'Schedule Name 134'),(135,'Schedule Name 135'),(136,'Schedule Name 136'),(137,'Schedule Name 137'),(138,'Schedule Name 138'),(139,'Schedule Name 139'),(140,'Schedule Name 140'),(141,'Schedule Name 141'),(142,'Schedule Name 142'),(143,'Schedule Name 143'),(144,'Schedule Name 144'),(145,'Schedule Name 145'),(146,'Schedule Name 146'),(147,'Schedule Name 147'),(148,'Schedule Name 148'),(149,'Schedule Name 149'),(150,'Schedule Name 150'),(151,'Schedule Name 151'),(152,'Schedule Name 152'),(153,'Schedule Name 153'),(154,'Schedule Name 154'),(155,'Schedule Name 155'),(156,'Schedule Name 156'),(157,'Schedule Name 157'),(158,'Schedule Name 158'),(159,'Schedule Name 159'),(160,'Schedule Name 160'),(161,'Schedule Name 161'),(162,'Schedule Name 162'),(163,'Schedule Name 163'),(164,'Schedule Name 164'),(165,'Schedule Name 165'),(166,'Schedule Name 166'),(167,'Schedule Name 167'),(168,'Schedule Name 168'),(169,'Schedule Name 169'),(170,'Schedule Name 170'),(171,'Schedule Name 171'),(172,'Schedule Name 172'),(173,'Schedule Name 173'),(174,'Schedule Name 174'),(175,'Schedule Name 175'),(176,'Schedule Name 176'),(177,'Schedule Name 177'),(178,'Schedule Name 178'),(179,'Schedule Name 179'),(180,'Schedule Name 180'),(181,'Schedule Name 181'),(182,'Schedule Name 182'),(183,'Schedule Name 183'),(184,'Schedule Name 184'),(185,'Schedule Name 185'),(186,'Schedule Name 186'),(187,'Schedule Name 187'),(188,'Schedule Name 188'),(189,'Schedule Name 189'),(190,'Schedule Name 190'),(191,'Schedule Name 191'),(192,'Schedule Name 192'),(193,'Schedule Name 193'),(194,'Schedule Name 194'),(195,'Schedule Name 195'),(196,'Schedule Name 196'),(197,'Schedule Name 197'),(198,'Schedule Name 198'),(199,'Schedule Name 199'),(200,'Schedule Name 200'),(201,'Schedule Name 201'),(202,'Schedule Name 202'),(203,'Schedule Name 203'),(204,'Schedule Name 204'),(205,'Schedule Name 205'),(206,'Schedule Name 206'),(207,'Schedule Name 207'),(208,'Schedule Name 208'),(209,'Schedule Name 209'),(210,'Schedule Name 210'),(211,'Schedule Name 211'),(212,'Schedule Name 212'),(213,'Schedule Name 213'),(214,'Schedule Name 214'),(215,'Schedule Name 215'),(216,'Schedule Name 216'),(217,'Schedule Name 217'),(218,'Schedule Name 218'),(219,'Schedule Name 219'),(220,'Schedule Name 220'),(221,'Schedule Name 221'),(222,'Schedule Name 222'),(223,'Schedule Name 223'),(224,'Schedule Name 224'),(225,'Schedule Name 225'),(226,'Schedule Name 226'),(227,'Schedule Name 227'),(228,'Schedule Name 228'),(229,'Schedule Name 229'),(230,'Schedule Name 230'),(231,'Schedule Name 231'),(232,'Schedule Name 232'),(233,'Schedule Name 233'),(234,'Schedule Name 234'),(235,'Schedule Name 235'),(236,'Schedule Name 236'),(237,'Schedule Name 237'),(238,'Schedule Name 238'),(239,'Schedule Name 239'),(240,'Schedule Name 240'),(241,'Schedule Name 241'),(242,'Schedule Name 242'),(243,'Schedule Name 243'),(244,'Schedule Name 244'),(245,'Schedule Name 245'),(246,'Schedule Name 246'),(247,'Schedule Name 247'),(248,'Schedule Name 248'),(249,'Schedule Name 249'),(250,'Schedule Name 250'),(251,'Schedule Name 251'),(252,'Schedule Name 252'),(253,'Schedule Name 253'),(254,'Schedule Name 254'),(255,'Schedule Name 255'),(256,'Schedule Name 256'),(257,'Schedule Name 257'),(258,'Schedule Name 258'),(259,'Schedule Name 259'),(260,'Schedule Name 260'),(261,'Schedule Name 261'),(262,'Schedule Name 262'),(263,'Schedule Name 263'),(264,'Schedule Name 264'),(265,'Schedule Name 265'),(266,'Schedule Name 266'),(267,'Schedule Name 267'),(268,'Schedule Name 268'),(269,'Schedule Name 269'),(270,'Schedule Name 270'),(271,'Schedule Name 271'),(272,'Schedule Name 272'),(273,'Schedule Name 273'),(274,'Schedule Name 274'),(275,'Schedule Name 275'),(276,'Schedule Name 276'),(277,'Schedule Name 277'),(278,'Schedule Name 278'),(279,'Schedule Name 279'),(280,'Schedule Name 280'),(281,'Schedule Name 281'),(282,'Schedule Name 282'),(283,'Schedule Name 283'),(284,'Schedule Name 284'),(285,'Schedule Name 285'),(286,'Schedule Name 286'),(287,'Schedule Name 287'),(288,'Schedule Name 288'),(289,'Schedule Name 289'),(290,'Schedule Name 290'),(291,'Schedule Name 291'),(292,'Schedule Name 292'),(293,'Schedule Name 293'),(294,'Schedule Name 294'),(295,'Schedule Name 295'),(296,'Schedule Name 296'),(297,'Schedule Name 297'),(298,'Schedule Name 298'),(299,'Schedule Name 299'),(300,'Schedule Name 300'),(301,'Schedule Name 301'),(302,'Schedule Name 302'),(303,'Schedule Name 303'),(304,'Schedule Name 304'),(305,'Schedule Name 305'),(306,'Schedule Name 306'),(307,'Schedule Name 307'),(308,'Schedule Name 308'),(309,'Schedule Name 309'),(310,'Schedule Name 310'),(311,'Schedule Name 311'),(312,'Schedule Name 312'),(313,'Schedule Name 313'),(314,'Schedule Name 314'),(315,'Schedule Name 315'),(316,'Schedule Name 316'),(317,'Schedule Name 317'),(318,'Schedule Name 318'),(319,'Schedule Name 319'),(320,'Schedule Name 320'),(321,'Schedule Name 321'),(322,'Schedule Name 322'),(323,'Schedule Name 323'),(324,'Schedule Name 324'),(325,'Schedule Name 325'),(326,'Schedule Name 326'),(327,'Schedule Name 327'),(328,'Schedule Name 328'),(329,'Schedule Name 329'),(330,'Schedule Name 330'),(331,'Schedule Name 331'),(332,'Schedule Name 332'),(333,'Schedule Name 333'),(334,'Schedule Name 334'),(335,'Schedule Name 335'),(336,'Schedule Name 336'),(337,'Schedule Name 337'),(338,'Schedule Name 338'),(339,'Schedule Name 339'),(340,'Schedule Name 340'),(341,'Schedule Name 341'),(342,'Schedule Name 342'),(343,'Schedule Name 343'),(344,'Schedule Name 344'),(345,'Schedule Name 345'),(346,'Schedule Name 346'),(347,'Schedule Name 347'),(348,'Schedule Name 348'),(349,'Schedule Name 349'),(350,'Schedule Name 350'),(351,'Schedule Name 351'),(352,'Schedule Name 352'),(353,'Schedule Name 353'),(354,'Schedule Name 354'),(355,'Schedule Name 355'),(356,'Schedule Name 356'),(357,'Schedule Name 357'),(358,'Schedule Name 358'),(359,'Schedule Name 359'),(360,'Schedule Name 360'),(361,'Schedule Name 361'),(362,'Schedule Name 362'),(363,'Schedule Name 363'),(364,'Schedule Name 364'),(365,'Schedule Name 365'),(366,'Schedule Name 366'),(367,'Schedule Name 367'),(368,'Schedule Name 368'),(369,'Schedule Name 369'),(370,'Schedule Name 370'),(371,'Schedule Name 371'),(372,'Schedule Name 372'),(373,'Schedule Name 373'),(374,'Schedule Name 374'),(375,'Schedule Name 375'),(376,'Schedule Name 376'),(377,'Schedule Name 377'),(378,'Schedule Name 378'),(379,'Schedule Name 379'),(380,'Schedule Name 380'),(381,'Schedule Name 381'),(382,'Schedule Name 382'),(383,'Schedule Name 383'),(384,'Schedule Name 384'),(385,'Schedule Name 385'),(386,'Schedule Name 386'),(387,'Schedule Name 387'),(388,'Schedule Name 388'),(389,'Schedule Name 389'),(390,'Schedule Name 390'),(391,'Schedule Name 391'),(392,'Schedule Name 392'),(393,'Schedule Name 393'),(394,'Schedule Name 394'),(395,'Schedule Name 395'),(396,'Schedule Name 396'),(397,'Schedule Name 397'),(398,'Schedule Name 398'),(399,'Schedule Name 399'),(400,'Schedule Name 400'),(401,'Schedule Name 401'),(402,'Schedule Name 402'),(403,'Schedule Name 403'),(404,'Schedule Name 404'),(405,'Schedule Name 405'),(406,'Schedule Name 406'),(407,'Schedule Name 407'),(408,'Schedule Name 408'),(409,'Schedule Name 409'),(410,'Schedule Name 410'),(411,'Schedule Name 411'),(412,'Schedule Name 412'),(413,'Schedule Name 413'),(414,'Schedule Name 414'),(415,'Schedule Name 415'),(416,'Schedule Name 416'),(417,'Schedule Name 417'),(418,'Schedule Name 418'),(419,'Schedule Name 419'),(420,'Schedule Name 420'),(421,'Schedule Name 421'),(422,'Schedule Name 422'),(423,'Schedule Name 423'),(424,'Schedule Name 424'),(425,'Schedule Name 425'),(426,'Schedule Name 426'),(427,'Schedule Name 427'),(428,'Schedule Name 428'),(429,'Schedule Name 429'),(430,'Schedule Name 430'),(431,'Schedule Name 431'),(432,'Schedule Name 432'),(433,'Schedule Name 433'),(434,'Schedule Name 434'),(435,'Schedule Name 435'),(436,'Schedule Name 436'),(437,'Schedule Name 437'),(438,'Schedule Name 438'),(439,'Schedule Name 439'),(440,'Schedule Name 440'),(441,'Schedule Name 441'),(442,'Schedule Name 442'),(443,'Schedule Name 443'),(444,'Schedule Name 444'),(445,'Schedule Name 445'),(446,'Schedule Name 446'),(447,'Schedule Name 447'),(448,'Schedule Name 448'),(449,'Schedule Name 449'),(450,'Schedule Name 450'),(451,'Schedule Name 451'),(452,'Schedule Name 452'),(453,'Schedule Name 453'),(454,'Schedule Name 454'),(455,'Schedule Name 455'),(456,'Schedule Name 456'),(457,'Schedule Name 457'),(458,'Schedule Name 458'),(459,'Schedule Name 459'),(460,'Schedule Name 460'),(461,'Schedule Name 461'),(462,'Schedule Name 462'),(463,'Schedule Name 463'),(464,'Schedule Name 464'),(465,'Schedule Name 465'),(466,'Schedule Name 466'),(467,'Schedule Name 467'),(468,'Schedule Name 468'),(469,'Schedule Name 469'),(470,'Schedule Name 470'),(471,'Schedule Name 471'),(472,'Schedule Name 472'),(473,'Schedule Name 473'),(474,'Schedule Name 474'),(475,'Schedule Name 475'),(476,'Schedule Name 476'),(477,'Schedule Name 477'),(478,'Schedule Name 478'),(479,'Schedule Name 479'),(480,'Schedule Name 480'),(481,'Schedule Name 481'),(482,'Schedule Name 482'),(483,'Schedule Name 483'),(484,'Schedule Name 484'),(485,'Schedule Name 485'),(486,'Schedule Name 486'),(487,'Schedule Name 487'),(488,'Schedule Name 488'),(489,'Schedule Name 489'),(490,'Schedule Name 490'),(491,'Schedule Name 491'),(492,'Schedule Name 492'),(493,'Schedule Name 493'),(494,'Schedule Name 494'),(495,'Schedule Name 495'),(496,'Schedule Name 496'),(497,'Schedule Name 497'),(498,'Schedule Name 498'),(499,'Schedule Name 499'),(500,'Schedule Name 500'),(501,'Schedule Name 501'),(502,'Schedule Name 502'),(503,'Schedule Name 503'),(504,'Schedule Name 504'),(505,'Schedule Name 505'),(506,'Schedule Name 506'),(507,'Schedule Name 507'),(508,'Schedule Name 508'),(509,'Schedule Name 509'),(510,'Schedule Name 510'),(511,'Schedule Name 511'),(512,'Schedule Name 512'),(513,'Schedule Name 513'),(514,'Schedule Name 514'),(515,'Schedule Name 515'),(516,'Schedule Name 516'),(517,'Schedule Name 517'),(518,'Schedule Name 518'),(519,'Schedule Name 519'),(520,'Schedule Name 520'),(521,'Schedule Name 521'),(522,'Schedule Name 522'),(523,'Schedule Name 523'),(524,'Schedule Name 524'),(525,'Schedule Name 525'),(526,'Schedule Name 526'),(527,'Schedule Name 527'),(528,'Schedule Name 528'),(529,'Schedule Name 529'),(530,'Schedule Name 530'),(531,'Schedule Name 531'),(532,'Schedule Name 532'),(533,'Schedule Name 533'),(534,'Schedule Name 534'),(535,'Schedule Name 535'),(536,'Schedule Name 536'),(537,'Schedule Name 537'),(538,'Schedule Name 538'),(539,'Schedule Name 539'),(540,'Schedule Name 540'),(541,'Schedule Name 541'),(542,'Schedule Name 542'),(543,'Schedule Name 543'),(544,'Schedule Name 544'),(545,'Schedule Name 545'),(546,'Schedule Name 546'),(547,'Schedule Name 547'),(548,'Schedule Name 548'),(549,'Schedule Name 549'),(550,'Schedule Name 550'),(551,'Schedule Name 551'),(552,'Schedule Name 552'),(553,'Schedule Name 553'),(554,'Schedule Name 554'),(555,'Schedule Name 555'),(556,'Schedule Name 556'),(557,'Schedule Name 557'),(558,'Schedule Name 558'),(559,'Schedule Name 559'),(560,'Schedule Name 560'),(561,'Schedule Name 561'),(562,'Schedule Name 562'),(563,'Schedule Name 563'),(564,'Schedule Name 564'),(565,'Schedule Name 565'),(566,'Schedule Name 566'),(567,'Schedule Name 567'),(568,'Schedule Name 568'),(569,'Schedule Name 569'),(570,'Schedule Name 570'),(571,'Schedule Name 571'),(572,'Schedule Name 572'),(573,'Schedule Name 573'),(574,'Schedule Name 574'),(575,'Schedule Name 575'),(576,'Schedule Name 576'),(577,'Schedule Name 577'),(578,'Schedule Name 578'),(579,'Schedule Name 579'),(580,'Schedule Name 580'),(581,'Schedule Name 581'),(582,'Schedule Name 582'),(583,'Schedule Name 583'),(584,'Schedule Name 584'),(585,'Schedule Name 585'),(586,'Schedule Name 586'),(587,'Schedule Name 587'),(588,'Schedule Name 588'),(589,'Schedule Name 589'),(590,'Schedule Name 590'),(591,'Schedule Name 591'),(592,'Schedule Name 592'),(593,'Schedule Name 593'),(594,'Schedule Name 594'),(595,'Schedule Name 595'),(596,'Schedule Name 596'),(597,'Schedule Name 597'),(598,'Schedule Name 598'),(599,'Schedule Name 599'),(600,'Schedule Name 600'),(601,'Schedule Name 601'),(602,'Schedule Name 602'),(603,'Schedule Name 603'),(604,'Schedule Name 604'),(605,'Schedule Name 605'),(606,'Schedule Name 606'),(607,'Schedule Name 607'),(608,'Schedule Name 608'),(609,'Schedule Name 609'),(610,'Schedule Name 610'),(611,'Schedule Name 611'),(612,'Schedule Name 612'),(613,'Schedule Name 613'),(614,'Schedule Name 614'),(615,'Schedule Name 615'),(616,'Schedule Name 616'),(617,'Schedule Name 617'),(618,'Schedule Name 618'),(619,'Schedule Name 619'),(620,'Schedule Name 620'),(621,'Schedule Name 621'),(622,'Schedule Name 622'),(623,'Schedule Name 623'),(624,'Schedule Name 624'),(625,'Schedule Name 625'),(626,'Schedule Name 626'),(627,'Schedule Name 627'),(628,'Schedule Name 628'),(629,'Schedule Name 629'),(630,'Schedule Name 630'),(631,'Schedule Name 631'),(632,'Schedule Name 632'),(633,'Schedule Name 633'),(634,'Schedule Name 634'),(635,'Schedule Name 635'),(636,'Schedule Name 636'),(637,'Schedule Name 637'),(638,'Schedule Name 638'),(639,'Schedule Name 639'),(640,'Schedule Name 640'),(641,'Schedule Name 641'),(642,'Schedule Name 642'),(643,'Schedule Name 643'),(644,'Schedule Name 644'),(645,'Schedule Name 645'),(646,'Schedule Name 646'),(647,'Schedule Name 647'),(648,'Schedule Name 648'),(649,'Schedule Name 649'),(650,'Schedule Name 650'),(651,'Schedule Name 651'),(652,'Schedule Name 652'),(653,'Schedule Name 653'),(654,'Schedule Name 654'),(655,'Schedule Name 655'),(656,'Schedule Name 656'),(657,'Schedule Name 657'),(658,'Schedule Name 658'),(659,'Schedule Name 659'),(660,'Schedule Name 660'),(661,'Schedule Name 661'),(662,'Schedule Name 662'),(663,'Schedule Name 663'),(664,'Schedule Name 664'),(665,'Schedule Name 665'),(666,'Schedule Name 666'),(667,'Schedule Name 667'),(668,'Schedule Name 668'),(669,'Schedule Name 669'),(670,'Schedule Name 670'),(671,'Schedule Name 671'),(672,'Schedule Name 672'),(673,'Schedule Name 673'),(674,'Schedule Name 674'),(675,'Schedule Name 675'),(676,'Schedule Name 676'),(677,'Schedule Name 677'),(678,'Schedule Name 678'),(679,'Schedule Name 679'),(680,'Schedule Name 680'),(681,'Schedule Name 681'),(682,'Schedule Name 682'),(683,'Schedule Name 683'),(684,'Schedule Name 684'),(685,'Schedule Name 685'),(686,'Schedule Name 686'),(687,'Schedule Name 687'),(688,'Schedule Name 688'),(689,'Schedule Name 689'),(690,'Schedule Name 690'),(691,'Schedule Name 691'),(692,'Schedule Name 692'),(693,'Schedule Name 693'),(694,'Schedule Name 694'),(695,'Schedule Name 695'),(696,'Schedule Name 696'),(697,'Schedule Name 697'),(698,'Schedule Name 698'),(699,'Schedule Name 699'),(700,'Schedule Name 700'),(701,'Schedule Name 701'),(702,'Schedule Name 702'),(703,'Schedule Name 703'),(704,'Schedule Name 704'),(705,'Schedule Name 705'),(706,'Schedule Name 706'),(707,'Schedule Name 707'),(708,'Schedule Name 708'),(709,'Schedule Name 709'),(710,'Schedule Name 710'),(711,'Schedule Name 711'),(712,'Schedule Name 712'),(713,'Schedule Name 713'),(714,'Schedule Name 714'),(715,'Schedule Name 715'),(716,'Schedule Name 716'),(717,'Schedule Name 717'),(718,'Schedule Name 718'),(719,'Schedule Name 719'),(720,'Schedule Name 720'),(721,'Schedule Name 721'),(722,'Schedule Name 722'),(723,'Schedule Name 723'),(724,'Schedule Name 724'),(725,'Schedule Name 725'),(726,'Schedule Name 726'),(727,'Schedule Name 727'),(728,'Schedule Name 728'),(729,'Schedule Name 729'),(730,'Schedule Name 730'),(731,'Schedule Name 731'),(732,'Schedule Name 732'),(733,'Schedule Name 733'),(734,'Schedule Name 734'),(735,'Schedule Name 735'),(736,'Schedule Name 736'),(737,'Schedule Name 737'),(738,'Schedule Name 738'),(739,'Schedule Name 739'),(740,'Schedule Name 740'),(741,'Schedule Name 741'),(742,'Schedule Name 742'),(743,'Schedule Name 743'),(744,'Schedule Name 744'),(745,'Schedule Name 745'),(746,'Schedule Name 746'),(747,'Schedule Name 747'),(748,'Schedule Name 748'),(749,'Schedule Name 749'),(750,'Schedule Name 750'),(751,'Schedule Name 751'),(752,'Schedule Name 752'),(753,'Schedule Name 753'),(754,'Schedule Name 754'),(755,'Schedule Name 755'),(756,'Schedule Name 756'),(757,'Schedule Name 757'),(758,'Schedule Name 758'),(759,'Schedule Name 759'),(760,'Schedule Name 760'),(761,'Schedule Name 761'),(762,'Schedule Name 762'),(763,'Schedule Name 763'),(764,'Schedule Name 764'),(765,'Schedule Name 765'),(766,'Schedule Name 766'),(767,'Schedule Name 767'),(768,'Schedule Name 768'),(769,'Schedule Name 769'),(770,'Schedule Name 770'),(771,'Schedule Name 771'),(772,'Schedule Name 772'),(773,'Schedule Name 773'),(774,'Schedule Name 774'),(775,'Schedule Name 775'),(776,'Schedule Name 776'),(777,'Schedule Name 777'),(778,'Schedule Name 778'),(779,'Schedule Name 779'),(780,'Schedule Name 780'),(781,'Schedule Name 781'),(782,'Schedule Name 782'),(783,'Schedule Name 783'),(784,'Schedule Name 784'),(785,'Schedule Name 785'),(786,'Schedule Name 786'),(787,'Schedule Name 787'),(788,'Schedule Name 788'),(789,'Schedule Name 789'),(790,'Schedule Name 790'),(791,'Schedule Name 791'),(792,'Schedule Name 792'),(793,'Schedule Name 793'),(794,'Schedule Name 794'),(795,'Schedule Name 795'),(796,'Schedule Name 796'),(797,'Schedule Name 797'),(798,'Schedule Name 798'),(799,'Schedule Name 799'),(800,'Schedule Name 800'),(801,'Schedule Name 801'),(802,'Schedule Name 802'),(803,'Schedule Name 803'),(804,'Schedule Name 804'),(805,'Schedule Name 805'),(806,'Schedule Name 806'),(807,'Schedule Name 807'),(808,'Schedule Name 808'),(809,'Schedule Name 809'),(810,'Schedule Name 810'),(811,'Schedule Name 811'),(812,'Schedule Name 812'),(813,'Schedule Name 813'),(814,'Schedule Name 814'),(815,'Schedule Name 815'),(816,'Schedule Name 816'),(817,'Schedule Name 817'),(818,'Schedule Name 818'),(819,'Schedule Name 819'),(820,'Schedule Name 820'),(821,'Schedule Name 821'),(822,'Schedule Name 822'),(823,'Schedule Name 823'),(824,'Schedule Name 824'),(825,'Schedule Name 825'),(826,'Schedule Name 826'),(827,'Schedule Name 827'),(828,'Schedule Name 828'),(829,'Schedule Name 829'),(830,'Schedule Name 830'),(831,'Schedule Name 831'),(832,'Schedule Name 832'),(833,'Schedule Name 833'),(834,'Schedule Name 834'),(835,'Schedule Name 835'),(836,'Schedule Name 836'),(837,'Schedule Name 837'),(838,'Schedule Name 838'),(839,'Schedule Name 839'),(840,'Schedule Name 840'),(841,'Schedule Name 841'),(842,'Schedule Name 842'),(843,'Schedule Name 843'),(844,'Schedule Name 844'),(845,'Schedule Name 845'),(846,'Schedule Name 846'),(847,'Schedule Name 847'),(848,'Schedule Name 848'),(849,'Schedule Name 849'),(850,'Schedule Name 850'),(851,'Schedule Name 851'),(852,'Schedule Name 852'),(853,'Schedule Name 853'),(854,'Schedule Name 854'),(855,'Schedule Name 855'),(856,'Schedule Name 856'),(857,'Schedule Name 857'),(858,'Schedule Name 858'),(859,'Schedule Name 859'),(860,'Schedule Name 860'),(861,'Schedule Name 861'),(862,'Schedule Name 862'),(863,'Schedule Name 863'),(864,'Schedule Name 864'),(865,'Schedule Name 865'),(866,'Schedule Name 866'),(867,'Schedule Name 867'),(868,'Schedule Name 868'),(869,'Schedule Name 869'),(870,'Schedule Name 870'),(871,'Schedule Name 871'),(872,'Schedule Name 872'),(873,'Schedule Name 873'),(874,'Schedule Name 874'),(875,'Schedule Name 875'),(876,'Schedule Name 876'),(877,'Schedule Name 877'),(878,'Schedule Name 878'),(879,'Schedule Name 879'),(880,'Schedule Name 880'),(881,'Schedule Name 881'),(882,'Schedule Name 882'),(883,'Schedule Name 883'),(884,'Schedule Name 884'),(885,'Schedule Name 885'),(886,'Schedule Name 886'),(887,'Schedule Name 887'),(888,'Schedule Name 888'),(889,'Schedule Name 889'),(890,'Schedule Name 890'),(891,'Schedule Name 891'),(892,'Schedule Name 892'),(893,'Schedule Name 893'),(894,'Schedule Name 894'),(895,'Schedule Name 895'),(896,'Schedule Name 896'),(897,'Schedule Name 897'),(898,'Schedule Name 898'),(899,'Schedule Name 899'),(900,'Schedule Name 900'),(901,'Schedule Name 901'),(902,'Schedule Name 902'),(903,'Schedule Name 903'),(904,'Schedule Name 904'),(905,'Schedule Name 905'),(906,'Schedule Name 906'),(907,'Schedule Name 907'),(908,'Schedule Name 908'),(909,'Schedule Name 909'),(910,'Schedule Name 910'),(911,'Schedule Name 911'),(912,'Schedule Name 912'),(913,'Schedule Name 913'),(914,'Schedule Name 914'),(915,'Schedule Name 915'),(916,'Schedule Name 916'),(917,'Schedule Name 917'),(918,'Schedule Name 918'),(919,'Schedule Name 919'),(920,'Schedule Name 920'),(921,'Schedule Name 921'),(922,'Schedule Name 922'),(923,'Schedule Name 923'),(924,'Schedule Name 924'),(925,'Schedule Name 925'),(926,'Schedule Name 926'),(927,'Schedule Name 927'),(928,'Schedule Name 928'),(929,'Schedule Name 929'),(930,'Schedule Name 930'),(931,'Schedule Name 931'),(932,'Schedule Name 932'),(933,'Schedule Name 933'),(934,'Schedule Name 934'),(935,'Schedule Name 935'),(936,'Schedule Name 936'),(937,'Schedule Name 937'),(938,'Schedule Name 938'),(939,'Schedule Name 939'),(940,'Schedule Name 940'),(941,'Schedule Name 941'),(942,'Schedule Name 942'),(943,'Schedule Name 943'),(944,'Schedule Name 944'),(945,'Schedule Name 945'),(946,'Schedule Name 946'),(947,'Schedule Name 947'),(948,'Schedule Name 948'),(949,'Schedule Name 949'),(950,'Schedule Name 950'),(951,'Schedule Name 951'),(952,'Schedule Name 952'),(953,'Schedule Name 953'),(954,'Schedule Name 954'),(955,'Schedule Name 955'),(956,'Schedule Name 956'),(957,'Schedule Name 957'),(958,'Schedule Name 958'),(959,'Schedule Name 959'),(960,'Schedule Name 960'),(961,'Schedule Name 961'),(962,'Schedule Name 962'),(963,'Schedule Name 963'),(964,'Schedule Name 964'),(965,'Schedule Name 965'),(966,'Schedule Name 966'),(967,'Schedule Name 967'),(968,'Schedule Name 968'),(969,'Schedule Name 969'),(970,'Schedule Name 970'),(971,'Schedule Name 971'),(972,'Schedule Name 972'),(973,'Schedule Name 973'),(974,'Schedule Name 974'),(975,'Schedule Name 975'),(976,'Schedule Name 976'),(977,'Schedule Name 977'),(978,'Schedule Name 978'),(979,'Schedule Name 979'),(980,'Schedule Name 980'),(981,'Schedule Name 981'),(982,'Schedule Name 982'),(983,'Schedule Name 983'),(984,'Schedule Name 984'),(985,'Schedule Name 985'),(986,'Schedule Name 986'),(987,'Schedule Name 987'),(988,'Schedule Name 988'),(989,'Schedule Name 989'),(990,'Schedule Name 990'),(991,'Schedule Name 991'),(992,'Schedule Name 992'),(993,'Schedule Name 993'),(994,'Schedule Name 994'),(995,'Schedule Name 995'),(996,'Schedule Name 996'),(997,'Schedule Name 997'),(998,'Schedule Name 998'),(999,'Schedule Name 999'),(1000,'Schedule Name 1000');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `secruy`
--

DROP TABLE IF EXISTS `secruy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `secruy` (
  `id` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `secruy`
--

LOCK TABLES `secruy` WRITE;
/*!40000 ALTER TABLE `secruy` DISABLE KEYS */;
INSERT INTO `secruy` VALUES (1,'bruce','bruce'),(2,'zhangsan','123456');
/*!40000 ALTER TABLE `secruy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setmeal`
--

DROP TABLE IF EXISTS `setmeal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `setmeal` (
  `id` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `code` varchar(32) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setmeal`
--

LOCK TABLES `setmeal` WRITE;
/*!40000 ALTER TABLE `setmeal` DISABLE KEYS */;
INSERT INTO `setmeal` VALUES (1415580119015145474,1413386191767674881,'儿童套餐A计划',4000.00,1,'','','61d20592-b37f-4d72-a864-07ad5bb8f3bb.jpg','2021-07-15 15:52:55','2021-07-15 15:52:55',1415576781934608386,1415576781934608386,0),(1788590605555888129,1413342269393674242,'猪屎',88800.00,1,'','好吃','ea1fe5ea-be69-4509-a44f-863248064e4d.jpg','2024-05-09 23:23:25','2024-05-09 23:23:25',1,1,0);
/*!40000 ALTER TABLE `setmeal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setmeal_dish`
--

DROP TABLE IF EXISTS `setmeal_dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `setmeal_dish` (
  `id` bigint(20) DEFAULT NULL,
  `setmeal_id` varchar(32) DEFAULT NULL,
  `dish_id` varchar(32) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `copies` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setmeal_dish`
--

LOCK TABLES `setmeal_dish` WRITE;
/*!40000 ALTER TABLE `setmeal_dish` DISABLE KEYS */;
INSERT INTO `setmeal_dish` VALUES (1415580119052894209,'1415580119015145474','1397862198033297410','老火靓汤',49800.00,1,0,'2021-07-15 15:52:55','2021-07-15 15:52:55',1415576781934608386,1415576781934608386,0),(1415580119061282817,'1415580119015145474','1413342036832100354','北冰洋',500.00,1,0,'2021-07-15 15:52:55','2021-07-15 15:52:55',1415576781934608386,1415576781934608386,0),(1415580119069671426,'1415580119015145474','1413385247889891330','米饭',200.00,1,0,'2021-07-15 15:52:55','2021-07-15 15:52:55',1415576781934608386,1415576781934608386,0),(1788590605576859649,'1788590605555888129','1397851668262465537','口味蛇',16800.00,2,0,'2024-05-09 23:23:25','2024-05-09 23:23:25',1,1,0);
/*!40000 ALTER TABLE `setmeal_dish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop`
--

DROP TABLE IF EXISTS `shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `shop` (
  `shop_id` int(11) DEFAULT NULL,
  `shop_name` varchar(500) DEFAULT NULL,
  `shop_price` int(11) DEFAULT NULL,
  `shop_desc` varchar(500) DEFAULT NULL,
  `shop_img` varchar(5000) DEFAULT NULL,
  `shop_info` varchar(5000) DEFAULT NULL,
  `shop_type` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop`
--

LOCK TABLES `shop` WRITE;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` VALUES (1,'Shop 1',134,'Description for Shop 1','http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960','Info about Shop 1',NULL),(2,'Shop 2',307,'Description for Shop 2','http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960','Info about Shop 2',NULL),(3,'Shop 3',1032,'Description for Shop 3','http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960','Info about Shop 3',NULL),(4,'Shop 4',138,'Description for Shop 4','https://example.com/images/shop4.jpg','Info about Shop 4',NULL),(5,'Shop 5',498,'Description for Shop 5','http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960','Info about Shop 5',NULL),(6,'Shop 6',974,'Description for Shop 6','https://example.com/images/shop6.jpg','Info about Shop 6',NULL),(7,'Shop 7',275,'Description for Shop 7','https://example.com/images/shop7.jpg','Info about Shop 7',NULL),(8,'Shop 8',355,'Description for Shop 8','https://example.com/images/shop8.jpg','Info about Shop 8',NULL),(9,'Shop 9',853,'Description for Shop 9','https://example.com/images/shop9.jpg','Info about Shop 9',NULL),(10,'Shop 10',1097,'Description for Shop 10','https://example.com/images/shop10.jpg','Info about Shop 10',NULL),(11,'Shop 11',827,'Description for Shop 11','https://example.com/images/shop11.jpg','Info about Shop 11',NULL),(12,'Shop 12',744,'Description for Shop 12','https://example.com/images/shop12.jpg','Info about Shop 12',NULL),(13,'Shop 13',140,'Description for Shop 13','https://example.com/images/shop13.jpg','Info about Shop 13',NULL),(14,'Shop 14',369,'Description for Shop 14','https://example.com/images/shop14.jpg','Info about Shop 14',NULL),(15,'Shop 15',324,'Description for Shop 15','https://example.com/images/shop15.jpg','Info about Shop 15',NULL),(16,'Shop 16',413,'Description for Shop 16','https://example.com/images/shop16.jpg','Info about Shop 16',NULL),(17,'Shop 17',995,'Description for Shop 17','https://example.com/images/shop17.jpg','Info about Shop 17',NULL),(18,'Shop 18',635,'Description for Shop 18','https://example.com/images/shop18.jpg','Info about Shop 18',NULL),(19,'Shop 19',1091,'Description for Shop 19','https://example.com/images/shop19.jpg','Info about Shop 19',NULL),(20,'Shop 20',449,'Description for Shop 20','https://example.com/images/shop20.jpg','Info about Shop 20',NULL),(21,'Shop 21',873,'Description for Shop 21','https://example.com/images/shop21.jpg','Info about Shop 21',NULL),(22,'Shop 22',918,'Description for Shop 22','https://example.com/images/shop22.jpg','Info about Shop 22',NULL),(23,'Shop 23',874,'Description for Shop 23','https://example.com/images/shop23.jpg','Info about Shop 23',NULL),(24,'Shop 24',516,'Description for Shop 24','https://example.com/images/shop24.jpg','Info about Shop 24',NULL),(25,'Shop 25',860,'Description for Shop 25','https://example.com/images/shop25.jpg','Info about Shop 25',NULL),(26,'Shop 26',651,'Description for Shop 26','https://example.com/images/shop26.jpg','Info about Shop 26',NULL),(27,'Shop 27',575,'Description for Shop 27','https://example.com/images/shop27.jpg','Info about Shop 27',NULL),(28,'Shop 28',822,'Description for Shop 28','https://example.com/images/shop28.jpg','Info about Shop 28',NULL),(29,'Shop 29',284,'Description for Shop 29','https://example.com/images/shop29.jpg','Info about Shop 29',NULL),(30,'Shop 30',857,'Description for Shop 30','https://example.com/images/shop30.jpg','Info about Shop 30',NULL),(31,'Shop 31',333,'Description for Shop 31','https://example.com/images/shop31.jpg','Info about Shop 31',NULL),(32,'Shop 32',994,'Description for Shop 32','https://example.com/images/shop32.jpg','Info about Shop 32',NULL),(33,'Shop 33',873,'Description for Shop 33','https://example.com/images/shop33.jpg','Info about Shop 33',NULL),(34,'Shop 34',283,'Description for Shop 34','https://example.com/images/shop34.jpg','Info about Shop 34',NULL),(35,'Shop 35',695,'Description for Shop 35','https://example.com/images/shop35.jpg','Info about Shop 35',NULL),(36,'Shop 36',529,'Description for Shop 36','https://example.com/images/shop36.jpg','Info about Shop 36',NULL),(37,'Shop 37',460,'Description for Shop 37','https://example.com/images/shop37.jpg','Info about Shop 37',NULL),(38,'Shop 38',612,'Description for Shop 38','https://example.com/images/shop38.jpg','Info about Shop 38',NULL),(39,'Shop 39',582,'Description for Shop 39','https://example.com/images/shop39.jpg','Info about Shop 39',NULL),(40,'Shop 40',972,'Description for Shop 40','https://example.com/images/shop40.jpg','Info about Shop 40',NULL),(41,'Shop 41',1017,'Description for Shop 41','https://example.com/images/shop41.jpg','Info about Shop 41',NULL),(42,'Shop 42',1069,'Description for Shop 42','https://example.com/images/shop42.jpg','Info about Shop 42',NULL),(43,'Shop 43',193,'Description for Shop 43','https://example.com/images/shop43.jpg','Info about Shop 43',NULL),(44,'Shop 44',660,'Description for Shop 44','https://example.com/images/shop44.jpg','Info about Shop 44',NULL),(45,'Shop 45',622,'Description for Shop 45','https://example.com/images/shop45.jpg','Info about Shop 45',NULL),(46,'Shop 46',1029,'Description for Shop 46','https://example.com/images/shop46.jpg','Info about Shop 46',NULL),(47,'Shop 47',179,'Description for Shop 47','https://example.com/images/shop47.jpg','Info about Shop 47',NULL),(48,'Shop 48',711,'Description for Shop 48','https://example.com/images/shop48.jpg','Info about Shop 48',NULL),(49,'Shop 49',918,'Description for Shop 49','https://example.com/images/shop49.jpg','Info about Shop 49',NULL),(50,'Shop 50',357,'Description for Shop 50','https://example.com/images/shop50.jpg','Info about Shop 50',NULL),(51,'Shop 51',932,'Description for Shop 51','https://example.com/images/shop51.jpg','Info about Shop 51',NULL),(52,'Shop 52',491,'Description for Shop 52','https://example.com/images/shop52.jpg','Info about Shop 52',NULL),(53,'Shop 53',559,'Description for Shop 53','https://example.com/images/shop53.jpg','Info about Shop 53',NULL),(54,'Shop 54',221,'Description for Shop 54','https://example.com/images/shop54.jpg','Info about Shop 54',NULL),(55,'Shop 55',329,'Description for Shop 55','https://example.com/images/shop55.jpg','Info about Shop 55',NULL),(56,'Shop 56',884,'Description for Shop 56','https://example.com/images/shop56.jpg','Info about Shop 56',NULL),(57,'Shop 57',333,'Description for Shop 57','https://example.com/images/shop57.jpg','Info about Shop 57',NULL),(58,'Shop 58',914,'Description for Shop 58','https://example.com/images/shop58.jpg','Info about Shop 58',NULL),(59,'Shop 59',472,'Description for Shop 59','https://example.com/images/shop59.jpg','Info about Shop 59',NULL),(60,'Shop 60',516,'Description for Shop 60','https://example.com/images/shop60.jpg','Info about Shop 60',NULL),(61,'Shop 61',1067,'Description for Shop 61','https://example.com/images/shop61.jpg','Info about Shop 61',NULL),(62,'Shop 62',688,'Description for Shop 62','https://example.com/images/shop62.jpg','Info about Shop 62',NULL),(63,'Shop 63',137,'Description for Shop 63','https://example.com/images/shop63.jpg','Info about Shop 63',NULL),(64,'Shop 64',523,'Description for Shop 64','https://example.com/images/shop64.jpg','Info about Shop 64',NULL),(65,'Shop 65',103,'Description for Shop 65','https://example.com/images/shop65.jpg','Info about Shop 65',NULL),(66,'Shop 66',849,'Description for Shop 66','https://example.com/images/shop66.jpg','Info about Shop 66',NULL),(67,'Shop 67',835,'Description for Shop 67','https://example.com/images/shop67.jpg','Info about Shop 67',NULL),(68,'Shop 68',528,'Description for Shop 68','https://example.com/images/shop68.jpg','Info about Shop 68',NULL),(69,'Shop 69',1037,'Description for Shop 69','https://example.com/images/shop69.jpg','Info about Shop 69',NULL),(70,'Shop 70',501,'Description for Shop 70','https://example.com/images/shop70.jpg','Info about Shop 70',NULL),(71,'Shop 71',295,'Description for Shop 71','https://example.com/images/shop71.jpg','Info about Shop 71',NULL),(72,'Shop 72',870,'Description for Shop 72','https://example.com/images/shop72.jpg','Info about Shop 72',NULL),(73,'Shop 73',368,'Description for Shop 73','https://example.com/images/shop73.jpg','Info about Shop 73',NULL),(74,'Shop 74',129,'Description for Shop 74','https://example.com/images/shop74.jpg','Info about Shop 74',NULL),(75,'Shop 75',441,'Description for Shop 75','https://example.com/images/shop75.jpg','Info about Shop 75',NULL),(76,'Shop 76',720,'Description for Shop 76','https://example.com/images/shop76.jpg','Info about Shop 76',NULL),(77,'Shop 77',175,'Description for Shop 77','https://example.com/images/shop77.jpg','Info about Shop 77',NULL),(78,'Shop 78',618,'Description for Shop 78','https://example.com/images/shop78.jpg','Info about Shop 78',NULL),(79,'Shop 79',463,'Description for Shop 79','https://example.com/images/shop79.jpg','Info about Shop 79',NULL),(80,'Shop 80',364,'Description for Shop 80','https://example.com/images/shop80.jpg','Info about Shop 80',NULL),(81,'Shop 81',332,'Description for Shop 81','https://example.com/images/shop81.jpg','Info about Shop 81',NULL),(82,'Shop 82',469,'Description for Shop 82','https://example.com/images/shop82.jpg','Info about Shop 82',NULL),(83,'Shop 83',248,'Description for Shop 83','https://example.com/images/shop83.jpg','Info about Shop 83',NULL),(84,'Shop 84',736,'Description for Shop 84','https://example.com/images/shop84.jpg','Info about Shop 84',NULL),(85,'Shop 85',835,'Description for Shop 85','https://example.com/images/shop85.jpg','Info about Shop 85',NULL),(86,'Shop 86',867,'Description for Shop 86','https://example.com/images/shop86.jpg','Info about Shop 86',NULL),(87,'Shop 87',730,'Description for Shop 87','https://example.com/images/shop87.jpg','Info about Shop 87',NULL),(88,'Shop 88',949,'Description for Shop 88','https://example.com/images/shop88.jpg','Info about Shop 88',NULL),(89,'Shop 89',454,'Description for Shop 89','https://example.com/images/shop89.jpg','Info about Shop 89',NULL),(90,'Shop 90',326,'Description for Shop 90','https://example.com/images/shop90.jpg','Info about Shop 90',NULL),(91,'Shop 91',169,'Description for Shop 91','https://example.com/images/shop91.jpg','Info about Shop 91',NULL),(92,'Shop 92',766,'Description for Shop 92','https://example.com/images/shop92.jpg','Info about Shop 92',NULL),(93,'Shop 93',226,'Description for Shop 93','https://example.com/images/shop93.jpg','Info about Shop 93',NULL),(94,'Shop 94',732,'Description for Shop 94','https://example.com/images/shop94.jpg','Info about Shop 94',NULL),(95,'Shop 95',880,'Description for Shop 95','https://example.com/images/shop95.jpg','Info about Shop 95',NULL),(96,'Shop 96',108,'Description for Shop 96','https://example.com/images/shop96.jpg','Info about Shop 96',NULL),(97,'Shop 97',797,'Description for Shop 97','https://example.com/images/shop97.jpg','Info about Shop 97',NULL),(98,'Shop 98',563,'Description for Shop 98','https://example.com/images/shop98.jpg','Info about Shop 98',NULL),(99,'Shop 99',326,'Description for Shop 99','https://example.com/images/shop99.jpg','Info about Shop 99',NULL),(100,'Shop 100',839,'Description for Shop 100','https://example.com/images/shop100.jpg','Info about Shop 100',NULL);
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_cart`
--

DROP TABLE IF EXISTS `shopping_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `shopping_cart` (
  `id` bigint(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `dish_id` bigint(20) DEFAULT NULL,
  `setmeal_id` bigint(20) DEFAULT NULL,
  `dish_flavor` varchar(50) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_cart`
--

LOCK TABLES `shopping_cart` WRITE;
/*!40000 ALTER TABLE `shopping_cart` DISABLE KEYS */;
INSERT INTO `shopping_cart` VALUES (1841503409381752833,'邵阳猪血丸子','2a50628e-7758-4c51-9fbb-d37c61cdacad.jpg',1767515082532777985,1397851370462687234,NULL,'多冰,不要香菜,中辣',1,138.00,'2024-10-02 23:40:01');
/*!40000 ALTER TABLE `shopping_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_live`
--

DROP TABLE IF EXISTS `t_live`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_live` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `host_id` bigint(20) NOT NULL COMMENT '主播ID',
  `host_username` varchar(50) NOT NULL COMMENT '主播用户名',
  `title` varchar(100) NOT NULL COMMENT '直播标题',
  `cover_url` varchar(255) DEFAULT NULL COMMENT '直播封面',
  `channel_name` varchar(100) NOT NULL COMMENT '直播频道名称',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '直播状态：0-已结束，1-直播中',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `viewer_count` int(11) DEFAULT '0' COMMENT '观看人数',
  `description` text COMMENT '直播描述',
  `replay_url` varchar(255) DEFAULT NULL COMMENT '直播回放地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_host_id` (`host_id`),
  KEY `idx_status` (`status`),
  KEY `idx_channel_name` (`channel_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='直播表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_live`
--

LOCK TABLES `t_live` WRITE;
/*!40000 ALTER TABLE `t_live` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_live` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_brand`
--

DROP TABLE IF EXISTS `tb_brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tb_brand` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(20) DEFAULT NULL,
  `company_name` varchar(20) DEFAULT NULL,
  `ordered` int(11) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_brand`
--

LOCK TABLES `tb_brand` WRITE;
/*!40000 ALTER TABLE `tb_brand` DISABLE KEYS */;
INSERT INTO `tb_brand` VALUES (1,'三只松鼠','三只松鼠股份有限公司',5,'好吃不上火',0),(2,'华为','华为技术有限公司',100,'华为致力于把数字世界带入每个人、每个家庭、每个组织，构建万物互联的智能世界',1),(3,'小米','小米科技有限公司',50,'are you ok',1);
/*!40000 ALTER TABLE `tb_brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer_record`
--

DROP TABLE IF EXISTS `transfer_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `transfer_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_user_id` bigint(20) NOT NULL,
  `to_user_id` bigint(20) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1930879505975586818 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transfer_record`
--

LOCK TABLES `transfer_record` WRITE;
/*!40000 ALTER TABLE `transfer_record` DISABLE KEYS */;
INSERT INTO `transfer_record` VALUES (1930565445207719938,144,145,33.00,'2025-06-05 10:00:44'),(1930565457257955329,144,145,33.00,'2025-06-05 10:00:47'),(1930565457325064194,144,145,2.00,'2025-06-05 10:00:47'),(1930565457392173057,144,145,2.00,'2025-06-05 10:00:47'),(1930873499681853441,144,145,2.00,'2025-06-06 06:24:50'),(1930873546112798722,144,145,2.00,'2025-06-06 06:25:01'),(1930873680133394434,144,145,2.00,'2025-06-06 06:25:33'),(1930873835372974082,144,145,2.00,'2025-06-06 06:26:10'),(1930874467773337601,144,145,2.00,'2025-06-06 06:28:41'),(1930874497502564353,144,145,2.00,'2025-06-06 06:28:48'),(1930875304029474817,144,145,1.00,'2025-06-06 06:32:01'),(1930875584397725697,144,145,1.00,'2025-06-06 06:33:07'),(1930875605037895682,144,145,1.00,'2025-06-06 06:33:12'),(1930876667417350145,144,145,1.00,'2025-06-06 06:37:26'),(1930876720970223618,144,145,6.00,'2025-06-06 06:37:38'),(1930878514265960449,144,145,6.00,'2025-06-06 06:44:46'),(1930879155323383809,144,145,1.00,'2025-06-06 06:47:19'),(1930879505975586817,144,145,1.00,'2025-06-06 06:48:42');
/*!40000 ALTER TABLE `transfer_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `user_pic` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `back_img` varchar(5000) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `jieshao` varchar(50) DEFAULT NULL,
  `age` varchar(50) DEFAULT NULL,
  `school` varchar(50) DEFAULT NULL,
  `role_id` varchar(500) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  `payment_password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (144,'bruceccc','lTSS+I5bQ524ds6BycaPMQ==','3213','https://p3-pc.douyinpic.com/img/aweme-avatar/mosaic-legacy_3795_3047680722~c5_300x300.jpeg?from=2956013662',NULL,NULL,NULL,NULL,NULL,NULL,'https://img1.baidu.com/it/u=755680603,1751569227&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1424',NULL,NULL,NULL,NULL,NULL,NULL,'111111'),(145,'2945090920@qq.com','lTSS+I5bQ524ds6BycaPMQ==','3123','https://p3-pc.douyinpic.com/img/aweme-avatar/mosaic-legacy_3795_3047680722~c5_300x300.jpeg?from=2956013662',NULL,NULL,NULL,'',NULL,NULL,'https://img1.baidu.com/it/u=755680603,1751569227&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1424',NULL,NULL,NULL,NULL,NULL,NULL,'111111'),(146,'1','fPwkBfNqthmLMucoaYhuUw==',NULL,'https://p3-pc.douyinpic.com/img/aweme-avatar/mosaic-legacy_3795_3047680722~c5_300x300.jpeg?from=2956013662',NULL,NULL,NULL,NULL,NULL,NULL,'https://img1.baidu.com/it/u=755680603,1751569227&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1424',NULL,NULL,NULL,NULL,'user',NULL,NULL),(147,'2','LNeIOan8cnSt0PemIB3sbQ==',NULL,'https://p3-pc.douyinpic.com/img/aweme-avatar/mosaic-legacy_3795_3047680722~c5_300x300.jpeg?from=2956013662',NULL,NULL,NULL,NULL,NULL,NULL,'https://img1.baidu.com/it/u=755680603,1751569227&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1424',NULL,NULL,NULL,NULL,'user',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `video` (
  `video_id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `music_id` bigint(20) DEFAULT NULL,
  `url` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `content` varchar(1000) CHARACTER SET utf8 DEFAULT NULL,
  `title` varchar(5000) CHARACTER SET utf8 DEFAULT NULL,
  `cover` varchar(5000) CHARACTER SET utf8 DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '图文：1  视频：2',
  `status` tinyint(4) DEFAULT NULL COMMENT '审核中：0   正常状态：1   未通过审核：2 '
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES (471938318880801,146,NULL,'http://192.168.200.130:9000/leadnews/videostore/2025/06/12/video_1749712238928.mp4','','hh',NULL,1,NULL);
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallet`
--

DROP TABLE IF EXISTS `wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `wallet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_wallet_user` (`user_id`),
  CONSTRAINT `fk_wallet_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wallet`
--

LOCK TABLES `wallet` WRITE;
/*!40000 ALTER TABLE `wallet` DISABLE KEYS */;
INSERT INTO `wallet` VALUES (1,144,935.00,'2025-06-05 09:59:39'),(2,145,565.00,'2025-06-05 09:59:39');
/*!40000 ALTER TABLE `wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallet_transaction`
--

DROP TABLE IF EXISTS `wallet_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `wallet_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wallet_id` bigint(20) NOT NULL,
  `type` varchar(20) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `balance_after` decimal(10,2) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_transaction_wallet` (`wallet_id`),
  CONSTRAINT `fk_transaction_wallet` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1930879505975586820 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wallet_transaction`
--

LOCK TABLES `wallet_transaction` WRITE;
/*!40000 ALTER TABLE `wallet_transaction` DISABLE KEYS */;
INSERT INTO `wallet_transaction` VALUES (1930565445207719939,1,'TRANSFER_OUT',33.00,967.00,'转出给 145','2025-06-05 10:00:44'),(1930565445207719940,2,'TRANSFER_IN',33.00,533.00,'来自 144','2025-06-05 10:00:44'),(1930565457257955330,1,'TRANSFER_OUT',33.00,967.00,'转出给 145','2025-06-05 10:00:47'),(1930565457257955331,2,'TRANSFER_IN',33.00,533.00,'来自 144','2025-06-05 10:00:47'),(1930565457325064195,1,'TRANSFER_OUT',2.00,998.00,'转出给 145','2025-06-05 10:00:47'),(1930565457325064196,2,'TRANSFER_IN',2.00,502.00,'来自 144','2025-06-05 10:00:47'),(1930565457392173058,1,'TRANSFER_OUT',2.00,965.00,'转出给 145','2025-06-05 10:00:47'),(1930565457392173059,2,'TRANSFER_IN',2.00,535.00,'来自 144','2025-06-05 10:00:47'),(1930873499681853442,1,'TRANSFER_OUT',2.00,963.00,'转出给 145','2025-06-06 06:24:50'),(1930873499681853443,2,'TRANSFER_IN',2.00,537.00,'来自 144','2025-06-06 06:24:50'),(1930873546179907586,1,'TRANSFER_OUT',2.00,961.00,'转出给 145','2025-06-06 06:25:01'),(1930873546179907587,2,'TRANSFER_IN',2.00,539.00,'来自 144','2025-06-06 06:25:01'),(1930873680133394435,1,'TRANSFER_OUT',2.00,959.00,'转出给 145','2025-06-06 06:25:33'),(1930873680133394436,2,'TRANSFER_IN',2.00,541.00,'来自 144','2025-06-06 06:25:33'),(1930873835419111426,1,'TRANSFER_OUT',2.00,957.00,'转出给 145','2025-06-06 06:26:10'),(1930873835419111427,2,'TRANSFER_IN',2.00,543.00,'来自 144','2025-06-06 06:26:10'),(1930874467773337602,1,'TRANSFER_OUT',2.00,955.00,'转出给 145','2025-06-06 06:28:41'),(1930874467773337603,2,'TRANSFER_IN',2.00,545.00,'来自 144','2025-06-06 06:28:41'),(1930874497502564354,1,'TRANSFER_OUT',2.00,953.00,'转出给 145','2025-06-06 06:28:48'),(1930874497569673217,2,'TRANSFER_IN',2.00,547.00,'来自 144','2025-06-06 06:28:48'),(1930875304029474818,1,'TRANSFER_OUT',1.00,952.00,'转出给 145','2025-06-06 06:32:01'),(1930875304029474819,2,'TRANSFER_IN',1.00,548.00,'来自 144','2025-06-06 06:32:01'),(1930875584464834561,1,'TRANSFER_OUT',1.00,951.00,'转出给 145','2025-06-06 06:33:07'),(1930875584464834562,2,'TRANSFER_IN',1.00,549.00,'来自 144','2025-06-06 06:33:07'),(1930875605037895683,1,'TRANSFER_OUT',1.00,950.00,'转出给 145','2025-06-06 06:33:12'),(1930875605037895684,2,'TRANSFER_IN',1.00,550.00,'来自 144','2025-06-06 06:33:12'),(1930876667417350146,1,'TRANSFER_OUT',1.00,949.00,'转出给 145','2025-06-06 06:37:26'),(1930876667417350147,2,'TRANSFER_IN',1.00,551.00,'来自 144','2025-06-06 06:37:26'),(1930876720970223619,1,'TRANSFER_OUT',6.00,943.00,'转出给 145','2025-06-06 06:37:38'),(1930876720970223620,2,'TRANSFER_IN',6.00,557.00,'来自 144','2025-06-06 06:37:38'),(1930878514265960450,1,'TRANSFER_OUT',6.00,937.00,'转出给 145','2025-06-06 06:44:46'),(1930878514265960451,2,'TRANSFER_IN',6.00,563.00,'来自 144','2025-06-06 06:44:46'),(1930879155323383810,1,'TRANSFER_OUT',1.00,936.00,'转出给 145','2025-06-06 06:47:19'),(1930879155323383811,2,'TRANSFER_IN',1.00,564.00,'来自 144','2025-06-06 06:47:19'),(1930879505975586818,1,'TRANSFER_OUT',1.00,935.00,'转出给 145','2025-06-06 06:48:42'),(1930879505975586819,2,'TRANSFER_IN',1.00,565.00,'来自 144','2025-06-06 06:48:42');
/*!40000 ALTER TABLE `wallet_transaction` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-12 17:32:10
