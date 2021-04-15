package application;

import application._SpecHandler.EdgeSpec;
import static application.scratch.printDebug;

public class AddEdgeSimpleController extends _SubController{
	@Override
	void initDefaultObjects() {
		printDebug("In simple edge init");
		if(this.spec==null || this.spec.isTemp) spec = new EdgeSpec(_MainWindowController.linkSrcNode, _MainWindowController.selNode);
	}
}