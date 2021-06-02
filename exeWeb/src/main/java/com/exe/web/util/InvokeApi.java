package com.exe.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kkd.common.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class InvokeApi {
    private static String apiUrl="http://www.yunmi.ren";



    public JSONObject json(String url, JSONObject param){
        String str=JSON.toJSONString(param);
        Map<String, String> head=new HashMap<>();
        head.put("Content-Type","application/json");
        head.put("Cookie","sessionId=f0764a58cde14e34a4dfe4326ba57d04");
        String jsonStr=HttpUtil.httpSendBase(apiUrl+url,str,null,head);
        JSONObject rs=JSONObject.parseObject(jsonStr);
        return rs;
    }

}
