package kkd.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 模板工具类
 * 
 * @author tabin
 * 
 */
public class TempleteUtil {
	private static final Map<String, String> templetes = new HashMap<String, String>();

	/**
	 * 解析配置文件 并将其缓存
	 * 
	 * @param path
	 *            模版文件夹
	 * 
	 */
	public static void parse(String path) {
		try {
			// System.out.println("解析配置文件 :" + resource);
			File file = new File(path);
			File[] files = file.listFiles();
			if (file != null) {
				for (File f : files) {
					if(f.isFile()){
						String fileName=f.getName();
						if(!StringUtil.isEmpty(fileName)&&fileName.lastIndexOf(".html")>0){
							Integer pos=fileName.lastIndexOf('.');
							InputStream is=null;
							BufferedReader br =null;
							try{
								is=new FileInputStream(f);
								StringBuffer buffer=new StringBuffer();
								br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
								String temp;
								while ((temp = br.readLine()) != null) {
									buffer.append(temp);
									buffer.append("\n");
								}
								templetes.put(fileName.substring(0, pos), buffer.toString());
							}finally{
								if(is!=null){
									is.close();
								}
								if(br!=null){
									br.close();
								}
							}
							
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("模版加载异常！");
		}
	}

	/**
	 * 获取内容 并替换内容
	 * {[替换的内容]}
	 * @param key
	 *            模版名称
	 * @param param
	 *            替换参数
	 * @return
	 */
	public static String get(String key, Map<String, String> param) {
		String rs = templetes.get(key);
		if (param != null && rs != null) {
			Set<Entry<String, String>> set = param.entrySet();
			for (Entry<String, String> entry : set) {
				rs = rs.replace("{["+entry.getKey()+"]}", entry.getValue());
			}
		}
		return rs;
	}
	public static void main(String[] args) {
		TempleteUtil.parse("E:/webGame1/KKDUserWeb/WebContent/WEB-INF/templetes/");
		Map<String,String> param=new HashMap<String, String>();
		param.put("title", "这是标题");
		String content=TempleteUtil.get("activateEmail", param);
		System.out.println(content);
	}
}
