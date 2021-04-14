package application;


import application._SpecHandler.DeviceSpec;
import application._SpecHandler.EdgeSpec;

public class AddEdgeFullController extends _SubController{
	@Override
    void initDefaultObject() {
        spec = new EdgeSpec(_MainWindowController.linkSrcNode, _MainWindowController.selNode);
    }
}