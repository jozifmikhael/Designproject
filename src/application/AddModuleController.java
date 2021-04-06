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
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import application.SetupJSONParser.DeviceSpec;
import application.SetupJSONParser.ModuleSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddModuleController {
	ModuleSpec m;
	SetupJSONParser parser;
	ObservableList<TupleSpec> data = FXCollections.observableArrayList();
	
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
	private TextField mips;
	
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
	void saveModuleHandler(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveModule.getScene().getWindow();
		ArrayList<TupleSpec> tupleMappings = new ArrayList<TupleSpec>();
		m = parser.new ModuleSpec(
				moduleName.getText(),
				nodeBox.getSelectionModel().getSelectedItem(),
				Integer.parseInt(ram.getText()),
				Long.parseLong(bandwidth.getText()),
				Long.parseLong(size.getText()), Integer.parseInt(mips.getText()),
				tupleMappings);
		stage.close();
	}
	public void initialize(ModuleSpec module, SetupJSONParser _parser) {
		parser=_parser;
		ObservableList<String> items = FXCollections.observableArrayList();
		SetupJSONParser.devicesList.forEach(d->items.add(d.name));
		nodeBox.setItems(items);
		if(module == null) {
			moduleName.setText("client");
			ram.setText("10");
			bandwidth.setText("100");
			size.setText("100");
			mips.setText("100");
		}
		else {
			nodeBox.getSelectionModel().select(module.name);
			moduleName.setText(module.name);
			ram.setText(String.valueOf(module.ram));
			bandwidth.setText(String.valueOf(module.bandwidth));
			size.setText(String.valueOf(module.size));
			mips.setText(String.valueOf(module.mips));
		}
	}
	public ModuleSpec getSpec() {return m;}
}