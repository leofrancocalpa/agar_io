package client;


import gui.UserCanvas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import server.Server;

public class LoginController {

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_password;

    @FXML
    private Button btn_play;

    @FXML
    private Button btn_singin;

    private Client client;

    SignInController signinC;

    public LoginController() {
    	client = new Client();
    	//client.start();
	}
    @FXML
    void login(ActionEvent event) {
    	//client.sendServer(txt_email.getText() + " " + txt_password.getText());

    	FXMLLoader loader = new FXMLLoader();
    	Stage alv = new Stage();
    	try {
			//AnchorPane root = loader.load(getClass().getResource("canvas.fxml").openStream());
			UserCanvas canvas = new UserCanvas();
			canvas.start();
			Scene scene = new Scene(canvas, 500, 500);
			alv.setScene(scene);
			alv.setFullScreen(true);
			alv.show();
		} catch(Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }

    @FXML
    void sign_In(ActionEvent event) {
    	FXMLLoader loader = new FXMLLoader();
    	Stage alv = new Stage();
    	try {
			AnchorPane root = loader.load(getClass().getResource("signin.fxml").openStream());

			signinC = loader.getController();
			signinC.putClient(client);
			Scene scene = new Scene(root);
			alv.setScene(scene);
			alv.show();
		} catch(Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }

    void closeSigninWindow(){

    }
}
