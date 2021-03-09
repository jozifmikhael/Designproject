package org.fog.test.perfeval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TextParser {
	private List<OutputSpec> outputs = new ArrayList<OutputSpec>();
	
	private static BufferedReader br;
	public static void main(String [] argv) throws NumberFormatException, IOException {
		String sourceFileName = "consolefile.txt";
		String jsonFileName = "output.json";
		
		TextParser reqg = new TextParser();
		reqg.getInput(sourceFileName);
		reqg.writeJSON(jsonFileName);
	}
	
	public void getInput(String fileName) throws NumberFormatException, IOException {
		File file = new File(fileName);
		br = new BufferedReader(new FileReader(file));
		String st;

		String insTupleName1;
		String insTupleName2;
		String insTupleName3;
		String insTupleName4;
		String insTupleName5;
		double insTupleTime1;
		double insTupleTime2;
		double insTupleTime3;
		double insTupleTime4;
		double insTupleTime5;
		
		String insNodeName1;
		String insNodeName2;
		String insNodeName3;
		double insNodeEnergy1;
		double insNodeEnergy2;
		double insNodeEnergy3;
		double insTotalNodeEnergy;
		double insTotalNetworkUsage;
		double insNodeCost1;
		double insNodeCost2;
		double insNodeCost3;
		double insTotalExecCost;
		
		double insExecTime;
		double insCalcTotalTime;
		double insCalcTimeWDelay;
		
		while ((st = br.readLine()) != null) {
			String stParts[] = st.split(" ");
			insTupleName1 = stParts[0];
			insTupleTime1 = Double.parseDouble(stParts[1]);
			insTupleName2 = stParts[2];
			insTupleTime2 = Double.parseDouble(stParts[3]);
			insTupleName3 = stParts[4];
			insTupleTime3 = Double.parseDouble(stParts[5]);
			insTupleName4 = stParts[6];
			insTupleTime4 = Double.parseDouble(stParts[7]);
			insTupleName5 = stParts[8];
			insTupleTime5 = Double.parseDouble(stParts[9]);
			insExecTime = Double.parseDouble(stParts[10]);
			insCalcTotalTime = Double.parseDouble(stParts[11]);
			insCalcTimeWDelay = Double.parseDouble(stParts[12]);
			insNodeName1 = stParts[13];
			insNodeEnergy1 = Double.parseDouble(stParts[14]);
			insNodeName2 = stParts[15];
			insNodeEnergy2 = Double.parseDouble(stParts[16]);
			insNodeName3 = stParts[17];			
			insNodeEnergy3 = Double.parseDouble(stParts[18]);			
			insTotalNodeEnergy = Double.parseDouble(stParts[19]);
			insTotalNetworkUsage = Double.parseDouble(stParts[20]);
			insNodeName1 = stParts[21];
			insNodeCost1 = Double.parseDouble(stParts[22]);
			insNodeName2 = stParts[23];
			insNodeCost2 = Double.parseDouble(stParts[24]);
			insNodeName3 = stParts[25];
			insNodeCost3 = Double.parseDouble(stParts[26]);
			insTotalExecCost = Double.parseDouble(stParts[27]);

			OutputSpec o = addOutput(insTupleName1, insTupleName2, insTupleName3, insTupleName4, insTupleName5, insTupleTime1, insTupleTime2, insTupleTime3, 
					insTupleTime4, insTupleTime5, insExecTime, insCalcTotalTime, insCalcTimeWDelay, insNodeName1, insNodeName2, insNodeName3, insNodeEnergy1, 
					insNodeEnergy2, insNodeEnergy3, insTotalNodeEnergy, insTotalNetworkUsage, insNodeCost1, insNodeCost2, insNodeCost3, insTotalExecCost);
					
		}
	}
	
	public OutputSpec addOutput(String TupleName1, String TupleName2, String TupleName3,String TupleName4,String TupleName5, double TupleTime1, double TupleTime2, 
			 double TupleTime3, double TupleTime4, double TupleTime5, double ExecTime, double CalcTotalTime, double CalcTimeWDelay, String NodeName1, String NodeName2,  String NodeName3, double NodeEnergy1, double NodeEnergy2, 
			 double NodeEnergy3, double TotalNodeEnergy, double TotalNetworkUsage, double NodeCost1, double NodeCost2, double NodeCost3, double TotalExecCost) {
		
		OutputSpec output = new OutputSpec(TupleName1, TupleName2, TupleName3,TupleName4,TupleName5, TupleTime1, TupleTime2, 
			 TupleTime3, TupleTime4, TupleTime5, ExecTime, CalcTotalTime, CalcTimeWDelay, NodeName1, NodeName2, NodeName3, NodeEnergy1, NodeEnergy2, 
			 NodeEnergy3, TotalNodeEnergy, TotalNetworkUsage, NodeCost1, NodeCost2, NodeCost3, TotalExecCost);
		
		outputs.add(output);
		return output;
	}
	
	class OutputSpec {
		String TupleName1;String TupleName2;String TupleName3;String TupleName4;String TupleName5;double TupleTime1;double TupleTime2;
		 double TupleTime3;double TupleTime4;double TupleTime5;double ExecTime; double CalcTotalTime; double CalcTimeWDelay; String NodeName1;String NodeName2; String NodeName3;double NodeEnergy1;double NodeEnergy2;
		 double NodeEnergy3;double TotalNodeEnergy;double TotalNetworkUsage;double NodeCost1;double NodeCost2;double NodeCost3; double TotalExecCost;
	
	@SuppressWarnings("unchecked")
	JSONObject toJSON() {
		OutputSpec o = this;
		JSONObject obj = new JSONObject();
		obj.put("First Tuple Name", o.TupleName1);
		obj.put("First Tuple Time", o.TupleTime1);
		obj.put("Second Tuple Name", o.TupleName2);
		obj.put("Second Tuple Time", o.TupleTime2);
		obj.put("Third Tuple Name", o.TupleName3);
		obj.put("Third Tuple Time", o.TupleTime3);
		obj.put("Fourth Tuple Name", o.TupleName4);
		obj.put("Fourth Tuple Time", o.TupleTime4);
		obj.put("Fifth Tuple Name", o.TupleName5);
		obj.put("Fifth Tuple Time", o.TupleTime5);
		obj.put("Execution Time", o.ExecTime);
		obj.put("Calculated Total Time", o.CalcTotalTime);
		obj.put("Calculated Total Time with Delays", o.CalcTimeWDelay);
		obj.put("First Node Name", o.NodeName1);
		obj.put("First Node Energy", o.NodeEnergy1);
		obj.put("Second Node Name", o.NodeName2);
		obj.put("Second Node Energy", o.NodeEnergy2);
		obj.put("Third Node Name", o.NodeName3);
		obj.put("Third Node Energy", o.NodeEnergy3);
		obj.put("Total Node Energy Usage", o.TotalNodeEnergy);
		obj.put("Total Network Usage", o.TotalNetworkUsage);
		obj.put("First Node Name", o.NodeName1);
		obj.put("First Node Cost", o.NodeCost1);
		obj.put("Second Node Name", o.NodeName2);
		obj.put("Second Node Cost", o.NodeCost2);
		obj.put("Third Node Name", o.NodeName3);
		obj.put("Third Node Cost", o.NodeCost3);
		obj.put("Total Node Execution Cost", o.TotalExecCost);
		return obj;	 
	}
	
	public OutputSpec(String TupleName1, String TupleName2, String TupleName3,String TupleName4,String TupleName5, double TupleTime1, double TupleTime2, 
			 double TupleTime3, double TupleTime4, double TupleTime5, double ExecTime, double CalcTotalTime, double CalcTimeWDelay, String NodeName1, String NodeName2,  String NodeName3, double NodeEnergy1, double NodeEnergy2, 
			 double NodeEnergy3, double TotalNodeEnergy, double TotalNetworkUsage, double NodeCost1, double NodeCost2, double NodeCost3, double TotalExecCost) {
		this.TupleName1 = TupleName1;
		this.TupleName2 = TupleName2 ; 
		this.TupleName3 = TupleName3; 
		this.TupleName4 = TupleName4; 
		this.TupleName5 =  TupleName5; 
		this.TupleTime1 =  TupleTime1; 
		this.TupleTime2 =  TupleTime2; 
		this.TupleTime3 =  TupleTime3; 
		this.TupleTime4 =  TupleTime4; 
		this.TupleTime5 =  TupleTime5; 
		this.ExecTime = ExecTime;
		this.CalcTotalTime = CalcTotalTime;
		this.CalcTimeWDelay = CalcTimeWDelay;
		this.NodeName1 =  NodeName1; 
		this.NodeName2 =  NodeName2; 
		this.NodeName3 =  NodeName3; 
		this.NodeEnergy1 =  NodeEnergy1; 
		this.NodeEnergy2 =  NodeEnergy2; 
		this.NodeEnergy3 =  NodeEnergy3; 
		this.TotalNodeEnergy =  TotalNodeEnergy; 
		this.TotalNetworkUsage =  TotalNetworkUsage; 
		this.NodeCost1 =  NodeCost1; 
		this.NodeCost2 =  NodeCost2; 
		this.NodeCost3 =  NodeCost3; 
		this.TotalExecCost = TotalExecCost;
		
		}
	}
	
	@SuppressWarnings("unchecked")
	public void writeJSON(String jsonFileName) {
		JSONObject obj = new JSONObject();
		
		JSONArray outputList = new JSONArray();
		
		for (OutputSpec o:outputs) outputList.add(o.toJSON());
		
		System.out.println("Outputs:\n"+outputs.toString()+"\n");
		
		obj.put("Outputs", outputList);
		
		try {
			FileWriter file = new FileWriter(jsonFileName, false);
			file.write(obj.toJSONString().replaceAll(",", ",\n"));
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(obj);
				
		
	}
	
}
