package kkd.common.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 集合工具类
 * @author Administrator
 *
 */
public class ListUtil {
	/**
	 * 判断是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(Collection<?> list){
		if(list!=null){
			if(!list.isEmpty()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * list  转 数组
	 * @param <T>
	 * @param list
	 * @param type
	 * @return
	 */
	 public static <T> T[]  listToArray(List<T> list,Class<?> type){
		 if(list == null || type == null){
			 return null;
		 }
		 @SuppressWarnings("unchecked")
		T[] ts =(T[])Array.newInstance(type, list.size());
		 for (int i = 0; i < list.size(); i++) {
			ts[i]=list.get(i);
		}
		 return ts;
	 }
	 /**
	  * 数组转 list
	  * @param <T>
	  * @param array
	  * @param type
	  * @return
	  */
	 public static <T> List<T>  arrayToList(T[] array){
		 if(array == null){
			 return null;
		 }
		List<T> list=new ArrayList<T>();
		 for (int i = 0; i < array.length; i++) {
			 list.add(array[i]);
		}
		 return list;
	 }
	 
	 
//	public static <T> T[]  arrayToArray(Object[] array,Class<?> type){
//		 if(array == null || type == null){
//			 return null;
//		 }
//		 @SuppressWarnings("unchecked")
//		T[] ts =(T[])Array.newInstance(type, array.length);
//		 for (int i = 0; i < array.length; i++) {
//			 if(array[i]==null){
//				 ts[i]=null;
//			 }else{
//				 if(array[i] instanceof String){
//					 ts[i]=(T)Integer.valueOf(array[i].toString().trim());
//				 }else{
//					 ts[i]=(T)array[i]; 
//				 }
//			 }
//		}
//		 return ts;
//	 }
	 
	 public static String listObjToString(List<Object> s){
			if(s==null || s.size()==0){
				return null;
			}
			StringBuilder sb= new StringBuilder();
			for (int i = 0; i < s.size(); i++) {
				sb.append("'"+s.get(i)+"'");
				if(i!=s.size()-1){
					sb.append(",");
				}
			}
			return sb.toString();
	 }
	 
	 public static String arrayObjToString(Object[] s){
			if(s==null || s.length==0){
				return null;
			}
			StringBuilder sb= new StringBuilder();
			for (int i = 0; i < s.length; i++) {
				sb.append("'"+s[i]+"'");
				if(i!=s.length-1){
					sb.append(",");
				}
			}
			return sb.toString();
	 }
	 
	 
	 public Set<String> list2Set(List<String> values) {
	        Set<String> result = new HashSet<String>();
	        for (String v : values) {
	            result.add(v);
	        }
	        return result;
	 }
	 
	 public List<String> set2List(Set<String> values) {
	        List<String> result = new ArrayList<String>();
	        for (String v : values) {
	            result.add(v);
	        }
	        return result;
	 }
	 
	 public static void main(String[] args) {
		 Double[] d=new Double[5];
		 d[0]=1.1;
		 d[1]=2.1;
		 d[2]=3.1;
		 d[3]=4.1;
		 d[4]=5.1;
		List<Double> r= arrayToList(d);
		for (int i = 0; i < r.size(); i++) {
			System.out.println(r.get(i));
		}
	}
}
