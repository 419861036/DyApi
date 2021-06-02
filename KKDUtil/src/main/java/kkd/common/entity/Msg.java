package kkd.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
 * 消息传递载体
 * 
 * @author Administrator
 * 
 */
public class Msg<T> implements Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SUCCESS = 200;//成功
	//自定义异常code范围1-199；
	public static final int ERROR = 0;//系统错误
	public static final int ERROR_KKDDb= -1;//数据库异常
	public static final int ERROR_KKDJson= -2;//json异常
	public static final int ERROR_KKDNetwork= -3;//网络异常
	public static final int ERROR_KKDValidation= -4;//验证异常
	public static final int ERROR_KKDXml= -5;//xml异常
	public static final int ERROR_KKDLogin = -6;//登录异常
	public static final int ERROR_KKDPermission  = -7;//权限异常
	
	
	public String msg;
	private int code;
	private T v;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Msg() {
		this.code = SUCCESS;
	}

	public Msg(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getV() {
		return v;
	}

	public void setV(T v) {
		this.v = v;
	}

	public boolean isSuccess(){
		if(SUCCESS==code){
			return true;
		}
		return false;
	}
	

	/**
	 * 将对象转成xml
	 * 
	 * @param alias
	 *            别名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String toXMl(Map<String, Class> alias) {
		return XmlUtil.toXml(alias,this);
	}

	
	/**
	 * 对象转成xml(默认名称为标签名)
	 */
	@SuppressWarnings("rawtypes")
	public String toXMls(Class... classs) {
		Map<String,Class> map = new HashMap<String,Class>();
		for(Class paramClass : classs ){
			map.put(paramClass.getSimpleName(), paramClass);
		}
		return this.toXMl(map);
	}
	

	/**
	 * xml转成对象
	 * 
	 * @param xml
	 *            xml内容
	 * @param alias
	 *            别名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Msg xmlToObject(String xml, Map<String, Class> alias) {
		return XmlUtil.xmlToObj(xml, alias);
	}

	
	/**
	 * 
	 * @param xml
	 * @param classs 需要取别名的类 默认为类名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Msg xmlToObject(String xml, Class... classs) {
		return XmlUtil.xmlToObject(xml, classs);
		
	}

	


	/**
	 * 将对象转成json
	 * 
	 * @param o
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public  String objectToJSON(Class...classs) {
		return XmlUtil.objectToJSON(this,classs);
	}

	
	
	@SuppressWarnings("rawtypes")
	@Deprecated
	public String toJSON(Map<String, Class> alias) {
		return XmlUtil.toJson(alias,this);
	}

	
	
	/**
	 * 将对象转换成json
	 * 将对象分层次的解析
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String objectToJSONHierarchical(Class...classs){
		return XmlUtil.objectToJSONHierarchical(this,classs);
	}

	
	/**
	 * 阿里方法
	 * 推荐使用
	 * @param classs
	 * @return
	 */
	public String objectToFastJSON(){
		return JSON.toJSONString(this);
	}
	
	public String objectToFastJSONDisRef(){
		return JSON.toJSONString(this,SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * 将json转对象
	 * 
	 * @param json
	 * @param alias
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public Msg jsonToObject(String json, Map<String, Class> alias) {
		return XmlUtil.jsonToObject(json, alias);
	}

	/**
	 * 将json转对象
	 * 
	 * @param json
	 * @param alias
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public Msg jsonToObject(String json, Class... classs) {
		return XmlUtil.jsonToObject(json, classs);
	}

	
	
	public static SerializeConfig mapping = new SerializeConfig();
	
	static {                 
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd hh:mm:ss"));
	}
	
	@SuppressWarnings("unused")
	private static final SerializerFeature[] features = {
		SerializerFeature.WriteMapNullValue, // 输出空置字段
		SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是nullSerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是nullSerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
		SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	}; 
	public static void main(String[] args) {
	}
}
