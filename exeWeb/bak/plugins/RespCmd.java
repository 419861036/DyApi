package com.exe.web.plugins;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.entity.Msg;

/**
 * 输出内容
 * @author tanbin
 *
 */
public class RespCmd extends Cmd{
	private final static Logger logger = LoggerFactory.getLogger(RespCmd.class);
	
	@Override
	public void exe(CmdVo cmd, Map<String, String> param) {
		Map<String, Object> sys=cmdHandle.getSys();
		HttpServletResponse resp=(HttpServletResponse) sys.get("resp");
		
		Map<String, Object> vars=cmdHandle.getVars();
		String var=param.get("key");
		String code=param.get("code");
		String msg=param.get("msg");
		String tpl=param.get("tpl");
		try {
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("text/html; charset=utf-8");
			if(tpl==null){
				Object jo= vars.get(var);
				Msg<Object> rs=new Msg<Object>();
				rs.setCode(Integer.parseInt(code));
				rs.setMsg(msg);
				rs.setV(jo);
				resp.getWriter().write(rs.objectToFastJSON());
			}else{
				
				String data=(String) cmdHandle.getVars().get(tpl);
				resp.getWriter().write(data);
			}
		} catch (IOException e) {
			logger.error("", e);
		}finally{
		}
	}

	@Override
	public String op() {
		return null;
	}

}
