package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer extends Thread{
	
	public WebServer() {
		run();
	}
	
	@Override
	public void run() {
		System.out.println("Webserver Started");
		
		try {
			ServerSocket serverSocket =  new ServerSocket(80);
			while(true) 
			{
				System.out.println("Waiting for the client request");
				Socket remote = serverSocket.accept();
				System.out.println("Connection made");
				new Thread(new RequestHandler(remote)).start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		WebServer server = new WebServer();
	}

}
