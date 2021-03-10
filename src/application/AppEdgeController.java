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
    	
    	if (parent.getText().trim().isEmpty()) {
    		parent.setText("default_edge");
 	   		 } 
 	   if (child.getText().trim().isEmpty()) {
 		  child.setText("0");
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
 	  
    	appEdgeName = parent.getText()+"-"+child.getText()+" edge";
    	addLine();
    	
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
        String line = parent.getText() +" " +child.getText() +" "+periodicity.getText()+" " +cpuLength.getText() +" " +newLength.getText()+" " +tupleType.getText()+" " +direction.getText() +" " +edgeType.getText()+"\n";
        FileWriter file_writer;
        try {
            file_writer = new FileWriter("edges.txt",true);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
            buffered_Writer.write(line);
            buffered_Writer.flush();
            buffered_Writer.close();
            

        } catch (IOException e) {
            System.out.println("Add line failed!" +e);
        }
        	try {
				TxtParser();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    String jsonDestinationFileName = createJsonController.jsonDestinationFileName;
    
    public void TxtParser() throws Exception, IOException {
		String sourceFileName = "edges.txt";
		String jsonFileName = jsonDestinationFileName + ".json";
		//String jsonFileName = "test6.json";
    	TxtParser textfile = new TxtParser();
    		textfile.createTopology(sourceFileName);
    		textfile.writeJSON(jsonFileName);
    }

    public String getAppEdgeName() {
        return appEdgeName;
    }
}
