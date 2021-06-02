package kkd.common.exception;

import kkd.common.entity.Msg;

/**
 * 验证异常
 * 
 * @author Administrator
 * 
 */
public class KKDValidationException extends KKDException {

	private static final long serialVersionUID = 1L;

	public KKDValidationException(String msg, int code) {
		super(msg, code);
	}

	public KKDValidationException(String msg) {
		super(msg, Msg.ERROR_KKDValidation);
	}

}
