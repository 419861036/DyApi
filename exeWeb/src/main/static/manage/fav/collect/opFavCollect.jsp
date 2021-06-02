<%@page import="com.base.auth.ssoclient.util.RespUtil"%>
<%@page import="com.base.auth.ssoclient.web.context.ConstantContext"%>
<%@page import="com.base.auth.ssoclient.vo.SSOUserVo"%>
<%@ page language="java" session="false" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head> 
    <meta charset="utf-8">
    <tag:basePath pageContext="${pageContext}"></tag:basePath>
    <title>编辑收藏</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Le styles -->
    <link href="${resourcePath}admin/Scripts/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">
    </style>
  </head>
  <body>
	<ol class="breadcrumb">
	  <li>收藏</li>
	  <li class="active">编辑收藏</li>
	</ol>
	
	<div class="panel panel-default">
	  <div class="panel-body"> 
			<form id="edit_form">
             	<div class="form-group">
 <label for="recipient-name" class="control-label">编号</label>
<input class="form-control" id="id" type="text">
<p class="help-block">编号</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">分类</label>
<input class="form-control" id="category" type="text">
<p class="help-block">分类</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">字符集</label>
<input class="form-control" id="charset" type="text">
<p class="help-block">字符集</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">创建时间</label>
<input class="form-control" id="createTime" type="text">
<p class="help-block">创建时间</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">描述</label>
<input class="form-control" id="description" type="text">
<p class="help-block">描述</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">收藏id</label>
<input class="form-control" id="favoritesId" type="text">
<p class="help-block">收藏id</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">是否删除</label>
<input class="form-control" id="isDelete" type="text">
<p class="help-block">是否删除</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">最后修改时间</label>
<input class="form-control" id="lastModifyTime" type="text">
<p class="help-block">最后修改时间</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">logo</label>
<input class="form-control" id="logoUrl" type="text">
<p class="help-block">logo</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">备注</label>
<input class="form-control" id="remark" type="text">
<p class="help-block">备注</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">标题</label>
<input class="form-control" id="title" type="text">
<p class="help-block">标题</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">类型</label>
<input class="form-control" id="type" type="text">
<p class="help-block">类型</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">url</label>
<input class="form-control" id="url" type="text">
<p class="help-block">url</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">用户ID</label>
<input class="form-control" id="userId" type="text">
<p class="help-block">用户ID</p> 
 </div>
            </form>
	  </div>
	</div>
	
<script type="text/javascript" src="${resourcePath}admin/Scripts/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="${resourcePath }admin/Scripts/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${resourcePath}admin/Scripts/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="${resourcePath}admin/Scripts/kkdUtil.js"></script>
<script type="text/javascript" src="${resourcePath}admin/Scripts/kkdUi.js"></script>
<script type="text/javascript" src="${resourcePath}admin/Scripts/common.js"></script>
<script type="text/javascript">
var id = "${param.id}";
var resourceManagerPath="${resourceManagerPath}";
var resourcePath="${resourcePath}";
</script>
<script type="text/javascript" src="./FavCollect.js"></script>
  </body>
</html>
