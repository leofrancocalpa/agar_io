package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.Ball;
import game.Match;
import http.PersistenceHandler;
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
	private static HashMap<Integer, SSLConnection> sslConnections = new HashMap<Integer, SSLConnection>();
	private static HashMap<Integer, ChatService> chatConnections = new HashMap<Integer, ChatService>();
	// private static ThreadGroup threadsGroup = new ThreadGroup("threadsGroup");
	private static HashMap<String, User> users = new HashMap<String, User>();
	private static Match match = new Match();
	private static PersistenceHandler persis = PersistenceHandler.getPersistanceHandler();
	private SSLServerSocket logSocket;
	private ServerSocket gameSocket;
	private ServerSocket chatSocket;

	public void startSSL() {

		System.setProperty("javax.net.ssl.trustStore", "src/main/resources/server/serverTrustedCerts.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
		System.setProperty("javax.net.ssl.keyStore", "src/main/resources/server/serverkey.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		final SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

		Thread condenser = new Thread(new Runnable() {

			public void run() {
				try {
					loadUsersDB();
					SSLServerSocket server = (SSLServerSocket) ssf.createServerSocket(Port.LOGIN.getPort());
					logSocket = server;
					while (true) {
						SSLSocket c = (SSLSocket) server.accept();
						SSLConnection clientListener = new SSLConnection(c);
						int address = c.hashCode();
						sslConnections.put(address, clientListener);
						clientListener.start();
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
			startChatConnection();
			while (playerC.clientsCount() < 2) {
				Socket c = server.accept();
				playerC.addSocket(c);
			}
		} catch (IOException e) {
			System.out.println("Tiempo agotado");
//				e.printStackTrace();
		}
		if (playerC.clientsCount() >= 1) {
			TransmitionAudio ta = new TransmitionAudio();
			ta.start();
			playerC.start();
			stream.start();
		}

		else
			JOptionPane.showMessageDialog(new JFrame(), "Cantidad de jugadores insuficiente");
	}

	private void startChatConnection() {
		Thread chatWaiter = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					ServerSocketFactory ssf = ServerSocketFactory.getDefault();
					ServerSocket server = ssf.createServerSocket(Port.CHAT.getPort());
					chatSocket = server;
					while (true) {
						Socket c = server.accept();
						ChatService cs = new ChatService(c);
						chatConnections.put(c.hashCode(), cs);
						cs.start();
					}
				} catch (IOException e) {
					System.out.println("Se cierra el chat para clientes");
				}
				
			}
		});
		chatWaiter.start();
	}

	public static void registerNewUser(int hashC, String[] info) {
		String response = signIn(info[0], info[1], info[2]);
		sslConnections.get(hashC).writeSessionStatus(response);
	}

	private static String signIn(String email, String nickname, String password) {
		User user = new User(email, nickname, password);
		users.put(email, user);
		persis.addUser(nickname, password, email);
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
			sslConnections.get(hashC).writeSessionStatus(ServerMessage.SESSION_FAILED.getMessage());
		else {
			if (!match.isInGame())
				sslConnections.get(hashC).writeSessionStatus(
						"Hello " + toPlay.getNickname() + " " + ServerMessage.WAITING_MATCH.getMessage());
			else {
				sslConnections.get(hashC).writeSessionStatus(
						"Hello " + toPlay.getNickname() + " " + ServerMessage.JOIN_SPECTATOR.getMessage());
			}
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
	
	public void loadUsersDB() {
		HashMap<String, String> usersDB = persis.getUsers();
		Iterator<Entry<String, String>> iterator = usersDB.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, String> map = (Entry<String, String>) iterator.next();
			String[] datos = map.getValue().split("/");
			User user = new User(datos[1],map.getKey(), datos[0]);
			users.put(map.getKey(), user);
		}
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
			chatSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isReceiving() {
		if (logSocket.isClosed())
			return false;
		return true;
	}

	public boolean isReady() {
		return logSocket != null && isReceiving();
	}

	public boolean isInGame() {
		return match.isInGame();
	}

	public static void sendToChat(String clientText) {
		for (ChatService chat : chatConnections.values()) {
			chat.sendToClient(clientText);			
		}
	}
}
