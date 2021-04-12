package application;

import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacement;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.Pair;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.application.AppEdge;
import org.fog.application.AppModule;
import org.fog.application.Application;
import org.fog.application.selectivity.FractionalSelectivity;
import org.fog.application.selectivity.SelectivityModel;
import org.fog.entities.Actuator;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.entities.Sensor;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.scheduler.TupleScheduler;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.distribution.DeterministicDistribution;
import org.fog.utils.distribution.Distribution;
import org.fog.utils.distribution.NormalDistribution;
import org.fog.utils.distribution.UniformDistribution;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import application._SpecHandler.TupleSpec;
import static application.scratch.printDebug;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.application.AppModule;
import org.fog.application.Application;
import org.fog.application.selectivity.FractionalSelectivity;
import org.fog.application.selectivity.SelectivityModel;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.scheduler.TupleScheduler;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
	
	static double R = 50;
	static double zoomFactor = 1;
	static int fontSize = 16;
	static String font = "monospaced";
	
	static GraphicsContext gc = null;
	
	static Color deviceColor = Color.RED;
	static Color moduleColor = Color.CYAN;
	static Color sensorColor = Color.PINK;
	static Color actuatorColor = Color.ORANGE;
	static Color transpColor = Color.TRANSPARENT;
	static List<Color> validColors = Arrays.asList(deviceColor, moduleColor, sensorColor, actuatorColor, transpColor);
	static Color _errorColor = Color.GREEN;
	
	public static void shiftPositionsByZoom(ScrollEvent event) {
		double minZoom = 0.25;
		double maxZoom = 1.5;
		double zoomStep = 0.05;
		double scale = 1;
		double preZoom = zoomFactor;
		zoomFactor += (event.getDeltaY() > 0) ? zoomStep : -zoomStep;
		if (zoomFactor < minZoom)
			zoomFactor = minZoom;
		if (zoomFactor > maxZoom)
			zoomFactor = maxZoom;

		double zoomRatio = zoomFactor / preZoom;
		gc.translate(preZoom, zoomRatio);
//		nodesList.forEach((d) -> {
//			d.x -= (d.x - event.getX()) * 2 * (1 - zoomRatio);
//			d.y -= (d.y - event.getY()) * 2 * (1 - zoomRatio);
//		});
		
//		scale *= zoomFactor / preZoom;
//		gc.scale(scale, scale);
//		gc.m
//		for(NodeSpec d : nodesList) {
//			d.sz *= scale;
//			d.x -= (d.x - event.getX()) * (1 - zoomFactor / preZoom);
//			d.y -= (d.y - event.getY()) * (1 - zoomFactor / preZoom);
//		}
	}
	
	public static NodeSpec getNode(MouseEvent mEvent) {
		Spec selNode = null;
		for (NodeSpec n : nodesList)
			if (Math.pow(Math.pow(n.x - mEvent.getX(), 2) + Math.pow(n.y - mEvent.getY(), 2), 0.5) <= 0.5 * n.sz
					* zoomFactor)
				selNode = n;
		if (selNode == null)
			return null;
		return (NodeSpec) selNode.setSelected();
	}

	public static EdgeSpec getEdge(MouseEvent mEvent) {
		for(NodeSpec n : _SpecHandler.nodesList) {
			for(EdgeSpec e : n.edgesList) {
				if(e.dst == null || e.src == null) continue;
				if((mEvent.getX() <= e.dst.x && mEvent.getX() >= e.src.x && mEvent.getY() <= e.dst.y && mEvent.getY() >= e.src.y)
						||(mEvent.getX() <= e.src.x && mEvent.getX() >= e.dst.x && mEvent.getY() <= e.src.y && mEvent.getY() >= e.dst.y)
						||(mEvent.getX() <= e.dst.x && mEvent.getX() >= e.src.x && mEvent.getY() <= e.src.y && mEvent.getY() >= e.dst.y)
						||(mEvent.getX() <= e.src.x && mEvent.getX() >= e.dst.x && mEvent.getY() <= e.dst.y && mEvent.getY() >= e.src.y)) {
					double m = (e.src.y - e.dst.y)/(e.src.x - e.dst.x);
					if(e.src.x - e.dst.x == 0 && Math.abs(mEvent.getX() - e.dst.x) <= 10) return (EdgeSpec) e.setSelected();
					else if(e.src.y - e.dst.y == 0 && Math.abs(mEvent.getY() - e.dst.y) <= 10) return (EdgeSpec) e.setSelected();
					else {
						double bLink = (e.dst.y - (m*e.dst.x));
						double mTemp = -(1/m);
						double bTemp = mEvent.getY() - (mTemp * mEvent.getX());
						double xLine = (bTemp - bLink)/(m - mTemp);
						double yLine = m * xLine + bLink;
						double distance = Math.sqrt(Math.pow(xLine - mEvent.getX(), 2) + Math.pow(yLine - mEvent.getY(),2));
						if(distance <= 10) return (EdgeSpec) e.setSelected();
					}
				}
			}
		}
		return null;
	}
	
	public static Spec getNode(String nodeName) {
		for(NodeSpec n : nodesList) if(n.name.equals(nodeName)) return n;
		return null;
	}
	
	public static void pruneLinks() {
		for (NodeSpec n : nodesList)
			n.edgesList = (ArrayList<EdgeSpec>) n.edgesList.stream().filter(e -> e.dst != null)
					.collect(Collectors.toList());
		for (NodeSpec n : nodesList)
			n.edgesList = (ArrayList<EdgeSpec>) n.edgesList.stream().filter(e -> !e.dst.isTemp)
					.collect(Collectors.toList());
	}
	
	public static void deselectAll() {
		for(NodeSpec n : nodesList) {
			n.selected = false;
			for(EdgeSpec e: n.edgesList) e.selected = false;
		}
	}
	
	public static Spec getSelected() {
		for (NodeSpec n : nodesList) {
			if (n.selected) return n;
			for(EdgeSpec e : n.edgesList) {
				if(e.selected) return e;
			}
		}
		return null;
	}
	
	public static Spec getSelected(String _type) {
		for (Spec n : nodesList) {
			if (n.selected && n.type.equals(_type)) {
				return n;
			}
		}
		return null;
	}
	
	public static ArrayList<NodeSpec> getLinkableNodes(ArrayList<String> types, String _type) {
		return (ArrayList<NodeSpec>) nodesList.stream().filter(n->types.contains(_type)).collect(Collectors.toList());
	}
	
	public static NodeSpec getLinkableNode(ArrayList<String> types, String _name) {
		for (NodeSpec n : nodesList)
			if (n.name.equals(_name) && types.contains(n.type))
				return n;
		return null;
	}
	
	public static ArrayList<String> getLinkableNodes(ArrayList<String> types) {
		ArrayList<String> list = new ArrayList<String>();
		for(NodeSpec n : nodesList) if(types.contains(n.type))list.add(n.name);
        return list;
    }
	
	public static ArrayList<String> linkableDestinations(String type) {//TODO Made just for the edge listeners
		ArrayList<String> linkables = new ArrayList<String>();
		if (type.equals("src")) {
			linkables.add("sensor");
			linkables.add("module");
		} else if (type.equals("dst")) {
			linkables.add("module");
			linkables.add("actuat");
		}else if (type.equals("module")) linkables.add("module");
		return linkables;
	}
	
	public static abstract class Spec {
		boolean selected = false;
		public String type;
		
		Spec setSelected() {
			deselectAll();
			this.selected = true;
			return this;
		}
	}
	
	public static class NodeSpec extends Spec {
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		double x;
		double y;
		double sz;
		Color nodeColor;
		public String name;
		boolean isTemp = false;
		public ArrayList<EdgeSpec> edgesList = null;
		ArrayList<NodeSpec> assocList = nodesList;
		ArrayList<String> test;
		
		public NodeSpec(double x, double y, String name, String type) {
			this.x = x;
			this.y = y;
			this.sz = R + R;
			this.name = name;
			this.type = type;
			this.edgesList = new ArrayList<EdgeSpec>();
			this.setSelected();
			this.setColor();
			this.add();
		}
		
		public NodeSpec(String name, String type) {
			this(0, 0, name, type);
		}
		
		public NodeSpec(String type, MouseEvent mEvent) {
			this.x = mEvent.getX();
			this.y = mEvent.getY();
			this.sz = R + R;
			this.name = "New " + type;
			this.type = type;
			this.edgesList = new ArrayList<EdgeSpec>();
			this.isTemp = true;
			this.setColor();
			this.add();
		}
		
		@Override
		public String toString() {
			return "x=" + x + ", y=" + y + ", sz=" + sz + ", nodeColor=" + nodeColor + ", name=" + name + ", type="
					+ type + ", selected=" + selected;
		}
		
		public String toStringLinks() {
			String s = "";
			if (this.edgesList.size() == 0)
				return s;
			s += "\n{";
			for (int i = 1; i <= this.edgesList.size(); i++) {
				s += ("\n " + i + " - " + this.edgesList.get(i - 1).toString());
			}
			s += "\n}";
			return s;
		}
		
		Spec setColor() {
			if (this.type == null)
				this.nodeColor = _errorColor;
			else if (this.type.equals("device"))
				this.nodeColor = deviceColor;
			else if (this.type.equals("module"))
				this.nodeColor = moduleColor;
			else if (this.type.equals("sensor"))
				this.nodeColor = sensorColor;
			else if (this.type.equals("actuat"))
				this.nodeColor = actuatorColor;
			else if (this.type.equals("linker"))
				this.nodeColor = transpColor;
			else
				this.nodeColor = _errorColor;
			return this;
		}
		
		void drawLink() {
			this.edgesList.forEach(e -> e.draw(gc));
		}
		
		void drawNode() {
			if (validColors.contains(this.nodeColor))
				gc.setFill(this.nodeColor);
			else
				gc.setFill(_errorColor);
			gc.fillOval(this.x - 0.5 * this.sz * zoomFactor, this.y - 0.5 * this.sz * zoomFactor, this.sz * zoomFactor,
					this.sz * zoomFactor);
			if (nodeColor != transpColor) {
				gc.setStroke(this.selected ? Color.BLUE : Color.BLACK);
				gc.setLineWidth(this.selected ? 10.0 : 1.0);
				gc.strokeOval(this.x - 0.5 * this.sz * zoomFactor, this.y - 0.5 * this.sz * zoomFactor,
						this.sz * zoomFactor, this.sz * zoomFactor);
				gc.setStroke(Color.BLACK);
				gc.setLineWidth(1.0);
				gc.strokeText(this.name, this.x, this.y + 0.4 * fontSize);
			}
		}
		
		Spec setPos(MouseEvent mEvent) {
			this.x = mEvent.getX();
			this.y = mEvent.getY();
			return this;
		}
		
		Spec setPos(NodeSpec _node) {
			this.x = _node.x;
			this.y = _node.y;
			return this;
		}
		
		NodeSpec pop() {
			nodesList.remove(this);
			assocList.remove(this);
			this.isTemp = true;
			pruneLinks();
			return this;
		}
		
		Spec add() {
			if (!(nodesList.stream().anyMatch(a -> a.name.equals(this.name))))
				nodesList.add(this);
			if (!(assocList.stream().anyMatch(a -> a.name.equals(this.name))))
				assocList.add(this);
			return this;
		}
		
		ArrayList<String> linkableDestinations() {
			ArrayList<String> linkables = new ArrayList<String>();
			if (this.type.equals("device")) {
				linkables.add("device");
				linkables.add("actuat");
			}else if (this.type.equals("module")) {
				linkables.add("module");
				linkables.add("actuat");
				linkables.add("device");
			} else if (this.type.equals("sensor")) {
				linkables.add("module");
				linkables.add("device");
			}
			return linkables;
		}
		
		NodeSpec addLink(EdgeSpec e) {
			for(EdgeSpec edge : this.edgesList) if(e.dst.equals(edge.dst)) return this;
			this.edgesList.add(e);
			return this;
		}
		
		Spec deviceModuleLink(String _dst) {
			NodeSpec dst = _SpecHandler.getLinkableNode(linkableDestinations(), _dst);
			if (dst == null)return this;
			this.edgesList.add(new EdgeSpec(this, dst));
			return this;
		}
	}
	
	public static class DeviceSpec extends NodeSpec {

		@SuppressWarnings("unchecked")
		public DeviceSpec(String name, int pe, long mips, int ram, int level, double rate, double ipower, double apower,
				double latency, long upbw, long downbw) {
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
			this.assocList = (ArrayList<NodeSpec>) ((ArrayList<?>) devicesList);
			this.test = new ArrayList<String>();
			this.setSelected();
			this.add();
		}
		
		@Override
        public String toString() {
            return "selected="+ selected +",pe=" + pe + ",mips=" + mips + ",ram=" + ram + ",level=" + level + ",rate=" + rate
                    + ",ipower=" + ipower + ",apower=" + apower + ",upbw=" + upbw + ",downbw=" + downbw + ",x=" + x
                    + ",y=" + y + ",name=" + name;
        }
		static DeviceSpec fromJSON(JSONObject obj) {
            DeviceSpec d = new DeviceSpec((String) obj.get("name"), 
                    Integer.parseInt((String)obj.get("pe")),
                    Long.parseLong((String)obj.get("mips")),
                    Integer.parseInt((String)obj.get("ram")),
                    Integer.parseInt((String)obj.get("level")),
                    Double.parseDouble((String)obj.get("rate")),
                    Double.parseDouble((String)obj.get("ipower")),
                    Double.parseDouble((String)obj.get("apower")), 
                    0.0, 
                    Long.parseLong((String)obj.get("upbw")), 
                    Long.parseLong((String)obj.get("downbw")));
		            d.x = Double.parseDouble((String)obj.get("x"));
		            d.y = Double.parseDouble((String)obj.get("y"));
            return d;
        }
		
		public FogDevice addToApp() {
			List<Pe> peList = new ArrayList<Pe>();
			
			// 3. Create PEs and add these into a list.
			peList.add(new Pe(0, new PeProvisionerOverbooking(this.mips))); // need to store Pe id and MIPS Rating
			
			int hostId = FogUtils.generateEntityId();
			long storage = 1000000; // host storage
			int bw = 10000;
			PowerHost host = new PowerHost(hostId, new RamProvisionerSimple(this.ram), new BwProvisionerOverbooking(bw),
					storage, peList, new StreamOperatorScheduler(peList),
					new FogLinearPowerModel(this.apower, this.ipower));
			List<Host> hostList = new ArrayList<Host>();
			hostList.add(host);
			String arch = "x86"; // system architecture
			String os = "Linux"; // operating system
			String vmm = "Xen";
			double time_zone = 10.0; // time zone this resource located
			double cost = 3.0; // the cost of using processing in this resource
			double costPerMem = 0.05; // the cost of using memory in this resource
			double costPerStorage = 0.001; // the cost of using storage in this
											// resource
			double costPerBw = 0.0; // the cost of using bw in this resource
			LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
			// devices by now
			FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(arch, os, vmm, host, time_zone,
					cost, costPerMem, costPerStorage, costPerBw);
			FogDevice fogdevice = null;
			try {
				fogdevice = new FogDevice(this.name, characteristics, new AppModuleAllocationPolicy(hostList),
						storageList, 10, this.upbw, this.downbw, 0, this.rate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fogdevice.setLevel(this.level);
			return fogdevice;
		}

		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			JSONObject obj = new JSONObject();
			String deviceString = this.toString();
			String[] deviceSplit = deviceString.split(",");
			
			for (int i = 0; i < deviceSplit.length; i++) {
				String[] deviceSplit2 = deviceSplit[i].split("=");
				obj.put(deviceSplit2[0], deviceSplit2[1]);
			}
			
			return obj;
		}

		public int getPe() {
			return pe;
		}

		public void setPe(int pe) {
			this.pe = pe;
		}

		public long getMips() {
			return mips;
		}

		public void setMips(long mips) {
			this.mips = mips;
		}

		public int getRam() {
			return ram;
		}

		public void setRam(int ram) {
			this.ram = ram;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public double getRate() {
			return rate;
		}

		public void setRate(double rate) {
			this.rate = rate;
		}

		public double getIpower() {
			return ipower;
		}

		public void setIpower(double ipower) {
			this.ipower = ipower;
		}

		public double getApower() {
			return apower;
		}

		public void setApower(double apower) {
			this.apower = apower;
		}

		public double getLatency() {
			return latency;
		}

		public void setLatency(double latency) {
			this.latency = latency;
		}
		//TODO Only have the one box for upbw/downbw
		//just assign equiv in the background?
		public long getUpbw() {
			return upbw;
		}

		public void setUpbw(long upbw) {
			this.upbw = upbw;
		}

		public long getDownbw() {
			return downbw;
		}

		public void setDownbw(long downbw) {
			this.downbw = downbw;
		}

		public int pe;
		public long mips;
		public int ram;
		public int level;
		public double rate;
		public double ipower;
		public double apower;
		public double latency;
		public long upbw;
		public long downbw;
		
	}
	
	public static class ModuleSpec extends NodeSpec {	
		
		public int getRam() {
			return ram;
		}

		public void setRam(int ram) {
			this.ram = ram;
		}

		public long getBandwidth() {
			return bandwidth;
		}

		public void setBandwidth(long bandwidth) {
			this.bandwidth = bandwidth;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public int getMips() {
			return mips;
		}

		public void setMips(int mips) {
			this.mips = mips;
		}

		public ArrayList<TupleSpec> getTupleMappings() {
			return tupleMappings;
		}

		public void setTupleMappings(ArrayList<TupleSpec> tupleMappings) {
			this.tupleMappings = tupleMappings;
		}

		@Override
		public String toString() {
			return "ram=" + ram + ", bandwidth=" + bandwidth + ", size=" + size + ", mips=" + mips + ", x=" + x + ", y="
					+ y + ", name=" + name;
		}
		
		public int ram;
		public long bandwidth;
		public long size;
		public int mips;
		public ArrayList<TupleSpec> tupleMappings;
		
		public ModuleSpec(String name, String nodeName, int ram, long bandwidth, long size, int mips,
				ArrayList<TupleSpec> _tupleMappings) {
			super(name, "module");
			this.ram = ram;
			this.bandwidth = bandwidth;
			this.size = size;
			this.tupleMappings = new ArrayList<TupleSpec>(_tupleMappings);
			this.add();
		}
		
		public Application addToApp(Application application) {
			application.addAppModule(this.name, this.ram, this.mips, this.size, this.bandwidth);
			for (TupleSpec tupleMaps : this.tupleMappings)
				application.addTupleMapping(this.name, tupleMaps.getInTuple(), tupleMaps.getOutTuple(),
						new FractionalSelectivity(tupleMaps.getFractionalSensitivity()));
			return application;
		}
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			JSONObject obj = new JSONObject();
			String moduleString = this.toString();
			String[] moduleSplit = moduleString.split(",");
			
			for (int i = 0; i < moduleSplit.length; i++) {
				String[] sensorSplit2 = moduleSplit[i].split("=");
				obj.put(sensorSplit2[0], sensorSplit2[1]);
			}
			
			JSONArray tupleMapsList = new JSONArray();
			tupleMappings.forEach(m -> tupleMapsList.add(m.toJSON()));
			obj.put("TupleMaps", tupleMapsList);
			return obj;
		}
	}
	
	public static class SensorSpec extends NodeSpec {
		public double getLatency() {
			return latency;
		}
		
		public void setLatency(double latency) {
			this.latency = latency;
		}
		
		public double getDeterministicValue() {
			return deterministicValue;
		}
		
		public void setDeterministicValue(double deterministicValue) {
			this.deterministicValue = deterministicValue;
		}
		
		public double getNormalMean() {
			return normalMean;
		}
		
		public void setNormalMean(double normalMean) {
			this.normalMean = normalMean;
		}
		
		public double getNormalStdDev() {
			return normalStdDev;
		}
		
		public void setNormalStdDev(double normalStdDev) {
			this.normalStdDev = normalStdDev;
		}
		
		public double getUniformMax() {
			return uniformMax;
		}
		
		public void setUniformMax(double uniformMax) {
			this.uniformMax = uniformMax;
		}
		
		public double getUniformMin() {
			return uniformMin;
		}
		
		public void setUniformMin(double uniformMin) {
			this.uniformMin = uniformMin;
		}
		
		public String getDistType() {
			return distType;
		}
		
		public void setDistType(String distType) {
			this.distType = distType;
		}
		
		double latency;
		double deterministicValue;
		double normalMean;
		double normalStdDev;
		double uniformMax;
		double uniformMin;
		String distType;
		
		public SensorSpec(String name, String parent, double latency, double deterministicValue, double normalMean,
				double normalStdDev, double uniformMax, double uniformMin, String distType) {
			super(name, "sensor");
			this.latency = latency;
			this.deterministicValue = deterministicValue;
			this.normalMean = normalMean;
			this.normalStdDev = normalStdDev;
			this.uniformMax = uniformMax;
			this.uniformMin = uniformMin;
			this.distType = distType;
		}
		
		public Sensor addToApp(int userId, String appId, Application application) {
			Distribution dist;
			switch (this.distType){
				case "Deterministic":
				dist = new DeterministicDistribution(this.deterministicValue);
				break;
				case "Normal":
				dist = new NormalDistribution(this.normalMean, this.normalStdDev);
				break;
				case "Uniform":
				dist = new UniformDistribution(this.uniformMin, this.uniformMax);
				break;
				default:
				dist = null;
			}
			return new Sensor(this.name, this.name, userId, appId, dist);
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
			
			for (int i = 0; i < sensorSplit.length; i++) {
				String[] sensorSplit2 = sensorSplit[i].split("=");
				obj.put(sensorSplit2[0], sensorSplit2[1]);
			}
			
			return obj;
		}
	}
	
	public static class ActuatSpec extends NodeSpec {
		public double getUpLinklatency() {
			return UpLinklatency;
		}
		
		public void setUpLinklatency(double upLinklatency) {
			UpLinklatency = upLinklatency;
		}
		
		public double UpLinklatency;
		
		public ActuatSpec(String name, String type, double UpLinklatency) {
			super(name, "actuat");
			this.UpLinklatency = UpLinklatency;
		}
		
		public Actuator addToApp(int userId, String appId) {
			return new Actuator(this.name, userId, appId, this.name);
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
			
			for (int i = 0; i < actuatorSplit.length; i++) {
				String[] actuatorSplit2 = actuatorSplit[i].split("=");
				obj.put(actuatorSplit2[0], actuatorSplit2[1]);
			}
			return obj;
		}
	}
	
	public static class EdgeSpec extends Spec {
		public Spec getSrc() {
			return src;
		}
		
		public void setSrc(NodeSpec src) {
			this.src = src;
		}
		
		public Spec getDst() {
			return dst;
		}
		
		public void setDst(NodeSpec dst) {
			this.dst = dst;
		}
		
		public String getEdgeType() {
			return edgeType+"";
		}
		
		public void setEdgeType(int edgeType) {
			this.edgeType = edgeType;
		}
		
		public String getLatency() {
			return latency+"";
		}
		
		public void setLatency(double latency) {
			this.latency = latency;
		}
		
		public String getTupleType() {
			return tupleType;
		}
		
		public void setTupleType(String tupleType) {
			this.tupleType = tupleType;
		}
		
		public String getPeriodicity() {
			return periodicity+"";
		}
		
		public void setPeriodicity(double periodicity) {
			this.periodicity = periodicity;
		}
		
		public String getCpuLength() {
			return cpuLength+"";
		}
		
		public void setCpuLength(double cpuLength) {
			this.cpuLength = cpuLength;
		}
		
		public String getNwLength() {
			return nwLength+"";
		}
		
		public void setNwLength(double nwLength) {
			this.nwLength = nwLength;
		}
		
		public String getDirection() {
			return direction+"";
		}
		
		public void setDirection(int direction) {
			this.direction = direction;
		}
		
		NodeSpec src;
		NodeSpec dst;
		// int DEVICE = 0;
		// public static final int SENSOR = 1; // App Edge originates from a sensor
		// public static final int ACTUATOR = 2; // App Edge leads to an actuator
		// public static final int MODULE = 3; // App Edge is between application
		// modules
		public int edgeType;
		public double latency;
		public String tupleType;
		public double periodicity;
		public double cpuLength;
		public double nwLength;
//		public static final int UP = 1; //I THINK this is src->dst
//		public static final int DOWN = 2; //I THINK this is dst->src
//		public static final int ACTUATOR = 3; //I THINK src->actuator
		public int direction = 1;
		
		@Override
		public String toString() {
			return "src=" + src.name + ",dst=" + dst.name + ",edgeType=" + edgeType + ",latency=" + latency
					+ ",tupleType=" + tupleType + ",periodicity=" + periodicity + ",cpuLength=" + cpuLength
					+ ",nwLength=" + nwLength + ",direction=" + direction;
		}
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			JSONObject obj = new JSONObject();
			String edgeString = this.toString();
			String[] edgeSplit = edgeString.split(",");
			for (int i = 0; i < edgeSplit.length; i++) {
				String[] edgeSplit2 = edgeSplit[i].split("=");
				obj.put(edgeSplit2[0], edgeSplit2[1]);
			}
			
			return obj;
		}
		
		public EdgeSpec(NodeSpec src, NodeSpec dst, int edgeType, double latency, String tupleType,
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
		
		public EdgeSpec(NodeSpec src, NodeSpec dst) {
			this(src, dst, -1, 0, "null", 0, 0, 0, 0);
		}

			
		public EdgeSpec(NodeSpec src, NodeSpec dst, double latency) {
			this(src, dst, 0, latency, "null", 0, 0, 0, 0);
		}
		
		public Application addToApp(List<FogDevice> fogDevices, List<Sensor> sensors, List<Actuator> actuators, Application application, ModuleMapping moduleMapping) {
			if (this.edgeType == 0){
				FogDevice srcDev =null;
			    FogDevice dstDev =null;
			    Sensor srcSen = null;
			    Actuator dstAct = null;
			    for(FogDevice f : fogDevices) {
			        if (f.getName().equals(this.src.name)) srcDev=f;
			        if (f.getName().equals(this.dst.name)) dstDev=f;
			    }
			    if(srcDev!=null && dstDev !=null) {
			        srcDev.setUplinkLatency(this.latency);
			        srcDev.setParentId(dstDev.getId());
			    }else {
			    	for(Sensor s: sensors)if(s.getName().equals(this.src.name))srcSen = s;
			    	for(Actuator a: actuators)if(a.getName().equals(this.dst.name))dstAct = a;
			    	if(srcSen != null) {
			    		srcSen.setGatewayDeviceId(dstDev.getId());
			    		srcSen.setLatency(this.latency);
			    	}else if(dstAct != null) {
			    		dstAct.setGatewayDeviceId(srcDev.getId());
			    		dstAct.setLatency(this.latency);
			    	}
			    }
			    return null;
			}else if(this.edgeType == -1) {
				moduleMapping.addModuleToDevice(this.src.name, this.dst.name);
				return null;
			}else {
				application.addAppEdge(this.src.name, this.dst.name, this.periodicity, this.cpuLength, this.nwLength, this.tupleType, this.direction, this.edgeType);
			}			
			return application;
		}
		
		void draw(GraphicsContext gc) {
			gc.beginPath();
			gc.setStroke(this.selected ? Color.BLUE : Color.BLACK);
			gc.setLineWidth(this.selected ? 10.0 : 1.0);
			gc.moveTo(this.src.x, this.src.y);
			gc.lineTo(this.dst.x, this.dst.y);
			gc.stroke();
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1.0);
		}
	}
		
	public static class TupleSpec extends Spec {
		public String getInTuple() {
			return inTuple;
		}

		public void setInTuple(String inTuple) {
			this.inTuple = inTuple;
		}

		public String getOutTuple() {
			return outTuple;
		}

		public void setOutTuple(String outTuple) {
			this.outTuple = outTuple;
		}

		public double getFractionalSensitivity() {
			return fractionalSensitivity;
		}

		public void setFractionalSensitivity(double fractionalSensitivity) {
			this.fractionalSensitivity = fractionalSensitivity;
		}

		public String inTuple;
		public String outTuple;
		public double fractionalSensitivity;
		@Override
		public String toString() {
			return "inTuple=" + inTuple + ",outTuple=" + outTuple + ",fractionalSensitivity="
					+ fractionalSensitivity;
		}
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			JSONObject obj = new JSONObject();
			String tupleString = this.toString();
			String[] tupleSplit = tupleString.split(",");
			for (int i = 0; i < tupleSplit.length; i++) {
				String[] tupleSplit2 = tupleSplit[i].split("=");
				obj.put(tupleSplit2[0], tupleSplit2[1]);
			}
			return obj;
		}
		
		public TupleSpec(String inTuple, String outTuple, double fractionalSensitivity) {
			this.inTuple = inTuple;
			this.outTuple = outTuple;
			this.fractionalSensitivity = fractionalSensitivity;
		}
	}
	
	public static class placementObject{
		public String device;
		public String module;
		
		public placementObject(String device, String module) {
			this.device = device;
			this.module = module;
		}
		
		public void addToPreview() {
			//TODO Preview
			ModulePlacement.placementList.add(this);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void writeJSON(String jsonFileName, int simGranularity, int simTotLength, String placementPolicy,
			String topLvNode) {
		JSONObject obj = new JSONObject();
		JSONArray devicesJSONObj = new JSONArray();
		JSONArray modulesJSONObj = new JSONArray();
		JSONArray sensorsJSONObj = new JSONArray();
		JSONArray actuatsJSONObj = new JSONArray();
		JSONArray edgesJSONObj = new JSONArray();
		
		for (DeviceSpec h : devicesList)
			devicesJSONObj.add(h.toJSON());
		for (ModuleSpec m : modulesList)
			modulesJSONObj.add(m.toJSON());
		for (SensorSpec s : sensorsList)
			sensorsJSONObj.add(s.toJSON());
		for (ActuatSpec a : actuatsList)
			actuatsJSONObj.add(a.toJSON());
//		for (EdgeSpec e : edgesList) edgesJSONObj.add(e.toJSON());
			
		JSONObject metaList = new JSONObject();
		// TODO colors + zoomlv need to be in here as well
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