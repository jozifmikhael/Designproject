package application;

import java.lang.reflect.Field;
import java.util.Arrays;

public class scratch {
	public int intVar=5;
	public long longVar=5;
	public String stringVar="test";
	Field[] test = scratch.class.getFields();
	public scratch(int _intVar, long _longVar, String _stringVar){
		this.intVar=_intVar;
		this.longVar=_longVar;
		this.stringVar=_stringVar;
		this.test = scratch.class.getFields();
	}
	@Override
	public String toString() {
		return "test=" + Arrays.toString(test);
	}
//	public static void main(String[] args) {
//		scratch testobj = new scratch(6, 7, "whatever");
//		System.out.println(testobj.getClass());
//	}
//	
	public static void printDebug(String msg){
		StackTraceElement callerSE = Thread.currentThread().getStackTrace()[2];
		System.out.println("("+ callerSE.getFileName() + ":" + callerSE.getLineNumber() + ") : " + msg);
	}
}
