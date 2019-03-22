package gui.controller;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoginController {
	
    @FXML
    private Button btn_play;

    @FXML
    private Button btn_singin;
    
    @FXML
    void login(ActionEvent event) {
    	System.out.println("clicked");

    }

}
