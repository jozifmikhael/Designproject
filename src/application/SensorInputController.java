package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SensorInputController {
	String sensor_Name;
	
	@FXML
    private TextField sensorName;

    @FXML
    private TextField sensorTupleType;

    @FXML
    private TextField sensorUserID;

    @FXML
    private Button saveSensor;

    @FXML
    private TextField sensorAppID;

    @FXML
    private TextField sensorDistribution;
    
    public String getSensorName() {
        return sensor_Name;
	}
	
	 @FXML
	    void saveSensorHandler(ActionEvent event) {
		 sensor_Name = sensorName.getText();
	    }
}
