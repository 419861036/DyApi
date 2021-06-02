package kkd.common.proxy.test;

import kkd.common.proxy.test.user.IUserService;
import kkd.common.proxy.test.user.UserService;



public class JavassistTest {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
//        System.out.println("*******************方式一*******************");
//        JavassistProxyFactory<UserService> jpf = new JavassistProxyFactory<UserService>();
//        UserService a = new UserService();
//        jpf.setTarget(a);
//        UserService proxy = jpf.getProxy();
//        proxy.hello();

//        System.out.println("*******************方式二*******************");
        JavassistProxyFactory02 jpf02 = new JavassistProxyFactory02();
        Class<?> cls=jpf02.getProxy(UserService.class);
        Long s=System.currentTimeMillis();
        IUserService a2 = (IUserService) cls.newInstance();
        for (int i = 0; i < 9999999; i++) {
//             int a=a2.hello("",1);
//             System.out.println(a);
		}
        System.out.println(System.currentTimeMillis()-s);
        IUserService a=new UserService();
        s=System.currentTimeMillis();
        for (int i = 0; i < 9999999; i++) {
//            a.hello("",1);
		}
        System.out.println(System.currentTimeMillis()-s);


    }

}