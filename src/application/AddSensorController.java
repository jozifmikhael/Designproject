package application;

import application._SpecHandler.SensorSpec;

public class AddSensorController extends _SubController{
	 @Override
    void initDefaultObject() {
        spec = new SensorSpec("node"+_SpecHandler.nodesList.size(), thisType, 0, 0, 0, 0, 0, 0, thisType);
    }
}
	