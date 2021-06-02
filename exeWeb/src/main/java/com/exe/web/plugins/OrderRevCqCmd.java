package com.exe.web.plugins;

import com.alibaba.fastjson.JSON;
import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;
import kkd.common.cache.server.CacheServlet;
import kkd.common.cache.server.LRUCache;
import kkd.common.dao.dbuitl.SqlParameter;
import kkd.common.util.DateUtil;
import kkd.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Types;
import java.util.*;

public class OrderRevCqCmd extends Cmd {
    private static LRUCache<String,Boolean> user=new LRUCache<>(20000);
    private final static Logger logger = LoggerFactory.getLogger(OrderRevCqCmd.class);
    @Override
    public void exe(CmdVo cmd, Map<String, String> param) {
        Map<String, Object> vars=cmdHandle.getVars();
        HttpServletRequest req= (HttpServletRequest) cmdHandle.getSys().get("req");
        HttpServletResponse resp=(HttpServletResponse) cmdHandle.getSys().get("resp");
        resp.addHeader("Access-Control-Allow-Origin","*");
        Map<String,String> map=new HashMap<>();
        String url=req.getParameter("url");
        map.put("url",url);
        map.put("stbID",req.getParameter("stbID"));
        map.put("STBType",req.getParameter("STBType"));
        map.put("mac",req.getParameter("mac"));
        map.put("userAgent",req.getHeader("User-Agent"));
        map.put("userID",req.getParameter("userId"));
        map.put("user_id",req.getParameter("userId"));
        map.put("token",req.getParameter("token"));
        map.put("product_id",req.getParameter("product_id"));
        map.put("platform",req.getParameter("platform"));

        String userId=map.get("userID");
        Boolean hasUserId=user.get(userId);
        if(!hasUserId){
            user.put(userId,true);
        }else{
            return;
        }
        String data=JSON.toJSONString(map);
        putLog(map.get("userID"),"",map.get("token"),map.get("product_id"),data,"未处理");
    }

    /**
     放入黑名单
     */
    public boolean putLog(String user_id,String user_type,String token,String product_id,String data,String description){
        Date create_date=new java.util.Date();
        String create_dateStr= DateUtil.format(create_date,"yyyy-MM-dd HH:mm:ss");
        kkd.common.dao.dbuitl.JDBC.DbHelper dh=kkd.common.dao.dbuitl.JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try{
            parameters.add(new SqlParameter(Types.VARCHAR,user_id));
            parameters.add(new SqlParameter(Types.VARCHAR,user_type));
            parameters.add(new SqlParameter(Types.VARCHAR,product_id));
            parameters.add(new SqlParameter(Types.VARCHAR,create_dateStr));
            parameters.add(new SqlParameter(Types.VARCHAR,description));
            parameters.add(new SqlParameter(Types.VARCHAR,data));
            String sql="insert into iptv_order_user(user_id,user_type,product_id,create_date,description,data)values";
            sql=sql+"(?,?,?,?,?,?)";
            Integer cnt=dh.executeUpdate(sql,parameters);
            if(cnt>0){
                return true;
            }
        }catch (Exception e){
            System.out.println("------rev data-------5-$e--");
            logger.error("",e);
        }
        return false;
    }

    @Override
    public String op() {
        return null;
    }


}
