package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LinkLatencyInputController {
	double latency = 2.0;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField UpLinkLatency;

    @FXML
    private Button setLinkLatency;

    @FXML
    void saveLinkLatency(ActionEvent event) {
    	Stage stage = (Stage) setLinkLatency.getScene().getWindow();
    	if (UpLinkLatency.getText().trim().isEmpty())latency = 2.0;
    	else latency = Double.parseDouble(UpLinkLatency.getText());
    	stage.close();
    }

    @FXML
    void initialize() {
    	
    }
    
    public double getLatency() {return latency;}
}