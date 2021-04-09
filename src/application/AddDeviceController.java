package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.Format.Field;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.fog.test.perfeval.TextParser;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.EdgeSpec;
import application._SpecHandler.NodeSpec;
import application._SpecHandler.SensorSpec;

import java.io.IOException;

public class AddDeviceController extends Controller implements Initializable {
	DeviceSpec h;
	DeviceSpec sel;
	_SpecHandler textfile = new _SpecHandler();
	ObservableList<DeviceSpec> nodeData = FXCollections.observableArrayList();
	ObservableList<NodeSpec> tempData = FXCollections.observableArrayList();
	
	@FXML
    private BorderPane bp;

    @FXML
    private AnchorPane ap;

    @FXML
    private Button saveSpec;

    @FXML
    private TableView<?> name;

    @FXML
    private TableView<?> upbw;

    @FXML
    private TableView<?> apower;

    @FXML
    private TableView<?> mips;

    @FXML
    private TableView<?> downbw;

    @FXML
    private TableView<?> ipower;

    @FXML
    private TableView<?> ram;

    @FXML
    private TableView<?> rate;

    @FXML
    private TableView<?> latency;

    @FXML
    private TableView<?> pe;

    @FXML
    void addNodeHandler(ActionEvent event) {
    }
	
	@FXML
	void saveSpecHandler(ActionEvent event) throws NumberFormatException, IOException {
		Stage stage = (Stage) saveSpec.getScene().getWindow();
		
		System.out.println(_SpecHandler.defaultDevice.toString());
        ap.getChildren().stream().forEach(selTable->{
            if(selTable instanceof TableView) {
                TableColumn selCols = (TableColumn) (((TableView) selTable).getColumns().get(0));
                ((TableView) selTable).setEditable(true);
                selCols.setCellValueFactory(new PropertyValueFactory <>(selTable.getId()));
                selCols.setCellFactory(TextFieldTableCell.forTableColumn());
            }
        });
//        stage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tempData.add(_SpecHandler.defaultDevice);
        ap.getChildren().stream().forEach(selTable->{
            if(selTable instanceof TableView) {
                ((TableView) selTable).setItems(tempData);
                TableColumn selCols = (TableColumn) (((TableView) selTable).getColumns().get(0));
                ((TableView) selTable).setEditable(true);
                selCols.setCellValueFactory(new PropertyValueFactory <>(selTable.getId()));
                selCols.setCellFactory(TextFieldTableCell.forTableColumn());
            }
        });
	}
	
	public DeviceSpec getSpec() {
		return h;
	}
	
	@Override
	void makeSpec() {
		// TODO Auto-generated method stub
		
	}
	
}

