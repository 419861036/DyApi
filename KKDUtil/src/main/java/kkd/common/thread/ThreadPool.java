package kkd.common.thread;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import kkd.common.util.EnvUtil;

/**
 * 线程池 创建线程池，销毁线程池，添加新任务
 * 
 */
public final class ThreadPool {
	/* 单例 */
	private static  ThreadPool instance = ThreadPool.getInstance();
	/* 池中的所有线程 */
	public List<PoolWorker> workers; 
	private String threadPoolName;
	private Queue<Task> taskQueue ;
	private Integer size=0;
	private Integer maxSize=50000;
	private Integer maxPoolSize=60;//最大线程数
	private Integer minPoolSize;//最小线程数
	private AtomicLong curSuccessCount=new AtomicLong();
	private AtomicLong curErrCount=new AtomicLong();
	private AtomicLong lastSuccessCount=new AtomicLong();
	private AtomicLong lastErrCount=new AtomicLong();
	private Double errRate;
	private	Long curDate;
	private boolean init=false;//是否初始化
	private Monitor mon;
	public static synchronized ThreadPool getInstance() {
		return getInstance(null);
	}
	
	public static synchronized ThreadPool getInstance(String threadPoolName) {
		if (instance == null){
			instance = new ThreadPool();
		}
		return instance;
	}
	
	

	private ThreadPool() {
		
	}
	
	public void init(Integer workThread,String threadPoolName1){
		if(init){
			return;
		}
		if(workThread==null){
			Runtime r = Runtime.getRuntime();  
			workThread=r.availableProcessors()*1;
		}
		taskQueue= new ConcurrentLinkedQueue<Task>();
		workers = new LinkedList<ThreadPool.PoolWorker>();
		minPoolSize=workThread;
		if(threadPoolName1==null){
			String projectName=EnvUtil.get(EnvUtil.projectName);
			threadPoolName=projectName==null?"Default":projectName;
		}else{
			threadPoolName=threadPoolName1;
		}
		for (int i = 0; i < workThread; i++) {
			PoolWorker w=new PoolWorker(threadPoolName);
			workers.add(w);
			w.start();
		}
		mon=new Monitor();
		mon.start();
		init=true;
	}

	/**
	 * 增加新的任务 每增加一个新任务，都要唤醒任务队列
	 * 
	 * @param newTask
	 */
	public ThreadPool addTask(Task newTask) {
		synchronized (taskQueue) {
			init(null,null);
			if(size>=maxSize){
				return this;
			}
			size++;
			taskQueue.add(newTask);
			/* 唤醒队列, 开始执行 */
			taskQueue.notifyAll();
		}
		return this;
	}

	/**
	 * 增加新的任务 每增加一个新任务，都要唤醒任务队列
	 * 
	 * @param newTask
	 */
	public boolean addTaskV1(Task newTask) {
		synchronized (taskQueue) {
			init(null,null);
			if(size>=maxSize){
				return false;
			}
			size++;
			taskQueue.add(newTask);
			/* 唤醒队列, 开始执行 */
			taskQueue.notifyAll();
		}
		return true;
	}
	
	/**
	 * 批量增加新任务
	 * 
	 * @param taskes
	 */
	public void batchAddTask(Task[] taskes) {
		if (taskes == null || taskes.length == 0) {
			return;
		}
		synchronized (taskQueue) {
			init(null,null);
			if(size>=maxSize){
				return ;
			}
			for (int i = 0; i < taskes.length; i++) {
				if (taskes[i] == null) {
					continue;
				}
				size++;
				taskQueue.add(taskes[i]);
			}
			/* 唤醒队列, 开始执行 */
			taskQueue.notifyAll();
		}
	}
	
	/**
	 * 批量增加新任务
	 * 
	 * @param taskes
	 */
	public boolean batchAddTaskV1(Task[] taskes) {
		if (taskes == null || taskes.length == 0) {
			return false;
		}
		synchronized (taskQueue) {
			init(null,null);
			if(size+taskes.length>=maxSize){
				return false;
			}
			for (int i = 0; i < taskes.length; i++) {
				if (taskes[i] == null) {
					continue;
				}
				size++;
				taskQueue.add(taskes[i]);
			}
			/* 唤醒队列, 开始执行 */
			taskQueue.notifyAll();
		}
		return true;
	}
	
	

	/**
	 * 销毁线程池
	 */
	public synchronized void destroy() {
		synchronized (taskQueue) {
			if (workers != null) { 
				for (int i = 0; i < workers.size(); i++) {
					workers.get(i).stopWorker();
					workers.remove(i);
				}
			}
			taskQueue.clear();
			mon.stop(true);
			//			taskQueue.notify();
		}

	}

	/**
	 * 池中工作线程
	 * 
	 */
	private static  AtomicLong tid=new AtomicLong();
	private class PoolWorker extends ServiceThread {
		/* 该工作线程是否可以执行新任务 */
		private boolean isWaiting = true;
		private long startTime=0;
		private Task task = null;
		private long myTid;
		private String  threadPoolName;
		public PoolWorker(String threadPoolName1) {
			myTid=tid.getAndIncrement();
			threadPoolName=threadPoolName1;
		}
		
		@Override
		public String getServiceName() {
			threadPoolName=threadPoolName==null?"Default":threadPoolName;
			return threadPoolName+"-ThreadPool-"+myTid;
		}

		public void stopWorker() {
			this.stop(true);
		}

		@SuppressWarnings("unused")
		public boolean isWaiting() {
			return this.isWaiting;
		}
		public void interrupted(){
			Thread.currentThread().interrupt();
		}

		/**
		 * 循环执行任务 这也许是线程池的关键所在
		 */
		public void run() {
			while (!isStoped()) {
				startTime=System.currentTimeMillis();
				try {
					//					if(taskQueue==null){
					//					taskQueue= new ConcurrentLinkedQueue<Task>();
					//				}
					while (taskQueue!=null&&taskQueue.isEmpty()) {
						try {
							synchronized (taskQueue) {
								/* 任务队列为空，则等待有新任务加入从而被唤醒 */
								//								taskQueue.wait(3000);
								taskQueue.wait();
							}
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					}
					if(taskQueue!=null){
						synchronized (taskQueue) {
							/* 取出任务执行 */
							task = (Task) taskQueue.poll();
							if(size>0){
								size--;
							}
						}
					}

					if (task != null) {
						isWaiting = false;
						try {
							task.execute();
							boolean success=task.getSuccess();
							int sleepTime=countErrRate(success);
							if(sleepTime!=0){
								Thread.sleep(sleepTime);
							}
//							System.out.println("耗时："+(System.currentTimeMillis()-startTime));
						} catch (Exception e) {
							curErrCount.getAndIncrement();
							e.printStackTrace();
						}
						isWaiting = true;
						task = null;
					}
				} catch (Exception e) {
				}finally{

				}
//				System.out.println("耗时："+(System.currentTimeMillis()-startTime));

			}
		}
		
		/**
		 * 根据错误率决定睡眠时间
		 * @param success
		 * @return
		 * @throws InterruptedException
		 */
		private int countErrRate(boolean success) throws InterruptedException {
			synchronized (taskQueue) {
				if(success){
					curSuccessCount.getAndIncrement();
				}else{
					curErrCount.getAndIncrement();
				}
				Long tmpDate=System.currentTimeMillis()/1000/15;
				if(curDate==null){
					curDate=tmpDate;
				}else if(curDate!=null&&!curDate.equals(tmpDate)){
					curDate=tmpDate;
					lastErrCount=curErrCount;
					lastSuccessCount=curSuccessCount;
					errRate=lastErrCount.doubleValue()/lastSuccessCount.doubleValue();
					System.out.println("QueueSize:"+size);
					System.out.println("ThreadSize:"+workers.size());
					System.out.println("lastErrCount:"+lastErrCount);
					System.out.println("lastSuccessCount:"+lastSuccessCount);
					System.out.println("last Err Rate:"+errRate.doubleValue());
					curErrCount.set(0);
					curSuccessCount.set(0);
				}
				if(errRate!=null){
					if(errRate>0.60){
						return 200;
					}else if(errRate>0.50){
						return 100;
					}else if(errRate>0.40){
						return 50;
					}else if(errRate>0.30){
						return 20;
					}else if(errRate>0.20){
						return 10;
					}else if(errRate>0.10){
						return 5;
					}else if(errRate>0.05){
						return 3;
					}else if(errRate>0.005){
						return 1;
					}else{
						return 0;
					}
				}else{
					return 5;
				}
			}
			
		}

		public long getStartTime() {
			return startTime;
		}

		@SuppressWarnings("unused")
		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		

	}
	/**
	 * 监视器 处理线程中的超长等待任务
	 * 智能操作线程数量
	 * @author 
	 *
	 */
	private class Monitor extends ServiceThread {
		private long time=System.currentTimeMillis();
		
		@Override
		public String getServiceName() {
			return threadPoolName+"-Monitor";
		}
		
		@Override
		public void run() {
//			while (workers.size()>0) {
			while (!isStoped()) {
				try { 
					boolean hasTask=false;
					if(workers.size()<maxPoolSize){
						int doNum=maxSize/(maxPoolSize-minPoolSize);
						int poolSize=size/doNum;
						if(poolSize>5){
							//相差30秒 再检查是否需要添加线程
							if(System.currentTimeMillis()-time>1000*30){
								time=System.currentTimeMillis();
								for (int j = 0;poolSize<=maxPoolSize && j < poolSize; j++) {
									
									PoolWorker w=new PoolWorker(threadPoolName);
									workers.add(w);
									w.start();
//									Thread t=new Thread(w,threadPoolName+"-ThreadPool-"+w.getMyTid());
//									t.setDaemon(true);
//									t.start();
//									System.out.println("poolSize:"+poolSize);
//									System.out.println("add Thread");
								}
							}
							
						}else if(size==0&&workers.size()>minPoolSize){
							workers.remove(workers.size()-1).stopWorker();
							System.out.println("remove Thread");
						}
						
					}
					
					for (int i = 0; i < workers.size(); i++) {
						PoolWorker pw=workers.get(i);
						
						
						if(pw.task!=null){
							hasTask=true;
							long curTime=System.currentTimeMillis();
							if(curTime-pw.getStartTime()>3000){
								pw.interrupted();
							}
						}
						
					}
					try {
						Thread.sleep(1000);
//						synchronized (taskQueue) {
//							if (taskQueue!=null&&taskQueue.isEmpty()) {
//								/* 任务队列为空，则等待有新任务加入从而被唤醒 */
//								//							if(!hasTask){
//								//								taskQueue.wait(3000);
//								//							}else{
//								//								Thread.sleep(100);
//								//							}
//								taskQueue.wait();
//							}else{
//								try {
//									taskQueue.wait(100);
//								} catch (InterruptedException e) {
//									//									e.printStackTrace();
//								}
//							}
//						}
					} catch (Exception ie) {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		

	}

	public  int getSize(){
		return size;
	}


	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public boolean contains(Object o) {
		if(taskQueue!=null){
			return taskQueue.contains(o);
		}
		return false;
	}
	
	
	public String getThreadPoolName() {
		return threadPoolName;
	}



	public void setThreadPoolName(String threadPoolName) {
		this.threadPoolName = threadPoolName;
	}


	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public static void main(String[] args) throws InterruptedException {
		
		while(true){
			PostTask p=new PostTask("http://www.sina.cn",null ,null);
			ThreadPool.getInstance().addTaskV1(p);
//			Thread.sleep(1);
		}


		//		Thread.sleep(10000);
		//		ThreadPool.getInstance().destroy();
	}
	
	
}