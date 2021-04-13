package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import application._SubController;
import application.Main;
//import org.fog.placement.Controller;
import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacement;
import org.fog.placement.ModulePlacementEdgewards;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.test.perfeval.VRGameFog;
import org.fog.utils.Config;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.TimeKeeper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import application._SpecHandler.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.beans.value.ChangeListener;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.io.BufferedWriter;
// Added by us
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//import java.io.FileNotFoundException;
//import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import org.json.simple.parser.ParseException;
import org.cloudbus.cloudsim.Vm;
//import org.cloudbus.cloudsim.VmAllocationPolicySimple;
//import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.Cloudlet;
//import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
//import java.text.DecimalFormat;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.application.AppEdge;
import org.fog.application.AppLoop;
import org.fog.application.selectivity.FractionalSelectivity;
import org.fog.entities.Actuator;
import org.fog.entities.FogBroker;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.entities.Sensor;
import org.fog.entities.Tuple;
//import org.fog.placement.Controller;
import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacementEdgewards;
import org.fog.placement.ModulePlacementMapping;
import org.fog.placement.ModulePlacementOnlyCloud;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.utils.Config;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.TimeKeeper;
import org.fog.utils.distribution.DeterministicDistribution;
import org.fog.utils.distribution.NormalDistribution;	
import org.fog.utils.distribution.UniformDistribution;
import static application.scratch.printDebug;
import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacementEdgewards;
import org.fog.placement.ModulePlacementMapping;
import org.fog.placement.ModulePlacementOnlyCloud;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.utils.Config;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.TimeKeeper;
import org.fog.utils.distribution.DeterministicDistribution;
import org.fog.utils.distribution.NormalDistribution;	
import org.fog.utils.distribution.UniformDistribution;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;

public class _MainWindowController implements Initializable, EventHandler<KeyEvent>{	
	@FXML
    private TextField deviceField;
	
	@FXML
    private MenuItem ProfitAwareItem;

    @FXML
    private MenuItem QosItem;

    @FXML
    private MenuItem LowestPowerItem;
    
    @FXML
    private MenuItem SetRunTimeGranularity;
    
    @FXML
    private MenuItem SetRunTimeItem;
    
    @FXML
    private MenuItem SetSimParams;

    @FXML
    private TextField policyView;
    
    @FXML
    private Button startButton;
    
    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Canvas topoField;
    
    @FXML
    private Canvas graphArea;
    
    @FXML
    private MenuItem addSensorMenu;
    
    @FXML
    private MenuItem addActuatorMenu;
    
    @FXML
    private MenuItem addDeviceMenu;

    @FXML
    private MenuItem addModuleMenu;

    @FXML
    private MenuItem addEdgeMenu;
    
    @FXML
    private MenuItem resetCacheMenu;
    
    @FXML
    private MenuItem newJSONMenu;
    
    @FXML
    private ListView<String> policyList;
    
    @FXML
    private AnchorPane backPane;

    @FXML
    private ChoiceBox<String> PolicyChoiceMain;

    @FXML
    private Button previewButton;
    
    @FXML
    private TreeView<String> treeView;
    
    ////////////////////////////////////////////////////////////////////////////
    // TODO Tidy up the todo list
	// TODO Actually implement/deal with errors caught by try-catches instead of just suppressing
    // double R=50;
	double xCenter=100;
	double yCenter=100;
	
    GraphicsContext gc;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	printDebug("Empty initialize function ran");
    }
	
	public List<String> selectedNodesList = new ArrayList<String>();
	Map<String, URL> loadersList = new HashMap<String, URL>();
	
    public void setupListeners(Stage parentStage, Scene scene) {
    	ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue)->screenSizeChangeHandler(parentStage);
    	parentStage.widthProperty().addListener(stageSizeListener);
    	parentStage.heightProperty().addListener(stageSizeListener);
    	
    	//TODO Do the other menu items like this
//    	addDeviceMenu.setOnAction(e->setupController("device"));
//    	addModuleMenu.setOnAction(e->setupController("module"));
////    	addSensorMenu.setOnAction(e->setupController("sensor"));
////    	addActuatorMenu.setOnAction(e->setupController("actuat"));
//    	addEdgeMenu.setOnAction(e->setupController("edgeFull"));
//    	
    	gc=topoField.getGraphicsContext2D();
    	gc.setTextAlign(TextAlignment.CENTER);
    	gc.setFont(new Font(_SpecHandler.font, _SpecHandler.fontSize));
    	_SpecHandler.gc=this.gc;
    	scene.setOnKeyPressed(this);
//    	ObservableList<String> items = FXCollections.observableArrayList();
//    	items.add("Edgewards");
//    	items.add("Cloud-Only");
//		PolicyChoiceMain.setItems(items);
 		
 		Stream.of("Edgewards", "Cloud-Only").forEach(
 				s->PolicyChoiceMain.getItems().add(s));
 		PolicyChoiceMain.setValue(PolicyChoiceMain.getItems().get(0));
 		
 		Stream.of("device", "module", "sensor", "actuat", "edgeFull", "edgeSimple").forEach(
 				s->loadersList.put(s, getClass().getResource("UI_"+s+".fxml")));
 		printDebug("setupListeners ran\n");
    }
    
	private void redrawNodes() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, topoField.getWidth(), topoField.getHeight());
		
		_SpecHandler.nodesList.forEach(n->n.drawLink());
		_SpecHandler.nodesList.forEach(n->n.drawNode());
	}
    boolean mouseL=false;
    boolean mouseR=false;
    boolean mouseM=false;
    NodeSpec draggingNode = null;
    static NodeSpec linkSrcNode = null;
    static NodeSpec selNode = null;
	static EdgeSpec selEdge = null;
	VRGameFog vrgame = new VRGameFog();
    
	@Override
	public void handle(KeyEvent event) {
		boolean keySetSuccess=false;
		switch (event.getCode()){
//			case C	: System.out.println(event.isControlDown()?"C+Ctrl":"C-"); keySetSuccess=InteractionState.trySetKey(event.getCode(), event.isControlDown()); break; // Copy Selected
//			case V	: System.out.println(event.isControlDown()?"C+Ctrl":"V-"); keySetSuccess=InteractionState.trySetKey(event.getCode(), event.isControlDown()); break; // Copy Selected
//			case Z 	: System.out.println(event.isControlDown()?"C+Ctrl":"Z-"); keySetSuccess=InteractionState.trySetKey(event.getCode(), event.isControlDown()); break; // Undo Last Action
			default : keySetSuccess=InteractionState.trySetKey(event.getCode());
		}
		if(!keySetSuccess) return;
		switch (InteractionState.getSetKey()){
			case ESCAPE	: _SpecHandler.deselectAll(); break;
			case F5		: System.out.println("Unimplemented save tool"); break; // Save File
			case DELETE : System.out.println("Unimplemented delete tool"); break; // Copy Selected
			case E		: System.out.println("Unimplemented edit tool"); break; // Copy Selected
			case C		: System.out.println("Unimplemented copy tool"); break; // Copy Selected
			case V		: System.out.println("Unimplemented paste tool"); break; // Paste Selected
			case Z		: System.out.println("Unimplemented undo tool"); break; // Undo Last
			default : {}
		}
		redrawNodes();
	}
	
	public static enum InteractionState {
	    LEFT_BTN, MIDDLE_BTN, RIGHT_BTN, NONE;
		public static KeyCode setKey = KeyCode.ESCAPE;
		public static InteractionState lastMouseState = NONE;

		public static KeyCode getSetKey() { return setKey; }
		public static boolean trySetKey(KeyCode _key) {
			if (lastMouseState==NONE) setKey=_key;
//			System.out.println("Tried setting key " + _key + " Success Flag " + (setKey==_key));
			return setKey==_key;
		}
		public static boolean trySetKey(KeyCode _key, boolean isModifierDown) {
			return (isModifierDown)?trySetKey(_key):isModifierDown;
		}
		public static InteractionState checkMouseBtn(boolean isBtnDown, InteractionState checkState) {
			if(isBtnDown) {lastMouseState = checkState; return checkState;}
	    	else if(lastMouseState==checkState) {lastMouseState=NONE; return checkState;}
	    	else return null;
		}
	    public static InteractionState getMouseState(MouseEvent mEvent){
	    	if(checkMouseBtn(mEvent.isPrimaryButtonDown(), LEFT_BTN)!=null) return LEFT_BTN;
	    	if(checkMouseBtn(mEvent.isMiddleButtonDown(), MIDDLE_BTN)!=null) return MIDDLE_BTN;
	    	if(checkMouseBtn(mEvent.isSecondaryButtonDown(), RIGHT_BTN)!=null) return RIGHT_BTN;
            return NONE;
	    }
	}
	
	@FXML
	private void mouseClickHandler(MouseEvent mEvent) {
        switch(InteractionState.getMouseState(mEvent)) {
			case LEFT_BTN:
				draggingNode=null;
				selEdge = _SpecHandler.getEdge(mEvent);
				selNode = _SpecHandler.getNode(mEvent);
				if (selNode!=null) selNode.setSelected();
				else if(selEdge!=null) selEdge.setSelected();
				switch(InteractionState.getSetKey()) {
					case ESCAPE : draggingNode=selNode; _SpecHandler.deselectAll(); break;
					case DIGIT1 : draggingNode=(selNode==null)?new NodeSpec("device", mEvent):selNode; break;// Select Device Placer
					case DIGIT2 : draggingNode=(selNode==null)?new NodeSpec("module", mEvent):selNode; break;// Select Module Placer
					case DIGIT3 : draggingNode=(selNode==null)?new NodeSpec("sensor", mEvent):selNode; break;// Select Sensor Placer
					case DIGIT4 : draggingNode=(selNode==null)?new NodeSpec("actuat", mEvent):selNode; break;// Select Actuat Placer
					case DIGIT5 : draggingNode=new NodeSpec("linker", mEvent); linkSrcNode=(selNode==null)?null:selNode.addLink(new EdgeSpec(selNode, draggingNode, 0));break;
					default : {} // Nothing
				} break;
//			case MIDDLE_BTN: screenPanHandler(); break; //TODO Screen panning is pretty necessary
			case MIDDLE_BTN: showOutput(); break;
			case RIGHT_BTN: break; //TODO If we have time, make a little right-click menu show with two options: Edit, Delete
			case NONE: System.out.println("_MainWindowController.java: ClickHandler Error - State NONE"); break;
			default: System.out.println("_MainWindowController.java: ClickHandler Error - State Default"); break;
        }
        redrawNodes();
	}
    
    private NodeSpec makeReqLink(NodeSpec src, NodeSpec dst) {
		//check the types of the src and dst and call the full edge controller or the mini one
    	System.out.println();
    	if(src==null || dst==null) return null;
		if(!_SpecHandler.nodesList.contains(src) && !_SpecHandler.nodesList.contains(dst)) return null;
		if((src.type.equals("device") && dst.type.equals("device")) 
				||(src.type.equals("sensor") && dst.type.equals("device"))
				||(src.type.equals("device") && dst.type.equals("actuat"))) {
			printDebug("Setting up controller with edgeSimple");
			EdgeSpec e = (EdgeSpec)setupController("edgeSimple");
			if(e != null) src.addLink(e);
		}else if((src.type.equals("module") && dst.type.equals("module")) 
				||(src.type.equals("sensor") && dst.type.equals("module"))
				||(src.type.equals("module") && dst.type.equals("actuat"))) {
			EdgeSpec e = (EdgeSpec) setupController("edgeFull");
			if(e != null) src.addLink(e);
		}
		else if(src.type.equals("module") && dst.type.equals("device")) src.deviceModuleLink(dst.name);
		return src;
	}
    
	@FXML
    private void mouseReleaseHandler(MouseEvent mEvent) throws IOException {
		switch(InteractionState.getMouseState(mEvent)) {
			case LEFT_BTN:
//				System.out.println("In Left: KeyState: " + InteractionState.setKey);
				Spec newNode = null;
				draggingNode=(draggingNode!=null && draggingNode.isTemp)?draggingNode.pop():null;
				selEdge = _SpecHandler.getEdge(mEvent);
				selNode = _SpecHandler.getNode(mEvent);
				switch(InteractionState.getSetKey()) {
					case DIGIT1 : newNode = (selNode!=null)?null:(NodeSpec)setupController("device"); break;
					case DIGIT2 : newNode = (selNode!=null)?null:(NodeSpec)setupController("module"); break;
					case DIGIT3 : newNode = (selNode!=null)?null:(NodeSpec)setupController("sensor"); break;
					case DIGIT4 : newNode = (selNode!=null)?null:(NodeSpec)setupController("actuat"); break;
					case DIGIT5 : linkSrcNode = (linkSrcNode==null)?null:makeReqLink(linkSrcNode,selNode); _SpecHandler.pruneLinks(); break;

//					case DIGIT5 : makeReqLink(linkSrcNode, selNode); _SpecHandler.pruneLinks(); break;
					default : {} // Nothing
				}
				if (newNode!=null) ((NodeSpec)newNode.setSelected()).setPos(mEvent);
			break;
			case MIDDLE_BTN:
			break;
			case NONE:
			break;
			case RIGHT_BTN:
			break;
			default:
			break;
		}
    	redrawNodes();
	}
	
	@FXML
    private void mouseMoveHandler(MouseEvent mEvent) {
    	if (draggingNode != null&&!draggingNode.selected) draggingNode.setSelected();
    	if(draggingNode != null) {
    		draggingNode.setPos(mEvent);
			redrawNodes();
    	}
    }
	
    private void screenPanHandler() {
		// TODO Shift the position of everything
		
	}
    
    @FXML
	private void mouseScrollHandler(ScrollEvent event) {
    	_SpecHandler.shiftPositionsByZoom(event);
    	redrawNodes();
    }
    
	private void screenSizeChangeHandler(Stage parentStage) {
		topoField.widthProperty().bind(parentStage.widthProperty());
		topoField.heightProperty().bind(parentStage.heightProperty());
		double w = topoField.getWidth(); double h = topoField.getHeight();
		xCenter=0.5*w; yCenter=0.5*h;
    	gc.setFill(Color.WHITE); gc.fillRect(0, 0, w, h);
    }
    
    String selectedJSON="test9.json";
    String policy = "Edgewards";
    int simTime = 1000;
    int simGranu = 10;
    
	@FXML
	    void startSim(ActionEvent event) throws Exception {
	    	String policy = setParamsController.policyType;
	    	int time = setParamsController.simulationTime;
	    	int granularity = setParamsController.granularityMetric;
	    	String centralNode = getCentralNode();
	    	_SpecHandler.writeJSON(selectedJSON, time, granularity, policy, centralNode);
	//		vrgame.createFogSimObjects(true, "Edgeward", 10, 10000);
	//     	FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("SimOutputBox.fxml"));
	//        Scene scene = new Scene(addNewNodeLoader.load(),900,600);
	//        Stage stage = new Stage();
	//        stage.setScene(scene);
	//        stage.setTitle("Output");
	//        stage.showAndWait();
	    }

	@FXML
    void newJSON(ActionEvent event) throws IOException {
		FXMLLoader root = new FXMLLoader(getClass().getResource("SaveFileBox.fxml"));
		Scene scene = new Scene(root.load(),414,139);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Save File");
		stage.showAndWait();
		String reqName=SaveFileController.jsonDestinationFileName;
		if(reqName!=null) selectedJSON=reqName + ".json";
		System.out.println("Trying to save to "+ selectedJSON);
		writeJSON();
    }
	
	void writeJSON() {
		//TODO change this so that the meta-specs are actually inside the SetupJSONParser
		//and we only set from here on change
		//So ideally just JSONHandler.writeJSON();
		
//     	
	}
	
	//TODO Change Parse[X] to be methods of the respective classes
	//i.e. a "[X]Spec fromJSON(JSONObject){}"
	//like existing "JSONObject toJSON(this)"
	@FXML
	void loadJson(ActionEvent event) throws FileNotFoundException, IOException, ParseException {
		Stage stage = new Stage();
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open JSON File");
		chooser.setInitialDirectory(new File("saves"));
		File selectedFile = chooser.showOpenDialog(stage);
		if (selectedFile != null) _SpecHandler.loadJSON(selectedFile);
	}
	
    @FXML
    void showOutput() {
        try {
            FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("SimOutputBox.fxml"));
            Scene scene = new Scene(addNewNodeLoader.load(),900,600);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Output");
            stage.showAndWait();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	Spec newNode = null;
    Spec addNodeType(String _type) {
    	if(_type.equals("device")) newNode = (Spec)setupController(_type);
		if(_type.equals("module")) newNode = (Spec)setupController(_type);
		if(_type.equals("sensor")) newNode = (Spec)setupController(_type);
		if(_type.equals("actuat")) newNode = (Spec)setupController(_type);
		return newNode;
    }
    
//    Spec setupController(String type, int w, int h) {
//    	FXMLLoader loader = new FXMLLoader(getClass().getResource(type+"InputBox.fxml"));
//		Scene scene = null;
//		try {
//			scene = new Scene(loader.load(),w,h);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Stage stage = new Stage();
//		stage.setScene(scene);
//		Controller controller = loader.getController();
//		stage.setTitle("Add "+type+" Node");
//		stage.showAndWait();
//		redrawNodes();
//		return controller.getSpec();
//    }
    
    Spec setupController(String type) {
    	printDebug("Starting setupController with type " + type + ", file " + loadersList.get(type).toString());
		FXMLLoader loader = new FXMLLoader(loadersList.get(type));
		try {
			Scene scene = new Scene(loader.load());
			_SubController controller = loader.getController();
			Stage stage = new Stage();
			stage.setScene(scene);
			
			Spec prevState=null;
			if (_SpecHandler.getSelected() != null)
				prevState = _SpecHandler.getSelected().type.equals(type) ? _SpecHandler.getSelected() : null;
			
			Spec specPtr = controller.init(prevState);
			
			stage.addEventHandler(KeyEvent.KEY_PRESSED,  (event) -> {
			    switch(event.getCode().getCode()) {
			    	case 27: controller.recover(); stage.close(); break; //Esc->Canceled action->If editing, reset the node to prev vals, if new, pop the node
			    	case 116: printDebug("ss"); break;
			        default:  {printDebug(event.getCode().getCode()+"");}
			    }
			});
			
			stage.setOnCloseRequest(e -> stage.close());
			
			stage.showAndWait();
			redrawNodes();
			return controller.spec;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    @FXML
    void editHandler() {
		System.out.println("Pre Edit there are " + _SpecHandler.nodesList.size() + " nodes");
    	Spec selectedNode = (Spec)_SpecHandler.getSelected();
    	if(selectedNode == null) return;
    	setupController(selectedNode.type);
		_SpecHandler.pruneLinks();
		redrawNodes();
    }
    
    @FXML
    void deleteHandler() {
    	((NodeSpec)_SpecHandler.getSelected()).pop();
		redrawNodes();
    }
    
    @FXML
    void exitApp(ActionEvent event) {
    	Stage stage;
    	stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
    	stage.close();
    }
    
    @FXML
    void displaySelected(MouseEvent event) {
    	String device = policyList.getSelectionModel().getSelectedItem();
    	if(device == null || device.isEmpty()) {
    		deviceField.setText("");
    	}else {
    		deviceField.setText(device);
    	}
    }
    
    @FXML
	void savePolicyHandler(ActionEvent event) {
		String policyType;
		if (PolicyChoiceMain.getSelectionModel().getSelectedItem() == null
				|| PolicyChoiceMain.getSelectionModel().getSelectedItem().trim().isEmpty()) {
			PolicyChoiceMain.setValue("Edgewards");
		} else
			policyType = PolicyChoiceMain.getSelectionModel().getSelectedItem().toString();
	}
    
	@FXML
    void confirmPolicy(ActionEvent event) {
    	try {
    		BorderPane root = FXMLLoader.load(getClass().getResource("PolicySelectionBox.fxml"));
    		Scene scene = new Scene(root,276,200);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		stage.setTitle("Add Sensor");
    		stage.show();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void setParams(ActionEvent event) {
    	try {
    		List<String> placementPolicyList = new ArrayList<String>();
    		placementPolicyList.add("Profit Aware Policy");
        	placementPolicyList.add("Quality of Service");
        	placementPolicyList.add("Lowest Power Usage");
        	
        	FXMLLoader dataFXML = new FXMLLoader(getClass().getResource("setSimParamsBox.fxml"));
			Scene scene = new Scene(dataFXML.load(),414,210);
			Stage stage = new Stage();
			stage.setScene(scene);
			setParamsController controller = dataFXML.getController();
			controller.populateList(placementPolicyList);
     		stage.setTitle("Setting Parameters");    		
     		stage.showAndWait();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    void testmethod() {
    	double testgran = org.fog.utils.Config.RESOURCE_MGMT_INTERVAL;
    	int testsimtime = org.fog.utils.Config.MAX_SIMULATION_TIME;
    	String testtopnode = org.fog.utils.Config.TOP_NODE;
    	System.out.print("TEST Maxtime: " + testsimtime);
    	System.out.print("\n TEST Gran: " + testgran);
    	System.out.print("\n TEST Top Node: " + testtopnode);
    }
    
    //TODO Fix this
    public String getCentralNode() {
//    	for(NodeSpec nodes)
    	return "cloud";
    }
    
    @FXML
    void startPreview(ActionEvent event) throws Exception {
    	vrgame.createFogSimObjects(false, "Edgeward", 10, 10000);
    	TreeItem<String> devices = new TreeItem<>("Devices");
		for(placementObject o : ModulePlacement.placementList) {
    		TreeItem<String> parent1 = new TreeItem<>(o.device);
    		for(String m : o.module) {
    			TreeItem<String> item = new TreeItem<>(m);
    			parent1.getChildren().add(item);
    		}
//    		TreeParentList.add(parent1);
    		devices.getChildren().add(parent1);
    	}
		treeView.setRoot(devices);
    	devices.setExpanded(true);
    }
    
    @FXML
    public void createJson(ActionEvent event) {
    	try {
    		BorderPane root = FXMLLoader.load(getClass().getResource("createJsonBox.fxml"));
    		Scene scene = new Scene(root,414,139);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		stage.setTitle("Create New Design File");
    		stage.show();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}
    
    @FXML
    void resetCache(ActionEvent event) {
    	System.out.println("resetting all text files");
    	FileWriter file_writer;
        try {
            file_writer = new FileWriter("instances.txt",false);
            file_writer = new FileWriter("edges.txt",false);
            file_writer = new FileWriter("modules.txt",false);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
         //   buffered_Writer.write(line);
            buffered_Writer.flush();
            buffered_Writer.close();
        } catch (IOException e) {
            System.out.println("Add line failed!" +e);
        }
    }
    
    @FXML
    public void exitApplication(ActionEvent event) {
       Platform.exit();
    }
}