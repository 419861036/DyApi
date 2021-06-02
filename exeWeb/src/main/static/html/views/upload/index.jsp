<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URL"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	String userId = request.getParameter("userId")==null?"":request.getParameter("userId");
	String userToken_s = request.getParameter("userToken")==null?"":request.getParameter("userToken");
	String backUrl = request.getParameter("ReturnURL")==null?"http://182.139.242.84:33200/EPG/jsp/yszhibo/en/category_Index.jsp":request.getParameter("ReturnURL");
	String secretKey = "";
	String platform = "";
	String returnInfo = "";
	String returnUrl = "";
	String inFo = request.getParameter("INFO");
	System.out.println("请求路径->" + request.getQueryString());
	if(inFo!=null){
		inFo = URLDecoder.decode(inFo);
		backUrl = URLDecoder.decode(backUrl);
		if(inFo.indexOf("<userId>") >= 0 && inFo.indexOf("</userId>") >= 0){
            userId = inFo.substring(inFo.indexOf("<userId>")+8,inFo.indexOf("</userId>"));
            if(userId.indexOf("\\") != -1){
                userId = userId.replaceAll("\\\\", "\\\\\\\\");
            }
		}
		if(inFo.indexOf("<userToken>") >= 0 && inFo.indexOf("</userToken>") >= 0){
			userToken_s = inFo.substring(inFo.indexOf("<userToken>")+11,inFo.indexOf("</userToken>"));
		}
		if(inFo.indexOf("<back_epg_url>") >= 0 && inFo.indexOf("</back_epg_url>") >= 0){
			returnUrl = inFo.substring(inFo.indexOf("<back_epg_url>")+14,inFo.indexOf("</back_epg_url>"));
		}
		if(inFo.indexOf("<key>") >= 0 && inFo.indexOf("</key>") >= 0){
			secretKey = inFo.substring(inFo.indexOf("<key>")+5,inFo.indexOf("</key>"));
		}
		if(inFo.indexOf("<epgPlatform>") >= 0 && inFo.indexOf("</epgPlatform>") >= 0){
			platform = inFo.substring(inFo.indexOf("<epgPlatform>")+13,inFo.indexOf("</epgPlatform>"));
		}
		returnInfo = inFo + "<optFlag>GAME</optFlag><SPID>20001063</SPID>";
        returnInfo = URLEncoder.encode(returnInfo, "gbk");
	}
	System.out.println("backUrl-->" + backUrl + "---" + returnInfo);
	String originalUserId = getOriginalInfo(secretKey, userId);
	String originalUserToken = getOriginalInfo(secretKey, userToken_s);
	System.out.println("originalUserId->" + originalUserId + ",originalUserToken->" + originalUserToken);
%>
<%!
    public String getOriginalInfo(String key, String Str) {
        if (key == null) {
            return null;
        }

        if (Str == null) {
            return null;
        }

        if (!key.contains(":")) {
            return null;
        }

        String result = "";

        String[] keys = null;
        try {
            keys = key.split(":");

            char[] use = Str.toCharArray();

            for (int i = 0; i < Str.length(); i++) {
                if ((i + 1) % 2 == 1) {
                    use[i] = (char) (use[i] + Integer.parseInt(keys[1]));
                } else {
                    use[i] = (char) (use[i] - Integer.parseInt(keys[1]));
                }
            }

            for (int i = 0; i < use.length; i++) {
                result += use[i];
            }
        } catch (Exception e) {
        }

        String m1 = result.substring(0,
                result.length() - Integer.parseInt(keys[0]));

        String m2 = result.substring(
                result.length() - Integer.parseInt(keys[0]), result.length());

        int s = 0, e = m1.length() - 1;

        char[] us = m1.toCharArray();

        while (s < e) {
            char temp = us[e];
            us[e] = us[s];
            us[s] = temp;

            s++;
            e--;
        }

        m1 = "";
        for (int i = 0; i < us.length; i++)
            m1 += us[i];

        result = m2 + m1;

        return result;
    }
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <title></title>
</head>
<body bgcolor="#000">
<div id="test" style="word-break: break-all;color: yellow;top: 100px;left: 100px;width: 1000px;height: 250px;z-index: 99;position: absolute;"></div>
<div id="test1" style="word-break: break-all;color: yellow;top: 500px;left: 100px;width: 1000px;height: 300px;z-index: 99;position: absolute;"></div>
<script type="text/javascript" src="js/eZoneSC.js"></script>
<script type="text/javascript">
var INTERFACE_IPPORT = "";
var report_resolution = "HD";
var enCodeUserID = "<%=userId%>";
var enCodeUserToken = "<%=userToken_s%>";
var backURL = "<%=returnUrl%>";
var key = "<%=secretKey%>";
var userID = "<%=originalUserId%>";
var userToken = "<%=originalUserToken%>";
var platform = "<%=platform%>";
var returnInfo = "<%=returnInfo%>";
var portalBackUrl = "<%=backUrl%>";
// $("test").innerHTML = enCodeUserID + "----1----" + key;
try{
    var STBType = Authentication.CTCGetConfig("STBType");
    if (userID == null) {
        userID = "default_null_" + STBType;
    }
} catch(e){}

if (userID) {
    Util.setCookie("jjsp_userID", userID);
} else {
    userID = Util.getCookie("jjsp_userID");
}

if (userToken) {
    Util.setCookie("jjsp_userToken", userToken);
} else {
    userToken = Util.getCookie("jjsp_userToken");
}

if (key) {
    Util.setCookie("jjsp_key", key);
} else {
    key = Util.getCookie("jjsp_key");
}

if (platform) {
    Util.setCookie("jjsp_platform", platform);
} else {
    platform = Util.getCookie("jjsp_platform");
}

if (enCodeUserID == null) {
    enCodeUserID = Util.getCookie("jjsp_enCodeUserID");
} else {
    Util.setCookie("jjsp_enCodeUserID", enCodeUserID);
}
if (enCodeUserToken == null) {
    enCodeUserToken = Util.getCookie("jjsp_enCodeUserToken");
} else {
    Util.setCookie("jjsp_enCodeUserToken", enCodeUserToken);
}
if(returnInfo){
    Util.setCookie("INFO", returnInfo);
}else {
    returnInfo= Util.getCookie("INFO");
}

localStorage.setItem("INFO", returnInfo);

window.onload = function () {
    Util.setCookie("INFO", returnInfo);
    Util.setCookie("jjsp_index_jsp_url", location.href);
    Util.setCookie("isActivity", "1");
	InterFace_report("", 0, "", "", "", "", processReportIndex);
  //   InterFace_authentication(processAuthentication);
    window.setTimeout(function () {
        Util.setCookie("INFO", returnInfo);
        window.location.href = "page/index.html?backURL=" + encodeURIComponent(portalBackUrl + (portalBackUrl.indexOf('?') > -1 ? '&' : '?') + "INFO=" + returnInfo);
    },1500);
};

function processReportIndex(data) {
}

function InterFace_authentication(callBack) {
    var param = "userID=" + userID + "&userToken=" + userToken;
    return Util.ajax("GET", INTERFACE_IPPORT + "/AEMI/servies/user/userService/authentication?" + param, callBack);
}

function InterFace_report(code, type, isRecom, playTime, parentCode, sequence, callBack) {
    var param = "";
    param += "defiFlag=" + report_resolution;
    param += "&userID=" + userID;
    if (code !== "") {
        param += "&code=" + code;
    }
    if (type !== "") {
        param += "&type=" + type;
    }
    if (isRecom !== "") {
        param += "&isRecom=" + isRecom;
    }
    if (playTime !== "") {
        param += "&playTime=" + playTime;
    }
    param += "&parentCode=" + parentCode;
    if (sequence !== "") {
        param += "&sequence=" + sequence;
    }
    return Util.ajax("GET", INTERFACE_IPPORT + "/AEMI/servies/report/visitReportInfoService/addVisitInfo?" + param, callBack);
}
</script>
</body>
</html>
