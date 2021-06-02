package kkd.common.test;

import java.util.Stack;

public class T {
	public static String baseString(int num,int base){
		if(base > 100){
			throw new RuntimeException("进制数超出范围，base<=16");
		}
		StringBuffer str = new StringBuffer("");
		String digths = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Stack<Character> s = new Stack<Character>();
		while(num != 0){
			s.push(digths.charAt(num%base));
			num/=base;
		}
		while(!s.isEmpty()){
			str.append(s.pop());
		}
		return str.toString();
	}
	public static void main(String[] args) {
		String bb=T.baseString(1984833415, 62);
		System.out.println(bb);
	}
}
