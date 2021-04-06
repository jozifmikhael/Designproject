package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.ModuleSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.event.EventHandler;
import application._SpecHandler.SensorSpec;

public class AddSensorController {
	SensorSpec s;
	_SpecHandler parser;
	
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
	private Accordion accord;
	
	@FXML
	private TitledPane deterministicPane;
	
	@FXML
	private TitledPane normalPane;
	
	@FXML
	private TitledPane uniformPane;
	
	@FXML
	void saveDeterministic(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveDeterministic.getScene().getWindow();
		saveSensor("Deterministic");
		stage.close();
	}
	
	@FXML
	void saveNormal(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveNormal.getScene().getWindow();
		saveSensor("Normal");
		stage.close();
	}
	
	@FXML
	void saveUniform(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveUniform.getScene().getWindow();
		saveSensor("Uniform");
		stage.close();
	}
	
	void saveSensor(String distType) {
		if(distType.equals("Deterministic")) {
			if (DeterministicValue.getText().trim().isEmpty()) DeterministicValue.setText("5.0");
		}else{
			DeterministicValue.setText("0.0");
		}
		
		if(distType.equals("Uniform")) {
			if (max.getText().trim().isEmpty()) max.setText("5.0");
			if (min.getText().trim().isEmpty())	min.setText("1.0");
		}else {
			max.setText("0.0");
			min.setText("0.0");
		}
		
		if(distType.equals("Normal")) {
			if (mean.getText().trim().isEmpty()) mean.setText("5.0");
			if (stdDev.getText().trim().isEmpty()) stdDev.setText("1.0");
		}else {
			mean.setText("0.0");
			stdDev.setText("0.0");
		}
		
		s = parser.new SensorSpec(
				sensorName.getText().toString(),
				nodeBox.getSelectionModel().getSelectedItem(),
				Double.parseDouble(sensorLatency.getText()),
				Double.parseDouble(DeterministicValue.getText()),
				Double.parseDouble(mean.getText()),
				Double.parseDouble(stdDev.getText()),
				Double.parseDouble(max.getText()),
				Double.parseDouble(min.getText()),
				distType);
	}
	
	public void initialize(SensorSpec sensor, _SpecHandler _parser) {
		parser=_parser;
		ObservableList<String> items = FXCollections.observableArrayList();
		_SpecHandler.devicesList.forEach(d->items.add(d.name));
		nodeBox.setItems(items);
		
		if(sensor == null) {
			sensorLatency.setText("6.0");
			sensorName.setText("sensor1");
			DeterministicValue.setText("5.1");
			mean.setText("5.0");
			stdDev.setText("1.0");
			max.setText("5.0");
			min.setText("1.0");
			accord.setExpandedPane(deterministicPane);
		}
		else {
			sensorLatency.setText(String.valueOf(sensor.latency));
			sensorName.setText(sensor.name);
			DeterministicValue.setText(String.valueOf(sensor.deterministicValue));
			mean.setText(String.valueOf(sensor.normalMean));
			stdDev.setText(String.valueOf(sensor.normalStdDev));
			max.setText(String.valueOf(sensor.uniformMax));
			min.setText(String.valueOf(sensor.uniformMin));
			switch(sensor.type) {
				case "Deterministic":
					accord.setExpandedPane(deterministicPane);
					break;
				case "Normal":
					accord.setExpandedPane(normalPane);
					break;
				case "Uniform":
					accord.setExpandedPane(uniformPane);
					break;
			}
		}
	}
	
	void populateList(List<DeviceSpec> devicesList) {
		ObservableList<String> items = FXCollections.observableArrayList();
		for(DeviceSpec d : devicesList) items.add(d.name);
		nodeBox.setItems(items);
	}
  	
	public SensorSpec getSpec() {return s;}
}