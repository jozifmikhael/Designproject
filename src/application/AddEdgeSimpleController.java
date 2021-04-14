package application;

import java.net.URL;
import java.util.ResourceBundle;

import application._SpecHandler.EdgeSpec;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static application.scratch.printDebug;

public class AddEdgeSimpleController extends _SubController{

	@Override
	void initDefaultObject() {
		// TODO Auto-generated method stub
		printDebug("In AddEdgeSimpleController");
        spec = new EdgeSpec(_MainWindowController.linkSrcNode, _MainWindowController.selNode);
	}
}