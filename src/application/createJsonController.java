package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class createJsonController extends MainWindowController {
	
	public static String jsonDestinationFileName = "default";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField designName;

    @FXML
    private Button setJsonName;

    @FXML
    void saveJsonNameHandler(ActionEvent event) {
    	//Stage stage = (Stage) setJsonName.getScene().getWindow();
        Stage stage = (Stage) setJsonName.getScene().getWindow();
        jsonDestinationFileName = designName.getText().toString();
        if (designName.getText() == null || designName.getText().trim().isEmpty()) {
        	jsonDestinationFileName = "default";
       }
        System.out.println(jsonDestinationFileName + "\n");    
        stage.close();
    }

    @FXML
    void initialize() {
        assert designName != null : "fx:id=\"designName\" was not injected: check your FXML file 'createJsonBox.fxml'.";
        assert setJsonName != null : "fx:id=\"setJsonName\" was not injected: check your FXML file 'createJsonBox.fxml'.";

    }
}
