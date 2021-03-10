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
		
		String stParts[] = line.split(" ");
		String tuplename = stParts[0];			
		TupleLatency = Double.parseDouble(stParts[1]);
		TupleSpec h = addTuple(tuplename, TupleLatency);	
	}
	
	public void getNodespec(String line) throws NumberFormatException, IOException {
		
		double cost;
		double energy;
		
		String stParts[] = line.split(" ");
		String nodeName = stParts[0];			
		cost = Double.parseDouble(stParts[1]);
		energy = Double.parseDouble(stParts[2]);
		NodeSpecs n = addNode(nodeName, cost, energy);	
	}
	
	public void getOthers(String line) throws NumberFormatException, IOException {
		
		double cost;
		double energy;
		
		String stParts[] = line.split(" ");
		String nodeName = stParts[0];			
		cost = Double.parseDouble(stParts[1]);
		energy = Double.parseDouble(stParts[2]);
		NodeSpecs n = addNode(nodeName, cost, energy);	
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
	
	public TupleSpec addTuple(String name, double TupleLatency) {
		TupleSpec tuple = new TupleSpec(name, TupleLatency);
		tuple.name = name;
		tuples.add(tuple);
		return tuple;
	}
	
	public NodeSpecs addNode(String nodeName, double cost, double energy) {
		NodeSpecs node = new NodeSpecs(nodeName, cost, energy);
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
		String name;
		double TupleLatency;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			TupleSpec t = this;
			JSONObject obj = new JSONObject();
			obj.put("name", t.name);
			obj.put("TupleLatency", t.TupleLatency);
			return obj;
		}
		
		public TupleSpec(String name, double TupleLatency) {
			this.name = name;
			this.TupleLatency = TupleLatency;			
		}
	}
	
	class NodeSpecs{
		String nodeName;
		double cost;
		double energy;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			NodeSpecs n = this;
			JSONObject obj = new JSONObject();
			obj.put("name", n.nodeName);
			obj.put("cost", n.cost);
			obj.put("energy", n.energy);
			return obj;
		}
		
		public NodeSpecs(String nodeName, double cost, double energy) {
			this.nodeName = nodeName;
			this.cost = cost;		
			this.energy = energy;			
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
		
		System.out.println("Tuples:\n"+tuples.toString()+"\n");
		System.out.println("Nodes:\n"+nodes.toString()+"\n");
		System.out.println("Energy:\n"+listEnergy.toString()+"\n");
		System.out.println("Network:\n"+listNetwork.toString()+"\n");
		
		obj.put("tuples", tupleList);
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
