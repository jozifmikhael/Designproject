package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class setSimTimeController {
	public static int simulationTime = 1000;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField simTimeValue;

    @FXML
    private Button setDurationButton;

    @FXML
    void saveSimDurationHandler(ActionEvent event) {
    	Stage stage = (Stage) setDurationButton.getScene().getWindow();
    	if (simTimeValue.getText() == null || simTimeValue.getText().trim().isEmpty()) simulationTime = 1000;
		else simulationTime = Integer.parseInt(simTimeValue.getText());
		System.out.println("Simulation will run for: " + simulationTime + " instructions \n");
    	stage.close();

    }

    @FXML
    void initialize() {
        assert simTimeValue != null : "fx:id=\"simTimeValue\" was not injected: check your FXML file 'setSimTimeBox.fxml'.";
        assert setDurationButton != null : "fx:id=\"setDurationButton\" was not injected: check your FXML file 'setSimTimeBox.fxml'.";

    }
}
