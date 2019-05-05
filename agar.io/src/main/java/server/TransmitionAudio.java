package server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class TransmitionAudio extends Thread{
	
	private static final byte audioBuffer[] = new byte[10000];
	private static TargetDataLine targetDataLine;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		setupAudio();
		broadcastAudio();

	}
	
	/*
	*This method return the Audioformat of the narration
	*/
	
	private static AudioFormat getAudioFormat() {
		float sampleRate = 16000F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = false;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	
	/*
	*this method allows the transmission of the audio through the adress 230.0.0.0  creating a adress group
	*/
	private static void broadcastAudio() {
		try {
			MulticastSocket socket = new MulticastSocket();
			InetAddress group = InetAddress.getByName("230.0.0.0");
//			socket.joinGroup(group);
			//InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
			// ...
			while (true) {
				int count = targetDataLine.read(audioBuffer, 0, audioBuffer.length);
				if (count > 0) {
					DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length, group, 9786);
					socket.send(packet);
				}
			}

		} catch (Exception ex) {
			// Handle exceptions
			ex.printStackTrace();
			System.out.println(ex.getMessage()+"\n");
		}
	}


	/*
	*this method set up the audio
	*/
	private static void setupAudio() {
		try {
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage()+"\n");
		}
	}

}
