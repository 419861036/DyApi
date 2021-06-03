/*
Navicat MySQL Data Transfer

Source Server         : 47.93.26.128
Source Server Version : 50636
Source Database       : xmlcore

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2021-06-03 15:46:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for api
-- ----------------------------
DROP TABLE IF EXISTS `api`;
CREATE TABLE `api` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `appId` int(11) DEFAULT NULL COMMENT '应用ID',
  `name` varchar(255) DEFAULT NULL COMMENT 'API名称',
  `remark` varchar(255) DEFAULT NULL COMMENT 'API描述',
  `path` varchar(255) DEFAULT NULL COMMENT 'API路径',
  `type` varchar(255) DEFAULT NULL COMMENT 'API请求类型',
  `request` varchar(255) DEFAULT NULL COMMENT 'API请求示例',
  `respose` varchar(255) DEFAULT NULL COMMENT 'API响应示例',
  `content` longtext COMMENT 'API内容',
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for cms_article
-- ----------------------------
DROP TABLE IF EXISTS `cms_article`;
CREATE TABLE `cms_article` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '栏目编号',
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '标题',
  `link` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文章链接',
  `color` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标题颜色',
  `image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文章图片',
  `keywords` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '关键字',
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述、摘要',
  `weight` int(11) DEFAULT '0' COMMENT '权重，越大越靠前',
  `weight_date` datetime DEFAULT NULL COMMENT '权重期限',
  `hits` int(11) DEFAULT '0' COMMENT '点击数',
  `posid` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '推荐位，多选',
  `custom_content_view` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '自定义内容视图',
  `view_config` text COLLATE utf8_unicode_ci COMMENT '视图配置',
  `create_by` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='文章表';

-- ----------------------------
-- Table structure for cms_article_data
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_data`;
CREATE TABLE `cms_article_data` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` text COLLATE utf8_unicode_ci COMMENT '文章内容',
  `copyfrom` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文章来源',
  `relation` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '相关文章',
  `allow_comment` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否允许评论',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='文章详表';

-- ----------------------------
-- Table structure for cms_category
-- ----------------------------
DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE `cms_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) COLLATE utf8_unicode_ci NOT NULL COMMENT '所有父级编号',
  `site_id` varchar(64) COLLATE utf8_unicode_ci DEFAULT '1' COMMENT '站点编号',
  `office_id` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '归属机构',
  `module` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '栏目模块',
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '栏目名称',
  `image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '栏目图片',
  `href` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '链接',
  `target` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '目标',
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `keywords` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '关键字',
  `sort` int(11) DEFAULT '30' COMMENT '排序（升序）',
  `in_menu` char(1) COLLATE utf8_unicode_ci DEFAULT '1' COMMENT '是否在导航中显示',
  `in_list` char(1) COLLATE utf8_unicode_ci DEFAULT '1' COMMENT '是否在分类页中显示列表',
  `show_modes` char(1) COLLATE utf8_unicode_ci DEFAULT '0' COMMENT '展现方式',
  `allow_comment` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否允许评论',
  `is_audit` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否需要审核',
  `custom_list_view` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '自定义列表视图',
  `custom_content_view` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '自定义内容视图',
  `view_config` text COLLATE utf8_unicode_ci COMMENT '视图配置',
  `create_by` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='栏目表';

-- ----------------------------
-- Table structure for cms_comment
-- ----------------------------
DROP TABLE IF EXISTS `cms_comment`;
CREATE TABLE `cms_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '栏目编号',
  `content_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '栏目内容的编号',
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '栏目内容的标题',
  `content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '评论内容',
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '评论姓名',
  `ip` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '评论IP',
  `create_date` datetime NOT NULL COMMENT '评论时间',
  `audit_user_id` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '审核人',
  `audit_date` datetime DEFAULT NULL COMMENT '审核时间',
  `del_flag` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='评论表';

-- ----------------------------
-- Table structure for exe_app
-- ----------------------------
DROP TABLE IF EXISTS `exe_app`;
CREATE TABLE `exe_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appName` varchar(255) NOT NULL COMMENT '应用名称',
  `contextPath` varchar(255) DEFAULT NULL COMMENT '应用路径',
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for exe_resource
-- ----------------------------
DROP TABLE IF EXISTS `exe_resource`;
CREATE TABLE `exe_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appId` int(11) DEFAULT NULL COMMENT '应用ID',
  `type` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `ruleName` varchar(255) DEFAULT NULL,
  `corn` varchar(255) DEFAULT NULL COMMENT '定时任务',
  `caiConfig` longtext,
  `caiRule` longtext,
  `loginRule` longtext,
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for exe_version
-- ----------------------------
DROP TABLE IF EXISTS `exe_version`;
CREATE TABLE `exe_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sourceId` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `tableName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `content` longtext CHARACTER SET utf8,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3270 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parentId` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `url` varchar(2000) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for resource_class_info
-- ----------------------------
DROP TABLE IF EXISTS `resource_class_info`;
CREATE TABLE `resource_class_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `indexId` varchar(255) DEFAULT NULL COMMENT '索引id',
  `package` varchar(255) DEFAULT NULL COMMENT '包名',
  `className` varchar(50) DEFAULT NULL COMMENT '类名',
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13907 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for resource_index
-- ----------------------------
DROP TABLE IF EXISTS `resource_index`;
CREATE TABLE `resource_index` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  `type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `md5` varchar(36) DEFAULT NULL COMMENT '指纹',
  `state` varchar(20) DEFAULT NULL COMMENT '索引状态 已处理，未处理',
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `path` (`path`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for resource_method_info
-- ----------------------------
DROP TABLE IF EXISTS `resource_method_info`;
CREATE TABLE `resource_method_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `packageName` varchar(255) DEFAULT NULL COMMENT '包名',
  `className` varchar(100) DEFAULT NULL COMMENT '类名',
  `indexId` varchar(255) DEFAULT NULL COMMENT '文件索引',
  `methodName` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `param` longtext COMMENT '参数',
  `lineNum` varchar(50) DEFAULT NULL COMMENT '行号',
  `returnType` varchar(255) DEFAULT NULL COMMENT '返回类型',
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=341496 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for role_menu_ref
-- ----------------------------
DROP TABLE IF EXISTS `role_menu_ref`;
CREATE TABLE `role_menu_ref` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleId` int(11) DEFAULT NULL,
  `menuId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ruleName` varchar(255) DEFAULT NULL,
  `corn` varchar(255) DEFAULT NULL COMMENT '定时任务',
  `caiConfig` longtext,
  `caiRule` longtext,
  `loginRule` longtext,
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for scj_sc
-- ----------------------------
DROP TABLE IF EXISTS `scj_sc`;
CREATE TABLE `scj_sc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `private` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '私有的',
  `remark` longtext COLLATE utf8_bin,
  `link` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `userName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for scj_tag
-- ----------------------------
DROP TABLE IF EXISTS `scj_tag`;
CREATE TABLE `scj_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for scj_tag_sc_ref
-- ----------------------------
DROP TABLE IF EXISTS `scj_tag_sc_ref`;
CREATE TABLE `scj_tag_sc_ref` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tagId` int(11) DEFAULT NULL,
  `scId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=405582157 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名',
  `description` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron表达式',
  `bean_class` varchar(255) DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名',
  `params` varchar(1000) DEFAULT NULL,
  `job_status` varchar(255) DEFAULT NULL COMMENT '任务状态',
  `job_group` varchar(255) DEFAULT NULL COMMENT '任务分组',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `upTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;


-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `nickName` varchar(255) DEFAULT NULL,
  `pwd` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `upTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for user_role_ref
-- ----------------------------
DROP TABLE IF EXISTS `user_role_ref`;
CREATE TABLE `user_role_ref` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `roleId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
SET FOREIGN_KEY_CHECKS=1;
