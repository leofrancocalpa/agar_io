package server;

import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;

import javax.net.ssl.SSLSocket;

public class ClientListener extends Thread {

	public static final String SESSION_FAILED = "Password or email wrong";
	public static final String REGISTER_SUCCESS = "User created successfully";

	SSLSocket client;
	BufferedReader readerHS;
	PrintWriter writerHS;

	public ClientListener(SSLSocket request) {
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
		while (!client.isClosed()) {
			String line = "";
			try {
				String[] supported = client.getSupportedCipherSuites();
				client.setEnabledCipherSuites(supported);
				line = readerHS.readLine();
				System.out.println(line);
				onInputLine(line);
			} catch (SocketException e1) {
				System.out.println("One user has left before to register");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			readerHS.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onInputLine(String line) {
		String[] arregloS = line.split(" ");
		int hash = client.hashCode();
		if (arregloS.length == 3)
			Server.registerNewUser(hash, arregloS);
		else if (arregloS.length == 2)
			Server.playGameOf(hash, arregloS);
		else {
			Server.sendStateFromGame(hash, arregloS);
		}
	}

	public void writeSessionStatus(String line) {
		writerHS.println(line);
	}

}
