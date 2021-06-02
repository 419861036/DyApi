package kkd.common.deploy;

import java.sql.SQLException;

import kkd.common.dao.dbuitl.test.SendGift1;

public class TestBo implements ITestBo{
	private ITestDao dao=Main.getIns(ITestDao.class, TestDao.class.getName());
	public SendGift1 find() throws SQLException{
//		System.out.println("bo 1");
		return dao.find();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("TestBo");
	}
}
