package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import server.ClientListener;

public class Client extends Thread {
	public static final String TRUSTTORE_LOCATION = "C:/Users/99031510240/alv";
	public static PrintWriter writerC;

	@Override
	public void run() {
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
						while (true) {
							Alert dialog;
							String line = readerC.readLine();
							if(line.equals(ClientListener.REGISTER_SUCCESS)) {

							} else if(line.equals(ClientListener.SESSION_FAILED)){
								dialog = new Alert(AlertType.ERROR);
								dialog.setTitle("Session failed");
								dialog.setContentText(ClientListener.SESSION_FAILED);
								dialog.show();
							}
						}
					} catch (IOException e) {
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
}

	public void sendServer(String parameter) {
					writerC.println(parameter);
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
