package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.SetupJSONParser.DeviceSpec;
import application.SetupJSONParser.ModuSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.event.EventHandler;
import application.SetupJSONParser.SensorSpec;

public class AddSensorController {
	
	SensorSpec s;
	
	@FXML
	private TextField DeterministicValue;
	
	@FXML
	private TextField mean;
	
	@FXML
	private TextField stdDev;
	
	@FXML
	private TextField max;
	
	@FXML
	private TextField min;
	
	@FXML
	private Button testButton;
	
	@FXML
	private Button saveSensor;
	
	@FXML
	private ChoiceBox<String> nodeBox;
	
	@FXML
	void saveDeterministic(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveSensor.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	void saveNormal(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveSensor.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	void saveUniform(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveSensor.getScene().getWindow();
		stage.close();
	}
	
	void populateList(List<String> str_list) {
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll(str_list);
		nodeBox.setItems(items);
	}
  	
	public SensorSpec getSpec() {return s;}
}