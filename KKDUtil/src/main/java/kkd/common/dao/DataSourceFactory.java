package kkd.common.dao;

import javax.sql.DataSource;

/***
 * 数据源工厂
 * 
 * @author Administrator
 * 
 */
public class DataSourceFactory {
	
//	private static final Map<String, DataSource> datasource = new HashMap<String, DataSource>();

	public static DataSource getDataSource(String dsName) {
//		DataSource ds = datasource.get(dsName);
//		if(ds==null){
//			ds=new ComboPooledDataSource(dsName);
//			datasource.put(dsName, ds);
//		}
//		return ds;
		return null;
	}
}
