package server;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
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
	private Label minutes;
	
	@FXML
	private Label seconds;
	
	@FXML
	private Label title;

	@FXML
	private TableView usersTable;

	@FXML
	private TableView matchTable;

	private Server server;

	@FXML
	void startServer (ActionEvent event) {
		Thread receptPlayers = new Thread(new Runnable() {
			
			public void run() {
				server.startSSL();
				server.startMatchConnection();
				
			}
		});
		receptPlayers.start();
		new Thread(new Runnable() {
			int min = Integer.parseInt(minutes.getText());
			int sec = Integer.parseInt(seconds.getText());
			
			public void run() {
				while(true){
					 Platform.runLater(new Runnable() {
				            public void run() {
				            	if(sec==00){
									if(min==00)
										//cerrar ciclo
										;
									minutes.setText(min--+"");
									seconds.setText(60+"");
								} else {
									seconds.setText(sec--+"");
								}
				            }
				        });
					
					try {
						
						System.out.println("no cambia la pantalla");
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		}
			}
		});

		System.out.println("pasa por aquí");
		server.timeOut();
	}
	
	void putServer(Server server) {
		this.server = server;
	}
}
