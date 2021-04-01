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
import application.SetupJSONParser.LinkSpec;


public class AddDeviceController {
	DeviceSpec h;
	LinkSpec l;
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
    private TextField upLinkLatency;
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
    private TextField transmissionTime;
	
	@FXML
	void saveNodeHandler(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveNode.getScene().getWindow();
		if (name.getText().trim().isEmpty()) {name.setText("default_device");}
		if (parentName.getText().trim().isEmpty()) {parentName.setText("default_parent");}
		h = textfile.createDevice(
				name.getText().toString(),
				parentName.getText().toString(),
				Long.parseLong(mips.getText()),
				Integer.parseInt(ram.getText()),
				Long.parseLong(upbw.getText()),
				Long.parseLong(downbw.getText()),
				Integer.parseInt(nodelvl.getText()),
				Double.parseDouble(ratePerMIPS.getText()),
				Double.parseDouble(busyPower.getText()),
				Double.parseDouble(idlePower.getText()),
				Double.parseDouble(upLinkLatency.getText()));
		
		l = textfile.createLink(name.getText().toString(), parentName.getText().toString(), Double.parseDouble(upLinkLatency.getText()));
		stage.close();
	}
	public DeviceSpec getSpec() {return h;}
	public LinkSpec getLinkSpec() {return l;}
}