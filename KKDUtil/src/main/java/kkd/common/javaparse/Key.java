package kkd.common.javaparse;

public class Key {
	
	public static final String t_keyword_package="package";
	public static final String t_keyword_import="import";
	public static final String t_keyword_class="class";
	public static final String t_keyword_private="private";
	public static final String t_keyword_static="static";
	public static final String t_keyword_public="public";
//	public static final String t_feildName="static";
	public static final String t_type_field="field";
	public static final String t_type_fieldName="fieldName";
	public static final String t_type_fieldType="fieldType";
	public static final String t_type_method="mothod";
	
	private String text;
	private int startPos;
	private int endPos;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getStartPos() {
		return startPos;
	}
	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}
	public int getEndPos() {
		return endPos;
	}
	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}
	
}
