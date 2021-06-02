package com.exe.base.vo;

import java.util.Map;

public class SegVo {
	private String name;
	private Integer order;
	private Map<String,CmdVo> actionMap;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, CmdVo> getActionMap() {
		return actionMap;
	}
	public void setActionMap(Map<String, CmdVo> actionMap) {
		this.actionMap = actionMap;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
}
