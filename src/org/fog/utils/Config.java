//package org.fog.utils;
//
//public class Config {
//
//	public static final double RESOURCE_MGMT_INTERVAL = 10;
//	public static int MAX_SIMULATION_TIME = 10000;
//	public static int RESOURCE_MANAGE_INTERVAL = 10;
//	public static String FOG_DEVICE_ARCH = "x86";
//	public static String FOG_DEVICE_OS = "Linux";
//	public static String FOG_DEVICE_VMM = "Xen";
//	public static double FOG_DEVICE_TIMEZONE = 10.0;
//	public static double FOG_DEVICE_COST = 3.0;
//	public static double FOG_DEVICE_COST_PER_MEMORY = 0.05;
//	public static double FOG_DEVICE_COST_PER_STORAGE = 0.001;
//	public static double FOG_DEVICE_COST_PER_BW = 0.0;
//}

package org.fog.utils;

public class Config {
	
	static int igran = application.setParamsController.granularityMetric;
	static double dgran = igran;
	static int stime = application.setParamsController.simulationTime;
	static String s_topnode = application.AddDeviceController.hardcodedtopnode;

	public static final double RESOURCE_MGMT_INTERVAL = dgran; //1000.00; //dgran;
	public static int MAX_SIMULATION_TIME = stime; // application.setSimTimeController.simulationTime;
	public static int RESOURCE_MANAGE_INTERVAL = igran; //1000; //igran;
	public static String TOP_NODE = s_topnode;
	public static String FOG_DEVICE_ARCH = "x86";
	public static String FOG_DEVICE_OS = "Linux";
	public static String FOG_DEVICE_VMM = "Xen";
	public static double FOG_DEVICE_TIMEZONE = 10.0;
	public static double FOG_DEVICE_COST = 3.0;
	public static double FOG_DEVICE_COST_PER_MEMORY = 0.05;
	public static double FOG_DEVICE_COST_PER_STORAGE = 0.001;
	public static double FOG_DEVICE_COST_PER_BW = 0.0;
	
	
	void test() {
		System.out.println(RESOURCE_MGMT_INTERVAL);
		System.out.println(MAX_SIMULATION_TIME);
		System.out.println(RESOURCE_MANAGE_INTERVAL);
		System.out.println(igran);
		System.out.println(dgran);
		System.out.println(stime);

	}
		
}