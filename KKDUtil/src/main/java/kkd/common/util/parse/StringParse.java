package kkd.common.util.parse;

/**
 * 字符解析器
 * @author tanbin
 *
 */
public class StringParse {
	private int currpos;
	private int strlen;
	private String data;
	
	public StringParse(String data) {
		this.data=data;
		strlen=data.length();
	}
	
	public void rest(){
		currpos=0;
	}
	/**
	 * 位置跳跃步数
	 * @param step
	 */
	public void next(int step){
		currpos=currpos+step;
	}
	
	/**
	 * 位置跳跃步数
	 * @param step
	 */
	public boolean nextAfter(String str){
		int pos=data.indexOf(str,currpos);
		if(pos!=-1){
			currpos=pos+str.length();
			return true;
		}
		return false;
	}
	/**
	 * 位置跳跃步数
	 * @param step
	 */
	public boolean nextBefore(String str){
		int pos=data.indexOf(str,currpos);
		if(pos!=-1){
			currpos=pos;
			return true;
		}
		return false;
	}
	
	public boolean lastBefore(String str){
		int pos=data.indexOf(str,currpos);
		while(pos!=-1){
			int pos1=data.indexOf(str,pos+1);
			if(pos1==-1){
				break;
			}else{
				pos=pos1;
			}
		}
		
		if(pos!=-1){
			currpos=pos;
			return true;
		}
		return false;
	}
	/**
	 * abcd len:2 返回值ab 位置1
	 * abcd len:8 返回值abcd 位置4
	 * 并获取固定长度字符串并跳跃
	 * @param len
	 * @return
	 */
	public String getNextString(int len){
		int endIndex=currpos+len;
		if(endIndex>strlen){
			endIndex=strlen;
		}
		String s= data.substring(currpos, endIndex);
		currpos=endIndex;
		return s;
	}
	/**
	 * abcd  dc 返回值：a 位置：1
	 * abcd  df 返回值：空 位置：不变
	 * 跳转到某个字符串前
	 * @param str
	 * @return
	 */
	public String getNextStringBefore(String str){
		int pos= data.indexOf(str,currpos);
		if(pos==-1){
			return null;
		}
		String s=data.substring(currpos,pos);
		currpos=pos;
		return s;
	}
	public int getCurrpos(){
		return currpos;
	}
}
