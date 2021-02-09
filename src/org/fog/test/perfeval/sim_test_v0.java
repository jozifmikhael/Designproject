package org.fog.test.perfeval;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

//Added by us
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.fog.entities.FogDevice;

/**
 * A simple example showing how to create a data center with one host and run one cloudlet on it.
 */
public class sim_test_v0 {
	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;
	/** The vmlist. */
	private static List<Vm> vmlist;
	
	static String sourceFile="test6.json";

	private static List<FogDevice> fogDevices;
	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Log.printLine("Starting CloudSimExample1...");

		try {
			// First step: Initialize the CloudSim package. It should be called before creating any entities.
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance(); // Calendar whose fields have been initialized with the current date and time.
 			boolean trace_flag = false; // trace events
 			
 			int pesNumber = 1;
 	        String vmm = "Xen";
 	        int vmid = 1;
			 
			CloudSim.init(num_user, calendar, trace_flag);

			

			// Third step: Create Broker
			String appId = "vr_game"; // identifier of the application
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();
			
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(sourceFile);
            Object obj = jsonParser.parse(reader);
            JSONObject nodeList = (JSONObject) obj;
            JSONArray nodeArr = (JSONArray) nodeList.get("nodes");
            nodeArr.forEach(n -> parseNodeObject( (JSONObject) n, broker.getId(), appId));
            JSONArray linkArr = (JSONArray) nodeList.get("links");
            linkArr.forEach(l -> parseLinkObject((JSONObject) l));


			// submit vm list to the broker
			broker.submitVmList(vmlist);

			// Fifth step: Create one Cloudlet
			cloudletList = new ArrayList<Cloudlet>();

			// Cloudlet properties
			int id = 0;
			long length = 400000;
			long fileSize = 300;
			long outputSize = 300;
			UtilizationModel utilizationModel = new UtilizationModelFull();

			Cloudlet cloudlet = 
                                new Cloudlet(id, length, pesNumber, fileSize, 
                                        outputSize, utilizationModel, utilizationModel, 
                                        utilizationModel);
			cloudlet.setUserId(brokerId);
			cloudlet.setVmId(vmid);

			// add the cloudlet to the list
			cloudletList.add(cloudlet);

			// submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);

			// Sixth step: Starts the simulation
			CloudSim.startSimulation();

			CloudSim.stopSimulation();

			//Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			printCloudletList(newList);

			Log.printLine("CloudSimExample1 finished!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}
	
	private static void parseNodeObject(JSONObject node, int userId, String appId) {
        double nodeBusyPower = (double) node.get("apower");
        int nodeLevel = Integer.parseUnsignedInt(node.get("level").toString());
        double nodeRatePerMips = (double) node.get("rate");
        double nodeIdlePower = (double) node.get("ipower");
        String nodeID = (String) node.get("name");
        long nodeDownBw = Long.parseUnsignedLong(node.get("down_bw").toString());
        long nodeUpBw = Long.parseUnsignedLong(node.get("up_bw").toString());
        long nodeMips = (long) node.get("mips");
        int nodeRam = Integer.parseUnsignedInt(node.get("ram").toString());
        int pesNumber = 1;
        String vmm = "Xen";
        
        Datacenter datacenter0 = createDatacenter(nodeID, nodeMips, nodeRam, nodeUpBw, nodeDownBw, nodeLevel, nodeRatePerMips, nodeBusyPower, nodeIdlePower);
        fogDevices.add((FogDevice)datacenter0);
        
        // create VM
		Vm vm = new Vm(1, userId, nodeMips, pesNumber, nodeRam, nodeUpBw, 10000, vmm, new CloudletSchedulerTimeShared());

		// add the VM to the vmList
		vmlist.add(vm);
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

	/**
	 * Creates the datacenter.
	 *
	 * @param name the name
	 *
	 * @return the datacenter
	 */
	private static Datacenter createDatacenter(String nodeName, long mips,
			int ram, long upBw, long downBw, int level, double ratePerMips, double busyPower, double idlePower) {

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();


		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

		// 4. Create Host with its id and list of PEs and add them to the list
		// of machines
		int hostId = 0;
		long storage = 1000000; // host storage
		int bw = 10000;

		hostList.add(
			new Host(
				hostId,
				new RamProvisionerSimple(ram),
				new BwProvisionerSimple(bw),
				storage,
				peList,
				new VmSchedulerTimeShared(peList)
			)
		); // This is our machine

		// 5. Create a DatacenterCharacteristics object that stores the
		// properties of a data center: architecture, OS, list of
		// Machines, allocation policy: time- or space-shared, time zone
		// and its price (G$/Pe time unit).
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = ratePerMips; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = cost; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(nodeName, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	// We strongly encourage users to develop their own broker policies, to
	// submit vms and cloudlets according
	// to the specific rules of the simulated scenario
	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	private static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects.
	 *
	 * @param list list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");
				Log.printLine(indent + indent + cloudlet.getResourceId()
						+ indent + indent + indent + cloudlet.getVmId()
						+ indent + indent
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent
						+ dft.format(cloudlet.getFinishTime())
						+ indent + cloudlet.getActualCPUTime()*cloudlet.getCostPerSec());
			}
		}
	}
}