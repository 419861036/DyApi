package kkd.common.thread;

/**
 * 服务线程基类
 * @author tanbin
 *
 */
public abstract class ServiceThread implements Runnable{
	
	 // 执行线程
    protected final Thread thread;
    // 线程是否已经停止
    protected volatile boolean stoped = false;
    protected static volatile boolean stopedAll = false;
    // 是否已经被Notify过
    protected volatile boolean hasNotified = false;
    
    public ServiceThread() {
        this.thread = new Thread(this, this.getServiceName()); 
        this.thread.setDaemon(true);
    }


    public abstract String getServiceName();
	
    public void start() {
        this.thread.start();
    }
    
    public void stop() {
        this.stop(false);
    }
    
    public void stop(final boolean interrupt) {
        this.stoped = true;
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }

        if (interrupt) {
            this.thread.interrupt();
        }
    }
    
    public static void stopAll() {
    	stopedAll=true;
    }
    
    public boolean isStoped() {
    	if(stopedAll||stoped){
    		return true;
    	}
        return stoped;
    }
    
    public void wakeup() {
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }
    }
    
    
    protected void waitForRunning(long interval) {
        synchronized (this) {
            if (this.hasNotified) {
                this.hasNotified = false;
                this.onWaitEnd();
                return;
            }

            try {
                this.wait(interval);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                this.hasNotified = false;
                this.onWaitEnd();
            }
        }
    }

    protected void onWaitEnd() {
    }

}
