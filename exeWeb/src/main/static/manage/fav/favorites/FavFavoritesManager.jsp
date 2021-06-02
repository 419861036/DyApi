<%@page import="com.base.auth.ssoclient.util.RespUtil"%>
<%@page import="com.base.auth.ssoclient.web.context.ConstantContext"%>
<%@page import="com.base.auth.ssoclient.vo.SSOUserVo"%>
<%@ page language="java" session="false" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <tag:basePath pageContext="${pageContext}"></tag:basePath>
    <title>收藏夹 </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Le styles -->
    <link href="${resourcePath}admin/Scripts/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${resourcePath}admin/Css/skin/default.css" rel="stylesheet">
    <style type="text/css">
    </style>
  </head>
  <body>
	<div class="panel panel-default">
	  <div class="panel-body" id="search">
			<label>名称:</label>
			<input type="text" id="typeName"/>
			<button type="button" class="btn btn-primary" onclick="init();">搜索</button>
	  </div>
	</div>
	<div class="panel panel-default">
	  <div class="panel-heading">
	    <h3 class="panel-title">
	    	<div class="btn-group" role="group" aria-label="...">
			  <button type="button" class="btn btn-primary btn-sm" onclick="editFavFavorites();">添加</button>
			  <button type="button" class="btn btn-danger btn-sm"  onclick="deleteFavFavorites();">删除</button>
			</div>
	    </h3>
	  </div>
	  <div class="panel-body">
	  		<table class="table table-hover table-condensed" cellpadding="0" cellspacing="0" border="0" >
				<thead>
					<tr>
						<th style="width: 30px;"></th> 
						<th><input class="checkbox" type="checkbox"id="checkAll" /></th>
						<th style="width:11.11111111111111%">编号</th>
<th style="width:11.11111111111111%">总数</th>
<th style="width:11.11111111111111%">创建时间</th>
<th style="width:11.11111111111111%">最后修改时间</th>
<th style="width:11.11111111111111%">名称</th>
<th style="width:11.11111111111111%">数量</th>
<th style="width:11.11111111111111%">用户ID</th>

						<th>操作</th>
					</tr>
				</thead>
			<tbody id="contentTable">
				
			</tbody>
		</table>
		<div id="pager"></div>
	  </div>
	</div>
<script type="text/javascript" src="${resourcePath}admin/Scripts/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="${resourcePath}admin/Scripts/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${resourcePath}admin/Scripts/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="${resourcePath}admin/Scripts/kkdUtil.js"></script>
<script type="text/javascript" src="${resourcePath}admin/Scripts/kkdUi.js"></script>
<script type="text/javascript" src="${resourcePath}admin/Scripts/common.js"></script>
<script type="text/javascript">
var id = "${param.id}";
var resourceManagerPath="${resourceManagerPath}";
var resourcePath="${resourcePath}";
</script>
<script type="text/javascript" src="./FavFavorites-list.js"></script>
  </body>
</html>
