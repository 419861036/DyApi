package kkd.common.thread;

public class Test {
	public Test() {
		Monitor mon=new Monitor();
		Thread t=new Thread(mon,"ThreadPoolMonitor");
		t.start();
	}
	public static void main(String[] args) {
		Test t=new Test();
		try {
			Thread.sleep(6000);
			synchronized (o) {
				o.notify();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static Object o=new Object();
	private class Monitor implements Runnable {

		@Override
		public void run() {
			
			synchronized (o) {
				try {
					System.out.println(2);
					o.wait();
					System.out.println(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		}
	}
}
