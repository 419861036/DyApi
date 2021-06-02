package kkd.common.proxy.test;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkd.common.proxy.javassist.MethodFilter;
import kkd.common.proxy.javassist.ProxyHandle;
import kkd.common.proxy.test.t.DbHelperHolder;
import kkd.common.proxy.test.t.MethodFilterImpl;
import kkd.common.proxy.test.user.IUserService;
import kkd.common.proxy.test.user.UserService;

import com.alibaba.fastjson.JSON;

public class ProxyTest {
	public static void main(String[] args) throws Exception {
		Method[] ms = UserService.class.getDeclaredMethods();
//		for (int i = 0; i < ms.length; i++) {
//			Method m = ms[i];
//			int mod = m.getModifiers();
//			if (!Modifier.isFinal(mod) && (Modifier.isProtected(mod) || Modifier.isPublic(mod))) {
//				System.out.println();
//				String s = ProxyHandle.getMethodStr(m,i);
//				System.out.println(s);
//			}
//		}
//		String s=ProxyHandle.getInvokerByNameStr(ms);
//		System.out.println(s);
//		Proxy1 p=new Proxy1();
//		p.setMethodFilter(new MethodFilterImpl());
//		p.hello("", 1);
		test1();
//		test2();
//		test3();
//		test4();
//		test5(null, null);
//		test2();
	}

	private static void test2() {
		Method[] ms = UserService.class.getDeclaredMethods();
		for (Method method : ms) {
			Class[] a=method.getParameterTypes();
			for (Class class1 : a) {
				System.out.println(class1.getCanonicalName()+"	"+class1.getName());
			}
		}
	}

	//	private static void test4() {
//		//对比 原生 new 和 反射new
//		Long s = System.currentTimeMillis();
//		for (int i = 0; i < 99999999; i++) {
//			UserService u=new UserService();
//		}
//		System.out.println(System.currentTimeMillis() - s);
//		Class<?> c=UserService.class;
//		UserService u=null;
//		try {
//			u=(UserService)c.newInstance();
//		} catch (InstantiationException | IllegalAccessException e1) {
//			e1.printStackTrace();
//		}
//		s = System.currentTimeMillis();
//		for (int i = 0; i < 99999999; i++) {
//			try {
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println(System.currentTimeMillis() - s);
//	}
//
//	private static void test3() {
//		ProxyHandle<UserService> p = new ProxyHandle<UserService>();
//		p.setSuperclass(UserService.class);
//		p.setFilter(new MethodFilterImpl());
//		p.createClass();
//		
//		
//		long s = System.currentTimeMillis();
//		for (int i = 0; i < 99999999; i++) {
//			Object obj=p.newInstance();
//		}
//		System.out.println(System.currentTimeMillis() - s);
//		
//		s = System.currentTimeMillis();
//		for (int i = 0; i < 99999999; i++) {
//			UserService u=new UserService();
//		}
//		System.out.println(System.currentTimeMillis() - s);
//		
//		Class cls=UserService.class;
//		s = System.currentTimeMillis();
//		for (int i = 0; i < 99999999; i++) {
//			try {
//				Object u=cls.newInstance();
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println(System.currentTimeMillis() - s);
//		
//		
//	}
//
//	private static void test2() {
//		ProxyHandle<UserService> p = new ProxyHandle<UserService>();
//		p.setSuperclass(UserService.class);
//		p.setFilter(new MethodFilterImpl());
//		int index=p.getMethodInfo().getIndex("hello", String.class);
//		Class<?> c=p.createClass();
//		try {
//			Object obj=c.newInstance();
//			Integer r=(Integer)p.invoke(index, obj, "");
//			System.out.println(r);
//		}catch (Exception e) {
//		}
//	}
	private static void test1() {
		ProxyHandle p = new ProxyHandle();
		p.setSuperclass(Tmp.class);
		Class[] inf={IUserService.class};
		p.setIntefaces(inf);
		MethodFilter mf=new RpcMethodFilter();
		mf.setMethodInfo(p.getMethodInfo());
		p.setFilter(mf);
		p.createClass();
		IUserService u=(IUserService)p.newInstance();
		Long s = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			int a=u.hello();
			System.out.println(a);
		}
	}
	
	private static void test5(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String path = req.getPathInfo();
		if(true){
			String className="kkd.common.proxy.test.user.UserService";
			String methd="addUser";
			String body="";
			Class<?> classImpl=Class.forName(className);
			ProxyHandle p = new ProxyHandle();
			p.setSuperclass(classImpl);
			
			MethodFilterImpl mfi=new MethodFilterImpl();
			mfi.setDh(DbHelperHolder.getDbHelper());
			p.setFilter(mfi);
			
			Object arg=JSON.parse(body);
			
			Object obj=p.newInstance();
			int index=p.getMethodInfo().getIndex(methd, arg.getClass());
			Object r=p.invoke(index, obj, arg.getClass());
			String rStr=JSON.toJSONString(r);
			resp.getWriter().print(rStr);
		}
		
		
	}
}
