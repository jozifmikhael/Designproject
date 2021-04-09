package application;

import java.lang.reflect.Field;
import java.util.ArrayList;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.NodeSpec;
import application._SpecHandler.Spec;
import application._SpecHandler.TupleSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import static application.scratch.printDebug;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;


public abstract class Controller {
	@FXML
	public Button saveButton;
	@FXML
    private AnchorPane ap;
	Spec spec;
	String thisType;
	
	//getAllTextFields.foreach(t->(get(t.name)=t.val));
	void makeSpec() {
		printDebug(spec.toString());
		for(int i=0; i<ap.getChildren().size(); i++) {
			Node selTable = ap.getChildren().get(i);
			if(selTable instanceof TableView) {
                TableColumn selCols = (TableColumn) (((TableView) selTable).getColumns().get(0));
//                setField
//                Object selectedItem = tupleTable.getSelectionModel().getSelectedItem()
                printDebug(selTable.getId() + " " + selCols.getCellObservableValue(spec).toString());
                
//                selCols.setCellValueFactory(new PropertyValueFactory <>(selCols.getId()));
//                selCols.setCellFactory(TextFieldTableCell.forTableColumn());
            }
		}
	}
	
	@FXML
	Spec saveSpecHandler() {
		ap.getChildren().stream().forEach(c->printDebug(c.getId()));
		((Stage) saveButton.getScene().getWindow()).close();
		return this.spec;
	}
	
	final public static void setField(Object targetObject, String fieldName, Object fieldValue) {
		Class<?> cClass = targetObject.getClass(); 
		Field field = null;
		try {
			do {
				printDebug("Trying to get field " + fieldName + " in class " + cClass.getName());
				field = cClass.getField(fieldName);
				cClass = cClass.getSuperclass();
			}while(field == null);
			field.setAccessible(true); //TODO Do we actually need this? Don't like it
			switch(field.getType().toString()) {
//				case "long": field.set(targetObject, Long.parseLong(fieldValue.toString())); break;
//				case "int": field.set(targetObject, Integer.parseInt(fieldValue.toString())); break;
//				case "double": field.set(targetObject, Double.parseDouble(fieldValue.toString())); break;
				default: field.set(targetObject, fieldValue);
			}
			
			
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
	
	public void editHandler(CellEditEvent<Spec, ?> t) {
		System.out.println("In Table " + t.getTableView().getId() + " : " + t.getOldValue() + " ->" + t.getNewValue());
		setField(spec, t.getTableView().getId(), t.getNewValue());
		t.getTableView().refresh();
	}
	
	final public void init() {
		ObservableList<Spec> tempData = FXCollections.observableArrayList();
		tempData.add(spec);
		
		for(int i=0; i<ap.getChildren().size(); i++) {
			Node selTable = ap.getChildren().get(i);
			if(selTable instanceof TableView) {
            	((TableView) selTable).setItems(tempData);
            	((TableView) selTable).setEditable(true);
                TableColumn selCol = (TableColumn) (((TableView) selTable).getColumns().get(0));
                selCol.setCellValueFactory(new PropertyValueFactory <>(selTable.getId()));
                selCol.setCellFactory(TextFieldTableCell.forTableColumn());
                selCol.setOnEditCommit(t -> editHandler((CellEditEvent<Spec, String>)t));
            }
		}
	}
}
