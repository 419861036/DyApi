package kkd.common.javaparse;

import java.util.LinkedList;
import java.util.List;

import kkd.common.util.FileUtil;
import kkd.common.util.FileUtil.FileUtilReader;

public class JsonParse {
	public static void main(String[] args) {
		test1();
//		LinkedList<String> l=new LinkedList<>();
//		l.push("a");
//		l.push("b");
//		l.push("c");
//		System.out.println(l.getFirst());
	}
	
	
	private static void test1() {
		final StringBuilder sb=new StringBuilder();
		FileUtil.readFileByLines("E:/workspaceGroup/workspace_new/KKDUtil/src/kkd/common/javaparse/test.json", new FileUtilReader() {
			
			@Override
			public void read(Object data) {
				sb.append(data+"\r\n");
			}
		});
		String content=sb.toString();
		char[] cs=content.toCharArray();
		
		Token rootToken=parse(content);
		
		//TODO 未递归获取
		List<Token> list=rootToken.getChild();
		for (int i = 0; i < list.size(); i++) {
			Token token=list.get(i);
			if("obj".equals(token.getType())){
				List<Token> child=token.getChild();
				List<Token> childkey=child.get(0).getChild();
				for (Token token2 : childkey) {
					List<Token> childval=token2.getChild();
					if("\"".equals(childval.get(0).getText())){
						System.out.println("key:"+childval.get(1).getText());
						if("\"".equals(childval.get(4).getText())){
							System.out.println("val:"+childval.get(5).getText());
						}else{
							System.out.println("val:"+childval.get(4).getText());
						}
					}else{
						System.out.println("key:"+childval.get(0).getText());
						if("\"".equals(childval.get(2).getText())){
							System.out.println("val:"+childval.get(3).getText());
						}else{
							System.out.println("val:"+childval.get(4).getText());
						}
					}
				}
			}else if("arr".equals(token.getType())){
				System.out.println("");
			}
		}

		
	}

	/**
	 * 解析语法树
	 * @param content
	 * @return
	 */
	private static Token parse(String content) {
		LinkedList<Token> stack=new LinkedList<>();
		Token rootToken=new Token();
		rootToken.setStart(0);
		char[] cs=content.toCharArray();
		rootToken.setEnd(cs.length-1);
		Token t=null;
		while(true){
			int start=t==null?0:t.getEnd()+1;
			t=getToken(cs, start);
			if(t==null){
				break;
			}else{
				String c=t.getContent(cs);
				if(c.equals("[")){
					stack.push(t);
				}else if(c.equals("]")){
					if(!stack.isEmpty()){
						Token t1=stack.pop();
						//找到结束标志
						while(true){
							if("[".equals(t1.getContent(cs))){
								Token t2=new Token();
								t2.setType("arr");
								t2.addChild(t1);
								t2.addChild(t);
								if(!stack.isEmpty()){
									Token t3=stack.getFirst();
									t3.addChild(t2);
								}else{
									rootToken.addChild(t2);
								}
								break;
							}else{
								t1=stack.pop();
							}
						}
					}else{
						rootToken.addChild(t);
					}
				}else if(c.equals("{")){
					stack.push(t);
				}else if(c.equals("}")){
					if(!stack.isEmpty()){
						Token t1=stack.pop();
						//找到结束标志
						while(true){
							if("{".equals(t1.getContent(cs))){
								Token t2=new Token();
								t2.setType("obj");
								t2.addChild(t1);
								t2.addChild(t);
								if(!stack.isEmpty()){
									Token t3=stack.getFirst();
									t3.addChild(t2);
								}else{
									rootToken.addChild(t2);
								}
								break;
							}else{
								t1=stack.pop();
							}
						}
						
					}else{
						rootToken.addChild(t);
					}
					
				}else if(c.equals(",")){
					if(!stack.isEmpty()){
						Token t1=stack.getFirst();
						if(t1!=null){
							if("objkey".equals(t1.getType())){
								stack.pop();
							}
						}
					}else{
						rootToken.addChild(t);
					}
				}else if(c.equals(":")){
					if(!stack.isEmpty()){
						Token t1=stack.getFirst();
						if("objkey".equals(t1.getType())){
							t1.addChild(t);
						}
					}else{
						rootToken.addChild(t);
					}
				}else if(c.equals("\"")){
					if(!stack.isEmpty()){
						Token t1=stack.getFirst();
						if("objkey".equals(t1.getType())){
							t1.addChild(t);
						}else if("key".equals(t1.getType())){
							t1.addChild(t);
						}else if("{".equals(t1.getContent(cs))){
							Token t2=new Token();
							t2.setType("objkey");
							t2.addChild(t);
							t1.addChild(t2);
							stack.push(t2);
						}
					}else{
						rootToken.addChild(t);
					}
					
				}else if("key".equals(t.getType())){
					if(!stack.isEmpty()){
						Token t1=stack.getFirst();
						if("}".equals(t1.getContent(cs))){
							Token t2=new Token();
							t2.setType("objkey");
							t2.addChild(t);
							t1.addChild(t2);
							stack.push(t2);
						}else if("{".equals(t1.getContent(cs))){
							Token t2=new Token();
							t2.setType("objval");
							t2.addChild(t);
							t1.addChild(t2);
						}else if("objkey".equals(t1.getType())){
							t1.addChild(t);
						}
					}else{
						rootToken.addChild(t);
					}
					
				}
			}
		}
		return rootToken;
	}

	
	/**
	 * 根据一些关键字获取一个token
	 * @param cs
	 * @param start
	 * @return
	 */
	private static Token getToken(char[] cs,int start) {
		Token token1=null;
		for (int i = start; i < cs.length; i++) {
			char data=cs[i];
//			Character nextdata=i<cs.length-1?cs[i+1]:null;
			Character predata=i-start>0?cs[i-1]:null;
			if(
					data==' '||
					data=='\t'||
					data=='\n'||
					data=='\r'
					){
				if(token1!=null){
					token1.setType("key");
					token1.setEnd(i-1);
					String t=new String(cs,token1.getStart(),token1.getEnd()-token1.getStart()+1);
					token1.setText(t);
					return token1;
				}
				
			}else	if(predata!=null && predata=='/'&&data=='*'){
				//处理多行注释
				if(token1!=null){
					token1.setType("key");
					token1.setEnd(i-2);
					//如果上个token确实没有数据 那么直接返回多行注释token
					if(token1.getEnd()<=token1.getStart()){
						int end=findMutiEndChar(cs, i+1);
						Token token=new Token();
						token.setStart(i);
						token.setEnd(end);
						token.setType("muticomment");
						String t=new String(cs,token.getStart(),token.getEnd()-token.getStart()+1);
						token.setText(t);
						return token;
					}
					String t=new String(cs,token1.getStart(),token1.getEnd()-token1.getStart()+1);
					token1.setText(t);
					return token1;
				}
				int end=findMutiEndChar(cs, i+1);
				Token token=new Token();
				token.setStart(i);
				token.setEnd(end);
				token.setType("muticomment");
				String t=new String(cs,token.getStart(),token.getEnd()-token.getStart()+1);
				token.setText(t);
				return token;
			}else	if(
					data=='['||
					data==']'||
					data=='{'||
					data=='}'||
					data==','||
					data==':'||
					predata!=null&&predata!='\\' && data=='"'||
					predata==null&&data=='"'
					){
				if(token1!=null){
					token1.setType("key");
					token1.setEnd(i-1);
					String t=new String(cs,token1.getStart(),token1.getEnd()-token1.getStart()+1);
					token1.setText(t);
					return token1;
				}
				Token token=new Token();
				token.setStart(i);
				token.setEnd(i);
				String t=new String(cs,token.getStart(),token.getEnd()-token.getStart()+1);
				token.setText(t);
				return token;
			}else{
				if(token1==null){
					token1=new Token();
					token1.setStart(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * 多行注释结尾
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
	
}
