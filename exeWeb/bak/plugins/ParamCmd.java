package com.exe.web.plugins;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import kkd.common.exception.KKDValidationException;
import kkd.common.util.StringUtil;

/**
 * 参数获取
 * @author tanbin
 *
 */
public class ParamCmd extends Cmd{

	@Override
	public void exe(CmdVo cmd, Map<String, String> param) {
		Map<String, Object> sys=cmdHandle.getSys();
		HttpServletRequest req=(HttpServletRequest) sys.get("req");
		
		Map<String, Object> vars=cmdHandle.getVars();
		
		String key=param.get("key");
		String var=param.get("var");
		String verify=param.get("verify");
		String reg=param.get("reg");
		String data=req.getParameter(key);
		val(data, key, verify, reg);
		vars.put(var, data);
	}


	public void val(String data,String fieldName,String verify,String reg){
        if(!StringUtil.isEmpty(verify)){
            String[] verrifys=StringUtil.split(verify, "|");
            for (String ver : verrifys) {
                if("required".equalsIgnoreCase(ver)){
                    if(StringUtil.isEmpty(data)){
                        throw new KKDValidationException(fieldName+"：不能为空！");
                    }
                }else if("number".equalsIgnoreCase(ver)){
                    try{
						Double.parseDouble(data);
                    }catch(Exception e){
                        throw new KKDValidationException(fieldName+"：不是数字！");
                    }
                }
            }
        }
        if(!StringUtil.isEmpty(reg)){
            Pattern r = Pattern.compile(reg);
            Matcher m = r.matcher(data);
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
