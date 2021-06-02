/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function(t) {
    var e = layui.$
      , i = layui.table
      , n = layui.form;
    i.render({
        elem: "#LAY-app-content-list",
        url: layui.setter.api + "/task/page",
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
            title: "任务ID",
            sort: !0
        }, {
            field: "job_name",
            title: "任务名称",
            minWidth: 100
        }, {
            field: "description",
            title: "任务描述"
        }, {
            field: "cron_expression",
            title: "CORN表达式"
        }, {
            field: "bean_class",
            title: "BeanClass"
         }, {
            field: "job_status",
            title: "任务状态"
         }, {
            field: "job_group",
            title: "任务分组"
        }, {
            field: "createTime",
            title: "创建时间"
        }, {
            field: "upTime",
            title: "修改时间",
            sort: !0
        }, {
            title: "操作",
            minWidth: 260,
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
        if("start" === t.event){
            layer.confirm("确定启动此任务？", function(e) {
				layui.$.ajax({
		        		async : true,
		        		"url" : "/api/task/start?id="+data.id,
		        		"global" : false,
		        		"type" : "POST",
		        		"contentType": "application/json; charset=utf-8",
		        		"dataType" : "json",
		        		"timeout" : "60000",
		        		"traditional":true,
		        		"success" : function(a,b,c){
                          if(a&&a.msg){
                              layer.msg(a.msg);
                              layui.table.reload('LAY-app-content-list'); //重载表格
                          }
            			  layer.close(e)
		        		},
		        		"error" : function(err, msg, code) {
		        			return false;
		        		}
		        	});
            
            })
            return;
        }else if("stop" === t.event){
            layer.confirm("确定停止此任务？", function(e) {
				layui.$.ajax({
		        		async : true,
		        		"url" : "/api/task/stop?id="+data.id,
		        		"global" : false,
		        		"type" : "POST",
		        		"contentType": "application/json; charset=utf-8",
		        		"dataType" : "json",
		        		"timeout" : "60000",
		        		"traditional":true,
		        		"success" : function(a,b,c){
                          if(a&&a.msg){
                              layer.msg(a.msg);
                              layui.table.reload('LAY-app-content-list'); //重载表格
                          }
            			  layer.close(e)
		        		},
		        		"error" : function(err, msg, code) {
		        			return false;
		        		}
		        	});
            
            })
            return;
        }
        "del" === t.event ? layer.confirm("确定删除此任务？", function(e) {
				layui.$.ajax({
		        		async : true,
		        		"url" : "/api/task/del?id="+data.id,
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
            
        }) : "edit" === t.event && layer.open({
            type: 2,
            title: "编辑任务",
            content: "/html/views/task/add.html?id=" + data.id,
            maxmin: !0,
            area: ["550px", "550px"],
            btn: ["确定", "取消"],
            yes: function(e, i) {
                var l = window["layui-layer-iframe" + e]
                  , a = i.find("iframe").contents().find("#layuiadmin-app-form-edit");
                l.layui.form.on("submit(layuiadmin-app-form-edit)", function(i) {
                var field = i.field;
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引  
        			layui.$.ajax({
		        		async : true,
		        		"url" : "/api/task/mod",
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
    }),
    t("taskList", {})
});
			
