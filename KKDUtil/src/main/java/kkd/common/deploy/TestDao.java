package kkd.common.deploy;

import java.sql.SQLException;

import kkd.common.dao.dbuitl.BaseDataSourceProviderImpl;
import kkd.common.dao.dbuitl.JDBC;
import kkd.common.dao.dbuitl.test.SendGift1;

public class TestDao implements ITestDao{
	
	public SendGift1 find() throws SQLException{
//		JDBC.setDefultDataSourceProvider(BaseDataSourceProviderImpl.getInstance(), "test");
//		SendGift1  list=JDBC.getDefaultNoTransactionDbHelper().executeQueryOne("select id uid  from Act_TFD_SendGiftHis limit 0,1", null,  SendGift1.class);
		System.out.println("dao 2");
		return null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("TestDao");
		super.finalize();
	}
}
