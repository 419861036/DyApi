package kkd.common.javaparse;

import java.util.ArrayList;
import java.util.List;

public class Token {
	private String type;
	private String text;
	private int start=-1;
	private int end=-1;
	private List<Token> child;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Token> getChild() {
		return child;
	}
	public void setChild(List<Token> child) {
		this.child = child;
	}
	public void addChild(Token token){
		if(child==null){
			child=new ArrayList<>();
		}
		child.add(token);
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	public String getContent(char[] c){
		if(end!=-1 && end>=start){
			return 	new String(c,start,end-start+1);
		}
		return "";
	}
	
}
