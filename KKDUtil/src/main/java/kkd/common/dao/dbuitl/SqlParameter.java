package kkd.common.dao.dbuitl;

/**
 * @author Administrator
 *
 */
public class SqlParameter {
	
	/**
	 * type sql 类型 对应 java.sql.Types
	 * value 参数值
	 * output 标记存储过程的输出参数
	 */
	private int type;
	private Object value;
	private boolean output=false;
	
	public SqlParameter(int type,Object vObject) {
		this.type=type;
		this.value=vObject;
	}
	public SqlParameter(int type,Object vObject,boolean output) {
		this.type=type;
		this.value=vObject;
		this.output=output;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean getOutput() {
		return output;
	}

	public void setOutput(boolean output) {
		this.output = output;
	}
	
}
