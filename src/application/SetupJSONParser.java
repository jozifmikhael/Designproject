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

import application.SetupJSONParser.ActuatorSpec;
import application.SetupJSONParser.DeviceSpec;
import application.SetupJSONParser.LinkSpec;
import application.SetupJSONParser.ModuEdgeSpec;
import application.SetupJSONParser.ModuSpec;
import application.SetupJSONParser.NodeSpec;
import application.SetupJSONParser.SensorSpec;
//import application.SetupJSONParser.dispNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SetupJSONParser {
	private List<DeviceSpec> hosts = new ArrayList<DeviceSpec>();
	private List<ModuEdgeSpec> edges = new ArrayList<ModuEdgeSpec>();
	private List<ModuSpec> modules = new ArrayList<ModuSpec>();
	private List<LinkSpec> links = new ArrayList<LinkSpec>();
	private List<ActuatorSpec> actuators = new ArrayList<ActuatorSpec>();
	static _MainWindowController mainWindowObj = new _MainWindowController();
	Color deviceColor=Color.RED;
	Color moduleColor=Color.CYAN;
	Color sensorColor=Color.PINK;
	Color actuatorColor=Color.ORANGE;
	Color transpColor=Color.TRANSPARENT;
	Color _errorColor=Color.GREEN;
	//public List<dispNode> dispNodesList = new ArrayList<dispNode>();
	//public List<dispLink> dispLinksList = new ArrayList<dispLink>();
	public List<String> selectedModulesList = new ArrayList<String>();
	
	public List<SensorSpec> sensorsList = new ArrayList<SensorSpec>();
	public List<ActuatorSpec> actuatorsList = new ArrayList<ActuatorSpec>();
	public List<LinkSpec> linksList = new ArrayList<LinkSpec>();
	
	public List<DeviceSpec> devicesList = new ArrayList<DeviceSpec>();
	public List<ModuSpec> modulesList = new ArrayList<ModuSpec>();
	public List<ModuEdgeSpec> moduleEdgesList = new ArrayList<ModuEdgeSpec>();
	
	private int globalID = 0;
	
	public ActuatorSpec createActuator(String actuatorName) {			
		ActuatorSpec a = new ActuatorSpec(actuatorName);		
		return a;
	}
	
	public ActuatorSpec createActuator(String actuatorName, double x, double y, double d) {			
		ActuatorSpec a = new ActuatorSpec(actuatorName, x, y, d);		
		return a;
	}
	
	public ActuatorSpec createActuator(String name, Color actuatorColor, double x, double y, double d) {
		ActuatorSpec a = new ActuatorSpec(name, actuatorColor, x, y, d);
		return a;
	}
	
	public DeviceSpec createDevice(String name, String parent, long mips, int ram, long upbw, long downbw, int level, double rate, 
			double apower, double ipower, double latency) {
		DeviceSpec h = new DeviceSpec(name, parent, mips, ram, upbw, downbw, level, rate, apower, ipower, latency);
		return h;
	}
	
	public DeviceSpec createDevice(String name, String parent, long mips, int ram, long upbw, long downbw, int level, double rate, 
			double apower, double ipower, double latency, double x, double y, double r) {
		DeviceSpec h = new DeviceSpec(name, parent, mips, ram, upbw, downbw, level, rate, apower, ipower, latency, x, y, r);
		return h;
	}
	
	public DeviceSpec createDevice(String name, Color deviceColor, double x, double y, double d) {
		DeviceSpec h = new DeviceSpec(name, deviceColor, x, y, d);
		return h;
	}
	
	public ModuSpec createModule(String nodeName, String moduleName, int modRam, long bandwidth, String inTuple, String outTuple, long size, int MIPS, double fractionalSensitivity, double x, double y, double r) {
		ModuSpec m = new ModuSpec(nodeName, moduleName, modRam, bandwidth, inTuple, outTuple, size, MIPS, fractionalSensitivity, x, y, r);
		return m;
	}
	
	public ModuSpec createModule(String nodeName, String moduleName, int modRam, long bandwidth, String inTuple, String outTuple, long size, int MIPS, double fractionalSensitivity) {
		ModuSpec m = new ModuSpec(nodeName, moduleName, modRam, bandwidth, inTuple, outTuple, size, MIPS, fractionalSensitivity);
		return m;
	}
	
	public ModuSpec createModule(String name, Color moduleColor, double x, double y, double d) {
		ModuSpec m = new ModuSpec(name, moduleColor, x, y, d);
		return m;
	}
	
	
	public SensorSpec createSensor(String sensorName, String sensorParent, double latency, double deterministicValue, 
			double normalMean, double normalStdDev, double uniformMax, double uniformMin, double x, double y, double d)  {
		SensorSpec s = new SensorSpec(sensorName, sensorParent, latency, deterministicValue, normalMean, normalStdDev, uniformMax, uniformMin, x, y, d);
		return s;
	}
	
	public SensorSpec createSensor(String sensorName, String sensorParent, double latency, double deterministicValue, 
			double normalMean, double normalStdDev, double uniformMax, double uniformMin)  {
		SensorSpec s = new SensorSpec(sensorName, sensorParent, latency, deterministicValue, normalMean, normalStdDev, uniformMax, uniformMin);
		return s;
	}
	
	public SensorSpec createSensor(String name, Color sensorColor, double x, double y, double d) {
		SensorSpec s = new SensorSpec(name, sensorColor, x, y, d);
		return s;
	}
	

	public int canLink(NodeSpec _nodeSrc, NodeSpec _nodeDst) {
		if (_nodeSrc.type.equals("device")&&_nodeDst.type.equals("device")) return 1;
		else if (_nodeSrc.type.equals("module")&&_nodeDst.type.equals("module")) return 2;
		else if (_nodeSrc.type.equals("sensor")&&_nodeDst.type.equals("module")) return 3;
		else if (_nodeSrc.type.equals("module")&&_nodeDst.type.equals("actuator")) return 4;
		else return 0;
	}
	
	public LinkSpec createLink(String srcID, String dstID, double latency) {
		LinkSpec l = new LinkSpec(srcID, dstID, latency);		
		return l;
	}
	
	public ModuEdgeSpec createModuleEdge(String parent, String child, String tupleType, double periodicity, double cpuLength, double newLength, String edgeType, int direction) {
		ModuEdgeSpec e = new ModuEdgeSpec(parent, child, tupleType, periodicity, cpuLength, newLength, edgeType, direction);
		return e;
	}

	
	public int getID() {
		return globalID++;
	}
	
	public void popNodeByID (int _id) {
		for(NodeSpec h: hosts) if(h.id == _id) hosts.remove(h);
		for(NodeSpec h: edges) if(h.id == _id) hosts.remove(h);
		for(NodeSpec h: modules) if(h.id == _id) hosts.remove(h);
	}

	public class NodeSpec {
		String name;
		String parent;
		String type;
		long upbw;
		long downbw;
		int id;
		double x;
		double y;
		double dispSize;
		GraphicsContext gc;
		double zoomFactor=1;
		int fontSize = 16;
		Color transpColor=Color.TRANSPARENT;
		NodeSpec data;
		NodeSpec src;
		
		void drawLink(GraphicsContext gc) {
			NodeSpec dst;
			List<NodeSpec> dsts = getDsts(this.name);
			if (this.type.equals("device")) dsts.add(getDevice(this.parent));			
			if (this.type.equals("module")|| this.type.equals("sensor")) dsts.addAll(getDsts(this.name));			
			for (NodeSpec n : dsts) {
				gc.beginPath();
			    gc.moveTo(this.x,this.y);
				gc.lineTo(n.x, n.y);
				gc.stroke();
			}
		}
		void drawNode(GraphicsContext gc) {
			if (this.type.equals("device")) gc.setFill(deviceColor);	
			else if (this.type.equals("module")) gc.setFill(moduleColor);
			else if (this.type.equals("sensor")) gc.setFill(sensorColor);
			else if (this.type.equals("actuator")) gc.setFill(actuatorColor);
			else gc.setFill(_errorColor);
			gc.fillOval(this.x-0.5*this.dispSize*zoomFactor, this.y-0.5*this.dispSize*zoomFactor, this.dispSize*zoomFactor, this.dispSize*zoomFactor);
			if(deviceColor!=transpColor) gc.strokeOval(this.x-0.5*this.dispSize*zoomFactor, this.y-0.5*this.dispSize*zoomFactor, this.dispSize*zoomFactor, this.dispSize*zoomFactor);
			gc.setFill(Color.BLACK);
			gc.strokeText(this.name, this.x, this.y+0.4*fontSize);
			double x1=0; double y1=0;
			double x2=0; double y2=0;			
		}
		
		public List<NodeSpec> getDsts(String _child){
			List<NodeSpec> parents = new ArrayList<NodeSpec>();
			for (ModuEdgeSpec m : moduleEdgesList) {
				if (m.child.equals(_child)) 
					parents.add((NodeSpec)getModule(m.parent));
					parents.add((NodeSpec)getActuator(m.parent));
			}
			return parents;
		}
		
		void setPos(MouseEvent mEvent) {
			this.x=mEvent.getX(); this.y=mEvent.getY();
		}
	}
	public ModuSpec getModule(String _name) {
    	if (_name==null) return null;
    	for (ModuSpec m : modulesList) if (m.name.equals(_name)) return m;
		return null;
    }
    public SensorSpec getSensor(String _name) {
    	if (_name==null) return null;
    	for (SensorSpec s : sensorsList) if (s.name.equals(_name)) return s;
		return null;
    }
    public DeviceSpec getDevice(String _name) {
    	if (_name==null) return null;
    	for (DeviceSpec d : devicesList) if (d.name.equals(_name)) return d;
		return null;
    }
    public ActuatorSpec getActuator(String _name) {
    	if (_name==null) return null;
    	for (ActuatorSpec a : actuatorsList) if (a.name.equals(_name)) return a;
		return null;
    }
    
		
    
	public class DeviceSpec extends NodeSpec {
		int pe;
		long mips;
		int ram;
		int level;
		double rate;
		double ipower;
		double apower;
		double latency;
		Color deviceColor=Color.RED;
		
		
		
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
			obj.put("latency", o.latency);
			obj.put("x_cord", o.x);
			obj.put("y_cord", o.y);
			obj.put("radius", o.dispSize);
			return obj;
		}
		
		public String toString() {
            String str = this.type + " ID: " + this.id+ "\nNode Name: " + this.name + "\nNode Parent Name:  " + this.parent + "\nUp Bandwidth: " 
        + this.upbw + "\nDown Bandwidth:  " + this.downbw + "\nMIPS: " + this.mips + "\nRAM: " + this.ram + "\nLevel: " + this.level + "\nRate: " + this.rate + "\nIdle Power: " + this.ipower + "\nActive Power: " + this.apower + "\n";
            String str1 = "--------------------------------------------------------------------------------";
            return str+str1;
        }
		
		public DeviceSpec(String name, String parent, long mips, int ram, long upbw, long downbw, int level, double rate, double apower,
				double ipower, double latency, double x, double y, double r) {
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
			this.latency = latency;
			this.type = "device";	
			this.x = x;
			this.y = y;
			this.dispSize = r;
		}
		
		public DeviceSpec(String name, Color deviceColor2, double x, double y, double d) {
			this.name = name;
			this.deviceColor = deviceColor2;
			this.x = x;
			this.y = y;
			this.dispSize = d;
		}

		public DeviceSpec(String name, String parent, long mips, int ram, long upbw, long downbw, int level,
				double rate, double apower, double ipower, double latency) {
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
			this.latency = latency;
			this.type = "device";
		}
		
	}
	
	public class ModuSpec extends NodeSpec {
		String nodeName;
		int modRam;
		long bandwidth;
		String inTuple;
		String outTuple;
		long size;
		int MIPS;
		double fractionalSensitivity;
		Color moduleColor=Color.CYAN;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			ModuSpec module = this;
			JSONObject obj = new JSONObject();
			obj.put("name", module.name);
			obj.put("ram", module.modRam);
			obj.put("bandwidth", module.bandwidth);
			obj.put("inTuple", module.inTuple);
			obj.put("outTuple", module.outTuple);
			obj.put("size", module.size);
			obj.put("mips", module.MIPS);
			obj.put("Fractional Sensitivity", module.fractionalSensitivity);
			obj.put("x_cord", module.x);
			obj.put("y_cord", module.y);
			obj.put("radius", module.size);
			obj.put("tupleMaps", "");
			return obj;
		}
		
		public String toString() {
			String str = this.type+" Node: " + this.nodeName + "\nModule: " + this.name + "\nRAM: " + this.modRam + "\nBW: " +this.bandwidth + "\nIn Tuple: " 
		+ this.inTuple + "OutTuple: " + this.outTuple + "Size: " + this.size + "MIPs: " + this.MIPS + "Sens: "+ this.fractionalSensitivity + "\n";
			String str1 = "--------------------------------------------------------------------------------";
			return str+str1;
		}
		
		public ModuSpec(String nodeName, String moduleName, int modRam, long bandwidth, String inTuple, String outTuple,
				long size, int MIPS, double fractionalSensitivity, double x, double y, double r) {
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
			this.x = x;
			this.y = y;
			this.dispSize = r;
		}

		public ModuSpec(String name, Color moduleColor2, double x, double y, double d) {
			this.name = name;
			this.moduleColor = moduleColor2;
			this.x = x;
			this.y = y;
			this.dispSize = d;
			
		}
		
		public ModuSpec(String nodeName, String moduleName, int modRam, long bandwidth, String inTuple,
				String outTuple, long size, int MIPS, double fractionalSensitivity) {
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
	
//	class dispLink{
//		NodeSpec src, dst;
//		void draw(GraphicsContext gc) {
//			double x1=0; double y1=0;
//			double x2=0; double y2=0;
//			if(src!=null) {x1=src.x; y1= src.y;}
//			if(dst!=null) {x2=dst.x; y2= dst.y;}
//			if(src!=null&&dst!=null) {
//				gc.beginPath();
//		    	gc.moveTo(x1, y1);
//				gc.lineTo(x2, y2);
//				gc.stroke();
//			}
//		}
//		dispLink(NodeSpec _src, NodeSpec _dst){this.src=_src; this.dst=_dst;}
//		dispLink(DeviceSpec _device) {
//			for (DeviceSpec dn : devicesList) if (dn.name.matches(_device.name)) this.src = dn;
//			for (DeviceSpec dn : devicesList) if (dn.name.matches(_device.parent)) this.dst = dn;
//		}
//		dispLink(DeviceSpec _src, DeviceSpec _dst) {
//	    	for(DeviceSpec dn : devicesList) if(dn.name==_src.name) {this.src=dn;}
//	    	for(DeviceSpec dn : devicesList) if(dn.name==_dst.name) {this.dst=dn;}
//		}
//		dispLink(ModuEdgeSpec _spec) {
//	    	for(ModuEdgeSpec dn : moduleEdgesList) if(dn.name.matches(_spec.child)) this.src=dn;
//	    	for(ModuEdgeSpec dn : moduleEdgesList) if(dn.name.matches(_spec.parent)) this.dst=dn;
//		}
//		public dispLink(ModuSpec _src, ModuSpec _dst) {
//			for(ModuSpec dn : modulesList) if(dn.name==_src.name) {this.src=dn;}
//	    	for(ModuSpec dn : modulesList) if(dn.name==_dst.name) {this.dst=dn;}
//		}
//	}
	
	public class SensorSpec extends NodeSpec {
		double latency;
		double deterministicValue;
		double normalMean;
		double normalStdDev;
		double uniformMax;
		double uniformMin;
		Color sensorColor=Color.PINK;
			
		@SuppressWarnings("unchecked")	
		JSONObject toJSON() {	
			SensorSpec sensor = this;	
			JSONObject obj = new JSONObject();
			obj.put("sensorName", sensor.name);
			obj.put("parentName", sensor.parent);
			obj.put("latency", sensor.latency);
			obj.put("deterministicValue", sensor.deterministicValue);	
			obj.put("normalMean", sensor.normalMean);	
			obj.put("normalStdDev", sensor.normalStdDev);	
			obj.put("uniformMax", sensor.uniformMax);	
			obj.put("uniformMin", sensor.uniformMin);
			obj.put("x_cord", sensor.x);
			obj.put("y_cord", sensor.y);
			obj.put("radius", sensor.dispSize);
			return obj;	
		}
		
		public SensorSpec(String sensorName, String sensorParent, double latency, double deterministicValue, double normalMean, double normalStdDev, double uniformMax,	
				double uniformMin, double x, double y, double d) {	
			this.name = sensorName;
			this.parent = sensorParent;
			this.latency = latency;
			this.deterministicValue = deterministicValue;
			this.normalMean = normalMean;
			this.normalStdDev = normalStdDev;
			this.uniformMax = uniformMax;
			this.uniformMin = uniformMin;
			this.id = getID();
			this.type = "sensor";
			this.x = x;
			this.y = y;
			this.dispSize = d;
		}

		public SensorSpec(String name, Color sensorColor2, double x, double y, double d) {
			this.name = name;
			this.sensorColor = sensorColor2;
			this.x = x;
			this.y = y;
			this.dispSize = d;
		}	
		
		public SensorSpec(String sensorName, String sensorParent, double latency, double deterministicValue,
				double normalMean, double normalStdDev, double uniformMax, double uniformMin) {
			this.name = sensorName;
			this.parent = sensorParent;
			this.latency = latency;
			this.deterministicValue = deterministicValue;
			this.normalMean = normalMean;
			this.normalStdDev = normalStdDev;
			this.uniformMax = uniformMax;
			this.uniformMin = uniformMin;
			this.id = getID();
			this.type = "sensor";
		}
	}

	public class ActuatorSpec extends NodeSpec {
		Color actuatorColor=Color.ORANGE;
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			JSONObject obj = new JSONObject();	
			obj.put("name", this.name);
			return obj;	
		}
		
		public ActuatorSpec(String actuatorName, double x, double y, double d) {
			this.name = actuatorName;
			this.type = "actuator";
			this.x = x;
			this.y = y;
			this.dispSize = d;
		}

		public ActuatorSpec(String name, Color actuatorColor2, double x, double y, double d) {
			this.name = name;
			this.actuatorColor = actuatorColor2;
			this.x = x;
			this.y = y;
			this.dispSize = d;
		}
		public ActuatorSpec(String actuatorName) {
			this.name = actuatorName;
		}

	}
	
	public class LinkSpec extends NodeSpec{
		String srcID;
		String dstID;
		double latency;
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			LinkSpec link = this;
			JSONObject obj = new JSONObject();
			obj.put("srcID", link.srcID);
			obj.put("dstID", link.dstID);
			obj.put("latency", link.latency);
			return obj;
		}
		
		public LinkSpec(String srcID, String dstID, double latency) {
			this.srcID = srcID;
			this.dstID = dstID;
			this.latency = latency;
			this.id = getID();
		}
	}

	public class ModuEdgeSpec extends NodeSpec {
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
			List<DeviceSpec> devicesList, List<ModuSpec> modulesList, List<ModuEdgeSpec> modEdgesList, List<SensorSpec> sensorsList, List<LinkSpec> linksList,
			int granularity, int time, String policy, String centralNode) {
		JSONObject obj = new JSONObject();
		JSONArray nodeList = new JSONArray();
		JSONArray edgeList = new JSONArray();
		JSONArray moduleList = new JSONArray();
		JSONArray sensorList = new JSONArray();
		JSONArray linkList = new JSONArray();
		
		
		for (DeviceSpec h : devicesList) nodeList.add(h.toJSON());
		for (ModuSpec m : modulesList) moduleList.add(m.toJSON());
		for (ModuEdgeSpec e : modEdgesList) edgeList.add(e.toJSON());
		for (SensorSpec s : sensorsList) sensorList.add(s.toJSON());

		JSONObject metaList = new JSONObject();
		metaList.put("policy", policy);
		metaList.put("central", centralNode);
		metaList.put("granularity", granularity);
		metaList.put("time", time);
		
		obj.put("meta", metaList);
		obj.put("nodes", nodeList);
		obj.put("modules", moduleList);
		obj.put("sensors", sensorList);
		obj.put("edges", edgeList);
        
		try {
			FileWriter file = new FileWriter(jsonFileName, false);
			file.write(obj.toJSONString().replaceAll(",", ",\n"));
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}