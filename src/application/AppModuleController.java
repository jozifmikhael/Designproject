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

public class AppModuleController {
	private String newStr;
	public void setName(String inp) {
		newStr=inp;
		System.out.println("Set str to " + inp);
	}
	
	String appModuleName;
	
	@FXML
    private TextField nodeName;

    @FXML
    private TextField moduleName;
    
    @FXML
    private ChoiceBox<String> nodeBox;

    @FXML
    private TextField ram;

    @FXML
    private Button saveModule;

    @FXML
    private TextField bandwidth;

    @FXML
    private TextField inTuple;

    @FXML
    private TextField outTuple;

    @FXML
    private TextField size;
    
    @FXML
    private TextField fractionalSensitivity;
    
    @FXML
    private TextField MIPS;
    
    @FXML
    void saveModuleHandler(ActionEvent event) {
    	Stage stage = (Stage) saveModule.getScene().getWindow();
    	
		if (nodeName.getText().trim().isEmpty()) {
			nodeName.setText("default_module");
		   		 } 
		   if (moduleName.getText().trim().isEmpty()) {
			  moduleName.setText("0");
		         } 
		   if (ram.getText().trim().isEmpty()) {
			   ram.setText("0");
		         } 
		   if (MIPS.getText().trim().isEmpty()) {
			  MIPS.setText("0");
		         } 
		   if (size.getText().trim().isEmpty()) {
			  size.setText("0");
		         } 
		   if (bandwidth.getText().trim().isEmpty()) {
			  bandwidth.setText("0");
		         } 
		   if (inTuple.getText().trim().isEmpty()) {
			  inTuple.setText("0");
		         } 
		   if (outTuple.getText().trim().isEmpty()) {
			  outTuple.setText("0");
		         } 
		   if (fractionalSensitivity.getText().trim().isEmpty()) {
			  fractionalSensitivity.setText("0");
		 } 

    	appModuleName = moduleName.getText();
    	addLine();
    	
    	stage.close();
    }
    
    void populateList(List<String> str_list) {
    	ObservableList<String> items = FXCollections.observableArrayList();
    	items.addAll(str_list);
    	nodeBox.setItems(items);
    }
    
    private void addLine() {
        String line = nodeName.getText() +" " +moduleName.getText() +" " +ram.getText() +" " +MIPS.getText() +" " +size.getText()+" " +bandwidth.getText()+" "+ inTuple.getText()+" " +outTuple.getText() +" "+fractionalSensitivity.getText() +"\n";
        FileWriter file_writer;
        try {
            file_writer = new FileWriter("modules.txt",true);
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
		String sourceFileName = "modules.txt";
		String jsonFileName = jsonDestinationFileName + ".json";
		//String jsonFileName = "test6.json";
    	TxtParser textfile = new TxtParser();
    		textfile.createTopology(sourceFileName);
    		textfile.writeJSON(jsonFileName);
    }
    
    public String getAppModuleName() {
        return appModuleName;
    }
}