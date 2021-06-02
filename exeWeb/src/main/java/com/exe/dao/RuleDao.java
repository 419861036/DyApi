package com.exe.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kkd.common.dao.PageList;
import kkd.common.dao.Paginator;
import kkd.common.dao.dbuitl.JDBC.DbHelper;
import kkd.common.util.StringUtil;
import kkd.common.dao.dbuitl.SqlParameter;

import com.exe.base.vo.ExeResourceVo;
import com.exe.param.RuleParam;

public class RuleDao {
	public static boolean insert(ExeResourceVo vo,DbHelper dh) throws SQLException{
		vo.setCreateTime(new Date());
		vo.setUpTime(new Date());
		return dh.insertObj(vo) > 0 ? true : false;
	}
	
	public static boolean update(ExeResourceVo vo, DbHelper dh) throws SQLException {
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		vo.setUpTime(new Date());
		params.add(new SqlParameter(Types.INTEGER, vo.getId()));
		return dh.updateObjByValue(vo, params, "where id=?") > 0 ? true : false;
	}
	
	public static boolean delete(Integer id, DbHelper dh) throws SQLException  {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from `exe_resource` where `Id`=?");
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		params.add(new SqlParameter(Types.INTEGER, id));
		return dh.executeUpdate(sb.toString(), params) > 0 ? true : false;
	}
	
	public static PageList<ExeResourceVo> page(RuleParam param, Paginator pg,  DbHelper dh) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM `exe_resource` p  ");
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		return dh.query(sb.toString(), params, pg, ExeResourceVo.class);
	}
	
	public static List<ExeResourceVo> list(RuleParam param,  DbHelper dh) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM `exe_resource` p where 1=1 ");
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		if(param!=null){
			if(!StringUtil.isEmpty(param.getType())){
				params.add(new SqlParameter(Types.VARCHAR, param.getType()));
				sb.append(" and type=?");
			}
		}
		return dh.executeQuery(sb.toString(),params, ExeResourceVo.class);
	}

	public static ExeResourceVo getById(Integer id, DbHelper dh) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM `exe_resource` WHERE `Id`=? ");
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		params.add(new SqlParameter(Types.INTEGER, id));
		return dh.executeQueryOne(sb.toString(), params, ExeResourceVo.class);
	}
	
	public static ExeResourceVo getByPath(String path, DbHelper dh) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM `exe_resource` WHERE `path`=? ");
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		params.add(new SqlParameter(Types.VARCHAR, path));
		return dh.executeQueryOne(sb.toString(), params, ExeResourceVo.class);
	}
}
