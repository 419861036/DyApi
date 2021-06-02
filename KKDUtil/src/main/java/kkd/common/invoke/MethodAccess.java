package kkd.common.invoke;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_1;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public abstract class MethodAccess {
	private  Map<String,Integer> pathMap=null;
	private String[] methodNames;
	private Class<?>[][] parameterTypes;
//	private static  int count=0;
	private final static String CLASSNAME=MethodAccess.class.getName().replace(".", "/");
	abstract public Object newInstance ();
	
	abstract public Object invoke (Object object, int methodIndex, Object... args);

	public Object invoke (Object object, String methodName, Object... args) {
		int index= getIndex(methodName);
		return invoke(object,index, args);
	}

	public Object invokeByPath (Object object, String path, Object... args) {
		if(pathMap!=null){
			int index=pathMap.get(path);
			return invoke(object, index, args);
		}
		throw new IllegalArgumentException("Unable to find the path: " + path);
	}
	
	public int getIndex (String methodName) {
		for (int i = 0, n = methodNames.length; i < n; i++){
			if (methodNames[i].equals(methodName)) return i;
		}
		throw new IllegalArgumentException("Unable to find public method: " + methodName);
	}

	public int getIndex (String methodName, Class<?>... paramTypes) {
		for (int i = 0, n = methodNames.length; i < n; i++){
			if (methodNames[i].equals(methodName) && Arrays.equals(paramTypes, parameterTypes[i])) return i;
		}
		throw new IllegalArgumentException("Unable to find public method: " + methodName + " " + Arrays.toString(parameterTypes));
	}

	public String[] getMethodNames () {
		return methodNames;
	}

	public Class<?>[][] getParameterTypes () {
		return parameterTypes;
	}

	public static MethodAccess get (Class<?> type) {
		List<Method> methods = new ArrayList<Method>();
		//获取所有方法
		getMethods(type, methods);
		
		String className = type.getName();
//		String accessClassName =Config.getProxyName();
		String accessClassName =className+"$";
		if (accessClassName.startsWith("java.")) accessClassName = "reflectasm." + accessClassName;
		Class<?> accessClass = null;

		AccessClassLoader loader = AccessClassLoader.get(type);
		synchronized (loader) {
			try {
				accessClass = loader.loadClass(accessClassName);
			} catch (ClassNotFoundException ignored) {
				String accessClassNameInternal = accessClassName.replace('.', '/');
				String classNameInternal = className.replace('.', '/');

				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				cw.visit(V1_1, ACC_PUBLIC + ACC_SUPER, accessClassNameInternal, null, CLASSNAME,
					null);
				{
					genInitCode(cw);
				}
				{
					genNewInstance(classNameInternal, cw);
				}
				{
					genGetCode(methods, classNameInternal, cw);
				}
				cw.visitEnd();
				byte[] data = cw.toByteArray();
//				FileUtil.appendMethodA("E:/a.class", data);
				accessClass = loader.defineClass(accessClassName, data);
			}
		}
		try {
			Class<?>[][] parameterTypes = new Class[methods.size()][];
			String[] methodNames = new String[methods.size()];
			Map<String,Integer> pathMap=null;
			for (int i = 0, n = methodNames.length; i < n; i++) {
				Method method = methods.get(i);
				MethodPath a=method.getAnnotation(MethodPath.class);
				if(a!=null){
					String path=a.value();
					if(pathMap==null){
						pathMap=new HashMap<String, Integer>();
					}
					pathMap.put(path, i);
				}
				methodNames[i] = method.getName();
				parameterTypes[i] = method.getParameterTypes();
			}
			MethodAccess access = (MethodAccess)accessClass.newInstance();
			access.methodNames = methodNames;
			access.parameterTypes = parameterTypes;
			access.pathMap=pathMap;
			return access;
		} catch (Exception ex) {
			throw new RuntimeException("Error constructing method access class: " + accessClassName, ex);
		}
	}

	private static void genGetCode(List<Method> methods,
			String classNameInternal, ClassWriter cw) {
		MethodVisitor mv;
		mv = cw.visitMethod(ACC_PUBLIC , "invoke",
			"(Ljava/lang/Object;I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		mv.visitCode();

		if (!methods.isEmpty()) {
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(CHECKCAST, classNameInternal);
			mv.visitVarInsn(ASTORE, 4);

			mv.visitVarInsn(ILOAD, 2);
			Label[] labels = new Label[methods.size()];
			for (int i = 0, n = labels.length; i < n; i++){
				labels[i] = new Label();
			}
				
			Label defaultLabel = new Label();
			mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

			StringBuilder buffer = new StringBuilder(128);
			for (int i = 0, n = labels.length; i < n; i++) {
				mv.visitLabel(labels[i]);
				if (i == 0)
					mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] {classNameInternal}, 0, null);
				else
					mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
				Method method = methods.get(i);
				mv.visitVarInsn(ALOAD, 4);
				buffer.setLength(0);
				buffer.append('(');
				Class<?>[] paramTypes = method.getParameterTypes();
				for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
					mv.visitVarInsn(ALOAD, 3);
					mv.visitIntInsn(BIPUSH, paramIndex);
					mv.visitInsn(AALOAD);
					Type paramType = Type.getType(paramTypes[paramIndex]);
					switch (paramType.getSort()) {
					case Type.BOOLEAN:
						mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
						break;
					case Type.BYTE:
						mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B");
						break;
					case Type.CHAR:
						mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C");
						break;
					case Type.SHORT:
						mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S");
						break;
					case Type.INT:
						mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
						break;
					case Type.FLOAT:
						mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F");
						break;
					case Type.LONG:
						mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J");
						break;
					case Type.DOUBLE:
						mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
						break;
					case Type.ARRAY:
						mv.visitTypeInsn(CHECKCAST, paramType.getDescriptor());
						break;
					case Type.OBJECT:
						mv.visitTypeInsn(CHECKCAST, paramType.getInternalName());
						break;
					}
					buffer.append(paramType.getDescriptor());
				}

				buffer.append(')');
				buffer.append(Type.getDescriptor(method.getReturnType()));
				
				if(Modifier.isStatic(method.getModifiers())){
					mv.visitMethodInsn(INVOKESTATIC, classNameInternal, method.getName(), buffer.toString());
				}else{
					mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, method.getName(), buffer.toString());
				}
				
				genReturnCode(mv, method);
			}

			mv.visitLabel(defaultLabel);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		}
		mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
		mv.visitInsn(DUP);
		mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
		mv.visitInsn(DUP);
		mv.visitLdcInsn("Method not found: ");
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
		mv.visitVarInsn(ILOAD, 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
		mv.visitInsn(ATHROW);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	private static void getMethods(Class<?> type, List<Method> methods) {
		Class<?> nextClass = type;
		while (nextClass != Object.class) {
			Method[] declaredMethods = nextClass.getDeclaredMethods();
			for (int i = 0, n = declaredMethods.length; i < n; i++) {
				Method method = declaredMethods[i];
				int modifiers = method.getModifiers();
//				if (Modifier.isStatic(modifiers)) continue;
				if (Modifier.isPrivate(modifiers)) continue;
				methods.add(method);
			}
			nextClass = nextClass.getSuperclass();
		}
	}

	private static void genReturnCode(MethodVisitor mv, Method method) {
		switch (Type.getType(method.getReturnType()).getSort()) {
			case Type.VOID:
				mv.visitInsn(ACONST_NULL);
				break;
			case Type.BOOLEAN:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
				break;
			case Type.BYTE:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
				break;
			case Type.CHAR:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
				break;
			case Type.SHORT:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
				break;
			case Type.INT:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
				break;
			case Type.FLOAT:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
				break;
			case Type.LONG:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
				break;
			case Type.DOUBLE:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
				break;
		}

		mv.visitInsn(ARETURN);
	}

	private static void genNewInstance(String classNameInternal, ClassWriter cw) {
		MethodVisitor mv;
		mv = cw.visitMethod(ACC_PUBLIC, "newInstance", "()Ljava/lang/Object;", null, null);
		mv.visitCode();
		mv.visitTypeInsn(NEW, classNameInternal);
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, classNameInternal, "<init>", "()V");
		mv.visitInsn(ARETURN);
		mv.visitMaxs(2, 1);
		mv.visitEnd();
	}

	private static void genInitCode(ClassWriter cw) {
		MethodVisitor mv;
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, CLASSNAME, "<init>", "()V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}
}
