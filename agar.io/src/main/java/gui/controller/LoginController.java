package gui.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_password;

    @FXML
    private Button btn_play;

    @FXML
    private Button btn_singin;

    @FXML
    void login(ActionEvent event) {
    	System.out.println("clicked play");

    }

    @FXML
    void sign_In(ActionEvent event) {
    	FXMLLoader loader = new FXMLLoader();
    	Stage alv = new Stage();
    	try {
			AnchorPane root = loader.load(getClass().getResource("signin.fxml").openStream());
			Scene scene = new Scene(root);
			alv.setScene(scene);
			alv.show();
		} catch(Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
}
