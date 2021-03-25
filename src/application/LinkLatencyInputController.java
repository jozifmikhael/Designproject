package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LinkLatencyInputController {
	static double LinkLatencyValue = 2.0;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField doubleLatency;

    @FXML
    private Button setLinkLatency;

    @FXML
    void saveLinkLatency(ActionEvent event) {
    	Stage stage = (Stage) setLinkLatency.getScene().getWindow();
    	if (doubleLatency.getText() == null || doubleLatency.getText().trim().isEmpty()) LinkLatencyValue = 1;
		else LinkLatencyValue = Double.parseDouble(doubleLatency.getText());
		System.out.println("Link Latency is: " + LinkLatencyValue + " \n");
    	stage.close();

    }

    @FXML
    void initialize() {
        assert doubleLatency != null : "fx:id=\"doubleLatency\" was not injected: check your FXML file 'LinkLatencyInputBox.fxml'.";
        assert setLinkLatency != null : "fx:id=\"setLinkLatency\" was not injected: check your FXML file 'LinkLatencyInputBox.fxml'.";

    }
}