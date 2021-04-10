package application;

import application._SpecHandler.ActuatSpec;
import application._SpecHandler.DeviceSpec;

public class ActuatorInputController extends _SubController{
	 @Override
    void setSpec() {
        spec = new ActuatSpec("node"+_SpecHandler.nodesList.size(), thisType, 0);
    }
}