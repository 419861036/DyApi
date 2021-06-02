package com.exe.web.plugins;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;
import com.exe.base.vo.ExeResourceVo;
import com.exe.bo.RuleBo;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import kkd.common.entity.Msg;
import kkd.common.util.StringUtil;

public class TplCmd extends Cmd{
	private final static Logger logger = LoggerFactory.getLogger(TplCmd.class);
	
	@Override
	public void exe(CmdVo cmd, Map<String, String> param) {
//		logger.setRuleId(cmdHandle.getCaiRule().getId().toString());
		Map<String, Object> sys=cmdHandle.getSys();
		HttpServletRequest req=(HttpServletRequest) sys.get("req");
		HttpServletResponse resp=(HttpServletResponse) sys.get("resp");
		
		String val=param.get("val");
		String src=param.get("src");
		String var=param.get("var");
		String model=param.get("model");
		if(!StringUtil.isEmpty(src)){
			Msg<ExeResourceVo> msg=new Msg<>();
			RuleBo.getByPath(src,msg);
			if(msg.isSuccess()){
				ExeResourceVo rule=msg.getV();
				val=rule.getCaiConfig();
			}
		}
		Object modelData=cmdHandle.getVars().get(model);
		Map<String,String> dataModel=new HashMap<String, String>();
		if(modelData!=null){
			dataModel.put(model, modelData.toString());
		}
		String data=tpl(val, cmdHandle.getVars());
		cmdHandle.getVars().put(var, data);
	}

	@Override
	public String op() {
		return null;
	}
	
	private String tpl(String tpl,Object model){
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		String firstTemplate = "firstTemplat";
		stringLoader.putTemplate(firstTemplate, tpl);
		
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
		cfg.setTemplateLoader(stringLoader);
		try {
			Template template = cfg.getTemplate(firstTemplate);
			StringWriter sw = new StringWriter();
			template.process(model, sw);
			logger.debug("{}",sw.getBuffer().toString());
			return sw.getBuffer().toString();
		} catch (Exception e) {
			logger.error("", e);
		} 
		return null;
	}
	
	public static void main(String[] args) {
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		String firstTemplate = "firstTemplat";
		stringLoader.putTemplate(firstTemplate, "Hello#set($name =\"velocity\") \n ${name}");
		
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
		cfg.setTemplateLoader(stringLoader);
		try {
			Template template = cfg.getTemplate(firstTemplate);
			StringWriter sw = new StringWriter();
			Map<String,String> dataModel=new HashMap<String, String>();
			dataModel.put("user", "admin");
			template.process(dataModel, sw);
			logger.debug(sw.getBuffer().toString());
		} catch (Exception e) {
			logger.error("", e);
		} 
	}
}
