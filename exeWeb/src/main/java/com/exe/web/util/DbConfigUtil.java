package com.exe.web.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.util.FileUtil;


/**
 * 配置文件处理类
 * @author tanbin
 *
 */
public class DbConfigUtil {
	private final static Logger logger = LoggerFactory.getLogger(DbConfigUtil.class);
	public static String getConfig(){
		final StringBuilder sb=new StringBuilder();
		FileUtil.FileUtilReader f=new FileUtil.FileUtilReader() {
			
			public void read(Object data) {
				sb.append(data.toString()+"\r\n");
			}
		}; 
		String conf=MyConfUtil.getRootPath()+File.separator+"conf"+File.separator;
		logger.debug("路径："+conf);
		FileUtil.readFileByLines(conf+"dbcp.properties", f, "utf-8");
		return sb.toString();
	}
}
