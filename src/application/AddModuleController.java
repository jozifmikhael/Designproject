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
	private TableView<TupleSpec> tupleTable;
	
	@FXML
	private TableColumn<TupleSpec, String> inTupleColumn;
	
	@FXML
	private TableColumn<TupleSpec, String> outTupleColumn;
	
	@FXML
	private TableColumn<TupleSpec, Double> sensitivityColumn;
	
	@FXML
	private Button addTupleMapping;
	
	@FXML
	void saveModuleHandler(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveModule.getScene().getWindow();
		ArrayList<TupleSpec> tupleMappings = new ArrayList<TupleSpec>();
		for(TupleSpec t: data)tupleMappings.add(t);
		m = parser.new ModuleSpec(
				moduleName.getText(),
				nodeBox.getSelectionModel().getSelectedItem(),
				Integer.parseInt(ram.getText()),
				Long.parseLong(bandwidth.getText()),
				Long.parseLong(size.getText()), Integer.parseInt(mips.getText()),
				new ArrayList<TupleSpec>(tupleMappings));
		stage.close();
	}
	
	@FXML
    void addTupleMap(ActionEvent event) {
		data.add(parser.new TupleSpec(inTuple.getText(),outTuple.getText(),Double.parseDouble(fractionalSensitivity.getText())));
		inTuple.clear();
		outTuple.clear();
		fractionalSensitivity.clear();
    }
	
	@FXML
    public void inTupleEdit(CellEditEvent<TupleSpec, String> t) {
		System.out.println("test");
        t.getTableView().getItems().get(
                t.getTablePosition().getRow()).setInTuple(t.getNewValue());
    }
	
	@FXML
    public void outTupleEdit(CellEditEvent<TupleSpec, String> t) {
        t.getTableView().getItems().get(
                t.getTablePosition().getRow()).setOutTuple(t.getNewValue());
    }
	
	@FXML
    public void sensitivityEdit(CellEditEvent<TupleSpec, String> t) {
        t.getTableView().getItems().get(
                t.getTablePosition().getRow()).setSensitivity(Double.parseDouble(t.getNewValue()));
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
		else {//TODO we need a new way to do forced module placement
//			nodeBox.getSelectionModel().select(module.nodeName);
			moduleName.setText(module.name);
			ram.setText(String.valueOf(module.ram));
			bandwidth.setText(String.valueOf(module.bandwidth));
			size.setText(String.valueOf(module.size));
			mips.setText(String.valueOf(module.mips));
			module.tupleMappings.forEach(t->data.add(t));
		}
		
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
		tupleTable.setEditable(true);		
	}
	public ModuleSpec getSpec() {return m;}
}