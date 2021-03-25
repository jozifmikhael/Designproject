package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class setParamsController {
	public static int simulationTime = 1000;
	public static int granularityMetric = 10;
	public static String finalPolChoice = "default";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField simTimeValue;

    @FXML
    private TextField GranValue;

    @FXML
    private ChoiceBox<String> PolicyChoice;
    
    @FXML
    private Button setParamsButton;
       
   
    @FXML
    void saveSimParamsHandler(ActionEvent event) {
    	Stage stage = (Stage) setParamsButton.getScene().getWindow();
    	
    	if (simTimeValue.getText() == null || simTimeValue.getText().trim().isEmpty()) simulationTime = 1000;
		else simulationTime = Integer.parseInt(simTimeValue.getText());
		System.out.println("Simulation will run for: " + simulationTime + " instructions \n");
		
		if (GranValue.getText() == null || GranValue.getText().trim().isEmpty()) granularityMetric = 10;
		else granularityMetric = Integer.parseInt(GranValue.getText());
		System.out.println("Simulation granularity will be: " + granularityMetric + " \n");
		
		if (PolicyChoice.getSelectionModel().getSelectedItem() == null
				|| PolicyChoice.getSelectionModel().getSelectedItem().trim().isEmpty()) {			
			PolicyChoice.setValue("ProfitAwareDefaultNew");
		}
		finalPolChoice = PolicyChoice.getSelectionModel().getSelectedItem().toString();
    	stage.close();

    }
    
    //Static String finalPolChoice = PolicyChoice.getSelectionModel().getSelectedItem().toString();
    
    void populateList(List<String> str_list) {
//    	System.out.println(str_list);
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll(str_list);
//		System.out.println(items.size());
		PolicyChoice.setItems(items);	
		PolicyChoice.setValue(items.get(0));
		
	}
    

    @FXML
    void initialize() {
//        assert simTimeValue != null : "fx:id=\"simTimeValue\" was not injected: check your FXML file 'setSimParamsBox.fxml'.";
//        assert setParamsButton != null : "fx:id=\"setParamsButton\" was not injected: check your FXML file 'setSimParamsBox.fxml'.";
//        assert GranValue != null : "fx:id=\"GranValue\" was not injected: check your FXML file 'setSimParamsBox.fxml'.";
//        assert PolicyChoice != null : "fx:id=\"PolicyChoice\" was not injected: check your FXML file 'setSimParamsBox.fxml'.";

    }
}
