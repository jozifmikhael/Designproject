package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.entities.FogBroker;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.placement.Controller;
import org.fog.placement.ModuleMapping;
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

import application.SetupJSONParser.*;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
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
import org.fog.placement.Controller;
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
    
    ////////////////////////////////////////////////////////////////////////////
    
//	double R=50;
	double xCenter=100;
	double yCenter=100;
	
    GraphicsContext gc;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    }
    
    

	public int state = 1;
	@Override
	public void handle(KeyEvent event) {
//		System.out.print("_MainWindowController.java: KeyPress ");
		switch (event.getCode()){
			case ESCAPE : state=0; break;	 // Select Pointer Tool | Escape Menu Without Saving
			case DIGIT1 : state=1; break;	 // Select Node Placer
			case DIGIT2 : state=2; break;	 // Select Module Placer
			case DIGIT3 : state=3; break;	 // Select Edge Placer
			case DIGIT4 : state=4; break;    // Select Sensor Placer
			case DIGIT5 : state=5; break;    // Select Actuator Placer
			case Z 		: System.out.println("Z"); break;	 // Undo Last Action
			case E 		: System.out.println("E"); break;	 // Edit Object Selected
			case F5 	: System.out.println("F5"); break;   // Save File
			case DELETE : System.out.println("Del"); break;  // Delete Object Selected
			default : {} // Nothing
		}
	}
	public List<String> selectedModulesList = new ArrayList<String>();
    
	public SetupJSONParser specsHandler;
    public void setupListeners(Stage parentStage, Scene scene) {
        specsHandler = new SetupJSONParser();
    	ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue)->screenSizeChangeHandler();
    	parentStage.widthProperty().addListener(stageSizeListener);
    	parentStage.heightProperty().addListener(stageSizeListener);
    	gc = topoField.getGraphicsContext2D();
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(new Font(SetupJSONParser.font, SetupJSONParser.fontSize));
    	scene.setOnKeyPressed(this); // uses handle method
    }
    
    @FXML
	private void mouseScrollHandler(ScrollEvent event) {
    	specsHandler.shiftPositionsByZoom(event);
    }
	private void redrawNodes() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, topoField.getWidth(), topoField.getHeight());
		for (EdgeSpec edge : SetupJSONParser.edgesList) edge.draw(gc);
		for (NodeSpec node : SetupJSONParser.nodesList) node.draw(gc);
	}
    
    boolean mouseL=false;
    boolean mouseR=false;
    boolean mouseM=false;
    NodeSpec draggingNode = null;
    NodeSpec linkSrcNode = null;
    @FXML
	private void mouseClickHandler(MouseEvent mEvent) {
        mouseL|=mEvent.isPrimaryButtonDown();
        mouseR|=mEvent.isSecondaryButtonDown();
        mouseM|=mEvent.isMiddleButtonDown();
        
//    	System.out.println(" L:"+mouseL+" M:"+mouseM+" R:"+mouseR);
    	if(mouseL) {
			NodeSpec selNode = specsHandler.getNode(mEvent);
			if(selNode==null) {
				switch(state) {
					case 1: draggingNode = specsHandler.new NodeSpec("device", mEvent); break;
					case 2: draggingNode = specsHandler.new NodeSpec("module", mEvent); break;
					case 3: draggingNode = specsHandler.new NodeSpec("sensor", mEvent); break;
					case 4: draggingNode = specsHandler.new NodeSpec("actuator", mEvent); break;
					case 5: System.out.println("src empty");
					default: draggingNode = selNode; break;
				}
			}else if (state==5) linkSrcNode = selNode;
			else draggingNode = selNode;
	    }
    	if(mouseM) {
    		screenPanHandler();
    	}
		redrawNodes();
	}
    
    private void screenPanHandler() {
		// TODO Shift the position of everything
		
	}

	@FXML
    private void mouseReleaseHandler(MouseEvent mEvent) throws IOException {
    	if(mouseL) {
    		mouseL=false;
    		if (draggingNode!=null) {draggingNode.setPos(mEvent);}
			NodeSpec newNode = null;
			switch(state) {
				case 1: newNode=addDevice(); break;
				case 2: newNode=addModule(); break;
				case 3: newNode=addSensor(); break;
				case 4: newNode=addActuat(); break;
				case 5: newNode=addActuat(); break;
				default: break;
			}
			if (draggingNode!=null) {draggingNode.pop(); draggingNode=null;}
			if(newNode!=null) newNode.setPos(mEvent);
    		System.out.println();
    	}
    	redrawNodes();
//    	
//		if (state == 3) {
//			if(linkSrcNode!=null && linkDstNode!=null) {
//				String srcType = linkSrcNode.data.type;
//				String dstType = linkSrcNode.data.type;
//				if(srcType.equals(dstType)) {
//					if(srcType.equals("device")) {
//						DeviceSpec srcDev = getDevice(linkSrcNode.name);
//						srcDev.parent = linkDstNode.name;
//						DeviceSpec dstDev = getDevice(srcDev.parent);
//						dispLink srcLink = getLinkBySrc(linkSrcNode.name);
//			        	FXMLLoader dataFXML = new FXMLLoader(getClass().getResource("LinkLatencyInputBox.fxml"));
//						Scene scene = new Scene(dataFXML.load(),414,139);
//						Stage stage = new Stage();
//						stage.setScene(scene);
//			     		stage.setTitle("Setting Link Latency");
//			     		stage.showAndWait();
//			     		double selLatency=LinkLatencyInputController.LinkLatencyValue;
//						if(srcLink!=null)srcLink.dst = linkDstNode;
//						else dispLinksList.add(new dispLink(srcDev, dstDev));
//					}else if(srcType.equals("module")) {
//						selectedModulesList.add(linkDstNode.name);
//						selectedModulesList.add(linkSrcNode.name);
//						addEdge();
//					}
//				} else if(srcType.equals("sensor")&&dstType.equals("modules")) {
//					System.out.println("Sensor-Node link detected");
//				} else System.out.println("_MainWindowController.java: Linker can't form Node-Module links");
//			} else if (linkSrcNode==null) {
//				System.out.println("_MainWindowController.java: Linker Src is null");
//			} else if (linkDstNode==null) {
//				System.out.println("_MainWindowController.java: Linker Dst is null");
//			}
//    	}
	}
    
    @FXML
    private void mouseMoveHandler(MouseEvent mEvent) {
    	if(draggingNode != null) {
//    		System.out.println("Dragging Node not null");
    		draggingNode.setPos(mEvent);
			redrawNodes();
    	}
    }
    
	private void screenSizeChangeHandler() {
    	double w=backPane.getWidth(); double h=backPane.getHeight();
		xCenter=0.5*w; yCenter=0.5*h;
		topoField.setWidth(w); topoField.setHeight(h);
    	gc.setFill(Color.WHITE); gc.fillRect(0, 0, w, h);
		redrawNodes();
    }
    
    String selectedJSON="test9.json";
    String policy = "Edgewards";
    int simTime = 1000;
    int simGranu = 10;
    
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
		String policy = setParamsController.policyType;
    	int time = setParamsController.simulationTime;
    	int granularity = setParamsController.granularityMetric;
    	String centralNode = getCentralNode();
     	specsHandler.writeJSON(selectedJSON, time, granularity, policy, centralNode);
	}
	
	//TODO Change Parse[X] to be methods of the respective classes
	//i.e. a "[X]Spec fromJSON(JSONObject){}"
	//like existing "JSONObject toJSON(this)"
	@FXML
	void loadJson(ActionEvent event) throws FileNotFoundException, IOException, ParseException {
		Stage stage = new Stage();
		//TODO FileChooser?
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open JSON File");
		chooser.setInitialDirectory(new File("saves"));
		File selectedDirectory = chooser.showOpenDialog(stage);
		if (selectedDirectory != null) {
			System.out.println("Loaded Json: "+selectedDirectory.getName());
			JSONObject jsonObject = (JSONObject)new JSONParser().parse(new FileReader(selectedDirectory.getName()));
				
			JSONArray devicesList = (JSONArray) jsonObject.get("nodes");
			JSONArray actuatsList = (JSONArray) jsonObject.get("actuats");
			JSONArray modulesList = (JSONArray) jsonObject.get("modules");
			JSONArray sensorsList = (JSONArray) jsonObject.get("sensors");
			JSONArray edgesList = (JSONArray) jsonObject.get("edges");	
		}
	}
    @FXML
    void showOutput(ActionEvent event) {
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
    @FXML
    ActuatSpec addActuat() {
    	try {
    		FXMLLoader addNewActuatorLoader = new FXMLLoader(getClass().getResource("ActuatorBox.fxml"));
    		Scene scene = new Scene(addNewActuatorLoader.load(),264,133);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		ActuatorInputController actuatorController = addNewActuatorLoader.getController();
    		actuatorController.initialize();
    		stage.setTitle("Add Actuator");
    		stage.showAndWait();
    		ActuatSpec a = actuatorController.getSpec();
			redrawNodes();
    		return a;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @FXML
    DeviceSpec addDevice() {
    	try {
    		FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("DeviceInputBox.fxml"));
    		Scene scene = new Scene(addNewNodeLoader.load(),450,320);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AddDeviceController controller = addNewNodeLoader.getController();
    		stage.setTitle("Add Device Node");
    		stage.showAndWait();
    		DeviceSpec d = controller.getSpec();
			redrawNodes();
    		return d;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    DeviceSpec editDevice = null;
    
    @FXML
    NodeSpec editHandler(ActionEvent event) {
    	NodeSpec selectedNode = SetupJSONParser.getSelected();
    	if(selectedNode == null) return null;
		if(selectedNode.type.equals("device")) {
			editDevice = getDevice();
			if(editDevice == null) return null;
			DeviceSpec tryEditNode = addDevice();
			if (tryEditNode == null) {
				SetupJSONParser.devicesList.add(editDevice);
				return null;
			}
			afterEditNode.x = editDevice.x;
			afterEditNode.y = editDevice.y;
			dispNode oldDispNode = getDispNode(editDevice.name);
			dispLink oldLink = getDispLink(editDevice.parent, editDevice.name);
			for(dispLink l: dispLinksList) 
				if(l.src.equals(oldDispNode))l.src = afterEditNode;
			if(oldDispNode != null)dispNodesList.remove(oldDispNode);
			if(oldLink != null) dispLinksList.remove(oldLink);
			redrawNodes();
			editDevice = null;
		}
    }
    
    @FXML
    ModuleSpec addModule() {
    	try {
    		FXMLLoader dataFXML = new FXMLLoader(getClass().getResource("ModuleInputBox.fxml"));
    		Scene scene = new Scene(dataFXML.load(),414,346);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AddModuleController controller = dataFXML.getController();
    		controller.initialize(selectedModule, specsHandler);
    		stage.setTitle("Add Module");
    		stage.showAndWait();
    		ModuleSpec m = controller.getSpec();
    		redrawNodes();
    		return m;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @FXML
    SensorSpec addSensor() {
    	try {
    		FXMLLoader addNewSensorLoader = new FXMLLoader(getClass().getResource("SensorBox.fxml"));
    		Scene scene = new Scene(addNewSensorLoader.load(),450,400);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AddSensorController sensorController = addNewSensorLoader.getController();
    		sensorController.populateList(SetupJSONParser.devicesList);
    		stage.setTitle("Add Sensor");
    		stage.showAndWait();
    		SensorSpec s = sensorController.getSpec();
			redrawNodes();
    		return s;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @FXML
    EdgeSpec addEdge() {
	    try {
			FXMLLoader dataFXML = new FXMLLoader(getClass().getResource("EdgeInputBox.fxml"));
			Scene scene = new Scene(dataFXML.load(),414,346);
			Stage stage = new Stage();
			stage.setScene(scene);
			AddEdgeController controller = dataFXML.getController();
			if(selectedModulesList.isEmpty()) controller.populateList(SetupJSONParser.modulesList);
			else controller.setChoices(selectedModulesList);
			selectedModulesList.removeAll(selectedModulesList);
			stage.setTitle("Add App Edge");
			stage.showAndWait();
    		EdgeSpec v = controller.getSpec();
    		redrawNodes();
    		return v;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    @FXML
    void deleteHandler(ActionEvent event) {
    	
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
    
    public String getCentralNode() {
    	return "cloud";
    }
    
    @FXML
    void startSim(ActionEvent event) throws Exception {
//    	writeJSON();
     	FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("SimOutputBox.fxml"));
        Scene scene = new Scene(addNewNodeLoader.load(),900,600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Output");
        stage.showAndWait();
        
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