//package com.exe.base.util;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Formatter;
//
///**
// * 日志打印
// * 
// * @author tanbin
// *
// */
//public class LogUtil {
//
//    private static Formatter formatter ;
//    public static String lev="info";
//    private String ruleId;
//    private String webPath="F:\\code_store\\new_yqyx\\module\\exe\\exeWeb\\static\\html\\logs";
//    
//    public LogUtil() {
//    	
//	}
//    
//    public void setRuleId(String ruleId) {
//    	this.ruleId=ruleId;
//    }
//    
//    public static void setLev(String lev1){
//        lev=lev1;
//    }
//
//
//	public  void info(String msg,Object... param){
//        if("info".equalsIgnoreCase(lev)){
//        	try {
////        		File f=new File(webPath+"\\"+ruleId+"\\log.log");
////        		if(!f.exists()) {
////        			f.getParentFile().mkdirs();
////        			f.createNewFile();
////        		}
////    			formatter = new Formatter(f);
//        		formatter = new Formatter(System.out);
//    		} catch (Exception e) {
//    			e.printStackTrace();
//    		}
//            formatter.format(msg+"\r\n", param);
////            formatter.close();
//        }
//    }
//
//    public  void error(String msg,Object... param){
//        if("error".equalsIgnoreCase(lev)){
//        	try {
////        		File f=new File(webPath+"\\"+ruleId+"\\log.log");
////        		if(!f.exists()) {
////        			f.getParentFile().mkdirs();
////        			f.createNewFile();
////        		}
////    			formatter = new Formatter(f);
//        		formatter = new Formatter(System.out);
//    		} catch (Exception e) {
//    			e.printStackTrace();
//    		}
//            formatter.format(msg+"\r\n", param);
////            formatter.close();
//        }
//    }
//
//    public static void main(String[] args) {
////        info("test{}", "12");
//    }
//
//}
