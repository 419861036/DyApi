package kkd.common.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import kkd.common.logger.LogWriter;
/**
 * 字符编码过滤器
 * @author Administrator
 *
 */
public class KKDEncodingFilter implements Filter {

	private String encoding;

	private boolean forceEncoding = false;

	public KKDEncodingFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) {
		try {
			if (this.encoding != null
					&& (this.forceEncoding || request.getCharacterEncoding() == null)) {
				request.setCharacterEncoding(this.encoding);
				if (this.forceEncoding) {
					response.setContentType("text/html;   charset="+this.encoding); 
					response.setCharacterEncoding(this.encoding);
				}
			}
//			HttpServletResponse response1=(HttpServletResponse)response;
//			response1.setHeader("Pragma","No-Cache");
//			response1.setHeader("Cache-Control","No-Cache");
//			response1.setDateHeader("Expires",0);
			chain.doFilter(request, response);
		} catch (Exception e) {
			LogWriter.error(null, e);
		}
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
		try {
			setEncoding(fConfig.getInitParameter("encoding"));
			String fe=fConfig.getInitParameter("forceEncoding");
			fe=fe==null?"false":fe;
			setForceEncoding(Boolean.valueOf(fe));
		} catch (Exception e) {
			LogWriter.error(null,e);
		}
		
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}
}
