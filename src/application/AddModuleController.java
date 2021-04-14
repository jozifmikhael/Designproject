package application;

import static application.scratch.printDebug;

import java.util.ArrayList;
import application._SpecHandler.EdgeSpec;
import application._SpecHandler.ModuleSpec;
import application._SpecHandler.TupleSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AddModuleController extends _SubController{
	ObservableList<TupleSpec> tupleData = FXCollections.observableArrayList();
	ObservableList<EdgeSpec> nodeData = FXCollections.observableArrayList();
	@FXML
	TextField inTuple_TupleSpec;
	@FXML
	TextField outTuple_TupleSpec;
	@FXML
	TextField fractionalSensitivity_TupleSpec;
	@FXML
	TableView TupleSpec;
	@FXML
	TableView NodeSpec;
	@Override
	void initDefaultObjects() {
        spec = new ModuleSpec("node"+_SpecHandler.nodesList.size(), thisType, 0, 0, 0, 0, new ArrayList<TupleSpec>(), new ArrayList<EdgeSpec>());
        printDebug("In mod extended init");
		tupleData.add(new TupleSpec("-", "-", 0.0, (ModuleSpec) spec));
		tupleData.addAll(((ModuleSpec)this.spec).tupleMappings);
		TupleSpec.setItems(tupleData);
		nodeData.add(new EdgeSpec());
		nodeData.addAll(((ModuleSpec)this.spec).moduleMappings);
		NodeSpec.setItems(nodeData);
		printDebug("Leaving mod extended init");
	}
	
	@FXML
    void addTupleMap() {
		TupleSpec t = new TupleSpec(inTuple_TupleSpec.getText(),outTuple_TupleSpec.getText(),Double.parseDouble(fractionalSensitivity_TupleSpec.getText()), (ModuleSpec) spec);
		tupleData.add(t);
		((ModuleSpec)this.spec).tupleMappings.add(t);
		inTuple_TupleSpec.clear();
		outTuple_TupleSpec.clear();
		fractionalSensitivity_TupleSpec.clear();
		TupleSpec.refresh();
    }
}