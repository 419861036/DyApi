package com.exe.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.orm.Rel_;
import kkd.common.orm.Table_;
import kkd.common.orm.Transient_;

/**
 * 采集字段描述
 * 
 * @author tanbin
 *
 */
@Table_("cai_field")
public class RuleField implements Cloneable {
	private final static Logger logger = LoggerFactory.getLogger(RuleField.class);
	private Integer id;
	private Integer ruleId;
	private Integer pageId;
	private String lev;
	private Integer levId;
	private String path;
	@Transient_
	private String var;//上下文的key
	@Transient_
	private String to;//跳转到下个节点
	// 名称
	private String name;
	@Rel_
	private String handle;// 处理类型 css选择器 groovy
	@Rel_
	private String dataType;// 文本、集合、
	// 描述
	@Rel_
	private String desc;
	// 选择器
	@Rel_
	private String cssSelector;
	// 属性
	@Rel_
	private String attr;
	@Rel_
	private String codes;// groovy代码
	@Rel_
	private Map<String, RuleField> fieldMap;
	private String data;// 数据内容
	private Date createTime;
	private Date upTime;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCssSelector() {
		return cssSelector;
	}

	public void setCssSelector(String cssSelector) {
		this.cssSelector = cssSelector;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public Map<String, RuleField> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, RuleField> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getLev() {
		return lev;
	}

	public void setLev(String lev) {
		this.lev = lev;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	
	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	
	public Integer getLevId() {
		return levId;
	}

	public void setLevId(Integer levId) {
		this.levId = levId;
	}
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	
	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public RuleField newCaiRuleField(){
		RuleField newField=new RuleField();
		newField.setTo(to);
		newField.setVar(var);
		return newField;
	}

	public Object clone() {
		try {
			RuleField newfield=(RuleField)super.clone();
			Map<String, RuleField> newFieldMap=new HashMap<String, RuleField>();
			for (Map.Entry<String, RuleField> ele : fieldMap.entrySet()) {
				String key=ele.getKey();
				RuleField  field=ele.getValue();
				RuleField  child=(RuleField) field.clone();
				newFieldMap.put(key, child);
			}
			newfield.setFieldMap(newFieldMap);
			return newfield;
		} catch (CloneNotSupportedException e) {
			logger.error("", e);
		}
		return null;
	}

}
