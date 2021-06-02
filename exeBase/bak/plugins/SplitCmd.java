package com.exe.base.plugins;

import java.util.Map;

import kkd.common.util.StringUtil;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

public class SplitCmd extends Cmd {
	@Override
	public String op() {
		return "";
	}
	/**
	 * 字符串分割成数组
	 * @param param
	 */
	@Override
	public void exe(CmdVo caiAction, Map<String, String> param) {
		Map<String, Object> vars=cmdHandle.getVars();
		String var=param.get("var");
		String key=param.get("key");
        String split=param.get("split");
        if(!StringUtil.isEmpty(key)){
            String[] data=StringUtil.split(key, split);
            vars.put(var, data);
        }else{
            vars.put(var, null);
        }
	}

}
