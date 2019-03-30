package server;

import java.io.IOException;
import java.lang.Thread.State;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application {

	public static final String KEYSTORE_LOCATION = "C:/Users/99031510240/alv";
	public static final String KEYSTORE_PASSWORD = "123456";

	private ServerStController serverSt;
	private static HashMap<Integer, SSLConnection> threads = new HashMap<Integer, SSLConnection>();
//	private static ThreadGroup threadsGroup = new ThreadGroup("threadsGroup");
	private static HashMap<String, User> users = new HashMap<String, User>();

	public void startSSL() {
		final SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
		Thread condenser = new Thread(new Runnable() {

			public void run() {
				try {
					SSLServerSocket server = (SSLServerSocket) ssf.createServerSocket(8030);
					while (true) {
//						for (Integer tId : threads.keySet()) {
//							if(!threads.get(tId).isAlive())
//								threads.remove(tId);
//						}
						SSLSocket c = (SSLSocket) server.accept();
						SSLConnection clientListener = new SSLConnection(c);
						int address = c.hashCode();
						threads.put(address, clientListener);
//						Thread th = new Thread(threadsGroup, clientListener);
//						th.start();
						clientListener.start();
//						System.out.println(threads.size());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		condenser.start();
	}

	public void startMatchConnection() {
		ServerSocketFactory ssf = ServerSocketFactory.getDefault();
		PlayerConnection clientListener = new PlayerConnection();
			try {
				while (true) {
				ServerSocket server = ssf.createServerSocket(8040);
				Socket c = server.accept();
				clientListener.addSocket(c);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			clientListener.start();
	}

	public static void registerNewUser(int hashC, String[] info) {
		String response = signIn(info[0], info[1], info[2]);
		threads.get(hashC).writeSessionStatus(response);
	}

	private static String signIn(String email, String nickname, String password) {
		User user = new User(email, nickname, password);
		users.put(email, user);
		return SSLConnection.REGISTER_SUCCESS;
	}

	public static void playGameOf(int hashC, String[] userAndPass) {
		User toPlay = users.get(userAndPass[0]);
		if (toPlay != null) {
			if (toPlay.getPassword().equals(userAndPass[1]))
				toPlay.setInGame(true);
		}
		if (toPlay == null || !toPlay.isInGame())
			threads.get(hashC).writeSessionStatus(SSLConnection.SESSION_FAILED);
		else
			threads.get(hashC).writeSessionStatus(SSLConnection.STARTING_MATCH);
	}

	public static void sendStateFromGame(int hash, String[] arregloS) {
		// TODO Auto-generated method stub

	}

	public static void receivePlayerInfo(String line) {
		String[] playerData = line.split(" ");
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("serverSt.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			serverSt = loader.getController();
			serverSt.putServer(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void setGamePositions() {
		
	}
}
