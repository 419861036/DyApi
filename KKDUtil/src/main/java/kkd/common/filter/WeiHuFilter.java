package kkd.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.util.EnvUtil;
import kkd.common.util.StringUtil;
import kkd.common.util.WebUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * 系统维护过滤器
 * @author tanbin
 *
 */
public class WeiHuFilter implements Filter{

	@Override
	public void destroy() {
		
	}
	
	private String oldProjectStateConfig;
	private String projectState=null;
	private String projectStateWeiHuURL=null;
	
	
	@Override
	public void doFilter(ServletRequest srRequest, ServletResponse srResponse,
			FilterChain fc) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)srRequest;
		HttpServletResponse response=(HttpServletResponse)srRequest;
		String type=WebUtil.getRequestType(request);
		//判断 请求数据的格式 
		if("html".equals(type)){
			String projectStateConfig=EnvUtil.get(EnvUtil.projectStateConfig);
			if(StringUtil.isEmpty(projectStateConfig)){
				fc.doFilter(srRequest, srResponse);
				return;
			}else if(!oldProjectStateConfig.equals(projectStateConfig)){
				JSONObject jo=JSONObject.parseObject(projectStateConfig);
				projectState=jo.getString("projectState");
				projectStateWeiHuURL=jo.getString("projectStateWeiHuURL");
				oldProjectStateConfig=projectStateConfig;
			}
			if(!StringUtil.isEmpty(projectStateConfig)){
				if("WeiHu".equals(projectState)&&!StringUtil.isEmpty(projectStateWeiHuURL)){
					response.sendRedirect(projectStateWeiHuURL);
				}
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}

}
