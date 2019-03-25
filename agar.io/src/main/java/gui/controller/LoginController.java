package gui.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    }

}
