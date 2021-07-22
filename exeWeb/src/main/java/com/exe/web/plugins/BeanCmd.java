package com.exe.web.plugins;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import kkd.common.exception.KKDValidationException;
import kkd.common.util.StringUtil;


/**
 * 定义实体操作
 * @author tanbin
 *
 */
public class BeanCmd extends Cmd{

    @Override
    public void exe(final CmdVo caiAction, final Map<String, String> param) {
        final String key=param.get("key");
        final String tableName=param.get("tableName");
        final String var=param.get("var");
        final JSONArray fields =caiAction.getJsonBody();
        JSONObject data=(JSONObject) cmdHandle.getVars().get(key);
        if(fields!=null){
            for (final Object object : fields) {
                final JSONObject field= (JSONObject) object;
                String fieldName=field.getString("key");
                String verify=field.getString("verify");
                String reg=field.getString("reg");//正则表达式
                val(data, fieldName, verify, reg);
            }
        }
        JSONObject bean=new JSONObject();
        bean.put("tableName",tableName);
        bean.put("objName",key);
        bean.put("fields",fields);
        cmdHandle.getVars().put(var,bean);
    }

    public void val(JSONObject data,String fieldName,String verify,String reg){
        if(!StringUtil.isEmpty(verify)){
            String[] verrifys=StringUtil.split(verify, "|");
            for (String ver : verrifys) {
                if("required".equalsIgnoreCase(ver)){
                    String val=data.getString(fieldName);
                    if(StringUtil.isEmpty(val)){
                        throw new KKDValidationException(fieldName+"：不能为空！");
                    }
                }else if("number".equalsIgnoreCase(ver)){
                    try{
                        data.getDouble(fieldName);
                    }catch(Exception e){
                        throw new KKDValidationException(fieldName+"：不是数字！");
                    }
                }
            }
        }
        if(!StringUtil.isEmpty(reg)){
            Pattern r = Pattern.compile(reg);
            String val=data.getString(fieldName);
            Matcher m = r.matcher(val);
            if(m.find()){
                throw new KKDValidationException(fieldName+"：验证失败！");
            }
        }
    }

    @Override
    public String op() {
        return null;
    }

}
