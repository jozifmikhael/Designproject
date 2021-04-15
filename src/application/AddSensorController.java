package application;

import application._SpecHandler.SensorSpec;

public class AddSensorController extends _SubController{
	 @Override
    void initDefaultObjects() {
		 if(this.spec==null) spec = new SensorSpec("node"+_SpecHandler.nodesList.size(), 5.1, 0, 0, 0, 0);
    }
}
	