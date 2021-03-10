package org.fog.placement;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.fog.application.AppEdge;
import org.fog.application.AppLoop;
import org.fog.application.AppModule;
import org.fog.application.Application;
import org.fog.entities.Actuator;
import org.fog.entities.FogDevice;
import org.fog.entities.Sensor;
import org.fog.test.perfeval.TextParser;
import org.fog.utils.Config;
import org.fog.utils.FogEvents;
import org.fog.utils.FogUtils;
import org.fog.utils.NetworkUsageMonitor;
import org.fog.utils.TimeKeeper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Controller extends SimEntity{
	
	double texec=0;
	double totalLoopTime;
	static int counter =0;
		
	public static boolean ONLY_CLOUD = false;
		
	private List<FogDevice> fogDevices;
	private List<Sensor> sensors;
	private List<Actuator> actuators;
	
	private double totalNodeCost;
	
	private Map<String, Application> applications;
	private Map<String, Integer> appLaunchDelays;

	private Map<String, ModulePlacement> appModulePlacementPolicy;
	
	public Controller(String name, List<FogDevice> fogDevices, List<Sensor> sensors, List<Actuator> actuators) {
		super(name);
		this.applications = new HashMap<String, Application>();
		setAppLaunchDelays(new HashMap<String, Integer>());
		setAppModulePlacementPolicy(new HashMap<String, ModulePlacement>());
		for(FogDevice fogDevice : fogDevices){
			fogDevice.setControllerId(getId());
		}
		setFogDevices(fogDevices);
		setActuators(actuators);
		setSensors(sensors);
		connectWithLatencies();
	}

	private FogDevice getFogDeviceById(int id){
		for(FogDevice fogDevice : getFogDevices()){
			if(id==fogDevice.getId())
				return fogDevice;
		}
		return null;
	}
	
	private void connectWithLatencies(){
		for(FogDevice fogDevice : getFogDevices()){
			FogDevice parent = getFogDeviceById(fogDevice.getParentId());
			if(parent == null)
				continue;
			double latency = fogDevice.getUplinkLatency();
			parent.getChildToLatencyMap().put(fogDevice.getId(), latency);
			parent.getChildrenIds().add(fogDevice.getId());
		}
	}
	
	@Override
	public void startEntity() {
		for(String appId : applications.keySet()){
			if(getAppLaunchDelays().get(appId)==0)
				processAppSubmit(applications.get(appId));
			else
				send(getId(), getAppLaunchDelays().get(appId), FogEvents.APP_SUBMIT, applications.get(appId));
		}

		send(getId(), Config.RESOURCE_MANAGE_INTERVAL, FogEvents.CONTROLLER_RESOURCE_MANAGE);
		
		send(getId(), Config.MAX_SIMULATION_TIME, FogEvents.STOP_SIMULATION);
		
		for(FogDevice dev : getFogDevices()) {
			System.out.println("Controller.java: Sending res call to : " + dev.getName());
			sendNow(dev.getId(), FogEvents.RESOURCE_MGMT);
			}

	}

	@Override
	public void processEvent(SimEvent ev) {
		switch(ev.getTag()){
		case FogEvents.APP_SUBMIT:
			processAppSubmit(ev);
			break;
		case FogEvents.TUPLE_FINISHED:
			processTupleFinished(ev);
			break;
		case FogEvents.CONTROLLER_RESOURCE_MANAGE:
			manageResources();
			break;
		case FogEvents.STOP_SIMULATION:
			CloudSim.stopSimulation();
			emptyMethod();
			printTimeDetails();
			printPowerDetails();
			printCostDetails();
			printNetworkUsageDetails();
			printNodeCosts();
			CloudSim.stopSimulation();	
			CloudSim.terminateSimulation();	
//			System.exit(0);
			break;
			
		}
	}
	private void emptyMethod() {
	String emptyLine = "";
	FileWriter file_writer;
    try {
        file_writer = new FileWriter("consolefile.txt",false);
        BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
        buffered_Writer.write(emptyLine);
        buffered_Writer.flush();
        buffered_Writer.close();
    } catch (IOException e) {
        System.out.println("Add line failed!" +e);
    }
	
	}
	private void printNetworkUsageDetails() {
		System.out.println("Total network usage = "+NetworkUsageMonitor.getNetworkUsage()/Config.MAX_SIMULATION_TIME + "\n");		
		String NetworkUsageLine = NetworkUsageMonitor.getNetworkUsage()/Config.MAX_SIMULATION_TIME + " ";
		FileWriter file_writer;
        try {
            file_writer = new FileWriter("consolefile.txt",true);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
            buffered_Writer.write(NetworkUsageLine);
            buffered_Writer.flush();
            buffered_Writer.close();
        } catch (IOException e) {
            System.out.println("Add line failed!" +e);
        }
	}

	private FogDevice getCloud(){
		for(FogDevice dev : getFogDevices())
			if(dev.getName().equals("cloud"))
				return dev;
		return null;
	}
	
	//Print out the costs of the nodes	
	private void printNodeCosts() {
		double totalNodeCosts = 0;
		for(FogDevice fogDevice : getFogDevices()) {
			if(!fogDevice.getName().equals("cloud")) {
				totalNodeCosts = totalNodeCosts + fogDevice.getTotalCost();
				System.out.println(fogDevice.getName() + " : Cost = " + fogDevice.getTotalCost());
				String FogDeviceLine = fogDevice.getName() + " " + fogDevice.getTotalCost() + " ";
				FileWriter file_writer;
		        try {
		            file_writer = new FileWriter("consolefile.txt",true);
		            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
		            buffered_Writer.write(FogDeviceLine);
		            buffered_Writer.flush();
		            buffered_Writer.close();
		        } catch (IOException e) {
		            System.out.println("Add line failed!" +e);
		        }
			}
		}
		System.out.println("Total cost of execution in the nodes = "+ totalNodeCosts);
		String TotalNodeCostLine = totalNodeCosts + " ";
		FileWriter file_writer;
        try {
            file_writer = new FileWriter("consolefile.txt",true);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
            buffered_Writer.write(TotalNodeCostLine);
            buffered_Writer.flush();
            buffered_Writer.close();
        } catch (IOException e) {
            System.out.println("Add line failed!" +e);
        }
        try {
			TextParser();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	private void printCostDetails(){
		//System.out.println("Cost of execution in cloud = "+getCloud().getTotalCost());
	}
	
	private void printPowerDetails() {
		double totalNodePower = 0;
		for(FogDevice fogDevice : getFogDevices()){
			if(!fogDevice.getName().equals("cloud")) {
				totalNodePower = totalNodePower + fogDevice.getEnergyConsumption();
				System.out.println(fogDevice.getName() + " : Energy Consumed = "+fogDevice.getEnergyConsumption());
				String NodePowerLine = fogDevice.getName() + " " + fogDevice.getEnergyConsumption() + " ";
				FileWriter file_writer;
		        try {
		            file_writer = new FileWriter("consolefile.txt",true);
		            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
		            buffered_Writer.write(NodePowerLine);
		            buffered_Writer.flush();
		            buffered_Writer.close();
		        } catch (IOException e) {
		            System.out.println("Add line failed!" +e);
		        }
				
			}
		}
		System.out.println("Total energy usage in the nodes = "+ totalNodePower+"\n");
		String TotalNodePowerLine = totalNodePower + " ";
		FileWriter file_writer;
        try {
            file_writer = new FileWriter("consolefile.txt",true);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
            buffered_Writer.write(TotalNodePowerLine);
            buffered_Writer.flush();
            buffered_Writer.close();
        } catch (IOException e) {
            System.out.println("Add line failed!" +e);
        }
	}

	private String getStringForLoopId(int loopId){
		for(String appId : getApplications().keySet()){
			Application app = getApplications().get(appId);
			for(AppLoop loop : app.getLoops()){
				if(loop.getLoopId() == loopId)
					return loop.getModules().toString();
			}
		}
		return null;
	}
	private void printTimeDetails() {
		System.out.println("=========================================");
		System.out.println("============== RESULTS ==================");
		System.out.println("=========================================");
		System.out.println("EXECUTION TIME : "+ (Calendar.getInstance().getTimeInMillis() - TimeKeeper.getInstance().getSimulationStartTime()));
		System.out.println("=========================================");
		System.out.println("APPLICATION LOOP DELAYS");
		System.out.println("=========================================");
		texec = (Calendar.getInstance().getTimeInMillis() - TimeKeeper.getInstance().getSimulationStartTime());
		double totalLoopTime=0;
		double totalLoopTimeCTT = 0;
		for(Integer loopId : TimeKeeper.getInstance().getLoopIdToTupleIds().keySet()){
			/*double average = 0, count = 0;
			for(int tupleId : TimeKeeper.getInstance().getLoopIdToTupleIds().get(loopId)){
				Double startTime = 	TimeKeeper.getInstance().getEmitTimes().get(tupleId);
				Double endTime = 	TimeKeeper.getInstance().getEndTimes().get(tupleId);
				if(startTime == null || endTime == null)
					break;
				average += endTime-startTime;
				count += 1;
			}
			System.out.println(getStringForLoopId(loopId) + " ---> "+(average/count));*/
			System.out.println(getStringForLoopId(loopId) + " ---> "+TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId));
			totalLoopTime+=TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId);
			totalLoopTimeCTT = TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId);
		}
		System.out.println("Calculated total time: " + totalLoopTime);
		System.out.println("=========================================");
		System.out.println("TUPLE CPU EXECUTION DELAY");
		System.out.println("=========================================");
		
		for(String tupleType : TimeKeeper.getInstance().getTupleTypeToAverageCpuTime().keySet()){
			System.out.println(tupleType + " ---> "+TimeKeeper.getInstance().getTupleTypeToAverageCpuTime().get(tupleType));
			totalLoopTime+=TimeKeeper.getInstance().getTupleTypeToAverageCpuTime().get(tupleType);
			String TupleTimeLine  = tupleType + " " + TimeKeeper.getInstance().getTupleTypeToAverageCpuTime().get(tupleType) + " ";
			FileWriter file_writer;
	        try {
	            file_writer = new FileWriter("consolefile.txt",true);
	            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
	            buffered_Writer.write(TupleTimeLine);
	            buffered_Writer.flush();
	            buffered_Writer.close();
	        } catch (IOException e) {
	            System.out.println("Add line failed!" +e);
	        }
		}

		System.out.println("Calculated total time with delays: " + totalLoopTime);
		System.out.println("=========================================");
		
		String TimeLine = texec + " " + totalLoopTimeCTT + " " + totalLoopTime + " ";
		FileWriter file_writer;
        try {
            file_writer = new FileWriter("consolefile.txt",true);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
            buffered_Writer.write(TimeLine);
            buffered_Writer.flush();
            buffered_Writer.close();
        } catch (IOException e) {
            System.out.println("Add line failed!" +e);
        }
	}

	protected void manageResources(){
		send(getId(), Config.RESOURCE_MANAGE_INTERVAL, FogEvents.CONTROLLER_RESOURCE_MANAGE);
//		System.out.println("Controller.java: Counter " + counter++);
	}
	
	private void processTupleFinished(SimEvent ev) {
	}
	
	@Override
	public void shutdownEntity() {	
	}
	
	public void submitApplication(Application application, int delay, ModulePlacement modulePlacement){
		FogUtils.appIdToGeoCoverageMap.put(application.getAppId(), application.getGeoCoverage());
		getApplications().put(application.getAppId(), application);
		getAppLaunchDelays().put(application.getAppId(), delay);
		getAppModulePlacementPolicy().put(application.getAppId(), modulePlacement);
		
		for(Sensor sensor : sensors){
			sensor.setApp(getApplications().get(sensor.getAppId()));
		}
		for(Actuator ac : actuators){
			ac.setApp(getApplications().get(ac.getAppId()));
		}
		
		for(AppEdge edge : application.getEdges()){
			if(edge.getEdgeType() == AppEdge.ACTUATOR){
				String moduleName = edge.getSource();
				for(Actuator actuator : getActuators()){
					if(actuator.getActuatorType().equalsIgnoreCase(edge.getDestination()))
						application.getModuleByName(moduleName).subscribeActuator(actuator.getId(), edge.getTupleType());
				}
			}
		}	
	}
	
	public void submitApplication(Application application, ModulePlacement modulePlacement){
		submitApplication(application, 0, modulePlacement);
	}
	
	
	private void processAppSubmit(SimEvent ev){
		Application app = (Application) ev.getData();
		processAppSubmit(app);
	}
	
	private void processAppSubmit(Application application){
		System.out.println(CloudSim.clock()+" Submitted application "+ application.getAppId());
		FogUtils.appIdToGeoCoverageMap.put(application.getAppId(), application.getGeoCoverage());
		getApplications().put(application.getAppId(), application);
		
		ModulePlacement modulePlacement = getAppModulePlacementPolicy().get(application.getAppId());
		for(FogDevice fogDevice : fogDevices){
			sendNow(fogDevice.getId(), FogEvents.ACTIVE_APP_UPDATE, application);
		}
		
		Map<Integer, List<AppModule>> deviceToModuleMap = modulePlacement.getDeviceToModuleMap();
		for(Integer deviceId : deviceToModuleMap.keySet()){
			for(AppModule module : deviceToModuleMap.get(deviceId)){
				sendNow(deviceId, FogEvents.APP_SUBMIT, application);
				sendNow(deviceId, FogEvents.LAUNCH_MODULE, module);
			}
		}
	}
	

	public List<FogDevice> getFogDevices() {
		return fogDevices;
	}

	public void setFogDevices(List<FogDevice> fogDevices) {
		this.fogDevices = fogDevices;
	}

	public Map<String, Integer> getAppLaunchDelays() {
		return appLaunchDelays;
	}

	public void setAppLaunchDelays(Map<String, Integer> appLaunchDelays) {
		this.appLaunchDelays = appLaunchDelays;
	}

	public Map<String, Application> getApplications() {
		return applications;
	}

	public void setApplications(Map<String, Application> applications) {
		this.applications = applications;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		for(Sensor sensor : sensors)
			sensor.setControllerId(getId());
		this.sensors = sensors;
	}

	public List<Actuator> getActuators() {
		return actuators;
	}

	public void setActuators(List<Actuator> actuators) {
		this.actuators = actuators;
	}

	public Map<String, ModulePlacement> getAppModulePlacementPolicy() {
		return appModulePlacementPolicy;
	}

	public void setAppModulePlacementPolicy(Map<String, ModulePlacement> appModulePlacementPolicy) {
		this.appModulePlacementPolicy = appModulePlacementPolicy;
	}
	
	
	public void TextParser() throws Exception, IOException {
			String sourceFileName = "consolefile.txt";
			String jsonFileName = "output.json";			
	    	TextParser textfile = new TextParser();
	    		textfile.getInput(sourceFileName);
	    		textfile.writeJSON(jsonFileName);
	    }
}