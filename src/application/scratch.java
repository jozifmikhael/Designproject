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
		String moduleString = Arrays.toString(test);
		moduleString = moduleString.replace("[", "");
		moduleString = moduleString.replace("]", "");
		moduleString = moduleString.replace(this.getClass().toString().replace("class ", "")+".", "");
		moduleString = moduleString.replace("public ", "");
		moduleString = moduleString.replace("java.lang.", "");
		String[] tokens = moduleString.split(", ");
		
		for (int i = 0; i <tokens.length;i++) {
			String[] tokens2 = tokens[i].split(" ");
			if (tokens2[0].equals("int"))
				System.out.println(tokens2[0] + " " + tokens2[1] + " = " + "Integer.parseUnsignedInt(device.get(" + tokens2[1] + ").toString());");		
		}
		
		return moduleString;
	}
	public static void main(String[] args) {
		scratch testobj = new scratch(6, 7, "whatever");	
		System.out.println(testobj.toString());
	}
}
