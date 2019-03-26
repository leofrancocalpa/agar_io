package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import server.ClientListener;

public class SignInController {

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_paasword;

    @FXML
    private Button btn_singin;

    @FXML
    private TextField txt_nickname;

    private Client client;

    private LoginController logInC;
    public void putClient(Client pClient) {
    	client = pClient;
	}

    public void putLoginController(LoginController pLoginC){
    	logInC = pLoginC;
    }
    @FXML
    void sign_In(ActionEvent event) {
    	client.sendServer(txt_email.getText() + " " + txt_nickname.getText() + " " + txt_paasword.getText());
    	Alert dialog;
		dialog = new Alert(AlertType.INFORMATION);
		dialog.setTitle("Register complete");
		dialog.setContentText(ClientListener.REGISTER_SUCCESS);
		dialog.show();
    }

}
