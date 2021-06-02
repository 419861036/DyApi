package kkd.common.invoke;

import java.util.HashMap;

public class InvokeUtil {

	private static final HashMap<Class<?>, MethodAccess> methodCache = new HashMap<Class<?>, MethodAccess>();

	public static Object invoke(Object ic, String method, Object... args) {
		Object rs = getMethodAccess(ic).invoke(ic, method, args);
		return rs;
	}

	public static Object invoke(Object ic, Integer index, Object... args) {
		Object rs = getMethodAccess(ic).invoke(ic, index, args);
		return rs;
	}

	public static Integer getIndex(Class<?> cls, String methodName) {
		return getMethodAccess(cls).getIndex(methodName);
	}

	public static Integer getIndex(Class<?> cls, String methodName,
			Class<?>... paramTypes) {
		return getMethodAccess(cls).getIndex(methodName, paramTypes);
	}

	public static MethodAccess getMethodAccess(Class<?> cls) {
		MethodAccess ma = null;
//		String clsName = cls.getName();
//		ma = methodCache.get(clsName);
		ma=methodCache.get(cls);
		if (ma == null) {
			ma = MethodAccess.get(cls);
			methodCache.put(cls, ma);
		}
		return ma;
	}

	public static MethodAccess getMethodAccess(Object ic) {
		MethodAccess ma = null;
		Class<?> cls = ic.getClass();
//		String clsName = cls.getName();
//		ma = methodCache.get(clsName);
		ma=methodCache.get(cls);
		if (ma == null) {
			ma = MethodAccess.get(cls);
			methodCache.put(cls, ma);
		}
		return ma;
	}

	public static void clear() {
		methodCache.clear();
	}
}
