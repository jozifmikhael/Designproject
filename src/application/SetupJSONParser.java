package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//import application.SetupJSONParser.dispNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

public class SetupJSONParser {
	public static List<DeviceSpec> devicesList = new ArrayList<DeviceSpec>();
	public static List<ModuleSpec> modulesList = new ArrayList<ModuleSpec>();
	public static List<ActuatSpec> actuatsList = new ArrayList<ActuatSpec>();
	public static List<SensorSpec> sensorsList = new ArrayList<SensorSpec>();
	public static List<EdgeSpec> moduleEdgesList = new ArrayList<EdgeSpec>();
	public static List<LinkSpec> 	 linksList		 = new ArrayList<LinkSpec>();
	

	double R=50;
	static double zoomFactor=1;
	static int fontSize = 16;
	static String font = "monospaced";
//	
//	public ActuatSpec createActuator(String actuatorName, double x, double y) {			
//		ActuatSpec a = new ActuatSpec(actuatorName, x, y);
//		return a;
//	}
//	
//	public DeviceSpec createDevice(String name, String parent, long mips, int ram, long upbw, long downbw, int level, double rate, 
//			double apower, double ipower, double latency, double x, double y) {
//		DeviceSpec h = new DeviceSpec(name, parent, mips, ram, upbw, downbw, level, rate, apower, ipower, latency, x, y);
//		return h;
//	}
//	
//	public ModuleSpec createModule(String nodeName, String moduleName, int modRam, long bandwidth, String inTuple, String outTuple, long size, int MIPS, double fractionalSensitivity, double x, double y) {
//		ModuleSpec m = new ModuleSpec(nodeName, moduleName, modRam, bandwidth, inTuple, outTuple, size, MIPS, fractionalSensitivity, x, y);
//		return m;
//	}
//	
//	
//	public SensorSpec createSensor(String sensorName, String sensorParent, double latency, double deterministicValue, 
//			double normalMean, double normalStdDev, double uniformMax, double uniformMin, double x, double y)  {
//		SensorSpec s = new SensorSpec(sensorName, sensorParent, latency, deterministicValue, normalMean, normalStdDev, uniformMax, uniformMin, x, y);
//		return s;
//	}
//	
//	public LinkSpec createLink(String srcID, String dstID, double latency) {
//		LinkSpec l = new LinkSpec(srcID, dstID, latency);		
//		return l;
//	}
//	
//	public EdgeSpec createModuleEdge(String parent, String child, String tupleType, double periodicity, double cpuLength, double newLength, String edgeType, int direction) {
//		EdgeSpec e = new EdgeSpec(child, parent, edgeType, 5.0, tupleType, periodicity, cpuLength, newLength, direction);
//		return e;
//	}
//	
	public int canLink(NodeSpec _nodeSrc, NodeSpec _nodeDst) {
		if (_nodeSrc.type.equals("device")&&_nodeDst.type.equals("device")) return 1;
		else if (_nodeSrc.type.equals("module")&&_nodeDst.type.equals("module")) return 2;
		else if (_nodeSrc.type.equals("sensor")&&_nodeDst.type.equals("module")) return 3;
		else if (_nodeSrc.type.equals("module")&&_nodeDst.type.equals("actuator")) return 4;
		else return 0;
	}
	
	Color deviceColor=Color.RED;
	Color moduleColor=Color.CYAN;
	Color sensorColor=Color.PINK;
	Color actuatorColor=Color.ORANGE;
	Color transpColor=Color.TRANSPARENT;
	List<Color> validColors=Arrays.asList(deviceColor, moduleColor, sensorColor, actuatorColor, transpColor);
	Color _errorColor=Color.GREEN;
	
	public void shiftPositionsByZoom(ScrollEvent event) {
    	double minZoom=0.25; double maxZoom=1.5; double zoomStep=0.05;
//    	System.out.println(event.getDeltaY() +" "+ event.getX() +" "+ event.getY());
    	double preZoom=zoomFactor;
    	zoomFactor+=(event.getDeltaY()>0)?zoomStep:-zoomStep;
    	if(zoomFactor<minZoom) zoomFactor=minZoom;
    	if(zoomFactor>maxZoom) zoomFactor=maxZoom;
    	double zoomRatio = zoomFactor/preZoom;
//    	System.out.println(zoomFactor + " " + zoomRatio);
    	actuatsList.forEach((d)->{d.x-=(d.x-event.getX())*2*(1-zoomRatio); d.y-=(d.y-event.getY())*2*(1-zoomRatio);});
    	sensorsList.forEach(  (d)->{d.x-=(d.x-event.getX())*2*(1-zoomRatio); d.y-=(d.y-event.getY())*2*(1-zoomRatio);});
    	devicesList.forEach(  (d)->{d.x-=(d.x-event.getX())*2*(1-zoomRatio); d.y-=(d.y-event.getY())*2*(1-zoomRatio);});
    	modulesList.forEach(  (d)->{d.x-=(d.x-event.getX())*2*(1-zoomRatio); d.y-=(d.y-event.getY())*2*(1-zoomRatio);});
    }

	public static List<NodeSpec> nodesList = new ArrayList<NodeSpec>();
	//TODO Ask why there were like 3 different constructors in each extension
	public class NodeSpec {
		public NodeSpec(double x, double y, String name, String parent, String type) {
			this.x = x;
			this.y = y;
			this.sz = R+R;
			this.name = name;
			this.parent = parent;
			this.type = type;
			setColor();
			nodesList.add(this);
		}
		
		public NodeSpec(String type, MouseEvent mEvent) {
			// TODO Auto-generated constructor stub
			this.x = mEvent.getX();
			this.y = mEvent.getY();
			this.sz = R+R;
			this.type = type;
			this.name = "New "+this.type;
			this.parent = this.name;
			setColor();
		}
		
		private void setColor() {
			if (this.type==null) this.nodeColor=_errorColor;
			else if(this.type.equals("device")) this.nodeColor=deviceColor;
			else if(this.type.equals("module")) this.nodeColor=moduleColor;
			else if(this.type.equals("sensor")) this.nodeColor=sensorColor;
			else if(this.type.equals("actuator")) this.nodeColor=actuatorColor;
			else if(this.type.equals("link")) this.nodeColor=transpColor;
			else this.nodeColor=_errorColor;
		}

		double x;
		double y;
		double sz;
		Color nodeColor;
		String name;
		String parent;
		String type;
		
		void drawLink(GraphicsContext gc) {
			List<NodeSpec> dsts = getDsts(this.name);
			if (this.type.equals("device")) dsts.add(getDevice(this.parent));
			if (this.type.equals("module")) dsts.addAll(getDsts(this.name));
			for (NodeSpec n : dsts) {
				gc.beginPath();
			    gc.moveTo(this.x,this.y);
				gc.lineTo(n.x, n.y);
				gc.stroke();
			}
		}
		
		public List<NodeSpec> getDsts(String srcName){
			//TODO I dislike this, janky af, should've went with some sort of Java-based list comprehension
			//having to make a new list and add shit to it for
			//every. single. module. by going through
			//every. single. module-edge. for
			//every. single. frame.
			//And yes, I know this was my idea to begin with - Suren
			List<NodeSpec> parents = new ArrayList<NodeSpec>();
			for (EdgeSpec m : moduleEdgesList) {
				if (m.src.equals(srcName)) {
					if (getModule(m.dst)!=null) parents.add((NodeSpec)getModule(m.dst));
					if (getActuator(m.dst)!=null) parents.add((NodeSpec)getActuator(m.dst));
				}
			}
			return parents;
		}
		
		void drawNode(GraphicsContext gc) {
			if (validColors.contains(this.nodeColor)) gc.setFill(this.nodeColor);
			else gc.setFill(_errorColor);
			gc.fillOval(this.x-0.5*this.sz*zoomFactor, this.y-0.5*this.sz*zoomFactor, this.sz*zoomFactor, this.sz*zoomFactor);
			if(nodeColor!=transpColor) gc.strokeOval(this.x-0.5*this.sz*zoomFactor, this.y-0.5*this.sz*zoomFactor, this.sz*zoomFactor, this.sz*zoomFactor);
			gc.setFill(Color.BLACK);
			gc.strokeText(this.name, this.x, this.y+0.4*fontSize);
		}
		
		void setPos(MouseEvent mEvent) {
			this.x=mEvent.getX(); this.y=mEvent.getY();
		}
	}
	
	public NodeSpec getNode(MouseEvent mEvent) {
		for(NodeSpec n : nodesList) if(Math.pow(Math.pow(n.x-mEvent.getX(),2)+Math.pow(n.y-mEvent.getY(),2),0.5)<=0.5*n.sz*zoomFactor) return n;
		return null;
	}
	
	public NodeSpec getNode(String _name) {
		if (_name==null) return null;
    	for (NodeSpec n : nodesList) if (n.name.equals(_name)) return n;
		return null;
	}
	
	public ModuleSpec getModule(String _name) {
    	if (_name==null) return null;
    	for (ModuleSpec m : modulesList) if (m.name.equals(_name)) return m;
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
    public ActuatSpec getActuator(String _name) {
    	if (_name==null) return null;
    	for (ActuatSpec a : actuatsList) if (a.name.equals(_name)) return a;
		return null;
    }
    
	
	public static void main(String[] args) {
		SetupJSONParser testobj = new SetupJSONParser();
		JSONObject obj = new JSONObject();
		DeviceSpec device = testobj.new DeviceSpec(1.0, 1.0, "device1", "device2",  "device", 1, 1, 1, 1, 1.0, 1.0, 1.0, 1.0, 1, 1);
		System.out.println(device.toJSON().toString());
	}
	
    
    ////7 Base Fields + 8 Fields = 17
	public class DeviceSpec extends NodeSpec {

		public DeviceSpec(double x, double y, String name, String parent, String type,
				int pe, long mips, int ram, int level, double rate, double ipower, double apower, double latency,
				long upbw, long downbw) {
			super(x, y, name, parent, type);
			this.pe = pe;
			this.mips = mips;
			this.ram = ram;
			this.level = level;
			this.rate = rate;
			this.ipower = ipower;
			this.apower = apower;
			this.latency = latency;
			this.upbw = upbw;
			this.downbw = downbw;
		}
		
		int pe;
		long mips;
		int ram;
		int level;
		double rate;
		double ipower;
		double apower;
		double latency;
		long upbw;
		long downbw;
		
		@Override
		public String toString() {
			return "pe=" + pe + ",mips=" + mips + ",ram=" + ram + ",level=" + level + ",rate=" + rate + ",ipower="
					+ ipower + ",apower=" + apower + ",latency=" + latency + ",upbw=" + upbw + ",downbw=" + downbw
					+ ",x=" + x + ",y=" + y + ",sz=" + sz
					+ ",name=" + name + ",parent=" + parent + ",type=" + type;
		}

		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			JSONObject obj = new JSONObject();
			String deviceString = this.toString();
			String[] deviceSplit = deviceString.split(",");
			
			for(int i = 0; i<deviceSplit.length;i++) {
				String[] deviceSplit2 = deviceSplit[i].split("=");
				obj.put(deviceSplit2[0], deviceSplit2[1]);
			}
			
			return obj;
		}
		
//		public DeviceSpec(String name, String parent, long mips, int ram, long upbw, long downbw, int level, double rate,
//				double apower, double ipower, double latency, double x, double y, double r, int pe) {
//			this.x = x;
//			this.y = y;
//			this.sz = r;
//			this.nodeColor = deviceColor;
//			this.name = name;
//			this.parent = parent;
//			this.type = "device";
//			this.upbw = upbw;
//			this.downbw = downbw;
//			
//			this.pe = pe;
//			this.mips = mips;
//			this.ram = ram;
//			this.level = level;
//			this.rate = rate;
//			this.apower = apower;
//			this.ipower = ipower;
//			this.latency = latency;
//		}
	}
	
	public class ModuleSpec extends NodeSpec {

		String nodeName;
		int modRam;
		long bandwidth;
		String inTuple;
		String outTuple;
		long size;
		int MIPS;
		double fractionalSensitivity;
		Field[] moduleField = ModuleSpec.class.getFields();
		
		public ModuleSpec(double x, double y, String name, String parent, String type, String nodeName, int modRam,
				long bandwidth, String inTuple, String outTuple, long size, int mIPS, double fractionalSensitivity) {
			super(x, y, name, parent, type);
			this.nodeName = nodeName;
			this.modRam = modRam;
			this.bandwidth = bandwidth;
			this.inTuple = inTuple;
			this.outTuple = outTuple;
			this.size = size;
			MIPS = mIPS;
			this.fractionalSensitivity = fractionalSensitivity;
		}
		
		@Override
		public String toString() {
			return "nodeName=" + nodeName + ",modRam=" + modRam + ",bandwidth=" + bandwidth + ",inTuple=" + inTuple
					+ ",outTuple=" + outTuple + ",size=" + size + ",MIPS=" + MIPS + ",fractionalSensitivity="
					+ fractionalSensitivity + ",x=" + x + ",y=" + y + ",sz=" + sz + ",nodeColor=" + nodeColor + ",name="
					+ name + ",parent=" + parent + ",type=" + type;
		}

		ModuleSpec fromJSON(JSONObject obj) {
			String moduleString = Arrays.toString(moduleField);
			moduleString = moduleString.replace("[", "");
			moduleString = moduleString.replace("]", "");
			moduleString = moduleString.replace(this.getClass().toString().replace("class ", "")+".", "");
			moduleString = moduleString.replace("public ", "");
			moduleString = moduleString.replace("java.lang.", "");
			String[] tokens = moduleString.split(", ");
			//int level = Integer.parseUnsignedInt(device.get("level").toString());
			for (int i = 0; i <tokens.length;i++) {
				String[] tokens2 = tokens[i].split(" ");
				if (tokens2[0].equals("int"))
					System.out.println(tokens2[0] + " " + tokens2[1] + " = " + "Integer.parseUnsignedInt(device.get(" + tokens2[1] + ").toString());");	
				if (tokens2[0])
			}
			return null;
		}
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			JSONObject obj = new JSONObject();
			String moduleString = this.toString();
			String[] moduleSplit = moduleString.split(",");
			
			for(int i = 0; i<moduleSplit.length; i++) {
				String[] sensorSplit2 = moduleSplit[i].split("=");
				obj.put(sensorSplit2[0], sensorSplit2[1]);
			}

			return obj;
		}
	}
	
	public class SensorSpec extends NodeSpec {

		double latency;
		double deterministicValue;
		double normalMean;
		double normalStdDev;
		double uniformMax;
		double uniformMin;
		
		public SensorSpec(double x, double y, String name, String parent, String type, double latency,
				double deterministicValue, double normalMean, double normalStdDev, double uniformMax,
				double uniformMin) {
			super(x, y, name, parent, type);
			this.latency = latency;
			this.deterministicValue = deterministicValue;
			this.normalMean = normalMean;
			this.normalStdDev = normalStdDev;
			this.uniformMax = uniformMax;
			this.uniformMin = uniformMin;
		}

		
		@Override
		public String toString() {
			return "latency=" + latency + ",deterministicValue=" + deterministicValue + ",normalMean=" + normalMean
					+ ",normalStdDev=" + normalStdDev + ",uniformMax=" + uniformMax + ",uniformMin=" + uniformMin
					+ ",x=" + x + ",y=" + y + ",sz=" + sz + ",nodeColor=" + nodeColor + ",name=" + name + ",parent="
					+ parent + ",type=" + type;
		}
		
		@SuppressWarnings("unchecked")	
		JSONObject toJSON() {	
			JSONObject obj = new JSONObject();
			String sensorString = this.toString();
			String[] sensorSplit = sensorString.split(",");
			
			for(int i = 0; i<sensorSplit.length;i++) {
				String[] sensorSplit2 = sensorSplit[i].split("=");
				obj.put(sensorSplit2[0], sensorSplit2[1]);
			}

			return obj;	
		}
	}
	
	public class ActuatSpec extends NodeSpec {
		
		public ActuatSpec(double x, double y, String name, String parent, String type) {
			super(x, y, name, parent, type);
		}

		@Override
		public String toString() {
			return "x=" + x + ",y=" + y + ",sz=" + sz + ",nodeColor=" + nodeColor + ",name=" + name + ",parent="
					+ parent + ",type=" + type;
		}

		@SuppressWarnings("unchecked")
		JSONObject toJSON() {	
			JSONObject obj = new JSONObject();
			String actuatorString = this.toString();
			String[] actuatorSplit = actuatorString.split(",");
			
			for(int i = 0; i<actuatorSplit.length;i++) {
				String[] actuatorSplit2 = actuatorSplit[i].split("=");
				obj.put(actuatorSplit2[0], actuatorSplit2[1]);
			}

			return obj;	
		}
	}

	public class LinkSpec{
		String srcID;
		String dstID;
		double latency;
		
		@Override
		public String toString() {
			return "srcID=" + srcID + ",dstID=" + dstID + ",latency=" + latency;
		}

		@SuppressWarnings("unchecked")
		JSONObject toJSON() {	
			JSONObject obj = new JSONObject();
			String linkString = this.toString();
			String[] linkSplit = linkString.split(",");
			
			for(int i = 0; i<linkSplit.length;i++) {
				String[] linkSplit2 = linkSplit[i].split("=");
				obj.put(linkSplit2[0], linkSplit2[1]);
			}

			return obj;	
		}
	}

	public class EdgeSpec {
		String src;
		String dst;
		//                      int DEVICE = 0;
		//	public static final int SENSOR = 1; // App Edge originates from a sensor
		//	public static final int ACTUATOR = 2; // App Edge leads to an actuator
		//	public static final int MODULE = 3; // App Edge is between application modules
		String edgeType;
		double latency;
		String tupleType;
		double periodicity;
		double cpuLength;
		double nwLength;
//		public static final int UP = 1; //I THINK this is src->dst
//		public static final int DOWN = 2; //I THINK this is dst->src
//		public static final int ACTUATOR = 3; //I THINK src->actuator
		int direction = 1;
		
		@Override
		public String toString() {
			return "src=" + src + ",dst=" + dst + ",edgeType=" + edgeType + ",latency=" + latency + ",tupleType="
					+ tupleType + ",periodicity=" + periodicity + ",cpuLength=" + cpuLength + ",nwLength=" + nwLength
					+ ",direction=" + direction;
		}

		@SuppressWarnings("unchecked")
		JSONObject toJSON() {	
			JSONObject obj = new JSONObject();
			String edgeString = this.toString();
			String[] edgeSplit = edgeString.split(",");
			for(int i = 0; i<edgeSplit.length;i++) {
				String[] edgeSplit2 = edgeSplit[i].split("=");
				obj.put(edgeSplit2[0], edgeSplit2[1]);
			}

			return obj;	
		}
		
		public EdgeSpec(String src, String dst, String edgeType, double latency, String tupleType,
				double periodicity, double cpuLength, double newLength, int direction) {
			this.dst = dst;
			this.src = src;
			this.edgeType = edgeType;
			this.latency = latency;
			this.tupleType = tupleType;
			this.periodicity = periodicity;
			this.cpuLength = cpuLength;
			this.nwLength = newLength;
			this.direction = direction;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void writeJSON(String jsonFileName,
			int simGranularity, int simTotLength, String placementPolicy, String topLvNode) {
		JSONObject obj = new JSONObject();
		JSONArray devicesJSONObj = new JSONArray();
		JSONArray modulesJSONObj = new JSONArray();
		JSONArray sensorsJSONObj = new JSONArray();
		JSONArray actuatsJSONObj = new JSONArray();
		JSONArray edgesJSONObj = new JSONArray();
		
		for (DeviceSpec h : devicesList) devicesJSONObj.add(h.toJSON());
		for (ModuleSpec m : modulesList) modulesJSONObj.add(m.toJSON());
		for (SensorSpec s : sensorsList) sensorsJSONObj.add(s.toJSON());
		for (ActuatSpec a : actuatsList) actuatsJSONObj.add(a.toJSON());
		for (EdgeSpec e : moduleEdgesList) edgesJSONObj.add(e.toJSON());

		JSONObject metaList = new JSONObject();
		//TODO colors + zoomlv need to be in here as well
		metaList.put("simGranularity", simGranularity);
		metaList.put("simDuration", simTotLength);
		metaList.put("topLevelNode", topLvNode);
		metaList.put("placementPolicy", placementPolicy);
		
		obj.put("meta", metaList);
		obj.put("edges", edgesJSONObj);
		obj.put("devices", devicesJSONObj);
		obj.put("modules", modulesJSONObj);
		obj.put("sensors", sensorsJSONObj);
		obj.put("actuats", actuatsJSONObj);
        
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