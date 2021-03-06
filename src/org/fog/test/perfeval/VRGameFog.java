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

import application.TxtParser;
import application.createJsonController;

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
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.TimeKeeper;
import org.fog.utils.distribution.DeterministicDistribution;

/**
 * @author 
 *
 */
public class VRGameFog {
	static List<FogDevice> fogDevices = new ArrayList<FogDevice>();
	static List<Sensor> sensors = new ArrayList<Sensor>();
	static List<Actuator> actuators = new ArrayList<Actuator>();
	static List<Cloudlet> cloudlets = new ArrayList<Cloudlet>();
	private static List<Vm> vmlist;
	
	static boolean CLOUD = false;
	static FogDevice cloud;
	static FogDevice proxy;
	static double EEG_TRANSMISSION_TIME = 5.1;

	static String sourceFile="test6.json";
	static String testout="blob.json";
	
	public static void main(String[] args) throws Exception{
		Log.printLine("Starting VRGame...");
		System.out.println(new DeterministicDistribution(5.1));
		testOut();
		try {
			Log.disable();
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			CloudSim.init(num_user, calendar, trace_flag);
			
			
			String appId = "vr_game"; // identifier of the application
			PowerDatacenterBroker broker = new FogBroker("broker");
			Application application = new Application(appId, broker.getId());
			application.setUserId(broker.getId());
			
			cloud = createFogDevice("cloud", 100000, 40000, 100, 10000, 0, 1, 3, 0); // creates the fog device Cloud at the apex of the hierarchy with level=0
			cloud.setParentId(-1);
			fogDevices.add(cloud);
			
//			proxy = createFogDevice("proxy-server", 2800, 4000, 10000, 10000, 1, 0.0, 107.339, 83.4333); // creates the fog device Proxy Server (level=1)
//			proxy.setParentId(cloud.getId());
//			fogDevices.add(proxy);
			
			
			//Parse JSON file and initialize nodes
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(sourceFile);
            Object obj = jsonParser.parse(reader);
            JSONObject nodeList = (JSONObject) obj;
            JSONArray nodeArr = (JSONArray) nodeList.get("nodes");
            nodeArr.forEach(n -> parseNodeObject( (JSONObject) n, broker.getId(), appId, cloud.getId()));
            JSONArray linkArr = (JSONArray) nodeList.get("links");
            linkArr.forEach(l -> parseLinkObject((JSONObject) l));
            JSONArray modArr = (JSONArray) nodeList.get("Modules");
            modArr.forEach(n -> parseModuleObject((JSONObject) n, application));
            JSONArray edgeArr = (JSONArray) nodeList.get("Edges");
            edgeArr.forEach(n -> parseEdgeObject((JSONObject) n, application));
            
            
//            vmlist = new ArrayList<Vm>();
			// VM description
//			int vmid = 0;
//			int mips = 1000;
//			long size = 10000; // image size (MB)
//			int ram = 512; // vm memory (MB)
//			long bw = 1000;
//			int pesNumber = 1; // number of cpus
//			String vmm = "Xen"; // VMM name
			
			// add the VM to the vmList
//			for(int i = 0; i<10000; i++) {
//				Cloudlet c = new Cloudlet(FogUtils.generateEntityId(), 1000, 1, 100, 100, null, null, null);
//				Vm vm = new Vm(vmid+i, broker.getId(), mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//				c.setVmId(vmid+i);
//				cloudlets.add(c);
//				vmlist.add(vm);
//			}
//			broker.submitCloudletList(cloudlets);
//			List<Cloudlet> newList = broker.getCloudletReceivedList();
//			broker.submitVmList(vmlist);
			//printCloudletList(newList);
            
			ModuleMapping moduleMapping = ModuleMapping.createModuleMapping(); // initializing a module mapping
//			moduleMapping.addModuleToDevice("client", "node1"); // fixing all instances of the Connector module to the Cloud
//			moduleMapping.addModuleToDevice("bus_stop", "node1"); // fixing all instances of the Connector module to the Cloud
//			moduleMapping.addModuleToDevice("concentration_calculator", "node1"); // fixing all instances of the Connector module to the Cloud
//			moduleMapping.addModuleToDevice("connector", "node1"); // fixing all instances of the Connector module to the Cloud
			
			Controller controller = new Controller("master-controller", fogDevices, sensors, actuators);
//			controller.submitApplication(application, 0, new ModulePlacementEdgewards(fogDevices, sensors, actuators, application, moduleMapping));
			controller.submitApplication(application, 0, new ModulePlacementOnlyCloud(fogDevices, sensors, actuators, application));
			
			TimeKeeper.getInstance().setSimulationStartTime(Calendar.getInstance().getTimeInMillis());
			System.out.println(moduleMapping.getModuleMapping());
			CloudSim.startSimulation();
			
			//CloudSim.stopSimulation();
			//System.out.println("VRGame finished!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}
	
	private static void testOut() {
		FileWriter file_writer;
        try {
            file_writer = new FileWriter("blob.txt");
//            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
         //   buffered_Writer.write(line);
            file_writer.write("whatever Im writing");
            file_writer.close();
//            buffered_Writer.flush();
//            buffered_Writer.close();
        } catch (IOException e) {
            System.out.println("Add line failed!" +e);
        }
	}
		
	private static void parseLinkObject(JSONObject link) {
		String srcID = (String) link.get("srcID");
		String dstID = (String) link.get("dstID");
		FogDevice src = null;
		FogDevice dst = null;
		double latency = (double) (link.get("latency"));
		//double bw = (double) link.get("bw");

		for(FogDevice device : fogDevices) {
			if (device.getName()==srcID) src=device;
			if (device.getName()==dstID) dst=device;
		}
		if(!(src==null || dst==null)) {
			src.setParentId(dst.getId());
			src.setUplinkLatency(latency);
		}
	}

	private static void parseNodeObject(JSONObject node, int userId, String appId, int cloudid) {
        double nodeBusyPower = (double) node.get("apower");
        int nodeLevel = Integer.parseUnsignedInt(node.get("level").toString());
        double nodeRatePerMips = (double) node.get("rate");
        double nodeIdlePower = (double) node.get("ipower");
        String nodeID = (String) node.get("name");
        long nodeDownBw = Long.parseUnsignedLong(node.get("down_bw").toString());
        long nodeUpBw = Long.parseUnsignedLong(node.get("up_bw").toString());
        long nodeMips = (long) node.get("mips");
        int nodeRam = Integer.parseUnsignedInt(node.get("ram").toString());
        
		FogDevice mobile = addMobile(userId, appId, cloudid, nodeID, nodeMips, nodeRam, nodeUpBw, nodeDownBw, nodeLevel, nodeRatePerMips, nodeBusyPower, nodeIdlePower); // adding a fog device for every Gateway in physical topology. The parent of each gateway is the Proxy Server
		mobile.setUplinkLatency(2); // latency of connection between the smartphone and proxy server is 4 ms
		fogDevices.add(mobile);
    }
	
	private static void parseModuleObject(JSONObject module, Application application) {
		String name = (String) module.get("name");
		int ram = Integer.parseUnsignedInt(module.get("ram").toString());
		int mips = Integer.parseUnsignedInt(module.get("mips").toString());
		long size = Long.parseUnsignedLong(module.get("size").toString());
		long bw = Long.parseUnsignedLong(module.get("bw").toString());
		
		application.addAppModule(name, ram, mips, size, bw);
		
		JSONArray tuplemap = (JSONArray) module.get("TupleMaps");
		tuplemap.forEach(n -> parseTupleMapping((JSONObject) n, application, name));
		
	}
	
	private static void parseTupleMapping(JSONObject tuplemaps, Application application, String name) {
		String inTuple = (String) tuplemaps.get("inTuple");
		String outTuple = (String) tuplemaps.get("outTuple");
		double fractionalSensitivity = (double) tuplemaps.get("fractionalSensitivity");
		
		application.addTupleMapping(name, inTuple, outTuple, new FractionalSelectivity(fractionalSensitivity));
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
		
		if(edgeType == 1) {
			application.addAppEdge("EEG", dest, periodicity, tupleCpuLength, tupleNwLength, tupleType, direction, edgeType);
		}
		else if(edgeType == 2) {
			application.addAppEdge(src, "DISPLAY", periodicity, tupleCpuLength, tupleNwLength, tupleType, direction, edgeType);
		}
		else {
			application.addAppEdge(src, dest, periodicity, tupleCpuLength, tupleNwLength, tupleType, direction, edgeType);
		}
	}
	
	private static FogDevice addMobile(int userId, String appId, int parentId, String nodeName, long nodeMips, int nodeRam, long nodeUpBw, long nodeDownBw, int nodeLevel, double nodeRatePerMips, double nodeBusyPower, double nodeIdlePower){
		FogDevice mobile = createFogDevice(nodeName, nodeMips, nodeRam, nodeUpBw, nodeDownBw, nodeLevel, nodeRatePerMips, nodeBusyPower, nodeIdlePower);
		mobile.setParentId(parentId);
		Sensor newSensor = new Sensor("s-"+nodeName, "EEG", userId, appId, new DeterministicDistribution(EEG_TRANSMISSION_TIME)); // inter-transmission time of EEG sensor follows a deterministic distribution
		sensors.add(newSensor);
		Actuator display = new Actuator("a-"+nodeName, userId, appId, "DISPLAY");
		actuators.add(display);
		newSensor.setGatewayDeviceId(mobile.getId());
		newSensor.setLatency(6.0);  // latency of connection between EEG sensors and the parent Smartphone is 6 ms
		display.setGatewayDeviceId(mobile.getId());
		display.setLatency(1.0);  // latency of connection between Display actuator and the parent Smartphone is 1 ms
		return mobile;
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
		double cost = ratePerMips; // the cost of using processing in this resource
		double costPerMem = 5000; // the cost of using memory in this resource
		double costPerStorage = 1000; // the cost of using storage in this resource
		double costPerBw = cost; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN devices by now

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
}