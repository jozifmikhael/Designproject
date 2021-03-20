package org.fog.entities;

import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

public class Tuple extends Cloudlet{

	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int ACTUATOR = 3;
	
	private String appId;
	
	private String tupleType;
	private String destModuleName;
	private String srcModuleName;
	private int actualTupleId;
	private int direction;
	private int actuatorId;
	private int sourceDeviceId;
	private int sourceModuleId;
	private double sendTime;
	private double arrivalTime;
	
	/**
	 * Map to keep track of which module instances has a tuple traversed.
	 * 
	 * Map from moduleName to vmId of a module instance
	 */
	private Map<String, Integer> moduleCopyMap;
	
	public Tuple(String appId, int cloudletId, int direction, long cloudletLength, int pesNumber,
			long cloudletFileSize, long cloudletOutputSize,
			UtilizationModel utilizationModelCpu,
			UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelBw) {
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize,
				cloudletOutputSize, utilizationModelCpu, utilizationModelRam,
				utilizationModelBw);
		setAppId(appId);
		setDirection(direction);
		setSourceDeviceId(-1);
		setModuleCopyMap(new HashMap<String, Integer>());
	}

	public int getActualTupleId() {
		return actualTupleId;
	}

	public void setActualTupleId(int actualTupleId) {
		this.actualTupleId = actualTupleId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTupleType() {
		return tupleType;
	}

	public void setTupleType(String tupleType) {
		this.tupleType = tupleType;
	}

	public String getDestModuleName() {
		return destModuleName;
	}

	public void setDestModuleName(String destModuleName) {
		this.destModuleName = destModuleName;
	}

	public String getSrcModuleName() {
		return srcModuleName;
	}

	public void setSrcModuleName(String srcModuleName) {
		this.srcModuleName = srcModuleName;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getActuatorId() {
		return actuatorId;
	}

	public void setActuatorId(int actuatorId) {
		this.actuatorId = actuatorId;
	}

	public int getSourceDeviceId() {
//		System.out.println("tuple src id" + sourceDeviceId);
		return sourceDeviceId;
	}

	public void setSourceDeviceId(int sourceDeviceId) {
		this.sourceDeviceId = sourceDeviceId;
	}

	public Map<String, Integer> getModuleCopyMap() {
		return moduleCopyMap;
	}

	public void setModuleCopyMap(Map<String, Integer> moduleCopyMap) {
		this.moduleCopyMap = moduleCopyMap;
	}

	public int getSourceModuleId() {
		//System.out.println("src mod id" + sourceModuleId);
		return sourceModuleId;
	}

	public void setSourceModuleId(int sourceModuleId) {
		//System.out.println("src mod id is set to " + sourceModuleId);
		this.sourceModuleId = sourceModuleId;
	}
	
	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public double getArrivalTime() {
		return arrivalTime;
	}
	
	public void setSendTime(double sendTime) {
		this.sendTime = sendTime;
	}
	
	public double getsendTime() {
		return sendTime;
	}

}