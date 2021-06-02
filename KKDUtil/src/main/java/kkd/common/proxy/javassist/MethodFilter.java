package kkd.common.proxy.javassist;


public interface MethodFilter {
	/**
	 * 设置方法信息
	 * @param mi
	 */
	void setMethodInfo(MethodInfo mi);
	
	/**
	 * 调用代理方法
	 * @param invoker
	 * @param key
	 * @param args
	 * @return
	 */
	Object invoke(Invoker invoker,int key,Object[] args);
}
