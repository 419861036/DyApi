package kkd.common.dao.dbuitl.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import kkd.common.dao.dbuitl.BaseDataSourceProviderImpl;
import kkd.common.dao.dbuitl.JDBC;
import kkd.common.orm.Id_;
import kkd.common.orm.Seq_;
import kkd.common.orm.Table_;


public class Test {
	
	
	
	public static void main(String[] args) throws SQLException {
		
		JDBC.setDefultDataSourceProvider(BaseDataSourceProviderImpl.getInstance(), "test");
		SendGift1  list=JDBC.getDefaultNoTransactionDbHelper().executeQueryOne("select id 'sendGift.userId' ,sendTime 'sendGift.sendTime',id uid  from Act_TFD_SendGiftHis limit 0,1", null,  SendGift1.class);
		System.out.println(list.getSendGift().getSendTime());
		System.out.println(list.getSendGift().getUserId());
		System.out.println(list.getUid());
		
		
//		ResultSet rs=null;
//		SendGift s=new SendGift();
//		s.setId(4);
//		s.setUserId((short)1);
//		s.setUserId(1);
//		s.setCardNum("'发生的fsdfs11");
//		s.setSendTime(new Date());
//		," where id="+
//		List<SqlParameter> args1=new ArrayList<SqlParameter>();
//		args1.add(new SqlParameter(Types.INTEGER, s.getId()));
//		JDBC.getDefaultNoTransactionDbHelper().insertObj(s);
//		JDBC.getDefaultNoTransactionDbHelper().updateObj(s, args1, " where id=?");
		
//		JDBC.getDefaultNoTransactionDbHelper().updateObj(s, args, " where id=?");
		
//		String ss=MysqlSQLUtil.getInsertSql(s,args1);
//		System.out.println(ss);
	}
	
}
