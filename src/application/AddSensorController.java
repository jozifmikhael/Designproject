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
	SetupJSONParser textfile = new SetupJSONParser();
	
	@FXML
	private TextField sensorLatency;
	
	@FXML
	private TextField sensorName;
	
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
	private Button saveDeterministic;
	
	@FXML
	private Button saveNormal;
	
	@FXML
	private Button saveUniform;
	
	@FXML
	private ChoiceBox<String> nodeBox;
	
	@FXML
	void saveDeterministic(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveDeterministic.getScene().getWindow();
		if (nodeBox.getSelectionModel().getSelectedItem() == null
				|| nodeBox.getSelectionModel().getSelectedItem().trim().isEmpty()) {
			nodeBox.setValue("0");
		}
		if (DeterministicValue.getText().trim().isEmpty()) {
			DeterministicValue.setText("5.0");
		}
		mean.setText("0.0");
		stdDev.setText("0.0");
		max.setText("0.0");
		min.setText("0.0");
		s = textfile.createSensor(nodeBox.getSelectionModel().getSelectedItem() + " " + sensorLatency.getText() + " " + sensorName.getText().toString() 
				+ " " + DeterministicValue.getText() + " " + mean.getText() + " " + stdDev.getText() + " " + max.getText() + " " + min.getText() 
				+ " " + "Deterministic");
		stage.close();
	}
	
	@FXML
	void saveNormal(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveNormal.getScene().getWindow();
		if (nodeBox.getSelectionModel().getSelectedItem() == null
				|| nodeBox.getSelectionModel().getSelectedItem().trim().isEmpty()) {
			nodeBox.setValue("0");
		}
		if (mean.getText().trim().isEmpty()) {
			mean.setText("5.0");
		}
		if (stdDev.getText().trim().isEmpty()) {
			stdDev.setText("1.0");
		}
		DeterministicValue.setText("0.0");
		max.setText("0.0");
		min.setText("0.0");
		s = textfile.createSensor(nodeBox.getSelectionModel().getSelectedItem() + " " + sensorLatency.getText() + " " + sensorName.getText().toString() 
				+ " " + DeterministicValue.getText() + " " + mean.getText() + " " + stdDev.getText() + " " + max.getText() + " " + min.getText() 
				+ " " + "Normal");
		stage.close();
	}
	
	@FXML
	void saveUniform(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveUniform.getScene().getWindow();
		if (nodeBox.getSelectionModel().getSelectedItem() == null
				|| nodeBox.getSelectionModel().getSelectedItem().trim().isEmpty()) {
			nodeBox.setValue("0");
		}
		if (max.getText().trim().isEmpty()) {
			max.setText("5.0");
		}
		if (min.getText().trim().isEmpty()) {
			min.setText("1.0");
		}
		DeterministicValue.setText("0.0");
		mean.setText("0.0");
		stdDev.setText("0.0");
		s = textfile.createSensor(nodeBox.getSelectionModel().getSelectedItem() + " " + sensorLatency.getText() + " " + sensorName.getText().toString() 
				+ " " + DeterministicValue.getText() + " " + mean.getText() + " " + stdDev.getText() + " " + max.getText() + " " + min.getText() 
				+ " " + "Uniform");
		stage.close();
	}
	
	void populateList(List<String> str_list) {
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll(str_list);
		nodeBox.setItems(items);
	}
  	
	public SensorSpec getSpec() {return s;}
}