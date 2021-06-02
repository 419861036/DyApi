package kkd.common.dao;

import java.sql.Connection;

/**
 * 链接辅助类
 * @author Administrator
 *
 */
public class ConnectionHandle {
	
	private String dsName;
	
	private Connection conn;
	/**
	 * 是否开启事务
	 */
	private boolean transaction;
	
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public boolean isTransaction() {
		return transaction;
	}
	public void setTransaction(boolean transaction) {
		this.transaction = transaction;
	}
	public String getDsName() {
		return dsName;
	}
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
}
