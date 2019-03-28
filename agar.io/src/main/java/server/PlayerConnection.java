package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerConnection extends Thread{

	Socket client;
	BufferedReader readerC;
	PrintWriter writerC;
	
	public PlayerConnection (Socket req) {
		client = req;
		try {
			writerC = new PrintWriter(client.getOutputStream(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run () {
		
	}
}
