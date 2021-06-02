package kkd.common.dao.dbuitl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

import kkd.common.dao.PageList;
import kkd.common.dao.Paginator;
import kkd.common.orm.MysqlSQLUtil;

/**
 * 数据库操作类
 * @author tanbin
 *
 */
public class JDBC {
	
	private DbHelper dh;//数据库操作辅助类
	private static DataSourceProvider defaultDsp;//默认数据源提供
	private static String defaultKey;
	private static boolean debug=false;
	
	public JDBC(){
		this.dh=new DbHelper();
	}
	
	public static void setDebug(boolean debug1){
		debug=debug1;
	}
	/**
	 * 执行方法
	 * 支持事务
	 * @param mb
	 * @return
	 */
	public final Object execute(MyBack mb){
		try {
			this.dh.isTransaction=true;
			this.dh.beginTransaction();
			Object o=mb.exe(dh);
			this.dh.commit();
			return o;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(dh.dbConn!=null){
					if(!dh.dbConn.getAutoCommit()){
						dh.dbConn.rollback();
						dh.dbConn.setAutoCommit(true);
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
		}finally{
			closeConnectionDirct(dh.dbConn);
		}
	}
	
	/**
	 * 直接关闭
	 * @param conn
	 */
	private void closeConnectionDirct(Connection conn){
		try {
			if(conn!=null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取没有事务的 dbHelper
	 * @return
	 */
	public static DbHelper getNoTransactionDbHelper(DataSourceProvider dsp,String key){
		DbHelper dh=new DbHelper();
		dh.isTransaction=false;
		if(dsp==null ||key==null){
			throw new RuntimeException("请提供数据源提供者");
		}
		dh.dsp=dsp;
		dh.key=key;
		return  dh;
	}
	
	/**
	 * 获取没有事务的 dbHelper
	 * @return
	 */
	public static DbHelper getDefaultNoTransactionDbHelper(){
		DbHelper dh=new DbHelper();
		dh.isTransaction=false;
		return  dh;
	}
	
	/**
	 * 支持事务并手动管理
	 * @return
	 */
	public static DbHelper getDefaultTransactionDbHelper(){
		DbHelper dh=new DbHelper();
		dh.isTransaction=false;
		return  dh;
	}
	
	/**
	 * 设置数据源提供者
	 * 最好单例
	 * @param dsp
	 * @param key
	 */
	public void setDataSourceProvider(DataSourceProvider dsp,String key) {
		this.dh.dsp=dsp;
		this.dh.key=key;
	}
	
	
	/**
	 * 设置默认数据源提供者
	 * 最好单例
	 * @param dsp
	 * @param key
	 */
	public static void setDefultDataSourceProvider(DataSourceProvider dsp,String key){
		defaultDsp=dsp;
		defaultKey=key;
	}
	
	/**
	 * 是否设置了默认数据源
	 * @return
	 */
	public static boolean hasDefultDataSourceProvider(){
		if(defaultDsp!=null  && defaultKey !=null ){
			return true;
		}
		return false;
	}
	
	public static  class DbHelper {
		
		private DataSourceProvider dsp;
		private  String key;
		private Connection dbConn;
		private boolean isTransaction;
		private DbHelper(){}
		
		private Connection getConnection() throws SQLException{
			if(isTransaction){
				if(dbConn==null||(dbConn!=null&& dbConn.isClosed())){
					dbConn=getConnectionByDsp();
					dbConn.setAutoCommit(false);
				}
				return dbConn;
			}else{
				return getConnectionByDsp();
			}
		}
		
		/**
		 * 获取数据库连接通过提供器
		 */
		private Connection getConnectionByDsp() {
			try {
				if(dsp!=null&& key !=null){
					Connection connection=dsp.getDataSource(key).getConnection();
					connection.setAutoCommit(true);
					return connection;
				}else if(defaultDsp!=null&& defaultKey !=null){
					Connection connection=defaultDsp.getDataSource(defaultKey).getConnection();
					connection.setAutoCommit(true);
					return connection;
				}else{
					throw new RuntimeException("没有默认的数据源提供者");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		private void closeStatement(Statement ps){
			try {
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void closeConnection(Connection conn){
			try {
				if(conn!=null&&!isTransaction){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		public void close(){
			try {
				if(dbConn!=null){
					dbConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void closeResultSet(ResultSet rs){
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * 查看所有表
		 * types TABLE,VIEW
		 * @return
		 */
		public List<Map<String,String>> listAllTables(String catalog,String schemaPattern,String tableNamePattern,String[] types){
			Connection connection=null;
			ResultSet rs = null;
			List<Map<String,String>> result = new ArrayList<>();
			try {
				connection=getConnection();
				DatabaseMetaData meta = connection.getMetaData();
				rs = meta.getTables(catalog, schemaPattern, tableNamePattern, types);
				ResultSetMetaData rsMeta=rs.getMetaData();
					int columnCount=rsMeta.getColumnCount();
				while(rs.next()){
					Map<String,String> item=new HashMap<String, String>();
					for (int i=1; i<=columnCount; i++) {
						item.put(rsMeta.getColumnLabel(i), rs.getString(i));
					}
					result.add(item);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				closeResultSet(rs);
				closeConnection(connection);
			}
			return result;
		}

		/**
		 * 查看所有表
		 * types TABLE,VIEW
		 * @return
		 */
		public List<Map<String,String>> listAllFields(String catalog,String schemaPattern,String tableNamePattern,String columnNamePattern){
			Connection connection=null;
			ResultSet rs = null;
			List<Map<String,String>> result = new ArrayList<>();
			try {
				connection=getConnection();
				DatabaseMetaData meta = connection.getMetaData();
				rs = meta.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
				ResultSetMetaData rsMeta=rs.getMetaData();
				int columnCount=rsMeta.getColumnCount();
				while(rs.next()){
					Map<String,String> item=new HashMap<String, String>();
					for (int i=1; i<=columnCount; i++) {
						item.put(rsMeta.getColumnLabel(i), rs.getString(i));
					}
					result.add(item);
				}

				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				closeResultSet(rs);
				closeConnection(connection);
			}
			return result;
		}

		/**
		 * 执行sql语句
		 * Statement.execute
		 * @param sql
		 * @return
		 * @throws SQLException
		 */
		public Boolean executeSql(String sql) throws SQLException{
			Boolean b=false;
			Connection connection=null;
			Statement st=null;
			try {
				connection=getConnection();
				st=connection.createStatement();
				b=st.execute(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				closeStatement(st);
				closeConnection(connection);
			}
			
			return b;
		}
		
		/**
		 * 插入对象
		 * @param sql
		 * @param args
		 * @return
		 * @throws SQLException 
		 */
		public Integer insertObj(Object obj) throws SQLException{
			PreparedStatement ps=null;
			Connection connection=getConnection();
			try {
				List<SqlParameter> args=new ArrayList<SqlParameter>();
				String sql=MysqlSQLUtil.getInsertSql(obj,args);
				if(debug){
					System.out.println(sql);
				}
				ps=connection.prepareStatement(sql);
				doParameter(args, ps);
				return ps.executeUpdate();
			} finally{
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 插入对象返回 
		 * @param sql
		 * @param args
		 * @return
		 * @throws SQLException 
		 */
		public Integer insertObjWithId(Object obj) throws SQLException{
			PreparedStatement ps=null;
			Connection connection=getConnection();
			try {
				List<SqlParameter> args=new ArrayList<SqlParameter>();
				String sql=MysqlSQLUtil.getInsertSql(obj,args);
				if(debug){
					System.out.println(sql);
				}
				ps=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				doParameter(args, ps);
				ps.executeUpdate();
				ResultSet rs=ps.getGeneratedKeys();
				if(rs.next()){
					Integer id=rs.getInt(1);
					return id==0&&rs.wasNull()?null:id;
				}
				return null;
			} finally{
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 更新对象
		 * @param sql
		 * @param args
		 * @return
		 * @throws SQLException 
		 */
		public Integer updateObj(Object obj,List<SqlParameter> args1,String where) throws SQLException{
			PreparedStatement ps=null;
			Connection connection=getConnection();
			try {
				List<SqlParameter> args=new ArrayList<SqlParameter>();
				String sql=MysqlSQLUtil.getUpdateSql(obj,args)+" "+where;
				if(debug){
					System.out.println(sql);
				}
				ps=connection.prepareStatement(sql);
				args.addAll(args1);
				doParameter(args, ps);
				return ps.executeUpdate();
			} finally{
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 根据值更新对象
		 * @param sql
		 * @param args
		 * @return
		 * @throws SQLException 
		 */
		public Integer updateObjByValue(Object obj,List<SqlParameter> args1,String where) throws SQLException{
			PreparedStatement ps=null;
			Connection connection=getConnection();
			try {
				List<SqlParameter> args=new ArrayList<SqlParameter>();
				String sql=MysqlSQLUtil.getUpdateSqlByValue(obj,args)+" "+where;
				if(debug){
					System.out.println(sql);
				}
				ps=connection.prepareStatement(sql);
				args.addAll(args1);
				doParameter(args, ps);
				return ps.executeUpdate();
			} finally{
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 更新
		 * @param sql
		 * @param args
		 * @return
		 * @throws SQLException 
		 */
		public Integer executeUpdate(String sql,List<SqlParameter> args) throws SQLException{
			PreparedStatement ps=null;
			Connection connection=getConnection();
			try {
				ps=connection.prepareStatement(sql);
				doParameter(args, ps);
				return ps.executeUpdate();
			} finally{
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 更新
		 * @param sql
		 * @param args
		 * @return
		 * @throws SQLException 
		 */
		public Integer executeUpdateWithId(String sql,List<SqlParameter> args) throws SQLException{
			PreparedStatement ps=null;
			Connection connection=getConnection();
			try {
				ps=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				doParameter(args, ps);
				ps.executeUpdate();
				ResultSet rs=ps.getGeneratedKeys();
				if(rs.next()){
					Integer id=rs.getInt(1);
					return id==0&&rs.wasNull()?null:id;
				}
				return null;
			} finally{
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 查询
		 * @param sql
		 * @param args
		 * @return
		 * @throws SQLException 
		 */
		public RowSet executeQuery(String sql,List<SqlParameter> args) throws SQLException{
			PreparedStatement ps=null;
			ResultSet rs=null;
			Connection connection= getConnection(); 
			try {
				CachedRowSet crs = new CachedRowSetImpl();
				ps=connection.prepareStatement(sql);
				doParameter(args, ps);
				ps.execute();
				rs=ps.getResultSet();
				crs.populate(rs);
				return crs;
			}finally{
				closeResultSet(rs);
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 查询list 基本类型默认选择第一列数据
		 * @param sql
		 * @param args
		 * @param c 支持8大类型 BigDecimal String javaBean
		 * @return
		 * @throws SQLException 
		 */
		public <T> List<T> executeQuery(String sql,List<SqlParameter> args,Class<T> c) throws SQLException{
			PreparedStatement ps=null;
			ResultSet rs=null;
			Connection connection= getConnection(); 
			try {
				ps=connection.prepareStatement(sql);
				doParameter(args, ps);
				rs=ps.executeQuery();
				List<T> list = new ArrayList<T>();
				while(rs.next()){
//					list.add(MysqlSQLUtil.getData(c, rs, getColumns(rs)));
					list.add(MysqlSQLUtil.getData(c, rs, getColumns1(rs)));
				}
				return list;
			}finally{
				closeResultSet(rs);
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 查询单个对象 基本类型默认选择第一列数据
		 * @param <T> 支持 8大类型 BigDecimal String javaBean
		 * @param sql
		 * @param args
		 * @param c
		 * @return
		 * @throws SQLException
		 */
		public <T> T executeQueryOne(String sql,List<SqlParameter> args,Class<T> c) throws SQLException{
			PreparedStatement ps=null;
			ResultSet rs=null;
			Connection connection= getConnection(); 
			try {
				ps=connection.prepareStatement(sql);
				doParameter(args, ps);
				rs=ps.executeQuery();
				if(rs.next()){
					return MysqlSQLUtil.getData(c, rs, getColumns1(rs));
				}
				return null;
			}finally{
				closeResultSet(rs);
				closeStatement(ps);
				closeConnection(connection);
			}
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
		 * 拼装 列名 格式 ： ,id,name,desc,
		 * 
		 * @param rs
		 * @return
		 * @throws SQLException
		 */
		protected static Map<String,Boolean> getColumns1(ResultSet rs) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			Map<String,Boolean> map=new HashMap<String, Boolean>();
			for (int i = 1; i <= count; i++) {
				String column=rsmd.getColumnLabel(i).toUpperCase();
				String[] columns=column.split("\\.");
				column="";
				for (int j = 0; j < columns.length; j++) {
					column=column+columns[j]+".";
					map.put(column, true);
				}
				
			}
			return map;
		}
		
		/**
		 * 查询
		 * @param sql
		 * @param args
		 * @param rsi
		 * @return
		 * @throws SQLException 
		 */
		public <T> T executeQueryOne(String sql,List<SqlParameter> args,RowSetImpl<T> rsi) throws SQLException{
			PreparedStatement ps=null;
			ResultSet rs=null;
			Connection connection= getConnection(); 
			try {
				ps=connection.prepareStatement(sql);
				doParameter(args, ps);
				rs=ps.executeQuery();
				if(rs.next()){
					return rsi.getObjcet(rs);
				}
				return null;
			}finally{
				closeResultSet(rs);
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 查询
		 * @param sql
		 * @param args
		 * @param rsi
		 * @return
		 * @throws SQLException 
		 */
		public <T> List<T> executeQuery(String sql,List<SqlParameter> args,RowSetImpl<T> rsi) throws SQLException{
			PreparedStatement ps=null;
			ResultSet rs=null;
			Connection connection= getConnection(); 
			try {
				ps=connection.prepareStatement(sql);
				doParameter(args, ps);
				rs=ps.executeQuery();
				List<T> list = new ArrayList<T>();
				while(rs.next()){
					list.add(rsi.getObjcet(rs));
				}
				return list;
			}finally{
				closeResultSet(rs);
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 分页查询
		 * @param <T>
		 * @param sql sql语句
		 * @param params 参数
		 * @param pg 分页对象
		 * @param rsi RowSet转对象辅助类
		 * @return 分页对象
		 * @throws SQLException
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public  <T> PageList<T> query(String sql, List<SqlParameter> params, Paginator pg,RowSetImpl<T> rsi) throws SQLException {
			long count=getTotalNum(sql, params);
			pg.setItems(count);
			List<T> list =null;
			if(count>0){
				list = this.executeQuery(getExceSql(sql, pg), params,rsi);
			}else{
				list = new ArrayList<T>();
			}
			return new PageList(list, pg);
		}
		
		/**
		 * 分页查询
		 * @param <T>
		 * @param sql sql语句
		 * @param params 参数
		 * @param pg 分页对象
		 * @param rsi RowSet转对象辅助类
		 * @return 分页对象
		 * @throws SQLException
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public  <T> PageList<T> query(String sql, List<SqlParameter> params, Paginator pg,Class<T> c) throws SQLException {
			PreparedStatement ps=null;
			ResultSet rs=null;
			Connection connection= getConnection(); 
			try {
				long count=getTotalNum(sql, params);
				pg.setItems(count);
				List<T> list = new ArrayList<T>();
				if(count>0){
					ps=connection.prepareStatement(getExceSql(sql, pg));
					doParameter(params, ps);
					rs=ps.executeQuery();
					while(rs.next()){
						list.add(MysqlSQLUtil.getData(c, rs, getColumns1(rs)));
					}
				}
				return new PageList(list, pg);
			}finally{
				closeResultSet(rs);
				closeStatement(ps);
				closeConnection(connection);
			}
		}
		
		/**
		 * 获得执行sql
		 * @param sql
		 * @param pg
		 * @return
		 */
		private  String getExceSql(String sql, Paginator pg) {
			sql = sql + " limit " + pg.getStartIndex() + "," + pg.getItemsPerPage();
			return sql;
		}
		
		/**
		 * 获得总条数
		 * @param sql
		 * @param params
		 * @return
		 * @throws SQLException
		 */
		private  long getTotalNum(String sql, List<SqlParameter> params)
				throws SQLException {
			String countSql = "SELECT COUNT(*) AS l FROM (" + sql + ") count";
			RowSet rs = this.executeQuery(countSql, params);
			if(rs.next()){
				return rs.getLong("l");
			}else{
				return 0l;
			}
		}
		
		/**
		 * 调用存储过程 
		 * @param sql
		 * @param args
		 * @param resultSetType
		 * @param resultSetConcurrency
		 * @param resultSetHoldability
		 * @return
		 * @throws SQLException
		 */
		public Integer prepareCall(String sql,List<SqlParameter> args,int resultSetType,int resultSetConcurrency) throws SQLException{
			CallableStatement  cs=null;
			Connection connection=getConnection();
			Integer ret=0;
			try {
				cs=connection.prepareCall(sql, resultSetType, resultSetConcurrency);
				doParameter(args, cs);
				boolean b = cs.execute();
				putOutParameter(args,cs);
				if (!b) {
			        ret = cs.getUpdateCount();
			    }
				return ret;
			} finally{
				closeStatement(cs);
				closeConnection(connection);
			}
		}
		
		/**
		 * 开始事务
		 * @throws SQLException
		 */
		public void beginTransaction() throws SQLException{
			if(!isTransaction){
				throw new RuntimeException("未开启事务");
			}
			getConnection().setAutoCommit(false);
		}
		
		/**
		 *提交事务 
		 * @throws SQLException
		 */
		public void commit() throws SQLException{
			if(!isTransaction){
				throw new RuntimeException("未开启事务");
			}
			getConnection().commit();
		}
		
		/**
		 * 设置保存点
		 * @param sp
		 * @throws SQLException
		 */
		public Savepoint setSavepoint() throws SQLException{
			if(!isTransaction){
				throw new RuntimeException("未开启事务");
			}
			return getConnection().setSavepoint();
		}
		
		/**
		 * 设置隔离级别
		 * @param sp
		 * @throws SQLException
		 */
		public void setTransactionIsolation(int level) throws SQLException{
			if(!isTransaction){
				throw new RuntimeException("未开启事务");
			}
			getConnection().setTransactionIsolation(level);
		}
		
		
		
		/**
		 * 回滚
		 * @param sp
		 * @throws SQLException
		 */
		public void rollback(Savepoint sp) throws SQLException{
			if(!isTransaction){
				throw new RuntimeException("未开启事务");
			}
			getConnection().rollback(sp);
		}
		/**
		 * 回滚
		 * @throws SQLException
		 */
		public void rollback() throws SQLException{
			if(!isTransaction){
				throw new RuntimeException("未开启事务");
			}
			getConnection().rollback();
		}
		
		/**
		 * 释放保存点
		 * @param sp
		 * @throws SQLException
		 */
		public void releaseSavepoint(Savepoint sp) throws SQLException{
			if(!isTransaction){
				throw new RuntimeException("未开启事务");
			}
			getConnection().releaseSavepoint(sp);
		}
		
		/**
		 * 处理输出参数
		 * @param args
		 * @param crs
		 * @throws SQLException
		 */
		private void putOutParameter(List<SqlParameter> args,CallableStatement crs) throws SQLException{
			for (int i = 0;args!=null && i < args.size(); i++) {
				SqlParameter sp=args.get(i);
				if(sp.getOutput()){
					sp.setValue(crs.getObject(i+1));
				}
			}
		}
		
		/**
		 * 处理输入参数
		 * @param args
		 * @param ps
		 * @throws SQLException
		 */
		private void doParameter(List<SqlParameter> args,PreparedStatement ps) throws SQLException{
			for (int i = 0;args!=null && i < args.size(); i++) {
				SqlParameter sp=args.get(i);
				if(sp.getOutput()){
					CallableStatement cs=(CallableStatement)ps;
					cs.registerOutParameter(i+1, sp.getType());
				}
				ps.setObject(i+1, sp.getValue(), sp.getType());
			}
		}
	}
	
	/**
	 * 数据库回调
	 * @author tanbin
	 *
	 */
	public interface MyBack {
		public Object exe(DbHelper dh) throws Exception;
	}
	
	/**
	 * 数据源提供者
	 * @author 
	 *
	 */
	public interface DataSourceProvider {
		public DataSource getDataSource(String key);
	}
	
	/**
	 * 结果集转javabean 
	 * @param <T>
	 * @author tanbin
	 */
	public interface RowSetImpl<T> {
		T getObjcet(ResultSet rs);
	}
}
