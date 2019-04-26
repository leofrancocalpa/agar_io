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
		if(butStart.getText().equals("Start Server")) {
		Thread receptPlayers = new Thread(new Runnable() {
			
			public void run() {
				server.startSSL();
				server.startMatchConnection();
				
			}
		});
		receptPlayers.start();
		butStart.setText("Stop Server");
		new Thread(new Runnable() {
			int min = Integer.parseInt(minutes.getText());
			int sec = Integer.parseInt(seconds.getText());
			
			@SuppressWarnings("restriction")
			public void run() {
				try {
				Thread.sleep(1000);
				while(server.isReceiving()){
					 Platform.runLater(new Runnable() {
				            public void run() {
				            	if(sec==00){
									if(min==00) {
										minutes.setText("02");
										seconds.setText("00");
										server.timeOut();
									}
									else {
										min--;
										sec = 59;
									minutes.setText("0"+min);
									seconds.setText(sec+"");
									}
								} else {
									sec--;
									if(sec<10)
									seconds.setText("0"+sec);
									else seconds.setText(sec+"");
								}
				            }
				        });
					 Thread.sleep(1000);
		}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		} else {
			server.timeOut();
			minutes.setText("02");
			seconds.setText("00");
			butStart.setText("Start Server");
		}
	}
	
	void putServer(Server server) {
		this.server = server;
	}
}
