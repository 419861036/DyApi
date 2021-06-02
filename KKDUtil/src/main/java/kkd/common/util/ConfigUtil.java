package kkd.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import kkd.common.exception.KKDValidationException;
import kkd.common.logger.LogWriter;


/**
 * 配置文件处理类
 * @author tanbin
 *
 */
public class ConfigUtil {
	
	private static Properties pConfig = null;
	
	public static void setDefaultConfig(String key){
	}
	public static void putSysConfig(String key,String value){
		init(key);
		pConfig.put(key, value);
	}
	
	public static String get(String key){
		init(key);
		String v=System.getProperty(key);
		if(v==null){
			v=pConfig.getProperty(key);
		}
		return v;
	}
	private static void init(String key) {
		if(pConfig==null){
			InputStream innerInputStream=null;
			InputStream outInputStream=null;
			try {
				URL innerU=ConfigUtil.class.getResource("/config.properties");
				if(innerU==null){
					throw new RuntimeException("配置文件存在");
				}
				File innerConfig=FileUtil.urlToFile(innerU);
				String projectConfigPath=EnvUtil.get(EnvUtil.projectConfigPath);
				if(projectConfigPath!=null){
					File outConfig=new File(projectConfigPath+"/config.properties");
					if(outConfig.exists()){
						innerInputStream = new FileInputStream(innerConfig);
						Properties innerP = new Properties();
						innerP.load(innerInputStream);
						
						outInputStream = new FileInputStream(outConfig);
						Properties outP = new Properties();
						outP.load(outInputStream);
						
						String innerVersion=innerP.getProperty("configVersion","0");
						String outVersion=outP.getProperty("configVersion","0");
						Double innerVersionD=Double.parseDouble(innerVersion);
						Double outVersionD=Double.parseDouble(outVersion);
						if(innerVersionD<=outVersionD){
							pConfig=outP;
						}else{
							pConfig=innerP;
							FileUtil.copy(innerConfig, outConfig);
						}
					}else{
						innerInputStream = new FileInputStream(innerConfig);
						Properties p = new Properties();
						p.load(innerInputStream);
						pConfig=p;
						FileUtil.copy(innerConfig, outConfig);
					}
				}else{
					innerInputStream = new FileInputStream(innerConfig);
					Properties p = new Properties();
					p.load(innerInputStream);
					pConfig=p;
				}
			} catch (Exception e) {
				LogWriter.error("配置文件读取异常",e);
				throw new KKDValidationException("key:'"+key+"'配置文件读取异常");
			}finally{
				FileUtil.closeInputStream(innerInputStream);
				FileUtil.closeInputStream(outInputStream);
			}
		}
	}
	
	
	public static void flush(){
		pConfig=null;
	}
	
	public static void main(String[] args) {
		URL u=ConfigUtil.class.getResource("/log4j.properties");
		File f=FileUtil.urlToFile(u);
		System.out.println(f.getAbsolutePath());
		
		
	}
}
