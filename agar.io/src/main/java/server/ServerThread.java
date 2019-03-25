package server;

import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;

import javax.net.ssl.SSLSocket;

public class ServerThread extends Thread {

	SSLSocket client;
	BufferedReader readerHS;
	PrintWriter writerHS;

	public ServerThread(SSLSocket request) {
		super();

		client = request;
		try {
			writerHS = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {

		try {
			readerHS = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// while (!client.isClosed()) {
		String line = "";
		try {
			String[] supported = client.getSupportedCipherSuites();
			client.setEnabledCipherSuites(supported);
			line = readerHS.readLine();
			System.out.println(line);
			onInputLine(line);
		} catch (SocketException e1) {
			System.out.println("One user has left before to register");
		}catch (IOException e) {
			e.printStackTrace();
		}
		// }
		try {
			readerHS.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// The next code lines are going to get the name and host from the user

	}

	public void onInputLine(String line) {
		Server.registerNewUser(client.hashCode(), line);
	}

	public void writeClientSuccessSignIn(String line) {
		writerHS.println(line);
	}

}
