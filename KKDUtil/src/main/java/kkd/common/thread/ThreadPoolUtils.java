package kkd.common.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {
	
	private static BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(20);  
	private static ThreadPoolExecutor blockThreadPool = new ThreadPoolExecutor(3, 10, 1, TimeUnit.HOURS, queue, new ThreadPoolExecutor.CallerRunsPolicy());
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
	private static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);
	
	public static void blockExecute(Runnable command){
		blockThreadPool.execute(command);
	}
	
	public static void execute(Runnable command){
		fixedThreadPool.execute(command);
	}
	
	public static void scheduleAtFixedRate(Runnable command,Integer seconds){
		scheduledThreadPool.scheduleAtFixedRate(command, 1, seconds, TimeUnit.SECONDS);
	}
	
	
	
	public static void shutdown(){
		if(fixedThreadPool!=null){
			fixedThreadPool.shutdown();
		}
		if(scheduledThreadPool!=null){
			scheduledThreadPool.shutdown();
			try {
				if(!scheduledThreadPool.awaitTermination(1000, TimeUnit.MILLISECONDS)){  
					scheduledThreadPool.shutdownNow();  
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  
		}
		if(blockThreadPool!=null){
			blockThreadPool.shutdown();
		}
	}
}
