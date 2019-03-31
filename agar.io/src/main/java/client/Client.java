package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.SSLConnection;
import javafx.scene.Parent;

public class Client extends Application{
	public static final String TRUSTTORE_LOCATION = "C:/Users/99031510240/alv";
	public static PrintWriter writerC;
	private static LoginController logInC;
	
	private boolean sessionOnFire;
	
	private String[] enemies;
	private String[] player;
	private String[] food;
	
	public void sendToServer(String parameter) {
		writerC.println(parameter);
	}

	public Object getlogInC() {
		return logInC;
	}

	public void putLogInC(LoginController loginController) {
		logInC = loginController;

	}

	public void connectToServer() {
				final SSLSocket client;
				System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
				SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
				
				try {
					client = (SSLSocket) sf.createSocket("localhost", 8030);
					String[] supported = client.getSupportedCipherSuites();
					client.setEnabledCipherSuites(supported);
					writerC = new PrintWriter(client.getOutputStream(), true);
					// INicia hilo que lee desde el servidor
					
			Thread tServer = new Thread(new Runnable() {

				public void run() {
					BufferedReader readerC;
					try {
						readerC = new BufferedReader(new InputStreamReader(client.getInputStream()));
						while (client.isConnected()) {
							final String serverAnswer = readerC.readLine();
							logInC.showMessage(serverAnswer);
							if(serverAnswer.equals(SSLConnection.STARTING_MATCH)) {
								sessionOnFire = true;
								connectToGame();
							}
						}
					} catch (IOException e) {
						try {
							client.shutdownInput();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
			});
					
			tServer.start();
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
}
	
	public void connectToGame() {
		final Socket client;
		System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
		SocketFactory sf = SocketFactory.getDefault();
		
		try {
			client = sf.createSocket("localhost", 8040);
			writerC = new PrintWriter(client.getOutputStream(), true);
			// INicia hilo que lee desde el servidor
			
	Thread tServer = new Thread(new Runnable() {

		public void run() {
			BufferedReader readerC;
			try {
				readerC = new BufferedReader(new InputStreamReader(client.getInputStream()));
				while (client.isConnected()) {
					final String infoGame = readerC.readLine();
					System.out.println(infoGame);
//					aquí debería llenar la información del cliente sobre el juego
				}
			} catch (IOException e) {
				try {
					client.shutdownInput();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	});
			
	tServer.start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
}
	/**
	 * 
	 * @return  Array of strings that represent the position x,y and mass (width and height) of every food in the game
	 * Example : food[0] -> x,y,w,h
	 */
	public String[] getFoodFromGame() {
		return null;
	}
	/**
	 * 
	 * @return Array of strings that represent the position x,y and mass (width and height) of every player in the game
	 * Example : player[0] -> x,y,w,h,id
	 */
	public String[] getPlayerFromGame() {
		return null;
	}
	/**
	 * 
	 * @return Array of strings that represent the initial info of the user. Initial position, initial mass
	 * (width and height) and id
	 * infoPlayer[0]-> x , infoPlayer[1]-> y, infoPlayer[2]-> w, infoPlayer[3]-> h, infoPlayer[4]-> id
	 */
	public String[] getInfoPlayer() {
		return null;
	}
	/**
	 * 
	 * @param state position x,y and mass that is represented by a ball
	 */
	public void updatePlayer(String[] state) {
		player = state;
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("login.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			logInC = loader.getController();
			logInC.putClient(this);
			connectToServer();
			sessionOnFire = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public boolean isOnFire() {
		return sessionOnFire;
	}

	public void setSession(boolean onFire) {
		this.sessionOnFire = onFire;
	}
}
