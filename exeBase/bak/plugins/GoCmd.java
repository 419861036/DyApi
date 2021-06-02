package com.exe.base.plugins;

import java.util.Map;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

public class GoCmd extends Cmd{
	@Override
	public String op() {
		return "go";
	}

	@Override
	public void exe(CmdVo caiAction, Map<String, String> param) {
		String name=param.get("to");
		cmdHandle.gotoNodeByName(caiAction,name);
//		Map<String, CaiSeg> segMap=caiRule.getSegMap();
//		for (Map.Entry<String, CaiSeg> ele : segMap.entrySet()) {
//			CaiSeg caiSeg=ele.getValue();
//			Map<String, CaiAction> actionMap=caiSeg.getActionMap();
//			for (Map.Entry<String, CaiAction> actionEle : actionMap.entrySet()) {
//				CaiAction caiActionTmp=actionEle.getValue();
//				if(name.equalsIgnoreCase(caiActionTmp.getName())){
//					//设置直接跳转动作
//					goAction=caiActionTmp;
//					return;
//				}
//			}
//		}
//		throw new RuntimeException("未找到下个节点："+name);
	}
	
	
}
