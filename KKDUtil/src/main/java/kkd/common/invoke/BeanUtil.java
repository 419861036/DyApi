package kkd.common.invoke;

import kkd.common.deploy.HotswapCL;

/**
 * bean util
 * @author tanbins
 *
 */
public class BeanUtil {
	
	private Boolean change;
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> t,String name){
		String [] s={name};
		HotswapCL cl=new HotswapCL("E:\\workespaceGroup\\KKD_V2\\KKDUtil\\bin",s);
		try {
			Class<?> cls=cl.loadClass(name);
			try {
				return  (T)cls.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
//		IFoo foo=BeanUtil.getBean(IFoo.class, "kkd.common.deploy.Foo");
//		foo.sayHello();
		for (int i = 0; i < 1; i++) {
//			IFoo foo=BeanUtil.getBean(IFoo.class, "kkd.common.deploy.Foo");
//			foo.sayHello();
		}
//		
		while(true){
			try {
				System.gc();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
