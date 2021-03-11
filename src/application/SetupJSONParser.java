package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SetupJSONParser {
	private List<HostSpec> hosts = new ArrayList<HostSpec>();
	private List<LinkSpec> links = new ArrayList<LinkSpec>();
	private List<EdgeSpec> edges = new ArrayList<EdgeSpec>();
	private List<ModuSpec> modules = new ArrayList<ModuSpec>();
	
	public void createTopology(String nodeLine) throws NumberFormatException, IOException {
		
		long insProcessingSpeed = 0;
		int insMemory = 0;
		long insNetworkSpeedUp = 0;
		long insNetworkSpeedDown = 0;
		int insLevel = 0;
		double insCostProcessing;
		double insBusyPower;
		double insIdlePower;
		double insServiceCharge;
		double insCostNetworking;
		
		String stParts[] = nodeLine.split(" ");
		String hostname = stParts[0];
		insProcessingSpeed = (long) Double.parseDouble(stParts[1]);
		insMemory = (int) Double.parseDouble(stParts[2]);
		insNetworkSpeedUp = (long) Double.parseDouble(stParts[3]);
		insNetworkSpeedDown = (long) Double.parseDouble(stParts[4]);
		insLevel = (int) Double.parseDouble(stParts[5]);
		insCostProcessing = Double.parseDouble(stParts[6]);
		// insCostNetworking = Double.parseDouble(stParts[7]);
		insBusyPower = Double.parseDouble(stParts[7]);
		insIdlePower = Double.parseDouble(stParts[8]);
		HostSpec h = addHost(hostname, insProcessingSpeed, insMemory, insNetworkSpeedUp, insNetworkSpeedDown, insLevel,
				insCostProcessing, insBusyPower, insIdlePower);
		// addLink(e, h, latency);
		
	}
	
	public void createEdgeTopology(String edgeLine) throws NumberFormatException, IOException {
		String st = edgeLine;
		
		// String insParent;
		// String insChild;
		// String insTupleType;
		double insPeriodicity;
		double insCpuLength;
		double insNwLength;
		// String insEdgeType;
		int insDirection = 1;
		
		String stParts[] = edgeLine.split(" ");
		String insParent = stParts[0];
		String insChild = stParts[1];
		String insTupleType = stParts[2];
		insPeriodicity = Double.parseDouble(stParts[3]);
		insCpuLength = Double.parseDouble(stParts[4]);
		insNwLength = Double.parseDouble(stParts[5]);
		String insEdgeType = stParts[6];
		insDirection = (int) Double.parseDouble(stParts[7]);
		
		EdgeSpec e = addEdge(insParent, insChild, insTupleType, insPeriodicity, insCpuLength, insNwLength, insEdgeType,
				insDirection);
		
	}
	
	public void createModuleTopology(String moduleLine) throws NumberFormatException, IOException {
		// String insNodeName;
		// String insModuleName;
		int insRam;
		long insBandwidth = 0;
		// String insInTuple;
		// String insOutTuple;
		long insSize = 0;
		int insMIPS;
		double insFractionalSensitivity;
		
		String stParts[] = moduleLine.split(" ");
		String insNodeName = stParts[0];
		String insModuleName = stParts[1];
		insRam = (int) Double.parseDouble(stParts[2]);
		insBandwidth = (long) Double.parseDouble(stParts[3]);
		String insInTuple = stParts[4];
		String insOutTuple = stParts[5];
		insSize = (long) Double.parseDouble(stParts[6]);
		insMIPS = (int) Double.parseDouble(stParts[7]);
		insFractionalSensitivity = Double.parseDouble(stParts[8]);
		
		ModuSpec m = addModule(insNodeName, insModuleName, insRam, insBandwidth, insInTuple, insOutTuple, insSize,
				insMIPS, insFractionalSensitivity);
	}
	
	public HostSpec addHost(String name, long mips, int ram, long upbw, long downbw, int level, double rate,
			double apower, double ipower) {
		HostSpec host = new HostSpec(mips, ram, upbw, downbw, level, rate, apower, ipower);
		host.name = name;
		host.type = "host";
		hosts.add(host);
		return host;
	}
	
	public EdgeSpec addEdge(String parent, String child, String tupleType, double periodicity, double cpuLength,
			double newLength, String edgeType, int direction) {
		EdgeSpec edge = new EdgeSpec(parent, child, tupleType, periodicity, cpuLength, newLength, edgeType, direction);
		edges.add(edge);
		return edge;
	}
	
	public ModuSpec addModule(String nodeName, String moduleName, int modRam, long bandwidth, String inTuple,
			String outTuple, long size, int MIPS, double fractionalSensitivity) {
		ModuSpec module = new ModuSpec(nodeName, moduleName, modRam, bandwidth, inTuple, outTuple, size, MIPS,
				fractionalSensitivity);
		modules.add(module);
		return module;
	}
	
	private void addLink(NodeSpec source, NodeSpec dest, double latency) {
		links.add(new LinkSpec(source.name, dest.name, latency));
	}
	
	class NodeSpec {
		String name;
		String type;
		long upbw;
		long downbw;
	}
	
	class HostSpec extends NodeSpec {
		int pe;
		long mips;
		int ram;
		int level;
		double rate;
		double ipower;
		double apower;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			HostSpec o = this;
			JSONObject obj = new JSONObject();
			obj.put("name", o.name);
			obj.put("mips", o.mips);
			obj.put("ram", o.ram);
			obj.put("up_bw", o.upbw);
			obj.put("down_bw", o.downbw);
			obj.put("level", o.level);
			obj.put("rate", o.rate);
			obj.put("apower", o.apower);
			obj.put("ipower", o.ipower);
			
			return obj;
		}
		
		public HostSpec(long mips, int ram, long upbw, long downbw, int level, double rate, double apower,
				double ipower) {
			this.mips = mips;
			this.ram = ram;
			this.upbw = upbw;
			this.downbw = downbw;
			this.level = level;
			this.rate = rate;
			this.apower = apower;
			this.ipower = ipower;
			
		}
	}
	
	class EdgeSpec {
		String parent;
		String child;
		String tupleType;
		double periodicity;
		double cpuLength;
		double newLength;
		String edgeType;
		int direction = 1;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			EdgeSpec edge = this;
			JSONObject obj = new JSONObject();
			obj.put("src", edge.child);
			obj.put("dest", edge.parent);
			obj.put("tupleType", edge.tupleType);
			obj.put("periodicity", edge.periodicity);
			obj.put("tupleCpuLength", edge.cpuLength);
			obj.put("tupleNwLength", edge.newLength);
			obj.put("edgeType", edge.edgeType);
			obj.put("direction", edge.direction);
			return obj;
		}
		
		public EdgeSpec(String parent, String child, String tupleType, double periodicity, double cpuLength,
				double newLength, String edgeType, int direction) {
			this.parent = parent;
			this.child = child;
			this.tupleType = tupleType;
			this.periodicity = periodicity;
			this.cpuLength = cpuLength;
			this.newLength = newLength;
			this.edgeType = edgeType;
			this.direction = direction;
		}
	}
	
	class LinkSpec {
		String source;
		String destination;
		double latency;
		
		public LinkSpec(String source, String destination, double latency2) {
			this.source = source;
			this.destination = destination;
			this.latency = latency2;
		}
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			LinkSpec link = this;
			JSONObject obj = new JSONObject();
			obj.put("source", link.source);
			obj.put("destination", link.destination);
			obj.put("latency", link.latency);
			return obj;
		}
	}
	
	class ModuSpec {
		String nodeName;
		String moduleName;
		int modRam;
		long bandwidth;
		String inTuple;
		String outTuple;
		long size;
		int MIPS;
		double fractionalSensitivity;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			ModuSpec module = this;
			JSONObject obj = new JSONObject();
			obj.put("Node Name", module.nodeName);
			obj.put("Module Name", module.moduleName);
			obj.put("RAM", module.modRam);
			obj.put("Bandwidth", module.bandwidth);
			obj.put("inTuple", module.inTuple);
			obj.put("outTuple", module.outTuple);
			obj.put("Size", module.size);
			obj.put("MIPS", module.MIPS);
			obj.put("Fractional Sensitivity", module.fractionalSensitivity);
			return obj;
		}
		
		public ModuSpec(String nodeName, String moduleName, int modRam, long bandwidth, String inTuple,
				String outTuple, long size, int MIPS, double fractionalSensitivity) {
			this.nodeName = nodeName;
			this.moduleName = moduleName;
			this.modRam = modRam;
			this.bandwidth = bandwidth;
			this.inTuple = inTuple;
			this.outTuple = outTuple;
			this.size = size;
			this.MIPS = MIPS;
			this.fractionalSensitivity = fractionalSensitivity;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void writeJSON(String jsonFileName) {
		JSONObject obj = new JSONObject();
		
		JSONArray nodeList = new JSONArray();
		JSONArray linkList = new JSONArray();
		JSONArray edgeList = new JSONArray();
		JSONArray moduleList = new JSONArray();
		
		for (HostSpec h : hosts) nodeList.add(h.toJSON());
		for (LinkSpec l : links) linkList.add(l.toJSON());
		for (EdgeSpec e : edges) edgeList.add(e.toJSON());
		for (ModuSpec m : modules) moduleList.add(m.toJSON());
		
//		System.out.println("Hosts:\n" 	+ hosts.toString() 	 + "\n");
//		System.out.println("Links:\n" 	+ links.toString() 	 + "\n");
//		System.out.println("Edges:\n" 	+ edges.toString() 	 + "\n");
//		System.out.println("Modules:\n" + modules.toString() + "\n");
		
		obj.put("nodes", nodeList);
		obj.put("links", linkList);
		obj.put("edges", edgeList);
		obj.put("modules", moduleList);
		
		try {
			FileWriter file = new FileWriter(jsonFileName, true);
			file.write(obj.toJSONString().replaceAll(",", ",\n"));
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(obj);
	}
}
