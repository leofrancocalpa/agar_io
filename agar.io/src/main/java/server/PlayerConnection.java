package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class PlayerConnection extends Thread {

	ArrayList<Socket> clients;
	ArrayList<BufferedReader> readerC;
	ArrayList<PrintWriter> writerC;

	public PlayerConnection() {
		clients = new ArrayList<Socket>();
		readerC = new ArrayList<BufferedReader>();
		writerC = new ArrayList<PrintWriter>();
	}

	public void run() {
		try {
			while (true) {
				for (int i = 0; i < clients.size(); i++) {
					readerC.add(new BufferedReader(new InputStreamReader(clients.get(i).getInputStream())));
					String line = "";
					line = readerC.get(i).readLine();
					System.out.println(line);
					Server.receivePlayerInfo(line);
				}
				Server.setGamePositions();
				
			}

//			readerC.get(i).close();
		} catch (SocketException e1) {
			System.out.println("One user has left");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void sendGameData() {
//		writerC.
	}

	public void addSocket(Socket c) {
		clients.add(c);
		try {
			writerC.add(new PrintWriter(c.getOutputStream(), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
