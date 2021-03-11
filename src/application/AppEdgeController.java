package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AppEdgeController {
	String appEdgeName;
	TxtParser textfile = new TxtParser();
	
    @FXML
    private TextField parent;

    @FXML
    private TextField child;

    @FXML
    private TextField tupleType;

    @FXML
    private Button saveAppEdge;

    @FXML
    private TextField periodicity;

    @FXML
    private TextField cpuLength;

    @FXML
    private TextField newLength;

    @FXML
    private TextField edgeType;

    @FXML
    private TextField direction;

    @FXML
    private ChoiceBox<String> parentChoice;

    @FXML
    private ChoiceBox<String> childChoice;
    
    @FXML
    void saveAppEdgeHandler(ActionEvent event) {
    	
    	Stage stage = (Stage) saveAppEdge.getScene().getWindow();
    	
    	if (parentChoice.getSelectionModel().getSelectedItem() == null || parentChoice.getSelectionModel().getSelectedItem().trim().isEmpty()) {
    		parentChoice.setValue("default_parent_edge");
 	   		 } 
 	   if (childChoice.getSelectionModel().getSelectedItem() == null || childChoice.getSelectionModel().getSelectedItem().trim().isEmpty()) {
 		  childChoice.setValue("default_child_edge");
 	   }
 	   if (periodicity.getText().trim().isEmpty()) {
 		  periodicity.setText("0");
 	         } 
 	   if (cpuLength.getText().trim().isEmpty()) {
 		  cpuLength.setText("0");
 	         } 
 	   if (newLength.getText().trim().isEmpty()) {
 		  newLength.setText("0");
 	         } 
 	   if (tupleType.getText().trim().isEmpty()) {
 		  tupleType.setText("0");
 	         } 
 	   if (direction.getText().trim().isEmpty()) {
 		  direction.setText("0");
 	         } 
 	   if (edgeType.getText().trim().isEmpty()) {
 		  edgeType.setText("0");
 	         } 
 	  
 	  appEdgeName = parentChoice.getSelectionModel().getSelectedItem().trim()+"-"+childChoice.getSelectionModel().getSelectedItem().trim()+" edge";
  	addLine();
  	String jsonDestinationFileName = createJsonController.jsonDestinationFileName;
   	textfile.writeJSON(jsonDestinationFileName + ".json");
    	
    	stage.close();
    }
    
    
    void populateParentList(List<String> str_list) {
    	ObservableList<String> items = FXCollections.observableArrayList();
    	items.addAll(str_list);
    	parentChoice.setItems(items);
    }
    
    void populateChildList(List<String> str_list) {
    	ObservableList<String> items = FXCollections.observableArrayList();
    	items.addAll(str_list);
    	childChoice.setItems(items);
    }
    
    private void addLine() {
//    	parent.setText("default_edge"); child.setText("0"); periodicity.setText("0"); cpuLength.setText("0"); newLength.setText("0"); tupleType.setText("0"); direction.setText("0"); edgeType.setText("0");
    	String edgeLine = parentChoice.getSelectionModel().getSelectedItem() +" " +childChoice.getSelectionModel().getSelectedItem() +" " +tupleType.getText() + " " + periodicity.getText() + " " + cpuLength.getText() +" " + newLength.getText() + " " + edgeType.getText() + " " + direction.getText() +"\n";
        try {
			textfile.createEdgeTopology(edgeLine);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    String jsonDestinationFileName = createJsonController.jsonDestinationFileName;

    public String getAppEdgeName() {
        return appEdgeName;
    }
}
