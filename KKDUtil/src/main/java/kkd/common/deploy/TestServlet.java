package kkd.common.deploy;

import java.sql.SQLException;

public class TestServlet implements ITestServlet{
	
	private ITestBo bo=Main.getIns(ITestBo.class, TestBo.class.getName());
	public void find(){
		try {
//			System.out.println("servlet 1");
			bo.find();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
