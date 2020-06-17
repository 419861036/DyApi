# DyApi

动态api


# 快速定义api框架

1、定义数据源
2、定义入参、出参
	正常时：出参
	异常时：出参
	
3、组装服务
		
4、定义服务
4.1、定义插件
		参数验证插件
		结果转换插件
		用户信息填充插件	
4.2、服务事件	
	获取参数时（非必须）
		groovyScript、js
	
	获取结果事件
		编写sql
		绑定入参
		
	结果获取成功事件（非必须）
		groovyScript、js
		
5、生成swagger 
		接口测试

# 结构定义
与语言无关
````		
module{
	code:
	name:

}
   
api{
	路径、名称、版本,module、param:voId、return:voId
	bizSript:业务组装
}  
vo{
	id,字段名称，字段类型,module
}
service{
	module
	serviceCode,seriveName
	param:voId
	return:voId
	inParam:js
	getData:
	sql:
	afterData:js
}

//共享
plugin{
	module
	pluginCode:
	pluginName:
	event:getData
	script:
	init:
}
````	
