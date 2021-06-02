//package kkd.common.dao.dbuitl.test;
//
//public class LogUtil {
//	
//	 private final static int STACKLEVEL= 1;  
//	 private final static int STACKLEVEL2= 2;  
//	 public static String Position1(){       
//	        StackTraceElement[] stacks = new Throwable().getStackTrace();       
//	  
//	        if(stacks.length < (STACKLEVEL + 1)){  
//	            return "";  
//	        }  
//	  
//	        StringBuffer sb = new StringBuffer();        
//	        sb.append("[")  
//	            .append(Thread.currentThread().getName())  
//	            .append(":" + stacks[STACKLEVEL2].getFileName())  
//	            .append(":" + stacks[STACKLEVEL2].getClassName())  
//	            .append(":" + stacks[STACKLEVEL2].getMethodName() + "()")  
//	            .append(":" + stacks[STACKLEVEL2].getLineNumber())  
//	            .append("]");  
//	  
//	        return sb.toString();       
//	    }  
//	    public static String Position2(){  
//	        return "[" +  
//	            Thread.currentThread().getName() + ":" +  
//	            Thread.currentThread().getStackTrace()[STACKLEVEL2].getFileName()   + ":" +  
//	            Thread.currentThread().getStackTrace()[STACKLEVEL2].getClassName()  + ":" +  
//	            Thread.currentThread().getStackTrace()[STACKLEVEL2].getMethodName() + "()" + ":" +  
//	            Thread.currentThread().getStackTrace()[STACKLEVEL2].getLineNumber() + "]";  
//	    }
//	public static void info(String s){
//		String s1=Position1();
//    	System.out.println(s1+s);
//	}
//	public static void main(String[] args) {
//		
//		System.out.println(Position2());
//	}
//}
