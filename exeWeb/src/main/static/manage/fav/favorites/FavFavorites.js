$(function() {
	findById();
});
function findById() {
	if(!id) return;
	PostDataJson(basePath+"center/fav/favorites/findFavFavoritesById.do", {'id':id}, function(data) {
		if(!data || data.code != 200 || !data.v) return;
		var res = data.v;
		var inputs=$("#edit_form").find("input");
		$.each(inputs,function(a,b){
			var key=$(b).attr("id");
			$(this).val(res[key]);
		});
	});
}
//TODO
function bindSelectXXXX(){
	$("#XXXX").click(function(){
		dialog = kkdUi.dialog({
			title:'选择XXXX',
			height : '400px'
		}, basePath+'manage/XXXX/XXXX/SelectXXXX.jsp?callback=SelectXXXXCallBack');
	});
	
}
//TODO
function SelectXXXXCallBack(key,value){
	$("#XXXXId").val(key);
	$("#XXXXName").val(value);
	dialog.close();
}
