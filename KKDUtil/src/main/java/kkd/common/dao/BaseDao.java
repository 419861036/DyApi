package kkd.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库无关功能封装
 * @author Administrator
 *
 */
public class BaseDao {
	
	public final static  boolean debug=false;
	protected Connection conn=null;
	protected ConnectionHandle handle=null;
	
	public BaseDao(String dsName) {
		handle=DbUtils.getConnection(dsName,false);
		conn=handle.getConn();
	}
	
	/**
	 * 根据sql插入操作
	 * 
	 * @param sql
	 * @return
	 */
	public Integer insert(String sql, List<Object> args) {
		PreparedStatement stmt = null;
		try {
			if(debug){
				System.out.println("insert:"+sql);
			}
			stmt = conn.prepareStatement(sql);
			setP(stmt, args);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			close(handle);
		}
		return null;
	}

	protected void close(ConnectionHandle handle) {
		if(handle!=null&&!handle.isTransaction()&&conn!=null){
			try {
				if(conn!=null&&!conn.isClosed()){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 执行sql
	 * 
	 * @param sql
	 * @return
	 */
	public Boolean execute(String sql, List<Object> args) {
		PreparedStatement stmt = null;
		try {
			if(debug){
				System.out.println("execute:"+sql);
			}
			stmt = conn.prepareStatement(sql);
			setP(stmt, args);
			return stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			close(handle);
		}
		return null;
	}
	/**
	 * 根据sql更新数据
	 * 
	 * @param sql
	 * @return
	 */
	public Integer update(String sql, List<Object> args) {
		PreparedStatement stmt = null;
		try {
			if(debug){
				System.out.println("update:"+sql);
			}
			stmt = conn.prepareStatement(sql);
			setP(stmt, args);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			close(handle);
		}
		return null;
	}
	/**
	 * 查询
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> find(String sql,List<Object> args) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if(debug){
				System.out.println("find:"+sql);
			}
			stmt = conn.prepareStatement(sql);
			setP(stmt, args);
			rs = stmt.executeQuery();
			Map<String, Object> map = null;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				ResultSetMetaData met = rs.getMetaData();
				map = new HashMap<String, Object>();
				for (int i = 1; i <= met.getColumnCount(); i++) {
					String columnName = met.getColumnName(i);
					map.put(columnName, rs.getObject(i));
				}
				list.add(map);
			}
			list.add(map);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				rs.close();
			}
			close(handle);
		}
		return null;
	}
	/**
	 * 拼装 列名 格式 ： ,id,name,desc,
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static String getColumns(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= count; i++) {
			sb.append(",");
			sb.append(rsmd.getColumnLabel(i));
			if (i == count) {
				sb.append(",");
			}
		}
		return sb.toString().toUpperCase();
	}
	

	
	/**
	 * 设置参数
	 * 
	 * @param stmt
	 * @param args
	 * @throws SQLException
	 */
	protected static void setP(PreparedStatement stmt, List<Object> args)
			throws SQLException {
		if (args != null) {
			for (int i = 0; i < args.size(); i++) {
				Object object = args.get(i);
				if (object instanceof Integer) {
					stmt.setInt(i + 1, (Integer) object);
				} else if (object instanceof String) {
					stmt.setString(i + 1, (String) object);
				} else if (object instanceof Double) {
					stmt.setDouble(i + 1, (Double) object);
				} else if (object instanceof Float) {
					stmt.setFloat(i + 1, (Float) object);
				} else if (object instanceof Short) {
					stmt.setShort(i + 1, (Short) object);
				}else{
					stmt.setObject(i + 1, object);
				}
			}
		}
	}
}
