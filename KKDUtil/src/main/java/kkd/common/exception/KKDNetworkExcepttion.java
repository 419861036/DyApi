package kkd.common.exception;

import kkd.common.entity.Msg;

/**
 * 网络异常
 * @author Administrator
 *
 */
public class KKDNetworkExcepttion extends KKDException{
	private static final long serialVersionUID = 1L;
	
	public KKDNetworkExcepttion(String msg) {
		super(msg,Msg.ERROR_KKDNetwork);
	}
	public KKDNetworkExcepttion(Throwable cause,String msg) {
		super(msg,cause,Msg.ERROR_KKDNetwork);
	}

}
