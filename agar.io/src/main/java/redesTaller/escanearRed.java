package redesTaller;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

public class escanearRed {

	static ArrayList<InetAddress> address = new ArrayList<InetAddress>();

	public static void main(String[] args) {
		checkHosts("192.168.17");
//		try {
//			address.add(InetAddress.getLocalHost());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
		for (int i = 0; i < address.size(); i++) {
			try {
				System.out.println("Host Address: " + address.get(i).getHostAddress());
				System.out.println("Host Name: " + address.get(i).getHostName());
				System.out.println("CanonicalHostName: " + address.get(i).getCanonicalHostName());
				System.out.println("Address: " + address.get(i).getAddress());
				System.out.println("LocalHost: " + address.get(i).getLocalHost());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println("LoopbackAddress: " + address.get(i).getLoopbackAddress());
		}
		escanearPuertos();
	}

	public static void checkHosts(String subnet) {
		int timeout = 1000;
		for (int i = 1; i < 255; i++) {
			try {
				String host = subnet + "." + i;
				if (InetAddress.getByName(host).isReachable(timeout)) {
					System.out.println(host + " Encontrado");
					address.add(InetAddress.getByName(host));
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void escanearPuertos(){
	      ExecutorService executor = Executors.newCachedThreadPool();
	      int ini=1,fin=100;
//	      65500
//	      while(fin<=100){
	      executor.execute(new Run(ini,fin, address));
//	      fin+=100;
//	      ini+=100;
//	      }
//	      executor.execute(new Run(65501,65535));
	 
	        } 
	 
	    }
	 
	class Run implements Runnable{
	    int puerto = 0;
	    int fin;
	    ArrayList<InetAddress> address;
	    public Run(int port,int fin,ArrayList<InetAddress> address){
	    puerto = port;
	    this.address = address;
	    this.fin=fin;
	    }
	    
	    public void run(){
	    	for (int i = 0; i < address.size(); i++) {
	    		String ip = address.get(i).getHostAddress();
	    		System.out.println("\n" + ip + ":");
	    		Scanner Scanner = new Scanner(puerto,fin, ip);
			}
	    }
	}
	 
	 
	class Scanner {
	 
	public Scanner(int puerto,int fin, String ip){
	 
	Socket dame;
//	String ip="localhost";
	 
	        for(int port =puerto;port<=fin;port++){
	         try{
	         dame = new Socket(ip,port);
	 
	         System.out.println("Puerto "+port+" Abierto");
	         dame.close();
	 
	         }
	         catch(Exception ex){}
	 
	      } 
	   }
}
