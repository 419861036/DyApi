//package kkd.common.dao.dbuitl.test;
//
//import kkd.common.exception.KKDValidationException;
//
//public class Source{  
//  
//	private final static int STACKLEVEL2= 2;  
//    public static void main(String[] args) {
//    	long s=System.currentTimeMillis();
//    	for (int i = 0; i < 99999; i++) {
//    		try {
//    			say();
//			} catch (Exception e) {
//			}
//		}
//    	System.out.println(System.currentTimeMillis()-s);
//    	
//	}
//    
//	private static void say() {
//		say1();
//	}
//	private static void say1() {
////		LogUtil.info("f");
//		Position2();
////		System.out.println(Position2());
//	}
//	 public static String Position2(){  
////		 StackTraceElement ste3 = new Throwable().getStackTrace()[1];
////		 ste3.getLineNumber();
//		 
////		 System.out.println(ste3.getLineNumber());
////	        return "[" +  
////	            Thread.currentThread().getName() + ":" +  
////	            Thread.currentThread().getStackTrace()[STACKLEVEL2].getFileName()   + ":" +  
////	            Thread.currentThread().getStackTrace()[STACKLEVEL2].getClassName()  + ":" +  
////	            Thread.currentThread().getStackTrace()[STACKLEVEL2].getMethodName() + "()" + ":" +  
////	            Thread.currentThread().getStackTrace()[STACKLEVEL2].getLineNumber() + "]";
////		 StringBuilder s=new  StringBuilder("[" );
////		 StackTraceElement st=Thread.currentThread().getStackTrace()[2];
////		 Thread th=Thread.currentThread();
////		 s.append(th.getName());
////		 s.append(":");
////		 s.append(st.getFileName());
////		 s.append(":");
////		 s.append(st.getClassName());
////		 s.append(":");
////		 s.append(st.getMethodName());
////		 s.append(":");
////		 s.append(st.getLineNumber());
////		 s.append(":");
//		 if(true){
//			 throw new KKDValidationException("");
//		 }
//		 StringBuilder s=new  StringBuilder("[" );
////		 StackTraceElement st=Thread.currentThread().getStackTrace()[2];
////		 new Throwable();
//		
////		 StackTraceElement ste3 = new Throwable().getStackTrace()[1];
//		 Thread th=Thread.currentThread();
//		 s.append(th.getName());
//		 s.append(":");
//		 s.append("s");
//		 s.append(":");
//		 s.append("1");
//		 s.append(":");
//		 s.append("sd");
//		 s.append(":");
//		 s.append("sdf");
//		 s.append(":");
//		
//		 return s.toString();
//		 
////		 StringBuilder s=new  StringBuilder("[" );
////		 StackTraceElement st=Thread.currentThread().getStackTrace()[2];
////		 Thread th=Thread.currentThread();
////		 s.append(th.getName());
////		 s.append(":");
////		 s.append(st.getFileName());
////		 s.append(":");
////		 s.append(st.getClassName());
////		 s.append(":");
////		 s.append(st.getMethodName());
////		 s.append(":");
////		 s.append(st.getLineNumber());
////		 s.append(":");
////		 return s.toString();
//	    }
//}  
