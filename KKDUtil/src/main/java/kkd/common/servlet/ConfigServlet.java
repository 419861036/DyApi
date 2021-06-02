package kkd.common.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.entity.Msg;
import kkd.common.util.ConfigUtil;
/**
 * 清除配置缓存
 * @author tanbins
 *
 */
public class ConfigServlet {
	
	public void flushConfig(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		ConfigUtil.flush();
		Msg<String> msg=new Msg<String>();
		resp.getWriter().print(msg.toXMls(Msg.class));
	}
}
