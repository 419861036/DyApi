package kkd.common.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class JSONUtil {
	
	public static String getValue(String json,String key){
		JSONObject jo = null;
		String result = null;
		try {
			jo=JSONObject.parseObject(json);
			result = jo.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jo = null;
		return result;
	}
}
