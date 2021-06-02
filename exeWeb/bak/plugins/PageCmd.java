package com.exe.web.plugins;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import kkd.common.dao.PageList;
import kkd.common.dao.Paginator;
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
public class PageCmd extends Cmd{
	private final static Logger logger = LoggerFactory.getLogger(PageCmd.class);
	@Override
	public void exe(CmdVo caiAction, Map<String, String> param) {
//		logger.setRuleId(cmdHandle.getCaiRule().getId().toString());
		String sql=param.get("sql");
		String var=param.get("var");
		String paramStr=param.get("param");
		String pageIndex=param.get("page");
		String pageSize=param.get("pageSize");
		 
		String cols=param.get("cols");
		String[] colsAttr=cols.split(",");
		String[] paramsAttr=paramStr.split(",");
		
		getJDBC(new MyBack() {
			@SuppressWarnings("unchecked")
			public Object exe(DbHelper dh) throws Exception {
				
				StringBuilder sb = new StringBuilder();
				sb.append(sql);
				List<SqlParameter> args=new ArrayList<SqlParameter>();
				for (String string : paramsAttr) {
					if(!StringUtil.isEmpty(string)){
						Object obj=getVal(args, string);
						if(!StringUtil.isEmpty(obj)){
							args.add(new SqlParameter(Types.VARCHAR, obj));
						}
					}
				}
				logger.debug("执行sql:{}",sql);
				
				JDBC.RowSetImpl<Object> rsi=new JDBC.RowSetImpl<Object>() {
					@Override
					public Object getObjcet(ResultSet rs) {
						Map<String,String> item=new HashMap<String, String>();
						for (String string : colsAttr) {
							try {
								String value=rs.getString(string);
								item.put(string, value);
							} catch (SQLException e) {
								logger.error("", e);
							}
						}
						return item;
					}
					
				};
				Paginator p=new Paginator();
				p.setPage(Integer.parseInt(pageIndex));
				p.setItemsPerPage(Integer.parseInt(pageSize));
				PageList<Object> data=dh.query(sql, args, p, rsi);
				if(data!=null){
					cmdHandle.getVars().put(var, data);
				}else{
					cmdHandle.getVars().put(var, null);
				}
				return null;
			}
		});
		
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
		jdbc.setDebug(true);
		jdbc.execute(myBack);
	}
	
	@Override
	public String op() {
		return null;
	}
	
}
