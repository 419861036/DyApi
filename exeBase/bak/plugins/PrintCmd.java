package com.exe.base.plugins;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintCmd extends Cmd{
	private final static Logger logger = LoggerFactory.getLogger(PrintCmd.class);
	
	@Override
	public void exe(CmdVo caiAction, Map<String, String> param) {
		String var=param.get("var");
		Object varData=cmdHandle.getVars().get(var);
		String sys=param.get("sys");
		Object sysData=cmdHandle.getVars().get(sys);

		logger.info("--------debug-------");
		logger.info("--------输出var:{}",JSONObject.toJSONString(varData));
		logger.info("--------输出sys:{}",JSONObject.toJSONString(sysData));
		logger.info("--------debug-------end");
	}

	@Override
	public String op() {
		return null;
	}

}
