package kkd.common.proxy.test.user;

import kkd.common.dao.dbuitl.JDBC.DbHelper;
import kkd.common.proxy.test.t.DbHelperHolder;

public class TransactionDao {
	
	public  DbHelper getDbHelper(){
		return DbHelperHolder.getDbHelper();
	}
}
