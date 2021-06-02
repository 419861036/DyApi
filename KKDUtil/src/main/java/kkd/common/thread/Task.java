package kkd.common.thread;

public abstract class  Task {
	protected Boolean success;
	public abstract void execute();
	
	public boolean getSuccess() {
		if(success==null){
			return true;
		}
		return success;
	}

	
}
