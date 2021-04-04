package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.SetupJSONParser.DeviceSpec;
import application.SetupJSONParser.ModuleSpec;
import application.SetupJSONParser.TupleSpec;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;

public class AddModuleController {
	ModuleSpec m;
	SetupJSONParser textfile = new SetupJSONParser();
	ObservableList<TupleSpec> data = FXCollections.observableArrayList();
	ArrayList<TupleSpec> tupleMapping = new ArrayList<TupleSpec>();
	
	@FXML
	private TextField nodeName;
	
	@FXML
	private TextField moduleName;
	
	@FXML
	private ChoiceBox<String> nodeBox;
	
	@FXML
	private TextField ram;
	
	@FXML
	private Button saveModule;

	@FXML  
	private TableView tupleTable;
	
	@FXML
	private TableColumn inTupleColumn;
	
	@FXML
	private TableColumn outTupleColumn;
	
	@FXML
	private TableColumn sensitivityColumn;
	
	@FXML
	private Button addTupleMapping;
	
	@FXML
	private TextField bandwidth;
	
	@FXML
	private TextField inTuple;
	
	@FXML
	private TextField outTuple;
	
	@FXML
	private TextField size;
	
	@FXML
	private TextField fractionalSensitivity;
	
	@FXML
	private TextField MIPS;
	
	@FXML
	void saveModuleHandler(ActionEvent event) throws NumberFormatException, IOException {
		
//		public ModuleSpec(String name, String parent, String type, String nodeName, int modRam,
//				long bandwidth, String inTuple, String outTuple, long size, int mIPS, double fractionalSensitivity)
		Stage stage = (Stage) saveModule.getScene().getWindow();
		for(TupleSpec t: data)tupleMapping.add(t);
		m = textfile.new ModuleSpec(
				nodeBox.getSelectionModel().getSelectedItem(),
				Long.parseLong(size.getText()),
				Long.parseLong(bandwidth.getText()),
				Integer.parseInt(ram.getText()),
				0.0,
				moduleName.getText(),
				0.0, 
				Integer.parseInt(MIPS.getText()),
				tupleMapping);
		tupleMapping.clear();
		stage.close();
	}
	
	@FXML
    void addTupleMap(ActionEvent event) {
		data.add(textfile.new TupleSpec(inTuple.getText(),outTuple.getText(),Double.parseDouble(fractionalSensitivity.getText())));
		inTuple.clear();
		outTuple.clear();
		fractionalSensitivity.clear();
    }
	
	void initialize() {
		inTuple.setPromptText("In Tuple");
		outTuple.setPromptText("Out Tuple");
		fractionalSensitivity.setPromptText("Sensitivity");
		
		inTupleColumn.setCellValueFactory(new PropertyValueFactory<TupleSpec, String>("inTuple"));		
		inTupleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		outTupleColumn.setCellValueFactory(new PropertyValueFactory<TupleSpec, String>("outTuple"));
		outTupleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		sensitivityColumn.setCellValueFactory(new PropertyValueFactory<TupleSpec, Double>("sensitivity"));
		sensitivityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		tupleTable.setItems(data);
//		if(module == null) {
			moduleName.setText("client");
			ram.setText("10");
			bandwidth.setText("100");
			size.setText("100");
			MIPS.setText("100");
//		}
//		else {
//			nodeBox.getSelectionModel().select(module.nodeName);
//			moduleName.setText(module.name);
//			ram.setText(String.valueOf(module.modRam));
//			bandwidth.setText(String.valueOf(module.bandwidth));
//			size.setText(String.valueOf(module.size));
//			MIPS.setText(String.valueOf(module.MIPS));
//		}
	}	
	
	@FXML
    public void inTupleEdit(CellEditEvent<TupleSpec, String> t) {
		System.out.println("test");
        ((TupleSpec) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setInTuple(t.getNewValue());
    }
	
	@FXML
    public void outTupleEdit(CellEditEvent<TupleSpec, String> t) {
        ((TupleSpec) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setOutTuple(t.getNewValue());
    }
	
	
	@FXML
    public void sensitivityEdit(CellEditEvent<TupleSpec, String> t) {
        ((TupleSpec) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setSensitivity(Double.parseDouble(t.getNewValue()));
    }
	
	
	void populateList(List<DeviceSpec> devicesList) {
		ObservableList<String> items = FXCollections.observableArrayList();
		for(DeviceSpec d : devicesList) items.add(d.name);
		nodeBox.setItems(items);
	}
	public ModuleSpec getSpec() {return m;}
	

}