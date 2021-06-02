package kkd.common.proxy.test.user;

import java.sql.SQLException;

public class UserDao extends TransactionDao{
	
	public boolean insert(User u) throws SQLException{
		getDbHelper().executeSql("");
		return false;
	}
	
	public boolean update(User u) throws SQLException{
		getDbHelper().executeSql("");
		return false;
	}
}
