package com.exe.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import kkd.common.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class ScanJava {
    public static final String CLASS_SUFFIX = ".class";

    private static final Pattern INNER_PATTERN = java.util.regex.Pattern.compile("\\$(\\d+).",
            java.util.regex.Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {
        ScanJava s = new ScanJava();
        s.test1(s);
    }

    private void test1(ScanJava s) {
        try {
            s.findCandidateComponents("ch");
        } catch (Exception e) {
        }

    }

    private void test() {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.getCtClass("kkd.common.filter.ControlFilter");
            CtMethod[] m = ctClass.getMethods();
            for (CtMethod ctMethod : m) {
                System.out.println(ctMethod.getName());
            }
        } catch (Exception e) {
        }
    }

    public Set<String> findCandidateComponents(String packageName) throws IOException {
        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        Map<String, String> classMap = new HashMap<>(32);
        String path = packageName.replace(".", "/");
        String filePath=null;
        Enumeration<URL> urls = findAllClassPathResources(path);
        while (urls != null && urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String protocol = url.getProtocol();
            System.out.println("file:"+url.getFile());

            JSONObject param=new JSONObject();
            if ("jar".equals(protocol)) {
                String jarPath=url.getFile();
                int pos=jarPath.lastIndexOf(".jar");
                filePath=jarPath.substring(0,pos+4);
                param.put("path",filePath);
            }else{
                filePath=url.getFile();
                param.put("path",url.getFile());
            }
            param.put("type",protocol);
            param.put("md5","1");
            param.put("state","ing");
            InvokeApi invokeApi=new InvokeApi();
            JSONObject rs=invokeApi.json("/api/resource/index/add",param);
            if(rs.getInteger("code")!=200){
                continue;
            }
            System.out.println(rs);
            if ("file".equals(protocol)) {
                String file = URLDecoder.decode(url.getFile(), "UTF-8");
                File dir = new File(file);
                if (dir.isDirectory()) {
                    parseClassFile(dir, packageName, classMap);
                } else {
                    throw new IllegalArgumentException("file must be directory");
                }
            } else if ("jar".equals(protocol)) {
                parseJarFile(url, classMap);
            }
        }

        Set<String> set = new HashSet<>(classMap.size());
        /*if(true){
            return set;
        }*/
        for (String key : classMap.keySet()) {
            String className = classMap.get(key);
            try {
                ClassPool pool = ClassPool.getDefault();
                ClassClassPath classPath = new ClassClassPath(this.getClass());
                pool.insertClassPath(classPath);

                CtClass ctClass = pool.getCtClass(className);
                JSONObject param=new JSONObject();
                param.put("indexId",filePath);
                param.put("package",ctClass.getPackageName());
                param.put("className",ctClass.getSimpleName());
                InvokeApi invokeApi=new InvokeApi();
                JSONObject rs=invokeApi.json("/api/resource/class/add",param);
                System.out.println(rs);

                System.out.println("className:" + ctClass.getName());

                CtMethod[] m = ctClass.getMethods();
                for (CtMethod ctMethod : m) {
                    int lineNum = ctMethod.getMethodInfo().getLineNumber(0);
                    System.out.println("--method:" + ctMethod.getName() + ",lineNum:" + lineNum);
                    JSONArray paramArr=new JSONArray();




                    CtClass[] paramClss = ctMethod.getParameterTypes();
                    for (int i=0;i<paramClss.length;i++){
                        CtClass param1 =paramClss[i];
                        JSONObject paramObj=new JSONObject();
                        paramArr.add(paramObj);
                        paramObj.put("className",param1.getName());
                        paramObj.put("simpleName",param1.getSimpleName());

                        CodeAttribute codeAttribute = ctMethod.getMethodInfo().getCodeAttribute();
                        if(codeAttribute!=null){
                            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                                    .getAttribute(LocalVariableAttribute.tag);
                            if (attr != null) {
                                int len = ctMethod.getParameterTypes().length;
                                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
                                String varName=attr.variableName(i + pos);
                                paramObj.put("varName",varName);
//                                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
//                                pos=0;
//                                for (int j = 0; j < len; j++) {
//                                    try {
//                                    System.out.println("----paramName1"+attr.getName());
//                                    System.out.println("----paramName:" + attr.variableName(j + pos));
//                                    }catch (Exception e){
//                                        //e.printStackTrace();
//                                    }
//
//                                }
                            }
                        }

                    }


                    JSONObject methodParam=new JSONObject();
                    methodParam.put("indexId",filePath);
                    methodParam.put("packageName",ctClass.getPackageName());
                    methodParam.put("className",ctClass.getSimpleName());
                    methodParam.put("methodName",ctMethod.getName());
                    methodParam.put("lineNum",lineNum);
                    CtClass returnType=ctMethod.getReturnType();
                    methodParam.put("returnType",returnType.getName());
                    String paramStr=JSON.toJSONString(paramArr);
                    methodParam.put("param",paramStr);
                    System.out.println("----param:"+ paramStr);
                    InvokeApi methodParamInvokeApi=new InvokeApi();
                    JSONObject methodRs=invokeApi.json("/api/resource/method/add",methodParam);
                    System.out.println(methodRs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return set;
    }

    protected void parseClassFile(File dir, String packageName, Map<String, String> classMap) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                parseClassFile(file, packageName, classMap);
            }
        } else if (dir.getName().endsWith(CLASS_SUFFIX)) {
            String name = dir.getPath();
            name = name.substring(name.indexOf("classes") + 8).replace("\\", ".");
            // System.out.println("file:"+dir+"\t class:"+name);
            addToClassMap(name, classMap);
        }
    }

    protected void parseJarFile(URL url, Map<String, String> classMap) throws IOException {
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            String name = entry.getName();
            if (name.endsWith(CLASS_SUFFIX)) {
                addToClassMap(name.replace("/", "."), classMap);
            }
        }
    }

    private boolean addToClassMap(String name, Map<String, String> classMap) {

        if (INNER_PATTERN.matcher(name).find()) { // 过滤掉匿名内部类
            // System.out.println("anonymous inner class:"+name);
            return false;
        }
        if (name.indexOf("$") > 0) { // 内部类
            // System.out.println("inner class:"+name);
        }
        if (!classMap.containsKey(name)) {
            classMap.put(name, name.substring(0, name.length() - 6)); // 去掉.class
        }
        return true;
    }

    protected Enumeration<URL> findAllClassPathResources(String path) throws IOException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Enumeration<URL> urls = ScanJava.class.getClassLoader().getResources(path);
        return urls;
    }
}
