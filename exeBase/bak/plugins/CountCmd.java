package com.exe.base.plugins;

import java.util.Map;

import kkd.common.util.StringUtil;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

public class CountCmd extends Cmd {
	@Override
	public String op() {
		return "count";
	}
	/**
	 * 计数器
	 * @param param
	 */
	@Override
	public void exe(CmdVo caiAction, Map<String, String> param) {
		Map<String, Object> vars=cmdHandle.getVars();
		String var=param.get("var");
		String initStr=param.get("init");
		String incStr=param.get("inc");
		String decStr=param.get("dec");
		Integer data=(Integer) vars.get(var);
		data=data==null?0:data;
		
		
		Integer init=null;
		if(!StringUtil.isEmpty(initStr)){
			init=Integer.parseInt(initStr);
		}
		
		Integer inc=null;
		if(!StringUtil.isEmpty(incStr)){
			inc=Integer.parseInt(incStr);
		}
		
		Integer dec=null;
		if(!StringUtil.isEmpty(decStr)){
			dec=Integer.parseInt(decStr);
		}
		if(init==null){
			if(inc!=null){
				vars.put(var, data+inc);
				return ;
			}
			if(dec!=null){
				vars.put(var, data-dec);
				return ;
			}
		}else{
			if(inc!=null){
				vars.put(var, init+inc);
				return ;
			}
			if(dec!=null){
				vars.put(var, init-dec);
				return ;
			}
		}
		vars.put(var, init);
	}

}
