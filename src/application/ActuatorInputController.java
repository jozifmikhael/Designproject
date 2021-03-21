package application;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ActuatorInputController {
	String actuator_Name;
	@FXML
	private TextField actuatorName;

	@FXML
	private TextField actuatorUserID;

	@FXML
	private TextField actuatorAppID;

	@FXML
	private Button saveActuator;

	@FXML
	private TextField actuatorType;

	@FXML
	void saveActuatorHandler(ActionEvent event) {
		// saveActuator.setOnAction( e -> System.out.println(actuatorName.getText() + "\n" + actuatorUserID.getText() + "\n" + actuatorAppID.getText() + "\n" + actuatorType.getText()));
		actuator_Name = actuatorName.getText();

	}
	    
	public String getActuatorName() {
        return actuator_Name;
	}

}
