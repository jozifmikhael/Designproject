package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import org.fog.test.perfeval.TextParser;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.NodeSpec;
import application._SpecHandler.SensorSpec;
import application._SpecHandler.jank;

import java.io.IOException;

//TODO Tidy up the todo list
//TODO Actually implement/deal with errors caught by try-catches instead of just suppressing

public class AddDeviceController extends Controller implements Initializable{
	DeviceSpec h;
	@FXML
	private Button saveNode;
	@FXML
    private AnchorPane ap;
    
    ObservableList<NodeSpec> temp = FXCollections.observableArrayList();
//    ObservableList<DeviceSpec> mipsData = FXCollections.observableArrayList();
//    ObservableList<DeviceSpec> ramData = FXCollections.observableArrayList();
	
	@FXML
	void saveNodeHandler(ActionEvent event) throws IOException, IllegalArgumentException, IllegalAccessException {
		Field[] nFields = DeviceSpec.class.getDeclaredFields();
	    Field[] bFields = NodeSpec.class.getDeclaredFields();
	    
	    Field[] totFields = new Field[nFields.length + bFields.length];
	    Arrays.setAll(totFields, i -> (i < nFields.length ? nFields[i] : bFields[i - nFields.length]));
	    
//		Stage stage = (Stage) saveNode.getScene().getWindow();
		System.out.println(_SpecHandler.defaultDevice.toString());
		totFields[15].set(_SpecHandler.defaultDevice, "a_New_Name");
		System.out.println(_SpecHandler.defaultDevice.toString());
//		for(int i=0; i<totFields.length; i++) {
//			System.out.println(i + "\t" + totFields[i]);
//		}
//    	ObservableList<NodeSpec> tempData = FXCollections.observableArrayList();
//    	tempData.add(jank.getDefD());
//		ap.getChildren().stream().forEach(c->System.out.println(c.getId()));
//		for(int i=0; i<ap.getChildren().size(); i++) {
//			Node selChild = ap.getChildren().get(i);
//			if(selChild instanceof TableView) {
//				System.out.println();
//            	((TableView) selChild).setItems(tempData);
//                TableColumn selCols = (TableColumn) (((TableView) selChild).getColumns().get(0));
//                selCols.setCellValueFactory(new PropertyValueFactory <>(selChild.getId()));
//                selCols.setCellFactory(TextFieldTableCell.forTableColumn());
//            }
//		}
//		stage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//[some parent node].[get children()].foreach(c->System.out.println(c.getId()));
//		System.out.println(nameColumn.getId());
		
//		nameColumn.setCellValueFactory(new PropertyValueFactory <>("name"));
//		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
////		mipsTable.setItems(mipsData);
//		mipsTable.setEditable(true);
//		ramTable.setItems(ramData);
//		ramTable.setEditable(true);
		
//		mipsTable.getItems().add(new DeviceSpec ("", "", 0, 50, 0, 0, 0, 0, 0, 0, 0, 0));
//		ramTable.getItems().add(new DeviceSpec ("", "", 0, 0, 12400, 0, 0, 0, 0, 0, 0, 0));
	}
	public DeviceSpec getSpec() {return h;}
	
	@Override
	void makeSpec() {
		// TODO Auto-generated method stub
		
	}
}