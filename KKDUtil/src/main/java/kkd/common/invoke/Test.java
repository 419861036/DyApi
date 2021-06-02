package kkd.common.invoke;

public class Test {
	public static void main(String[] args) {
		MethodAccess ma=MethodAccess.get(Test.class);
		Object ins=ma.newInstance();
		ma.invokeByPath(ins, "/say1","11");
//		Object[] objs=new Object[0];
//		ma.invoke(ins, 1); 
//		ma.invoke(ins, "say1","11");
	}
	@MethodPath("say")
	public  void say() {
		System.out.println("1");
	}
	
	@MethodPath("/say1")
	public  void say(String s) {
		System.out.println("2");
	}
	
	
}
