package kkd.common.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.midi.Sequence;

/*
 * 字符处理工具类
 */
public class StringUtil {

//	public static final String CH_PATTERN = "[\\u4e00-\\u9fa5]+";
	public static final Pattern DIGITAL_PATTERN = Pattern.compile("-?\\d+");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("[-\\+]?\\d+(\\.\\d+)?");
	/**
	 * 只有字母、数字和下划线且不能以下划线开头和结尾
	 */
	public static final Pattern LETTERS_DIGITS_NOUNDERSCORES_PATTERN = Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$");
	/**
	 * 字母数字汉字下划线
	 */
	public static final Pattern LETTERS_DIGITS_CHINESE_UNDERSCORES_PATTERN = Pattern.compile("[a-zA-Z0-9_\u4e00-\u9fa5]+");
	public static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d*\\.{0,1}\\d*");
	public static final Pattern CH_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]+");
	//ip_pattern  ((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|[1-9])
	public static final Pattern IP_PATTERN = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
	public static final Pattern EMAIL_PATTERN = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
	public static final Pattern MOBILENO_PATTERN = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); 
	public static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"); 
	public static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^[0-9]{6}$"); 
	
	/**
	 * 判断是否是邮编
	 * @param value
	 * @return
	 */
	public final static boolean isZipCode(String value){
		return StringUtil.isValidation(ZIP_CODE_PATTERN, value);
	}
	/**
	 * 判断是否为正确的IP地址
	 */
	public static boolean isIpAddress(String value) {
		return StringUtil.isValidation(IP_PATTERN, value);
	}
	/**
	 * 判断是否为正确的url地址
	 */
	public static boolean isURL(String value) {
		return StringUtil.isValidation(URL_PATTERN, value);
	}
	/**
	 * 判断是ip格式
	 * @param value
	 * @return
	 */
	public static boolean isIp(String value){
		return StringUtil.isValidation(IP_PATTERN, value);
	}
	/**
	 * 判断邮箱格式
	 * @param value
	 * @return
	 */
	public static boolean isMail(String value){
		return StringUtil.isValidation(EMAIL_PATTERN, value);
	}
	/**
	 * 判断手机号格式
	 * @param value
	 * @return
	 */
	public static boolean isMobileNO(String value){
		return StringUtil.isValidation(MOBILENO_PATTERN, value);
	}
	/**
	 * 验证正则表达式
	 * @param pattern
	 * @param value
	 * @return
	 */
	public static boolean isValidation(Pattern pattern,String value){
		if(!StringUtil.isEmpty(value)){
			Matcher matcher = pattern.matcher(value);
			return matcher.matches();	
		}
		return false;
	}
	/**
	 * 验证正则表达式
	 * @param pattern
	 * @param value
	 * @return
	 */
	public static boolean isValidationFind(Pattern pattern,String value){
		if(!StringUtil.isEmpty(value)){
			Matcher matcher = pattern.matcher(value);
			return matcher.find();	
		}
		return false;
	}
	/**
	 * 判断字符串是否是纯数字组成
	 * 
	 * @param str
	 */
	public static boolean isInteger(String str) {
//		if (str == null) {
//			return false;
//		}
//		return str.matches("\\d+");
		return isValidation(DIGITAL_PATTERN, str);
	}
	
	/**
	 * 判断是否是浮点类型
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {
		return isValidation(DOUBLE_PATTERN, str);
	}
	
	/**
	 * 数字是否在限定范围
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isRange(String value,Integer min,Integer max){
		if(min!=null &&max!=null&&!StringUtil.isEmpty(value)){
			Double l=Double.valueOf(value);
			return l>=min&&l<=max;
		}
		return false;
	}
	
	public static boolean isMin(String value,Integer min){
		if(min!=null && !StringUtil.isEmpty(value)){
			Long l=Long.valueOf(value);
			return l>=min;
		}
		return false;
	}
	
	public static boolean isMax(String value,Integer max){
		if(max!=null && !StringUtil.isEmpty(value)){
			Long l=Long.valueOf(value);
			return l<=max;
		}
		return false;
	}
	
	/**
	 * 判断字符串的范围
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isRangeLength(String value,Integer min,Integer max){
		if(min!=null &&max!=null&&!StringUtil.isEmpty(value)){
			int l=StringUtil.trim(value).length();
			return l>=min&&l<=max;
		}
		return false;
	}
	public static boolean isMaxLength(String value,Integer max){
		if(max!=null&&!StringUtil.isEmpty(value)){
			int l=StringUtil.trim(value).length();
			return l<=max;
		}
		return false;
	}
	public static boolean isMinLength(String value,Integer min){
		if(min!=null &&!StringUtil.isEmpty(value)){
			int l=StringUtil.trim(value).length();
			return l>=min;
		}
		return false;
	}
	/**
	 * 判断字符串是否为null或者空白字符或者空字符串
	 */
	public static boolean isEmpty(Object str) {
		return str == null || str.toString().matches("\\s*");
	}

	/**
	 * 判断两个对象是否相等
	 */
	public static boolean isEqual(Object o1, Object o2) {
		return HashUtil.isEqual(o1, o2);
	}

	/**
	 * 判断字符为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 判断字符不为空
	 * @param cs
	 * @return
	 */
	public static boolean isNotBlank(CharSequence cs) {
		return !StringUtil.isBlank(cs);
	}
	public static boolean isNull(Object value){
		if(value==null){
			return true;
		}
		return false;
	}
	public static boolean isTrue(Boolean value){
		if(value!=null){
			return value;
		}
		return false;
	}
	/**
	 * 判断字符串str可否转化成实型数据。
	 */
	public static boolean isNumber(String str) {
//		if (str == null) {
//			return false;
//		}
//		return str.length() > 0 && str.matches("\\d*\\.{0,1}\\d*")
//				&& !".".equals(str);
		return isValidation(NUMBER_PATTERN, str)&&!StringUtil.isEqual(str, ".");
	}
	
	/**
	 * 空格.Null返回null
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}
	
	/**
	 * 删除空格
	 */
	public static String delSpace(String str){
		return str == null ? null:str.replaceAll("\\s*", "");
	}
	
	/**
	 * 产生随机字符串
	 * 
	 * @param length
	 *            随机字符串的长度
	 * @return
	 */
	public static String randomKey(int length) {
		final String buffer = "0123456789abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder(length);
		Random r = new Random();
		final int range = buffer.length();
		for (int i = 0; i < length; i++) {
			sb.append(buffer.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	/**
	 * 如果str为空，则返回空字符串，否则trim之后返回
	 */
	public static String killNull(Object str) {
		return killNull(str, "");
	}

	/**
	 * 如果str为空，则返回字符串defaultStr，否则trim之后返回
	 * 
	 * @param str
	 * @param defaultStr
	 */
	public static String killNull(Object str, String defaultStr) {
		if (isEmpty(str)) {
			return defaultStr;
		}
		return str.toString().trim();
	}

	/**
	 * 文件长度友好显示
	 */
	public static String fomatFileSize(long size) {
		if (size < 1024) {
			return size + "B";
		} else if (size < 1024 * 1024) {
			return size / 1024 + "K";
		} else {
			return size / 1024 / 1024 + "M";
		}
	}

	

	

	/**
	 * 删除str最后一个字符如果是ch则删除这个字符，通常在根据list拼装字符串的时候使用
	 * 
	 * @param str
	 * @param ch
	 */
	public static StringBuilder delLastChar(StringBuilder str, char ch) {
		if (str.length() < 1) {
			return str;
		}
		if (str.charAt(str.length() - 1) == ch) {
			str.deleteCharAt(str.length() - 1);
		}
		return str;
	}
	/**
	 * 功能 ：根据原始路径找到对应图片
	 * 替换前： http://www.xxx.com/demo.jpg
	 * 替换后： http://www.xxx.com/demo_16x16.jpg
	 * @param value 图片路径
	 * @param replace 替换内容
	 * @return
	 */
	public static String getPath(String value,String replace){
		if(!StringUtil.isEmpty(value)){
			int pos=value.lastIndexOf(".");
			String prefix=value.substring(pos,value.length());
			value =value.substring(0, pos);
			value=value+replace+prefix;
			return value;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getVoClass(Object vo) {
		final Class clazz = vo.getClass();
		return clazz.getName().indexOf("$") > 0 ? clazz.getSuperclass() : clazz;
	}
	
	/**
	 * 字符串的补位操作 如果需要长度为8位不足的添加某个字符
	 * 
	 * @param length
	 *            长度需要补足
	 * @param addChar
	 *            填充的字符串
	 * @param origin
	 *            源字符串
	 */
	public static String addChar2Full(int length, String addChar, String origin) {
		if (origin.length() < length) {
			String temp = "";
			for (int i = 0; i < length - origin.length(); i++) {
				temp = addChar + temp;
			}
			origin = temp + origin;
		}
		return origin;
	}

	/**
	 * 获取自定sequence name
	 * 
	 * @param sequence
	 * @param clazz
	 * @return
	 * @since Fan Houjun 2009-8-14
	 */
	@SuppressWarnings("rawtypes")
	public static String getSeqName(Sequence sequence, Class clazz) {
		// return isEmpty(sequence.name()) ? (clazz.getSimpleName() + "_Seq")
		// : sequence.name();
		return null;
	}

	/**
	 * 第一个字母小写
	 */
	public static String unCapFirst(String str) {
		final char c = str.charAt(0);
		if (c > 'Z' || c < 'A') {
			return str;
		}
		return (char) (c + 32) + str.substring(1);
	}

	/**
	 * 第一个单词大写
	 */
	public static String capFirst(String str) {
		final char c = str.charAt(0);
		if (c > 'z' || c < 'a') {
			return str;
		}
		return (char) (c - 32) + str.substring(1);
	}



	private static List<Pattern> ptns = null;

	private static List<Pattern> getPtns() {
		if (ptns == null) {
			ptns = new ArrayList<Pattern>();
			ptns.add(Pattern.compile(".+\\.\\w+\\.\\w+\\..+"));
		}
		return ptns;
	}

	/**
	 * @return 将java对象转化为json字符串
	 */
	public static String toJson(Object data) {
		return toJson(data, getPtns());
	}

	private static String toJson(Object data, List<Pattern> excludePtns) {
		// try {
		// if (data instanceof Date) {
		// return DateUtil.format((Date) data,
		// DateUtil.TRIM_SECOND_PATTERN);
		// }
		// if (excludePtns == null || excludePtns.isEmpty()) {
		// return JSONUtil.serialize(data);
		// }
		// return JSONUtil.serialize(data, excludePtns, true);
		// } catch (JSONException e) {
		// throw new IllegalArgumentException("将java对象转化为json字符串出错：" + data, e);
		// }
		return null;
	}

	public static String toJson(Object data, String excludeRegex) {
		List<Pattern> list = new ArrayList<Pattern>(1);
		if (excludeRegex != null) {
			list.add(Pattern.compile(excludeRegex));
		}
		return toJson(data, list);
	}

	

    /**
     * 转换为半角字符串
     * 
     * @param str
     *            待转换字符串
     * @return 转换后的字符串
     */
    public static String toHalfWidthString(CharSequence str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append(toHalfWidthCharacter(str.charAt(i)));
        }
        return sb.toString();
    }

    /**
     * 判断是否是全角字符串(所有字符都是全角)
     * 
     * @param str
     *            被判断的字符串
     * @return 判断结果
     */
    public static boolean isFullWidthString(CharSequence str) {
        return charLength(str) == str.length() * 2;
    }

    /**
     * 判断是否是半角字符串(所有字符都是半角)
     * 
     * @param str
     *            被判断的字符串
     * @return 判断结果
     */
    public static boolean isHalfWidthString(CharSequence str) {
        return charLength(str) == str.length();
    }
    /**
	 * @功能描述 ：判断字符串中是否包含中文字符
	 * @输入参数 ：需要判断的字符串
	 * @返 回 值 ：判断结果
	 * @version ： 1.0
	 * @更新时间 ： 2011-7-25 上午10:37:10
	 */
	public static boolean containsChinese(String str) {
		return isValidationFind(CH_PATTERN, str);
	}
	
	
	  /**
     * 是中文字符吗?
     * 
     * @param c
     *            待判定字符
     * @return 判断结果
     */
    public static boolean isChineseCharacter(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
    
    public static boolean isEnglishCharacter(char c){
    	if(c>='a'&&c<='Z'){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 判断字符是否为全角字符
     * 
     * @param c
     *            字符
     * @return 判断结果
     */
    public static boolean isFullWidthCharacter(char c) {
        // 全角空格为12288，半角空格为32
        // 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
        // 全角空格 || 其他全角字符
        if (c == 12288 || (c > 65280 && c < 65375)) {
            return true;
        }
        // 中文全部是全角
        if (isChineseCharacter(c)) {
            return true;
        }
        // 日文判断
        // 全角平假名 u3040 - u309F
        // 全角片假名 u30A0 - u30FF
        if (c >= '\u3040' && c <= '\u30FF') {
            return true;
        }
        return false;
    }
    /**
     * 转换成半角字符
     * 
     * @param c
     *            待转换字符
     * @return 转换后的字符
     */
    public static char toHalfWidthCharacter(char c) {
        if (c == 12288) {
            return (char) 32;
        } else if (c > 65280 && c < 65375) {
            return (char) (c - 65248);
        }
        return c;
    }
    /**
     * 计算字符串的字符长度(全角算2, 半角算1)
     * 
     * @param str
     *            被计算的字符串
     * @return 字符串的字符长度
     */
    public static int charLength(CharSequence str) {
        int clength = 0;
        for (int i = 0; i < str.length(); i++) {
            clength += isFullWidthCharacter(str.charAt(i)) ? 2 : 1;
        }
        return clength;
    }
    
    /**
     * 复制字符串
     * 
     * @param cs
     *            字符串
     * @param num
     *            数量
     * @return 新字符串
     */
    public static String dup(CharSequence cs, int num) {
        if (isEmpty(cs) || num <= 0)
            return "";
        StringBuilder sb = new StringBuilder(cs.length() * num);
        for (int i = 0; i < num; i++)
            sb.append(cs);
        return sb.toString();
    }

    /**
     * 复制字符
     * 
     * @param c
     *            字符
     * @param num
     *            数量
     * @return 新字符串
     */
    public static String dup(char c, int num) {
        if (c == 0 || num < 1)
            return "";
        StringBuilder sb = new StringBuilder(num);
        for (int i = 0; i < num; i++)
            sb.append(c);
        return sb.toString();
    }

    /**
     * 将字符串首字母大写
     * 
     * @param s
     *            字符串
     * @return 首字母大写后的新字符串
     * @deprecated 推荐使用 {@link #upperFirst(CharSequence)}
     */
    public static String capitalize(CharSequence s) {
        return upperFirst(s);
    }

    /**
     * 将字符串首字母小写
     * 
     * @param s
     *            字符串
     * @return 首字母小写后的新字符串
     */
    public static String lowerFirst(CharSequence s) {
        if (null == s)
            return null;
        int len = s.length();
        if (len == 0)
            return "";
        char c = s.charAt(0);
        if (Character.isLowerCase(c))
            return s.toString();
        return new StringBuilder(len).append(Character.toLowerCase(c))
                                     .append(s.subSequence(1, len))
                                     .toString();
    }

    /**
     * 将字符串首字母大写
     * 
     * @param s
     *            字符串
     * @return 首字母大写后的新字符串
     */
    public static String upperFirst(CharSequence s) {
        if (null == s)
            return null;
        int len = s.length();
        if (len == 0)
            return "";
        char c = s.charAt(0);
        if (Character.isUpperCase(c))
            return s.toString();
        return new StringBuilder(len).append(Character.toUpperCase(c))
                                     .append(s.subSequence(1, len))
                                     .toString();
    }
    



    
    
    /**
     * 将一个字符串出现的HMTL元素进行转义，比如
     * 
     * <pre>
     *  escapeHtml("&lt;script&gt;alert("hello world");&lt;/script&gt;") => "&amp;lt;script&amp;gt;alert(&amp;quot;hello world&amp;quot;);&amp;lt;/script&amp;gt;"
     * </pre>
     * 
     * 转义字符对应如下
     * <ul>
     * <li>& => &amp;amp;
     * <li>< => &amp;lt;
     * <li>>=> &amp;gt;
     * <li>' => &amp;#x27;
     * <li>" => &amp;quot;
     * </ul>
     * 
     * @param cs
     *            字符串
     * 
     * @return 转换后字符串
     */
    public static String escapeHtml(CharSequence cs) {
        if (null == cs)
            return null;
        char[] cas = cs.toString().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cas) {
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '\'':
                sb.append("&#x27;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }
    

    /**
     * 将一个字符串由驼峰式命名变成分割符分隔单词
     * 
     * <pre>
     *  lowerWord("helloWorld", '-') => "hello-world"
     * </pre>
     * 
     * @param cs
     *            字符串
     * @param c
     *            分隔符
     * 
     * @return 转换后字符串
     */
    public static String lowerWord(CharSequence cs, char c) {
        StringBuilder sb = new StringBuilder();
        int len = cs.length();
        for (int i = 0; i < len; i++) {
            char ch = cs.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i > 0)
                    sb.append(c);
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    
    public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	} 
	 private static byte charToByte(char c) {   
		    return (byte) "0123456789ABCDEF".indexOf(c);   
	 } 
	
	public static String bytesToHexString(byte[] src){   
	    StringBuilder stringBuilder = new StringBuilder("");   
	    if (src == null || src.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < src.length; i++) {   
	        int v = src[i] & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv);   
	    }   
	    return stringBuilder.toString();   
	} 
    
    public static Map<String, String> getParaCollection(String sourceString, String keyValuePairSplit, String keyValueSplit){
    	int pos=sourceString.indexOf("?");
    	if(pos!=-1) {
    		System.out.println("sssssssssssssssss"+sourceString);
//    		sourceString=sourceString.substring(pos+1);
    	}
      String[] arrParaStrings = sourceString.split(keyValuePairSplit);
      if ((arrParaStrings == null) || (arrParaStrings.length <= 0))
        return null;
      Map<String,String> hashMap = new HashMap<String,String>();
      for (int i = 0; i < arrParaStrings.length; i++) {
        if ( !StringUtil.isEmpty(arrParaStrings[i])  && (arrParaStrings[i].contains(keyValueSplit))) {
          String[] arrItem = arrParaStrings[i].split(keyValueSplit);
          if (arrItem.length == 2){
            hashMap.put(arrItem[0], arrItem[1].replace("\r\n", ""));
          }
        }
      }
      return hashMap;
    }
    
    public static String arrayToString(Object[]  objs){
    	StringBuilder sb = new StringBuilder();
    	boolean flag = false;
    	for(int i=0;objs!=null&&i<objs.length;i++){
    		if(objs[i]!=null){
    			if(flag){
    				sb.append(",");
    			}
    			sb.append(objs[i].toString());
    			flag=true;
    		}
    	}
    	return sb.toString();
    }
    /**
	 *	将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
	 */
	public static long ipToLong(String strIp) {
		long[] ip = new long[4];
		
		// 先找到IP地址字符串中.的位置
		int position1 = strIp.indexOf(".");
		int position2 = strIp.indexOf(".", position1 + 1);
		int position3 = strIp.indexOf(".", position2 + 1);
		
		// 将每个.之间的字符串转换成整型
		ip[0] = Long.parseLong(strIp.substring(0, position1));
		ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strIp.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	/**
	 * 将十进制整数形式转换成127.0.0.1形式的ip地址
	 * @param longIp
	 * @return
	 */
	public static String longToIP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}
	
	/**
	 * 打印异常消息
	 * @param e
	 * @param newlineChar
	 * @return
	 */
	public static String printException(Exception e,String newlineChar){
		StackTraceElement[] ses=e.getStackTrace();
		StringBuilder sb=new StringBuilder(e.getMessage()+newlineChar);
		for (int j = 0; j < ses.length; j++) {
			sb.append(sb.append(ses[j])+newlineChar);
		}
		return sb.toString();
	}
	
	public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || 
                (codePoint == 0x9) ||                            
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }
	
	
	
	public static String utf8mb4ToUtf8GeneralCi(String source){
		if(StringUtil.isEmpty(source)){
			return "";
		}
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < source.length(); i++) {
			Character c=source.charAt(i);
			String c1=c+"";
			try {
				if(c1.getBytes("UTF-8").length>=4||c1.getBytes("UTF-8").length==1&&c1.getBytes("UTF-8")[0]==63){
					c1="";
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append(c1);
		}
		return sb.toString();
	}
	

	
	
	
	public static final int INDEX_NOT_FOUND = -1;
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	
	public static boolean isEmpty(String str) {
		//		return str == null || str.toString().matches("\\s*");
		return str == null || str.length() == 0;
	}

	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
	
	public static int indexOf(final CharSequence seq, final CharSequence searchSeq) {
		if (seq == null || searchSeq == null) {
			return INDEX_NOT_FOUND;
		}
		return indexOf(seq, searchSeq, 0);
	}

	public static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
		 return cs.toString().indexOf(searchChar.toString(), start);
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static String convertString(Object obj) {
		if(obj==null ){
			return null;
		}
		return obj.toString();
	}

	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	}

	/**
	 * 替换jdk String replace方案
	 * @param text
	 * @param searchString
	 * @param replacement
	 * @param max
	 * @return
	 */
	public static String replace(final String text, final String searchString, final String replacement, int max) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == INDEX_NOT_FOUND) {
			return text;
		}
		final int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		final StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != INDEX_NOT_FOUND) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	public static String replace(final String text, final String searchString, final String replacement) {
		return replace(text, searchString, replacement, -1);
	}

	public static String[] split(final String str, final String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	public static String[] split(final String str, final String separatorChars, final int max) {
		return splitWorker(str, separatorChars, max, false);
	}

	private static String[] splitWorker(final String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return null;
		}
		final int len = str.length();
		if (len == 0) {
			return EMPTY_STRING_ARRAY;
		}
		final List<String> list = new ArrayList<String>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			final char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || preserveAllTokens && lastMatch) {
			list.add(str.substring(start, i));
		}
		return list.toArray(new String[list.size()]);
	}

}
