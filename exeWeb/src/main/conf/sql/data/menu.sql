INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (2, 0, '数据开发平台', NULL, '/html/views/admin/', NULL, NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (3, 0, '主页', NULL, NULL, NULL, '2021-1-1 19:02:55');
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (4, 2, '数据开发', NULL, NULL, NULL, NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (5, 0, '系统管理', NULL, NULL, '2020-12-30 17:33:19', '2021-1-2 22:06:53');
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (8, 0, '收藏夹', NULL, NULL, '2021-1-2 22:07:38', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (9, 0, '设置', NULL, NULL, '2021-1-2 22:07:46', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (10, 4, '应用管理', NULL, '/html/views/app/list.html', '2021-1-2 22:08:07', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (11, 4, '资源管理', NULL, '/html/views/resource/list.html', '2021-1-2 22:08:17', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (12, 4, '文件管理', NULL, '/html/views/fileMgt/index.html', '2021-1-2 22:08:25', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (13, 4, '资源版本管理', NULL, '/html/views/version/list.html', '2021-1-2 22:08:36', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (14, 4, '数据库管理', NULL, '/html/views/db/index.html', '2021-1-2 22:08:48', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (15, 4, '文件索引', NULL, '/html/views/index/index.html', '2021-1-2 22:08:56', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (16, 4, '类索引', NULL, '/html/views/index/class.html', '2021-1-2 22:09:04', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (17, 4, '方法索引', NULL, '/html/views/index/method.html', '2021-1-2 22:09:12', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (18, 5, '用户管理', NULL, '/html/views/user/list.html', '2021-1-2 22:09:26', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (19, 5, '角色管理', NULL, '/html/views/role/list.html', '2021-1-2 22:09:33', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (20, 5, '菜单管理', NULL, '/html/views/menu/list.html', '2021-1-2 22:09:41', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (21, 8, '收藏管理', NULL, '/html/views/scj/list.html', '2021-1-2 22:10:02', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (22, 8, '公共收藏', NULL, '/html/views/scj/public.html', '2021-1-2 22:10:13', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (23, 8, '标签管理', NULL, '/html/views/scj/tag/list.html', '2021-1-2 22:10:26', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (24, 8, '网页收集工具', NULL, '/html/views/scj/webtools.html', '2021-1-2 22:10:40', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (25, 9, '系统设置', NULL, NULL, '2021-1-2 22:10:50', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (26, 25, '清理缓存', NULL, '/api/app/page?page=1&limit=10&id=&appName=&contextPath=&c=true', '2021-1-2 22:11:05', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (27, 3, '我的收藏', NULL, '/html/views/scj/list.html', '2021-1-2 22:14:33', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (28, 5, '任务管理', NULL, '/html/views/task/list.html', '2021-1-18 13:08:28', '2021-1-18 13:12:10');
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (29, 5, '查看日志', NULL, '/html/views/fileMgt/index.html?currPath=logs', '2021-1-20 11:13:24', '2021-3-9 10:38:00');
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (30, 0, 'IPTV', NULL, '_', '2021-1-25 10:01:48', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (31, 30, '订购用户管理', NULL, '/html/views/iptv/orderuser/list.html', '2021-1-25 10:02:28', NULL);
INSERT INTO `menu` (`id`, `parentId`, `name`, `remark`, `url`, `createTime`, `upTime`) VALUES (32, 30, '黑名单用户', NULL, '/html/views/iptv/blackuser/list.html', '2021-1-25 10:02:58', NULL);
