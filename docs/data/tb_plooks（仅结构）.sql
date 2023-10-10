/*
 Navicat MySQL Data Transfer

 Source Server         : 本地服务器
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : tb_plooks

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 26/09/2023 19:33:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announces
-- ----------------------------
DROP TABLE IF EXISTS `announces`;
CREATE TABLE `announces`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '链接',
  `important` tinyint(1) NULL DEFAULT 0 COMMENT '重要的',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_announces_deleted_at`(`deleted_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for at_messages
-- ----------------------------
DROP TABLE IF EXISTS `at_messages`;
CREATE TABLE `at_messages`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `vid` int(0) NOT NULL COMMENT '所在视频id',
  `uid` int(0) NOT NULL COMMENT '所属用户ID',
  `fid` int(0) NOT NULL COMMENT '关联用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_at_messages_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_at_messages_vid`(`vid`) USING BTREE,
  INDEX `idx_at_messages_uid`(`uid`) USING BTREE,
  INDEX `idx_at_messages_fid`(`fid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '提及消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for carousels
-- ----------------------------
DROP TABLE IF EXISTS `carousels`;
CREATE TABLE `carousels`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片链接',
  `title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '指向的链接',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主题色',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_carousels_deleted_at`(`deleted_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '轮播图表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp(0) NULL DEFAULT NULL,
  `updated_at` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted_at` timestamp(0) NULL DEFAULT NULL,
  `vid` int(0) NULL DEFAULT NULL,
  `cid` int(0) NULL DEFAULT NULL COMMENT '收藏夹id',
  `uid` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_collect_vid`(`vid`) USING BTREE,
  INDEX `idx_collect_cid`(`cid`) USING BTREE,
  INDEX `idx_collect_uid`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collections
-- ----------------------------
DROP TABLE IF EXISTS `collections`;
CREATE TABLE `collections`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `uid` int(0) NOT NULL COMMENT '所属用户',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收藏夹名称',
  `desc` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '简介',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面',
  `open` tinyint(1) NULL DEFAULT 0 COMMENT '是否公开',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_collections_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_collections_uid`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收藏夹表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `vid` int(0) NOT NULL COMMENT '视频ID',
  `created_at` datetime(0) NOT NULL COMMENT '创建时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `uid` int(0) NOT NULL COMMENT '用户ID',
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `at` json NULL COMMENT '被@的用户ID（JSON格式字符串）',
  `no_more` tinyint(1) NULL DEFAULT 1 COMMENT '没有子评论',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for danmukus
-- ----------------------------
DROP TABLE IF EXISTS `danmukus`;
CREATE TABLE `danmukus`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `vid` int(0) NOT NULL COMMENT '视频ID',
  `part` int(0) NULL DEFAULT 1 COMMENT '分集ID',
  `text` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `time` double NOT NULL COMMENT '时间',
  `mode` int(0) NULL DEFAULT 0 COMMENT '类型0滚动;1顶部;2底部',
  `color` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '#fff' COMMENT '颜色',
  `uid` int(0) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_danmakus_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_danmakus_vid`(`vid`) USING BTREE,
  INDEX `idx_danmakus_uid`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '弹幕表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for follows
-- ----------------------------
DROP TABLE IF EXISTS `follows`;
CREATE TABLE `follows`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `uid` int(0) NOT NULL COMMENT '用户ID',
  `fid` int(0) NOT NULL COMMENT '关注的用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_follows_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_follows_uid`(`uid`) USING BTREE,
  INDEX `idx_follows_fid`(`fid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for histories
-- ----------------------------
DROP TABLE IF EXISTS `histories`;
CREATE TABLE `histories`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `vid` int(0) NOT NULL COMMENT '所在视频id',
  `uid` int(0) NOT NULL COMMENT '所属用户ID',
  `part` int(0) NULL DEFAULT 1 COMMENT '分集',
  `time` double NOT NULL COMMENT '进度',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_histories_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_histories_vid`(`vid`) USING BTREE,
  INDEX `idx_histories_uid`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '历史记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `uid` int(0) NOT NULL,
  `vid` int(0) NOT NULL,
  PRIMARY KEY (`id`, `uid`, `vid`) USING BTREE,
  INDEX `idx_like_uid`(`uid`) USING BTREE,
  INDEX `idx_like_vid`(`vid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for like_messages
-- ----------------------------
DROP TABLE IF EXISTS `like_messages`;
CREATE TABLE `like_messages`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `vid` int(0) NOT NULL COMMENT '所在视频id',
  `uid` int(0) NOT NULL COMMENT '所属用户ID',
  `fid` int(0) NOT NULL COMMENT '发送用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_like_messages_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_like_messages_vid`(`vid`) USING BTREE,
  INDEX `idx_like_messages_uid`(`uid`) USING BTREE,
  INDEX `idx_like_messages_fid`(`fid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '点赞消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for partitions
-- ----------------------------
DROP TABLE IF EXISTS `partitions`;
CREATE TABLE `partitions`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `content` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分区名称',
  `parent_id` int(0) NULL DEFAULT 0 COMMENT '所属分区ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_partitions_deleted_at`(`deleted_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '分区表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NOT NULL COMMENT '创建时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `uid` int(0) NOT NULL COMMENT '用户ID',
  `fid` int(0) NOT NULL COMMENT '父评论id',
  `vid` int(0) NOT NULL COMMENT '视频id',
  `is_delete` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除',
  `at` json NULL COMMENT '被@的用户ID（JSON格式字符串）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reply_messages
-- ----------------------------
DROP TABLE IF EXISTS `reply_messages`;
CREATE TABLE `reply_messages`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `vid` int(0) NOT NULL COMMENT '所在视频id',
  `uid` int(0) NOT NULL COMMENT '所属用户ID',
  `fid` int(0) NOT NULL COMMENT '关联用户ID',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `target_reply_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上级回复内容',
  `root_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '根评论内容',
  `comment_id` int(0) NULL DEFAULT NULL COMMENT '根评论ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_reply_messages_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_reply_messages_vid`(`vid`) USING BTREE,
  INDEX `idx_reply_messages_uid`(`uid`) USING BTREE,
  INDEX `idx_reply_messages_fid`(`fid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '回复消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for resources
-- ----------------------------
DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `vid` int(0) NULL DEFAULT NULL COMMENT '所属视频',
  `uid` int(0) NULL DEFAULT NULL COMMENT '所属用户',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分P使用的标题',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '视频链接',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'mp4',
  `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原始mp4链接',
  `duration` double NULL DEFAULT 0 COMMENT '视频时长',
  `status` int(0) NOT NULL COMMENT '审核状态',
  `quality` int(0) NULL DEFAULT NULL COMMENT '视频最大质量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_resources_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_resources_vid`(`vid`) USING BTREE,
  INDEX `idx_resources_uid`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT '默认昵称' COMMENT '用户昵称',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `space_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'http://cdns.schuanhe.com//image/1695711358871.jpg' COMMENT '空间封面',
  `gender` int(0) NULL DEFAULT 0 COMMENT '性别:0未知、1男、3女',
  `birthday` datetime(0) NULL DEFAULT '1970-01-01 00:00:00' COMMENT '生日',
  `sign` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '这个人很懒，什么都没有留下' COMMENT '个性签名',
  `client_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '客户端IP',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '账号状态:0正常、1停用、2删除',
  `role` int(0) NULL DEFAULT 0 COMMENT '角色身份:0用户、1审核、2管理、3超管',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_UNIQUE`(`username`) USING BTREE,
  UNIQUE INDEX `email_UNIQUE`(`email`) USING BTREE,
  INDEX `idx_user_deleted_at`(`deleted_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs COMMENT = '用户表' ROW_FORMAT = Dynamic;


-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '2023-08-02 20:00:25', '2023-09-17 12:41:05', NULL, 'admin', 'admin@qq.com', '超级管理员', '$2a$10$CT10ufGLV9bR/BSkk9KPTejJAz41CeSwly8q9jlsYqxjMm2XQ6w2W', NULL, NULL, 1, '2000-05-14 00:00:00', '这个人很懒，什么都没有留下', NULL, '0', 3);


-- ----------------------------
-- Table structure for videos
-- ----------------------------
DROP TABLE IF EXISTS `videos`;
CREATE TABLE `videos`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '封面图',
  `desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '什么都没有' COMMENT '视频简介',
  `uid` int(0) NOT NULL COMMENT '用户ID',
  `copyright` tinyint(1) NOT NULL COMMENT '是否为原创',
  `clicks` bigint(0) NULL DEFAULT 0 COMMENT '点击量',
  `status` int(0) NOT NULL COMMENT '审核状态',
  `partition_id` int(0) NULL DEFAULT 0 COMMENT '分区ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_videos_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_videos_title`(`title`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '视频表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for whispers
-- ----------------------------
DROP TABLE IF EXISTS `whispers`;
CREATE TABLE `whispers`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(0) NULL DEFAULT NULL,
  `updated_at` datetime(0) NULL DEFAULT NULL,
  `deleted_at` datetime(0) NULL DEFAULT NULL,
  `uid` int(0) NULL DEFAULT NULL COMMENT '用户ID',
  `fid` int(0) NULL DEFAULT NULL COMMENT '关联ID',
  `from_id` int(0) NOT NULL COMMENT '发送者',
  `to_id` int(0) NOT NULL COMMENT '接受者',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '已读状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_whispers_deleted_at`(`deleted_at`) USING BTREE,
  INDEX `idx_whispers_uid`(`uid`) USING BTREE,
  INDEX `idx_whispers_fid`(`fid`) USING BTREE,
  INDEX `idx_whispers_from_id`(`from_id`) USING BTREE,
  INDEX `idx_whispers_to_id`(`to_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '私信表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
