package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class Controller {
	@FXML
	private Button saveSpec;
	ObservableList<?> data = FXCollections.observableArrayList();
	abstract void initialize();
	abstract void setDefaults();
	
	@FXML
	void saveSpecHandler() {
		Stage stage = (Stage) saveSpec.getScene().getWindow();
		makeSpec();
		stage.close();
	}
	void makeSpec() {
		//getAllTextFields.foreach(t->(get(t.name)=t.val));
	}
}
