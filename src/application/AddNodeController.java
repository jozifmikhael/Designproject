package application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.fog.test.perfeval.TextParser;

import application.SetupJSONParser.DeviceSpec;

import java.io.IOException;

public class AddNodeController {
	DeviceSpec h;
	SetupJSONParser textfile = new SetupJSONParser();

	@FXML
	private TextField name;
	
	@FXML
	private TextField parentName;
	
	@FXML
	private TextField mips;
	
	@FXML
	private TextField ram;
	
	@FXML
	private Button saveNode;
	
	@FXML
	private TextField upbw;
	
	@FXML
	private TextField downbw;
	
	@FXML
	private TextField nodelvl;
	
	@FXML
	private TextField ratePerMIPS;
	
	@FXML
	private TextField busyPower;
	
	@FXML
	private TextField idlePower;
	
	@FXML
	void saveNodeHandler(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveNode.getScene().getWindow();
		if (name.getText().trim().isEmpty()) {
			name.setText("default_device");
		}
		if (parentName.getText().trim().isEmpty()) {
			parentName.setText("default_parent");
		}
		if (mips.getText().trim().isEmpty()) {
			mips.setText("0");
		}
		if (ram.getText().trim().isEmpty()) {
			ram.setText("0");
		}
		if (upbw.getText().trim().isEmpty()) {
			upbw.setText("0");
		}
		if (downbw.getText().trim().isEmpty()) {
			downbw.setText("0");
		}
		if (nodelvl.getText().trim().isEmpty()) {
			nodelvl.setText("0");
		}
		if (ratePerMIPS.getText().trim().isEmpty()) {
			ratePerMIPS.setText("0");
		}
		if (busyPower.getText().trim().isEmpty()) {
			busyPower.setText("0");
		}
		if (idlePower.getText().trim().isEmpty()) {
			idlePower.setText("0");
		}
		h = textfile.createDevice(name.getText().toString() + " " + mips.getText() + " " + ram.getText() + " " + upbw.getText()
		+ " " + downbw.getText() + " " + nodelvl.getText() + " " + ratePerMIPS.getText() + " "
		+ busyPower.getText() + " " + idlePower.getText() + " " + parentName.getText().toString() + " \n");
		stage.close();
	}
	public DeviceSpec getSpec() {return h;}
}