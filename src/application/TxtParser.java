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

public class TxtParser {
	private List<HostSpec> hosts = new ArrayList<HostSpec>();
	private List<LinkSpec> links = new ArrayList<LinkSpec>();
	private List<EdgeSpec> edges = new ArrayList<EdgeSpec>();
	private List<ModuleSpec> modules = new ArrayList<ModuleSpec>();
	
	private static BufferedReader br;
	public static void main(String [] argv) throws NumberFormatException, IOException {
		String sourceFileName = "instances.txt";
		String jsonFileName = "test6.json";
		
		TxtParser reqg = new TxtParser();
		reqg.createTopology(sourceFileName);
		reqg.writeJSON(jsonFileName);
	}

	public void createTopology(String fileName) throws NumberFormatException, IOException {
		File file = new File(fileName);
		br = new BufferedReader(new FileReader(file));
		String st;

		long insProcessingSpeed=0;	
		int insMemory=0;
		long insNetworkSpeedUp=0;
		long insNetworkSpeedDown=0;		
		int insLevel=0;			
		double insCostProcessing;		
		double insBusyPower;
		double insIdlePower;
		double insServiceCharge;
		double insCostNetworking;
		
		while ((st = br.readLine()) != null) {
			String stParts[] = st.split(" ");
			String hostname = stParts[0];			
			insProcessingSpeed = (long)Double.parseDouble(stParts[1]);
			insMemory = (int)Double.parseDouble(stParts[2]);
			insNetworkSpeedUp = (long)Double.parseDouble(stParts[3]);
			insNetworkSpeedDown = (long)Double.parseDouble(stParts[4]);
			insLevel = (int)Double.parseDouble(stParts[5]);
			insCostProcessing = Double.parseDouble(stParts[6]);
			//insCostNetworking = Double.parseDouble(stParts[7]);
			insBusyPower = Double.parseDouble(stParts[7]);
			insIdlePower = Double.parseDouble(stParts[8]);
			HostSpec h = addHost(hostname, insProcessingSpeed, insMemory, insNetworkSpeedUp, insNetworkSpeedDown, insLevel, insCostProcessing, insBusyPower, insIdlePower);
			//addLink(e, h, latency);
		}
	}

	public HostSpec addHost(String name, long mips, int ram, long upbw, long downbw, int level, double rate, double apower, double ipower) {
		HostSpec host = new HostSpec(mips, ram, upbw, downbw, level, rate, apower, ipower);
		host.name = name;
		host.type = "host";
		hosts.add(host);
		return host;
	}
	/*
	public ModuleSpec addModule(String name, int ram, int mips, long size, long bw) {
		ModuleSpec module = new ModuleSpec(name, ram, mips, size, bw);
		module.name = "name";
		module.type = "module";
		modules.add(module);
		return module;
	}
	*/
	
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
			obj.put("apower", o.apower);
			obj.put("ipower", o.ipower);
			obj.put("mips", o.mips);
			obj.put("ram", o.ram);
			obj.put("up_bw", o.upbw);
			obj.put("name", o.name);
			obj.put("down_bw", o.downbw);
			obj.put("level", o.level);
			obj.put("rate", o.rate);
			return obj;
		}
		public HostSpec(long mips, int ram, long upbw, long downbw, int level, double rate, double apower, double ipower) {
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
			obj.put("periodicity", edge.periodicity);
			obj.put("tupleCpuLength", edge.cpuLength);
			obj.put("tupleNwLength", edge.newLength);
			obj.put("tupleType", edge.edgeType);
			obj.put("direction", edge.direction);
			obj.put("edgeType", edge.edgeType);
			return obj;
		}
	}
	class LinkSpec {
		String source;
		String destination;
		double latency;
		
		public LinkSpec(String source,String destination,double latency2) {
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
	
	
	class ModuleSpec {
		
		String nodeName;
		String moduleName;
		int ram;
		long bandwidth;
		String inTuple;
		String outTuple;
		long size;
		int MIPS;
		double fractionalSensitivity;
		@SuppressWarnings("unchecked") 
		JSONObject toJSON() {
			ModuleSpec module = this;
			JSONObject obj = new JSONObject();
			obj.put("name", module.moduleName);
			obj.put("ram", module.ram);
			obj.put("mips", module.MIPS);
			obj.put("size", module.size);
			obj.put("bw", module.bandwidth);
			return obj;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void writeJSON(String jsonFileName) {
		JSONObject obj = new JSONObject();

		JSONArray nodeList = new JSONArray();
		JSONArray linkList = new JSONArray();
		
		for(HostSpec h:hosts) nodeList.add(h.toJSON());
		for(LinkSpec l:links) linkList.add(l.toJSON());

		System.out.println("Hosts:\n"+hosts.toString()+"\n");
		System.out.println("Links:\n"+links.toString()+"\n");
		
		obj.put("nodes", nodeList);
		obj.put("links", linkList);
	 
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

