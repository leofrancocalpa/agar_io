package client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.ClientListener;

	public class MainClient extends Application {
		@Override
		public void start(Stage primaryStage) {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		public static void main(String[] args) {
			launch(args);
		}

}
