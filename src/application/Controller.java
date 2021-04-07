package application;

import application._SpecHandler.Spec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class Controller {
	@FXML
	public Button saveSpec;
	ObservableList<?> data = FXCollections.observableArrayList();
	abstract void makeSpec();  //getAllTextFields.foreach(t->(get(t.name)=t.val));
	abstract Spec getSpec();
	@FXML
	void saveSpecHandler() {
		Stage stage = (Stage) saveSpec.getScene().getWindow();
		makeSpec();
		stage.close();
	}
}
