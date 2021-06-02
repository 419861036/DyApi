package kkd.common.filebrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.util.ListUtil;

/**
 * 
/file/download.do=className:kkd.common.servlet.FileServlet;methodName:download
/file/upload.do=className:kkd.common.servlet.FileServlet;methodName:upload
/file/delete.do=className:kkd.common.servlet.FileServlet;methodName:delete
/file/fileBrowser.do=className:kkd.common.servlet.FileServlet;methodName:fileBrowser
/file/form.do=className:kkd.common.servlet.FileServlet;methodName:form
 * @author tanbins
 *
 */
public class FileBrowser {
	
	/**
	 * 得到当前路径的文件列表
	 * @param path
	 * @return
	 */
	public List<File> listFile(String path){
		File file=new File(path);
		List<File> list=ListUtil.arrayToList(file.listFiles());
		if(list!=null){
			Collections.sort(list,new Comparator<File>() {
				public int compare(File o1, File o2) {
					if(o1.isFile()){
						return -1;
					}else{
						return 1;
					}
				}
			});
		}
		
		return list;
	}
	
	/**
	 * 下载文件
	 * @param response
	 * @param fileName
	 * @throws IOException
	 */
	public void downLoadFile(HttpServletResponse response,String fileName) throws IOException{
		try {
			fileName= new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
//		 response.reset();//可以加也可以不加
		 response.setContentType("application/x-download");
//		 String filenamedownload = "想办法找到要提供下载的文件的物理路径＋文件名";
//		 String filedisplay = "给用户提供的下载文件名";
		 String filenamedownload =fileName;
//		 String filedisplay = "给用户提供的下载文件名";
		 File f=new File(filenamedownload);
		 String filedisplay=f.getName();
		 filedisplay = URLEncoder.encode(filedisplay,"UTF-8");
		 response.addHeader("Content-Disposition","attachment;filename=" + filedisplay);
		 OutputStream outp = null;
		 FileInputStream in = null;
		 try{
			 outp = response.getOutputStream();
			 in = new FileInputStream(filenamedownload);
			 byte[] b = new byte[1024];
			 int i = 0;
			 while((i = in.read(b)) > 0) {
				 outp.write(b, 0, i);
			 }
			 outp.flush();
		 }catch(Exception e) {
			 System.out.println("Error!");
			 e.printStackTrace();
		 }finally{
			 if(in != null)
			 {
				 in.close();
				 in = null;
			 }
			 if(outp != null)
			 {
				 outp.close();
				 outp = null;
			 }
		 }
	}
	
	/**
	 * 得到一个form表单
	 * @param url
	 * @param filename
	 * @param path
	 * @return
	 */
	public String getUploadForm(String url ,String path){
		StringBuilder sb=new StringBuilder();
		sb.append("<form id='form1' enctype='multipart/form-data' method='post' data-a='"+url+"' action='"+url+"'>");
		sb.append("<input type='file' name='filename'/>");
		sb.append("<input id='path' type='text' name='path' value='"+path+"' />");
		sb.append("<input type='button' onclick='upload()' value='提交' />");
		sb.append("</form>");
		sb.append("<script type=\"text/javascript\">\r\n");
		sb.append("function upload(id){\r\n");
		sb.append("var obj=document.getElementById('form1');\r\n");
		sb.append("var path=document.getElementById('path');\r\n");
		sb.append("var f=obj.attributes['data-a'];\r\n");
		sb.append("obj.action=f.value+'?path='+path.value;\r\n");
		sb.append("obj.submit();\r\n");
		sb.append("}\r\n");
		sb.append("</script>\r\n");
		return sb.toString();
	}
	
	/**
	 * 删除文件
	 * @param sPath
	 * @return
	 */
	public boolean deleteFile(String sPath) {  
		Boolean flag=false;
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}
	
	/**
	 * 删除目录
	 * 
	 * @param sPath
	 * @return
	 */
	public Boolean  deleteDirectory(String sPath){
		Boolean flag=false;
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() ) {  
	        return false;  
	    }
	    if(dirFile.isFile()){
	    	return deleteFile(sPath);
	    }
	    flag = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            flag = deleteFile(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        } //删除子目录  
	        else {  
	            flag = deleteDirectory(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        }  
	    }  
	    if (!flag) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}
	
	/**
	 * 上传
	 * @param rp 
	 */
	public void upload(HttpServletRequest request,String filename, Boolean rp){
		FileUpload jsp=new FileUpload();
		jsp.setRequest(request);
		jsp.setUploadPath(filename);
		jsp.setRp(rp);
		jsp.process();
		jsp.getUpdFileNames();
	}
	
	/**
	 * 一个简易的文件浏览器
	 */
	public String getSimpleFile(String download,String url,String path,String delete){
		try {
			path= new String(path.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		List<File> list=listFile(path);
		StringBuilder sb=new StringBuilder();
		sb.append("<a href='form.do?path="+path+"'>upload</a>");
		sb.append("<ul>");
		File parent=new File(path).getParentFile();
		if(parent!=null){
			sb.append("<li><a  href='");
			sb.append(url);
			String absolutePath=parent.getAbsolutePath();
			try {
				absolutePath=URLEncoder.encode(absolutePath, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append(absolutePath);
			sb.append("'>....</a></li>");
		}
		for (int i = 0; i < list.size(); i++) {
			File f=list.get(i);
			if(f.isFile()){
				sb.append("<li><a style='color:#000;' href='");
				sb.append(download);
				String absolutePath=f.getAbsolutePath();
				try {
					absolutePath=URLEncoder.encode(absolutePath, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sb.append(absolutePath);
				sb.append("'>");
				sb.append(f.getName());
				sb.append("</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id='data_"+i+"' data-f='"+delete+absolutePath+"' href=\"javascript: deleteFile('data_"+i+"')\">delete</a></li>");
			}else{
				sb.append("<li><a href='");
				sb.append(url);
				String absolutePath=f.getAbsolutePath();
				try {
					absolutePath=URLEncoder.encode(absolutePath, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sb.append(absolutePath);
				sb.append("'>");
				sb.append(f.getName());
				sb.append("</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id='data_"+i+"' data-f='"+delete+absolutePath+"' href=\"javascript: deleteFile('data_"+i+"')\">delete</a></li>");
				sb.append("</a></li>");
			}
		}
		sb.append("</ul>");
		sb.append("<script type=\"text/javascript\">\r\n");
		sb.append("function deleteFile(id){\r\n");
		sb.append("if (confirm('confirm delete?')) {\r\n");
		sb.append("var obj=document.getElementById(id);\r\n");
		sb.append("var f=obj.attributes['data-f'];\r\n");
		sb.append("window.open(f.value);\r\n");
		sb.append("}\r\n");
		sb.append("}\r\n");
		sb.append("</script>\r\n");
		return sb.toString();
	}
}
