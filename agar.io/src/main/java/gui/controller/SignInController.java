package gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SignInController {

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_paasword;

    @FXML
    private Button btn_singin;

    @FXML
    private TextField txt_nickname;

    @FXML
    void sign_In(ActionEvent event) {
    	System.out.println("clicked sign In");

    }

}
