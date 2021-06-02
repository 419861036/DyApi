package kkd.common.proxy.test;

import kkd.common.proxy.javassist.Invoker;
import kkd.common.proxy.test.user.IUserService;

public class Proxy extends Tmp implements IUserService, Invoker {

	@Override
	public Object invokeSuper(int key, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object newInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hello() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
