package kkd.common.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.filebrowser.FileBrowser;
import kkd.common.util.StringUtil;

/**
 * 文件管理器入口
 * @author tanbins
 *
 */
public class FileServlet {
	
	/**
	 * 下载文件
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void download(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("application/x-download");
		FileBrowser f=new FileBrowser();
		f.downLoadFile(resp,req.getParameter("filename"));
	}
	
	public void upload(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		FileBrowser f=new FileBrowser();
		String path=req.getParameter("path");
		if(!StringUtil.isEmpty(path)){
			File file=new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			if(file.isDirectory()){
				path= new String(path.getBytes("ISO-8859-1"), "UTF-8");
				f.upload(req,path,true);
			}
		}
	}
	
	/**
	 * 删除文件
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		FileBrowser f=new FileBrowser();
		String sPath=req.getParameter("filename");
		try {
			sPath= new String(sPath.getBytes("ISO-8859-1"), "UTF-8");
			resp.getWriter().print(f.deleteDirectory(sPath));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * 浏览文件
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void fileBrowser(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		FileBrowser f=new FileBrowser();
		String path=req.getParameter("path");
		if(path!=null&&path==""){
			resp.getWriter().print("path is null");
		}else{
			String output=f.getSimpleFile("download.do?filename=","fileBrowser.do?path=",path,"delete.do?filename=");
			resp.getWriter().print(output);
		}
	}
	
	/**
	 * 提交表单
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void form(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		PrintWriter  out=resp.getWriter();
		out.print("<!DOCTYPE><html>");
		FileBrowser f=new FileBrowser(); 
		String path=req.getParameter("path");
		path= new String(path.getBytes("ISO-8859-1"), "UTF-8");
		out.print(f.getUploadForm("upload.do",path));
		out.print("</html>");
	}
	
	
	
}
