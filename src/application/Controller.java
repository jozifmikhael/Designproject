package application;

import java.lang.reflect.Field;
import java.util.ArrayList;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.NodeSpec;
import application._SpecHandler.Spec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import static application.scratch.printDebug;
import javafx.stage.Stage;

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
	
	final public void init() {
		ObservableList<Spec> tempData = FXCollections.observableArrayList();
		tempData.add(spec);
		
		for(int i=0; i<ap.getChildren().size(); i++) {
			Node selChild = ap.getChildren().get(i);
			if(selChild instanceof TableView) {
            	((TableView) selChild).setItems(tempData);
            	((TableView) selChild).setEditable(true);
                TableColumn selCols = (TableColumn) (((TableView) selChild).getColumns().get(0));
                selCols.setCellValueFactory(new PropertyValueFactory <>(selChild.getId()));
                selCols.setCellFactory(TextFieldTableCell.forTableColumn());
            }
		}
	}
}
