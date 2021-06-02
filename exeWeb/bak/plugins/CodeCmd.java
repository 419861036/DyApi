package com.exe.web.plugins;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.exe.base.Cmd;
import com.exe.base.util.GroovyUtil;
import com.exe.base.vo.CmdVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import kkd.common.util.Md5Util;
import kkd.common.util.StringUtil;

/**
 * groovy 执行器
 * @author tanbin
 *
 */
public class CodeCmd extends Cmd{
	private final static Logger logger = LoggerFactory.getLogger(CodeCmd.class);

	@Override
	public void exe(CmdVo cmd, Map<String, String> param) {
		String code=cmd.getBody();
		if(StringUtil.isEmpty(code)){
			logger.error("{}节点：code 为空", cmd.getName());
			return;
		}
		Object[] cmdParam = { cmd,cmdHandle};
		GroovyUtil.exe(code, "exe", cmdParam);
	}

	public static void main(String[] args) {
		GroovyClassLoader classLoader = new GroovyClassLoader();
        Class groovyClass = classLoader.parseClass("def exe(cmd){\n" +
                "    print cmd.param.sql\n" +
                "}");
        try {
			CmdVo cmd=new CmdVo();
			Map<String,String> param=new HashMap<>();
			param.put("sql", "select * from test");
			cmd.setParam(param);
        	cmd.setName("test");
            Object[] param1 = { cmd};
            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
            groovyObject.invokeMethod("exe",param1);
        } catch (InstantiationException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        }
	}
	@Override
	public String op() {
		return null;
	}
	

}
