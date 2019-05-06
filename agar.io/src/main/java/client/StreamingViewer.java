package client;

import java.net.*;

import settings.Port;

import java.io.*;

public class StreamingViewer extends Thread {
	 protected MulticastSocket socket = null;
	 protected String gameState;
	    protected byte[] buf = new byte[256];
	 
	    public void run() {
	        try {
				socket = new MulticastSocket(Port.STREAM.getPort());
	        InetAddress group = InetAddress.getByName("230.0.0.0");
	        socket.joinGroup(group);
	        while (true) {
	            DatagramPacket packet = new DatagramPacket(buf, buf.length);
	            socket.receive(packet);
	            String received = new String(
	              packet.getData(), 0, packet.getLength());
	            if ("end".equals(received)) {
	                break;
	            } else {
	            	gameState= received;
	            }
	        }
	        socket.leaveGroup(group);
	        socket.close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	    }
}