package application;

import java.io.BufferedWriter;
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
	SetupJSONParser textfile = new SetupJSONParser();
	
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
	private TextField MIPS;
	
	@FXML
	void saveModuleHandler(ActionEvent event) throws NumberFormatException, IOException {
		
//		public ModuleSpec(String name, String parent, String type, String nodeName, int modRam,
//				long bandwidth, String inTuple, String outTuple, long size, int mIPS, double fractionalSensitivity)
		Stage stage = (Stage) saveModule.getScene().getWindow();
		m = textfile.new ModuleSpec(
				moduleName.getText(),
				nodeBox.getSelectionModel().getSelectedItem(),
				null, Integer.parseInt(ram.getText()),
				Long.parseLong(bandwidth.getText()),
				inTuple.getText(), outTuple.getText(),
				Long.parseLong(size.getText()),
				Integer.parseInt(MIPS.getText()),
				Double.parseDouble(fractionalSensitivity.getText()));
		stage.close();
	}
	
	void populateList(List<DeviceSpec> devicesList) {
		ObservableList<String> items = FXCollections.observableArrayList();
		for(DeviceSpec d : devicesList) items.add(d.name);
		nodeBox.setItems(items);
	}
	public ModuleSpec getSpec() {return m;}
}