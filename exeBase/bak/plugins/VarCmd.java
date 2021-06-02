package com.exe.base.plugins;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.util.DateUtil;
import kkd.common.util.StringUtil;
import kkd.common.util.UUIDUtil;

/**
 * 变量设置 或者 获取
 * @author tanbin
 *
 */
public class VarCmd extends Cmd{
	private final static Logger logger = LoggerFactory.getLogger(VarCmd.class);
	private String decode;
	@Override
	public void exe(CmdVo caiAction, Map<String, String> param) {
		String var=param.get("var");//设置变量
		decode=param.get("decode");//设置变量
		String init=param.get("init");
		String type=param.get("type");
		String key=param.get("key");//数据来源key
		String childKey=param.get("childKey");//通过子key获取
		String indexStr=param.get("index");//通过索引获取
		String size=param.get("size");//获取数组长度
		Integer index=indexStr==null?null:Integer.parseInt(indexStr);
		
		if(type!=null){
			if("uuid".equalsIgnoreCase(type)){
				cmdHandle.getVars().put(var, UUIDUtil.getUUID());
			}else if("date".equalsIgnoreCase(type)){
				String date=DateUtil.format(new Date(), DateUtil.DATETIME_PATTERN);
				cmdHandle.getVars().put(var, date);
			}
			return;
		}
		if(init!=null){
			cmdHandle.getVars().put(var, init);
			return;
		}
		
		Object data=getVal(cmdHandle.getVars(),key);
		if(data==null){
			cmdHandle.getVars().put(var, null);
			return;
		}
		
		//获取数组长度
		if(size!=null){
			if(data instanceof List){
				List<?> list=(List<?>) data;
				cmdHandle.getVars().put(var,list==null?0: list.size());
			}else if(data.getClass().isArray() ){
				Object[] list=(Object[]) data;
				cmdHandle.getVars().put(var,list==null?0: list.length);
			}
			return;
		}
		
		if(data instanceof List){
			List<?> list=(List<?>) data;
			if(index<list.size()){
				data=list.get(index);
			}else{
				data=null;
			}
		}else if(data.getClass().isArray() ){
			Object[] list=(Object[]) data;
			if(index<list.length){
				data=list[index];
			}else{
				data=null;
			}
		}
		if(data==null){
			cmdHandle.getVars().put(var, null);
		}else if(data instanceof Map){
			Map<String,Object> map=(Map) data;
			Object obj=map.get(childKey);
			cmdHandle.getVars().put(var, obj);
		}else if(StringUtil.isEmpty(childKey)){
			cmdHandle.getVars().put(var, data);
		}else{
			try {
				Method m=data.getClass().getMethod(childKey);
				Object obj=m.invoke(data);
				cmdHandle.getVars().put(var, obj);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		
	}

	private Object getVal(Map<String, Object> vars, String string) {
		if(string.contains(".")){
			String[] arr=string.split("\\.");
			Object obj=null;
			for (String string2 : arr) {
				if(obj!=null){
					if(obj instanceof Map){
						Map<String,Object> map=(Map) obj;
						obj=map.get(string2);
						//是否解码
						if("true".equalsIgnoreCase(decode)){
							if(obj instanceof String){
								try {
									obj=URLDecoder.decode((String)obj, "utf-8");
								} catch (Exception e) {
									logger.error("", e);
								}
							}
						}
						return obj;
					}
				}else{
					obj=vars.get(string2);
					//是否解码
					if("true".equalsIgnoreCase(decode)){
						if(obj instanceof String){
							try {
								obj=URLDecoder.decode((String)obj, "utf-8");
							} catch (Exception e) {
								logger.error("", e);
							}
						}
					}
				}
			}
		}else{
			return vars.get(string);
		}
		return null;
	}
	
	@Override
	public String op() {
		return null;
	}

}
