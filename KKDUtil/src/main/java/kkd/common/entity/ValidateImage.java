package kkd.common.entity;

import java.io.Serializable;

public class ValidateImage implements Serializable{
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private char contents[];
	private static String IMAGE_SAVE_PATH = "/";
	private String imageBackgroundColor;
	private int contentFontSize = 12;
	private String contentColor;
	private String fileName;
	@SuppressWarnings("unused")
	private String validateCode;
	
	
	
	public ValidateImage(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public ValidateImage(int width, int height, char contents[]) {
		this.width = width;
		this.height = height;
		this.contents = contents;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public char[] getContents() {
		return contents;
	}

	public void setContents(char[] contents) {
		this.contents = contents;
	}

	public static String getIMAGE_SAVE_PATH() {
		return IMAGE_SAVE_PATH;
	}

	public static void setIMAGE_SAVE_PATH(String iMAGE_SAVE_PATH) {
		IMAGE_SAVE_PATH = iMAGE_SAVE_PATH;
	}

	public String getImageBackgroundColor() {
		return imageBackgroundColor;
	}

	public void setImageBackgroundColor(String imageBackgroundColor) {
		this.imageBackgroundColor = imageBackgroundColor;
	}

	public int getContentFontSize() {
		return contentFontSize;
	}

	public void setContentFontSize(int contentFontSize) {
		this.contentFontSize = contentFontSize;
	}

	public String getContentColor() {
		return contentColor;
	}

	public void setContentColor(String contentColor) {
		this.contentColor = contentColor;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getValidateCode() {
		StringBuilder sb = new StringBuilder();
		for(char content: this.getContents()){
			sb.append(content);
		}
		return sb.toString();
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

}
