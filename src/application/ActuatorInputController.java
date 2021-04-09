package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application._SpecHandler.ActuatSpec;
import application._SpecHandler.DeviceSpec;
import application._SpecHandler.EdgeSpec;
import application._SpecHandler.NodeSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ActuatorInputController extends Controller implements Initializable{
	_SpecHandler textfile = new _SpecHandler();
	ActuatSpec a;
	
	String actuator_Name;
	@FXML
	private TextField actuatorName;
	@FXML
	private Button saveActuator;
	@FXML
	private TextField actuatLatency;
	@FXML
	private ChoiceBox<String> nodeBox;

	@FXML
	private TableColumn<ActuatSpec, String> testCol;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> items = FXCollections.observableArrayList();
		_SpecHandler.devicesList.forEach(d->items.add(d.name));
		nodeBox.setItems(items);
		
		ActuatSpec actuat= (ActuatSpec)_SpecHandler.getSelected("actuat");
		if(actuat == null) {
			actuatLatency.setText("6.0");
			actuatorName.setText("actuator1");
		}else {
			for(NodeSpec n : _SpecHandler.nodesList) {
				for(EdgeSpec e : n.edgesList) if(e.dst.equals(actuat)) actuatLatency.setText(String.valueOf(e.latency));
			}
			actuatorName.setText(actuat.name);
		}
	}
	
	@FXML
	void saveSpecHandler(ActionEvent event) {
		Stage stage = (Stage) save.getScene().getWindow();
		a = new ActuatSpec(actuatorName.getText().toString(), nodeBox.getSelectionModel().getSelectedItem(), Double.parseDouble(actuatLatency.getText()));
		stage.close();
		ArrayList<String> types = new ArrayList<String>(); types.add("device");
		DeviceSpec src = (DeviceSpec) _SpecHandler.getLinkableNode(types, nodeBox.getSelectionModel().getSelectedItem());
		if(src != null) src.addLink(actuator_Name, Double.parseDouble(actuatLatency.getText()));
		stage.close();
	}
	
	public ActuatSpec getSpec() {return a;}

	@Override
	void makeSpec() {
		// TODO Auto-generated method stub
		
	}
}
