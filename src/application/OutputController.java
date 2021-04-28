package application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.fog.test.perfeval.TextParser.TupleMetricSpec;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
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
	private List<TupleDelayBarGraph> tupleDelayList = new ArrayList<TupleDelayBarGraph>();
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
    private TableView<TupleMetrics> tupleTable;

    @FXML
    private TableColumn<TupleMetrics, String> checkCol;
    
    @FXML
    private TableColumn<TupleMetrics, String> sourceDevCol;

    @FXML
    private TableColumn<TupleMetrics, String> sourceModCol;

    @FXML
    private TableColumn<TupleMetrics, String> destDevCol;

    @FXML
    private TableColumn<TupleMetrics, String> destModCol;

    @FXML
    private TableColumn<TupleMetrics, String> typleCol;

    @FXML
    private TableColumn<TupleMetrics, Double> timeCol;

    @FXML
    private ChoiceBox<?> sourceChoiceBox;

    @FXML
    private ChoiceBox<?> destinationChoiceBox;

    @FXML
    private Button addButton;
    
    @FXML	
    private TextArea metricsBox;

	public static class Energy{
		static String name;
		private List<Double> timeList = new ArrayList<Double>();
		private static List<Double> EnergyConsumptionList = new ArrayList<Double>();
		
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
		@Override
		public String toString() {
			return "tupleType=" + tupleType + ",tupleSRC=" + tupleSRC + ",tupleDEST=" + tupleDEST + ",tupleSRCDev="
					+ tupleSRCDev + ",tupleDESTDev=" + tupleDESTDev + ",tupleNWLatency=" + tupleNWLatency + ",average="
					+ average;
		}

		public String getTupleType() {
			return tupleType;
		}

		public void setTupleType(String tupleType) {
			this.tupleType = tupleType;
		}

		public String getTupleSRC() {
			return tupleSRC;
		}

		public void setTupleSRC(String tupleSRC) {
			this.tupleSRC = tupleSRC;
		}

		public String getTupleDEST() {
			return tupleDEST;
		}

		public void setTupleDEST(String tupleDEST) {
			this.tupleDEST = tupleDEST;
		}

		public String getTupleSRCDev() {
			return tupleSRCDev;
		}

		public void setTupleSRCDev(String tupleSRCDev) {
			this.tupleSRCDev = tupleSRCDev;
		}

		public String getTupleDESTDev() {
			return tupleDESTDev;
		}

		public void setTupleDESTDev(String tupleDESTDev) {
			this.tupleDESTDev = tupleDESTDev;
		}

		public double getTupleNWLatency() {
			return tupleNWLatency;
		}

		public void setTupleNWLatency(double tupleNWLatency) {
			this.tupleNWLatency = tupleNWLatency;
		}

		public double getAverage() {
			return average;
		}

		public void setAverage(double average) {
			this.average = average;
		}

		public String tupleType;
		public String tupleSRC;
		public String tupleDEST;
		public String tupleSRCDev;
		public String tupleDESTDev;
		public double tupleNWLatency;
		public double average;
		
		TupleMetrics(String tupleType, String tupleSRCDev, String tupleDESTDev,String tupleSRC, String tupleDEST, double tupleNWLatency, double average){
			this.tupleType = tupleType;
			this.tupleSRC = tupleSRC;
			this.tupleDEST = tupleDEST;	
			this.tupleNWLatency = tupleNWLatency;
			this.tupleSRCDev = tupleSRCDev;
			this.tupleDESTDev = tupleDESTDev;
			this.average = average;
		}
	}
    
	public class TupleDelayBarGraph{
		String name;
		double delay;
		
		TupleDelayBarGraph(String tupleType, double delay){
			this.name = tupleType;
			this.delay = delay;
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
	
	@FXML
	void ParsingTupleDelay() throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(sourceFile);
        Object obj = jsonParser.parse(reader);
        JSONObject nodeList = (JSONObject) obj;
        JSONArray tuples = (JSONArray) nodeList.get("tupleDelays");
        tuples.forEach(l -> parseTupleDelay((JSONObject) l));
	}
	
	private void parseTupleDelay(JSONObject link){
		String name = (String) link.get("name");
		double delay = (double) (link.get("delay"));
		System.out.println("parse tuple delay name:" +name+"delay"+delay);
		TupleDelayBarGraph tupleOBJ = new TupleDelayBarGraph(name, delay);
		tupleDelayList.add(tupleOBJ);
		System.out.println(tupleDelayList.size());
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
		String tupleSCR = (String) link.get("tupleSRC");
		String tupleDEST = (String) link.get("tupleDEST");
		double tupleNWLatency = (double) link.get("tupleNWLatency");
		String tupleSRCDev = (String) link.get("tupleSrcDev");
		String tupleDESTDev = (String) link.get("tupleDstDev");
		double average = (double) link.get("average");
		TupleMetrics tupleOBJ = new TupleMetrics(tupleType, tupleSRCDev, tupleDESTDev,tupleSCR, tupleDEST, tupleNWLatency, average);
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
	
	void energyMetrics() {	
		String metricsAppend = "";	
		for(Energy energylist : Energylist) {	
			double avgEnergy = 0;	
			double maxEnergy = 0;	
			double totalEnergy = 0;	
			String name = null;	
			 for (double e : Energy.EnergyConsumptionList) {	
				 totalEnergy = totalEnergy + e;	
				 if (maxEnergy < e) {	
					 maxEnergy = e;	
				 }	
				 avgEnergy = totalEnergy / Energy.EnergyConsumptionList.size();	
				 name = Energy.name;				 	
			 }	
			 String metrics = "Name: " + name + " Avg: " + avgEnergy + " Max: " + maxEnergy + " Total: " + totalEnergy;	
			 System.out.println(metrics);	
			 metricsAppend =  metrics;	
			 metricsBox.appendText(metricsAppend + "\n");	
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
			ParsingTuples();
			ParsingTupleDelay();
			psParseLatency();
			energyMetrics();
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
        System.out.println(tupleDelayList.toString());
        XYChart.Series series = new XYChart.Series();  
        for (int i = 0; i < tupleDelayList.size(); i++) {
        	series.getData().add(new XYChart.Data(tupleDelayList.get(i).name,tupleDelayList.get(i).delay));
        }
        barChart.getData().add(series);
		ObservableList<TupleMetrics> tempData = FXCollections.observableArrayList();
		for (int i = 0; i < tupleList.size(); i++) {
        	tempData.add(tupleList.get(i));
        }
    	tupleTable.setItems(tempData);
    	typleCol.setCellValueFactory(new PropertyValueFactory <>("tupleType"));
    	typleCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	timeCol.setCellValueFactory(new PropertyValueFactory <>("average"));
//    	timeCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	destModCol.setCellValueFactory(new PropertyValueFactory <>("tupleDEST"));
    	destModCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	sourceModCol.setCellValueFactory(new PropertyValueFactory <>("tupleSRC"));
    	sourceModCol.setCellFactory(TextFieldTableCell.forTableColumn());
//    	timeCol.setCellValueFactory(new PropertyValueFactory <>("tupleNWLatency"));
//    	timeCol.setCellValueFactory(new PropertyValueFactory <>("tupleNWLatency"));
    	timeCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    	destDevCol.setCellValueFactory(new PropertyValueFactory <>("tupleDESTDev"));
        destDevCol.setCellFactory(TextFieldTableCell.forTableColumn());
        sourceDevCol.setCellValueFactory(new PropertyValueFactory <>("tupleSRCDev"));
        sourceDevCol.setCellFactory(TextFieldTableCell.forTableColumn());
	}
}