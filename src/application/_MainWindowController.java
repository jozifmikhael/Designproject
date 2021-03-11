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
	int globalID=0;
	double R=50;
	public class Node {
		String name="err";
		double x=100;
		double y=100;
		double d=R;
		int id;
		
		Node(String _name, double _x, double _y, double _r){
			name=_name;
			x=_x;
			y=_y;
			d=_r;
			id=globalID++;
		}
		
		Node(String _name){
			name=_name;
		}
		
		public void stick() {
			//TODO get the mouse's pos and update after adjusting for scene offsets etc.
		}
		
		public double getX() {return this.x;}
		public double getY() {return this.y;}
	}
	public List<Node> nodeList = new ArrayList<Node>();
	public List<String> moduleList = new ArrayList<String>();
	
	public int someNumber = 0;
	
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
//    	System.out.println("Init");
//    	nodeList.add("test1");
//    	nodeList.add("test2");
//    	moduleList.add("test1");
//    	moduleList.add("test2");
//    	topoField.addEventHandler(MouseEvent.MOUSE_CLICKED, mEvent->mouseClickHandler(mEvent));
    	
    }
    
    GraphicsContext gc;
    public void setupListeners(Stage parentStage, Scene scene) {
    	ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue)->screenDragHandler();
    	parentStage.widthProperty().addListener(stageSizeListener);
    	parentStage.heightProperty().addListener(stageSizeListener);
    	gc = topoField.getGraphicsContext2D();
    	scene.setOnKeyPressed(this); // uses handle method
    }
    
    @Override
	public void handle(KeyEvent event) {
    	switch(event.getCode()) {
			case DIGIT1: System.out.println("1"); break;
			case DIGIT2: System.out.println("2"); break;
			case DIGIT3: System.out.println("3"); break;
			case DIGIT4: System.out.println("4"); break;
			case Z	   : System.out.println("z"); break;
			case DELETE: System.out.println("Del"); break;
			default: break;
    	}
	}

	double clickX=0;
	double clickY=0;
	Node draggingNode = null;
    @FXML
    private void mouseClickHandler(MouseEvent mEvent){
        clickX=mEvent.getX();
        clickY=mEvent.getY();
        
        Node tempNode = getNodeOnClick(mEvent);
        if(tempNode==null) {
        	System.out.println("Making new node...");
        	Node newNode = new Node("something", clickX-R, clickY-R, R+R);
        	draggingNode = newNode;
        	redrawNodes();
        }else {
        	System.out.println("Picked up node...");
        	nodeList.remove(tempNode);
        	draggingNode = tempNode;
        }
    }
    
    @FXML
    private void mouseReleaseHandler(MouseEvent mEvent) {
    	addNode();
    	nodeList.add(draggingNode);
    	System.out.println("There are #" + nodeList.size() + " nodes");
    	System.out.println("MousePos ("+mEvent.getX()+","+mEvent.getY()+")");
    	System.out.println("MousePos ("+draggingNode.getX()+","+draggingNode.getY()+")");
    	draggingNode = null;
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
        
    private void drawNode(Node _node) {
    	gc.fillOval(_node.x, _node.y, R+R, R+R);
	}
    
	private Node getNodeOnClick(MouseEvent mEvent) {
		for(Node n : nodeList) if(Math.pow(Math.pow(n.x+(0.5*n.d)-mEvent.getX(),2)+Math.pow(n.y+(0.5*n.d)-mEvent.getY(),2),0.5)<=0.5*n.d) return n;
		return null;
	}

	private void screenDragHandler() {
    	System.out.println("Updated canvas size");
    	gc.setFill(Color.WHITE);
		topoField.setHeight(backPane.getHeight());
		topoField.setWidth(backPane.getWidth());
		gc.fillRect(0, 0, topoField.getWidth(), topoField.getHeight());
		redrawNodes();
    }
    
    private void redrawNodes() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, topoField.getWidth(), topoField.getHeight());
		gc.setFill(Color.RED);
		for(Node node : nodeList) drawNode(node);
		if (draggingNode!=null) drawNode(draggingNode);
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
    
//    @FXML
//    void showController(ActionEvent event) {
//    	try {	
//    		//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("InputBox.fxml"));
//    		FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("OutputController.fxml"));
//    		Scene scene = new Scene(addNewNodeLoader.load(),450,320);
//    		Stage stage = new Stage();
//    		stage.setScene(scene);
//    		NodeController saveNewNodeController = addNewNodeLoader.getController();
//    		stage.setTitle("Output");
//    		stage.showAndWait();
//    	} catch(Exception e) {
//    		e.printStackTrace();
//    	}
//    }
    
    @FXML
    void showController(ActionEvent event) {
        try {
            //BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("InputBox.fxml"));
            FXMLLoader addNewNodeLoader = new FXMLLoader(getClass().getResource("OutputController.fxml"));
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
    void addNode() {
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
