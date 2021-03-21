package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import application.SetupJSONParser.ModuEdgeSpec;
import application.SetupJSONParser.ModuSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEdgeController {
	ModuEdgeSpec v;
	SetupJSONParser textfile = new SetupJSONParser();
	
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
	void saveAppEdgeHandler(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveAppEdge.getScene().getWindow();
		if (parentChoice.getSelectionModel().getSelectedItem() == null
				|| parentChoice.getSelectionModel().getSelectedItem().trim().isEmpty()) {
			parentChoice.setValue("No Mods Found");
		}
		if (childChoice.getSelectionModel().getSelectedItem() == null
				|| childChoice.getSelectionModel().getSelectedItem().trim().isEmpty()) {
			childChoice.setValue("No Mods Found");
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

		v = textfile.createModuleEdge(parentChoice.getSelectionModel().getSelectedItem() + " "
				+ childChoice.getSelectionModel().getSelectedItem() + " " + tupleType.getText() + " "
				+ periodicity.getText() + " " + cpuLength.getText() + " " + newLength.getText() + " "
				+ edgeType.getText() + " " + direction.getText() + "\n");
		stage.close();
	}
	
	void populateList(List<String> str_list) {
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll(str_list);
		parentChoice.setItems(items);
		childChoice.setItems(items);
		if(items.size()>1) {
			parentChoice.setValue(items.get(0));
			childChoice.setValue(items.get(1));
		}
	}
	
	public ModuEdgeSpec getSpec() {
		return v;
	}
}
