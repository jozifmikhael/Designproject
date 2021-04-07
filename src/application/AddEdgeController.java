package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.EdgeSpec;
import application._SpecHandler.ModuleSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEdgeController {
	EdgeSpec v;
	_SpecHandler textfile = new _SpecHandler();
	
	@FXML
	private TextField parent;
	
	@FXML
	private TextField child;
	
	@FXML
	private TextField tupleType;
	
	@FXML
	private Button saveAppEdge;
	
	@FXML
	private TextField periodicity;
	
	@FXML
	private TextField cpuLength;
	
	@FXML
	private TextField nwLength;
	
	@FXML
	private TextField edgeType;

	@FXML
	private TextField direction;
	
	@FXML
	private TextField latency;
	
	@FXML
	private ChoiceBox<String> parentChoice;
	
	@FXML
	private ChoiceBox<String> childChoice;
	
	@FXML
	void saveAppEdgeHandler(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveAppEdge.getScene().getWindow();
		v = new EdgeSpec(
				childChoice.getSelectionModel().getSelectedItem(),
				parentChoice.getSelectionModel().getSelectedItem(),
				edgeType.getText(),
				Double.parseDouble(latency.getText()),
				tupleType.getText(),
				Double.parseDouble(periodicity.getText()),
				Double.parseDouble(cpuLength.getText()),
				Double.parseDouble(nwLength.getText()),
				Integer.parseInt(direction.getText()));
		stage.close();
	}
	
	void populateList(List<ModuleSpec> modulesList) {
		ObservableList<String> items = FXCollections.observableArrayList();
		for(ModuleSpec m : modulesList) items.add(m.name);
		parentChoice.setItems(items);
		childChoice.setItems(items);
		if(items.size()>1) {
			parentChoice.setValue(items.get(0));
			childChoice.setValue(items.get(1));
		}
	}
	
	public EdgeSpec getSpec() {
		return v;
	}

	public void setChoices(List<String> selectedModulesList) {
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll(selectedModulesList);
		parentChoice.setValue(items.get(0));
		childChoice.setValue(items.get(1));
	}
}
