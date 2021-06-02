package kkd.common.proxy.javassist;

public interface Invoker {
	public Object invokeSuper(int key,Object[] args);
	public Object newInstance();
	
}
