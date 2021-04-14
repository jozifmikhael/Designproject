package application;

import application._SpecHandler.DeviceSpec;
import application._SpecHandler.Spec;
import static application.scratch.printDebug;


public class AddDeviceController extends _SubController{
	public AddDeviceController(){
		printDebug("In const");
	}
	@Override
	void initDefaultObject() {
//		printDebug("In device controller");
		spec = new DeviceSpec("node"+_SpecHandler.nodesList.size(), 10, 1500, 10240, 0, 50.0, 1.0, 3.0, 2.0, 850, 850);
	}
}