package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

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
    private TextField simulationTime;

    @FXML
    private TextField policyView;
    
    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Canvas topoField;
    
    @FXML
    private Canvas graphArea;
    
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
	Color phyColor=Color.RED;
	Color virColor=Color.BLUE;
    GraphicsContext gc;
	dispNode draggingNode = null;
    SetupJSONParser textfile = new SetupJSONParser();
	
	class dispNode {
		String name = "err";
		double x,y,sz;
		int id;
		Color c;
		NodeSpec data;

		void setPos(MouseEvent mEvent) {this.x=mEvent.getX()-0.5*this.sz; this.y=mEvent.getY()-0.5*this.sz;}
		void draw() {gc.setFill(c); gc.fillOval(this.x, this.y, this.sz, this.sz);}
		dispNode(String _name, NodeSpec _n, Color _c) {this(_name, _n, xCenter, yCenter, R+R, _c);}
		dispNode(String _name, NodeSpec _n, double _x, double _y, double _r, Color _c) {
			name = _name;
			x = _x;
			y = _y;
			sz = _r;
			c = _c;
			id = globalID++;
		}
	}
	
	class dispLink{
		dispNode src, dst;
		void draw() {
			double x1=0; double y1=0;
			double x2=0; double y2=0;
			if(src!=null) {x1=src.x+src.sz; y1= src.y + src.sz;}
			if(dst!=null) {x2=dst.x+dst.sz; y2= dst.y + dst.sz;}
			if(src!=null&&dst!=null) {
				gc.beginPath();
		    	gc.moveTo(x1, y1);
				gc.lineTo(x2, y2);
				gc.stroke();
			}
		}
		dispLink(dispNode _src, dispNode _dst){this.src=_src; this.dst=_dst;}
		dispLink(DeviceSpec _src, DeviceSpec _dst) {
	    	for(dispNode dn : dispNodesList) if(dn.name==_src.name) {this.src=dn;}
	    	for(dispNode dn : dispNodesList) if(dn.name==_dst.name) {this.dst=dn;}
		}
		dispLink(DeviceSpec _device) {
			System.out.println("Given Spec's SrcName: " + _device.name + " DstName: " + _device.parent);
			//for (dispNode dn : dispNodesList) System.out.print("'"+dn.name + "' ");
			System.out.println("-");
			for (int i=0; i<dispNodesList.size(); i++) {
				if(dispNodesList.get(i).name==_device.name) System.out.println("Src@"+i);
			}
			for (int i=0; i<dispNodesList.size(); i++) {
				if(dispNodesList.get(i).name.matches(_device.parent)) {
					System.out.println("Dst@"+i);
				}else {
					System.out.println("No Match with " + dispNodesList.get(i).name);
				}
			}
//			for (dispNode dn : dispNodesList)
//				if (dn.name == _device.name) {
//					this.src = dn;
//				} else {
//					System.out.println("New Name " + dn.name + " != " + _device.name);
//				}
//			for (dispNode dn : dispNodesList) {
//				if (dn.name == _device.parent) {
//					this.dst = dn;
//				} else {
//					System.out.println("New Name " + dn.name + " != " + _device.parent);
//				}
//			}
		}
		dispLink(ModuEdgeSpec _spec) {
	    	for(dispNode dn : dispNodesList) if(dn.name==_spec.name)   {this.src=dn;}
	    	for(dispNode dn : dispNodesList) if(dn.name==_spec.parent) {this.dst=dn;}
		}
	}
	
	public List<dispNode> dispNodesList = new ArrayList<dispNode>();
	public List<dispLink> dispLinksList = new ArrayList<dispLink>();
	public List<String> deviceNamesList = new ArrayList<String>();
	public List<String> moduleNamesList = new ArrayList<String>();

	public List<DeviceSpec> devicesList = new ArrayList<DeviceSpec>();
	public List<ModuSpec> modulesList = new ArrayList<ModuSpec>();
	public List<ModuEdgeSpec> moduleEdgesList = new ArrayList<ModuEdgeSpec>();
	
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    }
	@Override
	public void handle(KeyEvent event) { 	
		switch (event.getCode()){
			case ESCAPE : System.out.println("Esc"); break;	 // Select Pointer Tool | Escape Menu Without Saving
			case DIGIT1 : System.out.println("1"); break;	 // Select Node Placer
			case DIGIT2 : System.out.println("2"); break;	 // Select Module Placer
			case DIGIT3 : System.out.println("3"); break;	 // Select Edge Placer
			case Z 		: System.out.println("Z"); break;	 // Undo Last Action
			case E 		: System.out.println("E"); break;	 // Edit Object Selected
			case F5 	: System.out.println("F5"); break; // Save File
			case DELETE : System.out.println("Del"); break;// Delete Object Selected
			default : {} // Nothing
		}
	}
    
    public void setupListeners(Stage parentStage, Scene scene) {
    	ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue)->screenDragHandler();
    	parentStage.widthProperty().addListener(stageSizeListener);
    	parentStage.heightProperty().addListener(stageSizeListener);
    	gc = topoField.getGraphicsContext2D();
    	scene.setOnKeyPressed(this); // uses handle method
    }
    
    @FXML
    private void mouseClickHandler(MouseEvent mEvent){
    	dispNode tempNode = getNodeOnClick(mEvent);
        if(tempNode==null) {
        	System.out.println("Making new node...");
        	dispNode newNode = new dispNode("dispItem", null, mEvent.getX()-R, mEvent.getY()-R, R+R, phyColor);
        	draggingNode = newNode;
        	redrawNodes();
        }else {
        	System.out.println("Picked up node...");
        	dispNodesList.remove(tempNode);
        	draggingNode = tempNode;
        }
    }
    
    @FXML
    private void mouseReleaseHandler(MouseEvent mEvent) {
    	addDevice().setPos(mEvent);
    	draggingNode=null;
    	redrawNodes();
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
    	System.out.println("Updated canvas size");
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
		System.out.println(dispNodesList.size() + " " + dispLinksList.size());
		for(dispLink link : dispLinksList) {
			String srcName = link.src!=null?link.src.name:"";
			String dstName = link.dst!=null?link.dst.name:"";
			System.out.println("Link src " + srcName + " dst " + dstName);
			link.draw();
		}
		for(dispNode node : dispNodesList) node.draw();
		if (draggingNode!=null) draggingNode.draw();
	}
    
	@FXML
    void newJSON(ActionEvent event) {
    	try {
    		FXMLLoader root = new FXMLLoader(getClass().getResource("createJsonBox.fxml"));
    		Scene scene = new Scene(root.load(),414,139);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		stage.setTitle("Create New Design File");
    		stage.show();
    	} catch(Exception e) {
    		e.printStackTrace();
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
    dispNode addDevice() {
    	try {
    		FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("DeviceInputBox.fxml"));
    		Scene scene = new Scene(addNewNodeLoader.load(),450,320);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AddNodeController controller = addNewNodeLoader.getController();
    		stage.setTitle("Add Device Node");
    		stage.showAndWait();
    		DeviceSpec d = controller.getSpec();
    		if (d==null) return null;
    		String name = d==null?"Error":d.name;
    		dispNode newDevice = new dispNode(name, d, xCenter, yCenter, R+R, phyColor);
			if (name != "Error" && deviceNamesList.indexOf(name) < 0) {
				devicesList.add(d);
				deviceNamesList.add(name);
				dispNodesList.add(newDevice);
				dispLinksList.add(new dispLink(d));
			}
//			System.out.println(d.toString());
//    		System.out.println("_MainWindowController.java: " + deviceNamesList.toString());
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
    		controller.populateList(deviceNamesList);
    		stage.setTitle("Add Module");
    		stage.showAndWait();
    		ModuSpec m = controller.getSpec();
    		if (m==null) return null;
    		String name = m.name==null?"Error":m.name;
    		dispNode newMod = new dispNode(name, m, xCenter, yCenter, R+R, virColor);
			if (name != "Error" && moduleNamesList.indexOf(name) < 0) {
				modulesList.add(m);
				moduleNamesList.add(name);
				dispNodesList.add(newMod);
			}
    		System.out.println("_MainWindowController.java: " + deviceNamesList.toString());
    		return newMod;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @FXML
    void addEdge(ActionEvent event) {
	    try {
			FXMLLoader dataFXML = new FXMLLoader(getClass().getResource("EdgeInputBox.fxml"));
			Scene scene = new Scene(dataFXML.load(),414,346);
			Stage stage = new Stage();
			stage.setScene(scene);
			AddEdgeController controller = dataFXML.getController();
			controller.populateList(moduleNamesList);
			stage.setTitle("Add App Edge");
			stage.showAndWait();
    		ModuEdgeSpec v = controller.getSpec();
    		if(v!=null) moduleEdgesList.add(v);
		} catch(Exception e) {
			e.printStackTrace();
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
    void startSim(ActionEvent event) throws IOException {
    	String policy = simulationTime.getText()+" "+policyView.getText();
    	FileWriter file_writer;
    	String jsonDestinationFileName = createJsonController.jsonDestinationFileName;
//     	textfile.writeJSON(jsonDestinationFileName + ".json");
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
    		//stage.close();
    		
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
