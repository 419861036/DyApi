package kkd.common.proxy.test;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

/*
 * 这种方式不需要事先创建
 * 要代理的对象
 * 
 * */
public class JavassistProxyFactory02 {

    @SuppressWarnings("deprecation")
    public Class<?> getProxy(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(clazz);
        proxyFactory.setUseCache(true);
        proxyFactory.setFilter(new MethodFilter() {
			
			@Override
			public boolean isHandled(Method arg0) {
				return true;
			}
		});
        proxyFactory.setHandler(new MethodHandler() {
            public Object invoke(Object self, Method thismethod, Method proceed, Object[] args) throws Throwable {
//            	System.out.println("1");
                Object result = proceed.invoke(self, args);
                return result;
            }
        });

        // 通过字节码技术动态创建子类实例
        return  proxyFactory.createClass();
    }

}