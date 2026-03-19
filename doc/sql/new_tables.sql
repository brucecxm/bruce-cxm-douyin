-- 创建话题表
DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '话题ID',
  `name` varchar(100) NOT NULL COMMENT '话题名称',
  `is_hot` tinyint(4) DEFAULT '0' COMMENT '是否热门话题：0-普通，1-热门',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_is_hot` (`is_hot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='话题表';

-- 创建位置表
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '位置ID',
  `name` varchar(200) NOT NULL COMMENT '位置名称',
  `province` varchar(100) DEFAULT NULL COMMENT '省份',
  `city` varchar(100) DEFAULT NULL COMMENT '城市',
  `district` varchar(100) DEFAULT NULL COMMENT '区县',
  `address` varchar(500) DEFAULT NULL COMMENT '详细地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='位置表';

-- 创建权限设置表
DROP TABLE IF EXISTS `privacy`;
CREATE TABLE `privacy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `name` varchar(100) NOT NULL COMMENT '权限名称',
  `description` varchar(200) DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限设置表';

-- 插入初始数据
INSERT INTO `topic` (`name`, `is_hot`) VALUES 
('#热门抖音助手', 1),
('#即拍即发', 1),
('#新偏橙视生活', 0),
('#美食推荐', 1),
('#旅行日记', 1),
('#宠物日常', 0),
('#健身打卡', 1);

INSERT INTO `location` (`name`, `province`, `city`, `district`) VALUES 
('神州数码西安科技园', '陕西省', '西安市', '雁塔区'),
('陕西大会堂', '陕西省', '西安市', '碑林区'),
('大雁塔', '陕西省', '西安市', '雁塔区'),
('回民街', '陕西省', '西安市', '莲湖区'),
('西安城墙', '陕西省', '西安市', '碑林区'),
('大唐不夜城', '陕西省', '西安市', '雁塔区');

INSERT INTO `privacy` (`name`, `description`) VALUES 
('公开', '所有人可见'),
('仅好友可见', '只有你的好友可以查看'),
('私密', '仅自己可见');
