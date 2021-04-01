package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StringBoxController {

	public static String FinalStringName = "defaultString";
	
    @FXML
    private TextField stringName;

    @FXML
    private Button setStringName;

    @FXML
    void saveStringNameHandler(ActionEvent event) {
    	Stage stage = (Stage) setStringName.getScene().getWindow();
    	if (stringName.getText() == null || stringName.getText().trim().isEmpty()) FinalStringName = "defaultString";
    	else FinalStringName = stringName.getText().toString();
    	System.out.println("Name has been set as: " + FinalStringName + "\n");
    	stage.close();
    }
}