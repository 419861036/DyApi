package kkd.common.dao;
/**
 * 事务定义
 * @author Administrator
 *
 */
public class TransactionDefinition {
	
	public static final int PROPAGATION_REQUIRED = 0;
//	public static final int PROPAGATION_SUPPORTS = 1;
//	public static final int PROPAGATION_MANDATORY = 2;
//	public static final int PROPAGATION_REQUIRES_NEW = 3;
//	public static final int PROPAGATION_NOT_SUPPORTED = 4;
//	public static final int PROPAGATION_NEVER = 5;
//	public static final int PROPAGATION_NESTED = 6;
	
	public static final int ISOLATION_READ_UNCOMMITTED = 1;
	public static final int ISOLATION_READ_COMMITTED = 2;
	public static final int ISOLATION_REPEATABLE_READ = 4;
	public static final int ISOLATION_SERIALIZABLE = 8;

	private int propagationBehavior = 0;
	private int isolationLevel = ISOLATION_READ_UNCOMMITTED;
	private String dsName;
	
	public TransactionDefinition(String dsName) {
		this.dsName=dsName;
	}
	public int getPropagationBehavior() {
		return propagationBehavior;
	}
	public void setPropagationBehavior(int propagationBehavior) {
		this.propagationBehavior = propagationBehavior;
	}
	public int getIsolationLevel() {
		return isolationLevel;
	}
	public void setIsolationLevel(int isolationLevel) {
		this.isolationLevel = isolationLevel;
	}
	public String getDsName() {
		return dsName;
	}
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
}
