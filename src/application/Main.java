package application;

import javafx.beans.value.ChangeListener;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.fog.test.perfeval.VRGameFog_src;

public class Main extends Application {
	public static void main(String[] args) throws Exception {
		System.out.println("Starting...");
//		new VRGameFog_src("test6.json");
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("_MainWindow.fxml"));
			BorderPane root = (BorderPane)loader.load();
			Scene scene = new Scene(root,900,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			_MainWindowController controller = (_MainWindowController)loader.getController();
			controller.setupListeners(primaryStage, scene);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Policy Placement Application");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop(){
//	    System.out.println("Default JSON Design File Self-Destructing...");
	    FileWriter file_writer;
        try {
            file_writer = new FileWriter("default.json",false);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);        
            buffered_Writer.flush();
            buffered_Writer.close();
        } catch (IOException e) {
            System.out.println("Overwrite Null failed"+e);
        }
	}
}
