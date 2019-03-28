package server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;

public class ServerStController {

	@FXML
	private Button butStart;

	@FXML
	private TitledPane matchTitledPane;

	@FXML
	private TitledPane usersTitledPane;

	@FXML
	private Label time;

	@FXML
	private Label title;

	@FXML
	private TableView usersTable;

	@FXML
	private TableView matchTable;

	private Server server;

	@FXML
	void startServer (ActionEvent event) {
		server.startSSL();
	}
	
	void putServer(Server server) {
		this.server = server;
	}
}
