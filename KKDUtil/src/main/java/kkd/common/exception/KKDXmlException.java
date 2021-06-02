package kkd.common.exception;

import kkd.common.entity.Msg;

/**
 * xml异常
 * @author Administrator
 *
 */
public class KKDXmlException extends KKDException{

	private static final long serialVersionUID = 1L;

	public KKDXmlException(String msg) {
		super(msg,Msg.ERROR_KKDXml);
	}

}
