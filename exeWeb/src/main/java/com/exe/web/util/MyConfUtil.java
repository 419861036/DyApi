package com.exe.web.util;

import java.io.File;
import java.io.IOException;

import kkd.common.util.StringUtil;

public class MyConfUtil {
    
    private static String rootPath;
    public static String getRootPath() {
		if (StringUtil.isEmpty(rootPath)) {
			String rootPath1 = System.getProperty("root");
			if (StringUtil.isEmpty(rootPath1)) {
				rootPath1 = DbConfigUtil.class.getResource("/").getFile() + "/../../src/main";
			}
			File rootFile = new File(rootPath1);
			if (rootFile.isDirectory() && rootFile.exists()) {
				try {
                    rootPath = rootFile.getCanonicalPath();
                    System.setProperty("root", rootPath);
				} catch (IOException e) {
                    //logger.info("",e);
                    e.printStackTrace();
				}
				System.out.println("rootpath is :"+rootPath);
			}else{
				throw new RuntimeException("root path is not found:"+rootPath1);
			}
		}
		return rootPath;
	}

	public static String getStaticPath() {
		String staticPath = System.getProperty("static");
		if (StringUtil.isEmpty(staticPath)) {
			return MyConfUtil.getRootPath()+File.separator+"static"+File.separator;
		}
		return staticPath;
	}
}