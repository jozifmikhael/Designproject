package application;

import application._SpecHandler.DeviceSpec;

//TODO Tidy up the todo list
//TODO Actually implement/deal with errors caught by try-catches instead of just suppressing

public class AddDeviceController extends Controller{
    @Override
    void setSpec() {
        spec = new DeviceSpec("node"+_SpecHandler.nodesList.size(), 10, 1500, 10240, 0, 50.0, 1.0, 3.0, 2.0, 850, 850);
    }
}