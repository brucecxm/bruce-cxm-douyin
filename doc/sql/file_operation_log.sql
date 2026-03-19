DROP TABLE IF EXISTS `file_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `file_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型：upload/download/delete',
  `file_url` varchar(2550) DEFAULT NULL COMMENT '文件URL',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小（字节）',
  `file_type` varchar(100) DEFAULT NULL COMMENT '文件类型',
  `ip_address` varchar(50) DEFAULT NULL COMMENT '操作IP地址',
  `status` int(11) DEFAULT 1 COMMENT '操作状态：1成功，0失败',
  `error_message` varchar(500) DEFAULT NULL COMMENT '错误信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件操作记录表';
/*!40101 SET character_set_client = @saved_cs_client */;