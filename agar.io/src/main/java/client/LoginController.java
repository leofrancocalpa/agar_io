package client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gui.UserCanvas;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.SSLConnection;
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

	public void putClient(Client client) {
		this.client = client;
	}

    @FXML
    void login(ActionEvent event) {
    	stage = new Stage();
    	
    	client.sendToServer(txt_email.getText() + " " + txt_password.getText());
		int cont = 120;
		while (client.getStatus()!=Client.PLAYING && cont > 0) {
			try {
				Thread.sleep(1000);
				cont--;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		startGame();
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
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				public void handle(WindowEvent event) {
					// TODO Auto-generated method stub
					stage.close();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	void startGame() {
		UserCanvas canvas = new UserCanvas();
    		canvas.setClient(client);
    		canvas.start();
        	Scene scene = new Scene(canvas);
        	stage.setScene(scene);
        	stage.setFullScreen(true);
        	stage.show();
	}

	void closeSigninWindow() {
		stage.hide();
	}

	public void showMessage(String line) {

		if (line.equals(SSLConnection.REGISTER_SUCCESS)) {
			JOptionPane.showMessageDialog(new JFrame(), SSLConnection.REGISTER_SUCCESS, "Register complete",
					JOptionPane.INFORMATION_MESSAGE);
//		closeSigninWindow();
		} else if (line.equals(SSLConnection.SESSION_FAILED)) {
			JOptionPane.showMessageDialog(new JFrame(), SSLConnection.SESSION_FAILED, "Session failed",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
