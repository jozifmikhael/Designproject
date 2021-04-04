package org.fog.test.perfeval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.io.BufferedWriter;
// Added by us
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//import java.io.FileNotFoundException;
//import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import org.json.simple.parser.ParseException;
import org.cloudbus.cloudsim.Vm;
//import org.cloudbus.cloudsim.VmAllocationPolicySimple;
//import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.Cloudlet;
//import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
//import java.text.DecimalFormat;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.application.AppEdge;
import org.fog.application.AppLoop;
import org.fog.application.Application;
import org.fog.application.selectivity.FractionalSelectivity;
import org.fog.entities.Actuator;
import org.fog.entities.FogBroker;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.entities.Sensor;
import org.fog.entities.Tuple;
import org.fog.placement.Controller;
import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacementEdgewards;
import org.fog.placement.ModulePlacementMapping;
import org.fog.placement.ModulePlacementOnlyCloud;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.utils.Config;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.TimeKeeper;
import org.fog.utils.distribution.DeterministicDistribution;
import org.fog.utils.distribution.NormalDistribution;	
import org.fog.utils.distribution.UniformDistribution;

/**
 * Simulation setup for case study 1 - EEG Beam Tractor Game
 *
 */
public class VRGameFog {
	static List<FogDevice> fogDevices = new ArrayList<FogDevice>();
	static List<Sensor> sensors = new ArrayList<Sensor>();
	static List<Actuator> actuators = new ArrayList<Actuator>();
	
	static String sensorTuple = null;	
	static String actuatorType = null;
	
	static int userId;
	static String appId;
	
	static int numOfDepts = 1;
	static int numOfMobilesPerDept = 1;
	static double EEG_TRANSMISSION_TIME = 5.1;
	
	public static void main(String[] args) throws Exception {
		VRGameFog obj = new VRGameFog("test10.json");
	}
	
	public VRGameFog(String jsonFile) throws Exception {

		System.out.println("Starting VRGame... ");
		Log.disable();
		int num_user = 1; // number of cloud users
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false; // mean trace events

		CloudSim.init(num_user, calendar, trace_flag);

		appId = "vr_game"; // identifier of the application
		
		PowerDatacenterBroker broker = new FogBroker("broker");
		userId = broker.getId();
		System.out.println("Starting parsing...");
		JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(jsonFile);
        Object obj = jsonParser.parse(reader);
        JSONObject jsonObject = (JSONObject) obj;
        Application application = createApplication(appId, broker.getId(), jsonObject);
        application.setUserId(broker.getId());
        
        
		
		
		
//		FogDevice cloud = createFogDevice("cloud", 44800, 40000, 100, 10000, 0, 0.01, 16*103, 16*83.25); // creates the fog device Cloud at the apex of the hierarchy with level=0
//		cloud.setParentId(-1);
//		FogDevice proxy = createFogDevice("proxy-server", 2800, 4000, 10000, 10000, 1, 0.0, 107.339, 83.4333); // creates the fog device Proxy Server (level=1)
//		proxy.setParentId(cloud.getId()); // setting Cloud as parent of the Proxy Server
		//proxy.setUplinkLatency(100); // latency of connection from Proxy Server to the Cloud is 100 ms
			
//		fogDevices.add(cloud);
//		fogDevices.add(proxy);
		
//		for(int i=0;i<numOfDepts;i++){
//			addGw(i+"", userId, appId, proxy.getId()); // adding a fog device for every Gateway in physical topology. The parent of each gateway is the Proxy Server
//		}
		


        JSONObject metaArr = (JSONObject) jsonObject.get("meta");
        String simPolicy = (String) metaArr.get("policy");
        long granularity = (long) metaArr.get("granularity");
        long time = (long) metaArr.get("time");
        Config.RESOURCE_MANAGE_INTERVAL = (int) granularity;
        Config.RESOURCE_MGMT_INTERVAL = (double) granularity;
        Config.MAX_SIMULATION_TIME = (int) time;
        Config.TOP_NODE = (String) metaArr.get("central");
        
        System.out.println("Finished meta");
        
		JSONArray sensorArr = (JSONArray) jsonObject.get("sensors");
		sensorArr.forEach(l -> parseSensorObject((JSONObject) l));
		JSONArray actuatorArr = (JSONArray) jsonObject.get("actuators");
		actuatorArr.forEach(l -> parseActuatorObject((JSONObject) l));
        JSONArray edgeArr = (JSONArray) jsonObject.get("edges");
        edgeArr.forEach(n -> parseEdgeObject((JSONObject) n, application));
    	JSONArray nodeArr = (JSONArray) jsonObject.get("nodes");
		nodeArr.forEach(n -> parseNodeObject( (JSONObject)n));
//        JSONArray linkArr = (JSONArray) jsonObject.get("links");
//        linkArr.forEach(l -> parseLinkObject((JSONObject) l));
		
		ModuleMapping moduleMapping = ModuleMapping.createModuleMapping(); // initializing a module mapping
		moduleMapping.addModuleToDevice("connector", "cloud");
//		moduleMapping.addModuleToDevice("concentration_calculator", "gateway1");
		Controller controller = new Controller("master-controller", fogDevices, sensors, actuators);
		controller.submitApplication(application, 0,(new ModulePlacementEdgewards(fogDevices, sensors, actuators, application, moduleMapping)));
		
		TimeKeeper.getInstance().setSimulationStartTime(Calendar.getInstance().getTimeInMillis());
		CloudSim.startSimulation();
		CloudSim.stopSimulation();
		Log.printLine("VRGame finished!");	
	}
	
	
	private static void parseNodeObject(JSONObject node) {
        double nodeBusyPower = (double) node.get("apower");
        int nodeLevel = Integer.parseUnsignedInt(node.get("level").toString());
        double nodeRatePerMips = (double) node.get("rate");
        double nodeIdlePower = (double) node.get("ipower");
        String nodeID = (String) node.get("name");
        long nodeDownBw = Long.parseUnsignedLong(node.get("downbw").toString());
        long nodeUpBw = Long.parseUnsignedLong(node.get("upbw").toString());
        long nodeMips = Long.parseUnsignedLong(node.get("mips").toString());
        int nodeRam = Integer.parseUnsignedInt(node.get("ram").toString());
        
        
		
        FogDevice mobile = createFogDevice(nodeID, nodeMips, nodeRam, nodeUpBw, nodeDownBw, nodeLevel, nodeRatePerMips, nodeBusyPower, nodeIdlePower);
		
		fogDevices.add(mobile);
		Sensor eegSensor = new Sensor("s-"+nodeID, "EEG", userId, appId, new DeterministicDistribution(EEG_TRANSMISSION_TIME)); // inter-transmission time of EEG sensor follows a deterministic distribution
		sensors.add(eegSensor);
		eegSensor.setGatewayDeviceId(mobile.getId());
		eegSensor.setLatency(6.0);
//		Actuator display = new Actuator("a-"+nodeID, userId, appId, "DISPLAY");
//		actuators.add(display);
//		display.setGatewayDevice////Id(mobile.getId());
//		display.setLatency(1.0);
    }

	private static void parseSensorObject(JSONObject sensor) {	
		String sensorName = (String) sensor.get("name");
		String distribution = (String) sensor.get("distribution");
		double deterministicValue = (double) sensor.get("deterministicValue");
		double normalMean = (double) sensor.get("normalMean");
		double normalStdDev = (double) sensor.get("normalStdDev");
		double uniformMax = (double) sensor.get("uniformMax");
		double uniformMin = (double) sensor.get("uniformMin");
		String tupleType = (String) sensor.get("tupleType");
		
		if(distribution.equals("Deterministic")) {	
			Sensor newSensor = new Sensor(sensorName, tupleType, userId, appId, new DeterministicDistribution(deterministicValue)); // inter-transmission time of EEG sensor follows a deterministic distribution	
			sensors.add(newSensor);	
		}
		else if(distribution.equals("Normal")) {	
			Sensor newSensor = new Sensor(sensorName, tupleType, userId, appId, new NormalDistribution(normalMean, normalStdDev)); // inter-transmission time of EEG sensor follows a deterministic distribution	
			sensors.add(newSensor);	
		}
		else if(distribution.equals("Uniform")) {	
			Sensor newSensor = new Sensor(sensorName, tupleType, userId, appId, new UniformDistribution(uniformMin, uniformMax)); // inter-transmission time of EEG sensor follows a deterministic distribution	
			sensors.add(newSensor);	
		}	
	}
	private static void parseActuatorObject(JSONObject actuator) {	
		String actuatorName = (String) actuator.get("name");
		String tupleType = (String) actuator.get("tupleType");
		Actuator display = new Actuator(actuatorName, userId, appId, tupleType);
		actuators.add(display);
	}
	
	private static void parseLinkObject(JSONObject link) {
		double latency = (double) (link.get("latency"));
		String srcID = (String) link.get("srcID");
		String dstID = (String) link.get("dstID");
		FogDevice src = null;
		FogDevice dst = null;
		Sensor sensorSrc = null;
		Actuator actuatorSrc = null;

		for(FogDevice device : fogDevices) {
			if (device.getName().equals(srcID)) src=device;
			if (device.getName().equals(dstID)) dst=device;
		}
		for(Sensor sensor : sensors) {	
			if (sensor.getName().equals(srcID)) sensorSrc=sensor;	
		}	
			
		for(Actuator actuator : actuators) {	
			if (actuator.getName().equals(srcID)) actuatorSrc = actuator;	
		}
		if(!(src==null || dst==null)) {
			src.setParentId(dst.getId());
			src.setUplinkLatency(latency);
		}else if(!(sensorSrc==null || dst==null)) {	
			sensorSrc.setGatewayDeviceId(dst.getId());	
			sensorSrc.setLatency(latency);	
		}else if(!(actuatorSrc==null || dst==null)) {	
			actuatorSrc.setGatewayDeviceId(dst.getId());	
			actuatorSrc.setLatency(latency);
		}else System.out.println("Error src/dst not found");
	}
	
	private static void parseModuleObject(JSONObject module, Application application) {
		String name = (String) module.get("name");
		int ram = Integer.parseUnsignedInt(module.get("ram").toString());
		int mips = Integer.parseUnsignedInt(module.get("mips").toString());
		long size = Long.parseUnsignedLong(module.get("size").toString());
		long bw = Long.parseUnsignedLong(module.get("bandwidth").toString()); 		
		application.addAppModule(name, ram, mips, size, bw);
		
		JSONArray tuplemap = (JSONArray) module.get("TupleMaps");
		tuplemap.forEach(n -> parseTupleMapping((JSONObject) n, application, name));
	}
	
	private static void parseEdgeObject(JSONObject edges, Application application) {
		String src = (String) edges.get("src");
		String dest = (String) edges.get("dest");
		double periodicity = (double) edges.get("periodicity");
		double tupleCpuLength = (double) edges.get("tupleCpuLength");
		double tupleNwLength = (double) edges.get("tupleNwLength");
		String tupleType = (String) edges.get("tupleType");
		int direction = Integer.parseUnsignedInt(edges.get("direction").toString());
		int edgeType = Integer.parseUnsignedInt(edges.get("edgeType").toString());
		double latency = (double) edges.get("latency");
		
		if (edgeType == 2) {
			for (Actuator actuator : actuators) {
				if (actuator.getName().equals(dest)) actuator.setLatency(latency);
			}
		} else if (edgeType == 1) {
			for (Sensor sensor : sensors) {
				if(sensor.getName().equals(src)) sensor.setLatency(latency);
			}
		} else if (edgeType == 0){
			FogDevice srcDev =null;
		    FogDevice dstDev =null;
		    for(FogDevice f : fogDevices) {
		        if (f.getName().equals(src)) srcDev=f;
		        if (f.getName().equals(dest)) dstDev=f;
		    }
		    if(srcDev!=null && dstDev !=null) {
		        srcDev.setUplinkLatency(latency);
		        srcDev.setParentId(dstDev.getId());
		    }
		}
		
		application.addAppEdge(src, dest, periodicity, tupleCpuLength, tupleNwLength, tupleType, direction, edgeType);	
	}
	
	private static void parseTupleMapping(JSONObject tuplemaps, Application application, String name) {
		String inTuple = (String) tuplemaps.get("inTuple");
		String outTuple = (String) tuplemaps.get("outTuple");
		double fractionalSensitivity = (double) tuplemaps.get("fractionalSensitivity");
		application.addTupleMapping(name, inTuple, outTuple, new FractionalSelectivity(fractionalSensitivity));
	}
	
	/**
	 * Creates a vanilla fog device
	 * @param nodeName name of the device to be used in simulation
	 * @param mips MIPS
	 * @param ram RAM
	 * @param upBw uplink bandwidth
	 * @param downBw downlink bandwidth
	 * @param level hierarchy level of the device
	 * @param ratePerMips cost rate per MIPS used
	 * @param busyPower
	 * @param idlePower
	 * @return
	 */
	private static FogDevice createFogDevice(String nodeName, long mips,
			int ram, long upBw, long downBw, int level, double ratePerMips, double busyPower, double idlePower) {
		
		List<Pe> peList = new ArrayList<Pe>();
		
		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerOverbooking(mips))); // need to store Pe id and MIPS Rating
		
		int hostId = FogUtils.generateEntityId();
		long storage = 1000000; // host storage
		int bw = 10000;

		PowerHost host = new PowerHost(
				hostId,
				new RamProvisionerSimple(ram),
				new BwProvisionerOverbooking(bw),
				storage,
				peList,
				new StreamOperatorScheduler(peList),
				new FogLinearPowerModel(busyPower, idlePower)
			);

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

		FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
				arch, os, vmm, host, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		FogDevice fogdevice = null;
		try {
			fogdevice = new FogDevice(nodeName, characteristics, 
					new AppModuleAllocationPolicy(hostList), storageList, 10, upBw, downBw, 0, ratePerMips);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fogdevice.setLevel(level);
		return fogdevice;
	}
	
	@SuppressWarnings({"serial" })
	private static Application createApplication(String appId, int userId,JSONObject jsonObject){		
		Application application = Application.createApplication(appId, userId); // creates an empty application model (empty directed graph)
		JSONArray modArr = (JSONArray) jsonObject.get("modules");	
        modArr.forEach(n -> parseModuleObject((JSONObject) n, application));
		/*
		 * Adding modules (vertices) to the application model (directed graph)
		 */
		/*
		 * Connecting the application modules (vertices) in the application model (directed graph) with edges
		 */
//		application.addAppEdge("EEG", "client", 0.0,3000, 500, "EEG", Tuple.UP, AppEdge.SENSOR);
//		application.addAppEdge("client", "concentration_calculator", 0.0,3500, 500, "_SENSOR", Tuple.UP, AppEdge.MODULE); // adding edge from Client to Concentration Calculator module carrying tuples of type _SENSOR
//		application.addAppEdge("concentration_calculator", "connector", 100, 1000, 1000, "PLAYER_GAME_STATE", Tuple.UP, AppEdge.MODULE); // adding periodic edge (period=1000ms) from Concentration Calculator to Connector module carrying tuples of type PLAYER_GAME_STATE
//		application.addAppEdge("concentration_calculator", "client", 0.0,14, 500, "CONCENTRATION", Tuple.DOWN, AppEdge.MODULE);  // adding edge from Concentration Calculator to Client module carrying tuples of type CONCENTRATION
//		application.addAppEdge("connector", "client", 100, 28, 1000, "GLOBAL_GAME_STATE", Tuple.DOWN, AppEdge.MODULE); // adding periodic edge (period=1000ms) from Connector to Client module carrying tuples of type GLOBAL_GAME_STATE
//		application.addAppEdge("client", "DISPLAY", 0.0,1000, 500, "SELF_STATE_UPDATE", Tuple.DOWN, AppEdge.ACTUATOR);  // adding edge from Client module to Display (actuator) carrying tuples of type SELF_STATE_UPDATE
//		application.addAppEdge("client", "DISPLAY", 0.0,1000, 500, "GLOBAL_STATE_UPDATE", Tuple.DOWN, AppEdge.ACTUATOR);  // adding edge from Client module to Display (actuator) carrying tuples of type GLOBAL_STATE_UPDATE
//		
		/*
		 * Defining the input-output relationships (represented by selectivity) of the application modules. 
		 */
//		application.addTupleMapping("client", "EEG", "_SENSOR", new FractionalSelectivity(1.0)); // 0.9 tuples of type _SENSOR are emitted by Client module per incoming tuple of type EEG 
//		application.addTupleMapping("client", "CONCENTRATION", "SELF_STATE_UPDATE", new FractionalSelectivity(1.0)); // 1.0 tuples of type SELF_STATE_UPDATE are emitted by Client module per incoming tuple of type CONCENTRATION 
//		application.addTupleMapping("concentration_calculator", "_SENSOR", "CONCENTRATION", new FractionalSelectivity(1.0)); // 1.0 tuples of type CONCENTRATION are emitted by Concentration Calculator module per incoming tuple of type _SENSOR 
//		application.addTupleMapping("client", "GLOBAL_GAME_STATE", "GLOBAL_STATE_UPDATE", new FractionalSelectivity(1.0)); // 1.0 tuples of type GLOBAL_STATE_UPDATE are emitted by Client module per incoming tuple of type GLOBAL_GAME_STATE 
	
		/*
		 * Defining application loops to monitor the latency of. 
		 * Here, we add only one loop for monitoring : EEG(sensor) -> Client -> Concentration Calculator -> Client -> DISPLAY (actuator)
		 */
		final AppLoop loop1 = new AppLoop(new ArrayList<String>(){{add("EEG");add("client");add("concentration_calculator");add("client");add("DISPLAY");}});
		List<AppLoop> loops = new ArrayList<AppLoop>(){{add(loop1);}};
		application.setLoops(loops);
		
		return application;
	}
}