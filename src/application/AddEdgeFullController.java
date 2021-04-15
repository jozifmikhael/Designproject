package application;


import application._SpecHandler.EdgeSpec;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;


public class AddEdgeFullController extends _SubController{
	@FXML
	ChoiceBox c_box1;
	@FXML
	ChoiceBox c_box2;
	
	@Override
    void initDefaultObjects() {
		if(this.spec==null) spec = new EdgeSpec(_MainWindowController.linkSrcNode, _MainWindowController.selNode);
		
    }
}