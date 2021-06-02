package kkd.common.exception;

import kkd.common.entity.Msg;

/**
 * json异常
 * @author Administrator
 *
 */
public class KKDJsonException extends KKDException{

	private static final long serialVersionUID = 1L;

	public KKDJsonException(String msg) {
		super(msg,Msg.ERROR_KKDJson);
	}

}
