package kkd.common.proxy.javassist;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

/**
 * 动态代理处理类
 * @author tanbin
 *
 * @param <T>
 */
public class ProxyHandle<T> {
	
	private Class<?>[] intefaces;
	private Class<T> superClass;
	private Class<T> proxyClass;
	private String	proxyClsName;
	private MethodFilter methodFilter;
	private MethodInfo methodInfo;
	private boolean debug=true;

	private static int proxyNameIndex=0;
	
	public ProxyHandle() {
		methodInfo=new MethodInfo();
	}
	public void setDebug(boolean debug){
		this.debug=debug;
	}
	
	public void setSuperclass(Class<T> clazz) {
		this.superClass = clazz;
	}

	
	public void setIntefaces(Class<?>[] intefaces) {
		this.intefaces = intefaces;
		
	}

	public void setFilter(MethodFilter methodFilter) {
		this.methodFilter = methodFilter;
		methodFilter.setMethodInfo(methodInfo);
	}

	public MethodInfo getMethodInfo(){
		return methodInfo;
	}
	private List<Method> getAllMethod(){
		Set<String> keys=new HashSet<>();
		List<Method> methods = new ArrayList<Method>();
		Method[]  ms=superClass.getMethods();
		addMethod(keys, methods, ms);
		for (int i = 0;intefaces!=null&& i < intefaces.length; i++) {
			ms=intefaces[i].getMethods();
			addMethod(keys, methods, ms);
		}
		return methods;
	}
	
	private void addMethod(Set<String> keys,List<Method> methods,Method[] ms){
		for (int j = 0; j < ms.length; j++) {
			Method method=ms[j];
			if(method.getDeclaringClass().getCanonicalName().startsWith("java.")){
				continue;
			}
			int mod = method.getModifiers();
			if(!Modifier.isFinal(mod)
					&& (Modifier.isProtected(mod) || Modifier.isPublic(mod))){
				String desc=ReflectUtils.getDesc(method);
				if(keys.contains(desc)){
					continue;
				}
				methods.add(ms[j]);	
				keys.add(desc);
			}
		}
	}
	
	
	
	public Object invoke(String methodName,Object obj,Class[] argTypes, Object... args){
		int index=methodInfo.getIndex(methodName, argTypes);
		return invoke(index, obj, args);
	}
	
	public Object invoke(int index,Object obj, Object... args){
		Invoker invoker=(Invoker)obj;
		return invoker.invokeSuper(index, args);
	}
	
	private Invoker invoker=null;
	public Object newInstance(){
		try {
			if(invoker==null){
				invoker=(Invoker)proxyClass.newInstance();
			}
			return invoker.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Class<?> createClass() {
		methodInfo.init(superClass, getAllMethod());
		ClassPool pool = ClassPool.getDefault();
		ClassPath cp=new LoaderClassPath(superClass.getClassLoader()); 
		pool.appendClassPath(cp);
		CtClass parent = null;
		CtClass ifs = null;
		CtClass cc = null;
		try {
			parent = pool.get(superClass.getName());
			ifs = pool.get(Invoker.class.getName());
			cc = pool.makeClass(genClassName(), parent);
			cc.addInterface(ifs);
			for (int i = 0;intefaces!=null&& i < intefaces.length; i++) {
				CtClass tmpIfc = pool.get(intefaces[i].getCanonicalName());
				cc.addInterface(tmpIfc);
			}
		} catch (NotFoundException e1) {
			e1.printStackTrace();
		}
		Class<T> clazz = null;
		// 创建属性
		try {
			String methodFilterName = methodFilter.getClass().getName();
			String field="public static " + methodFilterName+ " methodFilter;";
			log(field);
			CtField f1 = CtField.make(field, cc);
			cc.addField(f1);
			//构造方法
			CtConstructor c0 = new CtConstructor(new CtClass[]{}, cc);
			c0.setBody("{}");
			cc.addConstructor(c0);
			
			Method[] methods = getMethodInfo().getCurrentClassMethod();
			
			for (int i = 0; i < methods.length; i++) {
				Method m = methods[i];
				int mod = m.getModifiers();
				if (!Modifier.isFinal(mod)
						&& (Modifier.isProtected(mod) || Modifier.isPublic(mod))) {
					String str = getMethodStr(m, i);
					log(str);
					CtMethod m2 = CtMethod.make(str, cc);
					cc.addMethod(m2);
				}
			}
			
			String invokerStr=getInvokerStr(methods);
			log(invokerStr);
			CtMethod m1 = CtMethod.make(invokerStr, cc);
			cc.addMethod(m1);
			
			String newInstanceStr=getNewInstanceStr();
			log(newInstanceStr);
			CtMethod m3 = CtMethod.make(newInstanceStr, cc);
			cc.addMethod(m3);
			
			
			write2local(cc); 
			clazz = cc.toClass();
			Field f=clazz.getField("methodFilter");
			f.setAccessible(true); 
            f.set(null, methodFilter);
            f.setAccessible(false); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		proxyClass=clazz;
		return clazz;
	}

	private void write2local(CtClass cc) throws CannotCompileException,
			IOException {
		if(debug){
			cc.writeFile("E:/LearnJavaProjectText/myjava");
		}
	}

	private String genClassName() {
		proxyClsName=superClass.getCanonicalName()+"javassist"+(proxyNameIndex++);
		return proxyClsName;
	}

	private static String getMethodStr(Method m, int index) {
		StringBuilder sb = new StringBuilder();
		sb.append("public ");
		sb.append(m.getReturnType().getCanonicalName());
		sb.append(" ");
		sb.append(m.getName());
		
		sb.append("(");
		Class<?>[] cls = m.getParameterTypes();
		for (int i = 0; i < cls.length; i++) {
			sb.append(cls[i].getCanonicalName());
			sb.append(" a" + i);
			if (cls.length - 1 != i) {
				sb.append(",");
			}
		}
		sb.append(")");
		sb.append("{");

		StringBuilder sb1 = new StringBuilder();
		sb1.append("Object[] args=new Object[" + cls.length + "];");
		for (int i = 0; i < cls.length; i++) {
			sb1.append("args[" + i + "]=");
			String argType=cls[i].getName();
			TypeDef def=Types.getTypeByCanonicalName(argType);
			if(def!=null){
				sb1.append(" new java.lang."+def.getObjName()+"(a" + i + ");");
			}else{
				sb1.append(" a" + i + ";");
			}
		}
		sb.append(sb1);
		sb.append("int key=" + index + ";");
		
		String returnType=m.getReturnType().getName();
		TypeDef def=Types.getTypeByCanonicalName(returnType);
		if(def!=null){
			sb.append(def.getSimpleName()+" r=");
			sb.append(" ((java.lang."+def.getObjName()+")methodFilter.invoke(this,key,args))"+def.getSimpleVal()+";");
		}else{
			if(!m.getReturnType().getName().equals("void")){
//				sb.append("java.lang.Object r=(");
				sb.append(m.getReturnType().getName());
				sb.append(" r=(");
				sb.append(m.getReturnType().getName());
				sb.append(")");
			}
			
			sb.append(" methodFilter.invoke(this,key");
			sb.append(",args);");
		}
		
		if(!"void".equals(m.getReturnType().getName())){
			sb.append("return r;");
		}else{
			sb.append("return ;");
		}
		sb.append("}");
		return sb.toString();
	}

	public static String getInvokerStr(Method[] ms) {
		StringBuilder sb = new StringBuilder();
		sb.append("public java.lang.Object invokeSuper(int key,java.lang.Object[] args) {");
		sb.append("switch (key) {"); 
		for (int i = 0; i < ms.length; i++) {
			Method m = ms[i];
			int mod = m.getModifiers();
			if (!Modifier.isFinal(mod)
					&& (Modifier.isProtected(mod) || Modifier.isPublic(mod))) {
				sb.append("case "+i+":");
				Class<?>[] cls = m.getParameterTypes();
				for (int j = 0; j < cls.length; j++) {
					String argType=cls[j].getName();
					TypeDef def=Types.getTypeByCanonicalName(argType);
					if(def!=null){
						sb.append(cls[j].getCanonicalName()).append(" a" + j +"_"+i +"=(("+def.getObjName()+")args[").append(j).append("])"+def.getSimpleVal()+";");
					}else{
						sb.append(cls[j].getCanonicalName()).append(" a" + j +"_"+i +"=(").append(")args[").append(j).append("];");
					}
				}
				returnTypeConvert(sb, i, m, cls);
				
			}
		}
		sb.append("default:throw new IllegalArgumentException(\"method is not found\");");
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	private static void returnTypeConvert(StringBuilder sb, int i, Method m,
			Class<?>[] cls) {
		String returnType=m.getReturnType().getName();
		TypeDef def=Types.getTypeByCanonicalName(returnType);
		if(def!=null){
			sb.append("java.lang.Object r"+i+"=(java.lang.Object)new java.lang."+def.getObjName()+"(");
			sb.append(m.getName());
			sb.append("(");
			for (int j = 0; j < cls.length; j++) {
				sb.append(" a" + j +"_"+i );
				if (cls.length - 1 != j) {
					sb.append(",");
				}
			}
			sb.append("));");
		}else{
			if(!m.getReturnType().getName().equals("void")){
				sb.append("java.lang.Object r"+i+"=(java.lang.Object)");
			}
//			sb.append("super.")
			sb.append(m.getName()).append("(");
			for (int j = 0; j < cls.length; j++) {
				sb.append(" a" + j +"_"+i );
				if (cls.length - 1 != j) {
					sb.append(",");
				}
			}
			sb.append(");");
		}
		if(!"void".equals(m.getReturnType().getName())){
			sb.append("return r"+i+";");
		}else{
			sb.append("return null;");
		}
	}
	
	public  String getNewInstanceStr() {
		StringBuilder sb = new StringBuilder();
		sb.append("public  Object newInstance() {return new ").append(proxyClsName).append("();}");
		return sb.toString();
	}
	
	public void log(String msg){
		if(debug){
			System.out.println(msg);
		}
	}
	
	
}
