import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.*;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class EndScreen extends JFrame{

	//label showing who wins
	private JLabel winnerLabel = new JLabel();

	//constructor
	public EndScreen (String winner) {
		
		//formats the frame
		setLayout(null);
		setVisible(true);
		setBounds(0,0, 1000, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//creates new font
		Font titleFont = new Font("Serif", Font.ITALIC | Font.BOLD, 100);

		//formats the label stating who wins
		winnerLabel.setForeground(Color.BLUE);
		winnerLabel.setFont(titleFont);
		winnerLabel.setBounds(100, 300, 900, 300);
		winnerLabel.setText(winner + " won!");
		add(winnerLabel);

//		playMusic("./Endingaudionew.wav");

		
	}
	
	
	// Method for playing music
	public static void playMusic(String filepath) {
		InputStream music;
		try {
			music = new FileInputStream(new File(filepath));
			AudioStream audios = new AudioStream(music);
			AudioPlayer.player.start(audios);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "1 error");
		}
		InputStream effect;
		try {
			effect = new FileInputStream(new File(filepath));
			AudioStream poppo = new AudioStream(effect);
			AudioPlayer.player.start(poppo);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "2 error");
		}
	}

}
