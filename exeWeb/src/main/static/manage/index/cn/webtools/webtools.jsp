<%@ page language="java" session="false" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html >
<html lang="en">
<head>
<tag:basePath pageContext="${pageContext}"></tag:basePath>
   <meta charset="utf-8"/>
   <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
   <meta name="description" content="Bootstrap Admin App + jQuery"/>
   <meta name="keywords" content="app, responsive, jquery, bootstrap, dashboard, admin"/>
   <title>Favorites</title>
   <link rel="icon" href="/img/icon.ico" type="image/x-icon" />
 
   <link rel="stylesheet" href="./static/vendor/fontawesome/css/font-awesome.min.css"></link>
   <link rel="stylesheet" href="./static/vendor/simple-line-icons/css/simple-line-icons.css"></link>
   <link rel="stylesheet" href="./static/media/css/bootstrap.css" id="bscss"></link>
   <link rel="stylesheet" href="./static/media/css/app.css" id="maincss"></link>
   <link rel="stylesheet" href="./static/media/css/theme-i.css" id="maincss"></link>
</head>
<body>
   <div class="wrapper">
      <div class="block-center mt-xl wd-xl">
         <div class="panel panel-dark panel-flat">
            <div class="panel-heading text-center">
               <a href="/" style="color:#fff;">Favorites</a>
            </div>
            <div class="panel-body">
              <form id="collect-form" class="form-horizontal" onsubmit="return false">
               <div id="show1">
               <div class="media mb">
                  <a id="logoA" href="javascript:void(0);" target="_blank" class="pull-left">
                      <div class="media-object resource-card-image-small-loading" style="background-image:url('/img/loading.gif')"></div>
                  </a>
                  <div class="media-body">
                     <h4 class="media-heading resource-card-title-small form-control-static" id="titleShow"></h4>
                  </div>
               </div>
               </div>
               <div id="show2" style="display:none;">
               		<div class="mb">
                     标题
                     <textarea name="title" id="title" class="form-control" cols="30" rows="2" placeholder="留空则自动抓取"></textarea>
                  </div>
                  <div class="mb">
                     简介
                     <textarea name="description" id="description" class="form-control" cols="30" rows="2" placeholder="留空则自动抓取"></textarea>
                  </div>
                  <div class="mb">
                     图片链接地址
                     <textarea name="logoUrl" id="logoUrl" class="form-control" cols="30" rows="2" placeholder="留空则自动抓取" value=""></textarea>
                  </div>
                   <div class="mb">
                       所属类别
                       <select name="category" id="category" class="form-control" >
                           <option value="">--请选择--</option>
                           <option value="TRAVEL">旅行</option>
                           <option value="FOOD">料理</option>
                           <option value="EXERCISE">健身</option>
                           <option value="PHOTOGRAPH">摄影</option>
                           <option value="MUSIC">音乐</option>
                           <option value="REARING">育儿</option>
                           <option value="LOVE">恋爱</option>
                           <option value="BUSINESS">创业</option>
                           <option value="ART">美术设计</option>
                           <option value="MANAGER">产品经理</option>
                           <option value="MARKET">市场营销</option>
                           <option value="RUNNING">运营</option>
                       </select>
                   </div>
               </div>
               
                  <div class="mb">
                     收藏备注
                     <textarea name="remark" id="remark" class="form-control" cols="30" rows="2" placeholder="请输入收藏备注，也可以在这里@好友"></textarea>
                  </div>
                  <div class="pull-right dropdown dropdown-list">
                     <a href="javascript:void(0);" id="atshow" data-toggle="dropdown">
                        at好友 <i class="fa fa-caret-down"></i>
                     </a>
                     <ul class="dropdown-menu">
                        <li>
                            <!--<a href="javascript:void(0);" th:each="name : ${followList}" th:onclick="'javascript:showAt(\''+${name}+'\');'" th:text="${name}">好友名称</a>-->
                            
                        </li>
                     </ul>
                  </div>
                  <label style="font-weight:normal;margin-bottom:0;">
                  <input id="type" type="checkbox" name="type" class="mb" value="PRIVATE" /> 私密收藏</label>
               <p class="pt-lg text-center">选择一个收藏夹</p>
               <div class="collect-sort-folder">
                  <div class="collect-tab-head">
                      <a href="javascript:void(0);">智能推荐</a>
                      <a href="javascript:void(0);">我的收藏夹</a>
                  </div>
                  <div class="collect-tab-body">
                      <div class="wrap">
                          <div id="smartFavoritesList" class="sort">
                          </div>
                      </div>
                      <div class="wrap">
                          <div class="folder">
                               <select  name="favoritesId" id="favoritesId" class="mb form-control">
                               </select>
                               <p class="text-center">或者</p>
                               <input type="text" name="newFavorites" id="newFavorites" class="mb form-control" placeholder="新建分类收藏夹"/>
                          </div>
                      </div>
                  </div>
               </div>
               <button id="collect" class="mt-lg btn btn-primary btn-block">提交</button>
               <input type="hidden" name="url" id="url" value=""/>
               <input type="hidden" name="charset" id="charset" value=""/>
               </form>
               <p></p>
               <div id="errorMsg" class="alert alert-danger text-center" style="display:none;"></div>
            </div>
         </div>
         <!--<div class="text-right mt-sm" id="model1" >
            <a id="changeModel1" href="javascript:void(0);" style="color:#666;text-decoration:none;"> <i class="fa fa-refresh"></i>
               	切换成专家模式
            </a>
         </div>-->
         <div class="text-right mt-sm" id="model2" style="display:none;">
            <a id="changeModel2" href="javascript:void(0);" style="color:#666;text-decoration:none;"> <i class="fa fa-refresh"></i>
               	切换成简单模式
            </a>
         </div>
         <!-- <div class="text-center mb mt-lg">
            <a href="#">
               <img src="https://spoon-static.b0.upaiyun.com/app/img/collect_wechat.png" width="100px" height="50px" alt=""/></a>
            <a href="#">
               <img src="https://spoon-static.b0.upaiyun.com/app/img/collect_wechat_moment.png" width="100px" height="50px" alt=""/></a>
            <a href="#">
               <img src="https://spoon-static.b0.upaiyun.com/app/img/collect_qq.png" width="100px" height="50px" alt=""/></a>
         </div> -->
         <div class="p-lg text-center">
            <span>&copy;</span>
            <span>2016</span>
            <span>-</span>
            <span>2018</span>
            <br/>
            <span>cloudfavorites@126.com</span>
         </div>
      </div>
   </div>
   <script type="text/javascript" src="./jquery.min.js"></script>
   <script type="text/javascript" src="/ResourceWeb/admin/Scripts/kkdUtil.js"></script>
   <script type="text/javascript" src="/ResourceWeb/admin/Scripts/kkdUi.js"></script>
   <script type="text/javascript" src="/ResourceWeb/admin/Scripts/common.js"></script>

   <script src="./vue.min.js"></script>   
   <script src="./vue-resource.min.js"></script>
   <script src="./static/vendor/parsleyjs/dist/parsley.min.js"></script>  
   <script src="./comm.js"></script> 
   <script type="text/javascript" src="../templete.js"></script>
   <script id="option" type="text/html">
		<option value="{{id}}">{{name}}</option>
</script>
   <script type='text/javascript'>
   var basePath="/FavWeb/";
   	$(function(){
   		var config = {"id":4491,"userId":4506,"defaultFavorties":"16007","defaultCollectType":"public","defaultModel":"simple","createTime":1528942163679,"lastModifyTime":1528942163679,"collectTypeName":"\u516C\u5F00","modelName":"\u7B80\u5355"};
   		var followList = [];
   		var request = new UrlSearch();
   		$("#titleShow").html(decodeURI(request.title));
   		$("#title").val(decodeURI(request.title));
   		$("#url").val(decodeURIComponent(request.url));
   		$("#logoA").attr("href",decodeURIComponent(request.url));
   		$("#description").val(replaceEmpty(decodeURI(request.description)));
   		$("#charset").val(request.charset);
   		findFavList();
   		function findFavList(){
   	    	var url=basePath+"center/fav/favorites/findMyFavFavoritesPageList.do";
   	    	var itemsPerPage=100;
   	    	var data = {
   	    		page:1,
   	    		itemsPerPage:itemsPerPage
   	    	};
   	    	PostDataJson(url, data, function(res){
   	    		$.each(res.v.collection,function(a,b){
   	    			var cc=template('option', b)
   	    			$('#favoritesId').append(cc);
   	    		});
   	    	});
   		}
   		findLogoUrl();
   		function findLogoUrl(){
   	    	var url=basePath+"center/fav/webtools/getLogoUrl.do";
   	    	var data={'url':$("#url").val()};
   	    	PostDataJson(url, data, function(res){
   	    		var url=res.v;
   	    		url=url?url:basePath+"manage/index/cn/webtools/static/img/logo.jpg";
	   	    	 $("#logoUrl").val(url);
	             $("#logoA div").attr("class","media-object resource-card-image-small");
	             $("#logoA div").attr("style","background-image:url('"+url+"');");
   	    	});
   		}
//         $.ajax({
//             type: "POST",
//             url:"/collect/getCollectLogoUrl",
//             data:{'url':$("#url").val()},
//             success: function(response) {
//                 $("#logoUrl").val(response);
//                 $("#logoA div").attr("class","media-object resource-card-image-small");
//                 $("#logoA div").attr("style","background-image:url('"+response+"');");
//             },
//             error: function (jqXHR, textStatus, errorThrown) {
//                 console.log(jqXHR.responseText);
//                 console.log(jqXHR.status);
//                 console.log(jqXHR.readyState);
//                 console.log(jqXHR.statusText);
//                 console.log(textStatus);
//                 console.log(errorThrown);
//             }
//         });
        
        smartFavoritesFun($("#title").val(),$("#description").val(),'favoritesId');
   		if("private"== config.defaultCollectType){
   			$("#type").attr("checked","checked");
   		}
   		if("simple" == config.defaultModel){
   			$("#show2").hide();
   			$("#show1").show();
   			$("#model2").hide();
   			$("#model1").show();
   		}else{
   			$("#show1").hide();
   			$("#show2").show();
   			$("#model1").hide();
   			$("#model2").show();
   		}
   		$("#changeModel1").click(function(){
   			$("#show1").hide();
   			$("#show2").show();
   			$("#model1").hide();
   			$("#model2").show();
   		});
   		
   		$("#changeModel2").click(function(){
   			$("#show2").hide();
   			$("#show1").show();
   			$("#model2").hide();
   			$("#model1").show();
   		});
   		
   		$("#atshow").click(function(){
   			if(followList.length > 0){
   				$(".dropdown-menu").show();
   			}
   		});
   		
   		$(document).bind('click', function() {  
   	    	$(".dropdown-menu").hide();
   	    });  
   	    $('#atshow').bind('click', function(e) {  
   	    	if(e.stopPropagation){ 
   	            e.stopPropagation();
   	    	}else{ 
   	           e.cancelBubble = true;
   	     	} 
   	    });
   	    
   		
   	    
   		$("#collect").click(function(){
              if($("#title").val()==""){
                  $("#errorMsg").text("标题不能为空");
                  $("#errorMsg").show();
                  return;
              }
              
              var inputs=$("#collect-form").find("input, select,textarea");
	          	var data = {};
	          	$.each(inputs,function(a,b){
	          		var key=$(b).attr("id");
	          		var val=$(b).val();
	          		data[key]=val;
	          	});
	          	data.type=$("#type").prop("checked")?"PRIVATE":"PUBLIC";
// 	          $("#favorites_id").
              $("#collect").attr("disabled","disabled");
              $("#errorMsg").hide();
              var id="";
              var url=id?basePath +"center/fav/collect/updateFavCollect.do":basePath +"center/fav/collect/addFavCollect.do";
              
              PostDataJsonObj(url, JSON.stringify(data), function(res){
         	 		if(res && res.code == 200) {
         	 			$("#errorMsg").text('操作成功');
                        $("#errorMsg").show();
         	 		}else{
         	 			 $("#errorMsg").text(res.msg?res.msg:'操作失败');
                         $("#errorMsg").show();
         	 		}
         	 	});
//               $.ajax({
//                      type: "POST",
//                      url:"/collect/collect",
//                      data:$("#collect-form").serialize(),
//                      success: function(response) {
//                          if(response.rspCode == '000000'){
//                             window.location="/";
//                          }else{
//                              if(response.hasOwnProperty("rspCode")){
//                                  $("#errorMsg").text(response.rspMsg);
//                                  $("#errorMsg").show();
//                                  $("#collect").removeAttr("disabled");
//                              }else{
//                                  window.location="/login";
//                              }
//                          }
//                      },
//                      error: function (jqXHR, textStatus, errorThrown) {
//                          console.log(jqXHR.responseText);
//                          console.log(jqXHR.status);
//                          console.log(jqXHR.readyState);
//                          console.log(jqXHR.statusText);
//                          console.log(textStatus);
//                          console.log(errorThrown);
//                      }
//               });
         });
    });
   	function showAt(name){
   		var text = $("#remark").val();
   		$("#remark").val(text + "@" +name + " ").focus();
   		$(".dropdown-menu").hide();
   	}
   </script>  
   <div style="display:none">
   	  <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1260466344'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1260466344' type='text/javascript'%3E%3C/script%3E"));</script>
    </div>
   <script>
      $(document).ready(function() {
         $('.collect-sort-folder').each(function() {
            $(this).find('.collect-tab-head').each(function() {
                $(this).children().eq(0).addClass('active')
            });
            $(this).find('.collect-tab-body').each(function() {
                $(this).children().eq(0).show();
            });
            $(this).find('.collect-tab-head').children().on('click', function() {
                $(this).addClass('active').siblings().removeClass('active');
                var index = $('.collect-tab-head').children().index(this);
                $('.collect-tab-body').children().eq(index).show().siblings().hide();
            });
         });

         $(document).on('click', '.collect-tab-body .sort a', function(){
            if (!$(this).hasClass('active')) {
                $(this).addClass('active').siblings('a').removeClass('active')
            }
         });
      });
   </script>
</body>
</html>