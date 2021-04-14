package application;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import application._SpecHandler.*;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.NodeSpec;
import application._SpecHandler.Spec;
import application._SpecHandler.TupleSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

public abstract class _SubController {
	@FXML
	public Button saveButton;
	@FXML
    public AnchorPane ap;
	Spec spec;
	Spec specPrev;
	String thisType;
	
	
	final public static Field getField(Object targetObject, String fieldName) {
//		Field[] cFields = targetObject.getClass().getDeclaredFields();
//        for(int i=0; i<cFields.length; i++) printDebug(cFields[i].getGenericType().toString() + " " + cFields[i].getName());
		Class<?> cClass = targetObject.getClass(); 
		Field field = null;
		try {
			do {
				field = cClass.getField(fieldName);
				cClass = cClass.getSuperclass();
			}while(field == null);
			return field;
		} catch (NoSuchFieldException e) {
			printDebug("Controller.java : Couldn't find fieldname " + fieldName + " from " + targetObject.getClass());
			return null;
		}
	}
	
	final public static void setField(Object targetObject, String fieldName, Object fieldValue) {
		try {
			Field field = getField(targetObject, fieldName);
			if(field == null) return;
			switch(field.getType().toString()) {
				case "class java.lang.String": field.set(targetObject, fieldValue); break;
				case "long": field.set(targetObject, Long.parseLong(fieldValue.toString())); break;
            	case "int": field.set(targetObject, Integer.parseInt(fieldValue.toString())); break;
				case "double": field.set(targetObject, Double.parseDouble(fieldValue.toString())); break;
				default: printDebug("Unhandled type " + field.getType().toString() + " in setField for : " + fieldName);
			}
		} catch (IllegalAccessException e) {
			System.out.println("Controller.java : Couldn't set fieldname " + fieldName + " to be accessible");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("Controller.java : Couldn't set fieldname " + fieldName + " = " + fieldValue.toString());
		}
	}
	public void cellEditHandler(CellEditEvent<?, ?> t) {
        printDebug("In Table " + t.getTableView().getId() + " : " + t.getOldValue() + " -> " + t.getNewValue()); //t.getNewValue()
		String colID=t.getTableColumn().getId();
		printDebug(colID);
		setField(t.getTableView().getItems().get(t.getTablePosition().getRow()), colID, t.getNewValue());
		t.getTableView().refresh();
	}
	
	List<String> containers = Arrays.asList("Accordion","AnchorPane","BorderPane","ButtonBar","DialogPane",
            "FlowPane", "GridPane", "HBox", "Pane", "ScrollPane", "SplitPane","StackPane","Tab","TabPane",
            "TextFlow","TilePane", "TitledPane","ToolBar","VBox");
	final public void parseChildrenOf(Node cRoot) {
		for(int i=0; i<((Parent) cRoot).getChildrenUnmodifiable().size(); i++) {
			Node selChild = ((Parent) cRoot).getChildrenUnmodifiable().get(i);
			
			if (containers.contains(selChild.getClass().getSimpleName().toString())) parseChildrenOf(selChild);
			else if (selChild.getClass().getSimpleName().toString().equals("TableView")) {
				TableView selTable = (TableView)selChild;
//				selTable.setItems(tempData);
				selTable.setEditable(true);
				int nCol = selTable.getColumns().size();
				for(int j = 0; j<nCol; j++) {
					printDebug("Table ID " + selTable.getId());
					TableColumn selCol = (TableColumn) (selTable.getColumns().get(j));
	            	printDebug("Col ID " + selCol.getId());
					selCol.setCellValueFactory(new PropertyValueFactory <>(selCol.getId()));
					switch(getField(selTable.getItems().get(0), selCol.getId()).getType().toString()) {
						case "class java.lang.String": selCol.setCellFactory(TextFieldTableCell.forTableColumn()); break;
	                	case "long": selCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter())); break;
	                	case "int": selCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter())); break;
						case "double": selCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter())); break;
	                	default: printDebug("Unhandled type in cellFactory");
	                }
					selCol.setOnEditCommit(t -> cellEditHandler((CellEditEvent<?, ?>)t));
				}
			}
			else if (selChild.getClass().getSimpleName().toString().equals("TextField")) {
//				printDebug("Found a textField with ID " + selChild.getId());
            	TextField selTextField = (TextField)selChild;
            	if(selTextField.getId().contains("Spec")) continue;
            	selTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            		printDebug(selTextField.getId() + " -> " + newValue);
            	    setField(spec, selTextField.getId(), newValue);
            	});
            	try {
            		Field selField = getField(spec, selTextField.getId());
            		if(selField == null) return;
                    selTextField.setText(getField(spec, selTextField.getId()).get(spec).toString());
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
			} else if (selChild.getClass().getSimpleName().toString().equals("ChoiceBox")) {
        		ObservableList<String> tempNames = FXCollections.observableArrayList();
            	ChoiceBox<String> selBox = (ChoiceBox) selChild;
            	if(selBox.getId().contains("Spec")) {
            		String reqNamesOfType = selBox.getId().split("_")[0].replace("Spec","");
            		printDebug(reqNamesOfType);
            		_SpecHandler.nodesList.stream().filter(n->n.type.equals(reqNamesOfType)).forEach(n->tempNames.add(n.name));
            		printDebug(tempNames.toString());
            		selBox.setItems(tempNames);
            		if(tempNames.size()>-1) selBox.setValue(tempNames.get(0));
            		selBox.setOnAction((event) -> setField(spec, selBox.getId().split("_")[1], selBox.getSelectionModel().getSelectedItem().toString()));
            	}
            }
		}
	}
	
	abstract void initDefaultObjects();
	
	@FXML
	Spec saveSpecHandler() {
		ap.getChildren().stream().forEach(c->printDebug(c.getId()));
		((Stage) saveButton.getScene().getWindow()).close();
		printDebug(this.spec.toString());
		return this.spec;
	}
	final public void recover() {
		printDebug("Recovering...");
		printDebug("SpecC:"+spec.toString());
		spec.pop();
		if(specPrev!=null && !specPrev.isTemp) {
			specPrev.add();
			printDebug("SpecP:"+specPrev.toString());
		}
		spec=specPrev;
	}
	final public Spec init(Spec s) {
		if(s!=null) {
			spec=s;
			specPrev=new _SpecHandler.Spec(s.toJSON());
		}
		initDefaultObjects();
		parseChildrenOf(ap);
		printDebug("Finished parsing");
		return spec;
	}
}