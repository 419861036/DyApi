package kkd.common.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kkd.common.orm.MysqlSQLUtil;
/**
 * 基础dao
 * @author Administrator
 *
 */
public class MysqlDao extends BaseDao{
	
	public MysqlDao(String dsName) {
		super(dsName);
	}
	/**
	 * 根据对象插入操作
	 * 
	 * @param sql
	 * @return
	 */
	public Integer insert(Object o) {
//		String sql = MysqlSQLUtil.getInsertSql(o);
//		return insert(sql, null);
		return null;
	}

	

	/**
	 * 根据对象更新数据
	 * 
	 * @param sql
	 * @param o
	 *            更新对象
	 * @param where
	 *            更新条件
	 * @return
	 */
	public Integer updateObject(Object o, String where,List<Object> args) {
//		StringBuffer sql = new StringBuffer(MysqlSQLUtil.getUpdateSql(o));
//		sql.append(where);
//		String sqlStr=sql.toString();
//		return update(sqlStr, args);
		return null;
	}


	/**
	 * 根据sql 和Class 生成List<Bean>
	 * 
	 * @param sql
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findList(String sql,List<Object> args, final Class<T> c) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if(debug){
				System.out.println("findList:"+sql);
			}
			stmt = conn.prepareStatement(sql);
			setP(stmt, args);
			rs = stmt.executeQuery();
			return getList(rs, c);
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
	 * 根据结果集 和class 生成list
	 * 
	 * @param rs
	 * @param c
	 * @return
	 * @throws SQLException
	 */
	private static <T> List<T> getList(ResultSet rs, Class<T> c)
			throws SQLException {
		List<T> list = new ArrayList<T>();
		String sb = null;
		boolean flag = false;
		while (rs.next()) {
			if (!flag) {
				flag = true;
				sb = getColumns(rs);
			}
//			list.add(MysqlSQLUtil.getData(c, rs, sb));
		}
		return list;
	}

	/**
	 * 根据sql 和Class 生成bean
	 * 
	 * @param sql
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public <T> T findBean(String sql,List<Object> args ,final Class<T> c) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if(debug){
				System.out.println("findBean:"+sql);
			}
			stmt = conn.prepareStatement(sql);
			setP(stmt, args);
			rs = stmt.executeQuery();
			if (rs.next()) {
//				return MysqlSQLUtil.getData(c, rs, getColumns(rs));
				return null;
			}
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
	 * 分页查询
	 * 低性能
	 * @param sql
	 * @param pg
	 *            分页对象
	 * @param c
	 *            映射类型 ：UserBean.class
	 * @return
	 * @throws Exception
	 */
	public <T> PageList<T> paginationQuery(String sql,List<Object> args, Paginator pg,
			final Class<T> c) throws Exception {
		String execSql = null;
		Long totalNum = getTotalNum(sql);
		execSql = getExceSql(sql, pg);
		pg.setItems(totalNum);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if(debug){
				System.out.println("paginationQuery:"+execSql);
			}
			stmt = conn.prepareStatement(execSql);
			setP(stmt, args);
			rs = stmt.executeQuery();
			return new PageList<T>(getList(rs, c), pg);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
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

	
	@SuppressWarnings("unused")
	public PageList<Map<String, Object>> paginationQuery(String sql,List<Object> args,
			Paginator pg) throws Exception {
		String execSql = null;
		Long totalNum = getTotalNum(sql);
		execSql = getExceSql(sql, pg);
		pg.setItems(totalNum);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if(debug){
				System.out.println("paginationQuery:"+sql);
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
			map = new HashMap<String, Object>();
			map.put("ResultSet", rs);
			list.add(map);
			return new PageList<Map<String, Object>>(list, pg);
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
	 * 低性能
	 * 
	 * @param sb
	 * @param pg
	 * @return
	 */
	protected String getExceSql(String sql, Paginator pg) {
//		String countSql = "select count(*) c from ( " + sql + ") count";
		sql = sql + " limit " + pg.getStartIndex()+ "," + pg.getItemsPerPage();
		if(debug){
			System.out.println("count:"+sql);
		}
		return sql;
		
//		return "SELECT * FROM (SELECT A.*,ROWNUM RN FROM (" + sql
//				+ ") A WHERE ROWNUM<=" + pg.getEndIndex() + ") WHERE RN>="
//				+ pg.getStartIndex();
	}
	/**
	 * 统计查询总数
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected Long getTotalNum(String sql) throws Exception {
		String countSql = "SELECT COUNT(*) AS l FROM (" + sql + ") count";
		if(debug){
			System.out.println("count:"+countSql);
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if(debug){
				System.out.println("findBean:"+sql);
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(0);
			}
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
		return 0l;
	}
}
