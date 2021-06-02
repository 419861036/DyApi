package kkd.common.proxy.test.user;

import java.sql.SQLException;

public interface IUserDao {
	public boolean insert(User u) throws SQLException;
	public boolean update(User u) throws SQLException;
	
}
