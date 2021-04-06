package application;


import java.io.IOException;

import application._SpecHandler.ActuatSpec;
import application._SpecHandler.SensorSpec;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import application._SpecHandler.DeviceSpec;
import application._SpecHandler.ModuleSpec;
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

import application._SpecHandler.DeviceSpec;


public class ActuatorInputController{
	_SpecHandler textfile = new _SpecHandler();
	ActuatSpec a;
	
	String actuator_Name;
	@FXML
	private TextField actuatorName;
	@FXML
	private TextField parentName;
	@FXML
	private Button saveActuator;
	
    public void initialize() {
		saveActuator.setDisable(true);
    }
	
	@FXML
	void saveActuatorHandler(ActionEvent event) {
		Stage stage = (Stage) saveActuator.getScene().getWindow();
		a = textfile.new ActuatSpec(actuatorName.getText().toString(), parentName.getText().toString());
		stage.close();
	}
	public ActuatSpec getSpec() {return a;}
}
