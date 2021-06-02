package kkd.common.proxy.javassist;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MethodInfo {
	private Class<?> superClass;
	private Method[] currentClassMethod;
	private Method[] allMethod;
	
	public Method[] getCurrentClassMethod() {
		return allMethod;
	}
	

	public void init(Class<?> superClass,List<Method> allMethod){
		this.superClass=superClass;
		currentClassMethod=superClass.getDeclaredMethods();
		this.allMethod=new Method[allMethod.size()];
		for (int i = 0; i < allMethod.size(); i++) {
			this.allMethod[i]=allMethod.get(i);
		}
	}
	
	
	public int getIndex(String name,Class<?>... parameterTypes){
		try {
			for (int i = 0; i < currentClassMethod.length; i++) {
				Method m=currentClassMethod[i];
				Class<?>[] mtypes=m.getParameterTypes();
				if (m.getName().equals(name) && Arrays.equals(mtypes, parameterTypes)) {
					return i;
				}
			}
		} catch (Exception e) {
			return -1;
		}
		return -1;
	}
	
	
	public static void main(String[] args) {
		
	}
}
