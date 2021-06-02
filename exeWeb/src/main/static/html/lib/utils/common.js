


/**

 * 异步提交返回XML
 * 
 * @param url
 *            提交路径
 * @param data
 *            参数
 * @param callback
 *            回调函数
 */
function PostDataXml(url, data, callback) {
	$.ajax({
		async : false,
		"url" : url,
		"global" : false,
		"type" : "POST",
		"dataType" : "xml",
		"timeout" : "60000",
		"headers":{
			"X-Requested-With":"XMLHttpRequest"
		},
		"data" : data,
		"success" : callback,
		"error" : function(err, msg, code) {
			return false;
		}
	});
}
function PostDataXml(url, data, callback,async) {
	$.ajax({
		async : false,
		"url" : url,
		"global" : false,
		"type" : "POST",
		"dataType" : "xml",
		"timeout" : "60000",
		"headers":{
			"X-Requested-With":"XMLHttpRequest"
		},
		"data" : data,
		"success" : callback,
		"error" : function(err, msg, code) {
			return false;
		}
	});
}



function PostDataJson(url, data, callback,async){
	data.format = "json";
	if(typeof(v)!= "undefined"){
		data.v=v;
	}
	if(!async){
		async = false;
	}
	$.ajax({
		async : async,
		"url" : url,
		"global" : false,
		"type" : "POST",
//		"contentType": "application/json; charset=utf-8",
		"dataType" : "json",
		"timeout" : "60000",
		"headers":{
			"X-Requested-With":"XMLHttpRequest"
		},
		"data" : data,
		"traditional":true,
		"success" : callback,
		"error" : function(err, msg, code) {
			return false;
		}
	});
}
function PostDataJsonObj1(url, data, callback,async){
	if(!async){
		async = false;
	}
	$.ajax({
		async : async,
		"url" : url,
		"global" : false,
		"type" : "POST",
		"contentType": "application/json; charset=utf-8",
		"dataType" : "json",
		"timeout" : "60000",
		"data" : data,
		"traditional":true,
		"success" : callback,
		"error" : function(err, msg, code) {
			return false;
		}
	});
}
function PostDataJsonObj(url, data, callback,async){
	var v1=null;
	if(typeof(v)!= "undefined"){
		v1=v;
	}
	if(!async){
		async = false;
	}
	$.ajax({
		async : async,
		"url" : url,
		"global" : false,
		"type" : "POST",
		"contentType": "application/json; charset=utf-8",
		"dataType" : "json",
		"timeout" : "60000",
		"headers":{
			"X-Requested-With":"XMLHttpRequest",
			"format":"json",
			"v":v1
		},
		"data" : data,
		"traditional":true,
		"success" : callback,
		"error" : function(err, msg, code) {
			return false;
		}
	});
}


//添加收藏夹
function add_favorite(a, title, url) {
	url = url || a.href;
	title = title || a.title;
	try{ // IE
		window.external.addFavorite(url, title);
	} catch(e) {
		try{ // Firefox
			window.sidebar.addPanel(title, url, "");
		} catch(e) {
			if (/Opera/.test(window.navigator.userAgent)) { // Opera
				a.rel = "sidebar";
				a.href = url;
				return true;
			}
			alert('加入收藏失败，请使用 Ctrl+D 进行添加');
		}
	}
	return false;
}
function exceptionOpenRedirect() {
	if (window === window.top)
		location.href = "http://www.kkdian.com";
}

function topOpenURL() {
	if (window != window.top) {
		top.location.href = location.href;
	}
}

/**
 * 验证权限异步请求
 * @param url
 */
function authUser(url,callback){
	PostDataJson(url,{"dialog":true},callback);
}

function alertMsg(msg){
	var errOjb=$("#mm_error");
	if(errOjb.length<=0){
		errOjb=$('<div class="mm_error" id="mm_error" style="display:none;">'+msg+'</div>');
		$("body").append(errOjb);
	}
	errOjb.text(msg);
	resize();
	errOjb.show();
	function resize(){
		errOjb.css({
			position:'fixed',
			left: ($(window).width() - errOjb.outerWidth())/2,
			top: "40%"
			});
	}
	$(window).resize(function(){
		resize();
	}); 
	setTimeout(function(){
		errOjb.fadeOut(50);
	}, 2000);
}


/**
 * 弹出选择框
 * @param msg
 * @param con
 * @param cal
 */
function alertConfirm(msg,con,cal){
	var conifHtml = "";
	var conifObj = $("#confirmAlertDiv");
	if(conifObj.length<=0){
		conifHtml+="<div id=\"confirmAlertDiv\">";
		conifHtml+="<div class=\"black\" ></div>";
		conifHtml+="<div class=\"tuihui\" >";
		conifHtml+="<div class=\"yiwen\" id=\"confirmAlertMsg\">"+msg+"</div>";
		conifHtml+="<ul>";
		conifHtml+="<li><a href=\"javascript:void(0);\" id=\"confirm_soure\">确定</a></li>";
		conifHtml+="<li><a href=\"javascript:void(0);\" id=\"confirm_cacel\">取消</a></li>";
		conifHtml+="</ul>";
		conifHtml+="<div class=\"clear\"></div>";
		conifHtml+="</div>";
		conifHtml+="</div>";
		$("body").append($(conifHtml));
	}else{
		$("#confirmAlertMsg").text(msg);
		$(conifObj).show();
	}
	$("#confirm_soure").click(function(){
		con();
	});
	$("#confirm_cacel").click(function(){
		cal();
	});
}
/**
 * 弹出图层
 * @param msgSrc
 */	
function alertPicLayer(msgSrc){
	var picLayer = $("#picLayer");
	var picLayerHtm="";
	if(picLayer.length<=0){
		picLayerHtm+="<div id=\"picLayer\">";
		picLayerHtm+="<div class=\"black\"></div>";
		picLayerHtm+="<div class=\"picture\"><span><img id=\"picLayer_src\" src="+msgSrc+"></span></div>";
		picLayerHtm+="</div>";
		$("body").append($(picLayerHtm));
		$("#picLayer").click(function(){
			$(this).hide();
		});
	}else{
		$(picLayer).show();
		$("#picLayer_src").attr("src",msgSrc);
		
	}
}
	
