package kkd.common.proxy.javassist;
/**
 * 基础类型枚举
 * @author tanbin
 *
 */
public enum TypeDef {
	byteEnum("java.lang","byte","byte","Byte",".byteValue()",true),
	byteArrEnum("java.lang","byte[]","[B","Byte[]","",false),
	booleanEnum("java.lang","boolean","boolean","Boolean",".booleanValue()",true),
	booleanArrEnum("java.lang","boolean[]","[Z","Boolean[]","",false),
	shortEnum("java.lang","short","short","Short",".shortValue()",true),
	shortArrEnum("java.lang","short[]","[S","Short[]","",false),
	charEnum("java.lang","char","char","Character",".charValue()",true),
	charArrEnum("java.lang","char[]","[C","Character[]","",false),
	intEnum("java.lang","int","int","Integer",".intValue()",true),
	intArrEnum("java.lang","int[]","[I","Integer[]","",false),
	longEnum("java.lang","long","long","Long",".longValue()",true),
	longArrEnum("java.lang","long[]","[J","Long[]","",false),
	floatEnum("java.lang","float","float","Float",".floatValue()",true),
	floatArrEnum("java.lang","float[]","[F","Float[]","",false),
	doubleEnum("java.lang","double","double","Double",".doubleValue()",false),
	doubleArrEnum("java.lang","double[]","[D","Double[]","",true),
	;
	
	private String packageName;
	private String simpleName;
	private String objName;
	private String simpleVal;
	private String name;
	private boolean arr;
	
	
	private TypeDef(String packageName,String simpleName,String name,String objName,String simpleVal,boolean arr){
		this.packageName=packageName;
		this.simpleName=simpleName;
		this.objName=objName;
		this.simpleVal=simpleVal;
		this.name=name;
		this.arr=arr;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public String getObjName() {
		return objName;
	}

	public String getSimpleVal() {
		return simpleVal;
	}

	public String getName() {
		return name;
	}

	public boolean isArr() {
		return arr;
	}
	
	
	
}
