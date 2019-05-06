package client;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioUDPClient  extends Thread{
	
	public AudioUDPClient() {
		
	}
	
   // public static void main(String[] args) throws Exception {
	
	public void run() {
        
           
            // play soundfile from server
            System.out.println("Client: reading from 127.0.0.1:6666");
            try (Socket socket = new Socket("Despair", 6666)) {
            	while(true) {
                if (socket.isConnected()) {
                    InputStream in = new BufferedInputStream(socket.getInputStream());
                    play(in);
                }
                }
            }catch (Exception e) {
            	e.printStackTrace();
            }
        
            
        System.out.println("Client: end");
    }


    private  synchronized void play(final InputStream in) throws Exception {
        AudioInputStream ais = AudioSystem.getAudioInputStream(in);
        try (Clip clip = AudioSystem.getClip()) {
            clip.open(ais);
            clip.start();
            Thread.sleep(100); // given clip.drain a chance to start
            clip.drain();
        }
    }
}


