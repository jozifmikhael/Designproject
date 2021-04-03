package application;


import java.io.IOException;

import application.SetupJSONParser.ActuatSpec;
import application.SetupJSONParser.SensorSpec;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import application.SetupJSONParser.DeviceSpec;
import application.SetupJSONParser.ModuleSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.event.EventHandler;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.fog.test.perfeval.TextParser;

import application.SetupJSONParser.DeviceSpec;


public class ActuatorInputController {
	SetupJSONParser textfile = new SetupJSONParser();
	ActuatSpec a;
	
	String actuator_Name;
	@FXML
	private TextField actuatorName;
	@FXML
	private TextField parentName;
	@FXML
	private Button saveActuator;


	@FXML
	void saveActuatorHandler(ActionEvent event) {
		Stage stage = (Stage) saveActuator.getScene().getWindow();
		a = textfile.new ActuatSpec(actuatorName.getText().toString(), parentName.getText().toString());
		stage.close();
	}
	public ActuatSpec getSpec() {return a;}
}
