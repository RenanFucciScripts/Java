import java.io.FileInputStream;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Sound {


	public static void playSound(){
		try{
			//** add this into your application code as appropriate
			// Open an input stream  to the audio file.
			InputStream in = new FileInputStream("C:/Users/Renan Fucci/Music/Acabou.wav");

			// Create an AudioStream object from the input stream.
			AudioStream as = new AudioStream(in);         

			// Use the static class member "player" from class AudioPlayer to play
			// clip.
			AudioPlayer.player.start(as);            
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}

