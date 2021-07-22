package com.exe.web.plugins;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import kkd.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.dao.dbuitl.JDBC;
import kkd.common.dao.dbuitl.JDBC.DbHelper;
import kkd.common.dao.dbuitl.JDBC.MyBack;
import kkd.common.dao.dbuitl.SqlParameter;
import kkd.common.util.StringUtil;


/**
 * 通过Bean插入数据库
 * @author tanbin
 *
 */
public class UpdateCmd extends Cmd{

    private final static Logger logger = LoggerFactory.getLogger(UpdateCmd.class);

    @Override
    public void exe(CmdVo caiAction, Map<String, String> param) {
        long l=System.currentTimeMillis();
        String rsId=param.get("rsId");
        String body=caiAction.getBody();

        String var=param.get("var");
        String paramType=param.get("paramType");
        JSONObject bean = (JSONObject) cmdHandle.getVars().get(paramType);
        String tableName=bean.getString("tableName");
        String objName=bean.getString("objName");
        JSONArray fields=bean.getJSONArray("fields");

        logger.debug("当前节点执行完成，耗时0：{}",(System.currentTimeMillis()-l));
        getJDBC(new MyBack() {
            public Object exe(DbHelper dh) throws Exception {
                logger.debug("当前节点执行完成，耗时1：{}",(System.currentTimeMillis()-l));
                List<SqlParameter> args=new ArrayList<SqlParameter>();
                StringBuilder sql=new StringBuilder("update ");
                sql.append(tableName);
                sql.append(" set ");
                int size=fields.size();
                for(int i=0;i<size;i++){
                    JSONObject item= (JSONObject) fields.get(i);
                    String fieldName=item.getString("key");
                    if(!StringUtil.isEmpty(fieldName)){
                        sql.append("`");
                        sql.append(fieldName);
                        sql.append("`");
                        sql.append("=?");
                        if(i!=fields.size()-1){
                            sql.append(",");
                        }
                    }
                }
                sql.append(" where ");
                Object pkVal=null;
                for(int i=0;i<size;i++){
                    JSONObject item= (JSONObject) fields.get(i);
                    String fieldName=item.getString("key");
                    if(!StringUtil.isEmpty(fieldName)){
                        String pk=item.getString("pk");
                        Object obj=getVal(args, objName+"."+fieldName);
                        if("true".equalsIgnoreCase(pk)){
                            sql.append(fieldName);
                            sql.append("=?");
                            pkVal=obj;
                        }
                        String auto=item.getString("auto");
                        if("date".equals(auto)){
                            args.add(new SqlParameter(Types.VARCHAR, DateUtil.format(DateUtil.DATETIME_PATTERN)));
                        }else {
                            args.add(new SqlParameter(Types.VARCHAR, obj));
                        }
                    }
                }
                args.add(new SqlParameter(Types.VARCHAR, pkVal));


                String sql1=sql.toString();
                if(!StringUtil.isEmpty(body)){
                    sql1=body;
                }
                logger.error("exe sql:{}",sql1);
                logger.debug("当前节点执行完成，耗时2：{}",(System.currentTimeMillis()-l));
                if(!StringUtil.isEmpty(rsId)){
                    Integer id=dh.executeUpdateWithId(sql1, args);
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

