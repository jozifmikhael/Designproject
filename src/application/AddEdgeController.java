package application;


import application._SpecHandler.DeviceSpec;
import application._SpecHandler.EdgeSpec;

public class AddEdgeController extends _SubController{
	@Override
    void setSpec() {
        spec = new EdgeSpec(_MainWindowController.selNode, _MainWindowController.linkSrcNode);
    }
}