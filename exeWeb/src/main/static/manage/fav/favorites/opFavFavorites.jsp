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
    <title>编辑收藏夹</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Le styles -->
    <link href="${resourcePath}admin/Scripts/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">
    </style>
  </head>
  <body>
	<ol class="breadcrumb">
	  <li>收藏夹</li>
	  <li class="active">编辑收藏夹</li>
	</ol>
	
	<div class="panel panel-default">
	  <div class="panel-body"> 
			<form id="edit_form">
             	<div class="form-group">
 <label for="recipient-name" class="control-label">编号</label>
<input class="form-control" id="id" type="text">
<p class="help-block">编号</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">总数</label>
<input class="form-control" id="count" type="text">
<p class="help-block">总数</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">创建时间</label>
<input class="form-control" id="createTime" type="text">
<p class="help-block">创建时间</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">最后修改时间</label>
<input class="form-control" id="lastModifyTime" type="text">
<p class="help-block">最后修改时间</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">名称</label>
<input class="form-control" id="name" type="text">
<p class="help-block">名称</p> 
 </div><div class="form-group">
 <label for="recipient-name" class="control-label">数量</label>
<input class="form-control" id="publicCount" type="text">
<p class="help-block">数量</p> 
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
<script type="text/javascript" src="./FavFavorites.js"></script>
  </body>
</html>
