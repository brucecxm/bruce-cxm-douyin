-- MySQL dump 10.13  Distrib 8.0.11, for Win64 (x86_64)
--
-- Host: localhost    Database: douyintest
-- ------------------------------------------------------
-- Server version	8.0.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `douyintest`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `douyintest` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `douyintest`;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `comment` (
  `comment_id` int(11) NOT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `comment_video_id` int(11) DEFAULT NULL,
  `comment_user_id` int(11) DEFAULT NULL,
  `last_id` int(11) DEFAULT NULL,
  `comment_like` int(11) DEFAULT NULL,
  `comment_dislike` int(11) DEFAULT NULL,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'11',1,102,NULL,NULL,NULL),(2,'22',1,102,NULL,NULL,NULL),(3,'33',1,103,NULL,NULL,NULL),(4,'44',1,104,NULL,NULL,NULL),(5,'55',2,101,NULL,NULL,NULL),(6,'66',2,102,NULL,NULL,NULL);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_user`
--

DROP TABLE IF EXISTS `comment_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `comment_user` (
  `user_id` int(11) DEFAULT NULL,
  `comment_id` int(11) DEFAULT NULL,
  `like_or_dislike` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_user`
--

LOCK TABLES `comment_user` WRITE;
/*!40000 ALTER TABLE `comment_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likeone`
--

DROP TABLE IF EXISTS `likeone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `likeone` (
  `like_userid` int(11) DEFAULT NULL,
  `like_videoid` int(11) DEFAULT NULL,
  `share` int(11) DEFAULT NULL,
  `like` int(11) DEFAULT NULL,
  `collect` int(11) DEFAULT NULL,
  `comment` int(11) DEFAULT NULL,
  `follow` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likeone`
--

LOCK TABLES `likeone` WRITE;
/*!40000 ALTER TABLE `likeone` DISABLE KEYS */;
INSERT INTO `likeone` VALUES (101,1,1,1,1,1,1),(101,1,1,1,1,1,1),(101,1,1,1,1,1,1),(101,1,1,1,1,1,1),(101,1,1,1,1,1,1),(101,1,1,1,1,1,1),(101,1,1,1,1,1,1),(101,1,1,1,1,1,1),(101,1,1,1,1,1,1);
/*!40000 ALTER TABLE `likeone` ENABLE KEYS */;
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
  `music_video_id` int(11) NOT NULL,
  PRIMARY KEY (`music_video_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music`
--

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES (NULL,NULL,19),(NULL,NULL,20),(NULL,NULL,21),(NULL,NULL,22),(NULL,NULL,23),('https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662','aaaaa创作的原声',24),('https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662','aaaaa创作的原声',25),('https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662','aaaaa创作的原声',26),('https://p3-pc.douyinpic.com/img/aweme-avatar/mosaic-legacy_20b7700050147c01968f3~c5_168x168.jpeg?from=2956013662','月牙湾',1001),('https://p3-pc.douyinpic.com/img/aweme-avatar/mosaic-legacy_310e3000a130132511b54~c5_168x168.jpeg?from=2956013662','嚣张',1002),('https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662','陷阱',1003),('https://p3-pc.douyinpic.com/img/aweme-avatar/mosaic-legacy_310e3000a130132511b54~c5_168x168.jpeg?from=2956013662','十年',1004);
/*!40000 ALTER TABLE `music` ENABLE KEYS */;
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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `user_pic` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (101,'bruceccc','eee6ac52aa029bfba6f39e500a57f381','aaaaa',NULL,'https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662','2024-06-29 14:02:11','2024-06-29 14:02:11'),(102,'brucebbb','eee6ac52aa029bfba6f39e500a57f381','aaaa',NULL,'https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662','2024-06-29 14:02:54','2024-06-29 14:02:54'),(103,'bruceaaa','eee6ac52aa029bfba6f39e500a57f381','www',NULL,'https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662','2024-06-29 22:18:51','2024-06-29 22:18:51'),(104,'bruceeee','eee6ac52aa029bfba6f39e500a57f381','sss',NULL,'https://p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-avt-0015_c7784af3a0bd2e7c5fc92ece450f12e0~c5_168x168.jpeg?from=2956013662','2024-06-29 22:18:58','2024-06-29 22:18:58'),(105,'chenxueming','25f9e794323b453885f5181f1b624d0b',NULL,NULL,NULL,'2024-08-04 18:01:25','2024-08-04 18:01:25');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `video` (
  `video_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `auth_id` int(11) DEFAULT NULL,
  `music_id` int(11) DEFAULT NULL,
  `video_url` varchar(2000) DEFAULT NULL,
  `video_comment` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`video_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES (1,101,1001,'https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH','第一条内容'),(2,102,1002,'https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH','第2条内容'),(3,103,1003,'https://www.douyin.com/aweme/v1/play/?video_id=v0d00fg10000cj1lq4jc77u0ng6s1gt0&line=0&file_id=bed51c00899b458cbc5d8280147c22a1&sign=7749aec7bd62a3760065f60e40fc1867&is_play_url=1&source=PackSourceEnum_PUBLISH','第3条内容'),(4,104,1004,'https://www.douyin.com/aweme/v1/play/?video_id=v0200f9e0000bldvt42gd9fqp49ecpag&line=0&file_id=f87d7ac5d5a24b19b3f33f1b31ba02e7&sign=c872722944d05b0ccd56ac9f5fb8a51b&is_play_url=1&source=PackSourceEnum_PUBLISH','第4条内容'),(5,101,1001,'http://192.168.200.130:9000/douyin/test.mp4?Content-Disposition=attachment%3B%20filename%3D%22test.mp4%22&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minio%2F20240804%2F%2Fs3%2Faws4_request&X-Amz-Date=20240804T142413Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=fffac422c673dd8a8a46d8257e18a9031a916b9a1aea1f462a7c9133a44d792c','minio的内容'),(6,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(7,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(8,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(9,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(10,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(11,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(12,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(13,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(14,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(15,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(16,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(17,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(18,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(19,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(20,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(21,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(22,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(23,2013,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(24,101,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(25,101,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入'),(26,101,233,'www.douyin.com/aweme/v1/play/?file_id=370f33fcdaa74c9d97632b243e80adb0&is_play_url=1&line=0&sign=167396a9fbd6e693dd934fe73a0e7073&source=PackSourceEnum_SEARCH&uifid=c4683e1a43ffa6bc6852097c712d14b81f04bc9b5ca6d30214b0e66b4e385280917b1ee2dd6c8cf98d22bdbe31e765a617637cb5e3f8c7ea32ab2f6d6adf62d8c038b3a0718002d3d6e612782bc8e4790de3023e098e48eb6e37ef2259ee15803ce304237e3fdbf46a88caae910c209bb4614fbde6a15491bac590af176e8420b5d932e88ca503b38bbe056da33ab0de509ffbf6f2f2a34daa880354b8e41e82&video_id=v0200fg10000cm15vg3c77ua07n478h0&aid=6383','测试插入');
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xiaoxi`
--

DROP TABLE IF EXISTS `xiaoxi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `xiaoxi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chat_user_id` int(11) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `text` varchar(500) DEFAULT NULL,
  `img` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xiaoxi`
--

LOCK TABLES `xiaoxi` WRITE;
/*!40000 ALTER TABLE `xiaoxi` DISABLE KEYS */;
/*!40000 ALTER TABLE `xiaoxi` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-05 18:01:00
