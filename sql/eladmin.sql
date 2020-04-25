/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50562
 Source Host           : localhost:3306
 Source Schema         : eladmin

 Target Server Type    : MySQL
 Target Server Version : 50562
 File Encoding         : 65001

 Date: 22/12/2019 15:24:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alipay_config
-- ----------------------------
DROP TABLE IF EXISTS `alipay_config`;
CREATE TABLE `alipay_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用ID',
  `charset` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
  `format` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型 固定格式json',
  `gateway_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网关地址',
  `notify_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异步回调',
  `private_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '私钥',
  `public_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公钥',
  `return_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `sign_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名方式',
  `sys_service_provider_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付宝配置类' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of alipay_config
-- ----------------------------
INSERT INTO `alipay_config` VALUES (1, '2016091700532697', 'utf-8', 'JSON', 'https://openapi.alipaydev.com/gateway.do', 'http://api.auauz.net/api/aliPay/notify', 'MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC5js8sInU10AJ0cAQ8UMMyXrQ+oHZEkVt5lBwsStmTJ7YikVYgbskx1YYEXTojRsWCb+SH/kDmDU4pK/u91SJ4KFCRMF2411piYuXU/jF96zKrADznYh/zAraqT6hvAIVtQAlMHN53nx16rLzZ/8jDEkaSwT7+HvHiS+7sxSojnu/3oV7BtgISoUNstmSe8WpWHOaWv19xyS+Mce9MY4BfseFhzTICUymUQdd/8hXA28/H6osUfAgsnxAKv7Wil3aJSgaJczWuflYOve0dJ3InZkhw5Cvr0atwpk8YKBQjy5CdkoHqvkOcIB+cYHXJKzOE5tqU7inSwVbHzOLQ3XbnAgMBAAECggEAVJp5eT0Ixg1eYSqFs9568WdetUNCSUchNxDBu6wxAbhUgfRUGZuJnnAll63OCTGGck+EGkFh48JjRcBpGoeoHLL88QXlZZbC/iLrea6gcDIhuvfzzOffe1RcZtDFEj9hlotg8dQj1tS0gy9pN9g4+EBH7zeu+fyv+qb2e/v1l6FkISXUjpkD7RLQr3ykjiiEw9BpeKb7j5s7Kdx1NNIzhkcQKNqlk8JrTGDNInbDM6inZfwwIO2R1DHinwdfKWkvOTODTYa2MoAvVMFT9Bec9FbLpoWp7ogv1JMV9svgrcF9XLzANZ/OQvkbe9TV9GWYvIbxN6qwQioKCWO4GPnCAQKBgQDgW5MgfhX8yjXqoaUy/d1VjI8dHeIyw8d+OBAYwaxRSlCfyQ+tieWcR2HdTzPca0T0GkWcKZm0ei5xRURgxt4DUDLXNh26HG0qObbtLJdu/AuBUuCqgOiLqJ2f1uIbrz6OZUHns+bT/jGW2Ws8+C13zTCZkZt9CaQsrp3QOGDx5wKBgQDTul39hp3ZPwGNFeZdkGoUoViOSd5Lhowd5wYMGAEXWRLlU8z+smT5v0POz9JnIbCRchIY2FAPKRdVTICzmPk2EPJFxYTcwaNbVqL6lN7J2IlXXMiit5QbiLauo55w7plwV6LQmKm9KV7JsZs5XwqF7CEovI7GevFzyD3w+uizAQKBgC3LY1eRhOlpWOIAhpjG6qOoohmeXOphvdmMlfSHq6WYFqbWwmV4rS5d/6LNpNdL6fItXqIGd8I34jzql49taCmi+A2nlR/E559j0mvM20gjGDIYeZUz5MOE8k+K6/IcrhcgofgqZ2ZED1ksHdB/E8DNWCswZl16V1FrfvjeWSNnAoGAMrBplCrIW5xz+J0Hm9rZKrs+AkK5D4fUv8vxbK/KgxZ2KaUYbNm0xv39c+PZUYuFRCz1HDGdaSPDTE6WeWjkMQd5mS6ikl9hhpqFRkyh0d0fdGToO9yLftQKOGE/q3XUEktI1XvXF0xyPwNgUCnq0QkpHyGVZPtGFxwXiDvpvgECgYA5PoB+nY8iDiRaJNko9w0hL4AeKogwf+4TbCw+KWVEn6jhuJa4LFTdSqp89PktQaoVpwv92el/AhYjWOl/jVCm122f9b7GyoelbjMNolToDwe5pF5RnSpEuDdLy9MfE8LnE3PlbE7E5BipQ3UjSebkgNboLHH/lNZA5qvEtvbfvQ==', 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAut9evKRuHJ/2QNfDlLwvN/S8l9hRAgPbb0u61bm4AtzaTGsLeMtScetxTWJnVvAVpMS9luhEJjt+Sbk5TNLArsgzzwARgaTKOLMT1TvWAK5EbHyI+eSrc3s7Awe1VYGwcubRFWDm16eQLv0k7iqiw+4mweHSz/wWyvBJVgwLoQ02btVtAQErCfSJCOmt0Q/oJQjj08YNRV4EKzB19+f5A+HQVAKy72dSybTzAK+3FPtTtNen/+b5wGeat7c32dhYHnGorPkPeXLtsqqUTp1su5fMfd4lElNdZaoCI7osZxWWUo17vBCZnyeXc9fk0qwD9mK6yRAxNbrY72Xx5VqIqwIDAQAB', 'http://api.auauz.net/api/aliPay/return', 'RSA2', '2088102176044281');

-- ----------------------------
-- Table structure for column_config
-- ----------------------------
DROP TABLE IF EXISTS `column_config`;
CREATE TABLE `column_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `column_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `column_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dict_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `extra` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `form_show` bit(1) NULL DEFAULT NULL,
  `form_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `key_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `list_show` bit(1) NULL DEFAULT NULL,
  `not_null` bit(1) NULL DEFAULT NULL,
  `query_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `date_annotation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 139 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成字段信息存储' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of column_config
-- ----------------------------
INSERT INTO `column_config` VALUES (1, 'gen_test', 'id', 'int', NULL, 'auto_increment', b'0', NULL, 'PRI', b'0', b'1', NULL, 'ID', NULL);
INSERT INTO `column_config` VALUES (2, 'gen_test', 'sex', 'int', NULL, '', b'1', NULL, '', b'1', b'0', 'NotNull', '性别', NULL);
INSERT INTO `column_config` VALUES (3, 'gen_test', 'create_time', 'datetime', NULL, '', b'0', NULL, '', b'1', b'0', 'BetWeen', '', NULL);


-- ----------------------------
-- 添加行政区域表 region
-- ----------------------------
drop table if exists `region`;
create table `region`
(
  `id`              varchar (255) not null primary key comment '行政区域ID',
  `pid`             varchar(255) null default null comment '父级ID',
  `level`           bigint(20) null default null comment '等级',
  `name`            varchar(255) null default null comment '简称',
  `pinyin`          varchar(255) null default null comment '拼音',
  `ext_id`          varchar (255) null default null comment '拓展ID',
  `province_id`     varchar (255) null default null comment '省ID',
  `province_name`   varchar (255) null default null comment '省名',
  `city_id`         varchar (255) null default null comment '市ID',
  `city_name`       varchar (255) null default null comment '市名',
  `area_id`         varchar (255) null default null comment '县ID',
  `area_name`       varchar (255) null default null comment '县名',
  `town_id`         varchar (255) null default null comment '乡镇ID',
  `town_name`       varchar (255) null default null comment '乡镇名',
  `ext_name`        varchar (255) null default null comment '拓展名'
) engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- Records of region
insert into `region` values ('431125','431100',2,'江永','jiang yong','431125000000','430000','湖南省','431100','永州市','431125','江永县',null ,null ,'江永县');
insert into `region` values ('431124','431100',2,'道县','dao xian','431124000000','430000','湖南省','431100','永州市','431124','道县',null ,null ,'道县');


-- ----------------------------
-- Table structure for dept 部门表
-- 对应于单位表
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `pid` bigint(20) NOT NULL COMMENT '上级部门',
  `enabled` bit(1) NOT NULL COMMENT '状态',
-- 添加行政区域ID
  `region_id` varchar (255) null default null comment '所属行政区域ID',
  `type` bigint(10) null default null comment '机构类型',
  `address` varchar (255) null default null comment '地址',
  `code` varchar (255) null default null comment '编码',
  `full_name` varchar (255) null default null comment '全称',
  `short_name` varchar (255) null default null comment '简称',
  `remark` varchar (255) null default null comment '备注',
  `create_uid` bigint(20) null default null comment '创建者ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime null default null comment '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK5tpkpudf6d9nboiijdbgpnmddc`(`region_id`) USING BTREE,
  CONSTRAINT `FK5tpkpudf6d9nboiijdbgpnmddc` FOREIGN KEY (`region_id`) REFERENCES `region` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dept
-- ----------------------------
-- 需修改insert
INSERT INTO `dept` VALUES (1, '江永县农业局', 0, b'1', '431125',0,'湖南省永州市江永县',null ,'湖南省永州市江永县农业局',null,null ,1,'2020-03-01 12:07:37',null );
INSERT INTO `dept` VALUES (2, '采购部', 1, b'1', '431125',0,'湖南省永州市江永县',null ,'湖南省永州市江永县农业局',null,null ,1,'2020-03-01 12:07:37',null );
INSERT INTO `dept` VALUES (3, '生产部', 1, b'1', '431125',0,'湖南省永州市江永县',null ,'湖南省永州市江永县农业局',null,null ,1,'2020-03-01 12:07:37',null );

-- INSERT INTO `dept` VALUES (4, '道县农业局', 0, b'1', '2020-03-01 12:07:37');
-- -- INSERT INTO `dept` VALUES (5, '采购部', 4, b'1', '2020-03-25 11:04:50');
-- -- INSERT INTO `dept` VALUES (6, '生产部', 4, b'1', '2020-03-25 11:04:53');

INSERT INTO `dept` VALUES (63, '宁远县', 0, b'1', '431126',0,'湖南省永州市宁远县',null ,'湖南省永州市宁远县',null,null ,1,'2020-03-01 12:07:37',null );




-- ----------------------------
-- Table structure for dict
-- 字典
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dict
-- ----------------------------
INSERT INTO `dict` VALUES (1, 'user_status', '用户状态', '2019-10-27 20:31:36');
INSERT INTO `dict` VALUES (4, 'dept_status', '部门状态', '2019-10-27 20:31:36');
INSERT INTO `dict` VALUES (5, 'job_status', '岗位状态', '2019-10-27 20:31:36');

-- ----------------------------
-- Table structure for dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `dict_detail`;
CREATE TABLE `dict_detail`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典标签',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典值',
  `sort` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排序',
  `dict_id` bigint(11) NULL DEFAULT NULL COMMENT '字典id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK5tpkputc6d9nboxojdbgnpmyb`(`dict_id`) USING BTREE,
  CONSTRAINT `FK5tpkputc6d9nboxojdbgnpmyb` FOREIGN KEY (`dict_id`) REFERENCES `dict` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典详情' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dict_detail
-- ----------------------------
INSERT INTO `dict_detail` VALUES (1, '激活', 'true', '1', 1, '2019-10-27 20:31:36');
INSERT INTO `dict_detail` VALUES (2, '禁用', 'false', '2', 1, NULL);
INSERT INTO `dict_detail` VALUES (3, '启用', 'true', '1', 4, NULL);
INSERT INTO `dict_detail` VALUES (4, '停用', 'false', '2', 4, '2019-10-27 20:31:36');
INSERT INTO `dict_detail` VALUES (5, '启用', 'true', '1', 5, NULL);
INSERT INTO `dict_detail` VALUES (6, '停用', 'false', '2', 5, '2019-10-27 20:31:36');

-- ----------------------------
-- Table structure for email_config
-- ----------------------------
DROP TABLE IF EXISTS `email_config`;
CREATE TABLE `email_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `from_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人',
  `host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件服务器SMTP地址',
  `pass` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `port` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '端口',
  `user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发件者用户名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邮箱配置' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for gen_config
-- ----------------------------
DROP TABLE IF EXISTS `gen_config`;
CREATE TABLE `gen_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `table_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者',
  `cover` bit(1) NULL DEFAULT NULL COMMENT '是否覆盖',
  `module_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `pack` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '至于哪个包下',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端代码生成的路径',
  `api_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端Api文件路径',
  `prefix` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表前缀',
  `api_alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成器配置' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of gen_config
-- ----------------------------
INSERT INTO `gen_config` VALUES (3, 'gen_test', 'Zheng Jie', b'1', 'eladmin-system', 'me.zhengjie.gen', 'E:\\workspace\\me\\front\\eladmin-web\\src\\views\\gen', 'E:\\workspace\\me\\front\\eladmin-web\\src\\api', NULL, '测试生成');
-- ----------------------------
-- Table structure for gen_test
-- ----------------------------
DROP TABLE IF EXISTS `gen_test`;
CREATE TABLE `gen_test`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` int(255) NULL DEFAULT NULL COMMENT '性别',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成测试' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
  `enabled` bit(1) NOT NULL COMMENT '岗位状态',
  `sort` bigint(20) NOT NULL COMMENT '岗位排序',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKmvhj0rogastlctflsxf1d6k3i`(`dept_id`) USING BTREE,
  CONSTRAINT `FKmvhj0rogastlctflsxf1d6k3i` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of job
-- ----------------------------
INSERT INTO `job` VALUES (8, '采购员', b'1', 3, 2, '2019-03-29 14:52:28');
INSERT INTO `job` VALUES (10, '数据分析员', b'1', 4, 3, '2019-03-29 14:55:51');
-- INSERT INTO `job` VALUES (11, '测试员', b'1', 2, 5, '2019-03-31 13:39:30');
-- INSERT INTO `job` VALUES (12, '操作员', b'1', 5, 6, '2019-03-31 13:39:43');

-- ----------------------------
-- Table structure for local_storage
-- ----------------------------
DROP TABLE IF EXISTS `local_storage`;
CREATE TABLE `local_storage`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件真实的名称',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `suffix` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后缀',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `size` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大小',
  `operate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '本地存储' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exception_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `log_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `request_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` bigint(20) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `browser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 411426 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `i_frame` bit(1) NULL DEFAULT NULL COMMENT '是否外链',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `pid` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `sort` bigint(20) NULL DEFAULT NULL COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `cache` bit(1) NULL DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) NULL DEFAULT b'0' COMMENT '隐藏',
  `component_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '-' COMMENT '组件名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKqcf9gem97gqa5qjm4d3elcqt5`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 117 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, b'0', '基础信息维护', NULL, 0, 1, 'maintenance', 'product', b'0', b'0', NULL, '2018-12-18 15:11:29', NULL, 0);
INSERT INTO `menu` VALUES (2, b'0', '农产品分类管理', 'product/category/index', 1, 2, 'list', 'category', b'0', b'0', NULL, '2018-12-18 15:14:44', 'category:list', 1);
INSERT INTO `menu` VALUES (3, b'0', '基本信息描述管理', 'product/description/index', 1, 3, 'description', 'description', b'0', b'0', NULL, '2018-12-18 15:14:44', 'description:list', 1);


INSERT INTO `menu` VALUES (6, b'0', '农产品信息采集', NULL, 0, 2, 'chart', 'collection', b'0', b'0', NULL, '2018-12-18 15:11:29', NULL, 0);
INSERT INTO `menu` VALUES (7, b'0', '农产品信息管理', 'collection/products/index', 6, 7, 'skill', 'products', b'0', b'0', NULL, '2018-12-18 15:11:29', 'products:list', 1);
INSERT INTO `menu` VALUES (8, b'0', '产品生产信息管理', 'collection/info/index', 6, 8, 'redis', 'info', b'0', b'0', NULL, '2018-12-18 15:11:29', 'products-info:list', 1);
INSERT INTO `menu` VALUES (9, b'0', '产品检测数据管理', 'collection/detect/index', 6, 9, 'qiniu', 'detect', b'0', b'0', NULL, '2018-12-18 15:11:29', 'products-detect:list', 1);
INSERT INTO `menu` VALUES (10, b'0', '产品销售数据管理', 'collection/sale/index', 6, 10, 'redis', 'sale', b'0', b'0', NULL, '2018-12-18 15:11:29', 'products-sale:list', 1);
INSERT INTO `menu` VALUES (11, b'0', '农产品数据抓取','collection/grab/index', 6, 11, 'develop', 'grab', b'0', b'0', NULL, '2018-12-18 15:11:29','products-grab:list', 1);


INSERT INTO `menu` VALUES (15, b'0', '信息显示与分析', NULL, 0, 3, 'codeConsole', 'analysis', b'0', b'0', NULL, '2018-12-18 15:11:29', NULL, 0);
INSERT INTO `menu` VALUES (16, b'0', '农产品信息查询', 'analysis/search/index', 15, 16, 'search', 'search', b'0', b'0', NULL, '2018-12-18 15:11:29', 'search:list', 1);
INSERT INTO `menu` VALUES (17, b'0', '价格行情分析', 'analysis/price/index', 15, 17, 'sqlMonitor', 'price', b'0', b'0', NULL, '2018-12-18 15:11:29', 'price:list', 1);



INSERT INTO `menu` VALUES (20, b'0', '系统管理', NULL, 0, 4, 'system', 'system', b'0', b'0', NULL, '2018-12-18 15:11:29', NULL, 0);
INSERT INTO `menu` VALUES (21, b'0', '用户管理', 'system/user/index', 20, 21, 'peoples', 'user', b'0', b'0', 'User', '2018-12-18 15:14:44', 'user:list', 1);
INSERT INTO `menu` VALUES (23, b'0', '角色管理', 'system/role/index', 20, 23, 'role', 'role', b'0', b'0', 'Role', '2018-12-18 15:16:07', 'roles:list', 1);
INSERT INTO `menu` VALUES (25, b'0', '菜单管理', 'system/menu/index', 20, 25, 'menu', 'menu', b'0', b'0', 'Menu', '2018-12-18 15:17:28', 'menu:list', 1);
INSERT INTO `menu` VALUES (35, b'0', '单位管理', 'system/dept/index', 20, 35, 'dept', 'dept', b'0', b'0', 'Dept', '2019-03-25 09:46:00', 'dept:list', 1);
INSERT INTO `menu` VALUES (37, b'0', '岗位管理', 'system/job/index', 20, 37, 'Steve-Jobs', 'job', b'0', b'0', 'Job', '2019-03-29 13:51:18', 'job:list', 1);

-- INSERT INTO `menu` VALUES (39, b'0', '组织机构管理', 'system/organization/index', 20, 39, 'organization', 'organization', b'0', b'0', Null , '2019-03-25 09:46:00', 'organization:list', 1);


-- 用户管理权限
INSERT INTO `menu` VALUES (44, b'0', '用户新增', '', 21, 2, '', '', b'0', b'0', '', '2019-10-29 10:59:46', 'user:add', 2);
INSERT INTO `menu` VALUES (45, b'0', '用户编辑', '', 21, 3, '', '', b'0', b'0', '', '2019-10-29 11:00:08', 'user:edit', 2);
INSERT INTO `menu` VALUES (46, b'0', '用户删除', '', 21, 4, '', '', b'0', b'0', '', '2019-10-29 11:00:23', 'user:del', 2);

-- 角色管理权限
INSERT INTO `menu` VALUES (48, b'0', '角色创建', '', 23, 2, '', '', b'0', b'0', '', '2019-10-29 12:45:34', 'roles:add', 2);
INSERT INTO `menu` VALUES (49, b'0', '角色修改', '', 23, 3, '', '', b'0', b'0', '', '2019-10-29 12:46:16', 'roles:edit', 2);
INSERT INTO `menu` VALUES (50, b'0', '角色删除', '', 23, 4, '', '', b'0', b'0', '', '2019-10-29 12:46:51', 'roles:del', 2);

-- 菜单管理
INSERT INTO `menu` VALUES (52, b'0', '菜单新增', '', 25, 2, '', '', b'0', b'0', '', '2019-10-29 12:55:07', 'menu:add', 2);
INSERT INTO `menu` VALUES (53, b'0', '菜单编辑', '', 25, 3, '', '', b'0', b'0', '', '2019-10-29 12:55:40', 'menu:edit', 2);
INSERT INTO `menu` VALUES (54, b'0', '菜单删除', '', 25, 4, '', '', b'0', b'0', '', '2019-10-29 12:56:00', 'menu:del', 2);

-- 单位管理
INSERT INTO `menu` VALUES (56, b'0', '部门新增', '', 35, 2, '', '', b'0', b'0', '', '2019-10-29 12:57:09', 'dept:add', 2);
INSERT INTO `menu` VALUES (57, b'0', '部门编辑', '', 35, 3, '', '', b'0', b'0', '', '2019-10-29 12:57:27', 'dept:edit', 2);
INSERT INTO `menu` VALUES (58, b'0', '部门删除', '', 35, 4, '', '', b'0', b'0', '', '2019-10-29 12:57:41', 'dept:del', 2);

-- 岗位管理
INSERT INTO `menu` VALUES (60, b'0', '岗位新增', '', 37, 2, '', '', b'0', b'0', '', '2019-10-29 12:58:27', 'job:add', 2);
INSERT INTO `menu` VALUES (61, b'0', '岗位编辑', '', 37, 3, '', '', b'0', b'0', '', '2019-10-29 12:58:45', 'job:edit', 2);
INSERT INTO `menu` VALUES (62, b'0', '岗位删除', '', 37, 4, '', '', b'0', b'0', '', '2019-10-29 12:59:04', 'job:del', 2);

-- 系统监控
INSERT INTO `menu` VALUES (30, b'0', '系统监控', NULL, 0, 10, 'monitor', 'monitor', b'0', b'0', NULL, '2018-12-18 15:17:48', NULL, 0);
INSERT INTO `menu` VALUES (31, b'0', '操作日志', 'monitor/log/index', 30, 31, 'log', 'logs', b'0', b'0', 'Log', '2018-12-18 15:18:26', NULL, 1);
INSERT INTO `menu` VALUES (32, b'0', '异常日志', 'monitor/log/errorLog', 30, 32, 'error', 'errorLog', b'0', b'0', 'ErrorLog', '2019-01-13 13:49:03', NULL, 1);
INSERT INTO `menu` VALUES (80, b'0', '服务监控', 'monitor/server/index', 30, 80, 'codeConsole', 'server', b'0', b'0', 'ServerMonitor', '2019-11-07 13:06:39', 'server:list', 1);

-- 系统工具
INSERT INTO `menu` VALUES (36, b'0', '系统工具', '', 0, 30, 'sys-tools', 'sys-tools', b'0', b'0', NULL, '2019-03-29 10:57:35', NULL, 0);
INSERT INTO `menu` VALUES (14, b'0', '邮件工具', 'tools/email/index', 36, 35, 'email', 'email', b'0', b'0', 'Email', '2018-12-27 10:13:09', NULL, 1);


-- 运维服务
INSERT INTO `menu` VALUES (90, b'0', '运维管理', '', 0, 20, 'mnt', 'mnt', b'0', b'0', 'Mnt', '2019-11-09 10:31:08', NULL, 1);
INSERT INTO `menu` VALUES (92, b'0', '服务器', 'mnt/server/index', 90, 22, 'server', 'mnt/serverDeploy', b'0', b'0', 'ServerDeploy', '2019-11-10 10:29:25', 'serverDeploy:list', 1);
INSERT INTO `menu` VALUES (93, b'0', '应用管理', 'mnt/app/index', 90, 23, 'app', 'mnt/app', b'0', b'0', 'App', '2019-11-10 11:05:16', 'app:list', 1);
INSERT INTO `menu` VALUES (94, b'0', '部署管理', 'mnt/deploy/index', 90, 24, 'deploy', 'mnt/deploy', b'0', b'0', 'Deploy', '2019-11-10 15:56:55', 'deploy:list', 1);
INSERT INTO `menu` VALUES (97, b'0', '部署备份', 'mnt/deployHistory/index', 90, 25, 'backup', 'mnt/deployHistory', b'0', b'0', 'DeployHistory', '2019-11-10 16:49:44', 'deployHistory:list', 1);
INSERT INTO `menu` VALUES (98, b'0', '数据库管理', 'mnt/database/index', 90, 26, 'database', 'mnt/database', b'0', b'0', 'Database', '2019-11-10 20:40:04', 'database:list', 1);

INSERT INTO `menu` VALUES (102, b'0', '删除', '', 97, 999, '', '', b'0', b'0', '', '2019-11-17 09:32:48', 'deployHistory:del', 2);
INSERT INTO `menu` VALUES (103, b'0', '服务器新增', '', 92, 999, '', '', b'0', b'0', '', '2019-11-17 11:08:33', 'serverDeploy:add', 2);
INSERT INTO `menu` VALUES (104, b'0', '服务器编辑', '', 92, 999, '', '', b'0', b'0', '', '2019-11-17 11:08:57', 'serverDeploy:edit', 2);
INSERT INTO `menu` VALUES (105, b'0', '服务器删除', '', 92, 999, '', '', b'0', b'0', '', '2019-11-17 11:09:15', 'serverDeploy:del', 2);
INSERT INTO `menu` VALUES (106, b'0', '应用新增', '', 93, 999, '', '', b'0', b'0', '', '2019-11-17 11:10:03', 'app:add', 2);
INSERT INTO `menu` VALUES (107, b'0', '应用编辑', '', 93, 999, '', '', b'0', b'0', '', '2019-11-17 11:10:28', 'app:edit', 2);
INSERT INTO `menu` VALUES (108, b'0', '应用删除', '', 93, 999, '', '', b'0', b'0', '', '2019-11-17 11:10:55', 'app:del', 2);
INSERT INTO `menu` VALUES (109, b'0', '部署新增', '', 94, 999, '', '', b'0', b'0', '', '2019-11-17 11:11:22', 'deploy:add', 2);
INSERT INTO `menu` VALUES (110, b'0', '部署编辑', '', 94, 999, '', '', b'0', b'0', '', '2019-11-17 11:11:41', 'deploy:edit', 2);
INSERT INTO `menu` VALUES (111, b'0', '部署删除', '', 94, 999, '', '', b'0', b'0', '', '2019-11-17 11:12:01', 'deploy:del', 2);
INSERT INTO `menu` VALUES (112, b'0', '数据库新增', '', 98, 999, '', '', b'0', b'0', '', '2019-11-17 11:12:43', 'database:add', 2);
INSERT INTO `menu` VALUES (113, b'0', '数据库编辑', '', 98, 999, '', '', b'0', b'0', '', '2019-11-17 11:12:58', 'database:edit', 2);
INSERT INTO `menu` VALUES (114, b'0', '数据库删除', '', 98, 999, '', '', b'0', b'0', '', '2019-11-17 11:13:14', 'database:del', 2);

-- ----------------------------
-- Table structure for mnt_app
-- ----------------------------
DROP TABLE IF EXISTS `mnt_app`;
CREATE TABLE `mnt_app`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '应用ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `upload_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传目录',
  `deploy_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部署路径',
  `backup_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备份路径',
  `port` int(255) NULL DEFAULT NULL COMMENT '应用端口',
  `start_script` varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '启动脚本',
  `deploy_script` varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部署脚本',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用管理' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of mnt_app
-- ----------------------------
INSERT INTO `mnt_app` VALUES (1, 'eladmin-monitor-2.4.jar', '/opt/upload', '/opt/monitor', '/opt/backup', 8777, 'cd /opt/monitor\nnohup java -jar eladmin-monitor-2.4.jar >nohup.out 2>&1 &', 'mv -f /opt/upload/eladmin-monitor-2.4.jar /opt/monitor\ncd /opt/monitor\nnohup java -jar eladmin-monitor-2.4.jar >nohup.out 2>&1 &', '2019-11-24 20:52:59');
INSERT INTO `mnt_app` VALUES (2, 'eladmin-system-2.3.jar', '/opt/upload', '/opt/eladmin', '/opt/backup/eladmin', 8000, 'cd /opt/eladmin\nnohup java -jar eladmin-system-2.3.jar --spring.profiles.active=prod >nohup.out 2>&1 &', 'mv -f /opt/upload/eladmin-system-2.3.jar /opt/eladmin/\ncd /opt/eladmin\nnohup java -jar eladmin-system-2.3.jar --spring.profiles.active=prod >nohup.out 2>&1 &', '2019-12-21 16:39:57');

-- ----------------------------
-- Table structure for mnt_database
-- ----------------------------
DROP TABLE IF EXISTS `mnt_database`;
CREATE TABLE `mnt_database`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `jdbc_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'jdbc连接',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据库管理' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of mnt_database
-- ----------------------------
INSERT INTO `mnt_database` VALUES ('c4109eefc5724c65857ca9dd2690e0dd', 'eladmin', 'jdbc:mysql://localhost:3306/eladmin?serverTimezone=Asia/Shanghai', 'root', '123456', '2019-12-21 21:11:17');

-- ----------------------------
-- Table structure for mnt_deploy
-- ----------------------------
DROP TABLE IF EXISTS `mnt_deploy`;
CREATE TABLE `mnt_deploy`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `app_id` bigint(20) NULL DEFAULT NULL COMMENT '应用编号',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK6sy157pseoxx4fmcqr1vnvvhy`(`app_id`) USING BTREE,
  CONSTRAINT `FK6sy157pseoxx4fmcqr1vnvvhy` FOREIGN KEY (`app_id`) REFERENCES `mnt_app` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部署管理' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of mnt_deploy
-- ----------------------------
INSERT INTO `mnt_deploy` VALUES (3, 1, '2019-12-21 15:53:06');
INSERT INTO `mnt_deploy` VALUES (6, 2, '2019-12-21 17:09:02');

-- ----------------------------
-- Table structure for mnt_deploy_history
-- ----------------------------
DROP TABLE IF EXISTS `mnt_deploy_history`;
CREATE TABLE `mnt_deploy_history`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `app_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用名称',
  `deploy_date` datetime NOT NULL COMMENT '部署日期',
  `deploy_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部署用户',
  `ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务器IP',
  `deploy_id` bigint(20) NULL DEFAULT NULL COMMENT '部署编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部署历史管理' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of mnt_deploy_history
-- ----------------------------
INSERT INTO `mnt_deploy_history` VALUES ('4ee0edd1f3b648a396280a542d72c121', 'eladmin-monitor-2.4.jar', '2019-12-22 13:06:07', 'admin', '132.232.129.20', 3);
INSERT INTO `mnt_deploy_history` VALUES ('4fd432a128c947ccae55316b3d5dcd7b', 'eladmin-monitor-2.4.jar', '2019-12-22 13:49:09', 'admin', '132.232.129.20', 3);
INSERT INTO `mnt_deploy_history` VALUES ('cfda21f48da341b396657af94554c890', 'eladmin-monitor-2.4.jar', '2019-12-22 13:08:22', 'admin', '132.232.129.20', 3);

-- ----------------------------
-- Table structure for mnt_deploy_server
-- ----------------------------
DROP TABLE IF EXISTS `mnt_deploy_server`;
CREATE TABLE `mnt_deploy_server`  (
  `deploy_id` bigint(20) NOT NULL,
  `server_id` bigint(20) NOT NULL,
  PRIMARY KEY (`deploy_id`, `server_id`) USING BTREE,
  INDEX `FKeaaha7jew9a02b3bk9ghols53`(`server_id`) USING BTREE,
  CONSTRAINT `FK3cehr56tedph6nk3gxsmeq0pb` FOREIGN KEY (`deploy_id`) REFERENCES `mnt_deploy` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKeaaha7jew9a02b3bk9ghols53` FOREIGN KEY (`server_id`) REFERENCES `mnt_server` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用与服务器关联' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of mnt_deploy_server
-- ----------------------------
INSERT INTO `mnt_deploy_server` VALUES (3, 1);
INSERT INTO `mnt_deploy_server` VALUES (6, 1);

-- ----------------------------
-- Table structure for mnt_server
-- ----------------------------
DROP TABLE IF EXISTS `mnt_server`;
CREATE TABLE `mnt_server`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'IP地址',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `port` int(11) NULL DEFAULT NULL COMMENT '端口',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务器管理' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of mnt_server
-- ----------------------------
INSERT INTO `mnt_server` VALUES (1, 'root', '132.232.129.20', '腾讯云', 'Dqjdda1996.', 8013, '2019-11-24 20:35:02');

-- ----------------------------
-- Table structure for monitor_server
-- ----------------------------
DROP TABLE IF EXISTS `monitor_server`;
CREATE TABLE `monitor_server`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cpu_core` int(11) NULL DEFAULT NULL COMMENT 'CPU内核数',
  `cpu_rate` double NULL DEFAULT NULL COMMENT 'CPU使用率',
  `disk_total` double NULL DEFAULT NULL COMMENT '磁盘总量',
  `disk_used` double NULL DEFAULT NULL COMMENT '磁盘使用量',
  `mem_total` double NULL DEFAULT NULL COMMENT '内存总数',
  `mem_used` double NULL DEFAULT NULL COMMENT '内存使用量',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `port` int(11) NULL DEFAULT NULL COMMENT '访问端口',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `swap_total` double NULL DEFAULT NULL COMMENT '交换区总量',
  `swap_used` double NULL DEFAULT NULL COMMENT '交换区使用量',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务监控' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of monitor_server
-- ----------------------------
INSERT INTO `monitor_server` VALUES (1, 8, 0.05924018, 465.12402, 91.66521, 7.849415, 7.6052284, '本地', 8777, 999, '0', 14.599415, 11.263367, 'localhost');

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '上传日期',
  `delete_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除的URL',
  `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名称',
  `height` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片高度',
  `size` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片大小',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `width` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片宽度',
  `md5code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件的MD5值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Sm.Ms图床' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for qiniu_config
-- ----------------------------
DROP TABLE IF EXISTS `qiniu_config`;
CREATE TABLE `qiniu_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `access_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'accessKey',
  `bucket` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Bucket 识别符',
  `host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '外链域名',
  `secret_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'secretKey',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '空间类型',
  `zone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机房',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '七牛云配置' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for qiniu_content
-- ----------------------------
DROP TABLE IF EXISTS `qiniu_content`;
CREATE TABLE `qiniu_content`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bucket` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Bucket 识别符',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `size` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型：私有或公开',
  `update_time` datetime NULL DEFAULT NULL COMMENT '上传或同步的时间',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件url',
  `suffix` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '七牛云文件存储' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `quartz_job`;
CREATE TABLE `quartz_job`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Spring Bean名称',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron 表达式',
  `is_pause` bit(1) NULL DEFAULT NULL COMMENT '状态：1暂停、0启用',
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名称',
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of quartz_job
-- ----------------------------
INSERT INTO `quartz_job` VALUES (1, 'visitsTask', '0 0 0 * * ?', b'0', '更新访客记录', 'run', NULL, '每日0点创建新的访客记录', '2019-01-08 14:53:31');
INSERT INTO `quartz_job` VALUES (2, 'testTask', '0/5 * * * * ?', b'1', '测试1', 'run1', 'test', '带参测试，多参使用json', '2019-08-22 14:08:29');
INSERT INTO `quartz_job` VALUES (3, 'testTask', '0/5 * * * * ?', b'1', '测试', 'run', '', '不带参测试', '2019-09-26 16:44:39');

-- ----------------------------
-- Table structure for quartz_log
-- ----------------------------
DROP TABLE IF EXISTS `quartz_log`;
CREATE TABLE `quartz_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `baen_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exception_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `is_success` bit(1) NULL DEFAULT NULL,
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 288 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务日志' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `data_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据权限',
  `level` int(255) NULL DEFAULT NULL COMMENT '角色级别',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能权限',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '系统管理员', '-', '全部', 1, '2018-11-23 11:04:37', 'admin');
INSERT INTO `role` VALUES (2, '行政管理员', '-', '本级', 2, '2018-11-23 11:04:37', 'district-admin');
INSERT INTO `role` VALUES (3, '行政区用户', '-', '本级', 4, '2018-11-23 11:04:37', 'district-common');
INSERT INTO `role` VALUES (4, '组织机构管理员', '-', '本级', 3, '2018-11-23 11:04:37', 'dept-admin');
INSERT INTO `role` VALUES (5, '组织机构用户', '-', '本级', 5, '2018-11-23 13:09:06', 'dept-common');

-- ----------------------------
-- Table structure for roles_depts
-- ----------------------------
DROP TABLE IF EXISTS `roles_depts`;
CREATE TABLE `roles_depts`  (
  `role_id` bigint(20) NOT NULL,
  `dept_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE,
  INDEX `FK7qg6itn5ajdoa9h9o78v9ksur`(`dept_id`) USING BTREE,
  CONSTRAINT `FK7qg6itn5ajdoa9h9o78v9ksur` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKrg1ci4cxxfbja0sb0pddju7k` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色部门关联' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for roles_menus
-- ----------------------------
DROP TABLE IF EXISTS `roles_menus`;
CREATE TABLE `roles_menus`  (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
  INDEX `FKcngg2qadojhi3a651a5adkvbq`(`role_id`) USING BTREE,
  CONSTRAINT `FKo7wsmlrrxb2osfaoavp46rv2r` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKtag324maketmxffly3pdyh193` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单关联' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of roles_menus
-- ----------------------------
INSERT INTO `roles_menus` VALUES (1, 1);
INSERT INTO `roles_menus` VALUES (2, 1);
INSERT INTO `roles_menus` VALUES (3, 1);
INSERT INTO `roles_menus` VALUES (6, 1);
INSERT INTO `roles_menus` VALUES (7, 1);
INSERT INTO `roles_menus` VALUES (8, 1);
INSERT INTO `roles_menus` VALUES (9, 1);
INSERT INTO `roles_menus` VALUES (10, 1);
INSERT INTO `roles_menus` VALUES (11, 1);
INSERT INTO `roles_menus` VALUES (14, 1);
INSERT INTO `roles_menus` VALUES (15, 1);
INSERT INTO `roles_menus` VALUES (16, 1);
INSERT INTO `roles_menus` VALUES (17, 1);
INSERT INTO `roles_menus` VALUES (20, 1);
INSERT INTO `roles_menus` VALUES (21, 1);

INSERT INTO `roles_menus` VALUES (23, 1);
INSERT INTO `roles_menus` VALUES (25, 1);
INSERT INTO `roles_menus` VALUES (30, 1);
INSERT INTO `roles_menus` VALUES (31, 1);
INSERT INTO `roles_menus` VALUES (32, 1);
INSERT INTO `roles_menus` VALUES (35, 1);
INSERT INTO `roles_menus` VALUES (36, 1);
INSERT INTO `roles_menus` VALUES (37, 1);



-- INSERT INTO `roles_menus` VALUES (37, 1);
INSERT INTO `roles_menus` VALUES (44, 1);
INSERT INTO `roles_menus` VALUES (45, 1);
INSERT INTO `roles_menus` VALUES (46, 1);
INSERT INTO `roles_menus` VALUES (48, 1);
INSERT INTO `roles_menus` VALUES (49, 1);
INSERT INTO `roles_menus` VALUES (50, 1);
INSERT INTO `roles_menus` VALUES (52, 1);
INSERT INTO `roles_menus` VALUES (53, 1);
INSERT INTO `roles_menus` VALUES (54, 1);
INSERT INTO `roles_menus` VALUES (56, 1);
INSERT INTO `roles_menus` VALUES (57, 1);
INSERT INTO `roles_menus` VALUES (58, 1);
INSERT INTO `roles_menus` VALUES (60, 1);
INSERT INTO `roles_menus` VALUES (61, 1);
INSERT INTO `roles_menus` VALUES (62, 1);
INSERT INTO `roles_menus` VALUES (80, 1);
INSERT INTO `roles_menus` VALUES (90, 1);
INSERT INTO `roles_menus` VALUES (92, 1);
INSERT INTO `roles_menus` VALUES (93, 1);
INSERT INTO `roles_menus` VALUES (94, 1);
INSERT INTO `roles_menus` VALUES (97, 1);
INSERT INTO `roles_menus` VALUES (98, 1);


-- ----------------------------
-- Table structure for user
-- 添加了region_id 区域ID
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `avatar_id` bigint(20) NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `enabled` bigint(20) NULL DEFAULT NULL COMMENT '状态：1启用、0禁用',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',

-- 添加姓名
   `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL comment '姓名',

  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门名称',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `job_id` bigint(20) NULL DEFAULT NULL COMMENT '岗位名称',

  -- 添加
  `region_id` varchar(255) NULL DEFAULT NULL COMMENT '行政区域名称',

  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `last_password_reset_time` datetime NULL DEFAULT NULL COMMENT '最后修改密码的日期',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_kpubos9gc2cvtkb0thktkbkes`(`email`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `FK5rwmryny6jthaaxkogownknqp`(`dept_id`) USING BTREE,
  INDEX `FKfftoc2abhot8f2wu6cl9a5iky`(`job_id`) USING BTREE,
  INDEX `FKpq2dhypk2qgt68nauh2by22jb`(`avatar_id`) USING BTREE,
  -- 添加region_id
  INDEX `FKpq2dhvpkjqbtionvuh2vy00rd`(`region_id`) USING BTREE,

  CONSTRAINT `FK5rwmryny6jthaaxkogownknqp` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKfftoc2abhot8f2wu6cl9a5iky` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKpq2dhypk2qgt68nauh2by22jb` FOREIGN KEY (`avatar_id`) REFERENCES `user_avatar` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  -- 添加region_id
  CONSTRAINT `FKpq2dhvpkjqbtionvuh2vy00rd` FOREIGN KEY (`region_id`) REFERENCES `region` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT

) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, NULL, 'zhengjie@tom.com', 1, '$2a$10$fP.426qKaTmix50Oln8L.uav55gELhAd0Eg66Av4oG86u8km7D/Ky', 'admin', '王志通',2, '18888888888', 10,'431125' , '2018-08-23 09:11:56', '2019-05-18 17:34:21', '管理员', '男');
-- INSERT INTO `user` VALUES (3, NULL, 'test@eladmin.net', 1, '$2a$10$HhxyGZy.ulf3RvAwaHUGb.k.2i9PBpv4YbLMJWp8pES7pPhTyRCF.', 'test', 2, '17777777777', 12, '2018-12-27 20:05:26', '2019-04-01 09:15:24', '测试', '男');

-- ----------------------------
-- Table structure for user_avatar
-- ----------------------------
DROP TABLE IF EXISTS `user_avatar`;
CREATE TABLE `user_avatar`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实文件名',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `size` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大小',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户头像' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for users_roles
-- ----------------------------
DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE `users_roles`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `FKq4eq273l04bpu4efj0jd0jb98`(`role_id`) USING BTREE,
  CONSTRAINT `FKgd3iendaoyh04b95ykqise6qh` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKt4v0rrweyk393bdgt107vdx0x` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of users_roles
-- ----------------------------
INSERT INTO `users_roles` VALUES (1, 1);
INSERT INTO `users_roles` VALUES (3, 2);

-- ----------------------------
-- Table structure for verification_code
-- ----------------------------
DROP TABLE IF EXISTS `verification_code`;
CREATE TABLE `verification_code`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `status` bit(1) NULL DEFAULT NULL COMMENT '状态：1有效、0过期',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码类型：email或者短信',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收邮箱或者手机号码',
  `scenes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务名称：如重置邮箱、重置密码等',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '验证码' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for visits
-- ----------------------------
DROP TABLE IF EXISTS `visits`;
CREATE TABLE `visits`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NULL DEFAULT NULL,
  `date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip_counts` bigint(20) NULL DEFAULT NULL,
  `pv_counts` bigint(20) NULL DEFAULT NULL,
  `week_day` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_11aksgq87euk9bcyeesfs4vtp`(`date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '访客记录' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

-- 企业员工表
drop table if exists `employee`;
create table `employee` (
 `id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
 `number` varchar (255) null default null comment '编号',
 `name` varchar (255) null default null comment '姓名',
 `position` varchar (255) null default null comment '职位',
 `employee_type` varchar (255) null default null comment '员工类型',
 `dept_id` bigint(20) null default null comment '所属机构ID',
 `remark` varchar (255) null default null comment '备注',
 `create_time` datetime null default null comment '创建时间',
 `contact` varchar (255) null default null comment '联系方式',
 `id_number` varchar (255) null default null comment '身份证号码',
 `address` varchar (255) null default null comment '家庭住址',
 `health_condition` varchar (255) null default null comment '健康情况',
 `cause_poverty` varchar (255) null default null comment '致贫原因',
 `responsible_person` varchar (255) null default null comment '帮扶责任人',
 `education_level`varchar (255) null default null comment '文化程度',
 `family_situation` varchar (255) null default null comment '家庭情况',
 `cultivated_area` varchar (255) null default null comment '耕地面积',
 `object_property` varchar (255) null default null comment '对象属性',
 `identification_standard` varchar (255) null default null comment '识别标准',
 INDEX `FK5rwmryny6jthaaxkaaawnknqp`(`dept_id`) USING BTREE,
 CONSTRAINT `FK5rwmryny6jthaaxkaaawnknqp` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;


-- 农产品类别表
drop table if exists `category`;
create table `category`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment '分类ID',
`code` varchar (255) null default null comment '分类代码',
`name` varchar (255) null default null comment '分类名称',
`description` varchar (255) null default null comment '描述',
`pid` bigint(20) null default null comment '父级ID',
`create_time` datetime  null default null comment '创建时间',
`create_uid` datetime  null default null comment '创建者ID',
`update_time` datetime  null default null comment '更新时间',
`update_uid` datetime  null default null comment '更新者ID'
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- 农产品基本描述项
drop table if exists `description_item`;
create table `description_item`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
`code` varchar (255) null default null comment '编码',
`category_id` bigint(20) null default null comment '所属类型ID',
`chinese_name` varchar (255) null default null comment '中文名称',
`english_name` varchar (255) null default null comment '英文名称',
`mark` varchar (255) null default null comment '标记',
`description` varchar (255) null default null comment '说明',
`datatype_format` varchar (255) null default null comment '数据类型及格式',
`range` varchar (255) null default null comment '值域',
`synonym` varchar (255) null default null comment '同义词',
`restrictions` varchar (255) null default null comment '约束/条件',
`unit` varchar (255) null default null comment '计量单位',
`remark` varchar (255) null default null comment '备注',
`create_uid` bigint(20) null default null comment '创建人ID',
`create_time` datetime null default null comment '创建时间',
`create_username` varchar (255) null default null comment '创建者用户名',
`update_uid` bigint(20) null default null comment '修改人ID',
`update_time` datetime null default null comment '修改时间',
`update_username` varchar (255) null default null comment '修改者用户名',
INDEX `FK5xxxryny6jthaaxkaaawnknqp`(`category_id`) USING BTREE,
 CONSTRAINT `FK5xxxryny6jthaaxkaaawnknqp` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- 组织机构产品目录
drop table if exists `product`;
create table `product`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment '产品ID',
`name` varchar (255) null default null comment '产品名称',
`code` varchar (255) null default null comment '产品代码',
`category_id` bigint(20) null default null comment '分类ID',
`product_type` varchar (255) null default null comment '类型',
`scope` varchar (255) null default null comment '使用范围',
`enabled` int(11) null default null comment '启用',
`region_id` varchar (255) null default null comment '所属区域ID',
`dept_id` bigint(20) null default null comment '所属部门ID',
`remark` varchar (255) null default null comment '备注',
`create_uid` bigint(20) null default null comment '创建人ID',
`create_time` datetime null default null comment '创建时间',
`update_uid` bigint(20) null default null comment '修改者ID',
`update_time` datetime null default null comment '修改时间',
INDEX `FK5123ryny6jthaaxkaaawnknqp`(`category_id`) USING BTREE,
INDEX `FK5234ryny6jthaaxkaaawnknqp`(`region_id`) USING BTREE,
INDEX `FK5235ryny6jthaaxkaaawnknqp`(`dept_id`) USING BTREE,
CONSTRAINT `FK5123ryny6jthaaxkaaawnknqp` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `FK5234ryny6jthaaxkaaawnknqp` FOREIGN KEY (`region_id`) REFERENCES `region` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `FK5235ryny6jthaaxkaaawnknqp` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;


-- 检测项基础属性库
drop table if exists `inspection_item`;
create table `inspection_item`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
`code` varchar (255) null default null comment '编码',
`name` varchar (255) null default null comment '名称',
`unit` varchar (255) null default null comment '单位',
`compare_direction` varchar (255) null default null comment '比较方向',
`checkout_type` varchar (255) null default null comment '检出类型',
`lower_limit` decimal (16,3) null default null comment '参考下限',
`lower_limit_comparator` varchar (255) null default null comment '下限比较符',
`upper_limit` decimal (16,3) null default null comment '参考上限',
`upper_limit_comparator` varchar (255) null default null comment '上限比较符',
`reference_value1` varchar (255) null default null comment '参考值1',
`reference_value2` varchar (255) null default null comment '参考值2',
`reference_value3` varchar (255) null default null comment '参考值3',
`classification_code` varchar (255) null default null comment '分类代码',
`classification_name` varchar (255) null default null comment '分类名称',
`version` varchar (255) null default null comment '版本号',
`backup1` varchar (255) null default null comment '备用1',
`backup2` varchar (255) null default null comment '备用2',
`backup3` varchar (255) null default null comment '备用3',
`backup4` varchar (255) null default null comment '备用4',
`backup5` varchar (255) null default null comment '备用5',
`create_time` datetime null default null comment '创建时间'
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- 检测模板表
drop table if exists `inspection_template`;
create table `inspection_template`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
`name` varchar (255) null default null comment '模板名称',
`it_type` varchar (255) null default null comment '类别',
`scope` varchar (255) null default null comment '使用范围',
`user_id` bigint(20) null default null comment '使用者ID',
`product_id` bigint(20) null default null comment '产品ID',
`enabled` int(11) null default null comment '是否启用',
`remark` varchar (255) null default null comment '备注',
`create_time` datetime null default null comment '创建时间',
INDEX `FKsfjkryny6jthaaxkaaawnknqp`(`user_id`) USING BTREE,
INDEX `FKimefryny6jthaaxkaaawnknqp`(`product_id`) USING BTREE,
CONSTRAINT `FKimefryny6jthaaxkaaawnknqp` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `FKsfjkryny6jthaaxkaaawnknqp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- 检测模板与项目关联表
drop table if exists `inspection_template_item`;
create table `inspection_template_item`(
`inspection_template_id` bigint(20) not null comment '检测模板ID',
`inspection_item_id` bigint(20) not null comment '检测项目ID',
PRIMARY KEY (`inspection_template_id`, `inspection_item_id`) USING BTREE,
CONSTRAINT `FKgd3iendsyyh04b95ykqise6qh` FOREIGN KEY (`inspection_template_id`) REFERENCES `inspection_template` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `FKt4v0rrreyk393bdgt107vdx0x` FOREIGN KEY (`inspection_item_id`) REFERENCES `inspection_item` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;


-- 产品检测记录表
drop table if exists `inspection_record`;
create table `inspection_record`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
`code` varchar (255) null default null comment '编号',
`batch_number` varchar (255) null default null comment '检测批次',
`inspector` varchar (255) null default null comment '检测员',
`inspect_time` datetime null default null comment '检测日期',
`is_passed` int(11) null default null comment '是否通过',
`product_id` bigint(20) null default null comment '产品ID',
`dept_id` bigint(20) null default null comment '所属机构ID',
`is_submit` int(11) null default null comment '是否提交',
`remark` varchar (255) null default null comment '备注',
`create_time` datetime null default null comment '创建时间',
INDEX `FKimefryny6jththykaaawnknqp`(`product_id`) USING BTREE,
INDEX `FKimefryny6jthedikaaawnknqp`(`dept_id`) USING BTREE,
CONSTRAINT `FKimefryny6jththykaaawnknqp` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `FKimefryny6jthedikaaawnknqp` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;


-- 产品检测记录与模板关联表
-- #没有添加外键约束#
drop table if exists `inspection_record_template`;
create table `inspection_record_template`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
`inspection_record_id` bigint(20) null default null comment '检测记录ID',
`inspection_template_id` bigint(20) null default null comment '检测模板ID',
`inspection_template_name` varchar (255) null default null comment '模板名称',
`inspection_item_id` bigint(20) null default null comment '检测项目ID',
`inspection_item_name` varchar (255) null default null comment '检测项目名称',
`is_detected` int(11) null default null comment '是否检出',
`is_exceeded` int(11) null default null comment '是否超标',
`inspect_method` varchar (255) null default null comment '检测方法'
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- 产品采购记录表
drop table if exists `purchase_record`;
create table `purchase_record`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
`product_id` bigint(20) null default null comment '产品ID',
`product_name` varchar (255) null default null comment '产品名称',
`batch_number` varchar (255) null default null comment '采购批次',
`purchase_quantity` decimal (16,0) null default null comment '采购数量',
`unit` varchar (255) null default null comment '计数单位',
`goods_name` varchar (255) null default null comment '原材料名称',
`producer_name` varchar (255) null default null comment '生产者名称',
`producer_address` varchar (255) null default null comment '生产者地址',
`producer_contact` varchar (255) null default null comment '生产者联系方式',
`purchase_time` datetime null default null comment '采购时间',
`dept_id` bigint(20) null default null comment '所属组织机构',
`record_status` int (11) null default null comment '状态',
`remark` varchar (255) null default null comment '备注',
`create_time` datetime null default null comment '创建时间',
INDEX `FKimefryny6jththyk123wnknqp`(`product_id`) USING BTREE,
INDEX `FKimefryny6jthedik321wnknqp`(`dept_id`) USING BTREE,
CONSTRAINT `FKimefryny6jththyk123wnknqp` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `FKimefryny6jthedik321wnknqp` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- 产品生产数据表
drop table if exists `product_data`;
create table `product_data`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
`product_id` bigint(20) null default null comment '产品ID',
`product_name` varchar (255) null default null comment '产品名称',
`batch_number` varchar (255) null default null comment '生产批次',
`material_batch` varchar (255) null default null comment '原料批次',
`manufacture_date` datetime null default null comment '生产日期',
`area` decimal (16,0) null default null comment '种植面积',
`area_unit` varchar (255) null default null comment '面积单位',
`count` decimal (16,0) null default null comment '养殖数量',
`count_unit` varchar (255) null default null comment '数量单位',
`output` decimal (16,0) null default null comment '产量',
`output_unit` varchar (255) null default null comment '产量单位',
`data_status` int(11) null default null comment '状态',
`dept_id` bigint(20) null default null comment '所属组织机构',
`dept_name` varchar (255) null default null comment '组织机构名称',
`product_type` varchar (255) null default null comment '产品类型',
`remark` varchar (255) null default null comment '备注',
`pid` bigint(20) null default null comment '父级ID',
`create_time` datetime null default null comment '创建时间',
INDEX `FKimefryny6jththykiurwnknqp`(`product_id`) USING BTREE,
INDEX `FKimefryny6jthedikslgwnknqp`(`dept_id`) USING BTREE,
CONSTRAINT `FKimefryny6jththykiurwnknqp` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `FKimefryny6jthedikslgwnknqp` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- 产品销售数据表
drop table if exists `sales_record`;
create table `sales_record`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
`order_number` varchar (255) null default null comment '订单号',
`region_id` varchar (255) null default null comment '销售区域ID',
`sales_area` varchar (255) null default null comment '销售区域',
`batch_number` varchar (255) null default null comment '生产批次',
`sales_number` decimal(16,0) null default null comment '销量',
`sales_unit` varchar (255) null default null comment '计量单位',
`price` decimal (16,2) null default null comment '单价',
`price_unit` varchar (255) null default null comment '价格单位',
`sales` decimal null default null comment '销售额',
`sales_date` datetime null default null comment '销售日期',
`product_id` bigint(20) null default null comment '产品ID',
`product_code` varchar (255) null default null comment '产品编码',
`product_name` varchar (255) null default null comment '产品名称',
`dept_id` bigint(20) null default null comment '部门ID',
`dept_name` varchar (255) null default null comment '部门名称',
`sales_status` int(11) null default null comment '状态',
`remark` varchar (255) null default null comment '备注',
`create_time` datetime null default null comment '创建时间',
INDEX `FKimefryny6jsyzjykiurwnknqp`(`product_id`) USING BTREE,
INDEX `FKimefryny6juijlikslgwnknqp`(`dept_id`) USING BTREE,
CONSTRAINT `FKimefryny6jsyzjykiurwnknqp` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `FKimefryny6juijlikslgwnknqp` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- 行业数据统计
drop table if exists `industry_statistics`;
create table `industry_statistics`(
`id` bigint(20) not null primary key AUTO_INCREMENT comment 'ID',
`region_id` varchar (255) null default null comment '行政编码',
`region_name` varchar (255) null default null comment '行政名称',
`statistics_item` varchar (255) null default null comment '统计项目',
`statistics_total` decimal (16,0) null default null comment '统计总量',
`unit` varchar (255) null default null comment '计量单位',
`create_uid` bigint(20) null default null comment '统计人ID',
`create_username` varchar (255) null default null comment '统计人',
`statistics_time` datetime null default null comment '统计时间'
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;

-- 产品分布统计数据
drop table if exists `product_statistics`;
create table `product_statistics`(
`id` bigint(20) not null primary key comment 'ID',
`region_id` varchar (255) null default null comment '行政编码',
`region_name` varchar (255) null default null comment '行政名称',
`product_code` varchar (255) null default null comment '产品编码',
`product_name` varchar (255) null default null comment '产品名称',
`statistics_item` varchar (255) null default null comment '统计项目',
`statistics_total` decimal (16,0) null default null comment '统计总量',
`unit` varchar (255) null default null comment '计量单位',
`create_uid` bigint(20) null default null comment '统计人ID',
`create_username` varchar (255) null default null comment '统计人',
`statistics_time` datetime null default null comment '统计时间'
)engine = InnoDB default CHARSET = utf8 ROW_FORMAT = Compact;
