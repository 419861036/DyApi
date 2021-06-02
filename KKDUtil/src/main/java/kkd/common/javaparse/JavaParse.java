package kkd.common.javaparse;


import java.util.LinkedList;

import kkd.common.util.FileUtil;
import kkd.common.util.FileUtil.FileUtilReader;

public class JavaParse {
	public static void main(String[] args) {
		test1();
//		System.out.println(sb);
	}

	private static void test1() {
		final StringBuilder sb=new StringBuilder();
		FileUtil.readFileByLines("E:/workspaceGroup/workspace_new/KKDUtil/src/kkd/common/logger/DefaultLogger.java", new FileUtilReader() {
			
			@Override
			public void read(Object data) {
				sb.append(data+"\r\n");
			}
		});
		String content=sb.toString();
		char[] cs=content.toCharArray();
//		getCommentAndChar(cs);
		parse(content);
//		int pos=getEndPos(cs, 0, '{', '}');
//		System.out.println(pos);
		
	}
	private static LinkedList<Token> spList=null;
	
	/**
	 * 解析主方法
	 * @param content
	 */
	private static void parse(String content) {
		Token rootToken=new Token();
		char[] cs=content.toCharArray();
		//字符 注释等特殊字符
		spList=getCommentAndChar(cs);
		for (int i = 0; i < spList.size(); i++) {
//			System.out.println(new String(cs,spList.get(i).getStart(),spList.get(i).getEnd()-spList.get(i).getStart()+1));
		}
		Key packagekey=getSeg(cs,0,cs.length-1);
		if(Key.t_keyword_package.equals(packagekey.getText())){
			Token packageToken=new Token();
			rootToken.addChild(packageToken);
			packageToken.setType(Key.t_keyword_package);
//			t.set
			Key packageEndKey=getSeg(cs, packagekey.getEndPos(), cs.length-1);
			int endPackage=getPos(cs, packageEndKey.getEndPos(), ';');
			if(endPackage!=-1){
				//包结束
				String s=new String(cs,packagekey.getStartPos(),endPackage-packagekey.getStartPos()+1);
				packageToken.setText(s);
				
				boolean f=true;
//				Key importEndKey1=null;
				int start=packageEndKey.getEndPos();
				
				while(f){
					//处理import
					Key importsKey=null;
					importsKey=getSeg(cs, start,cs.length-1);
					if(Key.t_keyword_import.equals(importsKey.getText())){
						Token importToken=new Token();
						importToken.setType(Key.t_keyword_import);
						rootToken.addChild(importToken);
						int endImport=getPos(cs, importsKey.getEndPos(), ';');
//						Key importEndKey=getSeg(cs, importsKey.getEndPos(), cs.length-1);
						s=new String(cs,importsKey.getStartPos(),endImport-importsKey.getStartPos()+1);
						importToken.setText(s);
						start=endImport;
					}else{
//						Key classKey=getSeg(cs, importEndKey1.getEndPos(),cs.length-1);
						Token clsToken=new Token();
						clsToken.setType(Key.t_keyword_class);
						Key classXsFKey=getSeg(cs, start,cs.length-1);
						Key classClsFKey=getSeg(cs, classXsFKey.getEndPos(),cs.length-1);
						Key classClsNameFKey=getSeg(cs, classClsFKey.getEndPos(),cs.length-1);
						int clsStart=getPos(cs,classClsFKey.getEndPos(),'{');
						int clsEnd=getEndPos(cs, start, '{', '}');
						s=new String(cs,start,clsEnd-start+1);
						clsToken.setText(s);
						clsToken.setStart(start);
						clsToken.setEnd(clsEnd);
//						System.out.println(s);
						
						doClassBody(cs,clsToken,clsStart);
						
//						System.out.println(key1.getText());
						break;
					}
				}
				
				
			}
		}
	}

	/**
	 * 处理类body部分
	 * @param cs
	 * @param clsToken
	 * @param clsStart 
	 */
	private static void doClassBody(char[] cs,Token clsToken, int clsStart) {
		boolean flag=true;
		int start=clsStart;
		int endPos=clsToken.getEnd();
		String fieldType="type";
		int j=0;
		Token t=null;
		Key key1=null;
		while(flag){
			if(start>=endPos-1||start ==-1){
				break;
			}
			key1=getSeg(cs, start,endPos);
			start=key1.getEndPos();
			if(t==null){
				t=new Token();
				t.setStart(key1.getStartPos());
			}
			if(key1.getText().trim().equals(Key.t_keyword_private)||
					key1.getText().trim().equals(Key.t_keyword_public)||
					key1.getText().trim().equals(Key.t_keyword_static)
					){
				Token t1=new Token();
				t1.setStart(key1.getStartPos());
				t1.setEnd(key1.getEndPos());
				t1.setText(key1.getText());
				t.addChild(t1);
			}else{
				j++;
				if(fieldType.equals("type")){
					Token t1=new Token();
					t1.setType(Key.t_type_fieldType);
					t1.setStart(key1.getStartPos());
					t1.setEnd(key1.getEndPos());
					t1.setText(key1.getText());
					t.addChild(t1);
					fieldType="val";
					if(key1.getText().trim().equals("DefaultLogger")){
						int cpos=getNextChar(cs,key1.getEndPos());
						if(cpos!=-1){
							//判断是否有括号
							if(cs[cpos]=='('){
								t.setType(Key.t_type_method);
								int methodEnd=getEndPos(cs, cpos, '{', '}');
								t.setEnd(methodEnd);
								clsToken.addChild(t);
								System.out.println("---------m-start-------");
								print(cs, t.getStart(), t.getEnd());
								System.out.println("---------m-end-------");
								start=methodEnd+1;
								t=null;
								fieldType="type";
							}else {
								t.setType(Key.t_type_field);
								int fieldEnd= getPos(cs, cpos, ';');
								t.setEnd(fieldEnd);
								start=fieldEnd+1;
								clsToken.addChild(t);
								System.out.println("---------field-start------");
								print(cs, t.getStart(), t.getEnd());
								System.out.println("---------field-end------");
								String s=new String(cs,t.getStart(),t.getEnd()-t.getStart()+1);
								if(s.indexOf("Logger log = null;")!=-1){
									System.out.println("d");
								}
								fieldType="type";
								t=null;
							}
						}
					}
				}else if(fieldType.equals("val")){
					Token t1=new Token();
					t1.setType(Key.t_type_fieldName);
					t1.setStart(key1.getStartPos());
					t1.setEnd(key1.getEndPos());
					t1.setText(key1.getText());
					t.addChild(t1);
					int cpos=getNextChar(cs,key1.getEndPos());
					if(cpos!=-1){
						//判断是否有括号
						if(cs[cpos]=='('){
							t.setType(Key.t_type_method);
							int methodEnd=getEndPos(cs, cpos, '{', '}');
							t.setEnd(methodEnd);
							clsToken.addChild(t);
							System.out.println("---------m-start-------");
							print(cs, t.getStart(), t.getEnd());
							System.out.println("---------m-end-------");
							start=methodEnd+1;
							t=null;
							fieldType="type";
						}else {
							t.setType(Key.t_type_field);
							int fieldEnd= getPos(cs, cpos, ';');
							t.setEnd(fieldEnd);
							start=fieldEnd+1;
							clsToken.addChild(t);
							System.out.println("---------field-start------");
							print(cs, t.getStart(), t.getEnd());
							System.out.println("---------field-end------");
							String s=new String(cs,t.getStart(),t.getEnd()-t.getStart()+1);
							if(s.indexOf("Logger log = null;")!=-1){
								System.out.println("d");
							}
							fieldType="type";
							t=null;
						}
					}
				}
				
			}
		}
		
	}



	/**
	 * 找到单行注释 多行注释 字符串位置并标记 
	 * @param cs
	 * @return
	 */
	private static LinkedList<Token> getCommentAndChar(char[] cs){
		int i=0;
		int len=cs.length;
		String mutiComment="0";
		String signleComment="1";
		String chars="2";
		LinkedList<Token> list=new LinkedList<>();
		LinkedList<Token> newlist=new LinkedList<>();
		while(true){
			if(i==len){
				break;
			}
			if(cs[i]=='/'){
				if(i<len-1 && cs[i+1]=='/'){
					if(list.isEmpty()){
						Token token=new Token();
						token.setStart(i);
						token.setType(signleComment);
						i=findSingleEndChar(cs, i);
						token.setEnd(i);
						newlist.add(token);
					}else{
					}
					
				}else if(i<len-1 && cs[i+1]=='*'){
					if(list.isEmpty()){
						Token token=new Token();
						token.setStart(i-1);
						i=findMutiEndChar(cs,i);
						token.setEnd(i);
						token.setType(mutiComment);
						newlist.add(token);
					}else{
					}
					
				}
			}else if(cs[i]=='"'){
				if(list.isEmpty()){
					Token token=new Token();
					token.setStart(i);
					token.setType(chars);
					i=findStringEndChar(cs,i);
					token.setEnd(i);
					newlist.add(token);
				}else{
				}
			}else if(cs[i]=='\r'){
				if(list.isEmpty()){
				}else {
				}
			}else if(cs[i]=='\n'){
				if(list.isEmpty()){
				}else{
				}
			}else if(cs[i]=='*'){
				if(i<len-1 && cs[i+1]=='/'){
					if(list.isEmpty()){
						throw new RuntimeException("格式错误");
					}else {
					}
				}
			}
			
			i++;
		}
		return newlist;
	}
	
	private static void printToken(char[] cs,Token token){
		System.out.println(new String(cs,token.getStart(),token.getEnd()-token.getStart()+1));
	}
	
	private static void print(char[] cs,int startPos,int end){
		if(end==-1){
			System.out.println(new String(cs,startPos,cs.length-startPos));
		}else{
			System.out.println(new String(cs,startPos,end-startPos+1));
		}
	}
	
	/**
	 * 找到字符串
	 * @param cs
	 * @param startPos
	 * @return
	 */
	private static int findStringEndChar(char[] cs, int startPos) {
		while(true){
			if(cs[startPos]!='\\'&&
					startPos<cs.length-1 && cs[startPos+1]=='"'
					){
				return startPos+1;
			}
			startPos++;
		}
	}

	/**
	 * 找到单行注释结束标记
	 * @param cs
	 * @param startPos
	 * @return
	 */
	private static int findSingleEndChar(char[] cs,int startPos){
		while(true){
			if(cs[startPos]=='\r'||
					cs[startPos]=='\n'
					){
				return startPos;
			}
			startPos++;
		}
	}
	/**
	 * 找到多行注释结束标记
	 * @param cs
	 * @param startPos
	 * @return
	 */
	private static int findMutiEndChar(char[] cs,int startPos){
		while(true){
			if(cs[startPos]=='*'&&
					startPos<cs.length-1 && cs[startPos+1]=='/'
					){
				return startPos+1;
			}
			startPos++;
		}
	}
	
	/**
	 * 通过栈找到匹配字符
	 * @param cs
	 * @param startPos
	 * @param start
	 * @param end
	 * @return
	 */
	private static int getEndPos(char[] cs,int startPos,char start,char end){
		LinkedList<Boolean> list=new LinkedList<>();
		while(true){
			if(cs.length==startPos){
				return -1;
			}
			startPos=skipChar(spList,startPos);
			if(cs[startPos]==start){
				list.add(true);
			}else if(cs[startPos]==end){
				if(!list.isEmpty()){
					list.removeLast();
				}
				if(list.isEmpty()){
					return startPos;
				}
			}
			startPos++;
		}
	}
	
	/**
	 * 查找字符
	 * @param cs
	 * @param startPos
	 * @param string
	 * @return
	 */
	private static int getPos(char[] cs, int startPos, char string) {
		while(true){
			if(cs.length==startPos){
				return -1;
			}
			startPos=skipChar(spList,startPos);
			if(cs[startPos++]==string){
				return startPos;
			}
		}
	}
	
	/**
	 * 找到下个非特殊字符
	 * @param cs
	 * @param startPos
	 * @return
	 */
	private static int getNextChar(char[] cs, int startPos) {
		while(true){
			if(cs.length==startPos-1){
				return -1;
			}
			if(
					cs[startPos]!=' '&&
					cs[startPos]!='\r'&&
					cs[startPos]!='\n'&&
					cs[startPos]!='\t'
					){
				return startPos;
			}
			startPos++;
		}
	}
	
	/**
	 * 获取一个代码片段
	 * @param cs
	 * @param start
	 * @param end
	 * @return
	 */
	private static Key getSeg(char[] cs,int start,int end) {
		boolean f=true;
		StringBuilder sb=new StringBuilder();
		Key key=new Key();
		key.setStartPos(start);
		boolean hasData=false;
		while(f){
			start=skipChar(spList,start);
			if(
					cs[start]==' '||
					cs[start]=='\r'||
					cs[start]=='\n'||
					cs[start]=='('||
					cs[start]=='{'||
					cs[start]=='"'||
					cs[start]=='\t'||
					cs[start]==';'
					){
				if(hasData){
					key.setEndPos(start);
					key.setText(sb.toString());
					break;
				}else{
					start++;
					continue;
				}
			}else{
				sb.append(cs[start]);
				hasData=true;
			}
			start++;
		}
		return key;
	}

	/**
	 * 跳过字符 比如注释，字符
	 * 判断位置是否在规定范围 
	 * @param spList2
	 * @param start
	 * @return
	 */
	private static int skipChar(LinkedList<Token> spList2, int start) {
		for (Token token : spList2) {
			if(start>=token.getStart() &&  start<=token.getEnd()){
				return token.getEnd()+1;
			}
		}
		return start;
	}
}
