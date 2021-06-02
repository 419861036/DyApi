package kkd.common.util.exp;

import java.util.HashMap;
import java.util.Map;

import kkd.common.util.StringUtil;

/**
 * 简单占位符处理类
 * 主要解决：
 * 1、sql 占位符处理
 * 2、各种格式化替换
 * @author tanbin
 *
 */
public class Formatter {
	
	private String formatStartKey="#{";
	private String formatEndKey="}";
	private Map<String,Integer> codePos;
	private Map<Integer,String> posCode;
	private Map<String,String> replaceMap;;
	private Map<Integer,CodeInfo> codeInfoMap;
	private boolean putCodePos;
	private boolean putPosCode;
	private boolean putCodeInfo;
	//大小写敏感
	private boolean toCase;
	
	/**
	 * 解析占位符
	 * 例如：重庆市-${系统_年4 }-${系统_月}-${系统_序号_01}-<${ss_area}>-<${pipe_type}>-${随机数_01}
	 * @param format
	 * @param newFormat 新的字符串
	 * @param defaultReplace 所有占位符替换成 目标字符串
	 * String 占位符
	 * Integer 顺序
	 */
	public void parse(String format,StringBuilder newFormat,String defaultReplace) {
		String startKey=formatStartKey;
		String endKey=formatEndKey;
		int start=0;
		
		codePos=new HashMap<>();
		posCode=new HashMap<>();
		codeInfoMap=new HashMap<>(); 
		int i=0;
		while(true){
			int startIndex=format.indexOf(startKey,start);
			if(startIndex==-1){
				newFormat.append(format.substring(start, format.length()));
				break;
			}
			int endIndex=format.indexOf(endKey,startIndex+formatStartKey.length());
			if(endIndex==-1){
				newFormat.append(format.substring(start, format.length()));
				break;
			}else{
				int s=startIndex+formatStartKey.length();
				String oldKey=format.substring(s, endIndex);
				String key=oldKey.trim();
				key=key.trim();
				if(toCase){
					key=key.toUpperCase();
				}
				if(putCodePos){
					codePos.put(key, i);
				}
				if(putPosCode){
					posCode.put(i, key);
				}
				if(putCodeInfo){
					CodeInfo codeInfo=new CodeInfo();
					codeInfo.setStartIndex(s);
					codeInfo.setEndIndex(endIndex);
					codeInfo.setKey(oldKey);
					codeInfoMap.put(i, codeInfo);
				}
				
				i++;
				format.substring(0, 1);
				newFormat.append(format.substring(start,startIndex));
				if(replaceMap==null){
					if(StringUtil.isEmpty(defaultReplace)){
						newFormat.append(startKey);
						newFormat.append(key);
						newFormat.append(endKey);
					}else{
						newFormat.append(defaultReplace);
					}
				}else{
					if(StringUtil.isEmpty(replaceMap.get(key))){
						newFormat.append(key);
					}else{
						newFormat.append(replaceMap.get(key));
					}
				}
			}
			start=endIndex+formatEndKey.length();
		}
	}

	/**
	 * 按照位置替换字符串
	 * @param map
	 * @param replaceMap
	 * @return
	 */
//	a{
//	1-2
//	3-5
//	6-10
//}
	public String updateFormat(String s,Map<Integer,CodeInfo> map,Map<Integer,String> replaceMap){
		int startKeyLen=formatStartKey.length();
		int endKeyLen=formatEndKey.length();
		int size=map.size();
		if(size!=0){
			StringBuilder newsb=new StringBuilder();
			for (int i = 0; i < size; i++) {
				CodeInfo e=map.get(i);
				if(i==0){
					newsb.append(s.substring(0, e.getStartIndex()-startKeyLen));
					newsb.append(replaceMap.get(i));
				}
				if(i>0 ){
					//上个数据作为开始
					CodeInfo last=map.get(i-1);
					newsb.append(s.substring(last.getEndIndex()+endKeyLen, e.getStartIndex()-startKeyLen));
					newsb.append(replaceMap.get(i));
				}
				if(i==size-1){
					newsb.append(s.substring(e.getEndIndex()+endKeyLen));
				}
			}
			return newsb.toString();
		}else{
			return s;
		}
	}

	public void setFormatStartKey(String formatStartKey) {
		this.formatStartKey = formatStartKey;
	}

	public void setFormatEndKey(String formatEndKey) {
		this.formatEndKey = formatEndKey;
	}

	public Map<String, Integer> getCodePos() {
		return codePos;
	}

	public Map<Integer, String> getPosCode() {
		return posCode;
	}

	public void setPutCodePos(Boolean putCodePos) {
		this.putCodePos = putCodePos;
	}

	public void setPutPosCode(Boolean putPosCode) {
		this.putPosCode = putPosCode;
	}
	public void setToCase(Boolean toCase){
		this.toCase=toCase;
	}
	
	public void setPutCodeInfo(boolean putCodeInfo) {
		this.putCodeInfo = putCodeInfo;
	}
	


	public Map<Integer, CodeInfo> getCodeInfoMap() {
		return codeInfoMap;
	}



	public void replace(String key,String val){
		if(replaceMap==null){
			replaceMap=new HashMap<>();
		}
		if(toCase){
			replaceMap.put(key.toUpperCase(), val);
		}else{
			replaceMap.put(key, val);
		}
		
	}
	
	public static void main(String[] args) {
		String format="insert into Test(id,name) values('#{Id }',#{namE})";
		StringBuilder sb=new StringBuilder();
		Formatter f=new Formatter();
		f.setToCase(true);
		f.setPutPosCode(true);
		f.replace("id", "1");
		f.replace("name", "'张三'");
		f.parse(format, sb, "?");
		
		System.out.println(sb);
	}
}
