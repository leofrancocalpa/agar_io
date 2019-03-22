package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = new BorderPane();
			
			Parent root = FXMLLoader.load(getClass().getResource("fxml\\login.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("..\\application\\application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
