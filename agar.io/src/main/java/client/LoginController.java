package client;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import server.ClientListener;
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

    private Stage stage;

    public LoginController() {
    	client = new Client();
    	try {
			client.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	client.putLogInC(this);
	}
    @FXML
    void login(ActionEvent event) {
    	client.sendServer(txt_email.getText() + " " + txt_password.getText());
    }

    @FXML
    void sign_In(ActionEvent event) {
    	FXMLLoader loader = new FXMLLoader();
    	stage = new Stage();
    	try {
			AnchorPane root = loader.load(getClass().getResource("signin.fxml").openStream());

			SignInController signinC = loader.getController();
			signinC.putClient(client);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }

    void startGame() {
    	FXMLLoader loader = new FXMLLoader();
    	Stage alv = new Stage();
    	try {
			AnchorPane root = loader.load(getClass().getResource("canvas.fxml").openStream());
			Scene scene = new Scene(root);
			alv.setScene(scene);
			System.out.println("entra start game");
			alv.show();
		} catch(Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }

    void closeSigninWindow(){
    	stage.hide();
    }

	public void showMessage(String line) {

		if(line.equals(ClientListener.REGISTER_SUCCESS)){
			JOptionPane.showMessageDialog(new JFrame(), ClientListener.REGISTER_SUCCESS, "Register complete", JOptionPane.INFORMATION_MESSAGE);
//		closeSigninWindow();
		} else if(line.equals(ClientListener.SESSION_FAILED)){
			JOptionPane.showMessageDialog(new JFrame(), ClientListener.SESSION_FAILED, "Session failed", JOptionPane.ERROR_MESSAGE);
		} else {
			startGame();
		}
	}
}
