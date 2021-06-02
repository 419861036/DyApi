package com.exe.web.plugins;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.dao.dbuitl.JDBC;
import kkd.common.dao.dbuitl.JDBC.DbHelper;
import kkd.common.dao.dbuitl.JDBC.MyBack;
import kkd.common.dao.dbuitl.SqlParameter;
import kkd.common.util.StringUtil;


/**
 * 通过sql查询数据
 * @author tanbin
 *
 */
public class ExeSqlCmd extends Cmd{
	
	private final static Logger logger = LoggerFactory.getLogger(ExeSqlCmd.class);
	
	@Override
	public void exe(CmdVo caiAction, Map<String, String> param) {
//		logger.setRuleId(cmdHandle.getCaiRule().getId().toString());
		long l=System.currentTimeMillis();
		String sql=param.get("sql");
		String rsId=param.get("rsId");
		String body=caiAction.getBody();
		
		String var=param.get("var");
		String cols=param.get("cols");
		String[] colsAttr=cols.split(",");
		logger.debug("当前节点执行完成，耗时0：{}",(System.currentTimeMillis()-l));
		getJDBC(new MyBack() {
			public Object exe(DbHelper dh) throws Exception {
				logger.debug("当前节点执行完成，耗时1：{}",(System.currentTimeMillis()-l));
				List<SqlParameter> args=new ArrayList<SqlParameter>();
				for (String string : colsAttr) {
					if(!StringUtil.isEmpty(string)){
						Object obj=getVal(args, string);
						args.add(new SqlParameter(Types.VARCHAR, obj));
					}
				}
				String sql1=sql;
				if(!StringUtil.isEmpty(body)){
					sql1=body;
				}
				logger.debug("exe sql:{}",sql1);
				logger.debug("当前节点执行完成，耗时2：{}",(System.currentTimeMillis()-l));
				if(!StringUtil.isEmpty(rsId)){
					Integer id=dh.executeUpdateWithId(sql, args);
					cmdHandle.getVars().put(rsId, id);
				}else{
					Integer cnt=dh.executeUpdate(sql1, args);
					cmdHandle.getVars().put(var, cnt);
				}
				
				logger.debug("当前节点执行完成，耗时3：{}",(System.currentTimeMillis()-l));
				return null;
			}
		});
		logger.debug("当前节点执行完成，耗时4：{}",(System.currentTimeMillis()-l));
	}
	
	private Object getVal(List<SqlParameter> args, String string) {
		if(string.contains(".")){
			String[] arr=string.split("\\.");
			Object obj=null;
			for (String string2 : arr) {
				if(obj!=null){
					if(obj instanceof Map){
						Map<String,Object> map=(Map) obj;
						return obj=map.get(string2);
					}
				}else{
					obj=cmdHandle.getVars().get(string2);
				}
			}
		}else{
			return cmdHandle.getVars().get(string);
		}
		return null;
	}
	public static void getJDBC(MyBack myBack){
		JDBC jdbc=new JDBC();
		jdbc.execute(myBack);
	}
	
	@Override
	public String op() {
		return null;
	}
	
}
