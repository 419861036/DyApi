package kkd.common.proxy.test;

import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class JavassistUtil {
	 public static void main(String[] args) {
		 demo();
	 }

	private static void demo() {
		//创建class池
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass("com.dasenlin.bean.Employ");
		//创建属性
		try {
		    CtField f1 = CtField.make("private Integer empId;", cc);
		    CtField f2 = CtField.make("private String empName;",cc);
		    CtField f3 = CtField.make("private Integer empAge;", cc); 

		    cc.addField(f1);
		    cc.addField(f2);
		    cc.addField(f3);

		    CtMethod m1=CtMethod.make("public Integer getEmpId(){return empId;}", cc);
		    CtMethod m2=CtMethod.make("public void setEmpId(Integer empId){this.empId=empId;}", cc);

		    cc.addMethod(m1);
		    cc.addMethod(m2);

//	            CtConstructor constructor =new CtConstructor(new CtClass[]{CtClass.intType,pool.get("java.lang.String")},cc);
//	            constructor.setBody("{this.empId=empId;this.empName=empName;this.empAge=empAge;}");
//	            cc.addConstructor(constructor);
	            cc.writeFile("E:/LearnJavaProjectText/myjava");
		    Class<?> clazz =cc.toClass();
		    Object obj=clazz.newInstance();
		    Method m=clazz.getMethod("setEmpId",Integer.class);
		    m.invoke(obj, 2);
		    Method m3=clazz.getMethod("getEmpId");
		    Object r=m3.invoke(obj);
		    System.out.println(r);
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
