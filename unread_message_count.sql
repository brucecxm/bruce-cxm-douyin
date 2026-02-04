CREATE TABLE `unread_message_count` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL COMMENT '接收消息的用户ID',
  `from_user_id` varchar(50) NOT NULL COMMENT '发送消息的用户ID',
  `count` int(11) NOT NULL DEFAULT '0' COMMENT '未读消息数',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_from` (`user_id`,`from_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='未读消息计数表';
