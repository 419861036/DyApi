package kkd.common.orm;

import java.util.ArrayList;
import java.util.List;
/**
 * 表信息
 * @author Administrator
 *
 */
public class MyBean {

	private String tableName;
	private List<MyField> fields = new ArrayList<MyField>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<MyField> getFields() {
		return fields;
	}

	public void setFields(List<MyField> fields) {
		this.fields = fields;
	}

}
