package kkd.common.invoke;

import java.util.HashMap;
import java.util.Map;

import kkd.common.proxy.javassist.ProxyHandle;
import kkd.common.proxy.test.t.MethodFilterImpl;

public class JavassistInvokeUtil {
	private static final Map<Class<?>, ProxyHandle> methodCache = new HashMap<Class<?>, ProxyHandle>();

	public static ProxyHandle<?> getInvokerHandle(Class<?> cls) {
		ProxyHandle ph = null;
		ph=methodCache.get(cls);
		if (ph == null) {
			ph=new ProxyHandle<>();
			ph.setSuperclass(cls);
			MethodFilterImpl mfi=new MethodFilterImpl();
			ph.setFilter(mfi);
			ph.createClass();
			methodCache.put(cls, ph);
		}
		return ph;
	}

	public static ProxyHandle getInvokerHandle(Object ic) {
		ProxyHandle ph = null;
		Class<?> cls = ic.getClass();
		ph=methodCache.get(cls);
		if (ph == null) {
			ph.setSuperclass(ic.getClass());
			MethodFilterImpl mfi=new MethodFilterImpl();
			ph.setFilter(mfi);
			ph.createClass();
			
			methodCache.put(cls, ph);
		}
		return ph;
	}

	public static void clear() {
		methodCache.clear();
	}
	
}
