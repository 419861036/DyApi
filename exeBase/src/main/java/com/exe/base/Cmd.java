package com.exe.base;


import java.util.Map;

import com.exe.base.vo.CmdVo;


public abstract class Cmd {
	
	protected CmdHandle cmdHandle;
	
	
	public abstract void exe(CmdVo cmd,Map<String,String> param);
	
	public String opName;//命令
	public abstract String op();//命令

}
