package kkd.common.dao.dbuitl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory;

import kkd.common.dao.dbuitl.JDBC.DataSourceProvider;


public class BaseDataSourceProviderImpl implements DataSourceProvider {

	private static Object obj = new Object();
	private final static BaseDataSourceProviderImpl impl=new BaseDataSourceProviderImpl();
	private Map<String,DataSource> map=new HashMap<String,DataSource>();
	private Map<String,String> dbConfig=new HashMap<String,String>();
	private Map<String,Boolean> changeDs=new Hashtable<String,Boolean>();
	
	private BaseDataSourceProviderImpl(){
		
	}
	public static BaseDataSourceProviderImpl getInstance(){ 
		return impl; 
	}
	@Override
	public DataSource getDataSource(String key) {
		try {
			Boolean changeDsed=changeDs.get(key);
			DataSource ds=null;
			if(changeDsed==null || changeDsed!=null && !changeDsed){
				ds=map.get(key);
			}
			synchronized (obj) {
				if(ds==null){
					String fileKey="file:";
					if(key.startsWith(fileKey)){
						int pos=key.indexOf(fileKey);
						pos=pos+fileKey.length();
						String fileName=key.substring(pos);
						Properties dbProperties = new Properties();
						 dbProperties.load(BaseDataSourceProviderImpl.class.getClassLoader()
							     .getResourceAsStream(fileName));
						ds=BasicDataSourceFactory.createDataSource(dbProperties);
					}else if("test".equals(key)) {
//						ds = new ComboPooledDataSource("test");
						Properties dbProperties = new Properties();
						 dbProperties.load(BaseDataSourceProviderImpl.class.getClassLoader()
							     .getResourceAsStream("dbcp.properties"));
						ds=BasicDataSourceFactory.createDataSource(dbProperties);
					}else if(key.startsWith("java:comp/env")){
						//"java:comp/env/jdbc/ADManager"
//						ds=DataSourceManager.getDataSource(key,true);
				        try{
				          InitialContext context = new InitialContext();
				          ds = (DataSource)context.lookup(key);
				        }catch (NamingException e) {
				          e.printStackTrace();
				        }
					}else{
						String dbConfigStr=dbConfig.get(key);
						Properties dbProperties = new Properties();
						dbProperties.load(new ByteArrayInputStream(dbConfigStr.getBytes()));
						ds=BasicDataSourceFactory.createDataSource(dbProperties);
					}
					changeDs.put(key, false);
					map.put(key, ds);
				}
			}
			return ds;
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public static DataSource newCustomDataSource(String className,String url,String userName,String password) {
		 try {
			 Properties dbProperties = new Properties();
			 dbProperties.put("driverClassName", className);
			 dbProperties.put("url", url);
			 dbProperties.put("username", userName);
			 dbProperties.put("password", password);
			 dbProperties.put("maxActive", "100");
			 dbProperties.put("maxIdle", "10");
			 dbProperties.put("maxWait", "1000");
//			 dbProperties.put("validationQuery", "SELECT 1");
			 dbProperties.put("testWhileIdle", "true");
			 dbProperties.put("testOnBorrow", "true");
			 dbProperties.put("testOnReturn", "false");
			 dbProperties.put("numTestsPerEvictionRun", "10");
			 dbProperties.put("timeBetweenEvictionRunsMillis", "30000");
			 BasicDataSource   ds=(BasicDataSource)BasicDataSourceFactory.createDataSource(dbProperties);
			return ds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 创建一个数据源用于测试
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 */
	public static DataSource newCustomDataSource(String url,String userName,String password) {
		return newCustomDataSource("com.mysql.jdbc.Driver", url, userName, password);
	}
	
	public  void setDataSource(String key,DataSource ds){
		map.put(key, ds);
	}
	
	public  void setDbConfig(String key,String config){
		dbConfig.put(key, config);
		changeDs.put(key, true);
	}
	
	public static void main(String[] args) {
		Properties dbProperties = new Properties();
		 try {
			dbProperties.load(BaseDataSourceProviderImpl.class.getClassLoader()
				     .getResourceAsStream("dbcp.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			BasicDataSource   ds=(BasicDataSource)BasicDataSourceFactory.createDataSource(dbProperties);
			System.out.println(ds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
