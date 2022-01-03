import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public class GameMain {
	
	public static void main(String[] args) {
		int appWidth = 800;
		int appHeight = 600;
		
		SoundManager sm = new SoundManager();
    	try {
			sm.addClip("brick_broken.wav");
			sm.addClip("Claves.wav");
			sm.addClip("sadGameOver.wav");
			sm.addClip("happyGameOver.wav");
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ConfigurationDialog configUI = new ConfigurationDialog(400, 350);
		GameBoard gamePlay = new GameBoard(sm, appWidth, appHeight, configUI);
		
		configUI.registerSaveCallback(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {
				gamePlay.reset();
			}
		});
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		// int displayWidth = gd.getDisplayMode().getWidth();
		int displayHeight = gd.getDisplayMode().getHeight();
		
		int startingX = 400; // (displayWidth - 400 - appWidth)/2;
		int startingY = (displayHeight - appHeight)/2;
		
		JFrame frame = new JFrame("Brick Breaker");
		frame.setBounds(startingX, startingY, appWidth, appHeight);		
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(gamePlay);
		frame.setVisible(true);
		
		configUI.setVisible(true);
	}
}
