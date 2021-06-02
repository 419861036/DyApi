package kkd.common.exception;

import kkd.common.entity.Msg;

/**
 * 数据库异常
 * @author Administrator
 *
 */
public class KKDDbException extends KKDException{
	private static final long serialVersionUID = 1L;
	
	public KKDDbException(String msg) {
		super(msg,Msg.ERROR_KKDDb);
	}
	public KKDDbException(String msg,Throwable cause) {
		super(msg,cause);
		this.code = Msg.ERROR_KKDDb;
	}

}
