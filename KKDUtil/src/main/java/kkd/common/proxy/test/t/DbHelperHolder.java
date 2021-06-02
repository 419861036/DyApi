package kkd.common.proxy.test.t;

import kkd.common.dao.dbuitl.JDBC;
import kkd.common.dao.dbuitl.JDBC.DbHelper;

public class DbHelperHolder {
	
	private static ThreadLocal<DbHelper> threadLocal =new ThreadLocal<DbHelper>();

	public static  DbHelper getDbHelper(){
		DbHelper dh=threadLocal.get();
		if(dh==null){
			dh=JDBC.getDefaultTransactionDbHelper();
			threadLocal.set(dh);
		}
		return dh;
	}

}
