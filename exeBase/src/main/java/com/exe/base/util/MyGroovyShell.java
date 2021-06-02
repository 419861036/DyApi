package com.exe.base.util;

import java.lang.reflect.Method;

import groovy.lang.Script;

public class MyGroovyShell extends Script{

    public Object run() {
        //show usage
        Method[] methods = MyGroovyShell.class.getDeclaredMethods();
        StringBuilder sb=new StringBuilder();
        for (Method method : methods) {
            sb.append(method);
        }

        return sb.substring(0, sb.length()-1);
    }

    // public static Object nvl(Object str, Object val) {
    //     return str == null || "".equals(str) ? val : str;
    // }
    
}