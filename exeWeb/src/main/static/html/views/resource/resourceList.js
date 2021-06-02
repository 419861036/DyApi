/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function(t) {
    var e = layui.$
      ,$=layui.$
      , i = layui.table
      , n = layui.form;
    i.render({
        elem: "#LAY-app-content-list",
        url: layui.setter.api + "/resource/page",
        parseData: function(res){
        	if(res.code==200){
        		return {
            		"code": res.code, //解析接口状态
            		"msg": res.msg, //解析提示文本
            		"count": res.v.totalNum, //解析数据长度
            		"data": res.v.collection //解析数据列表
            	}
        	}else{
        		return {
            		"code": res.code, //解析接口状态
            		"msg": res.msg, //解析提示文本
            		"count": res.v.totalNum, //解析数据长度
            		"data": res.msg
            	}
        	}
        	
        },
        response: {
            statusCode: 200 //规定成功的状态码，默认：0
        },
        cols: [[{
            type: "checkbox",
            fixed: "left"
        }, {
            field: "id",
            width: 100,
            title: "资源ID",
            sort: !0
        }, {
            field: "type",
            width: 100,
            title: "类型",
            sort: !0
        }, {
            field: "path",
            title: "资源路径",
            minWidth: 100
        }, {
        	field: "resourceName",
        	title: "资源名称",
        	minWidth: 100
        }, {
            field: "createTime",
            title: "创建时间",
            sort: !0
        }, {
            field: "upTime",
            title: "修改时间",
            sort: !0
        }, {
            title: "操作",
            minWidth: 250,
            align: "center",
            fixed: "right",
            toolbar: "#table-content-list"
        }]],
        page: !0,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: "对不起，加载出现异常！"
    }),
    i.on("tool(LAY-app-content-list)", function(t) {
        var e = t.data;
        if("del" === t.event){
        	layer.confirm("确定删除此资源？", function(e) {
                layui.$.ajax({
		        		async : true,
		        		"url" : "/api/resource/del?id="+t.data.id,
		        		"global" : false,
		        		"type" : "POST",
		        		"contentType": "application/json; charset=utf-8",
		        		"dataType" : "json",
		        		"timeout" : "60000",
		        		"traditional":true,
		        		"success" : function(a,b,c){
		        			t.del(),
            			  layer.close(e)
		        		},
		        		"error" : function(err, msg, code) {
		        			return false;
		        		}
		        	});
            }) 
        }else if("edit" === t.event){
        	layer.open({
                type: 2,
                title: "编辑资源",
                content: "/html/views/resource/add.html?id=" + e.id,
                maxmin: !0,
                area: ["550px", "550px"],
                btn: ["确定", "取消"],
                yes: function(e, i) {
                    var l = window["layui-layer-iframe" + e]
                      , a = i.find("iframe").contents().find("#layuiadmin-app-form-edit");
                    l.layui.form.on("submit(layuiadmin-app-form-edit)", function(i) {
                    	var field=i.field;
                    	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引  
                		$.ajax({
                			async : true,
                			"url" : "/api/resource/mod",
                			"global" : false,
                			"type" : "POST",
                			"contentType": "application/json; charset=utf-8",
                			"dataType" : "json",
                			"timeout" : "60000",
                			"data" : JSON.stringify(field),
                			"traditional":true,
                			"success" : function(a,b,c){
                				layui.table.reload('LAY-app-content-list'); //重载表格
                	      		layer.close(index); //再执行关闭 
                			},
                			"error" : function(err, msg, code) {
                				return false;
                			}
                		});
                    }),
                    a.trigger("click")
                }
            })
        }else if("editContent" === t.event){
        	if(top.window===window){
            	window.open('/html/views/resource/modContent.html?id='+e.id+"&name="+e.resourceName);
            }else{
	        	top.layui.admin.events.newTab('/html/views/resource/modContent.html?id='+e.id,e.resourceName)
            }
        }else if("api" === t.event){
        	if(top.window===window){
            	window.open('/html/views/resource/api.html?id='+e.id);
            }else{
	        	top.layui.admin.events.newTab('/html/views/resource/api.html?id='+e.id,"API:"+e.resourceName)
            }
        }
    }),
    i.render({
        elem: "#LAY-app-content-tags",
        url: layui.setter.base + "json/content/tags.js",
        cols: [[{
            type: "numbers",
            fixed: "left"
        }, {
            field: "id",
            width: 100,
            title: "ID",
            sort: !0
        }, {
            field: "tags",
            title: "分类名",
            minWidth: 100
        }, {
            title: "操作",
            width: 150,
            align: "center",
            fixed: "right",
            toolbar: "#layuiadmin-app-cont-tagsbar"
        }]],
        text: "对不起，加载出现异常！"
    }),
    i.on("tool(LAY-app-content-tags)", function(t) {
        var i = t.data;
        if ("del" === t.event)
            layer.confirm("确定删除此分类？", function(e) {
                t.del(),
                layer.close(e)
            });
        else if ("edit" === t.event) {
            e(t.tr);
            layer.open({
                type: 2,
                title: "编辑分类",
                content: "../../../views/app/content/tagsform.html?id=" + i.id,
                area: ["450px", "200px"],
                btn: ["确定", "取消"],
                yes: function(e, i) {
                    var n = i.find("iframe").contents().find("#layuiadmin-app-form-tags")
                      , l = n.find('input[name="tags"]').val();
                    l.replace(/\s/g, "") && (t.update({
                        tags: l
                    }),
                    layer.close(e))
                },
                success: function(t, e) {
                    var n = t.find("iframe").contents().find("#layuiadmin-app-form-tags").click();
                    n.find('input[name="tags"]').val(i.tags)
                }
            })
        }
    }),
   
    t("resourceList", {})
});
