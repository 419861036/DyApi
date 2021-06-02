package com.exe.web.plugins;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.util.StringUtil;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

/**
 * 设置 、获取头信息
 * @author tanbin
 *
 */
public class HeaderCmd extends Cmd{

	@Override
	public void exe(CmdVo cmd, Map<String, String> param) {
		Map<String, Object> sys=cmdHandle.getSys();
		HttpServletRequest req=(HttpServletRequest) sys.get("req");
		HttpServletResponse resp=(HttpServletResponse) sys.get("resp");
		
		String val=param.get("val");
		String key=param.get("key");
		String var=param.get("var");
		if(!StringUtil.isEmpty(var)
				&&!StringUtil.isEmpty(key)
				){
			Map<String, Object> vars=cmdHandle.getVars();
			String header=req.getHeader(key);
			vars.put(var, header);
		}
		if(!StringUtil.isEmpty(val)){
			resp.addHeader(key, val);
		}
		
	}

	@Override
	public String op() {
		return null;
	}

}
