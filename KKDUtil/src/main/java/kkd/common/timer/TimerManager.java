package kkd.common.timer;

import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
	
	private static Timer timer=new Timer();
	
	private TimerManager(){}
	
	public static void cancel(){
		timer.cancel();
	}
	public static void schedule(TimerTask task,long delay,long period){
		timer.schedule(task, delay, period);
	}
	
}
