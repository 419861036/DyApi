package kkd.common.util;

import sun.misc.BASE64Decoder;

public class Base64Coder {
	private static final byte SUFFIX_CODE = '=';  
	/**
	 * encode to BASE64 string
	 * @param s
	 * @return
	 */
	public static String encode(String s) {
		if (s == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	/**
	 *将 BASE64 编码的字符串 s 进行解码 
	 */
	public static String decode(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 *将 BASE64 编码的字符串 s 进行解码 
	 */
	public static byte[] decodeBuffer(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return b;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		String str = "test123456";
		String str1 = encode(str);
		System.out.println("BASE64：" + str1);
		System.out.println("origin：" + decode(str1));

	}
	
	 public static byte[] decodeToByte(String s) {  
	        if (s == null || s.length() == 0)  
	        {  
	            return null;  
	        }  
	  
	        int length = s.length();  
	        // length must be a multiple of 4  
	        if (length % 4 != 0) {  
	            throw new IllegalArgumentException("String length must be a multiple of four.");  
	        }  
	  
	        int numOfGroups = length / 4;  
	        int numOfFullGroups = numOfGroups;  
	        int numOfPaddings = 0;  
	        if (s.charAt(length - 1) == SUFFIX_CODE) {  
	            numOfPaddings++;  
	            numOfFullGroups--;  
	            if (s.charAt(length - 2) == SUFFIX_CODE)  
	            {  
	                numOfPaddings++;  
	            }  
	        }  
	        byte[] result = new byte[3 * numOfGroups - numOfPaddings];  
	  
	        int srcIndex = 0, dstIndex = 0;  
	        for (int i = 0; i < numOfFullGroups; i++) {  
	            int ch0 = getCharIndex(s.charAt(srcIndex++));  
	            int ch1 = getCharIndex(s.charAt(srcIndex++));  
	            int ch2 = getCharIndex(s.charAt(srcIndex++));  
	            int ch3 = getCharIndex(s.charAt(srcIndex++));  
	  
	            result[dstIndex++] = (byte)((ch0 << 2) | (ch1 >> 4));  
	            result[dstIndex++] = (byte)((ch1 << 4) | (ch2 >> 2));  
	            result[dstIndex++] = (byte)((ch2 << 6) | ch3);  
	        }  
	  
	        if (numOfPaddings != 0) {  
	            int ch0 = getCharIndex(s.charAt(srcIndex++));  
	            int ch1 = getCharIndex(s.charAt(srcIndex++));  
	            result[dstIndex++] = (byte)((ch0 << 2) | (ch1 >> 4));  
	            if (numOfPaddings == 1) {  
	                int ch2 = getCharIndex(s.charAt(srcIndex++));  
	                result[dstIndex++] = (byte)((ch1 << 4) | (ch2 >> 2));  
	            }  
	        }  
	  
	        return result;  
	    }  
	 private static int getCharIndex(char c) {  
	        if (c >= 'A' && c <= 'Z') { // A-Z: 65-90  
	            return (int) c - 65;  
	        } else if (c >= 'a' && c <= 'z') { // a-z: 97-122  
	            return (int) c - 71;  
	        } else if (c >= '0' && c <= '9') {// 0-9: 48-57  
	            return (int) c + 4;  
	        } else if (c == '+') {  
	            return 62;  
	        } else if (c == '/') {  
	            return 63;  
	        }  
	        throw new IllegalArgumentException("Character " + c + " is not a BASE64 char");  
	    }  

}
