package com.exe.base.util;

import java.util.Map;
import java.util.WeakHashMap;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import kkd.common.util.Md5Util;

public class GroovyUtil {

    private final static Logger logger = LoggerFactory.getLogger(GroovyUtil.class);
    static{
        getConfig();
    }
    /**
	 * 缓存groovy脚本 弱引用
	 */
    private static Map<String,GroovyObject> cachedGroovy=new WeakHashMap<String, GroovyObject>();
    private static Map<String,Class> cachedClass=new WeakHashMap<String, Class>();
    private static Map<String,Script> cachedScript=new WeakHashMap<String, Script>();

    public static void clear(){
        cachedGroovy=new WeakHashMap<String, GroovyObject>();
        cachedClass=new WeakHashMap<String, Class>();
        cachedScript=new WeakHashMap<String, Script>();
    }

    public static Class getCmd(String code){
        String md5=Md5Util.encode(code);
        Class cls=cachedClass.get(md5);
        if(cls!=null){
            return cls;
        }else{
            GroovyClassLoader classLoader = new GroovyClassLoader();
            cls = classLoader.parseClass(code);
        }
        return cls;
    }

    public static Object exe(String code,String method,Object cmdParam){
        String md5=Md5Util.encode(code);
		GroovyObject groovyObject=cachedGroovy.get(md5);
		if(cachedGroovy.get(md5)!=null){
			return groovyObject.invokeMethod(method,cmdParam);
		}else{
			GroovyClassLoader classLoader = new GroovyClassLoader();
			Class groovyClass = classLoader.parseClass(code);
			try {
				groovyObject = (GroovyObject) groovyClass.newInstance();
                cachedGroovy.put(md5, groovyObject);
				return groovyObject.invokeMethod(method,cmdParam);
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("", e);
			}
		}
        return null;
    }
    
    private static GroovyShell shell;

    public static Object parse(String code,Map<?,?> map) {
        Script script = cachedScript.get(code);
        if (script == null) {
            script = shell.parse(code);
            cachedScript.put(code, script);
        }
        if(map!=null){
            Binding binding = new Binding(map);
            script.setBinding(binding);
        }
        return script.run();
    }

    public static void getConfig() {
        CompilerConfiguration cfg = new CompilerConfiguration();
        cfg.setScriptBaseClass(MyGroovyShell.class.getName());
        shell = new GroovyShell(cfg);
    }
    
}