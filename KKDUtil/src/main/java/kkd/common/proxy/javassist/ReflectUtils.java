package kkd.common.proxy.javassist;

import java.lang.reflect.Method;

public class ReflectUtils {
	/**
	 * void(V).
	 */
	public static final char JVM_VOID = 'V';

	/**
	 * boolean(Z).
	 */
	public static final char JVM_BOOLEAN = 'Z';

	/**
	 * byte(B).
	 */
	public static final char JVM_BYTE = 'B';

	/**
	 * char(C).
	 */
	public static final char JVM_CHAR = 'C';

	/**
	 * double(D).
	 */
	public static final char JVM_DOUBLE = 'D';

	/**
	 * float(F).
	 */
	public static final char JVM_FLOAT = 'F';

	/**
	 * int(I).
	 */
	public static final char JVM_INT = 'I';

	/**
	 * long(J).
	 */
	public static final char JVM_LONG = 'J';

	/**
	 * short(S).
	 */
	public static final char JVM_SHORT = 'S';

	public static String getDesc(Class<?> c) {
		StringBuilder ret = new StringBuilder();

		while (c.isArray()) {
			ret.append('[');
			c = c.getComponentType();
		}

		if (c.isPrimitive()) {
			String t = c.getName();
			if ("void".equals(t))
				ret.append(JVM_VOID);
			else if ("boolean".equals(t))
				ret.append(JVM_BOOLEAN);
			else if ("byte".equals(t))
				ret.append(JVM_BYTE);
			else if ("char".equals(t))
				ret.append(JVM_CHAR);
			else if ("double".equals(t))
				ret.append(JVM_DOUBLE);
			else if ("float".equals(t))
				ret.append(JVM_FLOAT);
			else if ("int".equals(t))
				ret.append(JVM_INT);
			else if ("long".equals(t))
				ret.append(JVM_LONG);
			else if ("short".equals(t))
				ret.append(JVM_SHORT);
		} else {
			ret.append('L');
			ret.append(c.getName().replace('.', '/'));
			ret.append(';');
		}
		return ret.toString();
	}

	public static String getDesc(final Class<?>[] cs) {
		if (cs.length == 0)
			return "";

		StringBuilder sb = new StringBuilder(64);
		for (Class<?> c : cs)
			sb.append(getDesc(c));
		return sb.toString();
	}

	public static String getDesc(final Method m) {
		StringBuilder ret = new StringBuilder(m.getName()).append('(');
		Class<?>[] parameterTypes = m.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++)
			ret.append(getDesc(parameterTypes[i]));
		ret.append(')').append(getDesc(m.getReturnType()));
		return ret.toString();
	}
}
