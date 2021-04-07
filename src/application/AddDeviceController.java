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
import java.util.ArrayList;

import org.fog.test.perfeval.TextParser;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.SensorSpec;

import java.io.IOException;

public class AddDeviceController extends Controller {
	DeviceSpec h;
	DeviceSpec sel;
	_SpecHandler textfile = new _SpecHandler();
	
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
		h = new DeviceSpec(
				name.getText().toString(),
				parentName.getText().toString(),0,
				Long.parseLong(mips.getText()),
				Integer.parseInt(ram.getText()),Integer.parseInt(nodelvl.getText()),
				Double.parseDouble(ratePerMIPS.getText()),
				Double.parseDouble(idlePower.getText()),
				Double.parseDouble(busyPower.getText()),Double.parseDouble(upLinkLatency.getText()),
				Long.parseLong(upbw.getText()),
				Long.parseLong(downbw.getText())
				);
		h.addLink(parentName.getText(), Double.parseDouble(upLinkLatency.getText()));
		System.out.println("Made new dev");
		stage.close();
	}
	public void initialize() {
		sel = (DeviceSpec)_SpecHandler.getSelected("device");
		if(sel == null) {
			name.setText("node1");
			mips.setText("1500");
			ram.setText("10240");
			upLinkLatency.setText("2.0");
			upbw.setText("850");
			downbw.setText("850");
			nodelvl.setText("1");
			ratePerMIPS.setText("50.0");
			busyPower.setText("3.0");
			idlePower.setText("1.0");
			return;
		}
		
		sel=(DeviceSpec) sel.pop();
		name.setText(sel.name);
		mips.setText(String.valueOf(sel.mips));
		ram.setText(String.valueOf(sel.ram));
		upLinkLatency.setText(String.valueOf(sel.ram));
		upbw.setText(String.valueOf(sel.upbw));
		downbw.setText(String.valueOf(sel.downbw));
		nodelvl.setText(String.valueOf(sel.level));
		ratePerMIPS.setText(String.valueOf(sel.rate));
		busyPower.setText(String.valueOf(sel.apower));
		idlePower.setText(String.valueOf(sel.ipower));
	}
	public DeviceSpec getSpec() {return h;}
	@Override
	void makeSpec() {
		// TODO Auto-generated method stub
		
	}
	@Override
	void setDefaults() {
		// TODO Auto-generated method stub
		
	}
}