/**
 * js 工具类
 */
var kkdUtil={};

/**
 * 判断是否是Double
 */
kkdUtil.isDouble = function(num, symbol){
	if(kkdUtil.isEmpty(num)) return;
	if(symbol)
		return /^[-\+]?\d+(\.\d+)?$/.test(num);
	else
		return /^\d+(\.\d+)?$/.test(num);
};

/**
 * 验证是否英文数字组合
 */
kkdUtil.isNumAndEn = function(data){
	var reg = /^[a-zA-Z0-9]+$/;
	var regStr = /^[a-zA-Z]+$/;
	var regIntger =  /^[0-9]+$/;
	var isnum = false;
	var isstr = false;
	if(!reg.test(data)){
		return false;
	}
	for(var i=0;i<data.length;i++){
			if(!isnum){
				isnum = regIntger.test(data.charAt(i))?true:false;
			}
			if(!isstr){
				isstr =regStr.test(data.charAt(i)) ?true:false;
			}
	}
	return isnum && isstr;
	
};
kkdUtil.isNumOrEn = function(data){
	var reg = /^[a-zA-Z0-9]+$/;
	var regStr = /^[a-zA-Z]+$/;
	var regIntger =  /^[0-9]+$/;
	var isnum = false;
	var isstr = false;
	if(!reg.test(data)){
		return false;
	}
	for(var i=0;i<data.length;i++){
			if(!isnum){
				isnum = regIntger.test(data.charAt(i))?true:false;
			}
			if(!isstr){
				isstr =regStr.test(data.charAt(i)) ?true:false;
			}
	}
	return isnum ||  isstr;
};


/**
 * 身份证验证
 */
kkdUtil.isIdCard = function(idNumber){
	 var factorArr = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1);
	    var varArray = new Array();
	    var lngProduct = 0;
	    var intCheckDigit;
	    

	    if ((idNumber.length != 15) && (idNumber.length != 18))
	    {
	        return false;
	    }   
	    for(var i=0;i<idNumber.length;i++)
	    {
	        varArray[i] = idNumber.charAt(i);
	        if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17))
	        {
	            return false;
	        }
	        else if (i < 17)
	        {
	            varArray[i] = varArray[i]*factorArr[i];
	        }
	    }
	    if (idNumber.length == 18)
	    {
	        var date8 = idNumber.substring(6,14);
	        if (kkdUtil.isDate(date8) == false)
	        {
	            return false;
	        }       
	        for(i=0;i<17;i++)
	        {
	            lngProduct = lngProduct + varArray[i];
	        }       
	        intCheckDigit = 12 - lngProduct % 11;
	        switch (intCheckDigit)
	        {
	            case 10:
	                intCheckDigit = 'X';
	                break;
	            case 11:
	                intCheckDigit = 0;
	                break;
	            case 12:
	                intCheckDigit = 1;
	                break;
	        }       
	        if (varArray[17].toUpperCase() != intCheckDigit)
	        {
	            return false;
	        }
	    }
	    else
	    {      
	        var date6 = idNumber.substring(6,12);
	        if (isDate(date6) == false)
	        {
	            return false;
	        }
	    }
	    return true;
};

/**
 * 验证邮箱
 */
kkdUtil.isEmail = function(data){
	// var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	 var myreg  = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
	 if(myreg.test(data))
	    {
	        return true;
	    }
	    return false;
};


/**
 * 传入字符是否为空
 */
kkdUtil.isEmpty = function (data){
//	if(data == null || !data || typeof data == undefined || data == '')
//    {
//        return true;
//    }
	if(data)
    {
        return false;
    }
    return true;
};

/**
 * 验证网址
 */
kkdUtil.isUrl = function (data){
	  var strRegex = "^((https|http|ftp|rtsp|mms)://)?[a-z0-9A-Z]{3}\.[a-z0-9A-Z][a-z0-9A-Z]{0,61}?[a-z0-9A-Z]\.com|net|cn|cc (:s[0-9]{1-4})?/$";
      var re = new RegExp(strRegex);
      if (re.test(data)) {
         // alert("成功");
          return true;
      } else {
          //alert("失败");
           return false;
      }
};
/**
 * 验证日期
 */
kkdUtil.isDate = function(data){
       var reg1 = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;
       //var reg = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;
     var reg2 = /^((((1[6-9]|[2-9]\d)\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;
       if(reg1.test(data) || reg2.test(data) )
    {
        return true;
    }
    return false;  
};

/**
 * 是否为一个整数
 */
kkdUtil.isInteger = function(data){
	 if(/^-?\d+$/.test(data))
	    {
	        return true;
	    }
	    return false;
};
/**
 * 是否为一个浮点数
 */
kkdUtil.isFloat = function (data)
{
    if(/^(-?\d+)(\.\d+)?$/.test(data)){
        return true;
    }
    return false;
};
/**
 * 输入长度判断，最大限制字符
 */
kkdUtil.isMaxLen = function (data,max){
	var length = data.length;
	if(length > max){
		return false;
	}
	return true;
};
/**
 * 输入长度判断，最少限制字符
 */
kkdUtil.isMinLen = function (data,min){
	var length = data.length;
	if(length < min){
		return false;
	}
	return true;
};

/**
 * 输入长度判断，介于两个边界之间
 */
kkdUtil.isBetweenLen = function(data,min,max){
	var length = data.length;
	if(length >= min && length<=max){
		return true;
	}
	return false;
};

/**
 * 验证邮编
 */
kkdUtil.isPost = function (data)
{
    if(/^\d{6}$/.test(data))
    {
        return true;
    }
    return false;
};
/**
*是否是手机号码
*str:要检测的字符串
*/
kkdUtil.isMobile = function (data)
{
    if(/^1[358]\d{9}$/.test(data))
      {
          return true;
      }
    return false;
};
/**
*是否是电话号码
*str:要检测的字符串
*电话号码必须有区号,可以有分机号
*/
kkdUtil.isPhone = function (data)
{
    if(/^(0[1-9]\d{1,2}-)\d{7,8}(-\d{1,8})?/.test(data))
    {
        return true;
    }
    return false;
};
/**
*是否是合法的QQ号码
*str:要检测的字符串
*/
kkdUtil.isQQ = function (str){
    if(/^\d{5,11}$/.test(str))
    {
        return true;
    }
    return false;
};
/**
*是否是合法的IP
*str:要检测的字符串
*/
kkdUtil.isIP = function (str){
    var reg = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
    if(reg.test(str))
    {
        return true;
    }
    return false;
};

/**
*是否是图片格式文件
*str:要测试的文件名
*/
kkdUtil.isImg = function (str)
{
    var objReg = new RegExp("[.]+(jpg|jpeg|swf|gif)$", "gi");
    if(objReg.test(str))
    {
        return true;
    }
    return false;
};
/**
*是否是中文
*str:要检测的字符串
*/
kkdUtil.isChinese = function (str)
{
   var str =  str.replace(/(^\s*)|(\s*$)/g,'');
   if (!(/^[\u4E00-\uFA29]*$/.test(str)
           && (!/^[\uE7C7-\uE7F3]*$/.test(str))))
   {
      return false;
   }
   return true;
};
/**
*忽略大小写比较两个字符串是否相等
*str1:要比较的字符串1
*str2:要比较的字符串2
*/
kkdUtil.isEqualsIgnoreCase =function (str1, str2)
{
    if(str1.toUpperCase() == str2.toUpperCase())
    {
        return true;
    }
    return false;
};



/**
*比较两个字符串是否相等
*str1:要比较的字符串1
*str2:要比较的字符串2
*/
kkdUtil.equals = function (str1, str2)
{
    if(str1 == str2)
    {
        return true;
    }
    return false;
};
/**
 * 将输入的字符转换为整数返回
 */
kkdUtil.parseInteger = function(data){    
	var max =  4294967296-1;
    if(typeof data == 'number'){  
        return data;  
    }else if(typeof data == 'string'){  
        var ret = parseInt(data);  
          
        if(isNaN(ret) || ret >max){  
            return 0;  
        }  
          
        return ret;  
    }else{  
        return 0;  
    }   
};  
/**
 * 将传入值转换为小数
 */
kkdUtil.parseFloat = function(data){  
    if(typeof data == 'number'){  
        return data ; 
    }else if(typeof data == 'string'){  
        var ret = parseFloat(data);          
        if(isNaN(ret) || !isFinite(ret)){  
            return 0;  
        }       
        return ret;  
    }else{  
        return 0;  
    }   
}; 




/**
 * trim 去掉首尾空格
 */
kkdUtil.trim = function(data){
	 return data.replace(/(^\s*)|(\s*$)/g, ''); 
};
/**
*去掉字符串前的空格
*str:将要除去空格的字符串
*/

kkdUtil.ltrim = function (data)
{
   return data.replace(/^\s*/g,'');
};

/**
*去掉字符串后的空格
*str:将要除去空格的字符串
*/
kkdUtil.rtrim = function rtrim(data)
{
   return data.replace(/\s*$/,'');
};
/**
* 截取字符串
*/
kkdUtil.substring = function (data,index,len){
	var length = data.length ;
	if(kkdUtil.isEmpty(data)){
		return false;
	}
	if(index > length || index <0){
		index = 0;
	}
	if(len >length || len<0){
		len = length;
	}
	return data.substring(index,len);
};

/**
* data字符串，len截取长度，replaceContent替换为的内容
*/
kkdUtil.subrepalce = function(data,len,replaceContent){
	var effectiveStr = kkdUtil.substring(data,0,len);
	
	if(effectiveStr==false){
		return false;
	}
	if(data.length-len>0){
		return effectiveStr+replaceContent;
	}else{
		return effectiveStr;
	}
	
};
/************************************************************** 
将传入值转换成小数,传入值可以是以逗号(,)分隔的数字，此方法将会过滤掉(,) 
**************************************************************/  
kkdUtil.parseDotFloat = function(data){  
    if(typeof data == 'number'){  
        return data;  
    }else if(typeof data == 'string'){           
    	data = data.replace(/[^\d|.]/g , '');  
    	
    	data = parseFloat(data);  
          
    	if(isNaN(data) || !isFinite(data)){  
            return 0 ; 
        }  
        return data;  
    }else{  
        return 0;  
    }     
} ;

/************************************************************** 
字符串传换成date类型 
 
@str {string}字符串格式表示的日期，格式为：yyyy-mm-dd
@return {Date}由str转换得到的Date对象 
**************************************************************/  
kkdUtil.str2date = function(data){
	var   re1   =   /^(\d{4})\S(\d{1,2})\S(\d{1,2})\s(\d{1,2})\S(\d{1,2})\S(\d{1,2})$/; 
    var   re2   =   /^(\d{4})\S(\d{1,2})\S(\d{1,2})$/;     
    var   dt;     
    if(re2.test(data)){     
        dt   =   new   Date(RegExp.$1,RegExp.$2   -   1,RegExp.$3);     
    }else if(re1.test(data)){
    	data = data.replace(/-/g,"/");
    	dt = new Date(data);
    }   
    return dt;  
} ;

/**
 * 验证字符串是否为日期格式:验证年月日 时分秒
 */
kkdUtil.fullDateStrVeify = function(data){
	var   re   =   /^(\d{4})\S(\d{1,2})\S(\d{1,2})\s(\d{1,2})\S(\d{1,2})\S(\d{1,2})$/; 
	if(re.test(data)){
		return true;
	}
	return false;
};



/************************************************************** 
计算2个日期之间的天数 
@day1 {Date}起始日期 
@day2 {Date}结束日期 
@return day2 - day1的天数差 
**************************************************************/   
kkdUtil.dayMinus = function(day1, day2){  
    var days = Math.floor((day2-day1)/(1000 * 60 * 60 * 24));  
    return days;  
};

/**
 * 控制台打印
 */
kkdUtil.log = function(data){
	console.warn(data);
};
kkdUtil.formatLong=function(l,fmt){
	if(l){
		
		return kkdUtil.format(new Date(l),fmt);  
	}
	return "";
}
/**       
 * 将 Date 转化为指定格式的String       
 * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q) 可以用 1-2 个占位符       
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)       
 * eg:       
 * format(date,"yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423       
 * format(date,"yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04       
 * format(date,"yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04       
 * format(date,"yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04       
 * format(date,"yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18       
 */   
kkdUtil.format=function(date,fmt){
	  var o = {           
			    "M+" : date.getMonth()+1, //月份           
			    "d+" : date.getDate(), //日           
			    "h+" : date.getHours()%12 == 0 ? 12 : date.getHours()%12, //小时           
			    "H+" : date.getHours(), //小时           
			    "m+" : date.getMinutes(), //分           
			    "s+" : date.getSeconds(), //秒           
			    "q+" : Math.floor((date.getMonth()+3)/3), //季度           
			    "S" : date.getMilliseconds() //毫秒           
			    };  
	  var week = {           
			    "0" : "\u65e5",           
			    "1" : "\u4e00",           
			    "2" : "\u4e8c",           
			    "3" : "\u4e09",           
			    "4" : "\u56db",           
			    "5" : "\u4e94",           
			    "6" : "\u516d"          
			    };  
	  if(/(y+)/.test(fmt)){           
	        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));           
	    }           
	    if(/(E+)/.test(fmt)){           
	        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[date.getDay()+""]);           
	    }           
	    for(var k in o){           
	        if(new RegExp("("+ k +")").test(fmt)){           
	            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));           
	        }           
	    }           
	    return fmt;   
};

/**
 * 毫秒数转成时间
 * 格式为：mouth-day
 * 
 */
kkdUtil.StringToDateMD = function(data){
	if(!data){
		return;
	}
	var time = new Date();
	time.setTime(data);
	var year = time.getFullYear();
	var mouth = time.getMonth()+1;
	var day = time.getDate();
	var hour = time.getHours();
	var min = time.getMinutes();
	var sec = time.getSeconds();
	if(mouth < 10){
		mouth = "0" + mouth;
	}
	if(day < 10){
		day = "0" + day;
	}
	if(hour<10){
		hour = "0"+hour;
	}
	if(min<10){
		min = "0"+min;
	}
	if(sec<10){
		sec = "0"+sec;
	}
	return year+"-"+mouth + "-" + day+" "+hour+":"+min+":"+sec;
};
/**
 * 计算日期跨度
 */
kkdUtil.dateSpan = function(date,num,format){
	var time =date;
	if(format=="month"){
		time.setMonth(time.getMonth()+num);
	}else if(format=="day"){
		time.setDate(time.getDate()+num);
	}else if(format=="year"){
		time.setFullYear(time.getFullYear()+num);
	}else if(format=="hour"){
		time.setHours(time.getHours()+num);
	}else if(format=="week"){
		num = 24*7*num;
		kkdUtil.log(num);
		time.setHours(time.getHours()+num);
	}
	kkdUtil.log(time);
	kkdUtil.log(new Date());
};

kkdUtil.dateNature = function(date,num,format){
	var time = new Date();
	if(format=="month"){
		time.setDate(1);
		time.setHours(0, 0, 0, 0);
		time.setMonth(time.getMonth()+num+1);
	}else if(format=="day"){
		time.setHours(0, 0, 0, 0);
		time.setDate(time.getDate()+num+1);
	}else if(format=="year"){
		time.setMonth(0);
		time.setDate(1);
		time.setHours(0, 0, 0, 0);
		time.setFullYear(time.getFullYear()+num+1);
	}else if(format=="hour"){
		time.setHours(time.getHours(), 0, 0, 0);
		time.setHours(time.getHours()+num+1);
	}else if(format=="week"){
		num = 24*7*num;
		var hourjian = (time.getDay()-1)*24;
		time.setHours(time.getHours()-hourjian,0,0,0);
		time.setHours(num, 0, 0, 0);
	}
	kkdUtil.log(time);
	kkdUtil.log(new Date());
};

/**
 * 解析格式化字符串为日期对象
 */
kkdUtil.parseDate =  function parseDate(str){   
	  if(typeof str == 'string'){   
	    var results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) *$/);   
	    if(results && results.length>3)   
	      return new Date(parseInt(results[1]),parseInt(results[2]) -1,parseInt(results[3]));    
	    results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2}) *$/);   
	    if(results && results.length>6)   
	      return new Date(parseInt(results[1]),parseInt(results[2]) -1,parseInt(results[3]),parseInt(results[4]),parseInt(results[5]),parseInt(results[6]));    
	    results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2})\.(\d{1,9}) *$/);   
	    if(results && results.length>7)   
	      return new Date(parseInt(results[1]),parseInt(results[2]) -1,parseInt(results[3]),parseInt(results[4]),parseInt(results[5]),parseInt(results[6]),parseInt(results[7]));    
	  }   
	  return null;   
	};   
/**
 * 判断浏览器类型
 */
kkdUtil.browser =function(){
	var userAgent=navigator.userAgent.toUpperCase();
	if ((userAgent.indexOf('MSIE') >= 0) && (userAgent.indexOf('OPR') < 0)||userAgent.indexOf("TRIDENT")>=0){
		return "IE";
	}else if (userAgent.indexOf('FIREFOX') >= 0){
		return "FIREFOX";
	}else if (userAgent.indexOf('OPR') >= 0){
		return "OPERA";
	}else if(userAgent.indexOf('CHROME') >= 0){
		return "CHROME";
	}else if(userAgent.indexOf('SAFARI') >= 0 ){
		return "SAFARI";
	}
};

/**
 * 获取浏览器版本
 */
kkdUtil.browserVerion =function(){
	var userAgent=navigator.userAgent.toUpperCase();
	var regStr_ie = /MSIE [\d.]+;/gi ;
	var regStr_ff = /FIREFOX\/[\d.]+/gi;
	var regStr_chrome = /CHROME\/[\d.]+/gi ;
	var regStr_saf = /SAFARI\/[\d.]+/gi ;
	var regStr_opera = /OPR\/[\d.]+/gi ;
	if ((userAgent.indexOf('MSIE') >= 0) && (userAgent.indexOf('OPR') < 0)||userAgent.indexOf("TRIDENT")>=0){
		return (userAgent.match(regStr_ie)+"").replace(/[^0-9.]/ig,"");
	}else if (userAgent.indexOf('FIREFOX') >= 0){
		return (userAgent.match(regStr_ff)+"").replace(/[^0-9.]/ig,"");
	}else if (userAgent.indexOf('OPR') >= 0){
		return (userAgent.match(regStr_opera)+"").replace(/[^0-9.]/ig,"");
	}else if(userAgent.indexOf('CHROME') >= 0){
		return (userAgent.match(regStr_chrome)+"").replace(/[^0-9.]/ig,"");
	}else if(userAgent.indexOf('SAFARI') >= 0 ){
		return (userAgent.match(regStr_saf)+"").replace(/[^0-9.]/ig,"");
	}
};
/**
 * ajax请求
 */

kkdUtil.ajax=function(opt){
	kkdUtil.ajax.opt={
			type:"POST",
			async:true,
			cache:true,
			cacheP:"_t",
			url:null,
			load:null,
			ok:null,
			err:null
	};
	function createXMLHttpRequest(){
		try {  
	        return new ActiveXObject("Msxml2.XMLHTTP");//IE高版本创建XMLHTTP  
	    }catch(e) {  
	        try {  
	            return new ActiveXObject("Microsoft.XMLHTTP");//IE低版本创建XMLHTTP  
	        }catch(E) {  
	            return new XMLHttpRequest();//兼容非IE浏览器，直接创建XMLHTTP对象  
	        }  
	    }
	}
	function sendAjaxRequest (newOpt){
		var opt=kkdUtil.merge(kkdUtil.ajax.opt, newOpt);
		if(!opt.url){
			throw "url is not null";
		}
		var XMLHttpReq=kkdUtil.ajax.XMLHttpReq;
		//创建XMLHttpRequest对象
		if(!XMLHttpReq){
			XMLHttpReq=createXMLHttpRequest();
			kkdUtil.ajax.XMLHttpReq=XMLHttpReq;
		}
		if(!opt.cache){
			if(opt.url.indexOf("?")>=0){
				opt.url=opt.url+"&"+opt.cacheP+"="+new Date().getTime();
			}else{
				opt.url=opt.url+"?"+opt.cacheP+"="+new Date().getTime();
			}
		}
		XMLHttpReq.open(opt.type, opt.url, opt.async);  //async
	    XMLHttpReq.onreadystatechange = function processResponse(event) {
	    	if(XMLHttpReq.readyState==1||XMLHttpReq.readyState==2||XMLHttpReq.readyState==3){
	    		if(opt.load){
	    			opt.load(event);
	    		}
	    	}else if (XMLHttpReq.readyState == 4) {  
		        if (XMLHttpReq.status == 200) {  
		        	if(opt.ok){
		        		opt.ok(XMLHttpReq.responseText,event);	
		        	}
		        }else{
		        	if(opt.err){
		        		opt.err(event);
		        	}
		        }
		    }else{
		    	if(opt.err){
	        		err(event);
	        	}
		    }
		};
	    XMLHttpReq.send(null);  
	}
	sendAjaxRequest(opt);
};
/**
 * 合并参数
 * 只合并一级
 */
kkdUtil.merge=function(opta,optb){
	var newOpt={};
	var item=null;
	for(item in opta){
		newOpt[item]=opta[item];
		item=null;
	}
	for(item in optb){
		newOpt[item]=optb[item];
		item=null;
	}
	return newOpt;
};

/**
 * 根据id获取对象
 */
kkdUtil.g=function(elementId){
	return document.getElementById(elementId);
};
/**
 * 添加事件
 */
kkdUtil.addEvent=function(eventName,element,fn){
	if (element.attachEvent) {
		element.attachEvent("on"+eventName,fn);
	}else{
		element.addEventListener(eventName,fn,false);
	}
};


