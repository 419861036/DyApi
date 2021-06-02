package kkd.common.timer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import kkd.common.cache.server.ServerThread;
import kkd.common.thread.ServiceThread;

/**
 * 扫描文件变动
 * @author tanbins
 *
 */

public class ScanChange {
	public static final int FILE_CREATED 	= 1;
	public static final int FILE_DELETED 	= 2;
	public static final int FILE_MODIFIED 	= 3;
//	public static final int FILE_RENAMED 	= 4;
	private Map<String,ScanFile> mapFiles=new HashMap<String, ScanFile>();
	private static Scaner scaner=null;
	public interface INotify{
		public void change(Integer state,File f);
	}
	
	public ScanChange() {
		Scaner sc=new Scaner();
		sc.start();
		scaner=sc;
	}
	public class ScanFile{
		private File f;
		private Long oldTime;
		
		public Long getOldTime() {
			return oldTime;
		}

		public void setOldTime(Long oldTime) {
			this.oldTime = oldTime;
		}

		public File getF() {
			return f;
		}

		public void setF(File f) {
			this.f = f;
		}
		
	}
	
	/**
	 * 添加监听文件
	 */
	public void addWatchFile(File file){
		String path=file.getAbsolutePath();
		if(file.exists()){
			Long modTime=file.lastModified();
			ScanFile sf=new ScanFile();
			sf.setF(file);
			sf.setOldTime(modTime);
			mapFiles.put(path, sf);
		}else{
			ScanFile sf=new ScanFile();
			sf.setF(file);
			sf.setOldTime((long)-1);
			mapFiles.put(path, sf);
		}
		
	}
	private Map<INotify,String> notifyMap=new HashMap<ScanChange.INotify, String>();
	
	/**
	 * 通知所有关注着
	 * @param state
	 * @param f
	 */
	private void notifyAllObj(Integer state ,File f){
		for (Map.Entry<INotify,String> entry:notifyMap.entrySet()) {
			try {
				entry.getKey().change(state, f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void addListener(INotify notify){
		notifyMap.put(notify, null);
	}
	
	/**
	 * 添加监听文件
	 */
	public void removeWatchFile(File file){
		mapFiles.remove(file.getAbsolutePath());
	}
	
	
	/**
	 * 扫描器
	 * @author tanbins
	 *
	 */
	public class Scaner extends ServiceThread{
		
		public Scaner() {
		}
		
		@Override
		public void run() {
			while(!isStoped()){
				//防止tomcat reload
//				if(Thread.currentThread()!=scaner){
//					break;
//				}
				for (Map.Entry<String, ScanFile> entry:mapFiles.entrySet()) {
					ScanFile sf=entry.getValue();
					File file=sf.getF();
					String path=file.getAbsolutePath();
					Long oldTime=sf.getOldTime();
					if(!file.exists()){
						if(oldTime==null||oldTime!=null && oldTime!=-1){
							sf.setOldTime((long)-1);
							mapFiles.put(path, sf);
//							System.out.println("delete");
							notifyAllObj(ScanChange.FILE_DELETED, file);
						}
						continue;
					}
					Long modTime=file.lastModified();
					if(mapFiles.get(path)==null){
						sf.setOldTime(modTime);
						mapFiles.put(path, sf);
						notifyAllObj(ScanChange.FILE_CREATED, file);
					}else if(oldTime==-1){
						sf.setOldTime(modTime);
						mapFiles.put(path, sf);
						notifyAllObj(ScanChange.FILE_CREATED, file);
					}else if(!oldTime.equals(modTime)){
						sf.setOldTime(modTime);
						mapFiles.put(path, sf);
						notifyAllObj(ScanChange.FILE_MODIFIED, file);
					}
				}
					
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public String getServiceName() {
			return "ScanChange";
		}
	}
	private static ScanChange sc=null;
	
	public static ScanChange getInstance(){
		if(sc==null){
			sc=new ScanChange();
		}
		return sc;
	}

	public static void main(String[] args) {
		File file=new File("E:/test/a1.txt");
		System.out.println(file.exists());
		ScanChange.getInstance().addWatchFile(file);
		ScanChange.getInstance().addListener(new INotify() {
			
			@Override
			public void change(Integer state, File f) {
				if(ScanChange.FILE_CREATED==state){
					System.out.println("create filename="+f.getName());
				}
				
			}
		});
	}
}
