package SpaceInvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferStrategy;


public class GameOver extends JPanel {
//this class allows me to create a JPanel for the game over mode
    
	public void paint(Graphics g) {
		super.paintComponent(g);
		 setBackground(Color.black);
		//bold font for "Game Over!"
        Font gameOverFont = new Font("Arial", Font.BOLD, 40);
        g.setFont(gameOverFont);
        g.setColor(Color.white); // Set color for the text
        String gameOverText = "GAME OVER!";
        int textWidth = g.getFontMetrics().stringWidth(gameOverText);
        int x = 800 / 2 - textWidth / 2;
        int y = 600 / 2 - 20; // Adjust the vertical position

        g.drawString(gameOverText, x, y);

        //smaller font for "Press any key to continue"
        Font restartFont = new Font("Arial", Font.PLAIN, 20);
        g.setFont(restartFont);
        String restartText = "Press any key to continue.";
        textWidth = g.getFontMetrics().stringWidth(restartText);
        x = 800 / 2 - textWidth / 2;
        y += 44; //place underneath
        g.drawString(restartText, x, y);
	}
}
