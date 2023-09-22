#MySQL 5.6以及以前的版本可执行
CREATE TABLE `interface_info` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `name` varchar(255) NOT NULL COMMENT '名称',
                                  `description` varchar(256) DEFAULT NULL COMMENT '描述',
                                  `url` varchar(512) NOT NULL COMMENT '接口地址',
                                  `requestParams` text NOT NULL COMMENT '请求参数',
                                  `requestHeader` text COMMENT '请求头',
                                  `responseHeader` text COMMENT '响应头',
                                  `status` int NOT NULL DEFAULT '0' COMMENT '接口状态（0-关闭，1-开启）',
                                  `method` varchar(256) NOT NULL COMMENT '请求类型',
                                  `sdk` varchar(256) DEFAULT NULL COMMENT '请求类型',
                                  `userId` bigint NOT NULL COMMENT '创建人',
                                  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='接口信息';

/*Data for the table `interface_info` */

insert  into `interface_info`(`id`,`name`,`description`,`url`,`requestParams`,`requestHeader`,`responseHeader`,`status`,`method`,`sdk`,`userId`,`createTime`,`updateTime`,`isDelete`) values (1,'getNameByPost','通过post获取名字','http://localhost:9091/api/name','{\"userName\": \"\"}','{\"Content-Type\":\"application/json\"}','{\"Content-Type\":\"application/json\"}',1,'POST','com.lhj.apiclient.NameApiClient',1,'2023-09-20 21:04:50','2023-09-20 22:34:47',0),(2,'getRandomWork','随机文本','http://localhost:9091/api/random/word','null',NULL,'',1,'GET','com.lhj.apiclient.RandomApiClient',1,'2023-09-20 22:40:49','2023-09-21 12:37:17',0),(3,'getRandomImageUrl','随机动漫图片地址','http://localhost:9091/api/random/image','null',NULL,NULL,1,'POST','com.lhj.apiclient.RandomApiClient',1,'2023-09-21 00:08:53','2023-09-21 00:10:38',0),(4,'getDayWallpaperUrl','每日壁纸URL','http://localhost:9091/api/day/wallpaper','null',NULL,NULL,1,'POST','com.lhj.apiclient.DayApiClient',1,'2023-09-21 00:21:09','2023-09-21 00:23:49',0);

/*Table structure for table `user` */

CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                        `userName` varchar(255) DEFAULT NULL COMMENT '用户昵称',
                        `userAccount` varchar(255) NOT NULL COMMENT '账号',
                        `userAvatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
                        `gender` tinyint DEFAULT NULL COMMENT '性别',
                        `userRole` varchar(256) NOT NULL DEFAULT 'user' COMMENT '用户角色：user / admin',
                        `userPassword` varchar(256) NOT NULL COMMENT '密码',
                        `accessKey` varchar(255) NOT NULL COMMENT 'accessKey',
                        `secretKey` varchar(255) NOT NULL COMMENT 'secretKey',
                        `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uni_userAccount` (`userAccount`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='用户';

/*Data for the table `user` */

insert  into `user`(`id`,`userName`,`userAccount`,`userAvatar`,`gender`,`userRole`,`userPassword`,`accessKey`,`secretKey`,`createTime`,`updateTime`,`isDelete`) values (1,NULL,'admin','https://tse2-mm.cn.bing.net/th/id/OIP-C.eDGsX9oJRl51gzRJqfWakgAAAA?w=209&h=204&c=7&r=0&o=5&dpr=1.4&pid=1.7',NULL,'admin','78e2a850e241281585f909b6dcea3863','ca1c07155f069b99893e244fa7fa0f8f','b41ad70e84c32cd32f646ad3834d0856','2023-09-14 19:00:08','2023-09-19 22:50:34',0);

/*Table structure for table `user_interface_info` */

CREATE TABLE `user_interface_info` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `userId` bigint NOT NULL COMMENT '调用用户 id',
                                       `interfaceInfoId` bigint NOT NULL COMMENT '接口 id',
                                       `totalNum` int NOT NULL DEFAULT '0' COMMENT '总调用次数',
                                       `leftNum` int NOT NULL DEFAULT '0' COMMENT '剩余调用次数',
                                       `status` int NOT NULL DEFAULT '0' COMMENT '0-正常，1-禁用',
                                       `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='用户调用接口关系';

/*Data for the table `user_interface_info` */

insert  into `user_interface_info`(`id`,`userId`,`interfaceInfoId`,`totalNum`,`leftNum`,`status`,`createTime`,`updateTime`,`isDelete`) values (1,1,1,8,92,0,'2023-09-20 01:52:04','2023-09-21 00:27:38',0),(2,1,2,10,90,0,'2023-09-20 22:51:24','2023-09-21 12:37:13',0),(3,1,3,1,9,0,'2023-09-21 00:10:48','2023-09-21 00:10:48',0),(4,1,4,1,9,0,'2023-09-21 00:22:34','2023-09-21 00:27:16',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
