package kkd.common.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kkd.common.exception.KKDException;
import kkd.common.util.StringUtil;

/**
 * 
 * @Filename DbUtils.java
 * 
 * @Description 连接操作类 
 * 支持分层事务
 * 
 * 
 */
public class DbUtils {
	
	private static ThreadLocal<List<ConnectionHandle>> suspendList=new ThreadLocal<List<ConnectionHandle>>();
	private static ThreadLocal<Map<String,ConnectionHandle>> tl = new ThreadLocal<Map<String,ConnectionHandle>>();
	
	/** 
	 * 根据数据库的Connection对象，并绑定到当前线程上 
	 * @return 成功，返回Connection对象，否则返回null 
	 */ 
	public static synchronized ConnectionHandle getConnection(String dsName,boolean transaction){
		if(StringUtil.isEmpty(dsName)){
			throw new KKDException("数据源名称不能为空！");
		}
		Map<String,ConnectionHandle> map=(Map<String,ConnectionHandle>)tl.get();
		if(map==null){
			map=new  HashMap<String, ConnectionHandle>();
		}
		ConnectionHandle chandle = map.get(dsName);
		 //先从当前线程上取出连接实例
		if(null == chandle){ //如果当前线程上没有Connection的实例 
			try {
				Connection conn = DataSourceFactory.getDataSource(dsName).getConnection(); // 从连接池中取出一个连接实例 
				chandle=new ConnectionHandle();
				chandle.setConn(conn);
				chandle.setTransaction(transaction);
				map.put(dsName, chandle);
				tl.set(map); //把它绑定到当前线程上
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return chandle;
	}
	
	/** 
	 * @param ds 数据源 默认不填
	 * 获取事务管理器 
	 * @return 事务管理实例 
	 */ 
	public static  TransactionManager getTranManager(TransactionDefinition td){
		TransactionManager tm=null;
		if(td.getPropagationBehavior()==TransactionDefinition.PROPAGATION_REQUIRED){
			tm=new TransactionManager(getConnection(td.getDsName(),true));
		}else if(td.getPropagationBehavior()==TransactionDefinition.PROPAGATION_REQUIRED){
			
		}
		if(tm!=null){
			tm.setTransactionIsolation(td.getIsolationLevel());
		}
		return tm;
	}
	/** 
	 * 关闭数据库连接，并卸装线程绑定 
	 * @param conn 要关闭数据库连接实例 
	 * @throws KKDException
	 */ 
	protected static void close(Connection conn,String dsName) throws KKDException{
		Map<String, ConnectionHandle> map=tl.get();
		try {
			if(map!=null&&!StringUtil.isEmpty(dsName)){
				ConnectionHandle ch=map.get(dsName);
				if(ch!=null&&conn!=null&&conn==ch.getConn()){
					map.remove(dsName);
					if(!conn.isClosed()){
						conn.close();
					}
				}
			}else{
				if(conn!=null&&!conn.isClosed()){
					conn.close();
				}
			}
		} catch (SQLException e) {
			throw new KKDException("关闭连接时出现异常",e);
		}finally{
			if( map.size()==0){
				tl.remove(); //卸装线程绑定
			}
		}
	}

	public static ThreadLocal<List<ConnectionHandle>> getSuspendList() {
		return suspendList;
	}

	public static void setSuspendList(
			ThreadLocal<List<ConnectionHandle>> suspendList) {
		DbUtils.suspendList = suspendList;
	}
	
}
