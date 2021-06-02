package kkd.common.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import kkd.common.bandlimit.BandwidthLimiter;
import kkd.common.bandlimit.DownloadLimiter;
import kkd.common.exception.KKDNetworkExcepttion;
import kkd.common.exception.KKDValidationException;
import kkd.common.logger.LogWriter;

/**
 * http请求操作类
 * 
 * @author tanbin
 * 
 */
public class HttpUtil {
	
	public static final int bufferSize=1024;
	
	private static MyX509TrustManager xtm = new MyX509TrustManager();
    private static MyHostnameVerifier hnv = new MyHostnameVerifier();
    
	static{
		
		 SSLContext sslContext = null;
	        try {
	            sslContext = SSLContext.getInstance("TLS"); //或SSL
	            X509TrustManager[] xtmArray = new X509TrustManager[] {
	            		xtm
	            		};
	            
	            sslContext.init(null, xtmArray, new java.security.SecureRandom());
	        } catch (GeneralSecurityException e) {
	            LogWriter.error("",e);
	        }
	        if (sslContext != null) {
	            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
	        }
	        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
	}
//	public interface AsyncReader {
//		/**
//		 * @param e
//		 *            KKDNetworkExcepttion 异常
//		 * @return false 不再重试
//		 */
//		public boolean error(KKDNetworkExcepttion e);
//
//		/**
//		 * @param data
//		 *            数据
//		 * @return false 不再重试
//		 */
//		public boolean read(String data);
//	}
//
//	/**
//	 * 
//	 * @param url
//	 *            url地址
//	 * @param params
//	 *            参数
//	 * @param retry
//	 *            重试次数
//	 */
//	@Deprecated
//	public static void asyncPost(String url, Map<String, String> params,
//			AsyncReader reader) {
//		asyncPost(url, params, reader, 0);
//	}
//
//	/**
//	 * 异步网络请求
//	 * 
//	 * @param url
//	 *            网址
//	 * @param params
//	 *            参数
//	 * @param reader
//	 *            读取器 (接口自己实现 如果不需要返回数据 可设置为空)
//	 * @param retry
//	 *            异常情况重试次数
//	 * @see AsyncReader
//	 * @author tanbin
//	 */
//	@Deprecated
//	public static void asyncPost(String url, Map<String, String> params,
//			AsyncReader reader, Integer retry) {
//
//		class AsyncThread implements Runnable {
//
//			int count = 0;
//			Map<String, String> params = null;
//			AsyncReader reader = null;
//			String url = null;
//
//			// 禁用无参构造方法
//			@SuppressWarnings("unused")
//			private AsyncThread() {
//			}
//
//			public AsyncThread(String url, Map<String, String> params,
//					AsyncReader reader, Integer retry) {
//				this.reader = reader;
//				this.url = url;
//				this.params = params;
//				this.count = retry == null ? 0 : retry;
//			}
//
//			@Override
//			public void run() {
//				for (int i = -1; i < count; i++) {
//					try {
//						String data = HttpUtil.post(url, params);
//						if (reader != null) {
//							if (!reader.read(data)) {
//								break;
//							}
//						}
//						break;
//					} catch (KKDNetworkExcepttion e) {
//						if (reader != null && count - 1 == i) {
//							if (reader.error(e)) {
//								break;
//							}
//						} else {
//							try {
//								Thread.sleep(3000);// 等待3秒后执行
//							} catch (InterruptedException e1) {
//								e1.printStackTrace();
//							}
//						}
//					}
//				}
//
//			}
//		}
//		AsyncThread async = new AsyncThread(url, params, reader, retry);
//		Thread t = new Thread(async);
//		t.setDaemon(true);
//		t.start();
//	}

	/**
	 * @author tanbin http请求
	 * @param url
	 *            请求url
	 * @return
	 */
	public static String post(String url) {
		return post(url, null, null);
	}

	/**
	 * @author tanbin http请求
	 * @param url
	 *            请求url
	 * @param params
	 *            将参数封装成map
	 * @return
	 */
	public static String post(String url, Map<String, String> params) {
		return post(url, params, null);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数
	 * @param timeout
	 *            超时
	 * @return
	 */
	public static String post(String url, Map<String, String> params,
			Integer timeout) {
		return post(url, params, timeout, true);
	}
	
	/**
	 * post请求
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数
	 * @param timeout
	 *            超时
	 * @return
	 */
	public static String post(String url, Map<String, String> params,
			Integer timeout, boolean debug) {
		Map<String,String> opts=new HashMap<String, String>();
		if(timeout!=null){
			opts.put("timeout", timeout.toString());
		}
		opts.put("debug", Boolean.valueOf(debug).toString());
		opts.put("charsetName", "UTF-8");
		opts.put("postType", "POST");
		return httpSendData(url, params, opts);
	}
	public static String get(String url, Map<String, String> params,
			Integer timeout, boolean debug) {
		Map<String,String> opts=new HashMap<String, String>();
		if(timeout!=null){
			opts.put("timeout", timeout.toString());
		}
		opts.put("debug", Boolean.valueOf(debug).toString());
		opts.put("charsetName", "UTF-8");
		opts.put("postType", "GET");
		return httpSendData(url, params, opts);
	}
	
	
	
	
	
	
	/**
	 * http 发送消息 最新推荐用法
	 * @param url
	 * @param params
	 * @param opts debug timeout charsetName
	 * @return
	 */
	public static String httpSendData(String url, Map<String, String> params,Map<String, String> opts){
		
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			Set<Map.Entry<String, String>> key = params.entrySet();
			for (Iterator<Map.Entry<String, String>> it = key.iterator(); it
					.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				sb.append(entry.getKey());
				sb.append("=");
				String v=entry.getValue();
				if(v!=null){
					sb.append(v);
				}
				if (it.hasNext()) {
					sb.append("&");
				}
			}
		}
		return HttpUtil.httpSendData(url, sb.toString(), opts);
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param url
	 * @param paramStr
	 * @param opts key:debug,timeout,charsetName
	 * @return
	 */
	public static String httpSendData(String url,String paramStr,Map<String, String> opts){
		if (StringUtil.isEmpty(url)) {
			throw new KKDValidationException("url不能为空");
		}
		String tmp = httpSendBase(url, paramStr, opts,null);
		return tmp;
	}
	public static String httpSendBase(String url, String paramStr,
			Map<String, String> opts, Map<String, String> head){
		return httpSendBase1(url, paramStr, opts, head, null);
	}
	public static String httpSendBase1(String url, String paramStr,
			Map<String, String> opts, Map<String, String> head,Map<String,List<String>> respHead) {
//		System.getProperties().put("proxySet", "true");
//		System.getProperties().put("proxyHost", "127.0.0.1");
//		System.getProperties().put("proxyPort", "8888");
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
		Boolean debug=true;
		Integer timeout=null;
		String charsetName="UTF-8";
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		OutputStreamWriter osw = null;
		OutputStream os=null;
		InputStream is=null;
		InputStream errorIs=null;
		String tmp = null;
		// 尝试发送请求
		try {
			if (paramStr != null) {
				sb.append(paramStr);
			}
			if(opts!=null&&opts.size()!=0){
				String debugStr=opts.get("debug");
				if(debugStr!=null){
					debug=Boolean.valueOf(debugStr);	
				}
				String timeoutStr=opts.get("timeout");
				if(timeoutStr!=null){
					timeout=Integer.valueOf(timeoutStr);	
				}
				String charsetNameStr=opts.get("charsetName");
				if(charsetNameStr!=null){
					charsetName=charsetNameStr;	
				}
			}
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			if (timeout != null) {
				con.setConnectTimeout(1000 * timeout);
				con.setReadTimeout(1000 * timeout);
			} else {
				con.setConnectTimeout(1000 * 60);
				con.setReadTimeout(1000 * 60);
			}
			con.setRequestMethod(opts==null||opts.get("postType")==null?"POST":opts.get("postType"));
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
//			con.setRequestProperty("Content-Type",
//			"application/x-www-form-urlencoded");
//			con.setRequestProperty("User-Agent",
//			"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
			if(head != null && !head.isEmpty()) {
				for (Map.Entry<String, String> e: head.entrySet()) {
					System.out.print(e.getKey());
					System.out.print(":");
					System.out.println(e.getValue());
					con.setRequestProperty(e.getKey(),e.getValue());
				}
			}
			if(con.getRequestMethod().equals("POST")){
				os=con.getOutputStream();
				osw = new OutputStreamWriter(os, charsetName);
				osw.write(sb.toString());
				osw.flush();
				if (osw != null) {
					try {
						osw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			// 读取返回内容
//			con.connect();
//            int respCode = con.getResponseCode(); // 这里内部发送请求
//            if (HttpURLConnection.HTTP_OK != respCode) {
//            	return null;
//            }
				// if(HttpURLConnection.HTTP_OK==con.getResponseCode()){
			if(HttpURLConnection.HTTP_OK==con.getResponseCode()){
				is=con.getInputStream();
				if(respHead!=null){
					Map<String,List<String>> respHead1=con.getHeaderFields();
					for (Map.Entry<String,List<String>> ele : respHead1.entrySet()) {
						respHead.put(ele.getKey(), ele.getValue());
					}
				}
				tmp = HttpUtil.readInputStream(is, charsetName);
				if (debug) {
					LogWriter.debug("\r\n请求url:" + url + "\r\n" + "参数：" + sb);
				}
			}else{
				errorIs=con.getErrorStream();
				tmp = HttpUtil.readInputStream(errorIs, charsetName);
				System.out.println(tmp);
				throw new KKDNetworkExcepttion(tmp);
			}
			//
			
		} catch (Exception e) {
			LogWriter.error("",e);
			throw new KKDNetworkExcepttion(e, "url网络异常:" + url + "\r\n" + "参数："
					+ sb);
		} finally {
			close(con, os, is);
		}
		return tmp;
	}

	private static void close(HttpURLConnection con, OutputStream os,
			InputStream is) {
		if(os!=null){
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(is!=null){
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static String readInputStream(InputStream inStream,
			String charsetName) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		String data = null;
		try {
			byte[] buffer = new byte[bufferSize];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			data = outStream.toString(charsetName);// 网页的二进制数据
			return data;
		} catch (Exception e) {
			LogWriter.error("",e);
			return data;
		} finally {
			outStream.close();
			inStream.close();
		}
	}

	/**
	 * 地址是否正常
	 * 
	 * @param urlStr
	 * @return
	 */
	public static boolean isAvailable(String urlStr) {
		URL url;
		try {
			url = new URL(urlStr);
			HttpURLConnection connt = (HttpURLConnection) url.openConnection();
			connt.setRequestMethod("HEAD");
			connt.getResponseMessage();
			return true;
		} catch (MalformedURLException e) {
		} catch (ProtocolException e) {
		} catch (IOException e) {
		}
		return false;
	}

	public static String toHTMLString(String in) {
		StringBuffer out = new StringBuffer();
		for (int i = 0; in != null && i < in.length(); i++) {
			char c = in.charAt(i);
			if (c == '\'')
				out.append("'");
			else if (c == '<')
				out.append("<");
			else if (c == '>')
				out.append(">");
			else if (c == '&')
				out.append("&");
			else if (c == '\n')
				out.append("");
			else
				out.append(c);
		}
		return out.toString();
	}

	public static String upload(String actionUrl,Map<String, String> params,List<String> uploadFiles){
		String[] filesArray = new String[uploadFiles.size()];
		for(int i=0;i<uploadFiles.size();i++){
			filesArray[i] = uploadFiles.get(i);
		}
		return upload(actionUrl,params,filesArray);
	}
	
	public static String upload(String actionUrl,List<String> uploadFiles){
		
		String[] filesArray = new String[uploadFiles.size()];
		for(int i=0;i<uploadFiles.size();i++){
			filesArray[i] = uploadFiles.get(i);
		}
		return upload(actionUrl,null,filesArray);
	}
	
	
	
	public static String upload(String actionUrl,String[] uploadFiles){
		return upload(actionUrl,null,uploadFiles);
	}
	
	
	/**
	 * 
	 * @param actionUrl
	 *            上传地址
	 * @param params
	 *            参数
	 * @param uploadFiles
	 *            文件路径
	 */
	public static String upload(String actionUrl, Map<String, String> params,
			String[] uploadFiles) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = UUIDUtil.getUUID();
		DataOutputStream ds = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(actionUrl);
			con = (HttpURLConnection) url.openConnection();
			// 发送POST请求必须设置如下两行
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			// head end
			ds = new DataOutputStream(con.getOutputStream());
			if(params!=null && !params.isEmpty()){
				Set<Entry<String, String>> sets = params.entrySet();
				for (Entry<String, String> ss : sets) {
					ds.writeBytes(twoHyphens + boundary + end);
					String content = "Content-Disposition: form-data; " + "name=\""
							+ ss.getKey() + "\"" + end + end + ss.getValue();
					ds.write(content.getBytes("UTF-8"));
					ds.writeBytes(end);
				}
			}
			for (int i = 0; i < uploadFiles.length; i++) {
				String uploadFile = uploadFiles[i];
				File file = new File(uploadFile);
				String filename = URLEncoder.encode(file.getName(),"UTF-8");
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("Content-Disposition: form-data; "
						+ "name=\"file" + i + "\";filename=\"" + filename
						+ "\"" + end);
				ds.writeBytes("Content-Type: text/plain"+ end);
				ds.writeBytes(end);
				FileInputStream fStream=null;
				try {
					 fStream = new FileInputStream(uploadFile);
					byte[] buffer = new byte[bufferSize];
					int length = -1;
					while ((length = fStream.read(buffer)) != -1) {
						ds.write(buffer, 0, length);
					}
					ds.writeBytes(end);
					ds.flush();
				} catch (Exception e) {
					LogWriter.error("",e);
				}finally{
					if(fStream!=null){
						fStream.close();	
					}
				}
			}
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();
		} catch (Exception e) {
			LogWriter.error("",e);
			return null;
		} finally {
			if (ds != null) {
				try {
					ds.close();
				} catch (IOException e) {
					LogWriter.error("",e);
				}
			}
		}
		// 读取返回内容
		String tmp = null;
		try {
			tmp = HttpUtil.readInputStream(con.getInputStream(), "UTF-8");
			
		} catch (Exception e) {
			LogWriter.error("",e);
			throw new KKDNetworkExcepttion(e, "url网络异常:" + actionUrl + "\r\n");
		}
		return tmp;
	}
	
	
	

	/**
	 * post请求
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数
	 * @param timeout
	 *            超时
	 * @return
	 */
	public static boolean downloadFile(String url, Map<String, String> params,
			Integer timeout,File fileName) {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			Set<Map.Entry<String, String>> key = params.entrySet();
			for (Iterator<Map.Entry<String, String>> it = key.iterator(); it
					.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
				if (it.hasNext()) {
					sb.append("&");
				}
			}
		}
		OutputStreamWriter osw = null;
		// 尝试发送请求
		try {
//			String userName="admin";
//			String password="admin123";
//			Authenticator.setDefault(new BaseicAuthenticator(userName, password)); 
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			if (timeout != null) {
				con.setConnectTimeout(1000 * timeout);
				con.setReadTimeout(1000 * timeout);
			} else {
				con.setConnectTimeout(1000 * 15);
				con.setReadTimeout(1000 * 15);
			}
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
//			RandomAccessFile randomFile = new RandomAccessFile(fileName, "r");
//			con.setRequestProperty("Range","bytes=" + 1 + "-");
			//TODO 断点下载
//			con.setRequestProperty("Range","bytes=" + randomFile.length() + "-");
//			randomFile.close()
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
//			osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
//			osw.write(sb.toString());
		} catch (Exception e) {
			LogWriter.error("",e);
			throw new RuntimeException(e);
		} finally {
			if (osw != null) {
				try {
					osw.flush();
					osw.close();
				} catch (IOException e) {
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
		// 读取返回内容
		try {
			// if(HttpURLConnection.HTTP_OK==con.getResponseCode()){
			return  HttpUtil.readInputStream(con.getInputStream(), "UTF-8",fileName);
		} catch (Exception e) {
			LogWriter.error("",e);
			throw new RuntimeException("网络异常请稍后重试",e);
		} finally {
		}
	}

	public static boolean readInputStream(InputStream inStream,
			String charsetName,File fileName) throws Exception {
		File f=fileName;
		if(f.exists()){
			f.delete();
		}else{
			File parent=f.getParentFile();
			if(!parent.exists()){
				parent.mkdirs();
			}
			f.createNewFile();
		}
		InputStream dl=new DownloadLimiter(inStream, new BandwidthLimiter(300));
		RandomAccessFile randomFile = new RandomAccessFile(f, "rw");
//		FileOutputStream fos=new FileOutputStream(f);
		long lens=randomFile.length();
		lens=lens<0?0:lens;
		randomFile.seek(lens);
		try {
			byte[] buffer = new byte[bufferSize];
			int len = 0;
			long i=0;
			while ((len = dl.read(buffer)) != -1) {
				randomFile.write(buffer, 0, len);
				i++;
				if(i%1024==0){
					System.out.println(i/1024+"M");
				}
//				Thread.sleep(4);
			}
			return true;
		} catch (Exception e) {
			LogWriter.error("",e);
			throw new RuntimeException(e);
		} finally {
			try {
				randomFile.close();
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
			try {
				dl.close();
			} catch (Exception e2) {
				throw new RuntimeException(e2);
			}
			
		}
	}
	
	
	
	public static void main(String[] args) throws IOException {
//		String[] strs = {"c:"+File.separator+"2.jpg","d:\\IMG_20140201_085946.jpg"};
//		
//		Map<String,String> mm = new HashMap<String,String>();
//		mm.put("2.jpg", "s");
//		String tmp = upload("http://127.0.0.1/KKDWeb/ControlServlet/upload/gamepic.do",mm,strs);
		//File file = new File("d:\\a\\..\\ll");
//		String s = "d:/a/............/ll";
//		String s2 = "a\\.......................\\";
//		System.out.println(s.matches(".*/\\.\\.*/.*"));
//		System.out.println(s2.matches(".*\\\\\\.\\.*\\\\.*"));
		
		String l = "0";
		Integer.getInteger(l);
		System.out.println(Integer.getInteger(l));
		System.out.println(Integer.valueOf(l));
		//System.out.println(File.separator);
//		String xxx = HttpUtil.httpSendData("https://api.mch.weixin.qq.com/pay/unifiedorder","",null);
//		System.out.println(xxx);
	}
}
