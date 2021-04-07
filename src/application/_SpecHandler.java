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
import java.util.stream.Collectors;

import org.fog.entities.FogDevice;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import application._SpecHandler.DeviceSpec;
//import application.SetupJSONParser.dispNode;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

public class _SpecHandler {
	public static ArrayList<DeviceSpec> devicesList = new ArrayList<DeviceSpec>();
	public static ArrayList<ModuleSpec> modulesList = new ArrayList<ModuleSpec>();
	public static ArrayList<ActuatSpec> actuatsList = new ArrayList<ActuatSpec>();
	public static ArrayList<SensorSpec> sensorsList = new ArrayList<SensorSpec>();
	public static ArrayList<NodeSpec> nodesList = new ArrayList<NodeSpec>();
//	public static ArrayList<EdgeSpec> edgesList = new ArrayList<EdgeSpec>();
	
	static double R=50;
	static double zoomFactor=1;
	static int fontSize = 16;
	static String font = "monospaced";
	
	static GraphicsContext gc=null;
	
	static Color deviceColor=Color.RED;
	static Color moduleColor=Color.CYAN;
	static Color sensorColor=Color.PINK;
	static Color actuatorColor=Color.ORANGE;
	static Color transpColor=Color.TRANSPARENT;
	static List<Color> validColors=Arrays.asList(deviceColor, moduleColor, sensorColor, actuatorColor, transpColor);
	static Color _errorColor=Color.GREEN;
	
	public static void shiftPositionsByZoom(ScrollEvent event) {
    	double minZoom=0.25; double maxZoom=1.5; double zoomStep=0.05;
    	double preZoom=zoomFactor;
    	zoomFactor+=(event.getDeltaY()>0)?zoomStep:-zoomStep;
    	if(zoomFactor<minZoom) zoomFactor=minZoom;
    	if(zoomFactor>maxZoom) zoomFactor=maxZoom;
    	double zoomRatio = zoomFactor/preZoom;
    	nodesList.forEach((d)->{d.x-=(d.x-event.getX())*2*(1-zoomRatio); d.y-=(d.y-event.getY())*2*(1-zoomRatio);});
    }
	
	public static NodeSpec getNode(MouseEvent mEvent) {
		NodeSpec selNode = null;
		for(NodeSpec n : nodesList) if(Math.pow(Math.pow(n.x-mEvent.getX(),2)+Math.pow(n.y-mEvent.getY(),2),0.5)<=0.5*n.sz*zoomFactor) selNode=n;
		if (selNode==null) return null;
		return selNode.setSelected();
	}
	
	public static void pruneLinks() {
		for(NodeSpec n: nodesList) n.edgesList=(ArrayList<EdgeSpec>) n.edgesList.stream().filter(e->e.dst!=null).collect(Collectors.toList());
		for(NodeSpec n: nodesList) n.edgesList=(ArrayList<EdgeSpec>) n.edgesList.stream().filter(e->!e.dst.isTemp).collect(Collectors.toList());
	}
	public static NodeSpec getSelected() {
		for(NodeSpec n: nodesList) if (n.selected) return n;
		return null;
	}
	public static NodeSpec getSelected(String _type) {
		for(NodeSpec n: nodesList) if (n.selected && n.type.equals(_type)) return n;
		return null;
	}
	public static NodeSpec getLinkableNode(ArrayList<String> types, String _name) {
		for(NodeSpec n: nodesList) if (n.name.equals(_name)&&types.contains(n.type)) return n;
		return null;
	}
	
	public static class NodeSpec {
		double x;
		double y;
		double sz;
		Color nodeColor;
		String name;
		String type;
		boolean selected;
		boolean isTemp=false;
		ArrayList<EdgeSpec> edgesList = null;
		ArrayList<NodeSpec> assocList = nodesList;
		
		public NodeSpec(double x, double y, String name, String type) {
			this.x = x;
			this.y = y;
			this.sz = R+R;
			this.name = name;
			this.type = type;
			this.selected = false;
			this.edgesList = new ArrayList<EdgeSpec>();
			this.setSelected();
			this.setColor();
			this.add();
		}
		public NodeSpec(String name, String type) {this(0, 0, name, type);}
		public NodeSpec(String type, MouseEvent mEvent) {this(mEvent.getX(), mEvent.getY(), "New "+type, type); this.isTemp=true;}
		
		@Override
		public String toString() {
			return "x=" + x + ", y=" + y + ", sz=" + sz + ", nodeColor=" + nodeColor + ", name=" + name + ", type="
					+ type + ", selected=" + selected;
		}
		public String toStringLinks() {
			String s = "";
			if (this.edgesList.size()==0) return s;
			s+= "\n{";
			for(int i=1; i<=this.edgesList.size(); i++) {
				s+=("\n "+i+" - "+this.edgesList.get(i-1).toString());
			}
			s+="\n}";
			return s;
		}
		
		NodeSpec setColor() {
			if (this.type==null) this.nodeColor=_errorColor;
			else if(this.type.equals("device")) this.nodeColor=deviceColor;
			else if(this.type.equals("module")) this.nodeColor=moduleColor;
			else if(this.type.equals("sensor")) this.nodeColor=sensorColor;
			else if(this.type.equals("actuat")) this.nodeColor=actuatorColor;
			else if(this.type.equals("linker")) this.nodeColor=transpColor;
			else this.nodeColor=_errorColor;
			return this;
		}
		
		void drawLink() {
			this.edgesList.forEach(e->e.draw(gc));
		}
		
		void drawNode() {
			if (validColors.contains(this.nodeColor)) gc.setFill(this.nodeColor);
			else gc.setFill(_errorColor);
			gc.fillOval(this.x-0.5*this.sz*zoomFactor, this.y-0.5*this.sz*zoomFactor, this.sz*zoomFactor, this.sz*zoomFactor);
			if(nodeColor!=transpColor) {
				gc.setStroke(this.selected?Color.BLUE:Color.BLACK);
				gc.setLineWidth(this.selected?10.0:1.0);
				gc.strokeOval(this.x-0.5*this.sz*zoomFactor, this.y-0.5*this.sz*zoomFactor, this.sz*zoomFactor, this.sz*zoomFactor);
				gc.setStroke(Color.BLACK); gc.setLineWidth(1.0);
				gc.strokeText(this.name, this.x, this.y+0.4*fontSize);
			}
		}
		
		NodeSpec setPos(MouseEvent mEvent) {
			this.x=mEvent.getX(); this.y=mEvent.getY();
			return this;
		}
		NodeSpec setPos(NodeSpec _node) {
			this.x=_node.x; this.y=_node.y;
			return this;
		}
		NodeSpec pop() {
			nodesList.remove(this);
			assocList.remove(this);
			this.isTemp=true;
			pruneLinks();
			return this;
		}
		NodeSpec add() {
			if(!(nodesList.stream().anyMatch(a->a.name.equals(this.name)))) nodesList.add(this);
			if(!(assocList.stream().anyMatch(a->a.name.equals(this.name)))) assocList.add(this);
			return this;
		}
		NodeSpec setSelected() {
			NodeSpec possiblePrev = getSelected();
			if (possiblePrev!=null) possiblePrev.selected=false;
			this.selected = true;
			System.out.println(this.toString()+this.toStringLinks());
			return this;
		}
		
		ArrayList<String> linkableDestinations() {
            ArrayList<String> linkables = new ArrayList<String>();
            if (this.type.equals("device")) linkables.add("device");
            else if (this.type.equals("module")){
                linkables.add("module");
                linkables.add("actuat");
                linkables.add("device");
            }
            else if (this.type.equals("sensor")) {
                linkables.add("module");
                linkables.add("device");
            }
            return linkables;
        }
		
        NodeSpec addLink(NodeSpec dst) {
        	this.edgesList.add(new EdgeSpec(this, dst, 0));
            return this;
        }
		
        NodeSpec addLink(String _dst, double UpLinkLatency) {
            NodeSpec dst = _SpecHandler.getLinkableNode(linkableDestinations(), _dst);
            if (dst==null) return this;
            this.edgesList.add(new EdgeSpec(this, dst, UpLinkLatency));
            return this;
        }
	}
	
	public static class DeviceSpec extends NodeSpec {
		@SuppressWarnings("unchecked")
		public DeviceSpec(String name, String parent,
				int pe, long mips, int ram, int level, double rate, double ipower, double apower, double latency,
				long upbw, long downbw) {
			super(name, "device");
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
		    this.assocList =  (ArrayList<NodeSpec>) ((ArrayList<?>) devicesList);
		    this.add();
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
			return "pe=" + pe + ", mips=" + mips + ", ram=" + ram + ", level=" + level + ", rate=" + rate + ", ipower="
					+ ipower + ", apower=" + apower + ", latency=" + latency + ", upbw=" + upbw + ", downbw=" + downbw
					+ ", x=" + x + ", y=" + y + ", sz=" + sz + ", nodeColor=" + nodeColor + ", name=" + name + ", type="
					+ type + ", selected=" + selected;
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
	
	public static class ModuleSpec extends NodeSpec {
		@Override
		public String toString() {
			return "ram=" + ram + ", bandwidth=" + bandwidth + ", size=" + size + ", mips="
					+ mips + ", x=" + x + ", y=" + y + ", name=" + name;
		}
		int ram;
		long bandwidth;
		long size;
		int mips;
		ArrayList<TupleSpec> tupleMappings;
		
		public ModuleSpec(String name, String nodeName, int ram, long bandwidth,
				long size, int mips, ArrayList<TupleSpec> _tupleMappings) {
			super(name, "module");
			this.ram = ram;
			this.bandwidth = bandwidth;
			this.size = size;
			this.tupleMappings = new ArrayList<TupleSpec>(_tupleMappings);
			this.add();
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
			
			JSONArray tupleMapsList = new JSONArray();
			tupleMappings.forEach(m->tupleMapsList.add(m.toJSON()));
			obj.put("TupleMaps",tupleMapsList);
			return obj;
		}
	}
	
	public static class SensorSpec extends NodeSpec {
		double latency;
		double deterministicValue;
		double normalMean;
		double normalStdDev;
		double uniformMax;
		double uniformMin;
		String distType;
		
		public SensorSpec(String name, String parent, double latency,
				double deterministicValue, double normalMean, double normalStdDev, double uniformMax,
				double uniformMin, String distType) {
			super(name, "sensor");
			this.latency = latency;
			this.deterministicValue = deterministicValue;
			this.normalMean = normalMean;
			this.normalStdDev = normalStdDev;
			this.uniformMax = uniformMax;
			this.uniformMin = uniformMin;
			this.distType = distType;
		}
		
		@Override
		public String toString() {
			return "latency=" + latency + ", deterministicValue=" + deterministicValue + ", normalMean=" + normalMean
					+ ", normalStdDev=" + normalStdDev + ", uniformMax=" + uniformMax + ", uniformMin=" + uniformMin
					+ ", distType=" + distType;
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
	
	public static class ActuatSpec extends NodeSpec {
		public double UpLinklatency;
		
        public ActuatSpec(String name, String type, double UpLinklatency) {
            super(name, "actuat");
            this.UpLinklatency = UpLinklatency;
        }
		
		@Override
		public String toString() {
			return "x=" + x + ",y=" + y + ",sz=" + sz + ",nodeColor=" + nodeColor + ",name=" + name + ",type=" + type;
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

	public static class EdgeSpec {
		NodeSpec src;
		NodeSpec dst;
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
			return "src=" + src.name + ",dst=" + dst.name + ",edgeType=" + edgeType + ",latency=" + latency + ",tupleType="
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
		
		public EdgeSpec(NodeSpec src, NodeSpec dst, String edgeType, double latency, String tupleType,
				double periodicity, double cpuLength, double newLength, int direction) {
			this.src = src;
			this.dst = dst;
			this.edgeType = edgeType;
			this.latency = latency;
			this.tupleType = tupleType;
			this.periodicity = periodicity;
			this.cpuLength = cpuLength;
			this.nwLength = newLength;
			this.direction = direction;
		}
		public EdgeSpec(NodeSpec src, NodeSpec dst, double latency) {this(src, dst, "0", latency, "null", 0, 0, 0, 0);}
		
		void draw(GraphicsContext gc) {
			gc.beginPath();
		    gc.moveTo(this.src.x, this.src.y);
			gc.lineTo(this.dst.x, this.dst.y);
			gc.stroke();
		}
	}
	
	public ArrayList<TupleSpec> tempTupleList = new ArrayList<TupleSpec>();
	public static class TupleSpec{
		SimpleStringProperty  inTuple;
		SimpleStringProperty  outTuple;
		double fractionalSensitivity;
		
		@Override
		public String toString() {
			return "inTuple=" + inTuple.get() + ",outTuple=" + outTuple.get() + ",fractionalSensitivity=" + fractionalSensitivity;
		}
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {	
			JSONObject obj = new JSONObject();
			String tupleString = this.toString();
			String[] tupleSplit = tupleString.split(",");
			for(int i = 0; i<tupleSplit.length;i++) {
				String[] tupleSplit2 = tupleSplit[i].split("=");
				obj.put(tupleSplit2[0], tupleSplit2[1]);
			}
			return obj;	
		}
		public String getInTuple() {
			return inTuple.get();
		}
		public void setInTuple(String inTuple) {
			this.inTuple.set(inTuple); 
		}
		public String getOutTuple() {
			return outTuple.get();
		}
		public void setOutTuple(String outTuple) {
			this.outTuple.set(outTuple); 
		}
		public double getSensitivity() {
			return fractionalSensitivity;
		}
		public void setSensitivity(double sensitivity) {
			this.fractionalSensitivity = sensitivity;
		}
		
		public TupleSpec(String  inTuple, String  outTuple, double fractionalSensitivity) {
			this.inTuple = new SimpleStringProperty(inTuple);
			this.outTuple = new SimpleStringProperty(outTuple);
			this.fractionalSensitivity = fractionalSensitivity;
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
//		for (EdgeSpec e : edgesList) edgesJSONObj.add(e.toJSON());

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