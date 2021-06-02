package kkd.common.util;

import java.io.PrintStream;
import java.util.Locale;

public class MyPrintStream extends PrintStream{

	public static void main(String[] args) {
		MyPrintStream.proxyPrintStream();
		System.err.print("ss");
	}
	
	public static void proxyPrintStream(){
//		PrintStream ps=System.out;
//		MyPrintStream my=new MyPrintStream(ps);
//		PrintStream erPs=System.err;
//		MyPrintStream erMy=new MyPrintStream(erPs);
//		System.setErr(erMy);
//		System.setOut(my);
	}
	public MyPrintStream(PrintStream ps)  {
		super(ps);
	}
	public void customPrint(){
		String projectName=EnvUtil.get(EnvUtil.projectName);
		super.print("STDOUT ["+projectName+"]");
	}
	
	public void print(boolean b) {
		customPrint();
		super.print(b);
	}

	public void print(char c) {
		customPrint();
		super.print(c);
	}

	public void print(int i) {
		customPrint();
		super.print(i);
	}

	public void print(long l) {
		customPrint();
		super.print(l);
	}

	public void print(float f) {
		customPrint();
		super.print(f);
	}

	public void print(double d) {
		customPrint();
		super.print(d);
	}

	public void print(char s[]) {
		customPrint();
		super.print(s);
	}

	public void print(String s) {
		customPrint();
		super.print(s);
	}

	public void print(Object obj) {
		customPrint();
		super.print(obj);
	}

//	public void println() {
//		customPrint();
//		super.println();
//	}
//
//
//	public void println(boolean x) {
//		customPrint();
//		super.println(x);
//	}
//
//	public void println(char x) {
//		customPrint();
//		super.println(x);
//	}
//
//	public void println(int x) {
//		customPrint();
//		super.println(x);
//	}
//
//	public void println(long x) {
//		customPrint();
//		super.println(x);
//	}
//
//	public void println(float x) {
//		customPrint();
//		super.println(x);
//	}
//
//	public void println(double x) {
//		customPrint();
//		super.println(x);
//	}
//
//	public void println(char x[]) {
//		customPrint();
//		super.println(x);
//	}
//
//	public void println(String x) {
//		customPrint();
//		super.println(x);
//	}
//
//	public void println(Object x) {
//		customPrint();
//		super.println(x);
//	}


	public PrintStream printf(String format, Object ... args) {
		customPrint();
		return super.printf(format, args);
	}

	public PrintStream printf(Locale l, String format, Object ... args) {
		customPrint();
		return super.format(l, format, args);
	}

}
