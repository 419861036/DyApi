/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form",'eleTree'], function(t) {
    var e = layui.$
      , i = layui.table
      , n = layui.form;
      var eleTree = layui.eleTree;
    
    function loadMenuData(pid,callback){
        layui.$.ajax({
		        		async : true,
		        		"url" : "/api/menu/page?page=1&limit=10&name=&parentId="+pid,
		        		"global" : false,
		        		"type" : "POST",
		        		"contentType": "application/json; charset=utf-8",
		        		"dataType" : "json",
		        		"timeout" : "60000",
		        		"traditional":true,
		        		"success" : function(a,b,c){
                            var data=[];
                            if(a && a.v&&a.v.collection){
                                var list=a.v.collection;
                                for(var i=0;i<list.length;i++){
                                    var item=list[i];
                                    var menu={
                                        id:item.id,
                                        label:item.name,
                                        href:item.url,
                                        isLeaf:false,
                                        children:[]
                                    };
                                    data.push(menu);
                                }
                            }
		        			callback(data)
		        		},
		        		"error" : function(err, msg, code) {
		        			return false;
		        		}
		        	});
    }
    
    loadMenuTree();
    function loadMenuTree(){
        currentMenu=null;
         var el4=eleTree.render({
                    elem: '#menu',
                    data: [
                        {
                            "id":0,
                            "label": "菜单管理"
                        }
                    ],
                    emptText: "暂无数据",
                    highlightCurrent:true,
                    showCheckbox: false,
                    expandOnClickNode:false,
                    lazy: true,
                    load: function(data,callback) {
                        loadMenuData(data.id,callback);
                    }
                });
        eleTree.on("nodeClick(menu)",function(d) {
            currentMenu=d.data.currentData;
            i.reload('LAY-app-content-list', {
                            where: {
                                parentId:d.data.currentData.id
                            }
                          });
        }); 
    }
    i.render({
        elem: "#LAY-app-content-list",
        url: layui.setter.api + "/menu/page",
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
            title: "菜单ID",
            sort: !0
        },{
            field: "parentId",
            width: 100,
            title: "上级菜单ID",
            sort: !0
        }, {
            field: "name",
            title: "菜单名称",
            minWidth: 100
        }, {
            field: "url",
            title: "菜单地址"
        }, {
            field: "createTime",
            title: "创建时间"
        }, {
            field: "upTime",
            title: "修改时间",
            sort: !0
        }, {
            title: "操作",
            minWidth: 150,
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
        "del" === t.event ? layer.confirm("确定删除此菜单？", function(e) {
				layui.$.ajax({
		        		async : true,
		        		"url" : "/api/menu/del?id="+data.id,
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
            title: "编辑菜单",
            content: "/html/views/menu/add.html?id=" + data.id,
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
		        		"url" : "/api/menu/mod",
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
    t("menu", {})
});
			
