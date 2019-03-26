package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import server.ClientListener;

	public class MainClient extends Application {
		@Override
		public void start(Stage primaryStage) {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("../client/login.fxml"));
				Scene scene = new Scene(root);
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
