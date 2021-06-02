/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function(t) {
    var e = layui.$
      ,$=layui.$
      , i = layui.table
      , n = layui.form;
    i.render({
        elem: "#LAY-app-content-list",
        url: layui.setter.api + "/scj/sc/my",
        parseData: function(res){
        	if(res.code==200){
              if(res.v){
						return {
            		"code": res.code, //解析接口状态
            		"msg": res.msg, //解析提示文本
            		"count": res.v.totalNum, //解析数据长度
            		"data": res.v.collection //解析数据列表
            	  }
              }
        		return {
            		"code": res.code, //解析接口状态
            		"msg": res.msg, //解析提示文本
            		"count": 0, //解析数据长度
            		"data": [{}] //解析数据列表
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
            title: "收藏夹ID",
            sort: !0
        }, {
            field: "name",
            width: 100,
            title: "收藏夹名称",
            sort: !0
        },
         {
            field: "private",
            width: 100,
            title: "是否公开",
            templet : function (d){if(d.private=='on'){return '私有'}else{return '公开'}},
            sort: !0
        }, {
            field: "remark",
            title: "描述",
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
        var data = t.data;
        if("open" === t.event){
            window.open(data.link);
//             if(top.window===window){
//             	window.open(data.link);
//             }else{
// 	        	top.layui.admin.events.newTab(data.link,data.name)
//             }
         }else if("del" === t.event){
        	layer.confirm("确定删除此收藏夹？", function(e) {
					  layui.$.ajax({
		        		async : true,
		        		"url" : "/api/scj/sc/del?id="+data.id,
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
                title: "编辑收藏夹",
                content: "/html/views/scj/add.html?id=" + data.id,
                maxmin: !0,
                area: ["550px", "550px"],
                btn: ["确定", "取消"],
                yes: function(e, i) {
                    var l = window["layui-layer-iframe" + e]
                      , a = i.find("iframe").contents().find("#layuiadmin-app-form-edit");
//                     var x=l.layui.$("#private").is(":checked")
//                     debugger
                    l.layui.form.on("submit(layuiadmin-app-form-edit)", function(i) {
                    	var field=i.field;
                    	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引  
                		$.ajax({
                			async : true,
                			"url" : "/api/scj/sc/mod",
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
        }
    }),
    t("myList", {})
});
			
			
