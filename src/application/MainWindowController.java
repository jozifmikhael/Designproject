package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.application.Platform;
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
import javafx.scene.canvas.*;




public class MainWindowController implements Initializable{
	public class Node {
		String name="err";
		double posX=100;
		double posY=100;
		double r=40;
		boolean dragging=false;
		
		Node(String _name, double _posX, double _posY, double _r){
			name=_name;
			posX=_posX;
			posY=_posY;
			r=_r;
		}
		
		Node(String _name){
			name=_name;
		}
		
		public void stick() {
			//TODO get the mouse's pos and update after adjusting for scene offsets etc.
		}
	}
	private static boolean initFlag = true;
	public List<Node> nodeList = new ArrayList<Node>();
	public List<String> moduleList = new ArrayList<String>();
	
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
    private MenuItem addNodeMenu;

    @FXML
    private MenuItem addModuleMenu;

    @FXML
    private MenuItem addEdgeMenu;
    
    @FXML
    private MenuItem resetCacheMenu;
    
    @FXML
    private MenuItem newJSONMenu;

    private ObservableList<String> devices = FXCollections.observableArrayList();
    //private ObservableList<String> sensors= FXCollections.observableArrayList();
    //private ObservableList<String> actuators= FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> policyList;
    
    @FXML
    private AnchorPane backPane;
        
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	System.out.println("Init");
//    	nodeList.add("test1");
//    	nodeList.add("test2");
//    	moduleList.add("test1");
//    	moduleList.add("test2");
//    	topoField.addEventHandler(MouseEvent.MOUSE_CLICKED, mEvent->mouseClickHandler(mEvent));
    }
    
    @FXML
    private void mouseClickHandler(MouseEvent mEvent){
        GraphicsContext gc = topoField.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		System.out.println(backPane.getHeight() + "" + backPane.getWidth());
		topoField.setHeight(backPane.getHeight());
		topoField.setWidth(backPane.getWidth());
		gc.fillRect(0, 0, topoField.getWidth(), topoField.getHeight());
		gc.setFill(Color.RED);
		
		//TODO this will be a for-loop over the nodeList to iteratively draw
		//will refer to the Node's x/y/r vals
		//for nodes
		//gc.fillOval(node.x, node.y, node.r+r, r+r);

		double clickX=mEvent.getX();
		double clickY=mEvent.getY();
		//TODO here well do a check to see if its a pre-existing Node's bounding box
		//if so well start editing that nodes x y pos onto the mouse's pos
		
		//placeholder
		double r=40;
		gc.fillOval(clickX-r, clickY-r, r+r, r+r);
    	System.out.println(clickX+", "+clickY);
    }
    
    @FXML
    void newJSON(ActionEvent event) {
    	// menu item implementation
    	try {	
//    		BorderPane root = FXMLLoader.load(getClass().getResource("createJsonBox.fxml"));
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
    void addNode(ActionEvent event) {
    	try {	
    		//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("InputBox.fxml"));
    		FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("InputBox.fxml"));
    		Scene scene = new Scene(addNewNodeLoader.load(),450,320);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		NodeController saveNewNodeController = addNewNodeLoader.getController();
    		stage.setTitle("Add Mobile Node");
    		stage.showAndWait();
    		nodeList.add(new Node(saveNewNodeController.getNodeName().toString()));
    		for(Node node : nodeList) {
    			System.out.println(node.name);
    		}
    		/*if (node == null || node.isEmpty()) {
            	node = Optional.of("");
           }*/
    		/* if(node.isPresent()) {
    			devices.add(node.get());	
    			policyList.setItems(devices); 
    		}
    		else {
    			
    		}*/
    		//policyList.setItems(devices); 
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void addEdge(ActionEvent event) {
    try {
    	//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("InputBox.fxml"));
		FXMLLoader addAppEdgeLoader = new FXMLLoader(getClass().getResource("AppEdgeInputBox.fxml"));
		Scene scene = new Scene(addAppEdgeLoader.load(),414,346);
		Stage stage = new Stage();
		stage.setScene(scene);
		AppEdgeController saveNewNodeController = addAppEdgeLoader.getController();
		saveNewNodeController.populateParentList(moduleList);
		saveNewNodeController.populateChildList(moduleList);
		stage.setTitle("Add App Edge");
		stage.showAndWait();
		String edge = saveNewNodeController.getAppEdgeName();
		/*if(edge.isPresent()) {
			devices.add(edge.get());	
		}
		policyList.setItems(devices);*/
	} catch(Exception e) {
		e.printStackTrace();
	}
	
    }
    
    @FXML
    void addModule(ActionEvent event) {
    	try {
        	//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("InputBox.fxml"));
    		FXMLLoader addAppModuleLoader = new FXMLLoader(getClass().getResource("AppModuleInputBox.fxml"));
    		Scene scene = new Scene(addAppModuleLoader.load(),414,346);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		AppModuleController saveNewNodeController = addAppModuleLoader.getController();
    		saveNewNodeController.populateList(nodeList.stream().map(n->n.name).collect(Collectors.toList()));
    		stage.setTitle("Add App Module");
    		saveNewNodeController.setName("ss");
    		stage.showAndWait();
    		moduleList.add(saveNewNodeController.getAppModuleName().toString());
    		System.out.println("Added module" + saveNewNodeController.getAppModuleName().toString());
    		String module = saveNewNodeController.getAppModuleName();
    		/*if(module.isPresent()) {
    			devices.add(module.get());	
    		}
    		policyList.setItems(devices); */
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
    
   /* @FXML
    void addActuator(ActionEvent event) {
    	try {	
    		//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("ActuatorBox.fxml"));
    		FXMLLoader addNewActuatorLoader = new FXMLLoader(getClass().getResource("ActuatorBox.fxml"));
    		Scene scene = new Scene(addNewActuatorLoader.load(),450,320);
    		ActuatorInputController saveNewNodeController = addNewActuatorLoader.getController();
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		stage.setTitle("Add Actuator");
    		stage.showAndWait();
    		Optional<String> node = saveNewNodeController.getActuatorName();
    		if(node.isPresent()) {
    			devices.add(node.get());	
    		}
    		policyList.setItems(devices);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@FXML
    void addCloud(ActionEvent event) {
    	try {	
    		//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("InputBox.fxml"));
    		FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("InputBox.fxml"));
    		Scene scene = new Scene(addNewNodeLoader.load(),450,320);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		InputBoxController saveNewNodeController = addNewNodeLoader.getController();
    		stage.setTitle("Add Cloud Node");
    		stage.showAndWait();
    		Optional<String> node = saveNewNodeController.getNodeName();
    		if(node.isPresent()) {
    			devices.add(node.get());	
    		}
    		policyList.setItems(devices);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }*/

    /*@FXML
    void addComputer(ActionEvent event) {
    	try {	
    		//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("InputBox.fxml"));
    		FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("InputBox.fxml"));
    		Scene scene = new Scene(addNewNodeLoader.load(),450,320);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		InputBoxController saveNewNodeController = addNewNodeLoader.getController();
    		stage.setTitle("Add Computer Node");
    		stage.showAndWait();
    		Optional<String> node = saveNewNodeController.getNodeName();
    		if(node.isPresent()) {
    			devices.add(node.get());	
    		}
    		policyList.setItems(devices);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    @FXML
    void exitApp(ActionEvent event) {
    	Stage stage;
    	stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
    	stage.close();
    }

    

   /* @FXML
    void addSensor(ActionEvent event) {
    	try {	
    		//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("SensorBox.fxml"));
    		FXMLLoader addNewSensorLoader = new FXMLLoader(getClass().getResource("SensorBox.fxml"));
    		Scene scene = new Scene(addNewSensorLoader.load(),450,320);
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		SensorInputController saveNewNodeController = addNewSensorLoader.getController();
    		stage.setTitle("Add Sensor");
    		stage.showAndWait();
    		Optional<String> node = saveNewNodeController.getSensorName();
    		if(node.isPresent()) {
    			devices.add(node.get());	
    		}
    		policyList.setItems(devices);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
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
        try {
            file_writer = new FileWriter("simTime.txt",true);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
            buffered_Writer.write(policy);
            buffered_Writer.flush();
            buffered_Writer.close();
        } catch (IOException e) {
            System.out.println("Add line failed!" +e);
        }

    	String command = "AutoClicker.exe";
           // Running the above command 
           Runtime run  = Runtime.getRuntime(); 
           Process proc = run.exec(command);
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
