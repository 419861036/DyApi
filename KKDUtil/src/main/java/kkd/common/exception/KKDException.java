package kkd.common.exception;


/**
 * 根异常
 * @author Administrator
 *
 */
public  class KKDException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	protected int code;
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public KKDException(Throwable cause) {
		super(cause);
	}
	public KKDException(Throwable cause,int code) {
		super(cause);
	}
	
	public KKDException(String msg) {
		super(msg);
	}

	public KKDException(String msg,int code) {
		super(msg);
		this.code = code;
	}

	public KKDException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public KKDException(String msg, Throwable cause,int code) {
		super(msg, cause);
		this.code = code;
	}
	/**
	 * 得到根异常
	 * @return
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}
	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return (rootCause != null ? rootCause : this);
	}
	public boolean contains(Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof KKDException) {
			return ((KKDException) cause).contains(exType);
		}
		else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}
//	public Throwable fillInStackTrace() {
//		return this;
//	}
}




