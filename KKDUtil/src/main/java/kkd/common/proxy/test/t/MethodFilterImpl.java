package kkd.common.proxy.test.t;

import java.sql.SQLException;

import kkd.common.dao.dbuitl.JDBC.DbHelper;
import kkd.common.proxy.javassist.Invoker;
import kkd.common.proxy.javassist.MethodFilter;
import kkd.common.proxy.javassist.MethodInfo;

public class MethodFilterImpl implements MethodFilter{
	
	private DbHelper dh;
	
	public void setDh(DbHelper dh) {
		this.dh = dh;
	}

	@Override
	public Object invoke(Invoker invoker, int key, Object[] args) {
//		Object a=null;
//		try {
//			a=invoker.invokeSuper(key, args);
//			dh.commit();
//		} catch (Exception e) {
//			try {
//				dh.rollback();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//		}finally{
//			dh.close();
//		}
//		return a;
//		System.out.println(mi.getCurrentClassMethod()[key].getName());
		return invoker.invokeSuper(key, args);
		
	}

	private MethodInfo mi;
	@Override
	public void setMethodInfo(MethodInfo mi) {
		this.mi=mi;
	}

}
