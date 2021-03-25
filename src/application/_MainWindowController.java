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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.fog.test.perfeval.VRGameFog;
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
	double xCenter=100;
	double yCenter=100;
	int fontSize = 16;
	String font = "monospaced";
	Color deviceColor=Color.RED;
	Color moduleColor=Color.CYAN;
	Color sensorColor=Color.PINK;
	Color transpColor=Color.TRANSPARENT;
	Color _errorColor=Color.GREEN;
    GraphicsContext gc;
    SetupJSONParser textfile = new SetupJSONParser();
	
	class dispNode {
		String name = "err";
		double x,y,sz;
		int id;
		Color c;
		NodeSpec data;
		
		void draw() {
			gc.setFill(c);
			gc.fillOval(this.x, this.y, this.sz, this.sz);
			if(c!=transpColor) gc.strokeOval(this.x, this.y, this.sz, this.sz);
			gc.setFill(Color.BLACK);
			gc.strokeText(this.name, this.x+0.5*this.sz, this.y+0.5*this.sz+0.4*fontSize);
		}
		void setPos(MouseEvent mEvent) {
			this.x=mEvent.getX()-0.5*this.sz; this.y=mEvent.getY()-0.5*this.sz;
			if(data.type.equals("device")) {
				DeviceSpec d = getDevice(this.data.name);
				devicesList.remove(d);
				d.x=this.x;
				d.y=this.y;
				d.dispSize=this.sz;
				devicesList.add(d);
			}else if(data.type.equals("module")) {
				ModuSpec m = getModule(this.data.name);
				devicesList.remove(m);
				m.x=this.x; m.y=this.y; m.dispSize=this.sz;
				modulesList.add(m);
			}else if(data.type.equals("sensor")) {
				SensorSpec s = getSensor(this.data.name);
				sensorsList.remove(s);
				s.x=this.x; s.y=this.y;
				s.dispSize=this.sz;
				sensorsList.add(s);
			}else if(data.type.equals("actuator")) {
				
			}
		}
		dispNode(String _name, NodeSpec _n) {this(_name, _n, xCenter, yCenter, R+R);}
		dispNode(String _name, NodeSpec _n, double _x, double _y, double _r) {
			name = _name;
			x = _x;
			y = _y;
			sz = _r;
			data = _n;
			if(data.type.equals("device")) c = deviceColor;
			else if(data.type.equals("module")) c = moduleColor;
			else if(data.type.equals("sensor")) c = sensorColor;
			else c = _errorColor;
			id = globalID++;
		}
		dispNode(String _name, Color _c, double _x, double _y, double _r) {
			name = _name;
			x = _x;
			y = _y;
			sz = _r;
			c = _c;
		}
	}
	
	class dispLink{
		dispNode src, dst;
		void draw() {
			double x1=0; double y1=0;
			double x2=0; double y2=0;
			if(src!=null) {x1=src.x+0.5*src.sz; y1= src.y+0.5*src.sz;}
			if(dst!=null) {x2=dst.x+0.5*dst.sz; y2= dst.y+0.5*dst.sz;}
			if(src!=null&&dst!=null) {
				gc.beginPath();
		    	gc.moveTo(x1, y1);
				gc.lineTo(x2, y2);
				gc.stroke();
			}
		}
		dispLink(dispNode _src, dispNode _dst){this.src=_src; this.dst=_dst;}
		dispLink(DeviceSpec _device) {
			for (dispNode dn : dispNodesList) if (dn.name.matches(_device.name)) this.src = dn;
			for (dispNode dn : dispNodesList) if (dn.name.matches(_device.parent)) this.dst = dn;
		}
		dispLink(DeviceSpec _src, DeviceSpec _dst) {
	    	for(dispNode dn : dispNodesList) if(dn.name==_src.name) {this.src=dn;}
	    	for(dispNode dn : dispNodesList) if(dn.name==_dst.name) {this.dst=dn;}
		}
		dispLink(ModuEdgeSpec _spec) {
	    	for(dispNode dn : dispNodesList) if(dn.name.matches(_spec.child)) this.src=dn;
	    	for(dispNode dn : dispNodesList) if(dn.name.matches(_spec.parent)) this.dst=dn;
		}
		public dispLink(ModuSpec _src, ModuSpec _dst) {
			for(dispNode dn : dispNodesList) if(dn.name==_src.name) {this.src=dn;}
	    	for(dispNode dn : dispNodesList) if(dn.name==_dst.name) {this.dst=dn;}
		}
	}
	
	public List<dispNode> dispNodesList = new ArrayList<dispNode>();
	public List<dispLink> dispLinksList = new ArrayList<dispLink>();
	public List<String> deviceNamesList = new ArrayList<String>();
	public List<String> moduleNamesList = new ArrayList<String>();
	public List<String> selectedModulesList = new ArrayList<String>();
	
	public List<String> sensorNameList = new ArrayList<String>();
	public List<SensorSpec> sensorsList = new ArrayList<SensorSpec>();
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

	dispNode draggingNode = null;
	dispLink draggingLink = null;
	dispNode linkSrcNode = null;
    @FXML
	private void mouseClickHandler(MouseEvent mEvent) {
//    	System.out.println("_MainWindowController.java: MClick State is " + state);
		dispNode selNode = getNodeOnClick(mEvent);
		if (state == 0) {
			draggingNode = selNode;
		} else if (state == 1) {
			if (selNode == null) {
//				System.out.println("_MainWindowController.java.java: Making new device node...");
				dispNode newNode = new dispNode("New Node", deviceColor, mEvent.getX() - R, mEvent.getY() - R, R + R);
				draggingNode = newNode;
			} else {
				draggingNode = selNode;
			}
		} else if (state == 2) {
			if (selNode == null) {
//				System.out.println("_MainWindowController.java.java: Making new module node...");
				dispNode newNode = new dispNode("New Module", moduleColor, mEvent.getX() - R, mEvent.getY() - R, R + R);
				draggingNode = newNode;
			} else {
				draggingNode = selNode;
			}
		} else if (state == 3) {
//			System.out.println("_MainWindowController.java.java: Making link...");
			linkSrcNode = selNode;
			dispNode newNode = new dispNode("", transpColor, mEvent.getX() - R, mEvent.getY() - R, R + R);
			draggingNode = newNode;
			draggingLink = new dispLink(linkSrcNode, draggingNode);
		} else if (state == 4) {
			if (selNode == null) {
//				System.out.println("_MainWindowController.java.java: Making new Sensor node...");
				dispNode newNode = new dispNode("New Sensor", sensorColor, mEvent.getX() - R, mEvent.getY() - R, R + R);
				draggingNode = newNode;
			} else {
				draggingNode = selNode;
			}
		}
		redrawNodes();
	}
    
    @FXML
    private void mouseReleaseHandler(MouseEvent mEvent) {
//    	System.out.println("_MainWindowController.java: MRelease State is " + state);
    	if (state==0) {
    		if(dispNodesList.indexOf(draggingNode)>=0)draggingNode.setPos(mEvent);
    	} else if (state==1) {
    		if(dispNodesList.indexOf(draggingNode)<0) {
    			dispNode newDevice=addDevice();
    			if(newDevice!=null) newDevice.setPos(mEvent);
    		} else draggingNode.setPos(mEvent);
        	draggingNode=null;
    	} else if (state==2) {
    		if(dispNodesList.indexOf(draggingNode)<0) {
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
						if(srcLink!=null) srcLink.dst = linkDstNode;
						else dispLinksList.add(new dispLink(srcDev, dstDev));
					}else if(srcType.equals("module")) {
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
    		if(dispNodesList.indexOf(draggingNode)<0) {
    			dispNode newSensor = addSensor();
    			if(newSensor!=null) newSensor.setPos(mEvent);
    		} else draggingNode.setPos(mEvent);
        	draggingNode=null;
		}
    	redrawNodes();
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
    public dispLink getLinkBySrc(String _src) {
    	if (_src==null) return null;
    	for (dispLink l : dispLinksList) if (l.src.name.equals(_src)) return l;
		return null;
    }
    
    @FXML
    private void mouseMoveHandler(MouseEvent mEvent) {
    	if(draggingNode != null){
			draggingNode.x = mEvent.getX()-R;
			draggingNode.y = mEvent.getY()-R;
			redrawNodes();
    	}
    }
    
	private void screenDragHandler() {
//    	System.out.println("_MainWindowController.java: Updated canvas size");
    	double w=backPane.getWidth(); double h=backPane.getHeight();
		xCenter=0.5*w; yCenter=0.5*h;
		topoField.setWidth(w); topoField.setHeight(h);
    	gc.setFill(Color.WHITE); gc.fillRect(0, 0, w, h);
		redrawNodes();
    }
	
	private dispNode getNodeOnClick(MouseEvent mEvent) {
		for(dispNode n : dispNodesList) if(Math.pow(Math.pow(n.x+(0.5*n.sz)-mEvent.getX(),2)+Math.pow(n.y+(0.5*n.sz)-mEvent.getY(),2),0.5)<=0.5*n.sz) return n;
		return null;
	}
	
    private void redrawNodes() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, topoField.getWidth(), topoField.getHeight());
		for(dispLink link : dispLinksList) link.draw();
		if (draggingLink!=null) draggingLink.draw();
		for(dispNode node : dispNodesList) node.draw();
		if (draggingNode!=null) draggingNode.draw();
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
				JSONArray deviceList = (JSONArray) jsonObject.get("nodes");
				for (int i = 0; i < deviceList.size(); i++) {
					JSONObject device = (JSONObject) deviceList.get(i);
					parseDeviceObj(device);
				}
				JSONArray edgeList = (JSONArray) jsonObject.get("edges");
				for (int i = 0; i < edgeList.size(); i++) {
					JSONObject edge = (JSONObject) edgeList.get(i);
					parseEdgeObj(edge);
				}
				JSONArray moduleList = (JSONArray) jsonObject.get("modules");
				for (int i = 0; i < moduleList.size(); i++) {
					JSONObject module = (JSONObject) moduleList.get(i);
					parseModuleObj(module);
				}
				JSONArray sensorList = (JSONArray) jsonObject.get("sensors");
				for (int i = 0; i < sensorList.size(); i++) {
					JSONObject sensor = (JSONObject) sensorList.get(i);
					parseSensorObj(sensor);
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
		double latency = (double) device.get("latency");
		DeviceSpec d = textfile.createDevice(nodename + " " + mips + " " + ram + " " + upbw + " " + downbw + " " + level + " " + rate + " "
		+ apower + " " + ipower + " " + parentname +  " " + latency +" \n");
		d.x = x_cord;
		d.y = y_cord;
		d.dispSize = size;
		devicesList.add(d);
		dispNode newDevice = new dispNode(nodename, d, d.x, d.y, d.dispSize);
		dispNodesList.add(newDevice);
		dispLinksList.add(new dispLink(d));
		redrawNodes();
	}
	void parseSensorObj(JSONObject sensor) throws NumberFormatException, IOException {
		//JSONObject deviceObj = (JSONObject) device.get("nodes");
		double uniformMax = (double) sensor.get("uniformMax");
		String sensorName = (String) sensor.get("sensorName");
		double normalStdDev = (double) sensor.get("normalStdDev");
		double normalMean = (double) sensor.get("normalMean");
		double deterministicValue = (double) sensor.get("deterministicValue");
		String distribution = (String) sensor.get("distribution");
		double uniformMin = (double) sensor.get("uniformMin");
		double x_cord = (double) sensor.get("x_cord");
		double y_cord = (double) sensor.get("y_cord");
		double size = (double) sensor.get("radius");
		SensorSpec s = textfile.createSensor("node" + " " + 2.0 + " " + sensorName + " " + deterministicValue + " " + normalMean + " " + normalStdDev + " " + uniformMax + " "
		+ uniformMin + " " + distribution + " \n");
		s.x = x_cord;
		s.y = y_cord;
		s.dispSize = size;
		sensorsList.add(s);
		dispNode newDevice = new dispNode(sensorName, s, s.x, s.y, s.dispSize);
		sensorNameList.add(sensorName);
		dispNodesList.add(newDevice);
		redrawNodes();
	}
	void parseEdgeObj(JSONObject edge) throws NumberFormatException, IOException {
		//JSONObject edgeObj = (JSONObject) edgeList.get("nodes");
		String src = (String) edge.get("src");
		String dest = (String) edge.get("dest");
		double tupleCpuLength = (double) edge.get("tupleCpuLength");
		String edgeType = (String) edge.get("edgeType");
		double periodicity = (double) edge.get("periodicity");
		String tupleType = (String) edge.get("typleType");
		double tupleNwLength = (double) edge.get("tupleNwLength");
		long direction = (long) edge.get("direction");
		String time = (String) edge.get("time");
		
		ModuEdgeSpec e = textfile.createModuleEdge(src + " " + dest + " " + tupleType + " " + periodicity + " " + tupleCpuLength + " " + tupleNwLength + " " + edgeType + " "
		+ direction  +" \n");
		moduleEdgesList.add(e);		
	}
	
	void parseModuleObj(JSONObject module) throws NumberFormatException, IOException {
		//JSONObject moduleObj = (JSONObject) moduleList.get("nodes");
		String nodeName = (String) module.get("Node Name");
		String moduleName = (String) module.get("Module Name");
		long Size = (long) module.get("Size");
		long Bandwidth = (long) module.get("Bandwidth");
		double FractionalSensitivity = (double) module.get("Fractional Sensitivity");
		String inTuple = (String) module.get("inTuple");
		long MIPS = (long) module.get("MIPS");
		String outTuple = (String) module.get("outTuple");
		long RAM = (long) module.get("RAM");
		double x_cord = (double) module.get("x_cord");
		double y_cord = (double) module.get("y_cord");
		double sz = (double) module.get("radius");
		
		ModuSpec m = textfile.createModule(nodeName + " " + moduleName + " " + RAM + " " + Bandwidth + " " + inTuple + " " + outTuple + " " + Size + " "
		+ MIPS + " " + FractionalSensitivity + " \n");
		m.x = x_cord;
		m.y = y_cord;
		m.dispSize = sz;
		modulesList.add(m);
		dispNode newMod = new dispNode(nodeName, m, m.x, m.y, m.dispSize);
		moduleNamesList.add(nodeName);
		dispNodesList.add(newMod);
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
    dispNode addSensor() {
    	try {
    		FXMLLoader addNewSensorLoader = new FXMLLoader(getClass().getResource("SensorBox.fxml"));
    		Scene scene = new Scene(addNewSensorLoader.load(),450,400);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AddSensorController sensorController = addNewSensorLoader.getController();
    		sensorController.populateList(deviceNamesList);
    		stage.setTitle("Add Sensor");
    		stage.showAndWait();
    		SensorSpec s = sensorController.getSpec();
    		if (s==null) return null;
    		String name = s==null?"Error":s.name;
    		dispNode newDevice = new dispNode(name, s, xCenter, yCenter, R+R);
			if (name != "Error" && sensorNameList.indexOf(name) < 0) {
				sensorsList.add(s);
				sensorNameList.add(name);
				dispNodesList.add(newDevice);
			}
			redrawNodes();
    		return newDevice;
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
    		dispNode newDevice = new dispNode(name, d, xCenter, yCenter, R+R);
			if (name != "Error" && deviceNamesList.indexOf(name) < 0) {
				devicesList.add(d);
				linksList.add(l);
				deviceNamesList.add(name);
				dispNodesList.add(newDevice);
				dispLinksList.add(new dispLink(d));
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
//    		System.out.println("_MainWindowController.java ln313: Adding module\n");
    		FXMLLoader dataFXML = new FXMLLoader(getClass().getResource("ModuleInputBox.fxml"));
    		Scene scene = new Scene(dataFXML.load(),414,346);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AddModuleController controller = dataFXML.getController();
    		controller.populateList(deviceNamesList);
    		stage.setTitle("Add Module");
    		stage.showAndWait();
    		ModuSpec m = controller.getSpec();
    		if (m==null) return null;
    		String name = m.name==null?"Error":m.name;
    		dispNode newMod = new dispNode(name, m, xCenter, yCenter, R+R);
			if (name != "Error" && moduleNamesList.indexOf(name) < 0) {
				modulesList.add(m);
				moduleNamesList.add(name);
				dispNodesList.add(newMod);
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
			if(selectedModulesList.isEmpty()) controller.populateList(moduleNamesList);
			else controller.populateList(selectedModulesList);
			selectedModulesList.removeAll(selectedModulesList);
			stage.setTitle("Add App Edge");
			stage.showAndWait();
    		ModuEdgeSpec v = controller.getSpec();
    		if(v!=null) {
    			moduleEdgesList.add(v);
    			dispLinksList.add(new dispLink(v));
    		}
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
    	writeJSON();
    	testmethod();
    	System.out.println(selectedJSON);
     	VRGameFog simObj = new VRGameFog(selectedJSON);
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