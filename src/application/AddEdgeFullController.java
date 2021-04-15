package application;


import application._SpecHandler.EdgeSpec;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;


public class AddEdgeFullController extends _SubController{
	@FXML
	Label srcLabel;
	@FXML
	Label dstLabel;
	
	@Override
    void initDefaultObjects() {
		if(this.spec==null) {
			spec = new EdgeSpec(_MainWindowController.linkSrcNode, _MainWindowController.selNode);
		}
		srcLabel.setText(_MainWindowController.linkSrcNode.name);
		dstLabel.setText(_MainWindowController.selNode.name);		
    }
}