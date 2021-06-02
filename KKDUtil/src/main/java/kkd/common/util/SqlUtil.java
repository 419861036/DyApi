package kkd.common.util;

import java.util.ArrayList;
import java.util.List;

import kkd.common.util.FileUtil.FileUtilReader;

/**
 * sql脚本解析
 * 解析成单个sql语句
 * @author tanbins
 *
 */
public class SqlUtil {
	
public static class DELIMITER{
		private String value;
		
		private DELIMITER(String value){
			this.value=value;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value){
			
		}
		public static DELIMITER createDELIMITER(String value){
			DELIMITER d=new DELIMITER(value);
			return d;
		}

		
		
	}
	/**
	 * 解析sql脚本文件
	 * @param sql
	 * @param normalDELIMITER	普通风格符
	 * @param startSegment		特殊开始分隔符 处理存储过程
	 * @param endSegment		特殊结束分隔符
	 * @return
	 */
	public static List<String> parseSql(String sql,DELIMITER normalDELIMITER,DELIMITER startSegment,DELIMITER endSegment){
		final StringBuilder sb=new StringBuilder();
		final DELIMITER normal=normalDELIMITER==null?DELIMITER.createDELIMITER(";"):normalDELIMITER;
		final DELIMITER start=startSegment==null?DELIMITER.createDELIMITER("DELIMITER ;;"):startSegment;
		final DELIMITER end=endSegment==null?DELIMITER.createDELIMITER("DELIMITER ;"):endSegment;
		final List<String> sqlList=new ArrayList<String>();
		FileUtil.readFileByLines(sql, new FileUtilReader() {
			
			DELIMITER curDELIMITER=normal;
			@Override
			public void read(Object data) {
				String line=(String)data;
				
				if(!StringUtil.isEmpty(line)){
					line=line.trim();
					if(line.startsWith("--")||line.startsWith("#")){
						return;
					}else{
						 if(line.indexOf("--")!=-1){//处理行内注释
								line = doCommentInLine(line);
						}
						//start
						if(line.endsWith(start.getValue())){
							if(curDELIMITER.getValue().equals(start.getValue())){
								throw new RuntimeException("格式错误");
							}
							curDELIMITER=start;
							sb.setLength(0);
						//end 结束标记	
						}else if(line.endsWith(end.getValue())){
							if(curDELIMITER.getValue().equals(start.getValue())){
								sqlList.add(sb.toString());
							}else if(curDELIMITER.getValue().equals(end.getValue())){
								throw new RuntimeException("格式错误");
							}
							curDELIMITER=normal;
							sb.setLength(0);
						//当前标记
						}else if(line.endsWith(curDELIMITER.getValue())){
							sb.append(line);
							sqlList.add(sb.toString());
							sb.setLength(0);
						}else{
							sb.append(line+"\r\n");
						}
					}
				}
			}
			
		});
		if(!StringUtil.isEmpty(sb.toString())){
			sqlList.add(sb.toString());
		}
		return sqlList;
	}
	
	private static String doCommentInLine(String line) {
		char[] cs=line.toCharArray();
		int count=0;
		int count1=0;
		boolean inStr=false;//在字符串中
		for(int i=0;i<cs.length;i++){
			Character c0=null;
			Character c=cs[i];
			Character c1=null;
			if(i!=0){
				c0=cs[i-1];
			}
			if(i!=cs.length-1){
				c1=cs[i+1];
			}
			if('\''==c&&c0!='\\'){//'
				count++;
				if(count%2==1){
					inStr=true;
				}else{
					inStr=false;
				}
			}
			if('`'==c&&c0!='\\'&&!inStr){//'
				count1++;
			}
//							System.out.print(c);
			if(c=='-'&&c1=='-'){//--
				if(count%2==0&&count1%2==0){
					line=line.substring(0, i).trim();
					break;
				}
			}
		}
		return line;
	}
}
