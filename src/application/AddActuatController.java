package application;

import application._SpecHandler.ActuatSpec;

public class AddActuatController extends _SubController{
	 @Override
    void initDefaultObjects() {
        spec = new ActuatSpec("node"+_SpecHandler.nodesList.size(), thisType, 2.0);
    }
}