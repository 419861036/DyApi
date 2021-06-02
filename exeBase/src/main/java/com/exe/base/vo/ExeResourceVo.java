package com.exe.base.vo;

import java.util.Date;
import java.util.Map;

import kkd.common.orm.Table_;

/**
 * 采集规则
 * @author tanbin
 *
 */
@Table_("exe_resource")
public class ExeResourceVo {
	private Integer id;//id
	private String path;//路径
	private String ruleName;//规则名称
	private String corn;//定时任务
	private String caiRule;//采集规则
	private String caiConfig;//采集配置
	private String loginRule;//登录规则
	private Map<String,SegVo> segMap;//页面动作片段
	
	private Date	createTime;//创建时间
	private Date	upTime;//更新时间
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getCorn() {
		return corn;
	}

	public void setCorn(String corn) {
		this.corn = corn;
	}

	public String getCaiRule() {
		return caiRule;
	}

	public void setCaiRule(String caiRule) {
		this.caiRule = caiRule;
	}

	public String getLoginRule() {
		return loginRule;
	}

	public void setLoginRule(String loginRule) {
		this.loginRule = loginRule;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpTime() {
		return upTime;
	}

	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}

	public Map<String, SegVo> getSegMap() {
		return segMap;
	}

	public void setSegMap(Map<String, SegVo> segMap) {
		this.segMap = segMap;
	}

	public String getCaiConfig() {
		return caiConfig;
	}

	public void setCaiConfig(String caiConfig) {
		this.caiConfig = caiConfig;
	}
	
	
}
