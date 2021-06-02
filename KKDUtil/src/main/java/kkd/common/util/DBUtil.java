//package kkd.common.util;
//
//import java.sql.SQLException;
//import java.util.List;
//
//import javax.sql.RowSet;
//
//import kkd.common.dao.PageList;
//import kkd.common.dao.Paginator;
//import ddb.dbutil.SQLDAO;
//import ddb.dbutil.SQLParameter;
///**
// * 数据操作辅助类
// * @author Administrator
// *
// */
//public class DBUtil {
//	
//	private static SQLDAO sqlDao = SQLDAO.getInstance();
//	
//	/**
//	 * 分页查询
//	 * @param <T>
//	 * @param sql sql语句
//	 * @param params 参数
//	 * @param pg 分页对象
//	 * @param rsi RowSet转对象辅助类
//	 * @return 分页对象
//	 * @throws SQLException
//	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static <T> PageList<T> query(String sql, SQLParameter[] params, Paginator pg,
//			RowSetImpl rsi) throws SQLException {
//		RowSet rs = sqlDao.executeQuery(getExceSql(sql, pg), params);
//		pg.setItems(getTotalNum(sql, params));
//		List<T> list = rsi.getList(rs);
//		return new PageList(list, pg);
//	}
//	/**
//	 * 获得执行sql
//	 * @param sql
//	 * @param pg
//	 * @return
//	 */
//	private static String getExceSql(String sql, Paginator pg) {
//		sql = sql + " limit " + pg.getStartIndex() + "," + pg.getItemsPerPage();
//		return sql;
//	}
//	/**
//	 * 获得总条数
//	 * @param sql
//	 * @param params
//	 * @return
//	 * @throws SQLException
//	 */
//	private static long getTotalNum(String sql, SQLParameter[] params)
//			throws SQLException {
//		String countSql = "SELECT COUNT(*) AS l FROM (" + sql + ") count";
//		RowSet rs = sqlDao.executeQuery(countSql, params);
//		if(rs.next()){
//			return rs.getLong("l");
//		}else{
//			return 0l;
//		}
//	}
//
//	public interface RowSetImpl<T> {
//		List<T> getList(RowSet rs);
//	}
//
//	public static void main(String[] args) {
////		Paginator p = new Paginator(1, 10);
////		RowSetImpl rsi = new RowSetImpl() {
////			public List<Object> getList(RowSet rs) {
////				return null;
////			}
////		};
////		try {
////			DBUtil.query("select * from test ", null, p, rsi);
////		} catch (SQLException e) {
////			LogWriter.error("",e);
////		}
////		SQLDAO dao=SQLDAO.getInstance();
//	}
//}
