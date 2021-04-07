package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import application._SpecHandler.NodeSpec;
import application._SpecHandler.Spec;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class Controller {
	@FXML
	protected Button saveSpec;
	ObservableList<?> data = FXCollections.observableArrayList();
	abstract void makeSpec();
	abstract Spec getSpec();
	
	@FXML
	void saveSpecHandler() {
		Stage stage = (Stage) saveSpec.getScene().getWindow();
		makeSpec();
		stage.close();
	}
}
