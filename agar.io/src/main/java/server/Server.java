package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.Match;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import settings.Port;
import settings.ServerMessage;

public class Server extends Application {

	public static final String KEYSTORE_LOCATION = "C:/Users/99031510240/alv";
	public static final String KEYSTORE_PASSWORD = "123456";

	private static ServerStController serverSt;
	private static HashMap<Integer, SSLConnection> threads = new HashMap<Integer, SSLConnection>();
//	private static ThreadGroup threadsGroup = new ThreadGroup("threadsGroup");
	private static HashMap<String, User> users = new HashMap<String, User>();
	private static Match match = new Match();
	private SSLServerSocket logSocket;
	private ServerSocket gameSocket;
	private InetAddress ip;
	
	public void startSSL() {
		
		System.setProperty("javax.net.ssl.trustStore", "src/main/resources/server/serverTrustedCerts.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
		System.setProperty("javax.net.ssl.keyStore", "src/main/resources/server/serverkey.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		final SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		
		Thread condenser = new Thread(new Runnable() {

			public void run() {
				try {
					SSLServerSocket server = (SSLServerSocket) ssf.createServerSocket(Port.LOGIN.getPort());
					logSocket = server;
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
					System.out.println("Se cierra el login para Clientes");
				}

			}
		});
		condenser.start();
	}

	public void startMatchConnection() {
		ServerSocketFactory ssf = ServerSocketFactory.getDefault();
		PlayerConnection playerC = new PlayerConnection();
		StreamingService stream = new StreamingService();
			try {
				ServerSocket server = ssf.createServerSocket(Port.GAME.getPort());
				gameSocket = server;
				while (playerC.clientsCount() < 1) {
				Socket c = server.accept();
				playerC.addSocket(c);
				}
			} catch (IOException e) {
				System.out.println("Tiempo agotado");
//				e.printStackTrace();
			}
			if(playerC.clientsCount()>=1) {
				TransmitionAudio ta = new TransmitionAudio();
				ta.start();
				playerC.start();
				stream.start();
			}
			
			else
				JOptionPane.showMessageDialog(new JFrame(), "Cantidad de jugadores insuficiente");
	}

	public static void registerNewUser(int hashC, String[] info) {
		String response = signIn(info[0], info[1], info[2]);
		threads.get(hashC).writeSessionStatus(response);
	}

	private static String signIn(String email, String nickname, String password) {
		User user = new User(email, nickname, password);
		users.put(email, user);
		return ServerMessage.REGISTER_SUCCESS.getMessage();
	}

	public static void playGameOf(int hashC, String[] userAndPass) {
		User toPlay = users.get(userAndPass[0]);
		if (toPlay != null) {
			if (toPlay.getPassword().equals(userAndPass[1])) {
				toPlay.setInGame(true);
				serverSt.clientJoined(toPlay);
			}
		}
		if (toPlay == null || !toPlay.isInGame())
			threads.get(hashC).writeSessionStatus(ServerMessage.SESSION_FAILED.getMessage());
		else {
			if(!match.isInGame())
			threads.get(hashC).writeSessionStatus("Hello "+ toPlay.getNickname() +" " +ServerMessage.WAITING_MATCH.getMessage());
			else threads.get(hashC).writeSessionStatus("Hello "+ toPlay.getNickname() +" " +ServerMessage.JOIN_SPECTATOR.getMessage());
		}
	}

	public static String getStateFromGame() {
		StringBuilder sb = new StringBuilder();
		String[] players = match.getPlayersFromGame();
		for (int i = 0; i < players.length; i++) {
			sb.append(players[i] + " ");
		}
		sb.append("/");
		String[] food = match.getFoodFromGame();
		for (int i = 0; i < food.length; i++) {
			sb.append(food[i] + " ");
		}
		sb.append("/");
		String[] scores = match.getScores();
		for (int i = 0; i < scores.length; i++) {
			sb.append(scores[i] + " ");
		}
		return sb.toString();
	}

	public static void setGamePositions(String[] playersInfo) {
		match.updateGame(playersInfo);
	}
	
	@Override
	public void start(final Stage primaryStage) {
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

	public static void initializeGame(String[] playersInfo) {
		ArrayList<String> players = new ArrayList<String>();
		for (int i = 0; i < playersInfo.length; i++) {
			players.add(playersInfo[i]);
		}
		match.setInGame(true);
		match.initialize(players);
	}

	public void timeOut() {
		try {
			gameSocket.close();
			logSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isReceiving() {
		if(logSocket.isClosed())
		return false;
		return true;
	}

	public boolean isReady() {
		return logSocket!=null && !logSocket.isClosed();
	}

	public boolean isInGame() {
		return match.isInGame();
	}
}
