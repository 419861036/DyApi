/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function(t) {
    var e = layui.$
      , i = layui.table
      , n = layui.form;
    i.render({
        elem: "#LAY-app-content-list",
        url: layui.setter.api + "/resource/method/page",
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
            title: "索引ID",
            sort: !0
        }, {
            field: "indexId",
            title: "路径",
            minWidth: 100
        }, {
            field: "packageName",
            title: "包"
        }, {
            field: "className",
            title: "类名"
        }, {
            field: "methodName",
            title: "方法名称"
        }, {
            field: "param",
            title: "参数"
        }, {
            field: "returnType",
            title: "返回类型"
       }, {
            field: "lineNum",
            title: "行号"
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
        
    }),
    t("method", {})
});
