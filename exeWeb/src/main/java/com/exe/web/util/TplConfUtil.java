package com.exe.web.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.util.FileUtil;


/**
 * 配置文件处理类
 * @author tanbin
 *
 */
public class TplConfUtil {
	private final static Logger logger = LoggerFactory.getLogger(TplConfUtil.class);
	public static String getConfig(){
		final StringBuilder sb=new StringBuilder();
		FileUtil.FileUtilReader f=new FileUtil.FileUtilReader() {
			
			public void read(Object data) {
				sb.append(data.toString()+"\r\n");
			}
		}; 
		String conf=MyConfUtil.getRootPath()+File.separator+"conf"+File.separator;
		logger.debug("路径：{}",conf);
		FileUtil.readFileByLines(conf+"velocity.properties", f, "utf-8");
		return sb.toString();
	}
	
	public static Properties getConf(){
		String conf=getConfig();
		Properties p = new Properties();
		ByteArrayInputStream b=new ByteArrayInputStream(conf.getBytes());
		try {
			p.load(b);
		} catch (IOException e) {
			logger.error("", e);
		}
		return p;
	}
}
