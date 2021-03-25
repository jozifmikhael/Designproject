package application;


import java.io.IOException;

import application.SetupJSONParser.ActuatorSpec;
import application.SetupJSONParser.SensorSpec;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.fog.test.perfeval.TextParser;

import application.SetupJSONParser.DeviceSpec;


public class ActuatorInputController {
	
	SetupJSONParser textfile = new SetupJSONParser();
	ActuatorSpec a;
	
	String actuator_Name;
	@FXML
	private TextField actuatorName;

//	@FXML
//	private TextField actuatorUserID;
//
//	@FXML
//	private TextField actuatorAppID;

	@FXML
	private Button saveActuator;

//	@FXML
//	private TextField actuatorType;

	@FXML
	void saveActuatorHandler(ActionEvent event) {
		if (actuatorName.getText().trim().isEmpty()) {
			actuatorName.setText("default_actuator");
		}
//		if (actuatorUserID.getText().trim().isEmpty()) {
//			actuatorUserID.setText("default_USERID");
//		}
//		if (actuatorAppID.getText().trim().isEmpty()) {
//			actuatorAppID.setText("default_APPID");
//		}
//		if (actuatorType.getText().trim().isEmpty()) {
//			actuatorType.setText("default_ActuatorType");
//		}
		Stage stage = (Stage) saveActuator.getScene().getWindow();
		actuator_Name = actuatorName.getText();
		
		try {
			a = textfile.createActuator(actuatorName.getText().toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stage.close();
		
	}
	
    
	public String getActuatorName() {
        return actuator_Name;
	}
	
	public ActuatorSpec getSpec() {return a;}
	

}
