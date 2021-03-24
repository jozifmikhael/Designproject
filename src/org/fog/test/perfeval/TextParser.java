package org.fog.test.perfeval;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TextParser {
	private List<TupleSpec> tuples = new ArrayList<TupleSpec>();
	private List<NodeSpecs> nodes = new ArrayList<NodeSpecs>();
	private List<EnergySpec> listEnergy = new ArrayList<EnergySpec>();
	private List<NetworkSpec> listNetwork = new ArrayList<NetworkSpec>();
	
	
	public void getTuples(String line) throws NumberFormatException, IOException {
		
		double TupleLatency;
		double arrivalTime;
		double sendTime;
		
		String stParts[] = line.split(" ");
		String tupleTyple = stParts[0];
		String tupleSRC = stParts[1];
		String tupleDEST = stParts[2];
		sendTime = Double.parseDouble(stParts[3]);
		arrivalTime = Double.parseDouble(stParts[4]);
		TupleLatency = arrivalTime - sendTime;
		//System.out.println("Source " + tupleSRC + " " + "DEST " + tupleDEST + " " +TupleLatency);
		
		TupleSpec h = addTuple(tupleTyple, tupleSRC, tupleDEST, TupleLatency);	
	}
	
	public void getNodespec(String line) throws NumberFormatException, IOException {
		
		double cost;
		double energy;
		
		String stParts[] = line.split(" ");
		String nodeName = stParts[0];			
		NodeSpecs n = addNode(nodeName);	
	}
	
	public void getEnergy(String line) throws NumberFormatException, IOException {
		
		double energy;
		double time;
		double cost;
		
		String stParts[] = line.split(" ");
		String name = stParts[0];			
		energy = Double.parseDouble(stParts[1]);
		time = Double.parseDouble(stParts[2]);
		cost = Double.parseDouble(stParts[3]);
		EnergySpec e = addEnergy(name, energy, time,cost);	
	}
	
	public void getNetwork(String line) throws NumberFormatException, IOException {
		
		double time;
		double networkUsage;
		
		String stParts[] = line.split(" ");		
		time = Double.parseDouble(stParts[0]);
		networkUsage = Double.parseDouble(stParts[1]);
		NetworkSpec w = addNetwork(time, networkUsage);	
	}
	
	public TupleSpec addTuple(String tupleTyple, String tupleSRC, String tupleDEST, double TupleLatency) {
		TupleSpec tuple = new TupleSpec(tupleTyple,tupleSRC,tupleDEST, TupleLatency);
		tuple.tupleTyple = tupleTyple;
		tuples.add(tuple);
		return tuple;
	}
	
	public NodeSpecs addNode(String nodeName) {
		NodeSpecs node = new NodeSpecs(nodeName);
		node.nodeName = nodeName;
		nodes.add(node);
		return node;
	}

	public EnergySpec addEnergy(String name, double energy, double time, double cost) {
		EnergySpec nodeEngery = new EnergySpec(name, energy, time, cost);
		nodeEngery.name = name;
		listEnergy.add(nodeEngery);
		return nodeEngery;
	}
	
	public NetworkSpec addNetwork(double time, double networkUsage) {
		NetworkSpec network = new NetworkSpec(time, networkUsage);
		listNetwork.add(network);
		return network;
	}
	
	class TupleSpec{
		String tupleTyple;
		String tupleSRC; 
		String tupleDEST;
		double TupleLatency;
		
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			TupleSpec t = this;
			JSONObject obj = new JSONObject();
			obj.put("tupleType", t.tupleTyple);
			obj.put("tupleSRC", t.tupleSRC);
			obj.put("tupleDEST", t.tupleDEST);
			obj.put("TupleLatency", t.TupleLatency);
			return obj;
		}
		
		public TupleSpec(String tupleTyple, String tupleSRC, String tupleDEST, double TupleLatency) {
			this.tupleTyple = tupleTyple;
			this.tupleSRC = tupleSRC;
			this.tupleDEST = tupleDEST;
			this.TupleLatency = TupleLatency;			
		}
	}
	
	class NodeSpecs{
		String nodeName;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			NodeSpecs n = this;
			JSONObject obj = new JSONObject();
			obj.put("name", n.nodeName);
			return obj;
		}
		
		public NodeSpecs(String nodeName) {
			this.nodeName = nodeName;		
		}
	}
	
	class EnergySpec{
		String name;
		double energy;
		double time;
		double cost;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			EnergySpec e = this;
			JSONObject obj = new JSONObject();
			obj.put("name", e.name);
			obj.put("energy", e.energy);
			obj.put("time", e.time);
			obj.put("cost", e.cost);
			return obj;
		}
		
		public EnergySpec(String name, double energy, double time, double cost) {
			this.name = name;
			this.energy = energy;		
			this.time = time;		
			this.cost = cost;
		}
	}
	
	class NetworkSpec{
		double time;
		double networkUsage;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			NetworkSpec w = this;
			JSONObject obj = new JSONObject();
			obj.put("time", w.time);
			obj.put("networkUsage", w.networkUsage);
			return obj;
		}
		
		public NetworkSpec(double time, double networkUsage) {
			this.time = time;		
			this.networkUsage = networkUsage;			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void writeJSON(String jsonFileName) {
		JSONObject obj = new JSONObject();
		
		JSONArray tupleList = new JSONArray();
		JSONArray nodeList = new JSONArray();
		JSONArray energyList = new JSONArray();
		JSONArray networkList = new JSONArray();
		
		for (TupleSpec t:tuples) tupleList.add(t.toJSON());
		for (NodeSpecs n:nodes) nodeList.add(n.toJSON());
		for (EnergySpec e:listEnergy) energyList.add(e.toJSON());
		for (NetworkSpec w:listNetwork) networkList.add(w.toJSON());
		
//		System.out.println("Tuples:\n"+tuples.toString()+"\n");
//		System.out.println("Nodes:\n"+nodes.toString()+"\n");
//		System.out.println("Energy:\n"+listEnergy.toString()+"\n");
//		System.out.println("Network:\n"+listNetwork.toString()+"\n");
		
		//obj.put("tuples", tupleList);
		obj.put("nodes", nodeList);
		obj.put("listEnergy", energyList);
		obj.put("listNetwork", networkList);
		
		try {
			FileWriter file = new FileWriter(jsonFileName, true);
			file.write(obj.toJSONString().replaceAll(",", ",\n"));
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(obj);
				
		
	}
	
}