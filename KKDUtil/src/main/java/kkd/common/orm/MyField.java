package kkd.common.orm;


/**
 * 字段信息
 * @author Administrator
 *
 */
public class MyField {
	private String name;// 字段名称
	private String colName;// 列名
	private String type;// 字段类型
	private Class<?> typeClass;//字段类型class
	private String getM;// get方法名称
	private Integer getMIndex;//方法索引
	private String setM;// set方法名称
	private Integer setMIndex;//方法索引
	private Boolean ignore;//是否忽略
	private Boolean pk;//是否主键
	private Boolean rel;//是否来自其他表的数据
	private Boolean update;//更新字段
	private String seq;//sequence
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Class<?> getTypeClass() {
		return typeClass;
	}
	public void setTypeClass(Class<?> typeClass) {
		this.typeClass = typeClass;
	}
	public String getGetM() {
		return getM;
	}
	public void setGetM(String getM) {
		this.getM = getM;
	}
	public Integer getGetMIndex() {
		return getMIndex;
	}
	public void setGetMIndex(Integer getMIndex) {
		this.getMIndex = getMIndex;
	}
	public String getSetM() {
		return setM;
	}
	public void setSetM(String setM) {
		this.setM = setM;
	}
	public Integer getSetMIndex() {
		return setMIndex;
	}
	public void setSetMIndex(Integer setMIndex) {
		this.setMIndex = setMIndex;
	}
	public Boolean getIgnore() {
		return ignore;
	}
	public void setIgnore(Boolean ignore) {
		this.ignore = ignore;
	}
	public Boolean getPk() {
		return pk;
	}
	public void setPk(Boolean pk) {
		this.pk = pk;
	}
	public Boolean getRel() {
		return rel;
	}
	public void setRel(Boolean rel) {
		this.rel = rel;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public Boolean getUpdate() {
		return update;
	}
	public void setUpdate(Boolean update) {
		this.update = update;
	}
	
	
	
}
