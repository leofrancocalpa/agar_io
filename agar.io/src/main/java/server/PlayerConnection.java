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

	public static final String STARTING_MATCH = "Starting match. 3, 2, 1...";
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
			System.out.println(clients.size() + " clientes conectados");
			String[] playersInfo = new String[clients.size()];
			for (int i = 0; i < clients.size(); i++) {
				String line = readerC.get(i).readLine();
				System.out.println(line);
				playersInfo[i] = line;
			}
			Server.initializeGame(playersInfo);
			String stateFromGame = Server.getStateFromGame();
			for (int i = 0; i < clients.size(); i++) {
				writerC.get(i).println(stateFromGame);
				writerC.get(i).println(STARTING_MATCH + i);
			}
			while (true) {
				for (int i = 0; i < clients.size(); i++) {
					String line = readerC.get(i).readLine();
					playersInfo[i] = line;
				}
				Server.setGamePositions(playersInfo);
				stateFromGame = Server.getStateFromGame();
//				System.out.println(stateFromGame);
				for (int i = 0; i < clients.size(); i++) {
					writerC.get(i).println(stateFromGame);
				}
				sleep(36);
			}

//			readerC.get(i).close();
		} catch (SocketException e1) {
			System.out.println("One user has left");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int clientsCount() {
		return clients.size();
//		writerC.
	}

	public void addSocket(Socket c) {
		clients.add(c);
		try {
			writerC.add(new PrintWriter(c.getOutputStream(), true));
			readerC.add(new BufferedReader(new InputStreamReader(c.getInputStream())));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
