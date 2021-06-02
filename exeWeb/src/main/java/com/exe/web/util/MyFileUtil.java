package com.exe.web.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.util.FileUtil;
import kkd.common.util.StringUtil;

public class MyFileUtil {
	private final static Logger logger = LoggerFactory.getLogger(MyFileUtil.class);

	/**
	 * 根据路径 得到path
	 * 
	 * @param url
	 * @return
	 */
	public static String getPath(String url) {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			try {
				URL u = new URL(url);
				url = u.getPath();
				return url;
			} catch (MalformedURLException e) {
				logger.error("", e);
			}
		}
		return url;
	}

	public static String getDir(String url) {
		String dir = null;
		if (url.startsWith("http://") || url.startsWith("https://")) {
			try {
				URL u = new URL(url);
				url = u.getPath();
				dir = url;
			} catch (MalformedURLException e) {
				logger.error("", e);
			}
		}
		dir = url;
		int pos = dir.lastIndexOf("/");
		dir = dir.substring(0, pos + 1);
		return dir;
	}

	/**
	 * 获取文件名
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
		Integer pos = url.lastIndexOf("/");
		String filename = "";
		if (pos != -1) {
			filename = url.substring(pos + 1);
		} else {
			filename = url;
		}
		return filename;
	}

	/**
	 * 判断是url 还是path
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isPath(String url) {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return false;
		}
		return true;
	}

	public static String getIndexFileName(String string) {
		for (int j = 1; j < 999999; j++) {
			String newFile = string.replace("{num}", j + "");
			File f = new File(newFile);
			if (f.exists()) {
				continue;
			}
			return f.getAbsolutePath();
		}
		return null;
	}

	public static String getRootFolder() {
		String file = MyFileUtil.class.getResource("/").getFile();
		return file;
	}

	public static String getText(String fileName) {
		final StringBuilder sb = new StringBuilder();
		FileUtil.FileUtilReader f = new FileUtil.FileUtilReader() {

			public void read(Object data) {
				sb.append(data.toString() + "\r\n");
			}
		};
		String file = getRootFolder();
		logger.debug("路径：{}", file);
		FileUtil.readFileByLines(file + fileName, f, "utf-8");
		return sb.toString();
	}

	

	


	public static void main(String[] args) {
//		String s=getIndexFileName("F:/data/downlist{num}.txt");
//		LogUtil.log("{}",s);
		// String txt=getText("rule.txt");
		// logger.debug("{}",txt);
		File f=new File("F:\\code_store\\new_yqyx\\module\\exe\\exeWeb\\target");
		try {
			System.out.println(f.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
