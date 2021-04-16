package application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.fog.test.perfeval.TextParser.TupleSpec;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import application._SpecHandler.NodeSpec;
import org.fog.test.perfeval.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.*;

public class OutputController implements Initializable{
	private static List<Energy> Energylist = new ArrayList<Energy>();
	private static List<String> nodeList = new ArrayList<String>();
	private static List<Double> timeNetworkList = new ArrayList<Double>();
	private static List<Double> NetworkUsageList = new ArrayList<Double>();
	private List<TupleMetrics> tupleList = new ArrayList<TupleMetrics>();
	public List<XYChart.Series> seriesList = new ArrayList<XYChart.Series>();
	
	static String sourceFile="output.json";

    @FXML
    private BarChart<?, ?> barChart;
	@FXML
    private AnchorPane backPane;

	@FXML
	private MenuItem saveEnergy;
	
	@FXML
    private MenuItem saveNetwork;

    @FXML
    private MenuItem saveTuple;
	
    @FXML
    private Canvas graphArea;

    @FXML
    private StackedAreaChart<?, ?> EnergyConsumption;

    @FXML
    private CategoryAxis lineXAxis;
    
    @FXML
    private AnchorPane backPane1;

    @FXML
    private Canvas graphArea1;

    @FXML
    private LineChart<Number, Number> NetworkUsage;

    @FXML
    private TableView<TupleSpec> tupleTable;

    @FXML
    private TableColumn<TupleSpec, String> checkCol;
    
    @FXML
    private TableColumn<TupleSpec, String> sourceDevCol;

    @FXML
    private TableColumn<TupleSpec, String> sourceModCol;

    @FXML
    private TableColumn<TupleSpec, String> destDevCol;

    @FXML
    private TableColumn<TupleSpec, String> destModCol;

    @FXML
    private TableColumn<TupleSpec, String> typleCol;

    @FXML
    private TableColumn<TupleSpec, String> timeCol;

    @FXML
    private ChoiceBox<?> sourceChoiceBox;

    @FXML
    private ChoiceBox<?> destinationChoiceBox;

    @FXML
    private Button addButton;

	public static class Energy{
		String name;
		private List<Double> timeList = new ArrayList<Double>();
		private List<Double> EnergyConsumptionList = new ArrayList<Double>();
		
		Energy(String name, List<Double> timeList, List<Double> EnergyConsumptionList) {
			this.name = name;
			this.timeList = timeList;	
			this.EnergyConsumptionList = EnergyConsumptionList;
		}
	}
	
	@FXML
    void saveEnergyHandler(ActionEvent event) {
		String timestamp = new SimpleDateFormat("HHmmss_yyyyMMdd").format(Calendar.getInstance().getTime());
		EnergyConsumption.setAnimated(false);
		WritableImage image = EnergyConsumption.snapshot(new SnapshotParameters(), null);
		File file = new File("Energy Consumption"+timestamp+".png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
       }           
    }
	
	@FXML
    void saveNetworkHandler(ActionEvent event) {
		String timestamp = new SimpleDateFormat("HHmmss_yyyyMMdd").format(Calendar.getInstance().getTime());
		NetworkUsage.setAnimated(false);
		WritableImage image = NetworkUsage.snapshot(new SnapshotParameters(), null);
		File file = new File("Network Usage"+timestamp+".png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
       } 
    }

    @FXML
    void saveTupleHandler(ActionEvent event) {
    	String timestamp = new SimpleDateFormat("HHmmss_yyyyMMdd").format(Calendar.getInstance().getTime());
		barChart.setAnimated(false);
		WritableImage image = barChart.snapshot(new SnapshotParameters(), null);
		File file = new File("Tuple Delay"+timestamp+".png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
       } 
    }
	
	public class TupleMetrics{
		String tupleType;
		String tupleSRC;
		String tupleDEST;
		double tupleLatency;
		
		TupleMetrics(String tupleType, String tupleSRC, String tupleDEST, double tupleLatency){
			this.tupleType = tupleType;
			this.tupleSRC = tupleSRC;
			this.tupleDEST = tupleDEST;	
			this.tupleLatency = tupleLatency;
		}
	}
    
	@FXML
    void ParseEnergy() throws IOException, ParseException {
    	JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(sourceFile);
        Object obj = jsonParser.parse(reader);
        JSONObject nodeList = (JSONObject) obj;
        JSONArray nodeArr = (JSONArray) nodeList.get("nodes");
        nodeArr.forEach(l -> parseNodeData((JSONObject) l));
        JSONArray timeE = (JSONArray) nodeList.get("listEnergy");
        timeE.forEach(l -> parsetimeEData((JSONObject) l));
        EnergyConsumptionGraph();
    }
	
	@FXML
    void ParseNetwork() throws IOException, ParseException {
    	JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(sourceFile);
        Object obj = jsonParser.parse(reader);
        JSONObject nodeList = (JSONObject) obj;
        JSONArray network = (JSONArray) nodeList.get("listNetwork");
        network.forEach(l -> parseNetworkUsage((JSONObject) l));
        NetworkLineChart();
    }
	
	@FXML
	void ParsingTuples() throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(sourceFile);
        Object obj = jsonParser.parse(reader);
        JSONObject nodeList = (JSONObject) obj;
        JSONArray tuples = (JSONArray) nodeList.get("tuples");
        tuples.forEach(l -> parseTuples((JSONObject) l));
	}
    
	private static void parseNodeData(JSONObject link) {
		String nodeName = (String) link.get("name");
		nodeList.add(nodeName);
		List<Double> tempEnergylist = new ArrayList<Double>();
		List<Double> tempTimeList = new ArrayList<Double>();
		Energy energyOBJ = new Energy(nodeName, tempTimeList, tempEnergylist);
		Energylist.add(energyOBJ);
	}
	
	private static void parsetimeEData(JSONObject link) {
		String nodeName = (String) link.get("name");
		for(Energy temp: Energylist) {
			if(nodeName.equals(temp.name)) {
				temp.EnergyConsumptionList.add((double) link.get("energy"));
				temp.timeList.add((double) link.get("time"));
			}
		}
	}
	
	double time_=0;
	double networkUsage_;
	private void parseNetworkUsage(JSONObject link){
		double time = (double) link.get("time");
		double networkUsage = (double) link.get("networkUsage");
		timeNetworkList.add(time);
		NetworkUsageList.add(networkUsage);
	}
	
	private void parseTuples(JSONObject link){
		String tupleType = (String) link.get("tupleType");
		String tupleSCR = (String) link.get("tupleSCR");
		String tupleDEST = (String) link.get("tupleDEST");
		double TupleLatency = (double) link.get("TupleLatency");
		TupleMetrics tupleOBJ = new TupleMetrics(tupleType, tupleSCR, tupleDEST, TupleLatency);
		tupleList.add(tupleOBJ);
	}
	
	@FXML
	void EnergyConsumptionGraph() {
		final NumberAxis xAxis = new NumberAxis();
	    final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Energy Consumption");
        EnergyConsumption.setTitle("Time vs Energy Consumption");
        EnergyConsumption.setCreateSymbols(false);
        for(Energy energylist : Energylist) {
        	XYChart.Series temp = new XYChart.Series();
        	temp.setName(energylist.name);
        	for(int i= 0; i<energylist.timeList.size(); i++) {
        		temp.getData().add(new XYChart.Data(energylist.timeList.get(i),energylist.EnergyConsumptionList.get(i)));
        	}
        	seriesList.add(temp);
        }
        for(int i= 0; i<seriesList.size(); i++) {
        	EnergyConsumption.getData().add(seriesList.get(i));
        }
	}
	
	@FXML
	void NetworkLineChart() {
		final NumberAxis xAxis = new NumberAxis();
	    final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Network Usage");
        NetworkUsage.setCreateSymbols(false);;
        NetworkUsage.getStyleClass().add("thick-chart");
        NetworkUsage.setTitle("Time vs Network Usage");
        XYChart.Series series = new XYChart.Series();
        series.setName("Network Usages"); 
        for(int i = 0; i < timeNetworkList.size(); i++) {
        	//series.getData().add(new XYChart.Data("test", 10));
        	 series.getData().add(new XYChart.Data(timeNetworkList.get(i).toString(), NetworkUsageList.get(i)));
        }
        NetworkUsage.getData().add(series);
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
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("In init");
		try {
			ParseEnergy();
			ParseNetwork();
			psParseLatency();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void psParseLatency() {
		final NumberAxis xAxis = new NumberAxis();
	    final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Network Usage");
        barChart.setTitle("CPU Execution Delays");
        barChart.setLegendVisible(false);
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("PLAYER_GAME_STATE",1.171428573130893));
        series.getData().add(new XYChart.Data("EEG",4.099993792751119));
        series.getData().add(new XYChart.Data("CONCENTRATION",1.171428573130893));
        series.getData().add(new XYChart.Data("_SENSOR",0.566240448301607));
        series.getData().add(new XYChart.Data("GLOBAL_GAME_STATE",0.04575000214585016));
        barChart.getData().add(series);
		ObservableList<TupleSpec> tempData = FXCollections.observableArrayList();
//		String tupleType;
//		String tupleSrc; 
//		String tupleDst;
//		double tupleNWLatency;
//		double tupleSentTime;
//		double tupleArrivTime;
//	    checkCol sourceDevCol sourceModCol destDevCol destModCol typleCol timeCol
		//String tupleType, String tupleSRC, String tupleDEST, double sentTime, double arrivalTime
//		PLAYER_GAME_STATE ---> 1.171428573130893
//		EEG ---> 4.099993792751119
//		CONCENTRATION ---> 0.12893009140234982
//		_SENSOR ---> 0.566240448301607
//		GLOBAL_GAME_STATE ---> 0.04575000214585016
		TextParser t = new TextParser();
		tempData.add(t.new TupleSpec("PLAYER_GAME_STATE ", "concentration_calculator", "connector", "gateway", "cloud", 0, 0.09056122448938758));
        tempData.add(t.new TupleSpec("EEG ", "EEG", "client", "sensor0", "mobile-0", 0, 5.003540903540709));
        tempData.add(t.new TupleSpec("EEG ", "EEG", "client", "sensor1", "mobile-1", 0, 5.003540903540709));
        tempData.add(t.new TupleSpec("EEG ", "EEG", "client", "sensor2", "mobile-2", 0, 5.003540903540709));
        tempData.add(t.new TupleSpec("_SENSOR ", "EEG", "Client", "sensor0", "mobile-0", 0, 8.81658119658707));
        tempData.add(t.new TupleSpec("_SENSOR ", "EEG", "Client", "sensor1", "mobile-1", 0, 8.81658119658707));
        tempData.add(t.new TupleSpec("_SENSOR ", "EEG", "Client", "sensor2", "mobile-2", 0, 8.81658119658707));
        tempData.add(t.new TupleSpec("CONCENTRATION ", "concentration_calculator", "client", "gateway-0", "mobile-0", 0, 0.30730012210177815));
        tempData.add(t.new TupleSpec("GLOBAL_GAME_STATE ", "connector", "client", "cloud", "mobile-0", 0, 0.2668171658911005));
        tempData.add(t.new TupleSpec("GLOBAL_GAME_STATE ", "connector", "client", "cloud", "mobile-1", 0, 0.2668171658911005));
        tempData.add(t.new TupleSpec("GLOBAL_GAME_STATE ", "connector", "client", "cloud", "mobile-2", 0, 0.2668171658911005));
        
    	tupleTable.setItems(tempData);
    	typleCol.setCellValueFactory(new PropertyValueFactory <>("tupleType"));
    	typleCol.setCellFactory(TextFieldTableCell.forTableColumn());
//    	timeCol.setCellValueFactory(new PropertyValueFactory <>("arrivalTime"));
//    	timeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	destModCol.setCellValueFactory(new PropertyValueFactory <>("tupleDst"));
    	destModCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	sourceModCol.setCellValueFactory(new PropertyValueFactory <>("tupleSrc"));
    	sourceModCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	timeCol.setCellValueFactory(new PropertyValueFactory <>("tupleNWLatency"));
    	timeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	destDevCol.setCellValueFactory(new PropertyValueFactory <>("tupleDstDev"));
        destDevCol.setCellFactory(TextFieldTableCell.forTableColumn());
        sourceDevCol.setCellValueFactory(new PropertyValueFactory <>("tupleSrcDev"));
        sourceDevCol.setCellFactory(TextFieldTableCell.forTableColumn());
	}
}