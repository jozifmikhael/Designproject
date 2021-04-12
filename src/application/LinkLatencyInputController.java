package application;

import java.net.URL;
import java.util.ResourceBundle;

import application._SpecHandler.EdgeSpec;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LinkLatencyInputController extends _SubController{

	@Override
	void initDefaultObject() {
		// TODO Auto-generated method stub
        spec = new _SpecHandler.EdgeSpec(_MainWindowController.selNode, _MainWindowController.linkSrcNode);
	}
}