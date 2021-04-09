package application;

import java.lang.reflect.Field;
import java.util.ArrayList;

import application._SpecHandler.Spec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class Controller {
	@FXML
	public Button save;
	String thisType;
	ObservableList<?> data = FXCollections.observableArrayList();
	ArrayList<Field> fields = new ArrayList<Field>();
//	abstract Spec setSpec();
	abstract void makeSpec();  //getAllTextFields.foreach(t->(get(t.name)=t.val));
	abstract Spec getSpec();
	
	@FXML
	void saveSpecHandler() {
		Stage stage = (Stage) save.getScene().getWindow();
		makeSpec();
		stage.close();
	}
	
	final public static void setField(Object targetObject, String fieldName, Object fieldValue) {
		Class<?> cClass = targetObject.getClass(); 
		Field field = null;
		try {
			do {
				field = cClass.getDeclaredField(fieldName);
				cClass = cClass.getSuperclass();
			}while(field == null);
			field.setAccessible(true); //TODO Do we actually need this? Don't like it
			field.set(targetObject, fieldValue);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			System.out.println("Controller.java : Couldn't find fieldname " + fieldName + " from " + targetObject.getClass());
		} catch (IllegalAccessException e) {
			System.out.println("Controller.java : Couldn't set fieldname " + fieldName + " to be accessible");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("Controller.java : Couldn't set fieldname " + fieldName + " = " + fieldValue.toString());
		}
	}
}
