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

import application._MainWindowController.dispNode;

public class SetupJSONParser {
	private List<DeviceSpec> hosts = new ArrayList<DeviceSpec>();
	private List<ModuEdgeSpec> edges = new ArrayList<ModuEdgeSpec>();
	private List<ModuSpec> modules = new ArrayList<ModuSpec>();
	private int globalID = 0;
	
	public DeviceSpec createDevice(String nodeLine) throws NumberFormatException, IOException {
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
		String parentName = stParts[9];
		DeviceSpec h = new DeviceSpec(hostname, parentName, insProcessingSpeed, insMemory, insNetworkSpeedUp, insNetworkSpeedDown, insLevel,
				insCostProcessing, insBusyPower, insIdlePower);
		return h;
	}
	
	public ModuEdgeSpec createModuleEdge(String edgeLine) throws NumberFormatException, IOException {
		double insPeriodicity;
		double insCpuLength;
		double insNwLength;
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
		
		ModuEdgeSpec e = new ModuEdgeSpec(insParent, insChild, insTupleType, insPeriodicity, insCpuLength, insNwLength, insEdgeType,
				insDirection);
		return e;
	}
	
	public ModuSpec createModule(String moduleLine) throws NumberFormatException, IOException {
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
		
		ModuSpec m = new ModuSpec(insNodeName, insModuleName, insRam, insBandwidth, insInTuple, insOutTuple, insSize, insMIPS,
				insFractionalSensitivity);
		return m;
	}
	
	class NodeSpec {
		String name;
		String parent;
		String type;
		long upbw;
		long downbw;
		int id;
	}
	
	public int getID() {
		return globalID++;
	}
	
	public void popNode(int _id) {
		for(NodeSpec h: hosts) if(h.id == _id) hosts.remove(h);
		for(NodeSpec h: edges) if(h.id == _id) hosts.remove(h);
		for(NodeSpec h: modules) if(h.id == _id) hosts.remove(h);
	}
	
	class DeviceSpec extends NodeSpec {
		int pe;
		long mips;
		int ram;
		int level;
		double rate;
		double ipower;
		double apower;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			DeviceSpec o = this;
			JSONObject obj = new JSONObject();
			obj.put("name", o.name);
			obj.put("parent", o.parent);
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
		
		
		public String toString() {
            String str = this.type + " ID: " + this.id+ "\nNode Name: " + this.name + "\nNode Parent Name:  " + this.parent + "\nUp Bandwidth: " 
        + this.upbw + "\nDown Bandwidth:  " + this.downbw + "\nMIPS: " + this.mips + "\nRAM: " + this.ram + "\nLevel: " + this.level + "\nRate: " + this.rate + "\nIdle Power: " + this.ipower + "\nActive Power: " + this.apower + "\n";
            String str1 = "--------------------------------------------------------------------------------";
            return str+str1;
        }
		
		
		public DeviceSpec(String name, String parent, long mips, int ram, long upbw, long downbw, int level, double rate, double apower,
				double ipower) {
			this.id = getID();
			this.name = name;
			this.parent = parent;
			this.mips = mips;
			this.ram = ram;
			this.upbw = upbw;
			this.downbw = downbw;
			this.level = level;
			this.rate = rate;
			this.apower = apower;
			this.ipower = ipower;
			this.type = "device";
		}
	}
	
	class ModuSpec extends NodeSpec {
		String nodeName;
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
			obj.put("Module Name", module.name);
			obj.put("RAM", module.modRam);
			obj.put("Bandwidth", module.bandwidth);
			obj.put("inTuple", module.inTuple);
			obj.put("outTuple", module.outTuple);
			obj.put("Size", module.size);
			obj.put("MIPS", module.MIPS);
			obj.put("Fractional Sensitivity", module.fractionalSensitivity);
			return obj;
		}
		
		public String toString() {
			String str = this.type+" Node: " + this.nodeName + "\nModule: " + this.name + "\nRAM: " + this.modRam + "\nBW: " +this.bandwidth + "\nIn Tuple: " 
		+ this.inTuple + "OutTuple: " + this.outTuple + "Size: " + this.size + "MIPs: " + this.MIPS + "Sens: "+ this.fractionalSensitivity + "\n";
			String str1 = "--------------------------------------------------------------------------------";
			return str+str1;
		}
		
		public ModuSpec(String nodeName, String moduleName, int modRam, long bandwidth, String inTuple, String outTuple,
				long size, int MIPS, double fractionalSensitivity) {
			this.nodeName = nodeName;
			this.name = moduleName;
			this.modRam = modRam;
			this.bandwidth = bandwidth;
			this.inTuple = inTuple;
			this.outTuple = outTuple;
			this.size = size;
			this.MIPS = MIPS;
			this.fractionalSensitivity = fractionalSensitivity;
			this.id = getID();
			this.type = "module";
		}
	}
	
	class ModuEdgeSpec extends NodeSpec {
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
			ModuEdgeSpec edge = this;
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
		
		public String toString() {
			String str = this.parent+ " " + this.child + " " + this.tupleType + " " 
		+this.periodicity + " " + this.cpuLength + " " + this.newLength + " " + this.edgeType + " " + this.direction + "\n";
			String str1 = "--------------------------------------------------------------------------------";
			return str+str1;
		}
		
		public ModuEdgeSpec(String parent, String child, String tupleType, double periodicity, double cpuLength,
				double newLength, String edgeType, int direction) {
			this.parent = parent;
			this.child = child;
			this.tupleType = tupleType;
			this.periodicity = periodicity;
			this.cpuLength = cpuLength;
			this.newLength = newLength;
			this.edgeType = edgeType;
			this.direction = direction;
			this.id = getID();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void writeJSON(String jsonFileName,
			List<DeviceSpec> devicesList, List<ModuSpec> modulesList, List<ModuEdgeSpec> modEdgesList,
			int time, String policy) {
		JSONObject obj = new JSONObject();
		JSONArray nodeList = new JSONArray();
		JSONArray edgeList = new JSONArray();
		JSONArray moduleList = new JSONArray();
		
		for (DeviceSpec h : devicesList) nodeList.add(h.toJSON());
		for (ModuSpec m : modulesList) moduleList.add(m.toJSON());
		for (ModuEdgeSpec e : modEdgesList) edgeList.add(e.toJSON());

		obj.put("nodes", nodeList);
		obj.put("modules", moduleList);
		obj.put("edges", edgeList);		
		obj.put("time", time);
		obj.put("policy", policy);
		
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

