package application;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class setGranController {

	public static int granularityMetric = 10;

	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField GranValue;

    @FXML
    private Button setGranButton;

    @FXML
    void saveGranHandler(ActionEvent event) {
    	Stage stage = (Stage) setGranButton.getScene().getWindow();
    	if (GranValue.getText() == null || GranValue.getText().trim().isEmpty()) granularityMetric = 10;
		else granularityMetric = Integer.parseInt(GranValue.getText());
		System.out.println("Simulation granularity will be: " + granularityMetric + " \n");
    	stage.close();    	

    }

    @FXML
    void initialize() {
        assert GranValue != null : "fx:id=\"GranValue\" was not injected: check your FXML file 'setSimTimeBox.fxml'.";
        assert setGranButton != null : "fx:id=\"setGranButton\" was not injected: check your FXML file 'setSimTimeBox.fxml'.";

    }
}
