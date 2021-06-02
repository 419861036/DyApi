package kkd.common.proxy.test;

import kkd.common.proxy.javassist.Invoker;
import kkd.common.proxy.javassist.MethodFilter;
import kkd.common.proxy.javassist.MethodInfo;

public class RpcMethodFilter implements MethodFilter{
	private MethodInfo mi;
	@Override
	public void setMethodInfo(MethodInfo mi) {
		this.mi=mi;
	}

	@Override
	public Object invoke(Invoker invoker, int key, Object[] args) {
		System.out.println(mi.getCurrentClassMethod()[key].getName());
		return 2;
	}

}
