package application;

import application._SpecHandler.EdgeSpec;
import static application.scratch.printDebug;

public class AddEdgeSimpleController extends _SubController{
	@Override
	void initDefaultObjects() {
		printDebug("In AddEdgeSimpleController");
        spec = new EdgeSpec(_MainWindowController.linkSrcNode, _MainWindowController.selNode);
	}
}