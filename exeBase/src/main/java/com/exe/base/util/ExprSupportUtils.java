package com.exe.base.util;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.codehaus.groovy.control.CompilerConfiguration;

public class ExprSupportUtils {

    private static final Object lock = new Object();
    private static final GroovyShell shell;

    private static Hashtable<String, Script> cache = new Hashtable<String, Script>();
    static {
        CompilerConfiguration cfg = new CompilerConfiguration();
        cfg.setScriptBaseClass(MyGroovyShell.class.getName());

        shell = new GroovyShell(cfg);
    }

    public static Object parseExpr(String expr) {
        Script s = getScriptFromCache(expr);
        return s.run();
    }

    public static Object parseExpr(String expr, Map<?, ?> map) {
        Binding binding = new Binding(map);
        Script script = getScriptFromCache(expr);
        script.setBinding(binding);
        return script.run();
    }

    private static Script getScriptFromCache(String expr) {
        if (cache.contains(expr)) {
            return cache.get(expr);
        }
        synchronized (lock) {
            if (cache.contains(expr)) {
                return cache.get(expr);
            }
            Script script = shell.parse(expr);
            cache.put(expr, script);
            return script;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Map<String,String> map=new HashMap<>();
        map.put("data", "s");
        Object obj=ExprSupportUtils.parseExpr("data!=''",map);
        System.out.println(obj);
    }

}