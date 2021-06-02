package com.exe.base.vo;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;

//指令
public class CmdVo {
	private SegVo caiSeg;//段落
	private String op;//动作类型
	private String name;//动作名称
	private Integer order;//顺序
	private Integer delay;//延迟执行时间
	private Integer retry;//重试次数
	private String body;//body
	private JSONArray jsonBody;//json格式body
	private Map<String,String> param;//动作参数
	
	
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getParam() {
		return param;
	}
	public void setParam(Map<String, String> param) {
		this.param = param;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Integer getDelay() {
		return delay;
	}
	public void setDelay(Integer delay) {
		this.delay = delay;
	}
	public Integer getRetry() {
		return retry;
	}
	public void setRetry(Integer retry) {
		this.retry = retry;
	}
	public SegVo getCaiSeg() {
		return caiSeg;
	}
	public void setCaiSeg(SegVo caiSeg) {
		this.caiSeg = caiSeg;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public JSONArray getJsonBody() {
		return jsonBody;
	}

	public void setJsonBody(JSONArray jsonBody) {
		this.jsonBody = jsonBody;
	}
	
	
	
}
