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
	@FXML
	TextField inTuple_TupleSpec;
	@FXML
	TextField outTuple_TupleSpec;
	@FXML
	TextField fractionalSensitivity_TupleSpec;
	@FXML
	TableView TupleSpec;
	@Override
	void initDefaultObjects() {
		if(this.spec==null) spec = new ModuleSpec("node"+_SpecHandler.nodesList.size(), thisType, 0, 0, 0, 0, new ArrayList<TupleSpec>(), new ArrayList<EdgeSpec>());
		tupleData.add(new TupleSpec("-", "-", 0.0, (ModuleSpec) spec));
		printDebug();
		tupleData.addAll(((ModuleSpec)this.spec).tupleMappings);
		TupleSpec.setItems(tupleData);
		printDebug("Leaving mod extended init");
	}
	
	@FXML
    void addTupleMap() {
		printDebug("Adding tuple");
		printDebug(inTuple_TupleSpec.getText());
		printDebug(outTuple_TupleSpec.getText());
		printDebug(fractionalSensitivity_TupleSpec.getText());
		TupleSpec t = new TupleSpec(inTuple_TupleSpec.getText(),outTuple_TupleSpec.getText(),Double.parseDouble(fractionalSensitivity_TupleSpec.getText()), (ModuleSpec) spec);
		tupleData.add(t);
		t.add();
		
		inTuple_TupleSpec.clear();
		outTuple_TupleSpec.clear();
		fractionalSensitivity_TupleSpec.clear();
		TupleSpec.refresh();
    }
}