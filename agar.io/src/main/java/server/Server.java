package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import client.User;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server {
	
	public static final String KEYSTORE_LOCATION = "C:/Users/99031510240/alv";
	public static final String KEYSTORE_PASSWORD = "123456";
	
	private static HashMap<Integer, ServerThread> threads = new HashMap<Integer, ServerThread>();
	private static ThreadGroup threadsGroup = new ThreadGroup("threadsGroup");
	private static HashMap<String, User> users = new HashMap<String, User>();
	
	public static void main(String[] args) {
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
		try {
			SSLServerSocket server = (SSLServerSocket) ssf.createServerSocket(8030);
			while (true) {
				SSLSocket c = (SSLSocket) server.accept();
				ServerThread threadd = new ServerThread(c);
				System.out.println(c.hashCode());
				int address = c.hashCode();
				threads.put(address, threadd);
				Thread th = new Thread(threadsGroup, threadd);
				th.start();
				System.out.println(threads.size());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void registerNewUser(int address, String line) {
		// 7 4
		String[] info = line.split(" ");
			String response = signIn(info[0], info[1], info[2]);
			threads.get(address).writeClientSuccessSignIn(response);
	}

	private static String signIn(String email, String nickname, String password) {
		User user = new User(email, nickname, password);
		users.put(email, user);
		return "User created successfully";
	}
}
