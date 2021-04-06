package org.fog.test.perfeval;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TextParser {
	private List<NodeSpecs> nodesList = new ArrayList<NodeSpecs>();
	private List<EnergySpec> energiesList = new ArrayList<EnergySpec>();
	private List<NetworkSpec> networkingList = new ArrayList<NetworkSpec>();
	
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
	
	public NodeSpecs addNode(String nodeName) {
		NodeSpecs node = new NodeSpecs(nodeName);
		node.nodeName = nodeName;
		nodesList.add(node);
		return node;
	}

	public EnergySpec addEnergy(String name, double energy, double time, double cost) {
		EnergySpec nodeEngery = new EnergySpec(name, energy, time, cost);
		nodeEngery.name = name;
		energiesList.add(nodeEngery);
		return nodeEngery;
	}
	
	public NetworkSpec addNetwork(double time, double networkUsage) {
		NetworkSpec network = new NetworkSpec(time, networkUsage);
		networkingList.add(network);
		return network;
	}
	
	static List<TupleSpec> globalTuplesList = new ArrayList<TupleSpec>();
	public class filterableTuples{
		List<TupleSpec> localList;
		public filterableTuples() {
			this.localList = new ArrayList<TupleSpec>();
			this.localList.addAll(globalTuplesList);
		}
		public filterableTuples updateTo(List<TupleSpec> newList) {
			localList = newList;
			return this;
		}
		public filterableTuples ofType(String reqType){
			return this.updateTo(localList.stream()
					.filter(t -> t.tupleType.equals(reqType))
					.collect(Collectors.toList()));
		}
		public filterableTuples ofSrc(String reqSrc){
			return this.updateTo(localList.stream()
					.filter(t -> t.tupleType.equals(reqSrc))
					.collect(Collectors.toList()));
		}
		public filterableTuples ofDst(String reqDst){
			return this.updateTo(localList.stream()
					.filter(t -> t.tupleType.equals(reqDst))
					.collect(Collectors.toList()));
		}
		public filterableTuples tholdSent(double reqSent, boolean dir){
			return this.updateTo(localList.stream()
					.filter(t -> dir?t.tupleSentTime>=reqSent:t.tupleSentTime<=reqSent)
					.collect(Collectors.toList()));
		}
		public filterableTuples tholdArrived(double reqArrived, boolean dir){
			return this.updateTo(localList.stream()
					.filter(t -> dir?t.tupleArrivTime>=reqArrived:t.tupleArrivTime<=reqArrived)
					.collect(Collectors.toList()));
		}
		public filterableTuples printNWLatencies() {
			for(TupleSpec t : this.localList) System.out.println(t.tupleArrivTime - t.tupleSentTime);
			return this;
		}
		public filterableTuples printAverage() {
			double avg=0;
			int count=0;
			for(TupleSpec t : this.localList) {
				avg+=(t.tupleArrivTime-t.tupleSentTime);
				count++;
			}	
			System.out.println(avg/count);
			return this;
		}
		public filterableTuples printVariance() {
			double avg=0;
			for(TupleSpec t : this.localList) avg+=(t.tupleArrivTime-t.tupleSentTime);
			avg/=this.localList.size();
			double var=0;
			for(TupleSpec t : this.localList) var+=Math.pow((t.tupleArrivTime-t.tupleSentTime-avg),2);
			System.out.println(var/this.localList.size());
			return this;
		}
		public filterableTuples printStrs() {
			double avg=0;
			for(TupleSpec t : this.localList) System.out.println(t.toString());
			return this;
		}
	}
	//filterableTuples newQuery = new filterableTuples;
	//newQuery.ofType("PLAYER_GAME_STATE").ofDst("wherever");
	
	public class TupleSpec{
		@Override
		public String toString() {
			return "TupleSpec [tupleType=" + tupleType + ", tupleSrc=" + tupleSrc + ", tupleDst=" + tupleDst
					+ ", tupleNWLatency=" + tupleNWLatency + ", tupleSentTime=" + tupleSentTime + ", tupleArrivTime="
					+ tupleArrivTime + "]";
		}
		String tupleType;
		String tupleSrc; 
		String tupleDst;
		double tupleNWLatency;
		double tupleSentTime;
		double tupleArrivTime;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			TupleSpec t = this;
			JSONObject obj = new JSONObject();
			obj.put("tupleType", t.tupleType);
			obj.put("tupleSRC", t.tupleSrc);
			obj.put("tupleDEST", t.tupleDst);
			obj.put("tupleNWLatency", t.tupleNWLatency);
			return obj;
		}
		public TupleSpec(String tupleType, String tupleSRC, String tupleDEST, double sentTime, double arrivalTime) {
			this.tupleType = tupleType;
			this.tupleSrc = tupleSRC;
			this.tupleDst = tupleDEST;
			this.tupleSentTime = sentTime;
			this.tupleArrivTime = arrivalTime;
			this.tupleNWLatency = arrivalTime-sentTime;
			globalTuplesList.add(this);
		}
		
		
	}
	public void logTuple(String tupleType, String tupleSRC, String tupleDEST, double sentTime, double arrivalTime) {
		this.new TupleSpec(tupleType, tupleSRC, tupleDEST, sentTime, arrivalTime);
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
		
		JSONArray tupleJList = new JSONArray();
		JSONArray nodeJList = new JSONArray();
		JSONArray energyJList = new JSONArray();
		JSONArray networkJList = new JSONArray();
		
		
		for (TupleSpec t:globalTuplesList) tupleJList.add(t.toJSON());
		for (NodeSpecs n:nodesList) nodeJList.add(n.toJSON());
		for (EnergySpec e:energiesList) energyJList.add(e.toJSON());
		for (NetworkSpec w:networkingList) networkJList.add(w.toJSON());
		
//		System.out.println("Tuples:\n"+tuplesList.toString()+"\n");
//		System.out.println("Nodes:\n"+nodesList.toString()+"\n");
//		System.out.println("Energy:\n"+energiesList.toString()+"\n");
//		System.out.println("Network:\n"+networkingList.toString()+"\n");
		
//		obj.put("tuples", tupleJList);
		obj.put("nodes", nodeJList);
		obj.put("listEnergy", energyJList);
		obj.put("listNetwork", networkJList);
		
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