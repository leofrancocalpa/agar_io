package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import javafx.concurrent.Task;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client extends Task {
	public static final String TRUSTTORE_LOCATION = "C:/Users/99031510240/alv";
	public static PrintWriter writerC;
	public static LoginController logInC;

	

	public void sendServer(String parameter) {
		writerC.println(parameter);
	}

	public Object getlogInC() {
		return logInC;
	}

	public void putLogInC(LoginController loginController) {
		logInC = loginController;

	}

	@Override
	protected Object call() throws Exception {
		final SSLSocket client;
		System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();

		try {
			client = (SSLSocket) sf.createSocket("localhost", 8030);
			String[] supported = client.getSupportedCipherSuites();
			client.setEnabledCipherSuites(supported);
			// INicia hilo que lee desde el servidor

			Thread tServer = new Thread(new Runnable() {

				public void run() {
					BufferedReader readerC;
					try {
						readerC = new BufferedReader(new InputStreamReader(client.getInputStream()));
						while (client.isConnected()) {
							final String line = readerC.readLine();
							logInC.showMessage(line);
						}
					} catch (IOException e) {
							try {
								client.shutdownInput();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						e.printStackTrace();
					}

				}
			});

			tServer.start();
			writerC = new PrintWriter(client.getOutputStream(), true);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
		
	}
}
