var SP = {};
initEnv();
SP = {
  "userID":"i5207817263@itv",
  "userToken":"0D6B40A5626739E1E1B5D1046B327C34",
   "userIDType":"0",
   "productID":"",
   "platform":"HUAWEI"
  };

var BASE_CODE = "00030000000814520171606274153061";
var ACTIVITY_CODE = "00150000000108820172106305658907";
var INDEX_LEFT_CODE = "00030000000167020171606270551645";
var INDEX_CAROUSE_CODE = "00030000000886220181701082929198";
var INDEX_CP_CODE = "00030000000926220170111110705766";
var INDEX_RECOM_CODE = "00030000000007020170111113505927";
var INDEX_PLAYER_CODE = "00030000000983120170111115020038";
var INDEX_LEFTBAR_CODE = "00030000000057220170111113104777";
var INDEX_RUNTXT_CODE = "00030000000365420171606271354602";
var RANK_CATEGORYCODE = "00030000000621320201606110455405";
var SEARCH_GAME_RECOM_CODE = "00030000000760520172207021411667";
var SEARCH_POSTER_RECOM_CODE = "00030000000745220201008270201444";
var MONTHLY_CODE = "00030000000195820161812175157562";

var GAME_PRODUCT_CODE = "00070000000375120201508134415607";  //00040000000058320172001054525567
var VIDEO_PRODUCT_CODE = "00070000000026420201708281005440"; //00040000000807420161312181903043

var GAME_DIANPIAN_CODE = "00040000000550620171008304113633";
var VIDEO_DIANPIAN_CODE = "00040000000334820171008301312754";//00040000000550620171008304113633
var DAOJU_DIANPIAN_CODE = "00040000000217520181801310839138";
var ANDROID_DIANPIAN_CODE = "00040000000326920181106130433060";

var NEW_ACTIVITY_CODE = "00150000000206220171908290101864";
var ACTIVITY_CODE_NOV = "00150000000551320171211020923613";
var ACTIVITY_CODE_CASHBACK_NOV = "00150000000569920171211025715348";

var ANDROID_GAME_CODE = "00030000000020920170111110009785";

var ANDROID_QUARTER_CODE = "00040000000326920181106130433060";
var VIDEO_QUARTER_CODE = "00040000000304620171608284532748";

var PASS_ORDER_PRODUCT_ID = "a5000000000000000000025";
var PASS_ORDER_CONTENT_ID = "00040000000640320181604092304078";
var PASS_ORDER_PURCHASE_TYPE = "0";

function getPicture(url) {
  if (url && url.indexOf("xgame-picture") != -1) {
    return url;
  } else {
    return url;
  }
}

// var IPPORT = "http://172.0.10.65:8080";
//var IPPORT = "http://172.0.10.50:8080";
//var IPPORT = "http://172.0.10.65:8082";
var IPPORT = "http://127.0.0.1/iptv";
function InterFace_All(path, code, curpage, pagesize, callback) {
    return Util.ajax("GET",
        IPPORT + "/xgame-inter/services/operationService/" + path + "?categoryCode=" + code + "&curpage=" + curpage + "&pagesize=" + pagesize + "&sortOrder=asc",
        callback);
}
function InterFace_getCategoryList(categoryCode, curpage, pagesize, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/categoryList?categoryCode=" +
      categoryCode +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize,
    callBack
  );
}

function InterFace_getCategoryDetail(categoryCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/categoryDetail?categoryCode=" +
      categoryCode,
    callBack
  );
}

function InterFace_getProductDetail(productCode, callBack, param) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/productDetail?productCode=" +
      productCode,
    callBack,
    param
  );
}

function InterFace_getContentList(
  categoryCode,
  curpage,
  pagesize,
  callBack,
  params
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/contentList?categoryCode=" +
      categoryCode +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize +
      "&sortOrder=desc",
    callBack,
    params
  );
}

function InterFace_getContentListOrderBy(
  categoryCode,
  curpage,
  pagesize,
  sortStr,
  callBack,
  params
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/contentListOrderBy?categoryCode=" +
      categoryCode +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize +
      "&sortStr=" +
      sortStr +
      "&sortOrder=desc",
    callBack,
    params
  );
}

function InterFace_getItemList(contentCode, callBack, params) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/itemsList?contentCode=" +
      contentCode +
      "&curpage=1&pagesize=99",
    callBack,
    params
  );
}

function InterFace_getContentListByPcode(
  productCode,
  curpage,
  pagesize,
  callBack
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/contentListByPcode?productCode=" +
      productCode +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize,
    callBack
  );
}

function InterFace_getContentDetail(contentCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/contentDetail?contentCode=" +
      contentCode,
    callBack
  );
}

function InterFace_getplayerCounts(contentCode, callBack, params) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/playerCounts?contentCode=" +
      contentCode,
    callBack,
    params
  );
}

function InterFace_getProductByContentCode(contentCode, callBack, params) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/getProductByContentCode?contentCode=" +
      contentCode,
    callBack,
    params
  );
}

function InterFace_searchList(
  searchType,
  searchKey,
  curpage,
  pagesize,
  callBack
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/searchList?searchType=" +
      searchType +
      "&searchKey=" +
      searchKey +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize,
    callBack
  );
}

function InterFace_searchHistory(
  operationType,
  searchKey,
  curpage,
  pagesize,
  code,
  callBack
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/searchHistory?userID=" +
      SP.userID +
      "&operationType=" +
      operationType +
      "&searchType=1&searchKey=" +
      searchKey +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize +
      "&code=" +
      code,
    callBack
  );
}

function InterFace_pushsearchHistory(
  operationType,
  searchKey,
  curpage,
  pagesize,
  code,
  callBack
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/searchHistory?userID=" +
      SP.userID +
      "&operationType=" +
      operationType +
      "&searchType=1&searchKey=" +
      searchKey +
      "&curpageStr=" +
      curpage +
      "&pagesizeStr=" +
      pagesize +
      "&code=" +
      code,
    callBack
  );
}

function InterFace_serviceAuth(userIDType, productID, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/businessService/serviceAuth?userID=" +
      SP.userID +
      "&userToken=" +
      SP.userToken +
      "&userIDType=" +
      userIDType +
      "&productID=" +
      productID +
      "&platform=" +
      SP.platform,
    callBack
  );
}

function InterFace_serviceAuthByContentCode(userIDType, contentCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/businessService/serviceAuthByContentCode?userID=" +
      SP.userID +
      "&userToken=" +
      SP.userToken +
      "&userIDType=" +
      userIDType +
      "&contentCode=" +
      contentCode +
      "&platform=" +
      SP.platform,
    callBack
  );
}

function InterFace_getOrderUrl(
  productID,
  contentID,
  purchaseType,
  returnUrl,
  callBack
) {
  return (
    IPPORT +
    "/xgame-inter/services/businessService/orderReq?userID=" +
    SP.userID +
    "&userToken=" +
    SP.userToken +
    "&productID=" +
    productID +
    "&contentID=" +
    contentID +
    "&platform=" +
    SP.platform +
    "&purchaseType=" +
    purchaseType +
    "&returnUrl=" +
    returnUrl
  );
  // return IPPORT + "/xgame-inter/services/businessService/serviceOrder?userID=" + SP.userID + "&userToken=" + SP.userToken + "&productID=" + productID + "&contentID=" + contentID + "&platform=" + SP.platform + "&purchaseType=" + purchaseType + "&returnUrl=" + returnUrl;
}

function InterFace_confirmOrder(orderCode, result, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/businessService/confirmOrder?orderCode=" +
      orderCode +
      "&result=" +
      result,
    callBack
  );
}

function InterFace_getOrderAddr(
  productID,
  contentID,
  purchaseType,
  returnUrl,
  callBack
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/businessService/orderAddr?userID=" +
      SP.userID +
      "&userToken=" +
      SP.userToken +
      "&productID=" +
      productID +
      "&contentID=" +
      contentID +
      "&platform=" +
      SP.platform +
      "&purchaseType=" +
      purchaseType +
      "&returnUrl=" +
      returnUrl,
    callBack
  );
}

function InterFace_channelOrderAuth(productCode, contentCode, type, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/channelOrderService/auth?userID=" +
      SP.userID +
      "&productCode=" +
      productCode +
      "&contentCode=" +
      contentCode +
      "&type=" +
      type,
    callBack
  );
}

function InterFace_channelOrderReport(
  orderCode,
  result,
  type,
  failType,
  callBack
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/channelOrderService/confirmOrder?orderCode=" +
      orderCode +
      "&result=" +
      result +
      "&type=" +
      type +
      "&failType=" +
      failType,
    callBack
  );
}

function InterFace_getRefreshUserTokenUrl(returnUrl) {
  return (
    IPPORT +
    "/xgame-inter/services/businessService/refreshUserToken?userID=" +
    SP.userID +
    "&oldUserToken=" +
    SP.userToken +
    "&platform=" +
    SP.platform +
    "&returnUrl=" +
    returnUrl
  );
}

function InterFace_activityDetail(activityCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/activityDetail?activityCode=" +
      activityCode,
    callBack
  );
}

function InterFace_drawChanceForDontWinByProduct(activityCode, userIDType, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/drawChanceForDontWinByProduct?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID +
      "&userToken=" +
      SP.userToken +
      "&userIDType=" +
      userIDType +
      "&platform=" +
      SP.platform,
    callBack
  );
}

function InterFace_getDrawChance(activityCode, userIDType, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/newDrawChance?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID +
      "&userToken=" +
      SP.userToken +
      "&userIDType=" +
      userIDType +
      "&platform=" +
      SP.platform,
    callBack
  );
}

function InterFace_getNewDrawChance(activityCode, userIDType, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/drawChanceForDontWin?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID +
      "&userToken=" +
      SP.userToken +
      "&userIDType=" +
      userIDType +
      "&platform=" +
      SP.platform,
    callBack
  );
}

function InterFace_getPrizeList(activityCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/prizeList?activityCode=" +
      activityCode,
    callBack
  );
}

function InterFace_getWinnerList(activityCode, curpage, pagesize, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/winnerList?activityCode=" +
      activityCode +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize,
    callBack
  );
}

function InterFace_changeWinnerType(activityCode, type, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/changeWinnerType?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID +
      "&type=" +
      type,
    callBack
  );
}

function InterFace_sign(activityCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/sign?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID,
    callBack
  );
}

function InterFace_signCount(activityCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/signCount?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID,
    callBack
  );
}

function InterFace_getPrize(activityCode, levelCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/signAcceptPrize?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID +
      "&levelCode=" +
      levelCode,
    callBack
  );
}

function InterFace_cashback(orderCode, activityCode, phone, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/cashBack?orderCode=" +
      orderCode +
      "&activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID +
      "&phone=" +
      phone,
    callBack
  );
}

function InterFace_draw(activityCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/draw?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID,
    callBack
  );
}

function InterFace_drawForNew(activityCode,userIDType, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/drawForNew?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID +
      "&userToken=" +
      SP.userToken +
      "&userIDType=" +
      userIDType +
      "&platform=" +
      SP.platform,
    callBack
  );
}


function InterFace_getUserPrize(activityCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/activityService/userPrize?activityCode=" +
      activityCode +
      "&userID=" +
      SP.userID,
    callBack
  );
}

function InterFace_saveOrUpdate(phone, qqNum, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/userService/saveOrUpdate?userID=" +
      SP.userID +
      "&phone=" +
      phone +
      "&qq=" +
      qqNum,
    callBack
  );
}

function InterFace_purseDetail(callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/purseService/purseDetail?userID=" +
      SP.userID,
    callBack
  );
}

function InterFace_getRechargeUrl(productID, contentID, returnUrl) {
  return (
    IPPORT +
    "/xgame-inter/services/businessService/serviceOrder?userID=" +
    SP.userID +
    "&userToken=" +
    SP.userToken +
    "&productID=" +
    productID +
    "&purchaseType=1&platform=" +
    SP.platform +
    "&contentID=" +
    contentID +
    "&returnUrl=" +
    returnUrl
  );
}

function InterFace_getOrderList(
  productID,
  startTime,
  endTime,
  curpage,
  pagesize,
  callBack
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/orderList?userID=" +
      SP.userID +
      "&productID=" +
      productID +
      "&startTime=" +
      startTime +
      "&endTime=" +
      endTime +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize,
    callBack
  );
}

function InterFace_getUserCollect(
  operationType,
  curpage,
  pagesize,
  contentCode,
  callBack
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/userCollect?userID=" +
      SP.userID +
      "&operationType=" +
      operationType +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize +
      "&contentCode=" +
      contentCode,
    callBack
  );
}

function InterFace_changeCollect(operationType, contentCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/userCollect?userID=" +
      SP.userID +
      "&operationType=" +
      operationType +
      "&contentCode=" +
      contentCode,
    callBack
  );
}

function InterFace_getConsumeList(
  startTime,
  endTime,
  curpage,
  pagesize,
  callBack,
  param
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/purseService/purseConsumeList?userID=" +
      SP.userID +
      "&startTime=" +
      startTime +
      "&endTime=" +
      endTime +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize,
    callBack,
    param
  );
}

function InterFace_getPlayRecord(
  operationType,
  curpage,
  pagesize,
  contentCode,
  callBack
) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/userPlayRecord?userID=" +
      SP.userID +
      "&operationType=" +
      operationType +
      "&curpage=" +
      curpage +
      "&pagesize=" +
      pagesize +
      "&contentCode=" +
      contentCode,
    callBack
  );
}

function InterFace_changeRecord(operationType, contentCode, callBack) {
  return Util.ajax(
    "GET",
    IPPORT +
      "/xgame-inter/services/operationService/userPlayRecord?userID=" +
      SP.userID +
      "&operationType=" +
      operationType +
      "&contentCode=" +
      contentCode,
    callBack
  );
}

function InterFace_categoryReport(categoryCode, categoryName) {
  var report_info =
    '{"code":"' +
    categoryCode +
    '","name":"' +
    categoryName +
    '","userID":"' +
    SP.userID +
    '","type":"2"}';
  report(report_info);
}

function InterFace_recommendReport(positionCode, positionName) {
  var report_info =
    '{"code":"' +
    positionCode +
    '","name":"' +
    positionName +
    '","userID":"' +
    SP.userID +
    '","type":"1"}';
  report(report_info);
}

function InterFace_recommendReport1(positionCode, positionName) {
  var report_info =
    '{"code":"' +
    positionCode +
    '","name":"' +
    positionName +
    '","userID":"' +
    SP.userID +
    '","type":1}';
  report(report_info);
}

function InterFace_categoryReport1(categoryCode, categoryName) {
  var report_info =
    '{"code":"' +
    categoryCode +
    '","name":"' +
    categoryName +
    '","userID":"' +
    SP.userID +
    '","type":"2"}';
  report(report_info);
}

function InterFace_contentReport1(code, name, productCode, productName,subType) {
  var report_info =
    '{"code":"' +
    code +
    '","name":"' +
    name +
    '","visitType":"' +
    dateFormate() +
    '","productCode":"' +
    productCode +
    '","productName":"' +
    productName +
    '","userID":"' +
    SP.userID +
	'","subType":"' +
    subType +
    '","type":3}';
  report(report_info);
}																 
function InterFace_contentReport(
  contentCode,
  contentName,
  productCode,
  productName,
  subType
) {
  var report_info =
    '{"code":"' +
    contentCode +
    '","name":"' +
    contentName +
    '","productCode":"' +
    productCode +
    '","productName":"' +
    productName +
    '","userID":"' +
    SP.userID +
    '","subType":"' +
    subType +
    '","type":"3"}';
  report(report_info);
}

function bindPursePrice(data) {
  var _tempData = eval("(" + data + ")");
  if (_tempData.resultCode != 0) {
    if ($("balance")) {
      $("balance").innerHTML = "账户余额：0V币";
    }
    return;
  } else {
    var tempStr = "";
    var tempDou = _tempData.remainVbean;
    if (tempDou >= 10000000) {
      tempStr = getW(tempDou, 0);
    } else if (tempDou >= 1000000) {
      tempStr = getW(tempDou, 1);
    } else if (tempDou >= 100000) {
      tempStr = getW(tempDou, 2);
    } else if (tempDou >= 10000) {
      tempStr = getW(tempDou, 3);
    } else {
      tempStr = tempDou;
    }
    if ($("balance")) {
      $("balance").innerHTML = "账户余额：" + tempStr + "V币";
    }
  }
}

function getW(num, f) {
  return Math.floor((parseInt(num) / 10000) * (10 * f)) / (10 * f) + "万";
}

function initEnv() {
  if (Util.getQueryString("userID")) {
    Util.setCookie("userID", Util.getQueryString("userID"));
    var userID = Util.getQueryString("userID");
  } else {
    var userID = Util.getCookie("userID");
  }
  if (Util.getQueryString("userToken")) {
    Util.setCookie("userToken", Util.getQueryString("userToken"));
    var userToken = Util.getQueryString("userToken");
  } else {
    var userToken = Util.getCookie("userToken");
  }
  if (Util.getQueryString("platform")) {
    Util.setCookie("platform", Util.getQueryString("platform"));
    var platform = Util.getQueryString("platform");
  } else {
    var platform = Util.getCookie("platform");
  }
  var stb = "";
  if (typeof Authentication != "undefined") {
    stb = Authentication.CTCGetConfig("STBType");
  } else {
    stb = "EC6108V9U_pub_cqydx";
  }

  SP.userID = userID;
  SP.userToken = userToken;
  SP.platform = platform;
  SP.stb = stb;
}

function checkAndroidSys() {
  return typeof STBAppManager != "undefined";
}

var alertMsg = function(msg) {
  var $con = document.getElementById("dia-alert");
  if (!$con) {
    $con = document.createElement("div");
  } else {
    $con.style.display = "block";
  }
  $con.id = "dia-alert";
  $con.style.position = "absolute";
  $con.style.zIndex = 99;
  $con.style.background = "red";
  $con.style.top = "100px";
  $con.style.left = "100px";
  $con.style.width = "900px";
  $con.style.height = "900px";
  $con.style.padding = "10px 20px";
  $con.style.fontSize = "24px";
  $con.style.wordWrap = "break-word";
  $con.innerHTML += msg;
  document.body.appendChild($con);
  if (alertTimer != null) {
    window.clearTimeout(alertTimer);
  }
  alertTimer = window.setTimeout(function() {
    $con.style.display = "none";
  }, 5000);
};

function showAuthMsg(code) {
  $("authPop").style.visibility = "visible";
  $("authResultCode").innerHTML = "错误码：" + code;
  if (code == "9102") {
    $("authResultMsg").innerHTML =
      "尊敬的用户，由于您已欠费或停机，导致您的电视无法正常点播，请及时登录网厅或拨打10000号查询并及时缴清费用。";
  } else {
    $("authResultMsg").innerHTML =
      "尊敬的用户，若电信电视无法正常观看及点播，建议您先重启机顶盒后再行尝试，若仍无法正常观看请拨打10000号！";
  }
  setTimeout(hiddenAuthMsg, 1500);
}

function hiddenAuthMsg() {
    $("authPop").style.visibility = "hidden";
}

var reportAddress = IPPORT + "/xgame-inter/services/logService/logReport";

function report(data) {
  var report_xmlHttp = "xmlHttp2013";
  if (window.XMLHttpRequest) {
    report_xmlHttp = new XMLHttpRequest();
  } else if (window.ActiveXObject) {
    report_xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  report_xmlHttp.open("POST", reportAddress, true);
  report_xmlHttp.setRequestHeader("Content-Type", "application/json");
  report_xmlHttp.send(data);
}

function slide(dir, num, dom) {
  dom = dom || "scroll";
  var ele = $(dom);
  if (dir == "transverse") {
    Util.animate(ele, { left: num, duration: 500 });
  } else if (dir == "vertical") {
    Util.animate(ele, { top: num, duration: 500 });
  }
}
function dateFormate(date, formatStr) {
  var d = date || new Date()
  var str = formatStr || 'yyyy-mm-dd hh:mm:ss';
  var Week = ['日','一','二','三','四','五','六'];

  str=str.replace(/yyyy|YYYY/,d.getFullYear());
  str=str.replace(/yy|YY/,(d.getYear() % 100)>9?(d.getYear() % 100).toString():'0' + (d.getYear() % 100));

  str=str.replace(/MM/,d.getMonth()>8 ? (d.getMonth() + 1).toString(): '0' + (d.getMonth() + 1));
  str=str.replace(/M/g,d.getMonth() + 1);

  str=str.replace(/w|W/g,Week[d.getDay()]);

  str=str.replace(/dd|DD/,d.getDate()>9?d.getDate().toString():'0' + d.getDate());
  str=str.replace(/d|D/g,d.getDate());

  str=str.replace(/hh|HH/,d.getHours()>9?d.getHours().toString():'0' + d.getHours());
  str=str.replace(/h|H/g,d.getHours());
  str=str.replace(/mm/,d.getMinutes()>9?d.getMinutes().toString():'0' + d.getMinutes());
  str=str.replace(/m/g,d.getMinutes());

  str=str.replace(/ss|SS/,d.getSeconds()>9?d.getSeconds().toString():'0' + d.getSeconds());
  str=str.replace(/s|S/g,d.getSeconds());

  return str;
}									   