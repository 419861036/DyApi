package kkd.common.proxy.javassist;

public class Types {

	public static TypeDef getTypeByName(String simple) {
		switch (simple) {
		case "[B":
			return TypeDef.byteArrEnum;
		case "byte":
			return TypeDef.byteEnum;
		case "[Z":
			return TypeDef.booleanArrEnum;
		case "boolean":
			return TypeDef.booleanEnum;
		case "[S":
			return TypeDef.shortArrEnum;
		case "short":
			return TypeDef.shortEnum;
		case "[C":
			return TypeDef.charArrEnum;
		case "char":
			return TypeDef.charEnum;
		case "[I":
			return TypeDef.intArrEnum;
		case "int":
			return TypeDef.intEnum;
		case "[J":
			return TypeDef.longArrEnum;
		case "long":
			return TypeDef.longEnum;
		case "[F":
			return TypeDef.floatArrEnum;
		case "float":
			return TypeDef.floatEnum;
		case "[D":
			return TypeDef.doubleArrEnum;
		case "double":
			return TypeDef.doubleEnum;
		default:
			return null;
		}
	}
	
	public static TypeDef getTypeByCanonicalName(String canonicalName) {
		switch (canonicalName) {
		case "byte[]":
			return TypeDef.byteArrEnum;
		case "byte":
			return TypeDef.byteEnum;
		case "boolean[]":
			return TypeDef.booleanArrEnum;
		case "boolean":
			return TypeDef.booleanEnum;
		case "short[]":
			return TypeDef.shortArrEnum;
		case "short":
			return TypeDef.shortEnum;
		case "char[]":
			return TypeDef.charArrEnum;
		case "char":
			return TypeDef.charEnum;
		case "int[]":
			return TypeDef.intArrEnum;
		case "int":
			return TypeDef.intEnum;
		case "long[]":
			return TypeDef.longArrEnum;
		case "long":
			return TypeDef.longEnum;
		case "float[]":
			return TypeDef.floatArrEnum;
		case "float":
			return TypeDef.floatEnum;
		case "double[]":
			return TypeDef.doubleArrEnum;
		case "double":
			return TypeDef.doubleEnum;
		default:
			return null;
		}
	}
	
	
	public static void main(String[] args) {
	}
}
