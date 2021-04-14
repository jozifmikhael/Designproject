package application;

import application._SpecHandler.SensorSpec;

public class AddSensorController extends _SubController{
	 @Override
    void initDefaultObjects() {
		 if(this.spec==null) spec = new SensorSpec("node"+_SpecHandler.nodesList.size(), thisType, 0, 0, 0, 0, 0, 0, thisType);
    }
}
	