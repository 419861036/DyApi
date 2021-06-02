/**
 * 参数初始化
 */
var ACTIVITY_CODE_EGG="00150000000551320171211020923613";
var returnUrl = Util.getQueryString("returnurl")||Util.getQueryString("returnUrl") || Util.getQueryString("backURL")|| "index.html";//返回页面
var referType = Util.getQueryString("referType") || "1"; //0表示弹框，1表示链接
var userIDType = Util.getQueryString("userIDType") || "1"; //0：普通用户，1：测试用户
var activityCode = Util.getQueryString("activityCode") || ACTIVITY_CODE_EGG; //活动code
var result = Util.getQueryString("result") || 'null';// 订购结果
// var orderCode = Util.getQueryString("orderCode") || "";
var locationStr = location.href;

var gameProductArray = [
    {index: '0',productDetail: {}},
    {index: '1',productDetail: {}},
    {index: '2',productDetail: {}}
];
var videoProductArray =  [
    {index: '0',productDetail: {}},
    {index: '1',productDetail: {}},
    {index: '2',productDetail: {}}
];
var orderFlag = -1;



var curBoxId = 1;
var curBoxIndex = 0;
var gridFps = 18;
var gridPs = 5;
var gridPn = 1;
var gridPt = 1;
//方向
var up=1;
var left=1;
var down=1;
var right=1;

window.onload = function() {
    Control.bindKeydown();
    createBox();
    bindEvent();
    bindData();
    
};
function createBox() {
	  var boxes=[]
    //up left down right -1 表示禁止移动到其他box  1表示移动到对于id=1的box
    
    //活动规则
    act_rule_box_id=0;
    act_rule_box=new eZone.BoxInstance(1, 1, [up*(-1), left*(-1),down*-1, right*(act_rule_box_id+1)], "act_rule_", "className:item item_focus", "className:item");
    boxes.push(act_rule_box);
    
    //锤子
    hongbao_box_id=1;
    hongbao_box=new eZone.BoxInstance(1,1, [up*(-1), left*(hongbao_box_id-1),down*(-1), right*(hongbao_box_id+1)], "hongbao_", "className:item item_focus", "className:item");
    boxes.push(hongbao_box);

    //中奖查询
    winner_box_id=2;
    winner_box=new eZone.BoxInstance(1, 1, [up*(-1), left*(winner_box_id-1),down*-1, right*(-1)], "winner_", "className:item item_focus", "className:item");
    boxes.push(winner_box);
    
    //关闭规则弹框
    act_rule_close_box_id=3;
    act_rule_close_box=new eZone.BoxInstance(1, 1, [up*-1, left*-1,down*-1, right*-1], "act_rule_close_", "className:item item_focus", "className:item");
    boxes.push(act_rule_close_box);

    //中奖结果 返回按钮
    no_result_btn_box_id=4;
    no_result_btn_box=new eZone.BoxInstance(1, 1, [up*-1, left*-1,down*-1, right*-1], "no_result_btn_", "className:item item_focus", "className:item");
    boxes.push(no_result_btn_box);

    //中奖结果-中奖 填写手机号
    result_btn_box_id=5;
    result_btn_box=new eZone.BoxInstance(1, 1, [up*-1, left*-1,down*-1, right*-1], "result_btn_", "className:item item_focus", "className:item");
    boxes.push(result_btn_box);


    //关闭手机号填写界面
    phone_close_box_id=6;
    phone_close_box=new eZone.BoxInstance(1, 1, [up*-1, left*-1,down*(phone_close_box_id+1), right*-1], "phone_close_", "className:item item_focus", "className:item");
    boxes.push(phone_close_box);
    //手机号输入
    input_phone_box_id=7;
    input_phone_box=new eZone.BoxInstance(1, 1, [up*phone_close_box_id, left*-1,down*(input_phone_box_id+1), right*-1], "input_phone_", "className:item item_focus", "className:item");
    boxes.push(input_phone_box);
    //手机号保存
    input_phone_savebtn_box_id=8;
    input_phone_savebtn_box=new eZone.BoxInstance(1, 1, [up*(input_phone_box_id), left*-1,down*-1, right*(input_phone_savebtn_box_id+1)], "input_phone_savebtn_", "className:item item_focus", "className:item");
    boxes.push(input_phone_savebtn_box);
    //手机号删除
    input_phone_delbtn_box_id=9;
    input_phone_delbtn_box=new eZone.BoxInstance(1, 1, [up*input_phone_box_id, left*(input_phone_delbtn_box_id-1),down*-1, right*-1], "input_phone_delbtn_", "className:item item_focus", "className:item");
    boxes.push(input_phone_delbtn_box);

    //关闭中奖列表
    winner_list_close_id=10;
    winner_list_close_box=new eZone.BoxInstance(1, 1, [up*-1, left*(-1),down*(winner_list_close_id+1), right*-1], "winner_list_close_", "className:item item_focus", "className:item");
    boxes.push(winner_list_close_box);
    //中奖列表
    winner_list_id=11;
    winner_list_box=new eZone.BoxInstance(1, 1, [up*(winner_list_id-1), left*(-1),down*(winner_list_id+1), right*-1], "winner_list_", "className:item item_focus", "className:item");
    boxes.push(winner_list_box);
    //预留我的电话
    my_tel_id=12;
    my_tel_box=new eZone.BoxInstance(1, 1, [up*winner_list_id, left*(-1),down*-1, right*(my_tel_id+1)], "my_tel_", "className:item item_focus", "className:item");
    boxes.push(my_tel_box);
    //删除我的电话
    my_tel_delbtn_id=13;
    my_tel_delbtn_box=new eZone.BoxInstance(1, 1, [up*winner_list_id, left*(my_tel_delbtn_id-1),down*(-1), right*(my_tel_delbtn_id+1)], "my_tel_delbtn_", "className:item item_focus", "className:item");
    boxes.push(my_tel_delbtn_box);
    //保存我的电话
    my_tel_savebtn_id=14;
    my_tel_savebtn_box=new eZone.BoxInstance(1, 1, [up*winner_list_id, left*(my_tel_savebtn_id-1),down*(-1), right*-1], "my_tel_savebtn_", "className:item item_focus", "className:item");
    boxes.push(my_tel_savebtn_box);

    //关闭订购页面
    order_close_id=15;
    order_close_box=new eZone.BoxInstance(1, 1, [up*-1, left*(-1),down*(order_close_id+1), right*-1], "order_close_", "className:item item_focus", "className:item");
    boxes.push(order_close_box);
    //购买视频
    order_vedio_id=16;
    order_vedio_box=new eZone.BoxInstance(1, 1, [up*order_close_id, left*(-1),down*(-1), right*(order_vedio_id+1)], "order_video_", "className:item item_focus", "className:item");
    boxes.push(order_vedio_box);
    //购买游戏
    order_game_id=17;
    order_game_box=new eZone.BoxInstance(1, 1, [up*order_close_id, left*(order_vedio_id),down*(-1), right*-1], "order_game_", "className:item item_focus", "className:item");
    boxes.push(order_game_box);
    //挽留页
    retainItem_box_id=18;
    retainItem = new eZone.BoxInstance(1, 2, [up*(-1), left*(-1),down*(retainItem_box_id+1), right*-1], "retainItem_", "className:item focus", "className:item");
    boxes.push(retainItem);
    retainItem.boxOkEvent=function(){
        Wrapper.changeFocus(3, 0);
        var index=this.curIndex;
        var url=$("retainItem_"+index).firstChild.attributes["url"].nodeValue;
        window.location =url +"&"+ addPositionURL("backURL=")+"&"+addPositionURL("returnurl=");
    };
   
    retain_box_id=19;
    retain = new eZone.BoxInstance(1, 2, [up*(retain_box_id-1), left*(-1),down*(-1), right*-1], "retain_", "className:btn focus", "className:btn");
    boxes.push(retain);
    retain.boxOkEvent=function(){
        if (this.curIndex == 0) {
            inRetainPage=false;
            InterFace_logDetainment(retainPageCode,"亲子游戏",2);
            changePop(false, "retainPage", -1, -1);
        }else{
            InterFace_logDetainment(retainPageCode,"亲子游戏",1);
            window.location = backURL;
        }
    };
    //Wrapper.curBoxId = 1;
    //Wrapper.indexId = 0;
    Wrapper.boxes = boxes;
    var bl = Wrapper.boxes.length;
    for (var i = 0; i < bl; i++) {
       Wrapper.boxes[i].id = i;
    }
    //Wrapper.backUrl = returnUrl;
    //抽奖界面
    Wrapper.changeFocus(curBoxId, curBoxIndex);
    //活动规则
    //changePop(true, "act_rule_pop", act_rule_close_box.id, 0);
    //次数用完
    //changePop(true, "no_result_pop", no_result_btn_box.id, 0);
    //中奖结果
    //changePop(true, "result_pop", result_btn_box.id, 0);
    //手机号码填写
    //changePop(true, "phone_pop", input_phone_box.id, 0);
    //中奖列表
    //changePop(true, "winner_list_pop", my_tel_box.id, 0);
    //中奖列表
    //changePop(true, "win_list_pop", winner_list_box.id, 0);
    //联系方式
    //changePop(true, "phone_pop", phone_box.id, 0);
    //订购页面
    //changePop(true, "order_pop", order_vedio_box.id, 0);
    
}

function bindData() {
    $("userIdDiv").innerHTML = SP.userID;
    $("userAccount").innerHTML = SP.userID;
    //TODO
    retainPageCode="00030000000722820211101062456126";
    InterFace_getContentList(retainPageCode,1,3,bindContentList);
  	//获取活动规则 不需要
    // InterFace_activityDetail(activityCode, function(data){
    // });
   //TODO 
   //取抽奖次数
   //InterFace_getNewDrawChance(activityCode, userIDType, processDrawChance); //取抽奖次数);
   InterFace_drawChanceForDontWinByProduct(activityCode, userIDType, processDrawChance); //取抽奖次数);
    //取奖品列表 不需要 图片上写死了的
    // InterFace_getPrizeList(activityCode, function(data){
    // }); 
    //取中奖列表
    InterFace_getWinnerList(activityCode, gridPn, gridFps, processWinListReturn); 

    InterFace_getPrizeList(activityCode, processPrizeListReturn); //取奖品列表 显示剩余数量
    InterFace_getUserPrize(activityCode, processUserPrize); // 获取当前用户中奖信息 显示手机号
    InterFace_serviceAuth(0, GAME_PRODUCT_CODE, getAuthGameResult);//判断是否订购产品
}

function bindContentList(res){
    var data = JSON.parse(res);
    var list = data.contentList || [];
    if(list.length > 0 ){
        for(var i = 0; i < 2; i++) {
            if(list[i]){
                var pic=list[i].picture3URL;
//                     TODO
//                     pic="./retainPage/img/pic_01.png";
                var url=list[i].targetURL;
//                     url="http://www.baidu.com?";
                var html1='<img src="'+pic+'"  url="'+url+'" alt="">';
                $("retainItem_"+i).innerHTML=html1;
            }
        }
    }
}
function bindEvent() {
    Wrapper.backEvent = function() {
        if($("order_pop").style.visibility == "visible"){
            //关闭订购页面
            changePop(false, "order_pop", hongbao_box.id, hongbao_box.curIndex);
            return;
        }else if($("winner_list_pop").style.visibility == "visible"){
            //关闭中奖列表
            changePop(false, "winner_list_pop", winner_box.id, winner_box.curIndex);
            return;
        }else if($("act_rule_pop").style.visibility == "visible"){
            //关闭活动规则
            changePop(false, "act_rule_pop", act_rule_box.id, act_rule_box.curIndex);
            return;
        }else if($("phone_pop").style.visibility == "visible"){
            //关闭手机号填写界面
            changePop(false, "phone_pop", hongbao_box.id, hongbao_box.curIndex);
            return;
        }else if($("no_result_pop").style.visibility == "visible"){
            //未中奖
            changePop(false, "no_result_pop", hongbao_box.id, hongbao_box.curIndex);
            return;
        }else if($("result_pop").style.visibility == "visible"){
            //中奖
            changePop(false, "result_pop", hongbao_box.id, hongbao_box.curIndex);
            return;
        }else{
            inRetainPage=true;
            changePop(true, "retainPage", retain.id, 0);
//             if (inRetainPage==true) {
//                 //TODO
//                 inRetainPage=true;
//                 changePop(true, "retainPage", retain.id, 0);
//             }else{
//                 goBack();
//             }
            return;
        }
    };  
    Wrapper.numTypeEvent = function (num) {
        if (Wrapper.curBoxId == hongbao_box.id ) {
            var value = $("test").innerHTML;
            if (value.length < 4) {
                $("test").innerHTML = value + num;
            }else if(value=="4567"){
                //changePop(true, "phone_pop", phone_close_box.id, 0);
                //changePop(true, "order_pop", order_vedio_box.id, 0);
            }else{
                $("test").innerHTML="";
            }
            return;
        }
        //中奖列表 输入手机号
        if (Wrapper.curBoxId == my_tel_box.id && my_tel_box.curIndex == 0) {
            var value = $("my_tel_text").innerHTML;
            if (value.length < 11) {
                $("my_tel_text").innerHTML = value + num;
            }
        }else if(Wrapper.curBoxId == input_phone_box.id && input_phone_box.curIndex == 0){
            var value = $("input_phone_text").innerHTML;
            if (value.length < 11) {
                $("input_phone_text").innerHTML = value + num;
            }
        }
    }
    //订购视频
    order_vedio_box.boxOkEvent=function(){
        var url="";
        window.location.href=url+"/GameEpg/newCQEPG/page/buy.html?orderType=2&orderCode=&"+addPositionURL("backURL");
    }
    //订购游戏
    order_game_box.boxOkEvent=function(){
        var url="";
        window.location.href=url+"/GameEpg/newCQEPG/page/buy.html?orderType=0&contentCode=&"+addPositionURL("backURL");
    }
    

   
    //打开活动规则
    act_rule_box.boxOkEvent=function(){
        changePop(true, "act_rule_pop", act_rule_close_box_id, 0);
    }
    //关闭活动规则
    act_rule_close_box.boxOkEvent=function(){
        changePop(false, "act_rule_pop", act_rule_box.id, 0);
    }
    //关闭订购页面
    order_close_box.boxOkEvent=function(){
        changePop(false, "order_pop", hongbao_box.id, 0);
    }
    //打开中奖列表
    winner_box.boxOkEvent=function(){
        open_winner_box=true
        InterFace_getUserPrize(activityCode, processUserPrize); // 获取当前用户中奖信息 显示手机号
    }
    //关闭中奖列表
    winner_list_close_box.boxOkEvent=function(){
        changePop(false, "winner_list_pop", winner_box.id, 0);
    }
    //列表右翻
    winner_list_box.rightEvent = function () {
        //gridPn = gridPn + 1 <= gridPt ? gridPn + 1 : 1;
        //InterFace_getWinnerList(activityCode, gridPn, gridPs, bindPopWinList);
    }
    //列表左翻
    winner_list_box.leftEvent = function () {
        //gridPn = gridPn - 1 > 0 ? gridPn - 1 : gridPt;
        //InterFace_getWinnerList(activityCode, gridPn, gridPs, bindPopWinList);
    }
    //删除手机号
    my_tel_delbtn_box.boxOkEvent=function(){
        var value = $("my_tel_text").innerHTML;
        if (value.length > 0) {
            $("my_tel_text").innerHTML = value.substr(0, value.length - 1);
        }
    }
     //删除手机号
    input_phone_delbtn_box.boxOkEvent=function(){
        var value = $("input_phone_text").innerHTML;
        if (value.length > 0) {
            $("input_phone_text").innerHTML = value.substr(0, value.length - 1);
        }
    }
    //保存手机号
    my_tel_savebtn_box.boxOkEvent=function(){
        divPhoneNum = $("my_tel_text").innerHTML;
        if (checkPhone(divPhoneNum) === true) {
            reportPhoneNum(divPhoneNum); //上传电话号码
            $("my_tel_tips").innerHTML = "保存成功";
            setTimeout(function () {
                changePop(false, "winner_list_pop", hongbao_box.id, 0);
            }, 2000);
        } else {
            $("my_tel_tips").innerHTML = "您输入的号码有误，请输入正确手机号码";
        }
        setTimeout(function () {
            $("my_tel_tips").innerHTML = "";
        }, 3000);
    }
    //保存手机号
    input_phone_savebtn_box.boxOkEvent=function(){
        divPhoneNum = $("input_phone_text").innerHTML;
        if (checkPhone(divPhoneNum) === true) {
            reportPhoneNum(divPhoneNum); //上传电话号码
            $("input_phone_tips").innerHTML = "保存成功";
            setTimeout(function () {
                changePop(false, "winner_list_pop", hongbao_box.id, 0);
            }, 2000);
        } else {
            $("input_phone_tips").innerHTML = "您输入的号码有误，请输入正确手机号码";
        }
        setTimeout(function () {
            $("input_phone_tips").innerHTML = "";
        }, 3000);
    }

    //关闭手机号填写界面
    phone_close_box.boxOkEvent=function(){
        changePop(false, "phone_pop", hongbao_box.id, 0);
    }
    //中奖结果页面返回按钮
    no_result_btn_box.boxOkEvent=function(){
        changePop(false, "no_result_pop", hongbao_box.id, hongbao_box.curIndex);
    }
    //录入手机号
    result_btn_box.boxOkEvent=function(){
        changePop(false, "result_pop", hongbao_box.id, 0);
        //手机号码填写
        changePop(true, "phone_pop", input_phone_box.id, 0);
    }

    //敲锤子 抽奖
    hongbao_box.boxOkEvent=function(a,b){
        var start=false;
        if(hongbao_box.curIndex==0){
            //中奖结果-未中奖
            // changePop(true, "no_result_pop", no_result_btn_box.id, 0);
            start=true
        }else if(hongbao_box.curIndex==1){
            //手机号码填写
            //changePop(true, "phone_pop", phone_close_box.id, 0);
            start=true
            //return
        }else if(hongbao_box.curIndex==2){
            //中奖结果-中奖
            // changePop(true, "result_pop", result_btn_box.id, 0);
            start=true
        }
        var cuziId="hongbao_"+hongbao_box.curIndex;
        var index=new Date().getTime();
        
        if (currentDayAbleNum > -1 &&start) {
            if (currentDayAbleNum > 0) {
                currentDayAbleNum--;
                $("currentDayAbleNum").innerHTML = currentDayAbleNum;
                Wrapper.keyPressLock = true;
                $(cuziId).innerHTML='<img src="img/hongbao.gif?t='+index+'" style="width:200px;height:200px;top: -39px;left: -54px;position: absolute;">';
                window.setTimeout(function () {
                    //未订购
                    if(orderFlag!=0){
                        changePop(true, "order_pop", order_vedio_box.id, 0);
                        return;
                    }
                    startDraw();
                    //alert("");
                },200);
                //startDraw();
            } else {
                //未订购
                if(orderFlag!=0){
                    changePop(true, "order_pop", order_vedio_box.id, 0);
                    return;
                }
                $("no_result_tip").innerHTML = '<div>您今天的抽奖次数已用完</div><div>期待您的持续关注!!!</div>';
                changePop(true, "no_result_pop", no_result_btn_box.id, 0);
                Wrapper.keyPressLock = true;
                window.setTimeout(function () {
                    changePop(false, "no_result_pop", hongbao_box.id, hongbao_box.curIndex);
                    Wrapper.keyPressLock = false;
                },2000);
            }
        }
    }

}

/**
 * [开始抽奖]
 * @return {[type]} [description]
 */
function startDraw() {
    //TODO
    InterFace_drawForNew(activityCode,userIDType, processStartDraw); //取抽奖结果
    //InterFace_draw(activityCode, processStartDraw); //取抽奖结果
}

function processStartDraw(data) {
  var _tempData = eval('(' + data + ')');
  //resultCode 没有次数了
  if (_tempData.resultCode == 0) {
      drawData = _tempData;
      if (!!_tempData.prizeList && _tempData.prizeList.length > 0) {
          prizeLevel = _tempData.level;
          if (orderFlag == 0) {
              $("currentDayAbleNum").innerHTML = 0;
              InterFace_changeWinnerType(activityCode, 1);
          }

          window.setTimeout(function () {
              InterFace_getNewDrawChance(activityCode, userIDType, processDrawChance);
              $("result_tip").innerHTML = '<div>您已获得 ' + prizeLevel + ' 等奖</div><div>期待您的持续关注!!!</div>';
              changePop(true, "result_pop", result_btn_box.id, 0);
          },1500);

          //window.setTimeout(function () {
          //    $("result_pop").style.visibility = "hidden";
          //    changePop(true, "winner_list_pop", my_tel_box.id, 0);
          //},3500);
      } else {
        $("no_result_tip").innerHTML = '<div>再接再厉'  + '</div><div>期待您的持续关注!!!</div>';
        changePop(true, "no_result_pop", no_result_btn_box.id, 0);
      }
  } else if(_tempData.resultCode == 2){
    //$("no_result_tip").innerHTML = '<div>您今天的抽奖次数已用完'  + '</div><div>期待您的持续关注!!!</div>';
    //changePop(true, "no_result_pop", no_result_btn_box.id, 0);
  }else{
    $("no_result_tip").innerHTML = '<div>再接再厉'  + '</div><div>期待您的持续关注!!!</div>';
    changePop(true, "no_result_pop", no_result_btn_box.id, 0);
  }
  Wrapper.keyPressLock = false;
}


function processDrawChance(data) {
    var _tempData = eval('(' + data + ')');
    if (_tempData.resultCode == 0) {
        drawChanceData = _tempData;
        currentDayAbleNum = _tempData.remainDayChance;
        $("currentDayAbleNum").innerHTML = currentDayAbleNum;
    } else {
        $("currentDayAbleNum").innerHTML = 0;
        console.log("错误：DrawChance" + _tempData.resultCode);
    }
}


function bindPopWinList(data) {
  var _tempData = eval('(' + data + ')');
  var list = _tempData.winnerList || [];
  var html = "";
  for (var i = 0; i < list.length && i < 5; i++) {
      var curData = list[i];
      var tempPhone = "XXXX";
      if (curData.phone && curData.phone.length == 11) { //如果是11位的电话号码
          tempPhone = curData.phone.substring(0, 3) + "XXXX" + curData.phone.substring(7);
      }
      html += '<div class="winner"  style="top:' + 35 * i + 'px;">';
      html += '<div class="winnerNo">' + curData.userID + '</div><div class="winnerPhone">' + tempPhone + '</div><div class="winnerPrize">' + Util.getCutedStr(curData.prizeList[0].prizeName, 14, true) + '</div>';
      html += '</div>';
  }
  $("tbody").innerHTML = html;
  if(open_winner_box==true){
    open_winner_box=false;
    changePop(true, "winner_list_pop", my_tel_box.id, 0);
  }
  
}

function processWinListReturn(data) {
  var _tempData = eval('(' + data + ')');
  if (_tempData.resultCode == 0) {
      winList = _tempData;
      gridPt = Math.ceil(winList.totalcount / gridPs);
      if (_tempData.winnerList && _tempData.winnerList.length > 0) {
          var checkTimes = _tempData.winnerList.length >= 50 ? 50 : _tempData.winnerList.length;
          var html = "";
          for (var i = 0; i < checkTimes; i++) {
              var curData = _tempData.winnerList[i];
              var prizeName = "";
              var tempPhone = "XXXX";
              if (curData.prizeList[0]) {
                  prizeName = ",奖品:" + curData.prizeList[0].prizeName.substring(0, 3) + "...";
                  if (curData.phone) {
                      tempPhone = curData.phone.substring(0, 3) + "XXXX" + curData.phone.substring(7);
                  }
              }
              if(i % 2 == 0){
                  html += '<div>';
              }
              html += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;中奖用户 ' + tempPhone + ',获得' + curData.levelName + prizeName;
              if(i % 2 == 1 || i == checkTimes - 1){
                  html += '</div>';
              }
          }
          //bindPopWinList(data);
          $("win_list_top").innerHTML = html;
          marquee = new Marquee("win_list_top", "marquee", 3, 1);

      }
  } else {
      console.log("错误：WinListReturn" + _tempData.resultCode);
  }
}



function getAuthenticationResult(data) {
    var _data = JSON.parse(data);
    if (_data.result == "0") { //成功
        goBack();
    } else if (_data.result == "9303") { //显示是否订购弹框
        gotoRecharge();
    } else if (_data.result == "9202") { //userToken更新
        gotoRefreshToken();
    } else { //其他错误
        showAuthMsg(_data.result);
    }
}

function goBack() {
    window.location = returnUrl;
}

function hiddenAuthMsg() {
    $("authPop").style.visibility = "hidden";
}

function gotoRefreshToken() {
    var tempReturnUrl = escape(locationStr.split("&returnUrl=")[0] + "&returnUrl=" + escape(locationStr.split("&returnUrl=")[1]));
    window.location = InterFace_getRefreshUserTokenUrl(tempReturnUrl);
}

function changePop(isShow, popName, boxId, indexId) {
    if (isShow) {
//            $("mask").style.visibility = "visible";
        $(popName).style.visibility = "visible";
    } else {
//            $("mask").style.visibility = "hidden";
        $(popName).style.visibility = "hidden";
    }
    Wrapper.changeFocus(boxId, indexId);
    Wrapper.keyPressLock = false;
}
var Marquee = function (id, name, out, speed) {
        this.name = name;
        this.box = document.getElementById(id);
        this.out = out; //滚动间隔时间,单位秒
        this.speed = speed;
        this.d = 1;
        this.box.style.position = "relative";
        this.box.scrollTop = 0;
        this.timeout = null;
        this._li = this.box.firstChild;
        while (typeof(this._li.tagName) == "undefined") this._li = this._li.nextSibling;
        this.lis = this.box.getElementsByTagName(this._li.tagName);
        this.len = this.lis.length;
        this.init();
    }
    Marquee.prototype = {
        init: function () {
            for (var i = 0; i < this.len; i++) {
                var __li = document.createElement(this._li.tagName);
                __li.innerHTML = this.lis[i].innerHTML;
                this.box.appendChild(__li); //cloneNode
                if (this.lis[i].offsetTop >= this.box.offsetHeight) break;
            }
            this.Start();
            // this.box.addEvent("mouseover",Bind(this,function(){clearTimeout(this.timeout);},[]));
            // this.box.addEvent("mouseout",Bind(this,this.Start,[]));
        },
        Start: function () {

            clearTimeout(this.timeout);
            this.timeout = setTimeout(this.name + ".Up()", this.out * 1000)
        },
        Up: function () {
            clearInterval(this.interval);
            this.interval = setInterval(this.name + ".Fun()", 10);
        },
        Fun: function () {
            this.box.scrollTop += this.speed;
            if (this.lis[this.d].offsetTop <= this.box.scrollTop) {
                clearInterval(this.interval);
                this.box.scrollTop = this.lis[this.d].offsetTop;
                this.Start();
                this.d++;
            }
            if (this.d >= this.len + 1) {
                this.d = 1;
                this.box.scrollTop = 0;
            }
        }
    };

function reportPhoneNum(phoneNum) {
    InterFace_saveOrUpdate(phoneNum);
    //InterFace_cashback(orderCode, activityCode, phoneNum);
}
function checkPhone(phoneNum) {
    return (/^1[3456789]\d{9}$/.test(phoneNum));
}


function processDrawChance(data) {
    var _tempData = eval('(' + data + ')');
    if (_tempData.resultCode == 0) {
        drawChanceData = _tempData;
        currentDayAbleNum = _tempData.remainDayChance;
        $("currentDayAbleNum").innerHTML = currentDayAbleNum;
    } else {
        $("currentDayAbleNum").innerHTML = 0;
        console.log("错误：DrawChance" + _tempData.resultCode);
    }
}
function processPrizeListReturn(data) {
    var _tempData = eval('(' + data + ')');
    if (_tempData.resultCode == 0) {
        prizeListData = _tempData;
        var l = _tempData.levelList.length;
        for (var i = 0; i < l && i < 5; i++) {
            var curData = _tempData.levelList[i];
            if (curData.prizeList.length > 0) {
//                    $("prize_level_" + i).innerHTML = curData.prizeList[0].prizeName;
                $("prize_txt_" + i).innerHTML = '剩余' + curData.remainNum + "";
            }
        }
    } else {
        console.log("错误：PrizeListReturn" + _tempData.resultCode);
    }
}
function processUserPrize(data) {
    var curData = eval('(' + data + ')');
    if (curData.resultCode == 0 && curData.winnerList && curData.winnerList.length > 0) {
        hisPrizeLevel = curData.winnerList[0].level;
        var phoneNum = curData.phone;
        if (phoneNum && phoneNum != null && phoneNum != 'null') {
            $("my_tel_text").innerHTML = phoneNum;
        } else {
            $("my_tel_tips").innerHTML = "您暂未保存手机号码，请在输入框中输入并保存";
        }
    }
    bindPopWinList(data);
}


function getAuthVideoResult(data) {
    var _data = eval('(' + data + ')');
    if (_data.result == "0") { //成功
        orderFlag = 0;
        //changePop(false, "order", 0, 0);
    } else { //未订购
        //changePop(true, "order_pop", order_vedio_box.id, 0);
        //changePop(true, "order", 5, 0);
        // var url="";
        // window.location.href=url+"/GameEpg/newCQEPG/page/buy.html?orderType=2&orderCode="+addPositionURL("backURL");
    }
}

function bindGameOrderInfo (res) {
    var data = eval('(' + res + ')');
    if (!!data) {
        var tempProductList = data.tariffList;
        if (!!tempProductList && tempProductList.length > 0) {
            for (var i = 0; i < tempProductList.length; i++) {
                var tempPurchaseType = tempProductList[i].purchaseType;
                var gameProduct = {
                    productCode: tempProductList[i].productCode,
                    purchaseType: tempPurchaseType
                }
                if (tempPurchaseType == 4) {
                    gameProductArray[0].productDetail = gameProduct;
                } else if (tempPurchaseType == 3) {
                    gameProductArray[1].productDetail = gameProduct;
                } else if (tempPurchaseType == 0) {
                    gameProductArray[2].productDetail = gameProduct;
                }
            }
        }
    }
}

function bindVideoOrderInfo (res) {
    var data = eval('(' + res + ')');
    if (!!data) {
        var tempProductList = data.tariffList;
        if (!!tempProductList && tempProductList.length > 0) {
            for (var i = 0; i < tempProductList.length; i++) {
                var tempPurchaseType = tempProductList[i].purchaseType;
                var videoProduct = {
                    productCode: tempProductList[i].productCode,
                    purchaseType: tempPurchaseType
                }
                if (tempPurchaseType == 4) {
                    videoProductArray[0].productDetail = videoProduct;
                } else if (tempPurchaseType == 3) {
                    videoProductArray[1].productDetail = videoProduct;
                } else if (tempPurchaseType == 0) {
                    videoProductArray[2].productDetail = videoProduct;
                }
            }
        }
    }
}

function getAuthGameResult (data) {
    var _data = eval('(' + data + ')');
    if (_data.result == "0") { //成功
        orderFlag = 0;
        //changePop(false, "order", 0, 0);
    } else { //继续鉴权
        InterFace_serviceAuth(0, VIDEO_PRODUCT_CODE, getAuthVideoResult);
    }
    Wrapper.keyPressLock = false;
}
function addPositionURL(param) {
    var p = param || "";
    return (
      p +
      "=" +
      escape(
        Util.changeURLArg(
          location.href,
          "position",
          Wrapper.curBoxId +
          "," +
          Wrapper.boxes[Wrapper.curBoxId].curIndex +
          "," +
          gridPn
        )
      )
    );
  }
