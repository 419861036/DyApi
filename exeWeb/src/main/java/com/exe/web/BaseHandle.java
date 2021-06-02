package com.exe.web;

import com.alibaba.fastjson.JSON;
import com.exe.base.CmdHandle;
import com.exe.base.util.GroovyUtil;
import com.exe.base.vo.ExeResourceVo;
import com.exe.bo.RuleBo;
import com.exe.param.RuleParam;
import kkd.common.entity.Msg;
import kkd.common.exception.KKDValidationException;
import kkd.common.util.StringUtil;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseHandle extends AbstractHandler{
	private final static Logger logger = LoggerFactory.getLogger(BaseHandle.class);
	private static List<ExeResourceVo> taskList;
	private static List<ExeResourceVo> apiList;
	private static List<ExeResourceVo> filterList;
	private static Map<String,ExeResourceVo> apiMapResource;
	
	 @Override
     public void handle(String target, Request baseRequest, 
             HttpServletRequest request, HttpServletResponse response)
             throws IOException, ServletException {

		 try {
			 String c=request.getParameter("c");
			 if(apiList ==null){
				 apiList =initData("api");
				 filterList=initData("filter");
				 iniMapData();
				 taskList = initData("task");
			 }else if(!StringUtil.isEmpty(c)){
				 if("true".equalsIgnoreCase(c)||
				 "all".equalsIgnoreCase(c)
				 ){
					GroovyUtil.clear();
					Init.regPlugins();
					apiList =initData("api");
					iniMapData();
					filterList=initData("filter");
					taskList = initData("task");
				}else if("rule".equalsIgnoreCase(c)){
					apiList =initData("api");
					iniMapData();
					 filterList=initData("filter");
				}else if("groovy".equalsIgnoreCase(c)){
					GroovyUtil.clear();
				}
			}
			 logger.debug("执行开始:");
			 long l1=System.currentTimeMillis();
			 long l=System.nanoTime();

			 List<ExeResourceVo> filterList=getFilterRule(request, response);
			 for(int i=0;filterList!=null && i<filterList.size();i++){
				 ExeResourceVo rule=filterList.get(i);
				 CmdHandle hanle=new CmdHandle();
				 hanle.init();
				 if(rule!=null){
					 hanle.setCaiRule(rule);
					 hanle.getSys().put("req", request);
					 hanle.getSys().put("resp", response);
					 hanle.start();
					 String return1= (String) hanle.getSys().get("return");
					 if("true".equalsIgnoreCase(return1)){
					 	return;
					 }
					 logger.debug("执行完成,耗时：{}",(System.nanoTime()- l));
					 logger.debug("执行完成,耗时：{}",(System.currentTimeMillis()- l1));
				 }else{
					 response.setStatus(HttpServletResponse.SC_OK);
					 response.setContentType("text/html; charset=utf-8");
					 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					 response.getWriter().write("page is not found");
				 }
			 }

			 apiExe(request, response, l1, l);
		 } catch (KKDValidationException e) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/html; charset=utf-8");
			Msg<Object> rs=new Msg<Object>();
			rs.setCode(e.getCode());
			rs.setMsg(e.getMessage());
			response.getWriter().write(rs.objectToFastJSON());
			logger.error("", e);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/html; charset=utf-8");
			Msg<Object> rs=new Msg<Object>();
			rs.setCode(Msg.ERROR);
			rs.setMsg(e.getMessage());
			response.getWriter().write(rs.objectToFastJSON());
			logger.error("", e);
		}finally{
			baseRequest.setHandled(true);
		}
     }

	private List<ExeResourceVo> getFilterRule(HttpServletRequest request, HttpServletResponse response) {
		List<ExeResourceVo> list=new ArrayList<>();
		String pathinfo=request.getPathInfo();
		if("/user/login".equals(pathinfo)){
			return list;
		}
	 	for(int i=0;i<filterList.size();i++){
			ExeResourceVo exeResourceVo=filterList.get(i);
			String pathRule=exeResourceVo.getPath();
			Pattern r = Pattern.compile(pathRule);
			Matcher m = r.matcher(pathinfo);
			if(m.find()){
				list.add(exeResourceVo);
			}
		}
	 	return list;
	}

	/**
	 * api执行
	 * @param request
	 * @param response
	 * @param l1
	 * @param l
	 * @throws IOException
	 */
	private void apiExe(HttpServletRequest request, HttpServletResponse response, long l1, long l) throws IOException {
		CmdHandle hanle=new CmdHandle();
		hanle.init();
		ExeResourceVo rule=getRule(request, response);
		//threadLocal.set(rule.getId().toString());
		if(rule!=null){
			hanle.setCaiRule(rule);
			hanle.getSys().put("req", request);
			hanle.getSys().put("resp", response);
			hanle.start();
			logger.debug("执行完成,耗时：{}",(System.nanoTime()- l));
			logger.debug("执行完成,耗时：{}",(System.currentTimeMillis()- l1));
		}else{
		   response.setStatus(HttpServletResponse.SC_OK);
		   response.setContentType("text/html; charset=utf-8");
		   response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		   response.getWriter().write("page is not found");
		}
	}


	public List<ExeResourceVo> initData(String type) {
		Msg<List<ExeResourceVo>> msg=new Msg<>();
		RuleParam param=new RuleParam();
		param.setType(type);
		RuleBo.list(param,msg);
		return msg.getV();
	}

	private void iniMapData() {
		apiMapResource =new HashMap<>();
		for (ExeResourceVo exeResourceVo : apiList) {
			apiMapResource.put(exeResourceVo.getPath(), exeResourceVo);
		}
	}
	 
	private ExeResourceVo getRule(HttpServletRequest request, HttpServletResponse response) {
		String pathinfo=request.getPathInfo();
		return apiMapResource.get(pathinfo);
		// for (ExeResourceVo ruleVo : list) {
		// 	String path=ruleVo.getPath();
		// 	if(pathinfo.equalsIgnoreCase(path)){
		// 		return ruleVo;
		// 	}
		// }
		// return null;
	}

	public List<ExeResourceVo> getList() {
		return apiList;
	}

	public void setList(List<ExeResourceVo> list) {
		this.apiList = list;
	}


	public static List<ExeResourceVo> getTaskList() {
		return taskList;
	}

	public static void setTaskList(List<ExeResourceVo> taskList) {
		BaseHandle.taskList = taskList;
	}
}
