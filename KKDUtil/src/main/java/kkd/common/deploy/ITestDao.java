package kkd.common.deploy;

import java.sql.SQLException;

import kkd.common.dao.dbuitl.test.SendGift1;

public interface ITestDao {
	public SendGift1 find() throws SQLException;
}
