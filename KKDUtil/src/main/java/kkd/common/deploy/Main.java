package kkd.common.deploy;

import kkd.common.invoke.InvokeUtil;
import kkd.common.invoke.MethodAccess;


public class Main {
	public static void main(String[] args) throws Exception {
		long l=21474853508l;
//		System.out.println(Long.toBinaryString(l));
//		10100000000000000000100001010000100
//		thequickbrownfoxjumpsoverthelazydog
//		t e					s    t e    d		
//		tested
////		t();
////		Class cls =  Class.forName("kkd.common.deploy.Foo");
//		MethodAccess ma = InvokeUtil.getMethodAccess(Foo.class);
//		int a=InvokeUtil.getIndex(Foo.class, "sayHello");
////		Object o = ma.newInstance();
////		Object obj=cls.newInstance();
////		Foo f=new Foo();
//		Object o = ma.newInstance();
//		Foo f=new Foo();
////		 Method m = cls.getMethod("sayHello", new Class[]{}); 
//		long s=System.currentTimeMillis();
//		for (int i = 0; i < 100000000; i++) {
////			String[] ss={""};
////			ma.invoke(o, 0,null);
////			InvokeUtil.invoke(obj, a);
////			InvokeUtil.invoke(o, a);
////			Foo f=new Foo();
////			f.sayHello();
////		     m.invoke(obj, new Object[]{}); 
//		}
//		System.out.println(System.currentTimeMillis()-s);
////		for (int i = 0; i < 4; i++) {
////			ITestServlet servlet=Main.getIns(ITestServlet.class, TestServlet.class.getName());
////			try {
////				servlet.find();
////				Thread.sleep(100);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
////		}
////		System.gc();
////		while(true){
////			Thread.sleep(10000);
////		}
	}

	public static <T> T getIns(Class<T> c,String impl)  {
		HotswapCL cl = new HotswapCL("E:\\workespaceGroup\\KKD_V2\\KKDUtil\\bin\\", new String[]{impl});
		try {
			Object foo;
			Class cls = cl.loadClass(impl); 
			foo = cls.newInstance();
			return (T)foo;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

//	private static void t() throws ClassNotFoundException,
//			InstantiationException, IllegalAccessException {
//		while(true){
//			HotswapCL cl = new HotswapCL("E:\\workespaceGroup\\KKD_V2\\KKDUtil\\bin\\", new String[]{"kkd.common.deploy.Foo"}); 
//			Class cls = cl.loadClass("kkd.common.deploy.Foo"); 
//			IFoo foo = (IFoo)cls.newInstance(); 
//			foo.sayHello(); 
////		        Method m = foo.getClass().getMethod("sayHello", new Class[]{}); 
////		        m.invoke(foo, new Object[]{}); 
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
}
