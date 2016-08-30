package tools.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GetDot extends Application{

	public  static void main(String[] args){
		Application.launch(args);
		
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(GetDot.class.getResourceAsStream("Common.fxml"));
        
        Scene scene = new Scene(root);        
        scene.getStylesheets().add(GetDot.class.getResource("main.css").toExternalForm());
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
}
