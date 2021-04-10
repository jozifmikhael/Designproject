package application;

import java.util.ArrayList;
import application._SpecHandler.DeviceSpec;
import application._SpecHandler.ModuleSpec;
import application._SpecHandler.TupleSpec;

public class AddModuleController extends Controller{
	@Override
	void setSpec() {
		ArrayList<TupleSpec> t = new ArrayList<TupleSpec>();

		tempData.addAll(t);
        spec = new ModuleSpec("node"+_SpecHandler.nodesList.size(), thisType, 0, 0, 0, 0, t);
	}
	
}