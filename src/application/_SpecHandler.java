package application;

import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacement;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import javafx.scene.effect.DropShadow;

import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.*;

public class _SpecHandler {
	@FXML
	AnchorPane backPane;
	public static ArrayList<NodeSpec> nodesList = new ArrayList<NodeSpec>();
	static double R = 50;
	static double zoomFactor = 1;
	static int fontSize = 16;
	static String font = "monospaced";
	
	static Spec selectedObject	  = null;
	static NodeSpec selectedNode = null;
	static EdgeSpec selectedEdge = null;
	
	static GraphicsContext gc = null;
	
	static Color selectColor = Color.web("#0091ff", 1);
	static Color deviceColor = Color.web("#ffa200", 1);//Color.RED; //Theme Orange
	static Color moduleColor = Color.web("#ffdd00", 1);//CYAN; //Yellowish Orange
	static Color sensorColor = Color.web("#bce105", 1);//.PINK; //GREEN #33dc04
	static Color actuatorColor = Color.web("#a0f3de", 1); //YELLOW; #04b1dc
	static Color transpColor = Color.TRANSPARENT;
	static List<Color> validColors = Arrays.asList(deviceColor, moduleColor, sensorColor, actuatorColor, transpColor);
	static Color _errorColor = Color.RED;
		
	public static void shiftPositionsByZoom(ScrollEvent event) {
		double minZoom=0.25; double maxZoom=1.5; double zoomStep=0.05;
    	double preZoom=zoomFactor;
    	zoomFactor+=(event.getDeltaY()>0)?zoomStep:-zoomStep;
    	if(zoomFactor<minZoom) zoomFactor=minZoom;
    	if(zoomFactor>maxZoom) zoomFactor=maxZoom;
    	double zoomRatio = zoomFactor/preZoom;
    	nodesList.forEach((d)->{d.x-=(d.x-event.getX())*2*(1-zoomRatio); d.y-=(d.y-event.getY())*2*(1-zoomRatio);});
	}
	
	public static NodeSpec makeNewNodeSelection(MouseEvent mEvent) {
		if(selectedNode!=null) selectedNode.isSelected=false;
		selectedNode=null; selectedObject=null;
		for (NodeSpec n : nodesList) {
			if (Math.pow(Math.pow(n.x - mEvent.getX(), 2) + Math.pow(n.y - mEvent.getY(), 2), 0.5) <= 0.5 * n.sz
					* zoomFactor) {
				selectedNode=n;
				selectedObject=selectedNode;
			}
		}
		if(selectedNode!=null) selectedNode.isSelected=true;
		return selectedNode;
	}
	
	public static EdgeSpec makeNewEdgeSelection(MouseEvent mEvent) {
		if(selectedEdge!=null) selectedEdge.isSelected=false;
		printDebug("In makeNewEdgeSelection");
		selectedEdge=null; selectedObject=null;
		for(NodeSpec n : _SpecHandler.nodesList) {
			for(EdgeSpec e : n.edgesList) {
				if(e.dst == null || e.src == null) continue;
				if((mEvent.getX() <= e.dst.x && mEvent.getX() >= e.src.x && mEvent.getY() <= e.dst.y && mEvent.getY() >= e.src.y)
						||(mEvent.getX() <= e.src.x && mEvent.getX() >= e.dst.x && mEvent.getY() <= e.src.y && mEvent.getY() >= e.dst.y)
						||(mEvent.getX() <= e.dst.x && mEvent.getX() >= e.src.x && mEvent.getY() <= e.src.y && mEvent.getY() >= e.dst.y)
						||(mEvent.getX() <= e.src.x && mEvent.getX() >= e.dst.x && mEvent.getY() <= e.dst.y && mEvent.getY() >= e.src.y)) {
					double m = (e.src.y - e.dst.y)/(e.src.x - e.dst.x);
					if(e.src.x - e.dst.x == 0 && Math.abs(mEvent.getX() - e.dst.x) <= 10) {selectedEdge=e; selectedObject=selectedEdge;}
					else if(e.src.y - e.dst.y == 0 && Math.abs(mEvent.getY() - e.dst.y) <= 10) {selectedEdge=e; selectedObject=selectedEdge;}
					else {
						double bLink = (e.dst.y - (m*e.dst.x));
						double mTemp = -(1/m);
						double bTemp = mEvent.getY() - (mTemp * mEvent.getX());
						double xLine = (bTemp - bLink)/(m - mTemp);
						double yLine = m * xLine + bLink;
						double distance = Math.sqrt(Math.pow(xLine - mEvent.getX(), 2) + Math.pow(yLine - mEvent.getY(),2));
						if(distance <= 10) {selectedEdge=e;  selectedObject=selectedEdge;}
					}
				}
			}
		}
		if(selectedEdge!=null) {
			selectedEdge.isSelected=true;
			printDebug("found new edge");
		}
		return selectedEdge;
	}

	public static void makeNewSelection(MouseEvent mEvent) {
		deselectAll();
		makeNewNodeSelection(mEvent);
		if(selectedObject!=null) return;
		makeNewEdgeSelection(mEvent);
	}
	
	public static void deselectAll() {
		if(selectedObject!=null) selectedObject.isSelected=false;
		selectedObject=null;
		selectedNode=null;
		selectedEdge=null;
	}
	public static void drawBorder() {
        gc.setStroke(Color.LIGHTGREY);
        gc.setLineWidth(10.0);
        double w = gc.getCanvas().getWidth(); double l = gc.getCanvas().getHeight();
        gc.strokeRect(0, 0, w, l);
    }
	public static Spec getNode(String nodeName) {
		for(NodeSpec n : nodesList) if(n.name.equals(nodeName)) return n;
		return null;
	}
	
	public static void pruneLinks() {
		for (NodeSpec n : nodesList)
			n.edgesList = (ArrayList<EdgeSpec>) n.edgesList.stream()
			.filter(e -> !(e.dst==null))
			.filter(e -> !e.dst.isTemp)
			.filter(e -> !e.dst.equals(e.src))
			.filter(e -> !(e.edgeType == -2))
			.collect(Collectors.toList());
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
	public static class Spec {
		protected boolean isTemp = false;
		public boolean isSelected = false;
		public String type;
				
		Spec copy() {return new Spec(this.toJSON());};
		Spec pop() {return this;}
		public Spec() {}
		
		public Spec(JSONObject obj) {
			Field[] cFields = this.getClass().getFields();
			for (int i = 0; i < cFields.length; i++) {
				Field f = cFields[i];
				try {
					switch (f.getType().toString()) {
						case "boolean": f.set(this, Boolean.parseBoolean((String) 	obj.get(f.getName()))); break;
						case "double": 	f.set(this, Double.parseDouble((String) 	obj.get(f.getName()))); break;
						case "int": 	f.set(this, Integer.parseInt((String) 		obj.get(f.getName()))); break;
						case "long": 	f.set(this, Long.parseLong((String) 		obj.get(f.getName()))); break;
						case "class java.lang.String": f.set(this, (String) 		obj.get(f.getName())); 	break;
						case "class java.util.ArrayList": {
							if (f.getGenericType() instanceof ParameterizedType) {
								Type[] params = ((ParameterizedType) f.getGenericType()).getActualTypeArguments();
								printDebug("Found ArrayList of types " + params[0].getTypeName());
//								obj.keySet().forEach(o -> printDebug(o + ":" + obj.get(o)));
								printDebug("-Finished ArrayList");
							} else printDebug("Error, ArrayList found but is not instanceof ParameterizedType"); break;
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		JSONObject toJSON() {
			JSONObject obj = new JSONObject();
			Field[] cFields = this.getClass().getFields();
			
			for (int i = 0; i < cFields.length; i++) {
				Field f = cFields[i];
				try {
					printDebug();
					if (f.getGenericType() instanceof ParameterizedType) {
						printDebug(f.getName());
						ArrayList<?> arrRef = (ArrayList<?>) f.get(this);
						if(arrRef == null) {
							printDebug(f.getName() + " is null");
							continue;
						}
						JSONArray newSet = new JSONArray();
						if(f.getGenericType().toString().contains("_SpecHandler$")) {
							printDebug("Adding a new spec entry");
							printDebug(arrRef.size());
							arrRef.forEach(a->newSet.add(((Spec)a).toJSON()));
						}else{
							printDebug("Adding a new non-spec entry");
							arrRef.forEach(a->newSet.add(a+""));
						}
						obj.put(f.getName(), newSet);
					}else{
						printDebug(cFields[i].getName());
						obj.put(cFields[i].getName(), cFields[i].get(this).toString());
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
			return obj;
		}
		
		public Spec reinit() {
			return null;
			
		}
		public Spec add() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public static class NodeSpec extends Spec {
		double sz;
		public double x;
		public double y;
		Color nodeColor;
		public String name;
		public ArrayList<EdgeSpec> edgesList = null;
		
		public Spec setSelected() {
			deselectAll();
			selectedNode=this;
			selectedObject=selectedNode;
			this.isSelected=true;
			return this;
		}
		
		public ArrayList<EdgeSpec> getEdgesList() {
			return edgesList;
		}
		
		public void setEdgesList(ArrayList<EdgeSpec> edgesList) {
			this.edgesList = edgesList;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public NodeSpec(JSONObject a) {
			super(a);
		}
		
		public NodeSpec(double x, double y, String name, String type) {
			this.x = x;
			this.y = y;
			this.sz = R + R;
			this.name = name;
			this.type = type;
			this.edgesList = new ArrayList<EdgeSpec>();
			this.setColor();
			this.add();
			this.setSelected();
			selectedNode=this;
			selectedObject=selectedNode;
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

		public NodeSpec(String name, String type) {
			this(0, 0, name, type);
		}

		NodeSpec copy() {return new NodeSpec(this.toJSON());};
		public NodeSpec add() {
			if (!(nodesList.stream().anyMatch(a -> a.name.equals(this.name))))
				nodesList.add(this);
			return this;
		}
		@Override
		public NodeSpec reinit() {
			this.edgesList = new ArrayList<EdgeSpec>();
			this.isSelected=false;
			this.isTemp = false;
			this.setPos(x+0.5*this.sz, y+0.5*this.sz);
			this.sz = R + R;
			this.setColor();
			return this;
		}
		NodeSpec pop() {
			nodesList.remove(this);
			this.isTemp = true;
			pruneLinks();
			return this;
		}

		@Override
		public String toString() {
			return "x=" + x + ", y=" + y + ", sz=" + sz + ", nodeColor=" + nodeColor + ", name=" + name + ", type="
					+ type + ", selected=" + isSelected;
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
//			DropShadow ds1 = new DropShadow();
//	        ds1.setOffsetY(3.0f);
//	        ds1.setOffsetX(3.0f);
//	        ds1.setColor(Color.LIGHTGREY);//web("#dbd7ce", 0.92));
			if (validColors.contains(this.nodeColor)) gc.setFill(this.nodeColor);
			else gc.setFill(_errorColor);
			gc.fillOval(this.x - 0.5 * this.sz * zoomFactor, this.y - 0.5 * this.sz * zoomFactor, this.sz * zoomFactor,
					this.sz * zoomFactor);
			if (nodeColor != transpColor) {
				gc.setStroke(Color.BLACK);
				gc.setLineWidth(1.0);
				if(zoomFactor < 0.4) gc.strokeText("...", this.x, this.y + 0.4 * fontSize);
	            else gc.strokeText(this.name, this.x, this.y + 0.4 * fontSize);
				gc.setStroke(this.isSelected ? Color.web("#0091ff", 1) : Color.BLACK);
				gc.setLineWidth(this.isSelected ? 5.0 : 3.0);
				gc.strokeOval(this.x - 0.5 * this.sz * zoomFactor, this.y - 0.5 * this.sz * zoomFactor,
						this.sz * zoomFactor, this.sz * zoomFactor);
//				gc.setEffect(ds1);
			}
			
		}
		
		NodeSpec setPos(MouseEvent mEvent) {
			this.x = mEvent.getX();
			this.y = mEvent.getY();
			return this;
		}
		
		NodeSpec setPos(double x, double y) {
			this.x = x;
			this.y = y;
			return this;
		}
		
		NodeSpec setPos(NodeSpec _node) {
			this.x = _node.x;
			this.y = _node.y;
			return this;
		}
		
		EdgeSpec newLinkTo(NodeSpec newDst) {
			if(newDst==null) return null;
			if(newDst==this) return null;
			for(EdgeSpec edge : this.edgesList) if(edge.dst.equals(newDst) && edge.dst.type.equals("device")) return null;
			EdgeSpec e = new EdgeSpec(this, newDst);
			this.edgesList.add(e);
			return e;
		}
	}
	
	public static class DeviceSpec extends NodeSpec {
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
			this.add();
		}
		
		public DeviceSpec(JSONObject a) {super(a);}
		
		@Override
        public String toString() {
            return "selected="+ isSelected +",pe=" + pe + ",mips=" + mips + ",ram=" + ram + ",level=" + level + ",rate=" + rate
                    + ",ipower=" + ipower + ",apower=" + apower + ",upbw=" + upbw + ",downbw=" + downbw + ",x=" + x
                    + ",y=" + y + ",name=" + name;
        }
		
		DeviceSpec copy() {return new DeviceSpec(this.toJSON());};
		public DeviceSpec add() {
			if (!(nodesList.stream().anyMatch(a -> a.name.equals(this.name)))) {
				nodesList.add(this);
			}
			return this;
		}

		@Override
		public DeviceSpec reinit() {
			this.edgesList = new ArrayList<EdgeSpec>();
			this.isSelected=false;
			this.isTemp = false;
			this.setPos(x+0.5*this.sz, y+0.5*this.sz);
			this.sz = R + R;
			this.setColor();
			return (DeviceSpec) this.add();
		}

		DeviceSpec pop() {
			nodesList.remove(this);
			this.isTemp = true;
			pruneLinks();
			return this;
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
	}
	
	public static class ModuleSpec extends NodeSpec {
		public long bandwidth;
		public long size;
		public int mips;
		public int ram;
		public ArrayList<TupleSpec> tupleMappings;
		
		public ModuleSpec(String name, String nodeName, int ram, long bandwidth, long size, int mips,
				ArrayList<TupleSpec> _tupleMappings) {
			super(name, "module");
			this.ram = ram;
			this.bandwidth = bandwidth;
			this.size = size;
			this.mips = mips;
			this.tupleMappings = new ArrayList<TupleSpec>(_tupleMappings);
			this.add();
		}

		public ModuleSpec reinit() {
			this.tupleMappings = new ArrayList<TupleSpec>();
			this.edgesList = new ArrayList<EdgeSpec>();
			this.isSelected=false;
			this.isTemp = false;
			this.sz = R + R;
			this.setColor();
			return (ModuleSpec) this.add();
		}

		ModuleSpec copy() {return new ModuleSpec(this.toJSON());};
		public ModuleSpec(JSONObject a) {super(a);}
		
		public Application addToApp(Application application) {
			application.addAppModule(this.name, this.ram, this.mips, this.size, this.bandwidth);
			for (TupleSpec tupleMap : this.tupleMappings)
				application.addTupleMapping(this.name, tupleMap.inTuple, tupleMap.outTuple,
						new FractionalSelectivity(tupleMap.fractionalSensitivity));
			return application;
		}
		@Override
		public String toString() {
			return "ram=" + ram + ", bandwidth=" + bandwidth + ", size=" + size + ", mips=" + mips + ", x=" + x + ", y="
					+ y + ", name=" + name + ", tuplemappings [ " + tupleMappings.toString() + "]";
		}
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
	}
	
	public static class SensorSpec extends NodeSpec {
		public double deterministicValue;
		public double normalMean;
		public double normalStdDev;
		public double uniformMax;
		public double uniformMin;
		public String distType;
		public SensorSpec(JSONObject a) {super(a);}

		public SensorSpec(String name, double deterministicValue, double normalMean, double normalStdDev, double uniformMax,
				double uniformMin) {
			super(name, "sensor");
			this.deterministicValue = deterministicValue;
			this.normalMean = normalMean;
			this.normalStdDev = normalStdDev;
			this.uniformMax = uniformMax;
			this.uniformMin = uniformMin;
			if(deterministicValue != 0)this.distType = "Deterministic";
			else if(normalMean != 0)this.distType = "Normal";
			else if(uniformMax != 0)this.distType = "Uniform";
		}

		public SensorSpec reinit() {
			this.edgesList = new ArrayList<EdgeSpec>();
			this.isSelected=false;
			this.isTemp = false;
			this.sz = R + R;
			this.setColor();
			return (SensorSpec) this.add();
		}

		SensorSpec copy() {return new SensorSpec(this.toJSON());};
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
			return "deterministicValue=" + deterministicValue + ", normalMean=" + normalMean
					+ ", normalStdDev=" + normalStdDev + ", uniformMax=" + uniformMax + ", uniformMin=" + uniformMin
					+ ", distType=" + distType;
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
	}
	
	public static class ActuatSpec extends NodeSpec {
		double latency;
		
		public ActuatSpec(String name, String type, double UpLinklatency) {
			super(name, "actuat");
			this.latency = UpLinklatency;
		}

		public ActuatSpec(JSONObject a) {super(a);}

		public ActuatSpec reinit() {
			this.edgesList = new ArrayList<EdgeSpec>();
			this.isSelected=false;
			this.isTemp = false;
			this.sz = R + R;
			this.setColor();
			return (ActuatSpec) this.add();
		}

		ActuatSpec copy() {return new ActuatSpec(this.toJSON());};
		@Override
		public String toString() {
			return "x=" + x + ",y=" + y + ",sz=" + sz + ",nodeColor=" + nodeColor + ",name=" + name + ",type=" + type;
		}

		public double getLatency() {
			return latency;
		}
		
		public void setLatency(double latency) {
			this.latency = latency;
		}
		
		public Actuator addToApp(int userId, String appId) {
			return new Actuator(this.name, userId, appId, this.name);
		}
	}
	
	static Map<String, Integer> allowableLinks = new HashMap<String, Integer>(){{
		put("devicedevice", -1);
		put("sensordevice", -1);
		put("deviceactuat", -1);
		put("moduledevice", 0);
		put("sensormodule", 1);
		put("moduleactuat", 2);
		put("modulemodule", 3);
	}};
	
	public static class EdgeSpec extends Spec {
		NodeSpec src;
		NodeSpec dst;
		public String srcName;
		public String dstName;
		public int edgeType;
		public double latency;
		public String tupleType;
		public double periodicity;
		public double cpuLength;
		public double nwLength;
		public int direction = 1;
		
		public EdgeSpec(NodeSpec src, NodeSpec dst, double latency, String tupleType, double periodicity,
				double cpuLength, double newLength, int direction, String type) {
			this.src = src;
			this.dst = dst;
			this.edgeType = allowableLinks.getOrDefault(src.type+dst.type, -2);
			this.type = this.edgeType>0?"edgeFull":"edgeSimple";
			if(this.src.type.equals("linker")) this.direction=2;
			if(this.dst.type.equals("actuat")) this.direction=2;
			this.dstName = dst.name;
			this.srcName = src.name;
			this.latency = latency;
			this.tupleType = tupleType;
			this.periodicity = periodicity;
			this.cpuLength = cpuLength;
			this.nwLength = newLength;
		}
		
		public EdgeSpec(NodeSpec src, NodeSpec dst) {
			this(src, dst, 2.0, "", 0, 0, 0, 1, "");
		}
		
		public EdgeSpec(JSONObject a) {super(a);}
		
		public Spec setSelected() {
			deselectAll();
			selectedEdge=this;
			selectedObject=selectedEdge;
			this.isSelected=true;
			return this;
		}
		
		EdgeSpec copy() {return new EdgeSpec(this.toJSON());};
		
		public EdgeSpec reinit() {
			printDebug("in edge reinit");
			if(this.src==null||this.dst==null) {
				for(NodeSpec n : nodesList) {
					if(n.name.equals(this.srcName))this.src = n;
					if(n.name.equals(this.dstName))this.dst = n;
				}
			}
			return this;
		}

		public EdgeSpec add() {
			this.src.edgesList.add(this);
			return this;
		}
		@Override
		Spec pop() {
			this.src.edgesList.remove(this);
			this.isTemp=true;
			return this;
		}

		void draw(GraphicsContext gc) {
			printDebug(this.toJSON());
            gc.beginPath();
            gc.setStroke(this.isSelected ? selectColor : Color.BLACK);
            gc.setLineWidth(this.isSelected ? 30.0 : 1.0);
            gc.moveTo(this.dst.x, this.dst.y);
            gc.lineTo(this.src.x, this.src.y);
            gc.stroke();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1.0);
            gc.setFill(this.isSelected ? selectColor : Color.BLACK);
            
            double distancex = this.src.x - this.dst.x;
            double distancey = this.src.y - this.dst.y;
            double rotation = -Math.atan2(distancex, distancey);
            double oldRotation = rotation;
            rotation = Math.toRadians(Math.toDegrees(rotation) + 90);
            double X = Math.round((float)(this.dst.x+Math.cos(rotation)*this.dst.sz/2));
            double Y = Math.round((float)(this.dst.y+Math.sin(rotation)*this.dst.sz/2));
            gc.save();
            if (this.src.isTemp) {
                double [] xpoints = {this.src.x, this.src.x+8, this.src.x-8};
                double [] ypoints = {this.src.y, this.src.y+8, this.src.y+8};
                
                rotate(gc,Math.toDegrees(oldRotation) + 180,this.src.x,this.src.y);
                gc.strokePolygon(xpoints, ypoints, 3);
                gc.fillPolygon(xpoints, ypoints, 3);
            } else {
                double [] xpoints = {X, X+8, X-8};
                double [] ypoints = {Y, Y+8, Y+8};
                gc.save();
                rotate(gc,Math.toDegrees(rotation) + 270,X,Y);
                gc.fillPolygon(xpoints, ypoints, 3);
            }
            gc.restore();
        }

		
		private void rotate(GraphicsContext gc, double angle, double px, double py) {
	        Rotate r = new Rotate(angle, px, py);
	        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	    }

		public Application addToApp(List<FogDevice> fogDevices, List<Sensor> sensors, List<Actuator> actuators, Application application, ModuleMapping moduleMapping) {
			if (this.edgeType == -1){
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
			}else if(this.edgeType == 0) {
				moduleMapping.addModuleToDevice(this.src.name, this.dst.name);
				return null;
			}else {
				application.addAppEdge(this.src.name, this.dst.name, this.periodicity, this.cpuLength, this.nwLength, this.tupleType, this.direction, this.edgeType);
			}			
			return application;
		}

		@Override
		public String toString() {
			return "srcName=" + srcName + ",dstName=" + dstName + ",latency=" + latency + ",isSelected=" + isSelected + ",isTemp=" + isTemp;
		}
	}
		
	public static class TupleSpec extends Spec {
		public String inTuple;
		public String outTuple;
		public double fractionalSensitivity;
		public String parentName;
		ModuleSpec parent;
		public TupleSpec(String inTuple, String outTuple, double fractionalSensitivity, ModuleSpec parent) {
			this.type = "tupleEdge";
			this.parent = parent;
			this.parentName = parent.name;
			this.inTuple = inTuple;
			this.outTuple = outTuple;
			this.fractionalSensitivity = fractionalSensitivity;
		}
		
		TupleSpec copy() {return new TupleSpec(this.toJSON());};
		public TupleSpec(JSONObject a) {super(a);}

		@Override
		Spec pop() {
			this.parent.tupleMappings.remove(this);
			return this;
		}

		public TupleSpec add() {
			if(this.parent!=null) this.parent.tupleMappings.add(this);
			return null;
		}
		
		public TupleSpec reinit() {
			if(this.parent==null) {
				for(NodeSpec n : nodesList) if(n.name.equals(this.parentName)) this.parent=(ModuleSpec) n;
			}
			return (TupleSpec)this.add();
		}
//
//		@Override
//		public String toString() {
//			return "inTuple=" + inTuple + ",outTuple=" + outTuple + ",fractionalSensitivity="
//					+ fractionalSensitivity;
//		}

		@Override
		public String toString() {
			return "TupleSpec [inTuple=" + inTuple + ", outTuple=" + outTuple + ", fractionalSensitivity="
					+ fractionalSensitivity + ", parentName=" + parentName + "]";
		}

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

		public String getParentName() {
			return parentName;
		}

		public void setParentName(String parentName) {
			this.parentName = parentName;
		}
	}
	
	public static class placementObject{
		public String device;
		public ArrayList<String> module = new ArrayList<String>();
		
		public placementObject(String device, String module) {
			this.device = device;
			this.module.add(module);
		}
		
		public static placementObject getDevice(String name) {
			for(placementObject o : ModulePlacement.placementList) if(o.device.equals(name)) return o;
			return null;
		}
		
		public void addToPreview() {
			ModulePlacement.placementList.add(this);
		}
	}
	
	public static void loadJSON(File file) {
		JSONParser jsonParser = new JSONParser();
		FileReader reader;
		try {
			reader = new FileReader(file);
	        JSONObject jsonFileObject = (JSONObject) jsonParser.parse(reader);
			printDebug("Loaded Json: " + file.getName());
	        for(Object jObj : (JSONArray) jsonFileObject.get("devices")) {
	        	DeviceSpec d = new DeviceSpec((JSONObject) jObj);
	        	d.reinit();
	        	if(((JSONObject)jObj).get("edgesList") == null) continue;
	        	for(Object jEdge : (JSONArray) ((JSONObject)jObj).get("edgesList")) {
	        		EdgeSpec e = new EdgeSpec((JSONObject) jEdge);
	        		d.edgesList.add(e);
	        	}
			}
	        for(Object jObj : (JSONArray) jsonFileObject.get("modules")) {
	        	ModuleSpec m = new ModuleSpec((JSONObject) jObj);
	        	m.reinit();
	        	if(((JSONObject)jObj).get("tupleMappings") == null) continue;
	        	for(Object jObject : (JSONArray) ((JSONObject)jObj).get("tupleMappings")) {
	        		TupleSpec t = new TupleSpec((JSONObject) jObject);
	        		t.reinit();
	        	}
	        	if(((JSONObject)jObj).get("edgesList") == null) continue;
	        	for(Object jEdge : (JSONArray) ((JSONObject)jObj).get("edgesList")) {
	        		EdgeSpec e = new EdgeSpec((JSONObject) jEdge);
	        		m.edgesList.add(e);
	        	}
			}
	        for(Object jObj : (JSONArray) jsonFileObject.get("sensors")) {
	        	SensorSpec s = new SensorSpec((JSONObject) jObj);
	        	s.reinit();
	        	if(((JSONObject)jObj).get("edgesList") == null) continue;
	        	for(Object jEdge : (JSONArray) ((JSONObject)jObj).get("edgesList")) {
	        		EdgeSpec e = new EdgeSpec((JSONObject) jEdge);
	        		s.edgesList.add(e);
	        	}
			}
	        for(Object jObj : (JSONArray) jsonFileObject.get("actuats")) {
	        	ActuatSpec a = new ActuatSpec((JSONObject) jObj);
	        	a.reinit();
	        	if(((JSONObject)jObj).get("edgesList") == null) continue;
	        	for(Object jEdge : (JSONArray) ((JSONObject)jObj).get("edgesList")) {
	        		EdgeSpec e = new EdgeSpec((JSONObject) jEdge);
	        		a.edgesList.add(e);
	        	}
			}
	        nodesList.forEach(n->n.edgesList.forEach(e->e.reinit()));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void writeJSON(File file2) {
		printDebug("In writeJSON");
		JSONObject obj = new JSONObject();
		JSONArray devicesJSONObj = new JSONArray();
		JSONArray modulesJSONObj = new JSONArray();
		JSONArray sensorsJSONObj = new JSONArray();
		JSONArray actuatsJSONObj = new JSONArray();
		JSONArray edgesJSONObj = new JSONArray();
		
		for (NodeSpec n : nodesList) {
			switch (n.type) {
				case "device": devicesJSONObj.add(n.toJSON()); break;
				case "module": modulesJSONObj.add(n.toJSON()); break;
				case "sensor": sensorsJSONObj.add(n.toJSON()); break;
				case "actuat": actuatsJSONObj.add(n.toJSON()); break;
			}
		}
		printDebug("Added jsons to lists");
		JSONObject metaList = new JSONObject();
		// TODO colors + zoomlv need to be in here as well
//		metaList.put("simGranularity", simGranularity);
//		metaList.put("simDuration", simTotLength);
////		metaList.put("topLevelNode", topLvNode);
//		metaList.put("placementPolicy", placementPolicy);

		obj.put("meta", metaList);
		obj.put("edges", edgesJSONObj);
		obj.put("devices", devicesJSONObj);
		obj.put("modules", modulesJSONObj);
		obj.put("sensors", sensorsJSONObj);
		obj.put("actuats", actuatsJSONObj);
		printDebug("Added lists to json");
		
		try {
			printDebug("Writing to " + file2);
			FileWriter file = new FileWriter(file2, false);
			printDebug(obj.toJSONString());
			file.write(obj.toJSONString().replaceAll(",", ",\n"));
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}