package kkd.common.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Deprecated
public class DownLoadServlet {
	public void downLoadFile(HttpServletRequest req, HttpServletResponse resp) throws IOException{
//		 resp.reset();
      
       String fileName = req.getParameter("file");
       System.out.println("The file is:" + fileName);
       OutputStream os = null;
       FileInputStream is = null;
       long since = 0;
       long until = 0;
       try {
           File f = new File("F:\\softwarfactory\\software\\" + fileName);
           is = new FileInputStream(f);
           long fSize = f.length();
           byte xx[] = new byte[4096];
//           resp.setHeader(" Content-Type", "application/octet-stream");
           resp.setHeader("Accept-Ranges", "bytes");
           resp.setHeader("Content-Disposition", "attachment;filename="
                   + fileName);
           if (req.getHeader("Range") != null) {
               // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
        	   String range = req.getHeader("Range");
               resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
//               pos = Long.parseLong(req.getHeader("Range")
//                       .replaceAll("bytes=", "").replaceAll("-", ""));
             //剖解range
				range=range.split("=")[1];
				String[] rs=range.split("-");
				if(rs.length==2){
					since=Long.parseLong(rs[0]);
					until=Long.parseLong(rs[1]);
				}else{
					since=Long.parseLong(rs[0]);
					until=fSize-1;
				}
				
           }else{
        	   until = fSize-1;
           }
//           resp.setHeader("Content-Length", (until -since+1)+ "");
           resp.setHeader("Content-Length", (until -since+1)+ "");
           if (since > 0 && until >0 && since<until ) {
               String contentRange = new StringBuffer("bytes ")
                       .append(new Long(since).toString()).append("-")
                       .append(new Long(until).toString()).append("/")
                       .append(new Long(fSize).toString()).toString();
               resp.setHeader("Content-Range", contentRange);
               System.out.println("Content-Range=" + contentRange);
               // 略过已经传输过的字节
               is.skip(since);
           }
           os = resp.getOutputStream();
           boolean all = false;
           long n=0;
           long jilu = 0;
           //3-4/5
           while((n=is.read(xx))>0){
        	   jilu+=n;
        	   if(jilu+since>=until){ 
        		  n =  n - (jilu+since- until);
        		  all = true;
        	   }
        	   os.write(xx, 0, (int)n);
        	   if(all){
        		   break;
        	   }
           }
       } catch (IOException e) {
           e.printStackTrace();
           return;
       } finally {
           if (is != null)
               is.close();
           if (os != null)
               os.close();
       }
   }
//	protected void service(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		//获取文件URI
//		  String fileName = request.getParameter("file");
//	       System.out.println("The file is:" + fileName);
////		String path = URLDecoder.decode(request.getRequestURI().replace(
////				getServletContext().getContextPath() + "/download", ""),"UTF-8");
//		//获取文件
//		File file = new File("F:\\softwarfactory\\software\\" + fileName);
//	
//		//获取文件读取对象
//		OutputStream os = null;
//	      FileInputStream is = null;
//	      
//		//获取浏览器类型
//		String browser=request.getHeader("user-agent");
//		// 设置响应头，206支持断点续传
//		int http_status=206;
//		if(browser.contains("MSIE"))
//			http_status=200;//200 响应头，不支持断点续传
//		response.reset();
//		response.setStatus(http_status);
//		//响应头
//		response.setContentType("application/octet-stream;charset=UTF-8");
//		try {
//			//下载起始位置
//			long since=0;
//			//下载结束位置
//			
//			long until=file.length()-1;
//			//获取Range，下载范围
//			String range=request.getHeader("range");
//			if(range!=null){
//				//剖解range
//				range=range.split("=")[1];
//				String[] rs=range.split("-");
//				since=Integer.parseInt(rs[0]);
//				until=Integer.parseInt(rs[1]);
//				
//			}
//			//设置响应头
//			response.setHeader("Accept-Ranges", "bytes");
//			response.setHeader("Content-Range", "bytes "+since+"-"+ until + "/"
//					+ file.length());
//			//文件名用ISO08859_1编码
//			response.setHeader("Content-Disposition", "attachment; filename=\"" +
//					new String(fileName.getBytes(),"ISO8859_1")+ "\"");
//			response.setHeader("Content-Length", "" + (until-since+1));
//			System.out.println("download: "+fileName);
//			//定位到请求位置
//			fr.seek(since);
//			byte[] buffer=new byte[128*1024];
//			int len;
//			boolean full=false;
//			//读取，输出流
//			while((len=fr.read(buffer))>0){
//				if(fr.tell()-1>until){
//					len=(int) (len-(fr.tell()-until-1));
//					full=true;
//				}
//				response.getOutputStream().write(buffer, 0, len);
//				if(full)
//					break;
//			}
//			//输出
//			response.getOutputStream().flush();
//			response.getOutputStream().close();
//		} catch (java.net.SocketException e) {
//			//连接断开
//		}finally{
//			if(fr!=null)
//				fr.close();
//		}
//		response.flushBuffer();
//	}


		
		
		
	}

