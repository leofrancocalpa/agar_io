package client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class ReceptionAudio extends Thread{
	
	static AudioInputStream audioInputStream;
	static SourceDataLine sourceDataLine;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		initiateAudio();

	}
	
	/*
	*this method allows the client to get the audio throug UDP conection 
	*initializing the transfer of the narration
	*/
	private static void initiateAudio() {
		try {
			MulticastSocket socket = new MulticastSocket(9786);
			InetAddress group = InetAddress.getByName("230.0.0.0");
			socket.joinGroup(group);
			byte[] audioBuffer = new byte[10000];
			// ...
			while (true) {
				DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length);
				socket.receive(packet);
				// ...

				try {
					byte audioData[] = packet.getData();
					InputStream byteInputStream = new ByteArrayInputStream(audioData);
					AudioFormat audioFormat = getAudioFormat();
					audioInputStream = new AudioInputStream(byteInputStream, audioFormat,
							audioData.length / audioFormat.getFrameSize());
					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

					sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					sourceDataLine.open(audioFormat);
					sourceDataLine.start();
					playAudio();
				} catch (Exception e) {
					// Handle exceptions
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	*this method return the audioFormat 
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
	*this method play the audio on the client's side
	*/
	private static void playAudio() {
		byte[] buffer = new byte[10000];
		try {
			int count;
			while ((count = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
				if (count > 0) {
					sourceDataLine.write(buffer, 0, count);
				}
			}
		} catch (Exception e) {
			// Handle exceptions
		}
	}

}
