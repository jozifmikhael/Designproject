package application;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.*;

public class OutputController {
	private static List<Energy> Energylist = new ArrayList<Energy>();
	private static List<String> nodeList = new ArrayList<String>();
	private static List<Double> timeNetworkList = new ArrayList<Double>();
	private static List<Double> NetworkUsageList = new ArrayList<Double>();
	private List<TupleMetrics> tupleList = new ArrayList<TupleMetrics>();
	public List<XYChart.Series> seriesList = new ArrayList<XYChart.Series>();
	
	static String sourceFile="output.json";
	
	@FXML
	private StackedAreaChart EnergyConsumption;
	
	@FXML
	private LineChart NetworkUsage;
	
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
    void ParsingEnergy() throws IOException, ParseException {
    	JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(sourceFile);
        Object obj = jsonParser.parse(reader);
        JSONObject nodeList = (JSONObject) obj;
        JSONArray nodeArr = (JSONArray) nodeList.get("nodes");
        nodeArr.forEach(l -> parseNodeData((JSONObject) l));
        JSONArray timeE = (JSONArray) nodeList.get("listEnergy");
        timeE.forEach(l -> parsetimeEData((JSONObject) l));
        EnergyConsuptionGraph();
    }
	
	@FXML
    void ParsingNetwork() throws IOException, ParseException {
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
	void EnergyConsuptionGraph() {

		final NumberAxis xAxis = new NumberAxis();
	    final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Energy Consumption");
        EnergyConsumption.setTitle("Time vs Energy Consumption");
        
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
        NetworkUsage.setCreateSymbols(false);
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
}