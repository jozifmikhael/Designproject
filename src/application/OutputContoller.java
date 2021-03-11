package application;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.MenuItem;

public class OutputContoller {

    @FXML
    private StackedAreaChart areaChart;
    
	@FXML
	void EnergyConsuptionGraph() {
		System.out.println("Called");
		final NumberAxis xAxis = new NumberAxis();
	    final NumberAxis yAxis = new NumberAxis();
//		StackedAreaChart<String, Number> areaChart = new StackedAreaChart(xAxis, yAxis);
        xAxis.setLabel("Time");
        yAxis.setLabel("Energy Consumption");
        areaChart.setTitle("Time vs Energy Consumption");
        XYChart.Series series1 = new XYChart.Series();  
        series1.setName("Asia"); 
        series1.getData().add(new XYChart.Data(1750, 502)); 
        series1.getData().add(new XYChart.Data(1800, 635)); 
        series1.getData().add(new XYChart.Data(1850, 809)); 
        series1.getData().add(new XYChart.Data(1900, 947)); 
        series1.getData().add(new XYChart.Data(1950, 1402)); 
        series1.getData().add(new XYChart.Data(1999, 3634)); 
        series1.getData().add(new XYChart.Data(2050, 5268));  

        XYChart.Series series2 = new XYChart.Series();  
        series2.setName("Africa"); 
        series2.getData().add(new XYChart.Data(1750, 106)); 
        series2.getData().add(new XYChart.Data(1800, 107)); 
        series2.getData().add(new XYChart.Data(1850, 111)); 
        series2.getData().add(new XYChart.Data(1900, 133)); 
        series2.getData().add(new XYChart.Data(1950, 221)); 
        series2.getData().add(new XYChart.Data(1999, 767)); 
        series2.getData().add(new XYChart.Data(2050, 1766));       

        XYChart.Series series3 = new XYChart.Series();  
        series3.setName("Europe"); 
        series3.getData().add(new XYChart.Data(1750, 163)); 
        series3.getData().add(new XYChart.Data(1800, 203)); 
        series3.getData().add(new XYChart.Data(1850, 276)); 
        series3.getData().add(new XYChart.Data(1900, 408)); 
        series3.getData().add(new XYChart.Data(1950, 547)); 
        series3.getData().add(new XYChart.Data(1999, 729)); 
        series3.getData().add(new XYChart.Data(2050, 628));  
        
        areaChart.getData().addAll(series1, series2, series3); 
        
//        for(Energy energylist : Energylist) {
//        	XYChart.Series temp = new XYChart.Series();
//        	temp.setName(energylist.name);
//        	for(int i= 0; i<energylist.timeList.size(); i++) {
//        		temp.getData().add(new XYChart.Data(energylist.timeList.get(i),energylist.EnergyConsumptionList.get(i)));
//        	}
//        	seriesList.add(temp);
//        }
//        for(int i= 0; i<seriesList.size(); i++) {
//        	 areaChart.getData().add(seriesList.get(i));
//        }
        
	}
}
