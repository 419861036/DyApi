package com.exe.web.plugins;

import java.util.Map;

import kkd.common.util.StringUtil;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

/**
 * 字符串操作
 * @author tanbin
 *
 */
public class AppendCmd extends Cmd{

	@Override
	public void exe(CmdVo caiAction, Map<String, String> param) {
		String key=param.get("key");
		String when=param.get("when");
		String value=param.get("value");
		Object whenVal= cmdHandle.getVars().get(when);
		if(!StringUtil.isEmpty(whenVal)){
			String keyVal=(String) cmdHandle.getVars().get(key);
			cmdHandle.getVars().put(key, keyVal+value);		
		}
	}

	@Override
	public String op() {
		return null;
	}

}
