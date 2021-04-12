package application;

import static application.scratch.printDebug;

import java.util.ArrayList;
import application._SpecHandler.DeviceSpec;
import application._SpecHandler.ModuleSpec;
import application._SpecHandler.NodeSpec;
import application._SpecHandler.Spec;
import application._SpecHandler.TupleSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

public class AddModuleController extends _SubController{
	ObservableList<TupleSpec> tupleData = FXCollections.observableArrayList();
	ObservableList<NodeSpec> nodeData = FXCollections.observableArrayList();
	@FXML
	TextField inTuple_TupleSpec;
	@FXML
	TextField outTuple_TupleSpec;
	@FXML
	TextField fractionalSensitivity_TupleSpec;
	@FXML
	TableView TupleSpec;
	@Override
	void initDefaultObject() {
        spec = new ModuleSpec("node"+_SpecHandler.nodesList.size(), thisType, 0, 0, 0, 0, new ArrayList<TupleSpec>());
	}
	void extendedInit() {
		tupleData.add(new TupleSpec("-", "-", 0.0));
		tupleData.addAll(((ModuleSpec)this.spec).tupleMappings);
		TupleSpec.setItems(tupleData);
	}
	
	@FXML
    void addTupleMap() {
		TupleSpec t = new TupleSpec(inTuple_TupleSpec.getText(),outTuple_TupleSpec.getText(),Double.parseDouble(fractionalSensitivity_TupleSpec.getText()));
		tupleData.add(t);
		((ModuleSpec)this.spec).tupleMappings.add(t);
		inTuple_TupleSpec.clear();
		outTuple_TupleSpec.clear();
		fractionalSensitivity_TupleSpec.clear();
		TupleSpec.refresh();
//		printDebug("tupleMappings Size : "+((ModuleSpec)this.spec).tupleMappings.size());
    }
}