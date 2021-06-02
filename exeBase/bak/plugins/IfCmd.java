package com.exe.base.plugins;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.exe.base.Cmd;
import com.exe.base.util.GroovyUtil;
import com.exe.base.vo.CmdVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.util.StringUtil;

/**
 * 通过js执行判断
 * @author tanbin
 *
 */
public class IfCmd extends Cmd{
	private final static Logger logger = LoggerFactory.getLogger(IfCmd.class);
	
	@Override
	public void exe(CmdVo caiAction, Map<String, String> param) {
		String js=param.get("js");
		String trueVal=param.get("true");
		String falseVal=param.get("false");
		try {
			boolean b=(boolean)GroovyUtil.parse(js, cmdHandle.getVars());
			logger.debug("执行js:{},结果：{}",js,b);
			if(b){
				if(!StringUtil.isEmpty(trueVal)){
					cmdHandle.gotoNodeByName(caiAction,trueVal);
				}
			}else{
				String var=param.get("var");
				String val=param.get("val");
				if(!StringUtil.isEmpty(val) && !StringUtil.isEmpty(var)){
					cmdHandle.getVars().put(var, val);
				}
				cmdHandle.gotoNodeByName(caiAction,falseVal);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public String op() {
		return null;
	}
	
	private static ScriptEngine engine;
	
	private static ScriptEngine getJs() {
		if(engine==null){
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	        engine = scriptEngineManager.getEngineByName("javascript");
		}
        return engine;
	}
	
	public static void main(String[] args) {
		String s="'ss'!=''";
		s="1+2";
		Map<String,String> map=new HashMap<>();
		GroovyUtil.parse(s,null);
	}
}
