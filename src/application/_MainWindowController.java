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
    
    int globalID=0;
	double R=50;
	double zoomFactor=1;
	double xCenter=100;
	double yCenter=100;
	int fontSize = 16;
	String font = "monospaced";
	Color deviceColor=Color.RED;
	Color moduleColor=Color.CYAN;
	Color sensorColor=Color.PINK;
	Color actuatorColor=Color.ORANGE;
	Color transpColor=Color.TRANSPARENT;
	Color _errorColor=Color.GREEN;
    GraphicsContext gc;
    SetupJSONParser textfile = new SetupJSONParser();
	
	public List<String> selectedModulesList = new ArrayList<String>();
	
	public List<SensorSpec> sensorsList = new ArrayList<SensorSpec>();
	public List<ActuatorSpec> actuatorsList = new ArrayList<ActuatorSpec>();
	public List<LinkSpec> linksList = new ArrayList<LinkSpec>();
	
	public List<DeviceSpec> devicesList = new ArrayList<DeviceSpec>();
	public List<ModuSpec> modulesList = new ArrayList<ModuSpec>();
	public List<ModuEdgeSpec> moduleEdgesList = new ArrayList<ModuEdgeSpec>();

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
    
    public void setupListeners(Stage parentStage, Scene scene) {
    	ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue)->screenDragHandler();
    	parentStage.widthProperty().addListener(stageSizeListener);
    	parentStage.heightProperty().addListener(stageSizeListener);
    	gc = topoField.getGraphicsContext2D();
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(new Font(font, fontSize));
    	scene.setOnKeyPressed(this); // uses handle method
    }
    
    @FXML
	private void mouseScrollHandler(ScrollEvent event) {
    	System.out.println(event.getDeltaY() +" "+ event.getX() +" "+ event.getY());
    	zoomFactor+=(event.getDeltaY()>0)?0.05:-0.05;
    	System.out.println(zoomFactor);
    	redrawNodes();
    }
	
    private void redrawNodes() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, topoField.getWidth(), topoField.getHeight());
		for(dispLink link : textfile.dispLinksList) link.draw(gc);
		if (draggingLink!=null) draggingLink.draw(gc);
		for(dispNode node : textfile.dispNodesList) node.draw(gc);
		if (draggingNode!=null) draggingNode.draw(gc);
	}

	dispNode draggingNode = null;
	dispLink draggingLink = null;
	dispNode linkSrcNode = null;
    @FXML
	private void mouseClickHandler(MouseEvent mEvent) {
		dispNode selNode = getNodeOnClick(mEvent);
		if (state == 0) {
			draggingNode = selNode;
		} else if (state == 1) {
			if (selNode == null) {
				dispNode newNode = textfile.createDispNode("New Node", deviceColor, mEvent.getX(), mEvent.getY(), R*2*zoomFactor);
				draggingNode = newNode;
			} else {
				draggingNode = selNode;
			}
		} else if (state == 2) {
			if (selNode == null) {
				dispNode newNode = textfile.createDispNode("New Module", moduleColor, mEvent.getX(), mEvent.getY(), R*2*zoomFactor);
				draggingNode = newNode;
			} else {
				draggingNode = selNode;
			}
		} else if (state == 3) {
			linkSrcNode = selNode;
			dispNode newNode = textfile.createDispNode("", transpColor, mEvent.getX(), mEvent.getY(), R*2*zoomFactor);
			draggingNode = newNode;
			draggingLink = textfile.createDispLink(linkSrcNode, draggingNode);
		} else if (state == 4) {
			if (selNode == null) {
				dispNode newNode = textfile.createDispNode("New Sensor", sensorColor, mEvent.getX(), mEvent.getY(), R*2*zoomFactor);
				draggingNode = newNode;
			} else {
				draggingNode = selNode;
			}
		} else if (state == 5) {
			if (selNode == null) {
				dispNode newNode = textfile.createDispNode("New Actuator", actuatorColor, mEvent.getX(), mEvent.getY(), R*2*zoomFactor);
				draggingNode = newNode;
			} else {
				draggingNode = selNode;
			}
		}		
		redrawNodes();
	}
   
	@FXML
    private void mouseReleaseHandler(MouseEvent mEvent) throws IOException {
    	if (state==0) {
    		if(textfile.dispNodesList.indexOf(draggingNode)>=0)draggingNode.setPos(mEvent);
    	} else if (state==1) {
    		if(textfile.dispNodesList.indexOf(draggingNode)<0) {
    			dispNode newDevice=addDevice();
    			if(newDevice!=null) newDevice.setPos(mEvent);
    		} else draggingNode.setPos(mEvent);
        	draggingNode=null;
    	} else if (state==2) {
    		if(textfile.dispNodesList.indexOf(draggingNode)<0) {
    			dispNode newModule = addModule();
    			if(newModule!=null) newModule.setPos(mEvent);
    		} else draggingNode.setPos(mEvent);
        	draggingNode=null;
    	} else if (state == 3) {
        	draggingLink=null;
        	draggingNode=null;
    		dispNode linkDstNode = getNodeOnClick(mEvent);
			if(linkSrcNode!=null && linkDstNode!=null) {
				String srcType = linkSrcNode.data.type;
				String dstType = linkSrcNode.data.type;
				if(srcType.equals(dstType)) {
					if(srcType.equals("device")) {
						DeviceSpec srcDev = getDevice(linkSrcNode.name);
						srcDev.parent = linkDstNode.name;
						DeviceSpec dstDev = getDevice(srcDev.parent);
						dispLink srcLink = getLinkBySrc(linkSrcNode.name);   				        	
			        	FXMLLoader dataFXML = new FXMLLoader(getClass().getResource("LinkLatencyInputBox.fxml"));
						Scene scene = new Scene(dataFXML.load(),414,139);
						Stage stage = new Stage();
						stage.setScene(scene);							
			     		stage.setTitle("Setting Link Latency");    		
			     		stage.showAndWait();
			     		double selLatency=LinkLatencyInputController.LinkLatencyValue;
						if(srcLink!=null)srcLink.dst = linkDstNode;
						else textfile.dispLinksList.add(textfile.createDispLink(srcDev, dstDev));
					}else if(srcType.equals("module")) {
						textfile.selectedModulesList.add(linkDstNode.name);
						textfile.selectedModulesList.add(linkSrcNode.name);
						selectedModulesList.add(linkDstNode.name);
						selectedModulesList.add(linkSrcNode.name);
						addEdge();
					}
				} else if(srcType.equals("sensor")&&dstType.equals("modules")) {
					System.out.println("Sensor-Node link detected");
				} else System.out.println("_MainWindowController.java: Linker can't form Node-Module links");
			} else if (linkSrcNode==null) {
				System.out.println("_MainWindowController.java: Linker Src is null");
			} else if (linkDstNode==null) {
				System.out.println("_MainWindowController.java: Linker Dst is null");
			}
		} else if (state==4) {
    		if(textfile.dispNodesList.indexOf(draggingNode)<0) {
    			dispNode newSensor = addSensor();
    			if(newSensor!=null) newSensor.setPos(mEvent);
    		} else draggingNode.setPos(mEvent);
        	draggingNode=null;
		} else if (state==5) {			
    		if(textfile.dispNodesList.indexOf(draggingNode)<0) {
    			dispNode newActuator = addActuator();
    			if(newActuator!=null) newActuator.setPos(mEvent);   			
    		}
		}
    	redrawNodes();
    }
    
    @FXML
    private void mouseMoveHandler(MouseEvent mEvent) {
    	if(draggingNode != null){
			draggingNode.x = mEvent.getX();
			draggingNode.y = mEvent.getY();
			redrawNodes();
    	}
    }
    
	private void screenDragHandler() {
    	double w=backPane.getWidth(); double h=backPane.getHeight();
		xCenter=0.5*w; yCenter=0.5*h;
		topoField.setWidth(w); topoField.setHeight(h);
    	gc.setFill(Color.WHITE); gc.fillRect(0, 0, w, h);
		redrawNodes();
    }
	
	private dispNode getNodeOnClick(MouseEvent mEvent) {
		for(dispNode n : textfile.dispNodesList) if(Math.pow(Math.pow(n.x-mEvent.getX(),2)+Math.pow(n.y-mEvent.getY(),2),0.5)<=0.5*n.sz*zoomFactor) return n;
		return null;
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
		String policy = setParamsController.policyType;
    	int time = setParamsController.simulationTime;
    	int granularity = setParamsController.granularityMetric;
    	String centralNode = getCentralNode();
     	textfile.writeJSON(selectedJSON, devicesList, modulesList, moduleEdgesList, sensorsList, linksList, time, granularity, policy, centralNode);
	}
	
	@FXML
	void loadJson(ActionEvent event) {
		JSONParser parser = new JSONParser();
		Stage stage = new Stage();
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open JSON File");
		chooser.setInitialDirectory(new File("saves"));
		File selectedDirectory = chooser.showOpenDialog(stage);
		if (selectedDirectory != null) {
			System.out.println("Loaded Json: "+selectedDirectory.getName());
			try {
				Object obj = parser.parse(new FileReader(selectedDirectory.getName()));
				JSONObject jsonObject = (JSONObject) obj;
				
				JSONArray devicesList = (JSONArray) jsonObject.get("nodes");				
				for (int i = 0; i < devicesList.size(); i++) {
					JSONObject device = (JSONObject) devicesList.get(i);
					parseDeviceObj(device);
				}
				JSONArray edgesList = (JSONArray) jsonObject.get("edges");
				for (int i = 0; i < edgesList.size(); i++) {
					JSONObject edge = (JSONObject) edgesList.get(i);
					parseEdgeObj(edge);
				}
				
				JSONArray modulesList = (JSONArray) jsonObject.get("modules");
				for (int i = 0; i < modulesList.size(); i++) {
					JSONObject module = (JSONObject) modulesList.get(i);
					parseModuleObj(module);
				}
				
				JSONArray sensorsList = (JSONArray) jsonObject.get("sensors");
				for (int i = 0; i < sensorsList.size(); i++) {
					JSONObject sensor = (JSONObject) sensorsList.get(i);
					parseSensorObj(sensor);
				}
				JSONArray actuatorsList = (JSONArray) jsonObject.get("actuators");
				for (int i = 0; i < actuatorsList.size(); i++) {
                    JSONObject actuator = (JSONObject) actuatorsList.get(i);
                    parseSensorObj(actuator);
                }
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	void parseDeviceObj(JSONObject device) throws NumberFormatException, IOException {
		//JSONObject deviceObj = (JSONObject) device.get("nodes");
		String parentname = (String) device.get("parent");
		String nodename = (String) device.get("name");
		double apower = (double) device.get("apower");
		int level = Integer.parseUnsignedInt(device.get("level").toString());
		double rate = (double) device.get("rate");
		double ipower = (double) device.get("ipower");
		int downbw = Integer.parseUnsignedInt(device.get("down_bw").toString());
		int upbw = Integer.parseUnsignedInt(device.get("up_bw").toString());
		int ram =  Integer.parseUnsignedInt(device.get("ram").toString());
		int mips = Integer.parseUnsignedInt(device.get("mips").toString());
		double x_cord = (double) device.get("x_cord");
		double y_cord = (double) device.get("y_cord");
		double size = (double) device.get("radius");
		double latency = 6.0;
		DeviceSpec d = textfile.createDevice(nodename, parentname, mips, ram, upbw, downbw, level, rate, apower, ipower, latency);
		d.x = x_cord;
		d.y = y_cord;
		d.dispSize = size;
		textfile.devicesList.add(d);
		devicesList.add(d);
		dispNode newDevice = textfile.createDispNode(nodename, d, d.x, d.y, d.dispSize);
		textfile.dispNodesList.add(newDevice);
		textfile.dispLinksList.add(textfile.createDispLink(d));
		redrawNodes();
	}
	
	void parseActuatorObj(JSONObject actuator) throws NumberFormatException, IOException {
        String actuatorName = (String) actuator.get("Actuator Name");
        double x_cord = (double) actuator.get("x_cord");
        double y_cord = (double) actuator.get("y_cord");
        double size = (double) actuator.get("radius");
        ActuatorSpec a = textfile.createActuator(actuatorName + " \n");
        a.x = x_cord;
        a.y = y_cord;
        a.dispSize = size;
        textfile.actuatorsList.add(a);
        actuatorsList.add(a);
        dispNode newDevice = textfile.createDispNode(actuatorName, a, a.x, a.y, a.dispSize);
        textfile.dispNodesList.add(newDevice);
        redrawNodes();
    }
	
	void parseSensorObj(JSONObject sensor) throws NumberFormatException, IOException {
		double uniformMax = (double) sensor.get("uniformMax");
		String sensorName = (String) sensor.get("sensorName");
		double normalStdDev = (double) sensor.get("normalStdDev");
		double normalMean = (double) sensor.get("normalMean");
		double latency = (double) sensor.get("latency");
		double deterministicValue = (double) sensor.get("deterministicValue");
		String parentName = (String) sensor.get("parentName");
		double uniformMin = (double) sensor.get("uniformMin");
		double x_cord = (double) sensor.get("x_cord");
		double y_cord = (double) sensor.get("y_cord");
		double size = (double) sensor.get("radius");
		SensorSpec s = textfile.createSensor(sensorName, parentName, latency, deterministicValue, normalMean, normalStdDev, uniformMax, uniformMin);
		s.x = x_cord;
		s.y = y_cord;
		s.dispSize = size;
		textfile.sensorsList.add(s);
		sensorsList.add(s);
		dispNode newSensor =textfile.createDispNode(sensorName, s, s.x, s.y, s.dispSize);
		textfile.dispNodesList.add(newSensor);
		redrawNodes();
	}
	
	void parseEdgeObj(JSONObject edge) throws NumberFormatException, IOException {
		String src = (String) edge.get("src");
		String dest = (String) edge.get("dest");
		double tupleCpuLength = (double) edge.get("tupleCpuLength");
		String edgeType = (String) edge.get("edgeType");
		double periodicity = (double) edge.get("periodicity");
		String tupleType = (String) edge.get("typleType");
		double tupleNwLength = (double) edge.get("tupleNwLength");
		int direction = (int) edge.get("direction");
		ModuEdgeSpec e = textfile.createModuleEdge(dest, src, tupleType, periodicity, tupleCpuLength,
				tupleNwLength, edgeType, direction);
		textfile.moduleEdgesList.add(e);
		moduleEdgesList.add(e);
	}
	
	void parseModuleObj(JSONObject module) throws NumberFormatException, IOException {
		String nodeName = (String) module.get("node name");
		String moduleName = (String) module.get("module name");
		int size = (int) module.get("size");
		long bandwidth = (long) module.get("bandwidth");
		double fractionalSensitivity = (double) module.get("Fractional Sensitivity");
		String inTuple = (String) module.get("inTuple");
		int mips = (int) module.get("mips");
		String outTuple = (String) module.get("outTuple");
		int modRam = (int) module.get("ram");
		double x_cord = (double) module.get("x_cord");
		double y_cord = (double) module.get("y_cord");
		long szT = (long) module.get("radius");
		double sz = (double) szT;
		
		ModuSpec m = textfile.createModule(nodeName, moduleName, modRam, bandwidth, inTuple, outTuple,
				size, mips, fractionalSensitivity);
		m.x = x_cord;
		m.y = y_cord;
		m.dispSize = sz;
		textfile.modulesList.add(m);
		modulesList.add(m);
		dispNode newMod = textfile.createDispNode(nodeName, m, m.x, m.y, m.dispSize);
		textfile.dispNodesList.add(newMod);
		redrawNodes();
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
    dispNode addActuator() {
    	try {
    		FXMLLoader addNewActuatorLoader = new FXMLLoader(getClass().getResource("ActuatorBox.fxml"));
    		Scene scene = new Scene(addNewActuatorLoader.load(),264,133);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		ActuatorInputController actuatorController = addNewActuatorLoader.getController();
    		stage.setTitle("Add Actuator");
    		stage.showAndWait();
    		ActuatorSpec a = actuatorController.getSpec();
    		if (a==null) return null;
    		String name = a.name;

    		dispNode newActuator = textfile.createDispNode(a.name, a, xCenter, yCenter, R+R);
			if (name != "Error" && getActuator(name)==null) {
				textfile.actuatorsList.add(a);
				actuatorsList.add(a);
				textfile.dispNodesList.add(newActuator);
			}
			redrawNodes();
    		return newActuator;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @FXML
    dispNode addDevice() {
    	try {
    		FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("DeviceInputBox.fxml"));
    		Scene scene = new Scene(addNewNodeLoader.load(),450,320);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AddDeviceController controller = addNewNodeLoader.getController();
    		stage.setTitle("Add Device Node");
    		stage.showAndWait();
    		DeviceSpec d = controller.getSpec();
    		LinkSpec l = controller.getLinkSpec();
    		if (d==null) return null;
    		String name = d==null?"Error":d.name;
    		dispNode newDevice = textfile.createDispNode(name, d, xCenter, yCenter, R+R);
			if (name != "Error" && getDevice(name)==null) {
				textfile.devicesList.add(d);
				textfile.linksList.add(l);
				devicesList.add(d);
				linksList.add(l);
				textfile.dispNodesList.add(newDevice);
				textfile.dispLinksList.add(textfile.createDispLink(d));
			}
			redrawNodes();
    		return newDevice;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @FXML
    dispNode addSensor() {
    	try {
    		FXMLLoader addNewSensorLoader = new FXMLLoader(getClass().getResource("SensorBox.fxml"));
    		Scene scene = new Scene(addNewSensorLoader.load(),450,400);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AddSensorController sensorController = addNewSensorLoader.getController();
    		sensorController.populateList(devicesList);
    		stage.setTitle("Add Sensor");
    		stage.showAndWait();
    		SensorSpec s = sensorController.getSpec();
    		if (s==null) return null;
    		String name = s==null?"Error":s.name;
    		dispNode newDevice = textfile.createDispNode(name, s, xCenter, yCenter, R+R);
			if (name != "Error" && getSensor(name)==null) {
				textfile.sensorsList.add(s);
				sensorsList.add(s);
				textfile.dispNodesList.add(newDevice);
			}
			redrawNodes();
    		return newDevice;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @FXML
    dispNode addModule() {
    	try {
    		FXMLLoader dataFXML = new FXMLLoader(getClass().getResource("ModuleInputBox.fxml"));
    		Scene scene = new Scene(dataFXML.load(),414,346);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AddModuleController controller = dataFXML.getController();
    		controller.populateList(devicesList);
    		stage.setTitle("Add Module");
    		stage.showAndWait();
    		ModuSpec m = controller.getSpec();
    		if (m==null) return null;
    		String name = m.name==null?"Error":m.name;
    		dispNode newMod = textfile.createDispNode(name, m, xCenter, yCenter, R+R);
			if (name != "Error" && getModule(name)==null) {
				textfile.modulesList.add(m);
				modulesList.add(m);
				textfile.dispNodesList.add(newMod);
			}
    		redrawNodes();
    		return newMod;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @FXML
    ModuEdgeSpec addEdge() {
	    try {
			FXMLLoader dataFXML = new FXMLLoader(getClass().getResource("EdgeInputBox.fxml"));
			Scene scene = new Scene(dataFXML.load(),414,346);
			Stage stage = new Stage();
			stage.setScene(scene);
			AddEdgeController controller = dataFXML.getController();
			if(selectedModulesList.isEmpty()) controller.populateList(modulesList);
			else controller.setChoices(selectedModulesList);
			selectedModulesList.removeAll(selectedModulesList);
			stage.setTitle("Add App Edge");
			stage.showAndWait();
    		ModuEdgeSpec v = controller.getSpec();
    		if(v!=null) {
    			moduleEdgesList.add(v);
    			textfile.dispLinksList.add(textfile.createDispLink(v));
    		}
    		redrawNodes();
    		return v;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public ModuSpec getModule(String _name) {
    	if (_name==null) return null;
    	for (ModuSpec m : modulesList) if (m.name.equals(_name)) return m;
		return null;
    }
    public SensorSpec getSensor(String _name) {
    	if (_name==null) return null;
    	for (SensorSpec s : sensorsList) if (s.name.equals(_name)) return s;
		return null;
    }
    public DeviceSpec getDevice(String _name) {
    	if (_name==null) return null;
    	for (DeviceSpec d : devicesList) if (d.name.equals(_name)) return d;
		return null;
    }
    public ActuatorSpec getActuator(String _name) {
    	if (_name==null) return null;
    	for (ActuatorSpec a : actuatorsList) if (a.name.equals(_name)) return a;
		return null;
    }
    public dispLink getLinkBySrc(String _src) {
    	if (_src==null) return null;
    	for (dispLink l : textfile.dispLinksList) if (l.src.name.equals(_src)) return l;
		return null;
    }
    
    @FXML
    void deleteHandler(ActionEvent event) {
    	
    }
    
    @FXML
    void editHandler(ActionEvent event) {
    	
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
    private static FogDevice addMobile(String nodeName, long nodeMips, int nodeRam, long nodeUpBw, long nodeDownBw, int nodeLevel, double nodeRatePerMips, double nodeBusyPower, double nodeIdlePower){
		FogDevice mobile = createFogDevice(nodeName, nodeMips, nodeRam, nodeUpBw, nodeDownBw, nodeLevel, nodeRatePerMips, nodeBusyPower, nodeIdlePower);
		if (nodeLevel == 0) {
			mobile.setParentId(-1);
		}
		return mobile;
	}

	private static FogDevice createFogDevice(String nodeName, long mips,
			int ram, long upBw, long downBw, int level, double ratePerMips, double busyPower, double idlePower) {
		
		List<Pe> peList = new ArrayList<Pe>();
		
		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerOverbooking(mips))); // need to store Pe id and MIPS Rating

		int hostId = FogUtils.generateEntityId();
		long storage = 1000000; // host storage
		int bw = 10000;

		PowerHost host = new PowerHost(
				hostId,
				new RamProvisionerSimple(ram),
				new BwProvisionerOverbooking(bw),
				storage,
				peList,
				new StreamOperatorScheduler(peList),
				new FogLinearPowerModel(busyPower, idlePower)
			);

		List<Host> hostList = new ArrayList<Host>();
		hostList.add(host);

		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now

		FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
				arch, os, vmm, host, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		FogDevice fogdevice = null;
		try {
			fogdevice = new FogDevice(nodeName, characteristics, 
					new AppModuleAllocationPolicy(hostList), storageList, 10, upBw, downBw, 0, ratePerMips);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fogdevice.setLevel(level);
		return fogdevice;
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