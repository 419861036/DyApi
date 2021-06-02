package kkd.common.util;

public enum EBooleanCode {
	
	falseCode(0),
	trueCode(1);
	int value;
	private EBooleanCode(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}

}
