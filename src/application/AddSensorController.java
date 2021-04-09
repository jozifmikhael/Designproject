package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import application._SpecHandler.DeviceSpec;
import application._SpecHandler.EdgeSpec;
import application._SpecHandler.ModuleSpec;
import application._SpecHandler.NodeSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.event.EventHandler;
import application._SpecHandler.SensorSpec;

public class AddSensorController extends Controller{
	SensorSpec spec;
	
	@FXML
    private ChoiceBox<String> nodeBox;
	
	
	
	void populateList(List<DeviceSpec> devicesList) {
		ObservableList<String> items = FXCollections.observableArrayList();
		for(DeviceSpec d : devicesList) items.add(d.name);
		nodeBox.setItems(items);
	}
}
	