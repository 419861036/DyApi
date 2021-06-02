$(function() {
	init();
}); 

/**
 * 分页
 */
function ajaxFind(page,itemsPerPage){
	init(page,itemsPerPage); 
}
 
/**
 * 初始化
 */
function init(page,itemsPerPage){
	itemsPerPage=10;
	var inputs=$("#search").find("input");
	var data = {
		page:page,
		itemsPerPage:itemsPerPage
	};
	$.each(inputs,function(a,b){
		var key=$(b).attr("id");
		var val=$(b).val();
		data[key]=val;
	});
	
	var url=basePath+"center/fav/collect/findFavCollectPageList.do";
	PostDataJson(url, data, function(res){
		grid(res,$("#contentTable"));
		if(res.v && res.v.collection){ 
			kkdUi.pager(res.v.paginator,$("#pager"));
		}
		kkdUi.allchick("#checkAll", "input[name='subBox']");
	});
	
}
var rowData={};
function grid(res,$e){
	if(!(res.v && res.v.collection&&res.v.collection.length>0)){
		var nodata='<tr><td  colspan="16" style="text-align:center">查无数据</td></tr>';
		$e.html(nodata); 
		return ;
	}
	var rows=res.v.collection;
	rowData=rows;
	var trStr='';
	var i=0;
	for(i;i<rows.length;i++){
		var row=rows[i];
		var edit='<button type="button" class="btn btn-primary btn-xs" onclick="editFavCollect(\''+i+'\');">编辑</button>';
		if(typeof(select)!="undefined"){
			edit='<button type="button" class="btn btn-primary btn-xs" onclick="selectFavCollect(\''+i+'\');" >选择</button>';
		}
		var data={ 
			"no":i+1,
			"ck":'<input type="checkbox" class="checkbox" name="subBox" value="'+row.id+'" />',
			"id":row.id,
"category":row.category,
"charset":row.charset,
"createTime":row.createTime,
"description":row.description,
"favoritesId":row.favoritesId,
"isDelete":row.isDelete,
"lastModifyTime":row.lastModifyTime,
"logoUrl":row.logoUrl,
"remark":row.remark,
"title":row.title,
"type":row.type,
"url":row.url,
"userId":row.userId,

			"edit":edit	
		};
		var ss=kkdUi.addRow(data);
		trStr+=ss;
	}
	$e.html(trStr);
}

/**
 * 选择页时使用
 */
function selectFavCollect(rownum){
	if(callback){
		var api = frameElement.api, W = api.opener;
		var key=rowData[rownum].id;
		//TODO
		//var data={"key":key,"value":value};
		var value=rowData[rownum].name;
		W.eval(callback+"('"+key+"','"+value+"')");
	}
}
/**
 * edit
 */
function editFavCollect(rownum){
	var id="";
	if(typeof(rowData[rownum])!="undefined"){
		id=rowData[rownum].id;
	}
	var dialog = kkdUi.dialog({
		title:'编辑',
		height : '400px'
	}, basePath+'manage/fav/collect/opFavCollect.jsp?id=' + id, function() {
		return AddOrUpdate(dialog, "更新", id);
	});
}

/**
 * del
 */
function deleteFavCollect() {
	if ($("input[name='subBox']:checked").length == 0) {
		kkdUi.error("请选择要删除的项");
		return;
	}
	kkdUi.confirm("确认删除吗", function() {
		var array = $("input[name='subBox']:checked");
		var ids = new Array();
		for ( var i = 0; i < array.length; i++) {
			ids.push($(array[i]).val());
		} 
		var url=basePath+"center/fav/collect/delFavCollect.do";
		PostDataJson(url, {
			ids : ids
		}, function(res){
			kkdUi.alertMsg(res);
		});
	}, true);
}

/**
 * 添加/更新功能信息
 * @param res 对象
 * @param title 题目
 * @param id 更新时，目标编号
 */
function AddOrUpdate(dialog,title, id) {
	var $res = $(dialog.content.document);
	var inputs=$res.find("#edit_form").find("input");
	var data = {};
	$.each(inputs,function(a,b){
		var key=$(b).attr("id");
		var val=$(b).val();
		data[key]=val;
	});
	var url=id?basePath +"center/fav/collect/updateFavCollect.do":basePath +"center/fav/collect/addFavCollect.do";
	
	PostDataJsonObj(url, JSON.stringify(data), function(res){
		if(res && res.code == 200) {
			kkdUi.alert("操作成功！",{autoClose:true});
			setTimeout(function() {
				ajaxFind(1);
			}, 1000);	
			dialog.close();
		}else{
			kkdUi.error(res.msg?res.msg:'操作失败');
		}
	});
	return false;
}

