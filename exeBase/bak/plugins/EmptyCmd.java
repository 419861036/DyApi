package com.exe.base.plugins;

import java.util.Map;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

/**
 * 空节点
 * @author tanbin
 *
 */
public class EmptyCmd extends Cmd{

	@Override
	public void exe(CmdVo cmd, Map<String, String> param) {
		
	}

	@Override
	public String op() {
		return null;
	}

}
