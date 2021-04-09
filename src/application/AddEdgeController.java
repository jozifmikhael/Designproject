package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.EdgeSpec;
import application._SpecHandler.ModuleSpec;
import application._SpecHandler.NodeSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEdgeController extends Controller implements Initializable{
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
		NodeSpec src = null, dst = null;
		for(NodeSpec n : _SpecHandler.nodesList) {
			if(n.name.equals(childChoice.getSelectionModel().getSelectedItem())) src = n;
			if(n.name.equals(parentChoice.getSelectionModel().getSelectedItem())) dst = n;
		}
		v = new EdgeSpec(
				src,
				dst,
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void makeSpec() {
		// TODO Auto-generated method stub
		
	}
}
