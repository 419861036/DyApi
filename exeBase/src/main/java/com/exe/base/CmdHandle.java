package com.exe.base;


import java.util.HashMap;
import java.util.Map;

import com.exe.base.vo.CmdVo;
import com.exe.base.vo.ExeResourceVo;
import com.exe.base.vo.SegVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.util.StringUtil;
import kkd.common.util.exp.CodeInfo;
import kkd.common.util.exp.Formatter;


/**
 * 执行命令
 * @author tanbin
 *
 */
public class CmdHandle {
	private final static Logger logger = LoggerFactory.getLogger(CmdHandle.class);
	private CmdVo currentCmd;//当前命令
	private CmdVo goCmd;//跳转命令
	private boolean ret;//返回但不退出
	private ExeResourceVo caiRule;//采集规则
	private Map<String, Object> vars;//上下文数据
	private Map<String, Object> sys;//系统变量
	private static Map<String,Class<?>> cmdMap=null;
	private static boolean init=false;
	
	public static void clearCmd(){
		cmdMap=null;
	}
	/**
	 * 注册插件
	 */
	public static void register(String key,Class cls){
		if(cmdMap==null){
			cmdMap=new HashMap<>();
		}
		key=key.replace("Cmd", "");
		cmdMap.put(key, cls);
	}
	public static Class<?> getCmd(String key){
		if(cmdMap!=null){
			return cmdMap.get(key);
		}
		return null;
	}

	public void init(){
		if(cmdMap==null){
			cmdMap=new HashMap<>();
		}
		if(sys==null){
			sys=new HashMap<>();
		}
		if(vars==null){
			vars=new HashMap<>();
		}
		
		
		if(!init){
			/**
			 * 基础命令
			 */
			// cmdMap.put("go", GoCmd.class);
			// //空节点
			// cmdMap.put("empty", EmptyCmd.class);
			// //变量设置 与获取
			// cmdMap.put("var", VarCmd.class);
			// //计数器器
			// cmdMap.put("count", CountCmd.class);
			// //打印数据
			// cmdMap.put("print", PrintCmd.class);
			// //条件判断
			// cmdMap.put("if", IfCmd.class);
			// //字符串分割成数组
			// cmdMap.put("split", SplitCmd.class);
			
			init=true;
			
		}
		
	}
	
	/**
	 * 开始执行
	 */
	public void start(){
		init();
		CmdVo cmd=null;
		do{
			long l=System.currentTimeMillis();
			cmd=getNext();
			if(cmd==null){
				break;
			}
			
			Integer retry=cmd.getRetry();
			retry=retry==null?0:retry;
			boolean canRetry=false;
			do{
				try {
					
					try {
						logger.debug("当前命令：{} 耗时1：{}",cmd.getName(),(System.currentTimeMillis()-l));
						Integer delay=cmd.getDelay();
						delay=delay==null?0:delay;
						if(delay>0){
							Thread.sleep(delay);
						}
					} catch (InterruptedException e) {
						logger.error("", e);
					}
					cmd(cmd);
					logger.debug("当前命令完成,耗时：{}",(System.currentTimeMillis()-l));
				
				} catch (RuntimeException e) {
					Object id=caiRule.getId();
					Object name=caiRule.getRuleName();
					Object cmdName=cmd.getName();
					logger.error("--------error-------id:"+id+",name="+name+",cmdName="+cmdName+"-----");
					throw e;
				} catch (Exception e) {
					//发生异常开启重试机制
					canRetry=true;
					Object id=caiRule.getId();
					Object name=caiRule.getRuleName();
					Object cmdName=cmd.getName();
					logger.error("--------error-------id:"+id+",name="+name+",cmdName="+cmdName+"-----");
					logger.error("", e);
				}finally{
					if(canRetry){
						logger.debug("剩余次数：{}",retry);
						retry--;
						if(retry>=0){
							canRetry=true;
						}else{
							canRetry=false;
						}
					}
				}
			}while(canRetry);
			
		}while(cmd!=null);
	}

	/**
	 * 获取下个节点
	 * @return
	 */
	private CmdVo getNext() {
		//直接返回但不关闭
		if(ret){
			return null;
		}
		if(goCmd!=null){
			currentCmd=goCmd;
			goCmd=null;
			return currentCmd;
		}
		Map<String, SegVo> segMap=caiRule.getSegMap();
		boolean findCurrent=false;
		for (Map.Entry<String, SegVo> ele : segMap.entrySet()) {
			SegVo caiSeg=ele.getValue();
			if(currentCmd==null){
				Map<String, CmdVo> actionMap=caiSeg.getActionMap();
				for (Map.Entry<String, CmdVo> actionEle : actionMap.entrySet()) {
					CmdVo caiAction=actionEle.getValue();
					currentCmd=caiAction;
					return caiAction;
				}	
			}else if(caiSeg.getName().equalsIgnoreCase(currentCmd.getCaiSeg().getName())){
				//next只能在当前片段跳转  
				Map<String, CmdVo> actionMap=caiSeg.getActionMap();
				for (Map.Entry<String, CmdVo> actionEle : actionMap.entrySet()) {
					CmdVo caiAction=actionEle.getValue();
					if(findCurrent){
						//上次找到节点 直接返回下个节点 作为当前节点
						return caiAction;
					}
					//如果不存在当前节点  直接去 第一个命令执行
					if(currentCmd==null){
						currentCmd=caiAction;
						return caiAction;
					}else{
						//默认根据当前节点 找到相近节点 作为下个节点
						if(currentCmd.equals(caiAction)){
							//找到当前节点
							findCurrent=true;
							continue;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 执行命令
	 * @param caiAction
	 */
	public void cmd(CmdVo caiAction){
		this.currentCmd=caiAction;
		String cmd=caiAction.getOp();
		Map<String,String> param=caiAction.getParam();
		parseParam(param);
		//printParam(param);
		Class<?> cls=cmdMap.get(cmd);
		try {
			if(cls!=null){
				Cmd cmdObj=(Cmd) cls.newInstance();
				cmdObj.cmdHandle=this;
				cmdObj.exe(caiAction, param);
			}else{
				throw new RuntimeException("cmd:"+cmd+"is not found");
			}
			
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("", e);

		}
	}
	


	private void printParam(Map<String, String> param) {
		for (Map.Entry<String, String> ele : param.entrySet()) {
			logger.debug("--参数:{}:{}",ele.getKey(),ele.getValue());
		}
	}

	/**
	 * 解析参数：
	 * 例如：<action op="open"  name="点击评论下一页" :url="" />
	 * 所有属性称为参数，:url 带冒号标识里面包含表达式，需要重上下文获取数据并替换。
	 * :url="http://www.baidu.com?page=${page}" 上下文：page=1
	 * 替换后：url="http://www.baidu.com?page=1"
	 * @param param
	 */
	private void parseParam(Map<String, String> param) {
		Object[] keys=param.keySet().toArray();
		for (Object object : keys) {
			String key=object.toString();
			String format=param.get(key);
			//是否冒号开始
			if(StringUtil.indexOf(key, ":")==0){
				String newKey=key.substring(1);
				StringBuilder sb=new StringBuilder();
				Formatter f=new Formatter();
				f.setToCase(true);
				f.setPutCodeInfo(true);
				f.setPutPosCode(true);
				f.parse(format, sb, null);
				Map<Integer, CodeInfo>  codeMap=f.getCodeInfoMap();
				Map<Integer,String> replaceMap=new HashMap<>();
				for (Map.Entry<Integer, CodeInfo> codeEle : codeMap.entrySet()) {
					String dataKey=codeEle.getValue().getKey();
					
					Object obj=getVal(dataKey);
					if(obj!=null){
						replaceMap.put(codeEle.getKey(), obj.toString());
					}else{
						replaceMap.put(codeEle.getKey(), "");
					}
				}
				String data=f.updateFormat(format, codeMap, replaceMap);
				if(!StringUtil.isEmpty(data)){
					param.put(newKey, data);
				}else{
					param.put(newKey, "");
				}
			}
		}
	}

	private Object getVal(String string) {
		if(string.contains(".")){
			String[] arr=string.split("\\.");
			Object obj=null;
			for (String string2 : arr) {
				if(obj!=null){
					if(obj instanceof Map){
						Map<String,Object> map=(Map) obj;
						return obj=map.get(string2);
					}
				}else{
					obj=vars.get(string2);
				}
			}
		}else{
			return vars.get(string);
		}
		return null;
	}

	public ExeResourceVo getCaiRule() {
		return caiRule;
	}

	public void setCaiRule(ExeResourceVo caiRule) {
		this.caiRule = caiRule;
	}

	public Map<String, Object> getVars() {
		return vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}
	
	public CmdHandle newCmd(){
		CmdHandle cmd=new CmdHandle();
		cmd.setCaiRule(caiRule);
		return cmd;
	}
	
	public void setVar(String key,Object value){
		if(vars==null){
			vars=new HashMap<>();
		}
		vars.put(key, value);
	}
	//goto 根据 名称查找节点
//		public void gotoNodeByName(String name){
//			Map<String, CaiSeg> segMap=caiRule.getSegMap();
//			for (Map.Entry<String, CaiSeg> ele : segMap.entrySet()) {
//				CaiSeg caiSeg=ele.getValue();
//				Map<String, CaiAction> actionMap=caiSeg.getActionMap();
//				for (Map.Entry<String, CaiAction> actionEle : actionMap.entrySet()) {
//					CaiAction caiAction=actionEle.getValue();
//					if(name.equalsIgnoreCase(caiAction.getName())){
//						goAction=caiAction;
//						return;
//					}
//				}
//			}
//			throw new RuntimeException("未找到下个节点："+name);
//		}

	/**
	 * 节点跳转
	 * @param to
	 */
	public void gotoNodeByName(CmdVo caiAction,String to) {
		String name=to;
		Map<String, SegVo> segMap=caiRule.getSegMap();
		for (Map.Entry<String, SegVo> ele : segMap.entrySet()) {
			SegVo caiSeg=ele.getValue();
			String segName=ele.getKey();
			if(name.equalsIgnoreCase(segName)){
				//如果找到seg name 那么直接找到当前seg下面的第一个命令节点
				Map<String, CmdVo> actionMap=caiSeg.getActionMap();
				for (Map.Entry<String, CmdVo> actionEle : actionMap.entrySet()) {
					CmdVo caiActionTmp=actionEle.getValue();
					goCmd=caiActionTmp;
					return;
				}
			}else{
				//段落必须相等
				if(segName.equalsIgnoreCase(caiAction.getCaiSeg().getName())){
					Map<String, CmdVo> actionMap=caiSeg.getActionMap();
					for (Map.Entry<String, CmdVo> actionEle : actionMap.entrySet()) {
						CmdVo caiActionTmp=actionEle.getValue();
						if(name.equalsIgnoreCase(caiActionTmp.getName())){
							//设置直接跳转命令
							goCmd=caiActionTmp;
							return;
						}
					}
				}
			}
			
		}
		throw new RuntimeException("未找到下个节点："+name);
	}
	
	public void returnCmd(){
		ret=true;
	}

	public Map<String, Object> getSys() {
		return sys;
	}

	public void setSys(Map<String, Object> sys) {
		this.sys = sys;
	}

	

	
	
}
