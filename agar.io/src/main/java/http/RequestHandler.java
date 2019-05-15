package http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class RequestHandler implements Runnable{
	
	private final Socket socket;
	
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("RequestHandler Started for " + this.socket);
		handleRequest(socket);
		
		
	}
	
	/*
	*this method handle a request make it in the web page 
	*/
	public void handleRequest(Socket socket)
	{
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String headerLine = in.readLine();
			// A tokenizer is a process that splits text into a series of tokens
			StringTokenizer tokenizer =  new StringTokenizer(headerLine);
			//The nextToken method will return the next available token
			String httpMethod = tokenizer.nextToken();
			// The next code sequence handles the GET method. A message is displayed on the
			// server side to indicate that a GET method is being processed
			if(httpMethod.equals("GET"))
			{
				System.out.println("Get method processed");
				String httpQueryString = tokenizer.nextToken();
				StringBuilder responseBuffer=new StringBuilder();
				System.out.println(httpQueryString);
				if(httpQueryString.equals("/")) {
					
					 responseBuffer =  loadPage("signin.html");
					 sendResponse(socket, 200, responseBuffer.toString());
				}
				
				else if(httpQueryString.equals("/puntajes")) {
					
					responseBuffer =  loadPage("puntajes.html");
					sendResponse(socket, 200, responseBuffer.toString());
				}
				

			}
			
			else if(httpMethod.equals("POST"))
			{
				while(!headerLine.equals("")) {
					headerLine= in.readLine();
				}
				
				headerLine=in.readLine();
				String[] data= headerLine.split("=");
				String cedula= data[1];
				StringBuilder responseBuffer=new StringBuilder();
				responseBuffer= loadPage(cedula);
				sendResponse(socket, 200, responseBuffer.toString());
				
				
			
			}
			else
			{
				System.out.println("The HTTP method is not recognized");
				sendResponse(socket, 405, "Method Not Allowed");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/*
	*this method send a response to the web page
	*/
	public void sendResponse(Socket socket, int statusCode, String responseString)
	{
		String statusLine;
		String serverHeader = "Server: WebServer\r\n";
		String contentTypeHeader = "Content-Type: text/html\r\n";
		System.out.println(responseString);
		try {
			DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
			if (statusCode == 200) 
			{
				statusLine = "HTTP/1.0 200 OK" + "\r\n";
				String contentLengthHeader = "Content-Length: "
				+ responseString.length() + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes(serverHeader);
				out.writeBytes(contentTypeHeader);
				out.writeBytes(contentLengthHeader);
				out.writeBytes("\r\n");
				out.writeBytes(responseString);
				} 
			else if (statusCode == 405) 
			{
				statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			} 
			else 
			{
				statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public StringBuilder loadPage(String page) {
		String route = "src/main/resources/server/html/"+page;
		StringBuilder salida = new StringBuilder();
		
		try {
			//SE LEE EL ARCHIVO INDEX.HTML
			FileReader file = new FileReader(new File(route));
			BufferedReader br = new BufferedReader(file);

			while (br.ready()) {
				salida.append(br.readLine());
			}
				

		}catch (Exception e) {
			System.out.println("Error al cargar html signin: "+e.getMessage());
			
		}
		return salida;
		
	}

}
