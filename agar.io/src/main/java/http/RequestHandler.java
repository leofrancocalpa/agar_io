package http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;


public class RequestHandler implements Runnable{
	
	private final Socket socket;
	private static PersistenceHandler persistanceHandler;
	
	public RequestHandler(Socket socket) {
		super();
		this.socket = socket;
//		persistanceHandler= PersistenceHandler.getPersistanceHandler();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("RequestHandler Started for " + this.socket);
		persistanceHandler= PersistenceHandler.getPersistanceHandler();
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
			System.out.println(headerLine);
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
				if(httpQueryString.equals("/")) {
					
					 responseBuffer =  loadPage("signin.html");
					 sendResponse(socket, 200, responseBuffer.toString());
				}
				
				else if(httpQueryString.contains("/?username")) {
					
					String query = httpQueryString;
					query = query.replace("/", "");
					query= query.replace("?", "");
					String[] data = query.split("&");
					data[0]=data[0].substring(9);
					data[1]=data[1].substring(9);
					System.out.println(data[0]+ " "+data[1]+" data");
					if(persistanceHandler.conatainsUser(data[0], data[1])) {
						responseBuffer =  loadPage("puntajes.html");

						responseBuffer.append(createTable().toString());
						sendResponse(socket, 200, responseBuffer.toString());
					}
					else {
						responseBuffer =  loadPage("signin.html");
						
						sendResponse(socket, 200, responseBuffer.toString());
					}
				}
				

			}
			else
			{
				System.out.println("The HTTP method is not recognized");
				sendResponse(socket, 405, "Method Not Allowed");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}		
	}
	
	public StringBuilder createTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table><thead><tr class=\"table100-head\"><th class=\"column1\">Date</th><th class=\"column2\">User</th><th class=\"column3\">Score</th><th class=\"column4\">Winner</th></tr></thead><tbody>");
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append("	<div class=\"limiter\"><div class=\"container-table100\"><SELECT NAME=\"selCombo\" id=\"selectHome\" SIZE=1 onChange=\"javascript:alert(selectedIndex);\">");
		
		HashMap<String, String> matchs = persistanceHandler.getMatchs();
		Iterator<Entry<String, String>> iterator = matchs.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, String> map = (Entry<String, String>) iterator.next();
			sb2.append("<OPTION id=\"optioHome\" VALUE=\"link pagina 1\">"+map.getKey()+"</OPTION>");
			String[] values = map.getValue().split("-");
			
			for(int i=0; i<values.length; i++) {
				sb.append("<tr>");
				String[] data = values[i].split("/");
				sb.append("<td class=\"column1\">"+map.getKey()+"</td>");
				sb.append("<td class=\"column1\">"+data[0]+"</td>");
				sb.append("<td class=\"column1\">"+data[1]+"</td>");
				sb.append("<td class=\"column1\">"+data[2]+"</td>");
				sb.append("</tr>");
			}
			
						
		}
		sb2.append(" </SELECT></div>");
		sb.append("</tbody></table></div></div></body>");
		sb2.append(sb.toString());
		return sb2;
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
			out.flush();
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
			br.close();	

		}catch (Exception e) {
			System.out.println("Error al cargar html signin: "+e.getMessage());
			
		}
		return salida;
		
	}

}
