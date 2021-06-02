package kkd.common.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XmlUtil {
	public static String toXml(Map<String, Class> alias,Object obj) {
		XStream xs = new XStream(){
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                    	if (definedIn == Object.class) {
                    		try {
                    			return this.realClass(fieldName) != null;
                    		} catch(Exception e) {
                    			return false;
                    		}
                    	} else {
                    		return super.shouldSerializeMember(definedIn, fieldName);
                    	}
                    }
                };
            }
        };
		xs.registerConverter(new CustomDateConverter());
		
		map(alias, xs);
		return xs.toXML(obj);
	}
	
	
	public static Msg xmlToObject(String xml, Class... classs) {
		if(xml!=null){
			XStream xs = new XStream(){
	            protected MapperWrapper wrapMapper(MapperWrapper next) {
	                return new MapperWrapper(next) {
	                    @Override
	                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
	                    	if (definedIn == Object.class) {
	                    		try {
	                    			return this.realClass(fieldName) != null;
	                    		} catch(Exception e) {
	                    			return false;
	                    		}
	                    	} else {
	                    		return super.shouldSerializeMember(definedIn, fieldName);
	                    	}
	                    }
	                };
	            }
	        };
			xs.alias("Msg", Msg.class);
			xs.registerConverter(new CustomDateConverter());
			XmlUtil.map(mapAlias(classs), xs);
			return (Msg) xs.fromXML(xml);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void map(Map<String, Class> alias, XStream xs) {
		xs.alias("Msg", Msg.class);
		if (alias != null) {
			Set<Entry<String, Class>> set = alias.entrySet();
			for (Entry<String, Class> entry : set) {
				xs.alias(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * 别名映射
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Class> mapAlias(Class... obj) {
		Map<String, Class> map = new HashMap<String, Class>();
		if(obj==null){
			return null;
		}
		for(Class o : obj){
			map.put(o.getSimpleName(), o);
		}
		return map;
	}
	
	public static Msg xmlToObj(String xml, Map<String, Class> alias) {
		if(xml!=null){
			XStream xs = new XStream(){
	            protected MapperWrapper wrapMapper(MapperWrapper next) {
	                return new MapperWrapper(next) {
	                    @Override
	                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
	                    	if (definedIn == Object.class) {
	                    		try {
	                    			return this.realClass(fieldName) != null;
	                    		} catch(Exception e) {
	                    			return false;
	                    		}
	                    	} else {
	                    		return super.shouldSerializeMember(definedIn, fieldName);
	                    	}
	                    }
	                };
	            }
	        };
			xs.registerConverter(new CustomDateConverter());
			XmlUtil.map(alias, xs);
			return (Msg) xs.fromXML(xml);
		}else{
			return null;
		}
	}
	
	public static String toJson(Map<String, Class> alias,Object obj) {
		XStream xs = new XStream(new JettisonMappedXmlDriver());
		xs.registerConverter(new CustomDateConverter());
		XmlUtil.map(alias, xs);
		return xs.toXML(obj);
	}
	public static String objectToJSONHierarchical(Object obj,Class... classs) {
		XStream xs = new XStream(new JsonHierarchicalStreamDriver());
		xs.registerConverter(new CustomDateConverter());
		XmlUtil.map(XmlUtil.mapAlias(classs), xs);
		return xs.toXML(obj);
	}
	
	public static String objectToJSON(Object obj,Class... classs) {
		XStream xs = new XStream(new JettisonMappedXmlDriver());
		xs.registerConverter(new CustomDateConverter());
		XmlUtil.map(XmlUtil.mapAlias(classs), xs);
		return xs.toXML(obj);
	}
	
	public static Msg jsonToObject(String json, Map<String, Class> alias) {
		XStream xs = new XStream(new JettisonMappedXmlDriver());
		xs.registerConverter(new CustomDateConverter());
		XmlUtil.map(alias, xs);
		return (Msg) xs.fromXML(json);
	}
	
	public static Msg jsonToObject(String json, Class... classs) {
		XStream xs = new XStream(new JettisonMappedXmlDriver());
		xs.registerConverter(new CustomDateConverter());
		XmlUtil.mapAlias(classs);
		return (Msg) xs.fromXML(json);
	}
}
