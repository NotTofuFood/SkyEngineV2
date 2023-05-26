package audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
	
	public AudioManager() {
		
	}
	
	public static synchronized void playAudio(String audio) {
		try {
			Clip audio_player = AudioSystem.getClip();
			AudioInputStream is;
			try {
				is = AudioSystem.getAudioInputStream(new File(audio));
				audio_player.open(is);
			} catch (UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			audio_player.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
}
