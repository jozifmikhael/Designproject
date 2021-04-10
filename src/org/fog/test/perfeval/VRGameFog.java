package org.fog.test.perfeval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import application._SpecHandler.*;
import application._SpecHandler;

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
	static int userId;
	static String appId;
	
	public void startVRGame(Application application, ModuleMapping moduleMapping, String placementPolicy) {
		System.out.println("Starting VRGame... ");		
		// initializing a module mapping
		Controller controller = new Controller("master-controller", fogDevices, sensors, actuators);
//		controller.submitApplication(application, 0,(new ModulePlacementMapping(fogDevices,application, moduleMapping)));
		if(placementPolicy.equals("Edgeward")) controller.submitApplication(application, 0,(new ModulePlacementEdgewards(fogDevices, sensors, actuators, application, moduleMapping)));
    	else if(placementPolicy.equals("Cloudward")) controller.submitApplication(application, 0,(new ModulePlacementOnlyCloud(fogDevices, sensors, actuators, application)));
		TimeKeeper.getInstance().setSimulationStartTime(Calendar.getInstance().getTimeInMillis());
		CloudSim.startSimulation();
		CloudSim.stopSimulation();
		Log.printLine("VRGame finished!");
	}
	//TODO put back sim params
	public void createFogSimObjects(boolean startSimulation, String placementPolicy) throws Exception {
		fogDevices.clear();
		actuators.clear();
		sensors.clear();
		_SpecHandler.placementList.clear();//now in ModulePlacement.java
		Log.disable();
		int num_user = 1; // number of cloud users
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false; // mean trace events
		CloudSim.init(num_user, calendar, trace_flag);
		appId = "vr_game"; // identifier of the application
		PowerDatacenterBroker broker = new FogBroker("broker");
        Application application = new Application(appId, broker.getId());
        userId = broker.getId();
        ModuleMapping moduleMapping = ModuleMapping.createModuleMapping();
		_SpecHandler.nodesList.stream().filter(n->n.type.equals("device")).forEach(n->fogDevices.add(((DeviceSpec)n).addToApp()));
        _SpecHandler.nodesList.stream().filter(n->n.type.equals("sensor")).forEach(n->sensors.add(((SensorSpec)n).addToApp(userId,appId,application)));
        _SpecHandler.nodesList.stream().filter(n->n.type.equals("actuat")).forEach(n->actuators.add(((ActuatSpec)n).addToApp(userId,appId)));
        _SpecHandler.nodesList.stream().filter(n->n.type.equals("module")).forEach(n->((ModuleSpec)n).addToApp(application));
        for(NodeSpec n : _SpecHandler.nodesList) for(EdgeSpec e : n.edgesList) e.addToApp(fogDevices, sensors, actuators, application, moduleMapping);
        if(startSimulation)startVRGame(application, moduleMapping, placementPolicy);
        else {
        	if(placementPolicy.equals("Edgeward")) new ModulePlacementEdgewards(fogDevices, sensors, actuators, application, moduleMapping);
        	else if(placementPolicy.equals("Cloudward")) new ModulePlacementOnlyCloud(fogDevices, sensors, actuators, application);
        }
	}
	
	public VRGameFog(){
		
	}
}