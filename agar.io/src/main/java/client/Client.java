package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client {
	public static final String TRUSTTORE_LOCATION = "C:/Users/99031510240/alv";
	public static PrintWriter writerC;

	public static void main(String[] args) throws Exception {
		final SSLSocket client;
		System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		final Scanner scanner = new Scanner(System.in);

		// Inicia el hilo que lee las solicitudes del cliente por consola

		Thread tScanner = new Thread(new Runnable() {
//			lee las entradas del cliente: usuario y contraseña
			public void run() {
				while(true) {
					String line = scanner.nextLine();
					writerC.println(line);
				}
			}
		});
		
		tScanner.start();
		try {
			client = (SSLSocket) sf.createSocket("localhost", 8030);
			String[] supported = client.getSupportedCipherSuites();
			client.setEnabledCipherSuites(supported);
			Writer out = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
			// INicia hilo que lee desde el servidor

			Thread tServer = new Thread(new Runnable() {

				public void run() {
					BufferedReader readerC;
					try {
						readerC = new BufferedReader(new InputStreamReader(client.getInputStream()));
//						while (true) {
							String line = readerC.readLine();
							System.out.println(line);
//						}
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
}
