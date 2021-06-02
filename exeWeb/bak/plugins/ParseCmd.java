package com.exe.web.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import kkd.common.util.IOUtils;

public class ParseCmd extends Cmd{
	private final static Logger logger = LoggerFactory.getLogger(ParseCmd.class);
	
	private JSONObject getVo() throws IOException {
		Map<String, Object> sys=cmdHandle.getSys();
		HttpServletRequest req=(HttpServletRequest) sys.get("req");
		InputStream is=req.getInputStream();
		String str=IOUtils.toString(is, "utf-8");
		logger.debug("parse data:{}",str);
		JSONObject vo=JSON.parseObject(str);
		return vo;
	}

	@Override
	public void exe(CmdVo cmd, Map<String, String> param) {
//		logger.setRuleId(cmdHandle.getCaiRule().getId().toString());
		Map<String, Object> vars=cmdHandle.getVars();
		String var=param.get("var");
		try {
			JSONObject json=getVo();
			vars.put(var, json);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@Override
	public String op() {
		return null;
	}
}
